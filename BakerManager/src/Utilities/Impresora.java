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
import DB.DB_manager;
import Entities.Caja;
import Entities.M_campoImpresion;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
import Entities.M_rol_usuario;
import MenuPrincipal.DatosUsuario;
import Parametros.TipoOperacion;
import java.awt.Graphics;
import java.awt.print.PageFormat;
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

    public static void main(String[] args) {
        try {
            //ServicioDeImpresion impresion = new ServicioDeImpresion();
            //1552 vico c
            DB_manager.conectarBD("postgres", "postgresql");
        } catch (SQLException ex) {
            Logger.getLogger(Impresora.class.getName()).log(Level.SEVERE, null, ex);
        }
        M_pedido pedido = DB_Pedido.obtenerPedido(1552);
        ArrayList<M_pedidoDetalle> pedidoDetalle = DB_Pedido.obtenerPedidoDetalles(1);
        System.out.println("op");
        //Impresora.imprimirPedido(DatosUsuario.getRol_usuario(), pedido, pedidoDetalle);
        //Impresora.imprimirGenerico(TICKET_CABECERA + TICKET_PIE);
        Impresora.imprimirPaginaPrueba();

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
        job.setPrintable(new MyPrintable(textoAImprimir));
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
        final List<M_campoImpresion> textoAImprimir = DB_manager.obtenerCampoImpresion(2, MyConstants.ACTIVO);
        Date today = Calendar.getInstance().getTime();
        final String fechaEntrega = sdfs.format(today);
        String rucAux = "-";
        if (facturaCabecera.getCliente().getRuc() != null) {
            if (facturaCabecera.getCliente().getRucId() != null) {
                rucAux = facturaCabecera.getCliente().getRuc() + "-" + facturaCabecera.getCliente().getRucId();
            }
        }
        final String ruc = rucAux;
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

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
                int total = 0;
                Integer exenta = 0;
                Integer iva5 = 0;
                Integer iva10 = 0;
                if (pi == 0) {
                    for (M_campoImpresion object : textoAImprimir) {
                        if (object.getCampo().equals(MyConstants.DATE_FULL)) {
                            g.drawString(fechaEntrega, object.getX().intValue(), object.getY().intValue());
                        }
                        if (object.getCampo().equals(MyConstants.TIOP_CONTADO)) {
                            if (facturaCabecera.getIdCondVenta() == TipoOperacion.CONTADO) {
                                g.drawString("X", object.getX().intValue(), object.getY().intValue());
                            }
                        }
                        if (object.getCampo().equals(MyConstants.TIOP_CREDITO)) {
                            if (facturaCabecera.getIdCondVenta() == TipoOperacion.CREDITO) {
                                g.drawString("X", object.getX().intValue(), object.getY().intValue());
                            }
                        }
                        if (object.getCampo().equals(MyConstants.RS)) {
                            g.drawString(facturaCabecera.getCliente().getEntidad(), object.getX().intValue(), object.getY().intValue());
                        }
                        if (object.getCampo().equals(MyConstants.RUC)) {
                            g.drawString(ruc, object.getX().intValue(), object.getY().intValue());
                        }
                        if (object.getCampo().equals(MyConstants.DIR)) {
                            g.drawString(facturaCabecera.getCliente().getDireccion(), object.getX().intValue(), object.getY().intValue());
                        }
                        if (object.getCampo().equals(MyConstants.DIR)) {
                            g.drawString(facturaCabecera.getIdNotaRemision() + "", object.getX().intValue(), object.getY().intValue());
                        }
                        if (object.getCampo().equals(MyConstants.DETAIL_SIMPLE)) {
                            int cantidadPosX = object.getX().intValue();
                            int descripcionPosX = cantidadPosX + 20;
                            int precioPosX = descripcionPosX + 20;
                            int exentaPosX = precioPosX + 10;
                            int iva5PosX = exentaPosX + 10;
                            int iva10PosX = iva5PosX + 10;
                            for (M_facturaDetalle pedidoDetalle1 : facturaDetalle) {
                                int subtotal = Math.round(Math.round(pedidoDetalle1.getCantidad() * pedidoDetalle1.getPrecio()));
                                total = total + subtotal;
                                g.drawString(pedidoDetalle1.getCantidad() + "", cantidadPosX, object.getY().intValue());
                                g.drawString(pedidoDetalle1.getProductoDescripcion() + "", descripcionPosX, object.getY().intValue());
                                g.drawString(pedidoDetalle1.getPrecio() + "", precioPosX, object.getY().intValue());
                                switch (pedidoDetalle1.getProducto().getImpuesto()) {
                                    case 0: {
                                        g.drawString(subtotal + "", exentaPosX, object.getY().intValue());
                                        exenta = exenta + subtotal;
                                        break;
                                    }
                                    case 1: {
                                        g.drawString(subtotal + "", iva5PosX, object.getY().intValue());
                                        iva5 = iva5 + subtotal;
                                        break;
                                    }
                                    case 2: {
                                        g.drawString(subtotal + "", iva10PosX, object.getY().intValue());
                                        iva10 = iva10 + subtotal;
                                        break;
                                    }
                                }
                            }
                        }
                        if (object.getCampo().equals(MyConstants.SUB_TOTAL_EXENTA)) {
                            g.drawString(facturaCabecera.getIdNotaRemision() + "", object.getX().intValue(), object.getY().intValue());
                        }
                        if (object.getCampo().equals(MyConstants.SUB_TOTAL_EXENTA)) {
                            g.drawString(facturaCabecera.getIdNotaRemision() + "", object.getX().intValue(), object.getY().intValue());
                        }
                    }
                    return PAGE_EXISTS;
                } else {
                    return NO_SUCH_PAGE;
                }
            }
        });
        try {
            if (job != null) {
                job.print();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
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
