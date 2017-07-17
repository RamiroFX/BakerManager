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
import DB.DB_Funcionario;
import DB.DB_Ingreso;
import DB.DB_Pedido;
import DB.DB_manager;
import Entities.Caja;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
import Entities.M_rol_usuario;
import MenuPrincipal.DatosUsuario;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
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
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class Impresora {

    private final static SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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

    public static void main(String[] args) {
        try {
            //ServicioDeImpresion impresion = new ServicioDeImpresion();
            //1552 vico c
            DB_manager.conectarBD("postgres", "postgres");
        } catch (SQLException ex) {
            Logger.getLogger(Impresora.class.getName()).log(Level.SEVERE, null, ex);
        }
        M_pedido pedido = DB_Pedido.obtenerPedido(1552);
        ArrayList<M_pedidoDetalle> pedidoDetalle = DB_Pedido.obtenerPedidoDetalles(1552);
        System.out.println("op");
        Impresora.imprimirPedido(DatosUsuario.getRol_usuario(), pedido, pedidoDetalle);
        //Impresora.imprimirGenerico(TICKET_CABECERA + TICKET_PIE);

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

    public static void imprimirCaja(Caja caja) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String CABECERA = "Fecha y hora: " + fechaEntrega + "\n"
                + "Funcionario: " + DB_Funcionario.obtenerDatosFuncionarioID(caja.getIdEmpleadoCierre()).getNombre() + "\n"
                + "Hora cierre: " + sdfs.format(caja.getTiempoCierre()) + "\n"
                + "---------------------------------\n";

        int Ingresos = caja.getIngresoCredito() + caja.getIngresoContado();
        int Egresos = caja.getEgresoContado() + caja.getIngresoCredito();
        String CUERPO = "Ingreso: " + Ingresos + "\n"
                + "Egreso: " + Egresos + "\n"
                + "Caja chica: " + caja.getMontoFinal() + "\n"
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
