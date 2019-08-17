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
import DB.DB_Caja;
import DB.DB_Egreso;
import DB.DB_Funcionario;
import DB.DB_Ingreso;
import DB.DB_Pedido;
import DB.DB_Preferencia;
import DB.DB_manager;
import Entities.Caja;
import Entities.M_campoImpresion;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
import Entities.M_preferenciasImpresion;
import Entities.M_rol_usuario;
import MenuPrincipal.DatosUsuario;
import Parametros.TipoOperacion;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.JOptionPane;
import org.bolivia.qulqi.Qulqi;

/**
 *
 * @author Administrador
 */
public class Impresora {

    public static M_preferenciasImpresion PREF_PRINT = DB_Preferencia.obtenerPreferenciaImpresion();
    //public static M_preferenciasImpresion PREF_PRINT;
    private final static Font FUENTE_LETRA = new Font("Arial", Font.PLAIN, 8);
    private final static SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final static SimpleDateFormat SDFS_DATE_ONLY = new SimpleDateFormat("dd/MM/yyyy");
    private final static SimpleDateFormat SDFS_DATE_ONLY_WRITED_MONTH = new SimpleDateFormat("dd/MMMM/yyyy");
    private final static SimpleDateFormat SDFS_DATE_ONLY_WRITED_MONTH_2DIGIT_YEAR = new SimpleDateFormat("dd/MMMM/yy");
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

    public static void main(String[] args) {
        try {
            //ServicioDeImpresion impresion = new ServicioDeImpresion();
            //1552 vico c
            DB_manager.conectarBD("postgres", "postgres");
        } catch (SQLException ex) {
            Logger.getLogger(Impresora.class.getName()).log(Level.SEVERE, null, ex);
        }
        PREF_PRINT = DB_Preferencia.obtenerPreferenciaImpresion();
        //PREF_PRINT = DB_Preferencia.obtenerPreferenciaImpresion();
        System.err.println("PREF_PRINT: " + PREF_PRINT);
        //M_pedido pedido = DB_Pedido.obtenerPedido(1552);
        //ArrayList<M_pedidoDetalle> pedidoDetalle = DB_Pedido.obtenerPedidoDetalles(1);
        M_facturaCabecera facturaCabecera = DB_Ingreso.obtenerIngresoCabeceraCompleto(6);
        ArrayList<M_facturaDetalle> facturaDetalle = DB_Ingreso.obtenerVentaDetalles(6);
        Impresora.imprimirVentaFactura(DatosUsuario.getRol_usuario(), facturaCabecera, facturaDetalle);
        //Impresora.imprimirPedido(DatosUsuario.getRol_usuario(), pedido, pedidoDetalle);
        //Impresora.imprimirGenerico(TICKET_CABECERA + TICKET_PIE);
        //Impresora.imprimirPaginaPrueba();

    }

    public static void imprimirCocina(String textoAImprimir) {
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

    public static void imprimirPaginaPrueba() {
        List<M_campoImpresion> textoAImprimir = DB_manager.obtenerCampoImpresion(2, MyConstants.ACTIVO);
        PrinterJob job = PrinterJob.getPrinterJob();
//        Paper p = new Paper();
//        int width = 595;
//        int height = 842;
//        p.setSize(width, height);
//        p.setImageableArea(0.0, 0.0, width, height);
//        PageFormat pf = new PageFormat();
//        pf.setPaper(p);
        job.setPrintable(new MyPrintable(textoAImprimir));
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < services.length; i++) {
            if (services[i].getName().equals("CutePDF")) {
                try {
                    job.setPrintService(services[i]);
                } catch (PrinterException ex) {
                    Logger.getLogger(Impresora.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            job.print();
        } catch (PrinterException e) {
            e.printStackTrace();
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

    public static void imprimirPedidoGuardado(M_rol_usuario rol_usuario, M_pedido pedidoCabecera) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String ruc = "-";
        if (pedidoCabecera.getCliente().getRuc() != null) {
            if (pedidoCabecera.getCliente().getRucId() != null) {
                ruc = pedidoCabecera.getCliente().getRuc() + "-" + pedidoCabecera.getCliente().getRucId();
            }
        }
        String CABECERA = "Fecha y hora: " + fechaEntrega + "\n"
                + "Cajero: " + pedidoCabecera.getFuncionario().getNombre() + "\n"
                + "Cliente: " + pedidoCabecera.getCliente().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "Fecha pedido: " + sdfs.format(pedidoCabecera.getTiempoRecepcion()) + "\n"
                + "---------------------------------\n";
        ArrayList<M_pedidoDetalle> pedidoDetalle = DB_Pedido.obtenerPedidoDetalles(pedidoCabecera.getIdPedido());
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        int total = 0;
        for (M_pedidoDetalle pedidoDetalle1 : pedidoDetalle) {
            int subtotal = Math.round(Math.round(pedidoDetalle1.getCantidad() * pedidoDetalle1.getPrecio()));
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + pedidoDetalle1.getProducto().getDescripcion() + "\n" + pedidoDetalle1.getCantidad() + " " + pedidoDetalle1.getPrecio() + "  " + subtotal + "\n";
        }
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n";
        String ticket = TICKET_CABECERA + CABECERA + COLUMNAS + DETALLE + SUMATOTAL + TICKET_PIE_SIN_GRACIAS;
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

    public static void imprimirPedido(M_rol_usuario rol_usuario, M_pedido pedidoCabecera, ArrayList<M_pedidoDetalle> pedidoDetalle) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String ruc = "-";
        if (pedidoCabecera.getCliente().getRuc() != null) {
            if (pedidoCabecera.getCliente().getRucId() != null) {
                ruc = pedidoCabecera.getCliente().getRuc() + "-" + pedidoCabecera.getCliente().getRucId();
            }
        }
        String CABECERA = "Fecha y hora: " + fechaEntrega + "\n"
                + "Cajero: " + pedidoCabecera.getFuncionario().getNombre() + "\n"
                + "Cliente: " + pedidoCabecera.getCliente().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "---------------------------------\n";
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        int total = 0;
        for (M_pedidoDetalle pedidoDetalle1 : pedidoDetalle) {
            int subtotal = Math.round(Math.round(pedidoDetalle1.getCantidad() * pedidoDetalle1.getPrecio()));
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + pedidoDetalle1.getProducto().getDescripcion() + "\n" + pedidoDetalle1.getCantidad() + " " + pedidoDetalle1.getPrecio() + "  " + subtotal + "\n";
        }
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n";
        String ticket = TICKET_CABECERA + CABECERA + COLUMNAS + DETALLE + SUMATOTAL + TICKET_PIE_SIN_GRACIAS;
        byte[] bytes = ticket.getBytes();
        System.out.println(ticket);
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

    public static void imprimirVentaGuardada(M_rol_usuario rol_usuario, M_facturaCabecera facturaCabecera) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        String fechaEntrega = sdfs.format(facturaCabecera.getTiempo());
        String ruc = "-";
        if (facturaCabecera.getCliente().getRuc() != null) {
            if (facturaCabecera.getCliente().getRucId() != null) {
                ruc = facturaCabecera.getCliente().getRuc() + "-" + facturaCabecera.getCliente().getRucId();
            }
        }
        String CABECERA = "Fecha y hora: " + fechaEntrega + "\n"
                + "Ticket nro.: " + facturaCabecera.getIdFacturaCabecera() + "\n"
                + "Cajero: " + facturaCabecera.getFuncionario().getNombre() + "\n"
                + "Cliente: " + facturaCabecera.getCliente().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "---------------------------------\n";
        ArrayList<M_facturaDetalle> facturaDetalle = DB_Ingreso.obtenerVentaDetalles(facturaCabecera.getIdFacturaCabecera());
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        int total = 0;
        for (M_facturaDetalle facturaDetalle1 : facturaDetalle) {
            int subtotal = Math.round(Math.round(facturaDetalle1.getCantidad() * facturaDetalle1.getPrecio()));
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + facturaDetalle1.getProducto().getDescripcion() + "\n" + facturaDetalle1.getCantidad() + " " + facturaDetalle1.getPrecio() + "  " + subtotal + "\n";
        }
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n";
        String ticket = TICKET_CABECERA + CABECERA + COLUMNAS + DETALLE + SUMATOTAL + TICKET_PIE_SIN_GRACIAS;
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

    public static void imprimirVenta(M_rol_usuario rol_usuario, M_facturaCabecera facturaCabecera, ArrayList<M_facturaDetalle> facturaDetalle) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String ruc = "-";
        if (facturaCabecera.getCliente().getRuc() != null) {
            if (facturaCabecera.getCliente().getRucId() != null) {
                ruc = facturaCabecera.getCliente().getRuc() + "-" + facturaCabecera.getCliente().getRucId();
            }
        }
        String CABECERA = "Fecha y hora: " + fechaEntrega + "\n"
                + "Ticket nro.: " + facturaCabecera.getIdFacturaCabecera() + "\n"
                + "Cajero: " + facturaCabecera.getFuncionario().getNombre() + "\n"
                + "Cliente: " + facturaCabecera.getCliente().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "---------------------------------\n";
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        int total = 0;
        for (M_facturaDetalle pedidoDetalle1 : facturaDetalle) {
            int subtotal = Math.round(Math.round(pedidoDetalle1.getCantidad() * pedidoDetalle1.getPrecio()));
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + pedidoDetalle1.getProducto().getDescripcion() + "\n" + pedidoDetalle1.getCantidad() + " " + pedidoDetalle1.getPrecio() + "  " + subtotal + "\n";
        }
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n";
        String ticket = TICKET_CABECERA + CABECERA + COLUMNAS + DETALLE + SUMATOTAL + TICKET_PIE_SIN_GRACIAS;
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

    public static void imprimirVentaFactura(M_rol_usuario rol_usuario, final M_facturaCabecera facturaCabecera, final ArrayList<M_facturaDetalle> facturaDetalle) {
        final int espaciadorY = 10;
        SimpleDateFormat dateFormat = new SimpleDateFormat(PREF_PRINT.getFormatoFecha());
        final List<M_campoImpresion> textoAImprimir = DB_manager.obtenerCampoImpresion(2, MyConstants.ACTIVO);
        Date fecha = Calendar.getInstance().getTime();
        fecha.setTime(facturaCabecera.getTiempo().getTime());
        final String fechaEntrega = dateFormat.format(fecha);
        final String[] fechas = fechaEntrega.split("/");
        String rucAux = "";
        Paper p = new Paper();
        int width = 595;
        int height = 842;
        p.setSize(width, height);
        p.setImageableArea(0.0, 0.0, width, height);
        PageFormat pf = new PageFormat();
        pf.setPaper(p);
        if (facturaCabecera.getCliente().getRuc() != null) {
            if (facturaCabecera.getCliente().getRucId() != null) {
                rucAux = facturaCabecera.getCliente().getRuc() + "-" + facturaCabecera.getCliente().getRucId();
            }
        }
        final String ruc = rucAux;
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
                g.setFont(new Font(PREF_PRINT.getLetterFont(), Font.PLAIN, PREF_PRINT.getLetterSize()));
                Integer exenta = 0;
                Integer iva5 = 0;
                Integer iva10 = 0;
                Integer liquidacionIva5 = 0;
                Integer liquidacionIva10 = 0;
                if (pi == 0) {
                    for (M_campoImpresion object : textoAImprimir) {
                        int posY = object.getY().intValue();
                        int posX = object.getX().intValue();
                        if (object.getCampo().equals(MyConstants.DATE_FULL)) {
                            g.drawString(fechaEntrega, posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(fechaEntrega, posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(fechaEntrega, posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DATE_DAY)) {
                            g.drawString(fechas[0], posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(fechas[0], posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(fechas[0], posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DATE_MONTH)) {
                            g.drawString(fechas[1], posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(fechas[1], posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(fechas[1], posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DATE_YEAR_MIN)) {
                            g.drawString(fechas[2], posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(fechas[2], posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(fechas[2], posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DATE_YEAR_FULL)) {
                            g.drawString(fechas[2], posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(fechas[2], posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(fechas[2], posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.NRO_FACTURA)) {
                            String nroFactura = facturaCabecera.getNroFactura() + "";
                            g.drawString(nroFactura, posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(nroFactura, posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(nroFactura, posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.TIOP_CONTADO)) {
                            if (facturaCabecera.getIdCondVenta() == TipoOperacion.CONTADO) {
                                g.drawString("X", posX, posY);
                                if (PREF_PRINT.getIdDuplicado() == 1) {
                                    int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString("X", posX, duplicadoDist);
                                }
                                if (PREF_PRINT.getIdTriplicado() == 1) {
                                    int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString("X", posX, triplicadoDist);
                                }
                            }
                        }
                        if (object.getCampo().equals(MyConstants.TIOP_CREDITO)) {
                            if (facturaCabecera.getIdCondVenta() == TipoOperacion.CREDITO) {
                                g.drawString("X", posX, posY);
                                if (PREF_PRINT.getIdDuplicado() == 1) {
                                    int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString("X", posX, duplicadoDist);
                                }
                                if (PREF_PRINT.getIdTriplicado() == 1) {
                                    int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString("X", posX, triplicadoDist);
                                }
                            }
                        }
                        if (object.getCampo().equals(MyConstants.RS)) {
                            g.drawString(facturaCabecera.getCliente().getEntidad(), posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(facturaCabecera.getCliente().getEntidad(), posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(facturaCabecera.getCliente().getEntidad(), posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.RUC)) {
                            g.drawString(ruc, posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(ruc, posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(ruc, posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DIR)) {
                            String dir = "";
                            if (facturaCabecera.getCliente().getDireccion() != null) {
                                dir = facturaCabecera.getCliente().getDireccion();
                            }
                            g.drawString(dir, posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(dir, posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(dir, posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.REMISION)) {
                            String remision = "";
                            if (facturaCabecera.getIdNotaRemision() != null) {
                                remision = facturaCabecera.getIdNotaRemision() + "";
                            }
                            g.drawString(remision, posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(remision, posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(remision, posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DETAIL_CANT)) {
                            for (M_facturaDetalle fd : facturaDetalle) {
                                g.drawString(fd.getCantidad() + "", posX, posY);
                                if (PREF_PRINT.getIdDuplicado() == 1) {
                                    int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString(fd.getCantidad() + "", posX, duplicadoDist);
                                }
                                if (PREF_PRINT.getIdTriplicado() == 1) {
                                    int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString(fd.getCantidad() + "", posX, triplicadoDist);
                                }
                                posY = posY + espaciadorY;
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DETAIL_COD)) {
                            for (M_facturaDetalle fd : facturaDetalle) {
                                g.drawString(fd.getProducto().getCodBarra() + "", posX, posY);
                                if (PREF_PRINT.getIdDuplicado() == 1) {
                                    int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString(fd.getProducto().getCodBarra() + "", posX, duplicadoDist);
                                }
                                if (PREF_PRINT.getIdTriplicado() == 1) {
                                    int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString(fd.getProducto().getCodBarra() + "", posX, triplicadoDist);
                                }
                                posY = posY + espaciadorY;
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DETAIL_PROD)) {
                            for (M_facturaDetalle fd : facturaDetalle) {
                                g.drawString(fd.getProducto().getDescripcion() + "", posX, posY);
                                if (PREF_PRINT.getIdDuplicado() == 1) {
                                    int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString(fd.getProducto().getDescripcion() + "", posX, duplicadoDist);
                                }
                                if (PREF_PRINT.getIdTriplicado() == 1) {
                                    int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString(fd.getProducto().getDescripcion() + "", posX, triplicadoDist);
                                }
                                posY = posY + espaciadorY;
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DETAIL_PRECIO)) {
                            for (M_facturaDetalle fd : facturaDetalle) {
                                g.drawString(fd.getPrecio() + "", posX, posY);
                                if (PREF_PRINT.getIdDuplicado() == 1) {
                                    int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString(fd.getPrecio() + "", posX, duplicadoDist);
                                }
                                if (PREF_PRINT.getIdTriplicado() == 1) {
                                    int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                    g.drawString(fd.getPrecio() + "", posX, triplicadoDist);
                                }
                                posY = posY + espaciadorY;
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DETAIL_EXENTA)) {
                            for (M_facturaDetalle fd : facturaDetalle) {
                                if (fd.getProducto().getIdImpuesto() == 1) {
                                    int subtotal = Math.round(Math.round(fd.getCantidad() * fd.getPrecio()));
                                    g.drawString(subtotal + "", posX, posY);
                                    if (PREF_PRINT.getIdDuplicado() == 1) {
                                        int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                        g.drawString(subtotal + "", posX, duplicadoDist);
                                    }
                                    if (PREF_PRINT.getIdTriplicado() == 1) {
                                        int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                        g.drawString(subtotal + "", posX, triplicadoDist);
                                    }
                                    exenta = exenta + subtotal;
                                }
                                posY = posY + espaciadorY;
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DETAIL_IVA5)) {
                            for (M_facturaDetalle fd : facturaDetalle) {
                                if (fd.getProducto().getIdImpuesto() == 2) {
                                    int subtotal = Math.round(Math.round(fd.getCantidad() * fd.getPrecio()));
                                    g.drawString(subtotal + "", posX, posY);
                                    if (PREF_PRINT.getIdDuplicado() == 1) {
                                        int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                        g.drawString(subtotal + "", posX, duplicadoDist);
                                    }
                                    if (PREF_PRINT.getIdTriplicado() == 1) {
                                        int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                        g.drawString(subtotal + "", posX, triplicadoDist);
                                    }
                                    iva5 = iva5 + subtotal;
                                }
                                posY = posY + espaciadorY;
                            }
                        }
                        if (object.getCampo().equals(MyConstants.DETAIL_IVA10)) {
                            for (M_facturaDetalle fd : facturaDetalle) {
                                if (fd.getProducto().getIdImpuesto() == 3) {
                                    int subtotal = Math.round(Math.round(fd.getCantidad() * fd.getPrecio()));
                                    g.drawString(subtotal + "", posX, posY);
                                    if (PREF_PRINT.getIdDuplicado() == 1) {
                                        int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                        g.drawString(subtotal + "", posX, duplicadoDist);
                                    }
                                    if (PREF_PRINT.getIdTriplicado() == 1) {
                                        int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                        g.drawString(subtotal + "", posX, triplicadoDist);
                                    }
                                    iva10 = iva10 + subtotal;
                                }
                                posY = posY + espaciadorY;
                            }
                        }
                        if (object.getCampo().equals(MyConstants.SUB_TOTAL_EXENTA)) {
                            g.drawString(exenta + "", posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(exenta + "", posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(exenta + "", posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.SUB_TOTAL_IVA5)) {
                            g.drawString(iva5 + "", posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(iva5 + "", posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(iva5 + "", posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.SUB_TOTAL_IVA10)) {
                            g.drawString(iva10 + "", posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(iva10 + "", posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(iva10 + "", posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.TOTAL_LETRA)) {
                            //Calcular el total
                            int total_letra = 0;
                            for (M_facturaDetalle fd : facturaDetalle) {
                                int subtotal = Math.round(Math.round(fd.getCantidad() * fd.getPrecio()));
                                total_letra = total_letra + subtotal;
                            }
                            Qulqi qulqi = new Qulqi();
                            qulqi.setDecimalPartVisible(false);
                            String totalLetra = qulqi.showMeTheMoney(total_letra + "");
                            g.drawString(totalLetra + "", posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(totalLetra + "", posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(totalLetra + "", posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.TOTAL_NUMERO)) {
                            //Calcular el total
                            int total = 0;
                            for (M_facturaDetalle fd : facturaDetalle) {
                                int subtotal = Math.round(Math.round(fd.getCantidad() * fd.getPrecio()));
                                total = total + subtotal;
                            }
                            g.drawString(total + "", posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(total + "", posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(total + "", posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.LIQUIDACION_IVA5)) {
                            //Calcular liquidacion de iva5 si es que hay productos con impuesto de 5%
                            if (iva5 > 0) {
                                liquidacionIva5 = iva5 / 21;
                            }
                            g.drawString(liquidacionIva5 + "", posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(liquidacionIva5 + "", posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(liquidacionIva5 + "", posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.LIQUIDACION_IVA10)) {
                            //Calcular liquidacion de iva5 si es que hay productos con impuesto de 10%
                            if (iva10 > 0) {
                                liquidacionIva10 = iva10 / 11;
                            }
                            g.drawString(liquidacionIva10 + "", posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(liquidacionIva10 + "", posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(liquidacionIva10 + "", posX, triplicadoDist);
                            }
                        }
                        if (object.getCampo().equals(MyConstants.LIQUIDACION_TOTAL)) {
                            int liquidTotal = liquidacionIva5 + liquidacionIva10;
                            g.drawString(liquidTotal + "", posX, posY);
                            if (PREF_PRINT.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(liquidTotal + "", posX, duplicadoDist);
                            }
                            if (PREF_PRINT.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + PREF_PRINT.getDistanceBetweenCopies() + PREF_PRINT.getDistanceBetweenCopies();
                                g.drawString(liquidTotal + "", posX, triplicadoDist);
                            }
                        }
                    }
                    return PAGE_EXISTS;
                } else {
                    return NO_SUCH_PAGE;
                }
            }
        }, pf);
        try {
            if (job != null) {
                job.print();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            System.out.println(ex);
        }
        //----------
        /*PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null); //nos da el array de los servicios de impresion
        if (services.length > 0) {
            for (int i = 0; i < services.length; i++) {
                System.err.println("Printer: " + services[i].getName());
                if (services[i].getName().equals("CutePDF")) {//aqui escribimos/elegimos la impresora por la que queremos imprimir
                    //manejar error en caso de que no esté conectada
                    //Desktop.getDesktop().print(null);//para imprimir un archivo ya existente  
                    try {
                        if (job != null) {
                            job.setPrintService(services[i]);
                            job.print();
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (PrinterException ex) {
                        System.out.println(ex);
                    }
                    break;
                } else {
                    System.out.println("No se encontró la impresora CutePDF");
                }
            }

        }*/
    }

    public static void imprimirMesa(M_mesa mesaCabecera, ArrayList<M_mesa_detalle> detalles) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String ruc = "-";
        if (mesaCabecera.getCliente().getRuc() != null) {
            if (mesaCabecera.getCliente().getRucId() != null) {
                ruc = mesaCabecera.getCliente().getRuc() + "-" + mesaCabecera.getCliente().getRucId();
            }
        }
        String CABECERA = "Fecha y hora: " + fechaEntrega + "\n"
                + "Mesa nro.: " + mesaCabecera.getNumeroMesa() + "\n"
                + "Cajero: " + mesaCabecera.getFuncionario().getNombre() + "\n"
                + "Cliente: " + mesaCabecera.getCliente().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "---------------------------------\n";
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        int total = 0;
        for (M_mesa_detalle mesaDetalle : detalles) {
            int subtotal = Math.round(Math.round(mesaDetalle.getCantidad() * mesaDetalle.getPrecio()));
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + mesaDetalle.getProducto().getDescripcion() + "\n" + mesaDetalle.getCantidad() + " " + mesaDetalle.getPrecio() + "  " + subtotal + "\n";
        }
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n";
        String ticket = TICKET_CABECERA + CABECERA + COLUMNAS + DETALLE + SUMATOTAL + TICKET_PIE_SIN_GRACIAS;
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

    public static void imprimirVentaMesa(M_mesa mesaCabecera, ArrayList<M_mesa_detalle> detalles) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String ruc = "-";
        if (mesaCabecera.getCliente().getRuc() != null) {
            if (mesaCabecera.getCliente().getRucId() != null) {
                ruc = mesaCabecera.getCliente().getRuc() + "-" + mesaCabecera.getCliente().getRucId();
            }
        }
        String CABECERA = "Fecha y hora: " + fechaEntrega + "\n"
                + "Ticket nro.: " + mesaCabecera.getIdMesa() + "\n"
                + "Cajero: " + mesaCabecera.getFuncionario().getNombre() + "\n"
                + "Cliente: " + mesaCabecera.getCliente().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "---------------------------------\n";
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        int total = 0;
        for (M_mesa_detalle mesaDetalle : detalles) {
            int subtotal = Math.round(Math.round(mesaDetalle.getCantidad() * mesaDetalle.getPrecio()));
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + mesaDetalle.getProducto().getDescripcion() + "\n" + mesaDetalle.getCantidad() + " " + mesaDetalle.getPrecio() + "  " + subtotal + "\n";
        }
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n";
        String ticket = TICKET_CABECERA + CABECERA + COLUMNAS + DETALLE + SUMATOTAL + TICKET_PIE_SIN_GRACIAS;
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

    public static void imprimirCaja(Caja caja, int efectivoDepositado) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(caja.getTiempoApertura());
        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MINUTE, 0);
        Calendar fin = Calendar.getInstance();
        fin.setTime(caja.getTiempoApertura());
        fin.set(Calendar.HOUR_OF_DAY, 23);
        fin.set(Calendar.MINUTE, 59);
        Timestamp ini = new Timestamp(inicio.getTimeInMillis());
        Timestamp fi = new Timestamp(fin.getTimeInMillis());
        int egresoContado = DB_Egreso.obtenerTotalEgreso(ini, fi, 1);
        int egresoCretdito = DB_Egreso.obtenerTotalEgreso(ini, fi, 2);
        int ingresoContado = DB_Ingreso.obtenerTotalIngreso(ini, fi, 1);
        int ingresoCretdito = DB_Ingreso.obtenerTotalIngreso(ini, fi, 2);
        int cajaCierre = DB_Caja.obtenerTotalArqueoCaja(caja.getIdCaja(), 2);

        String CABECERA = "Fecha y hora: " + fechaEntrega + "\n"
                + "Funcionario: " + DB_Funcionario.obtenerDatosFuncionarioID(caja.getIdEmpleadoCierre()).getNombre() + "\n"
                + "Hora cierre: " + sdfs.format(caja.getTiempoCierre()) + "\n"
                + "---------------------------------\n";

        int Ingresos = ingresoContado + ingresoCretdito;
        int Egresos = egresoContado + egresoCretdito;
        String CUERPO = "Ingreso: " + Ingresos + "\n"
                + "Egreso: " + Egresos + "\n"
                + "Caja chica: " + cajaCierre + "\n"
                + "Efectivo depositado: " + efectivoDepositado + "\n"
                + "Ingreso-Egreso: " + (Ingresos - Egresos) + "\n"
                + "Ingreso+Egreso: " + (Ingresos + Egresos) + "\n";

        String PIE = "---------------------------------\n";
        String ticket = CABECERA + CUERPO + PIE + TICKET_PIE_SIN_GRACIAS;
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
}
