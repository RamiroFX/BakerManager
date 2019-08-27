/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Impresora;

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
import Entities.E_ticketPreferencia;
import Entities.M_campoImpresion;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
import Entities.M_preferenciasImpresion;
import Entities.M_producto;
import Entities.M_rol_usuario;
import MenuPrincipal.DatosUsuario;
import Utilities.MyConstants;
import java.awt.Font;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
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

    public static M_preferenciasImpresion PREF_PRINT = DB_Preferencia.obtenerPreferenciaImpresion();
    public static E_ticketPreferencia PREF_PRINT_TICKET = DB_Preferencia.obtenerPreferenciaImpresionTicket();
    //public static M_preferenciasImpresion PREF_PRINT;
    private final static Font FUENTE_LETRA = new Font("Arial", Font.PLAIN, 8);
    private final static SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final static SimpleDateFormat SDFS_DATE_ONLY = new SimpleDateFormat("dd/MM/yyyy");
    private final static SimpleDateFormat SDFS_DATE_ONLY_WRITED_MONTH = new SimpleDateFormat("dd/MMMM/yyyy");
    private final static SimpleDateFormat SDFS_DATE_ONLY_WRITED_MONTH_2DIGIT_YEAR = new SimpleDateFormat("dd/MMMM/yy");

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
        String nombreImpresora = PREF_PRINT.getNombreImpresora();
        M_cliente cliente = new M_cliente();
        cliente.setNombre("Xxxxxx Xxxxxxxx");
        cliente.setRuc("12345678");
        cliente.setRucId("0");
        cliente.setDireccion("35XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        cliente.setEntidad("Xxxxxx Xxxxxxxx");
        M_facturaCabecera fc = new M_facturaCabecera();
        fc.setCliente(cliente);
        fc.setIdCondVenta(1);
        fc.setIdNotaRemision(1);
        fc.setNroFactura(123456789);
        fc.setTiempo(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        M_producto prod1 = new M_producto("XXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod1.setIdImpuesto(1);
        M_producto prod2 = new M_producto("XXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod2.setIdImpuesto(2);
        M_producto prod3 = new M_producto("XXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod3.setIdImpuesto(3);
        M_producto prod4 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod4.setIdImpuesto(3);
        M_producto prod5 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod5.setIdImpuesto(3);
        M_producto prod6 = new M_producto("XXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod6.setIdImpuesto(3);
        M_producto prod7 = new M_producto("XXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod7.setIdImpuesto(2);
        M_producto prod8 = new M_producto("XXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod8.setIdImpuesto(2);
        M_producto prod9 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod9.setIdImpuesto(2);
        M_producto prod10 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod10.setIdImpuesto(3);
        ArrayList<M_facturaDetalle> faDetalles = new ArrayList<>();
        M_facturaDetalle fd1 = new M_facturaDetalle();
        fd1.setCantidad(1.0);
        fd1.setDescuento(0.0);
        fd1.setPrecio(100);
        fd1.setProducto(prod1);
        faDetalles.add(fd1);
        M_facturaDetalle fd2 = new M_facturaDetalle();
        fd2.setCantidad(10.0);
        fd2.setDescuento(10.0);
        fd2.setPrecio(150);
        fd2.setProducto(prod2);
        faDetalles.add(fd2);
        M_facturaDetalle fd3 = new M_facturaDetalle();
        fd3.setCantidad(15.0);
        fd3.setDescuento(0.0);
        fd3.setPrecio(500);
        fd3.setProducto(prod3);
        faDetalles.add(fd3);
        M_facturaDetalle fd4 = new M_facturaDetalle();
        fd4.setCantidad(750.0);
        fd4.setDescuento(0.0);
        fd4.setPrecio(100);
        fd4.setProducto(prod4);
        faDetalles.add(fd4);
        M_facturaDetalle fd5 = new M_facturaDetalle();
        fd5.setCantidad(1000.0);
        fd5.setDescuento(0.0);
        fd5.setPrecio(850);
        fd5.setProducto(prod5);
        faDetalles.add(fd5);
        M_facturaDetalle fd6 = new M_facturaDetalle();
        fd6.setCantidad(400.0);
        fd6.setDescuento(0.0);
        fd6.setPrecio(430);
        fd6.setProducto(prod6);
        faDetalles.add(fd6);
        M_facturaDetalle fd7 = new M_facturaDetalle();
        fd7.setCantidad(9.0);
        fd7.setDescuento(0.0);
        fd7.setPrecio(5000);
        fd7.setProducto(prod7);
        faDetalles.add(fd7);
        M_facturaDetalle fd8 = new M_facturaDetalle();
        fd8.setCantidad(19.0);
        fd8.setDescuento(0.0);
        fd8.setPrecio(20000);
        fd8.setProducto(prod8);
        faDetalles.add(fd8);
        M_facturaDetalle fd9 = new M_facturaDetalle();
        fd9.setCantidad(1.0);
        fd9.setDescuento(0.0);
        fd9.setPrecio(47000);
        fd9.setProducto(prod9);
        faDetalles.add(fd9);
        M_facturaDetalle fd10 = new M_facturaDetalle();
        fd10.setCantidad(3.0);
        fd10.setDescuento(0.0);
        fd10.setPrecio(250000);
        fd10.setProducto(prod10);
        faDetalles.add(fd10);
        List<M_campoImpresion> textoAImprimir = DB_manager.obtenerCampoImpresion(2, MyConstants.ACTIVO);
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new VentaPrintable(PREF_PRINT, fc, faDetalles, textoAImprimir));
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        boolean existeImpresora = false;
        if (services.length > 0) {
            for (int i = 0; i < services.length; i++) {
                if (services[i].getName().equals(nombreImpresora)) {
                    existeImpresora = true;
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
                }
            }
            if (!existeImpresora) {
                JOptionPane.showMessageDialog(null, "No se encontró la impresora " + nombreImpresora, "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontro impresoras disponibles", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void imprimirTicketPrueba() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String nombreImpresora = PREF_PRINT_TICKET.getNombreImpresora();
        M_cliente cliente = new M_cliente();
        cliente.setNombre("Xxxxxx Xxxxxxxx");
        cliente.setRuc("12345678");
        cliente.setRucId("0");
        cliente.setDireccion("35XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        cliente.setEntidad("Xxxxxx Xxxxxxxx");
        M_funcionario func = new M_funcionario();
        func.setNombre("Empleado test");
        M_facturaCabecera fc = new M_facturaCabecera();
        fc.setFuncionario(func);
        fc.setCliente(cliente);
        fc.setIdCondVenta(1);
        fc.setIdNotaRemision(1);
        fc.setNroFactura(123456789);
        fc.setTiempo(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        M_producto prod1 = new M_producto("XXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod1.setIdImpuesto(1);
        M_producto prod2 = new M_producto("XXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod2.setIdImpuesto(2);
        M_producto prod3 = new M_producto("XXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod3.setIdImpuesto(3);
        M_producto prod4 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod4.setIdImpuesto(3);
        M_producto prod5 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod5.setIdImpuesto(3);
        M_producto prod6 = new M_producto("XXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod6.setIdImpuesto(3);
        M_producto prod7 = new M_producto("XXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod7.setIdImpuesto(2);
        M_producto prod8 = new M_producto("XXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod8.setIdImpuesto(2);
        M_producto prod9 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod9.setIdImpuesto(2);
        M_producto prod10 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1, 1, 1, 1.0);
        prod10.setIdImpuesto(3);
        ArrayList<M_facturaDetalle> faDetalles = new ArrayList<>();
        M_facturaDetalle fd1 = new M_facturaDetalle();
        fd1.setCantidad(1.0);
        fd1.setDescuento(0.0);
        fd1.setPrecio(100);
        fd1.setProducto(prod1);
        faDetalles.add(fd1);
        M_facturaDetalle fd2 = new M_facturaDetalle();
        fd2.setCantidad(10.0);
        fd2.setDescuento(10.0);
        fd2.setPrecio(150);
        fd2.setProducto(prod2);
        faDetalles.add(fd2);
        M_facturaDetalle fd3 = new M_facturaDetalle();
        fd3.setCantidad(15.0);
        fd3.setDescuento(0.0);
        fd3.setPrecio(500);
        fd3.setProducto(prod3);
        faDetalles.add(fd3);
        M_facturaDetalle fd4 = new M_facturaDetalle();
        fd4.setCantidad(750.0);
        fd4.setDescuento(0.0);
        fd4.setPrecio(100);
        fd4.setProducto(prod4);
        //faDetalles.add(fd4);
        M_facturaDetalle fd5 = new M_facturaDetalle();
        fd5.setCantidad(1000.0);
        fd5.setDescuento(0.0);
        fd5.setPrecio(850);
        fd5.setProducto(prod5);
        //faDetalles.add(fd5);
        M_facturaDetalle fd6 = new M_facturaDetalle();
        fd6.setCantidad(400.0);
        fd6.setDescuento(0.0);
        fd6.setPrecio(430);
        fd6.setProducto(prod6);
        //faDetalles.add(fd6);
        M_facturaDetalle fd7 = new M_facturaDetalle();
        fd7.setCantidad(9.0);
        fd7.setDescuento(0.0);
        fd7.setPrecio(5000);
        fd7.setProducto(prod7);
        faDetalles.add(fd7);
        M_facturaDetalle fd8 = new M_facturaDetalle();
        fd8.setCantidad(19.0);
        fd8.setDescuento(0.0);
        fd8.setPrecio(20000);
        fd8.setProducto(prod8);
        //faDetalles.add(fd8);
        M_facturaDetalle fd9 = new M_facturaDetalle();
        fd9.setCantidad(1.0);
        fd9.setDescuento(0.0);
        fd9.setPrecio(47000);
        fd9.setProducto(prod9);
        //faDetalles.add(fd9);
        M_facturaDetalle fd10 = new M_facturaDetalle();
        fd10.setCantidad(3.0);
        fd10.setDescuento(0.0);
        fd10.setPrecio(250000);
        fd10.setProducto(prod10);
        //faDetalles.add(fd10);
        String ruc = "-";
        if (fc.getCliente().getRuc() != null) {
            if (fc.getCliente().getRucId() != null) {
                ruc = fc.getCliente().getRuc() + "-" + fc.getCliente().getRucId();
            }
        }
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String datoVenta = "Fecha y hora: " + fechaEntrega + "\n"
                + "Cajero: " + fc.getFuncionario().getNombre() + "\n"
                + "Cliente: " + fc.getCliente().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "---------------------------------\n";
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        int total = 0;
        for (M_facturaDetalle pedidoDetalle1 : faDetalles) {
            int subtotal = Math.round(Math.round(pedidoDetalle1.getCantidad() * pedidoDetalle1.getPrecio()));
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + pedidoDetalle1.getProducto().getDescripcion() + "\n" + pedidoDetalle1.getCantidad() + " " + pedidoDetalle1.getPrecio() + "  " + subtotal + "\n";
        }
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n";
        String ticket = cabecera + datoVenta + COLUMNAS + DETALLE + SUMATOTAL + pie;
        byte[] bytes = ticket.getBytes();
        System.out.println(ticket);
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job = null;
        boolean existeImpresora = false;
        if (services.length > 0) {
            for (PrintService service : services) {
                if (service.getName().equals(nombreImpresora)) {
                    job = service.createPrintJob();
                    existeImpresora = true;
                    try {
                        if (job != null) {
                            job.print(doc, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (PrintException ex) {
                        JOptionPane.showMessageDialog(null, "Ocurrio un error al imprimir: " + ex.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            if (!existeImpresora) {
                JOptionPane.showMessageDialog(null, "No se encontró la impresora " + nombreImpresora, "Error", JOptionPane.INFORMATION_MESSAGE);
            }
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
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + CABECERA + COLUMNAS + DETALLE + SUMATOTAL + pie;
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
        String ventaCabecera = "Fecha y hora: " + fechaEntrega + "\n"
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
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
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
        String ventaCabecera = "Fecha y hora: " + fechaEntrega + "\n"
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
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
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
        String ventaCabecera = "Fecha y hora: " + fechaEntrega + "\n"
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
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
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

    public static void imprimirVentaFactura(final M_facturaCabecera facturaCabecera, final ArrayList<M_facturaDetalle> facturaDetalle) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String nombreImpresora = PREF_PRINT.getNombreImpresora();
        final List<M_campoImpresion> textoAImprimir = DB_manager.obtenerCampoImpresion(2, MyConstants.ACTIVO);
        Paper p = new Paper();
        PageFormat pf = new PageFormat();
        VentaPrintable vp = new VentaPrintable(PREF_PRINT, facturaCabecera, facturaDetalle, textoAImprimir);
        PrinterJob job = PrinterJob.getPrinterJob();
        int width = PREF_PRINT.getAnchoPagina();
        int height = PREF_PRINT.getLargoPagina();
        p.setSize(width, height);
        p.setImageableArea(PREF_PRINT.getMargenX(), PREF_PRINT.getMargenY(), width, height);
        pf.setPaper(p);
        job.setPrintable(vp, pf);
        if (services.length > 0) {
            for (int i = 0; i < services.length; i++) {
                if (services[i].getName().equals(nombreImpresora)) {
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
                    System.out.println("No se encontró la impresora " + nombreImpresora);
                }
            }
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
        String ventaCabecera = "Fecha y hora: " + fechaEntrega + "\n"
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
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
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
        String ventaCabecera = "Fecha y hora: " + fechaEntrega + "\n"
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
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
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

        String cabeceraTicket = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabeceraTicket + CABECERA + CUERPO + pie;
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
