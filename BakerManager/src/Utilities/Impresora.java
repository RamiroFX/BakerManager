/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

/**
 *
 * @author Ramiro
 */
import DB.DB_Pedido;
import DB.DB_manager;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
import Entities.M_rol_usuario;
import MenuPrincipal.DatosUsuario;
import java.awt.Desktop;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSize;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class Impresora {

    private final static String TICKET_CABECERA = "**** Panaderia Le Croissant ****\n"
            + "R.U.C.: 3777437-9\n"
            + "Direccion: Av. Rodriguez de Francia esq.Brasil 1199\n"
            + "Telefono: (021) 227247\n"
            + "_________________________________\n";
    private final static String TICKET_PIE = "_________________________________\n"
            + "******Gracias por su compra******\n"
            + "\n\n\n\n\n\n\n\n\n\n";
    private final static String TICKET_PIE_SIN_GRACIAS = "_________________________________\n"
            + "\n\n\n\n\n\n\n\n\n\n";

    public static void imprimirCocina(String textoAImprimir) {
        agregarLogoATicket(textoAImprimir);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null); //nos da el array de los servicios de impresion

        byte[] bytes = textoAImprimir.getBytes();

//Especificamos el tipo de dato a imprimir
//Tipo: bytes; Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        Doc doc = new SimpleDoc(bytes, flavor, null);
//Creamos un trabajo de impresión
        DocPrintJob job = null;
        if (services.length > 0) {
            for (int i = 0; i < services.length; i++) {

                if (services[i].getName().equals("cocina")) {//aqui escribimos/elegimos la impresora por la que queremos imprimir
                    //manejar error en caso de que no esté conectada
                    //Desktop.getDesktop().print(null);//para imprimir un archivo ya existente  
                    job = services[i].createPrintJob();// System.out.println(i+": "+services[i].getName());

                } else {
                    System.out.println("No se encontró la impresora cocina");
                }
            }

            PrinterJob pj = PrinterJob.getPrinterJob();

            PageFormat mPageFormat = pj.defaultPage();
            //pj.setPrintable(cp, mPageFormat);
            if (pj.printDialog()) {
                try {
                    //PrintService service = pj.getPrintService();//PrintServiceLookup.lookupDefaultPrintService();
                    pj.print();
                    //DocPrintJob pjb;
                    // job = service.createPrintJob();
                } catch (PrinterException ex) {
                    Logger.getLogger(Impresora.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

//Imprimimos dentro de un try obligatoriamente
//        try {
//            if (job != null) {
//                job.print(doc, null);
//            }
//        } catch (PrintException ex) {
//            System.out.println(ex);
//        }
    }

    public static void imprimirCaja(String textoAImprimir) {
        agregarLogoATicket(textoAImprimir);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null); //nos da el array de los servicios de impresion

        byte[] bytes = textoAImprimir.getBytes();

//Especificamos el tipo de dato a imprimir
//Tipo: bytes; Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        Doc doc = new SimpleDoc(bytes, flavor, null);
//Creamos un trabajo de impresión
        DocPrintJob job = null;
        if (services.length > 0) {
//            for (int i = 0; i < services.length; i++) {
//                if (services[i].getName().equals("caja")) {//aqui escribimos/elegimos la impresora por la que queremos imprimir
//                    job = services[i].createPrintJob();// System.out.println(i+": "+services[i].getName());
//                }
//            }
            for (PrintService service : services) {
                if (service.getName().equals("caja")) {
                    //aqui escribimos/elegimos la impresora por la que queremos imprimir
                    job = service.createPrintJob(); // System.out.println(i+": "+services[i].getName());
                }
            }
        }

//Imprimimos dentro de un try obligatoriamente
        try {
            job.print(doc, null);
        } catch (PrintException ex) {
            System.out.println(ex);
        }

    }

    public static void imprimirGenerico(String contentTicket) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        byte[] bytes = contentTicket.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job = service.createPrintJob();
        try {
            if (job != null) {
                job.print(doc, null);
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrintException ex) {
            System.out.println(ex);
        }
    }

    //TODO arreglo de impresoras de caja
    //TODO arreglo de impresoras de cocina
    public static void main(String[] args) {
        try {
            //ServicioDeImpresion impresion = new ServicioDeImpresion();
            //1552 vico c
            DB_manager.conectarBD("postgres", "postgres");
        } catch (SQLException ex) {
            Logger.getLogger(Impresora.class.getName()).log(Level.SEVERE, null, ex);
        }
        M_pedido pedido = DB_Pedido.obtenerPedido(1552);
        Impresora.imprimirPedido(DatosUsuario.getRol_usuario(), pedido);
        //Impresora.imprimirGenerico(TICKET_CABECERA + TICKET_PIE);

    }

    public static void imprimirPedido(M_rol_usuario rol_usuario, M_pedido pedidoCabecera) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String fechaEntrega = sdfs.format(today);
        String CABECERA = "Fecha y hora:" + fechaEntrega + "\n"
                + "Cajero:" + pedidoCabecera.getFuncionario().getNombre() + "\n"
                + "Cliente: " + pedidoCabecera.getCliente().getEntidad() + "\n"
                + "Fecha pedido: " + pedidoCabecera.getTiempoRecepcion() + "\n"
                + "---------------------------------\n";

        ArrayList<M_pedidoDetalle> pedidoDetalle = DB_Pedido.obtenerPedidoDetalles(pedidoCabecera.getIdPedido());
        String COLUMNAS = "cant  producto   precio  subtotal\n";
        String DETALLE = "";
        for (int i = 0; i < pedidoDetalle.size(); i++) {
            int subtotal = Math.round(Math.round(pedidoDetalle.get(i).getCantidad() * pedidoDetalle.get(i).getPrecio()));
            DETALLE = DETALLE + pedidoDetalle.get(i).getCantidad() + " " + pedidoDetalle.get(i).getProducto().getDescripcion() + " " + pedidoDetalle.get(i).getPrecio() + "  " + subtotal + "\n";
        }
        String ticket = TICKET_CABECERA + CABECERA + COLUMNAS + DETALLE + TICKET_PIE_SIN_GRACIAS;
        System.out.println(ticket);
        byte[] bytes = ticket.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job = service.createPrintJob();
        try {
            if (job != null) {
                job.print(doc, null);
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrintException ex) {
            System.out.println(ex);
        }
    }

    private static String agregarLogoATicket(String textoAImprimir) {
        //TODO hacer pruebas de impresión de logo
        //TODO tal vez también se necesite imprimir una parte común al final
        return textoAImprimir;
    }
}
