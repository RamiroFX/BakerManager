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
import DB.DB_Preferencia;
import DB.DB_manager;
import Entities.Caja;
import Entities.E_facturaDetalle;
import Entities.E_impresionOrientacion;
import Entities.E_ticketPreferencia;
import Entities.Estado;
import Entities.M_campoImpresion;
import Entities.M_cliente;
import Entities.M_egresoCabecera;
import Entities.M_egreso_detalle;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Entities.M_pedido;
import Entities.M_preferenciasImpresion;
import Entities.M_producto;
import Entities.M_rol_usuario;
import Utilities.MyConstants;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
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
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class Impresora {

    public static M_preferenciasImpresion PREF_PRINT_FACTURA = DB_Preferencia.obtenerPreferenciaImpresionFactura();
    public static M_preferenciasImpresion PREF_PRINT_BOLETA = DB_Preferencia.obtenerPreferenciaImpresionBoleta();
    public static E_ticketPreferencia PREF_PRINT_TICKET = DB_Preferencia.obtenerPreferenciaImpresionTicket();
    private final static SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static M_cliente obtenerClientePrueba() {
        M_cliente cliente = new M_cliente();
        cliente.setNombre("Xxxxxx Xxxxxxxx");
        cliente.setRuc("12345678");
        cliente.setRucId("0");
        cliente.setDireccion("35XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        cliente.setEntidad("Xxxxxx Xxxxxxxx");
        return cliente;
    }

    private static M_facturaCabecera obtenerFacturaCabeceraPrueba(M_cliente cliente) {
        M_facturaCabecera fc = new M_facturaCabecera();
        fc.setCliente(cliente);
        fc.setIdCondVenta(1);
        fc.setIdNotaRemision(1);
        fc.setNroFactura(123456789);
        fc.setTiempo(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        return fc;
    }

    private static ArrayList<E_facturaDetalle> obtenerFacturaDetallePrueba() {
        ArrayList<E_facturaDetalle> faDetalles = new ArrayList<>();
        M_producto prod1 = new M_producto("XXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1.0, 1.0, 1.0, 1.0, "");
        prod1.setIdImpuesto(1);
        M_producto prod2 = new M_producto("XXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1.0, 1.0, 1.0, 1.0, "");
        prod2.setIdImpuesto(2);
        M_producto prod3 = new M_producto("XXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1.0, 1.0, 1.0, 1.0, "");
        prod3.setIdImpuesto(3);
        M_producto prod4 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1.0, 1.0, 1.0, 1.0, "");
        prod4.setIdImpuesto(3);
        M_producto prod5 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1.0, 1.0, 1.0, 1.0, "");
        prod5.setIdImpuesto(3);
        M_producto prod6 = new M_producto("XXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1.0, 1.0, 1.0, 1.0, "");
        prod6.setIdImpuesto(3);
        M_producto prod7 = new M_producto("XXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1.0, 1.0, 1.0, 1.0, "");
        prod7.setIdImpuesto(2);
        M_producto prod8 = new M_producto("XXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1.0, 1.0, 1.0, 1.0, "");
        prod8.setIdImpuesto(2);
        M_producto prod9 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1.0, 1.0, 1.0, 1.0, "");
        prod9.setIdImpuesto(2);
        M_producto prod10 = new M_producto("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "XXXXXXX", "XXXX", "Activo", 1, "XXXXXXXX", 2, 1.0, 1.0, 1.0, 1.0, "");
        prod10.setIdImpuesto(3);
        E_facturaDetalle fd1 = new E_facturaDetalle();
        fd1.setCantidad(1.0);
        fd1.setDescuento(0.0);
        fd1.setPrecio(100.0);
        fd1.setProducto(prod1);
        faDetalles.add(fd1);
        E_facturaDetalle fd2 = new E_facturaDetalle();
        fd2.setCantidad(10.0);
        fd2.setDescuento(10.0);
        fd2.setPrecio(150.0);
        fd2.setProducto(prod2);
        faDetalles.add(fd2);
        E_facturaDetalle fd3 = new E_facturaDetalle();
        fd3.setCantidad(15.0);
        fd3.setDescuento(0.0);
        fd3.setPrecio(500.0);
        fd3.setProducto(prod3);
        faDetalles.add(fd3);
        E_facturaDetalle fd4 = new E_facturaDetalle();
        fd4.setCantidad(750.0);
        fd4.setDescuento(0.0);
        fd4.setPrecio(100.0);
        fd4.setProducto(prod4);
        faDetalles.add(fd4);
        E_facturaDetalle fd5 = new E_facturaDetalle();
        fd5.setCantidad(1000.0);
        fd5.setDescuento(0.0);
        fd5.setPrecio(850.0);
        fd5.setProducto(prod5);
        faDetalles.add(fd5);
        E_facturaDetalle fd6 = new E_facturaDetalle();
        fd6.setCantidad(400.0);
        fd6.setDescuento(0.0);
        fd6.setPrecio(430.0);
        fd6.setProducto(prod6);
        faDetalles.add(fd6);
        E_facturaDetalle fd7 = new E_facturaDetalle();
        fd7.setCantidad(9.0);
        fd7.setDescuento(0.0);
        fd7.setPrecio(5000.0);
        fd7.setProducto(prod7);
        faDetalles.add(fd7);
        E_facturaDetalle fd8 = new E_facturaDetalle();
        fd8.setCantidad(19.0);
        fd8.setDescuento(0.0);
        fd8.setPrecio(20000.0);
        fd8.setProducto(prod8);
        faDetalles.add(fd8);
        E_facturaDetalle fd9 = new E_facturaDetalle();
        fd9.setCantidad(1.0);
        fd9.setDescuento(0.0);
        fd9.setPrecio(47000.0);
        fd9.setProducto(prod9);
        faDetalles.add(fd9);
        E_facturaDetalle fd10 = new E_facturaDetalle();
        fd10.setCantidad(3.0);
        fd10.setDescuento(0.0);
        fd10.setPrecio(250000.0);
        fd10.setProducto(prod10);
        faDetalles.add(fd10);
        return faDetalles;
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
            for (PrintService service : services) {
                if (service.getName().equals("cocina")) {
                    //aqui escribimos/elegimos la impresora por la que queremos imprimir
                    //manejar error en caso de que no esté conectada
                    //Desktop.getDesktop().print(null);//para imprimir un archivo ya existente
                    job = service.createPrintJob(); // System.out.println(i+": "+services[i].getName());
                } else {
                    System.out.println("No se encontró la impresora cocina");
                }
            }

            PrinterJob pj = PrinterJob.getPrinterJob();

            PageFormat mPageFormat = pj.defaultPage();
            //pj.setPrintable(cp, mPageFormat);
            if (pj.printDialog()) {
                try {
                    //PrintService service = pj.getPrintService();//PrintServiceLookup.();
                    pj.print();
                    //DocPrintJob pjb;
                    // job = service.createPrintJob();
                } catch (PrinterException ex) {
                    Logger.getLogger(Impresora.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    /*
    IMPRESIONES DE PRUEBA
     */
    public static void imprimirFacturaPrueba() {
        String nombreImpresora = PREF_PRINT_FACTURA.getNombreImpresora();
        M_cliente cliente = obtenerClientePrueba();
        ArrayList<E_facturaDetalle> facturaDetalle = obtenerFacturaDetallePrueba();
        M_facturaCabecera facturaCabecera = obtenerFacturaCabeceraPrueba(cliente);
        List<M_campoImpresion> textoAImprimir = DB_manager.obtenerCampoImpresion(2, MyConstants.ACTIVO);
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();
        int width = PREF_PRINT_FACTURA.getAnchoPagina();
        int height = PREF_PRINT_FACTURA.getLargoPagina();
        switch (PREF_PRINT_FACTURA.getOrientacion().getId()) {
            case E_impresionOrientacion.PORTRAIT: {
                paper.setSize(width, height);
                paper.setImageableArea(PREF_PRINT_FACTURA.getMargenX(), PREF_PRINT_FACTURA.getMargenY(), width, height);
                pageFormat.setOrientation(PageFormat.PORTRAIT);
                break;
            }
            case E_impresionOrientacion.LANDSCAPE: {
                pageFormat.setOrientation(PageFormat.LANDSCAPE);
                paper.setSize(height, width);
                paper.setImageableArea(PREF_PRINT_FACTURA.getMargenX(), PREF_PRINT_FACTURA.getMargenY(), height, width);
                break;
            }
            case E_impresionOrientacion.REVERSE_LANDSCAPE: {
                paper.setSize(height, width);
                paper.setImageableArea(PREF_PRINT_FACTURA.getMargenX(), PREF_PRINT_FACTURA.getMargenY(), width, height);
                pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
                break;
            }
        }
        pageFormat.setPaper(paper);
        VentaPrintable vp = new VentaPrintable(PREF_PRINT_FACTURA, facturaCabecera, facturaDetalle, textoAImprimir);
        job.setPrintable(vp, pageFormat);
        Book book = new Book();//java.awt.print.Book
        book.append(vp, pageFormat);
        job.setPageable(book);
        //job.setPrintable(vp, pageFormat);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        boolean existeImpresora = false;
        if (services.length > 0) {
            for (PrintService service : services) {
                if (service.getName().equals(nombreImpresora)) {
                    existeImpresora = true;
                    try {
                        if (job != null) {
                            job.setPrintService(service);
                            job.print();
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (PrinterException ex) {
                        JOptionPane.showMessageDialog(null, "Ocurrio un error al tratar de imprimir la factura. Intente nuevamente.", "Error", JOptionPane.INFORMATION_MESSAGE);
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

    public static void imprimirBoletaPrueba() {
        String nombreImpresora = PREF_PRINT_BOLETA.getNombreImpresora();
        M_cliente cliente = obtenerClientePrueba();
        M_facturaCabecera facturaCabecera = obtenerFacturaCabeceraPrueba(cliente);
        ArrayList<E_facturaDetalle> facturaDetalle = obtenerFacturaDetallePrueba();
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();
        int width = PREF_PRINT_BOLETA.getAnchoPagina();
        int height = PREF_PRINT_BOLETA.getLargoPagina();
        switch (PREF_PRINT_BOLETA.getOrientacion().getId()) {
            case E_impresionOrientacion.PORTRAIT: {
                paper.setSize(width, height);
                paper.setImageableArea(PREF_PRINT_BOLETA.getMargenX(), PREF_PRINT_BOLETA.getMargenY(), width, height);
                pageFormat.setOrientation(PageFormat.PORTRAIT);
                break;
            }
            case E_impresionOrientacion.LANDSCAPE: {
                paper.setSize(width, height);
                paper.setImageableArea(PREF_PRINT_BOLETA.getMargenX(), PREF_PRINT_BOLETA.getMargenY(), width, height);
                pageFormat.setOrientation(PageFormat.PORTRAIT);
                break;
            }
            case E_impresionOrientacion.REVERSE_LANDSCAPE: {
                paper.setSize(width, height);
                paper.setImageableArea(PREF_PRINT_BOLETA.getMargenX(), PREF_PRINT_BOLETA.getMargenY(), width, height);
                pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
                break;
            }
        }
        pageFormat.setPaper(paper);
        job.setPrintable(new BoletaPrintable(PREF_PRINT_BOLETA, facturaCabecera, facturaDetalle), pageFormat);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        boolean existeImpresora = false;
        if (services.length > 0) {
            for (PrintService service : services) {
                if (service.getName().equals(nombreImpresora)) {
                    existeImpresora = true;
                    try {
                        if (job != null) {
                            job.setPrintService(service);
                            job.defaultPage().setPaper(paper);
                            job.print();
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (PrinterException ex) {
                        JOptionPane.showMessageDialog(null, "Ocurrio un error al tratar de imprimir la boleta. Intente nuevamente.", "Error", JOptionPane.INFORMATION_MESSAGE);
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
        M_cliente cliente = obtenerClientePrueba();
        M_funcionario func = new M_funcionario();
        func.setNombre("Empleado test");
        M_facturaCabecera facturaCabecera = obtenerFacturaCabeceraPrueba(cliente);
        facturaCabecera.setFuncionario(func);
        ArrayList<E_facturaDetalle> facturaDetalle = obtenerFacturaDetallePrueba();
        String ruc = "-";
        if (facturaCabecera.getCliente().getRuc() != null) {
            if (facturaCabecera.getCliente().getRucId() != null) {
                ruc = facturaCabecera.getCliente().getRuc() + "-" + facturaCabecera.getCliente().getRucId();
            }
        }
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String datoVenta = "Fecha y hora: " + fechaEntrega + "\n"
                + "Cajero: " + facturaCabecera.getFuncionario().getNombre() + "\n"
                + "Cliente: " + facturaCabecera.getCliente().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "---------------------------------\n";
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        int total = 0;
        int iva5 = 0;
        int iva10 = 0;
        for (E_facturaDetalle fd : facturaDetalle) {
            double precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
            int subtotal = Math.round(Math.round(fd.getCantidad() * precio));
            total = total + subtotal;
            if (fd.getProducto().getIdImpuesto() == 2) {
                iva5 = iva5 + subtotal;
            } else if (fd.getProducto().getIdImpuesto() == 3) {
                iva10 = iva10 + subtotal;
            }
            DETALLE = DETALLE + "-> " + fd.getProducto().getDescripcion() + "\n" + fd.getCantidad() + " " + precio + "  " + subtotal + "\n";
        }
        iva5 = iva5 / 21;
        iva10 = iva10 / 11;
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n"
                + "liq. iva5% = " + iva5 + "\n"
                + "liq. iva10% = " + iva10 + "\n"
                + "liq. total = " + (iva5 + iva10) + "\n";
        String ticket = cabecera + datoVenta + COLUMNAS + DETALLE + SUMATOTAL + pie;
        byte[] bytes = ticket.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job;
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

    /*
    IMPRIMIR TICKETS
     */
    public static void imprimirTicketPedido(M_rol_usuario rol_usuario, M_pedido pedidoCabecera, List<E_facturaDetalle> pedidoDetalle) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String nombreImpresora = PREF_PRINT_TICKET.getNombreImpresora();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String ruc = "-";
        if (pedidoCabecera.getCliente().getRuc() != null) {
            if (pedidoCabecera.getCliente().getRucId() != null) {
                ruc = pedidoCabecera.getCliente().getRuc() + "-" + pedidoCabecera.getCliente().getRucId();
            } else {
                ruc = pedidoCabecera.getCliente().getRuc();
            }
        }
        String ventaCabecera = "Fecha y hora: " + fechaEntrega + "\n"
                + "Cajero: " + pedidoCabecera.getFuncionario().getNombre() + "\n"
                + "Cliente: " + pedidoCabecera.getCliente().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "Fecha pedido: " + sdfs.format(pedidoCabecera.getTiempoEntrega()) + "\n"
                + "---------------------------------\n";
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        double total = 0;
        double iva5 = 0;
        double iva10 = 0;
        for (E_facturaDetalle fd : pedidoDetalle) {
            double subtotal = fd.calcularSubTotal();
            total = total + subtotal;
            if (fd.getProducto().getIdImpuesto() == 2) {
                iva5 = iva5 + subtotal;
            } else if (fd.getProducto().getIdImpuesto() == 3) {
                iva10 = iva10 + subtotal;
            }
            DETALLE = DETALLE + "-> " + fd.getProducto().getDescripcion() + "\n" + fd.getCantidad() + " " + fd.getPrecioConDescuento() + "  " + subtotal + "\n";
        }
        iva5 = iva5 / 21;
        iva10 = iva10 / 11;
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n"
                + "liq. iva5% = " + iva5 + "\n"
                + "liq. iva10% = " + iva10 + "\n"
                + "liq. total = " + (iva5 + iva10) + "\n";
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
        byte[] bytes = ticket.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job;
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

    public static void imprimirTicketVentaGuardada(M_rol_usuario rol_usuario, M_facturaCabecera facturaCabecera) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String nombreImpresora = PREF_PRINT_TICKET.getNombreImpresora();
        String fechaEntrega = sdfs.format(facturaCabecera.getTiempo());
        String ruc = "-";
        if (facturaCabecera.getCliente().getRuc() != null) {
            if (facturaCabecera.getCliente().getRucId() != null) {
                ruc = facturaCabecera.getCliente().getRuc() + "-" + facturaCabecera.getCliente().getRucId();
            } else {
                ruc = facturaCabecera.getCliente().getRuc();
            }
        }
        String ventaCabecera = "Fecha y hora: " + fechaEntrega + "\n"
                + "Ticket nro.: " + facturaCabecera.getIdFacturaCabecera() + "\n"
                + "Cajero: " + facturaCabecera.getFuncionario().getNombre() + "\n"
                + "Cliente: " + facturaCabecera.getCliente().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "---------------------------------\n";
        ArrayList<E_facturaDetalle> facturaDetalle = DB_Ingreso.obtenerVentaDetalles(facturaCabecera.getIdFacturaCabecera());
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        int total = 0;
        int iva5 = 0;
        int iva10 = 0;
        for (E_facturaDetalle fd : facturaDetalle) {
            double precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
            int subtotal = Math.round(Math.round(fd.getCantidad() * precio));
            if (fd.getProducto().getIdImpuesto() == 2) {
                iva5 = iva5 + subtotal;
            } else if (fd.getProducto().getIdImpuesto() == 3) {
                iva10 = iva10 + subtotal;
            }
            //agregar observacion si es que tiene
            String prodDescripcion = fd.getProducto().getDescripcion();
            if (null != fd.getObservacion()) {
                if (!fd.getObservacion().isEmpty()) {
                    prodDescripcion = prodDescripcion + "-(" + fd.getObservacion() + ")";
                }
            }
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + prodDescripcion + "\n" + fd.getCantidad() + " " + precio + "  " + subtotal + "\n";
        }
        iva5 = iva5 / 21;
        iva10 = iva10 / 11;
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n"
                + "liq. iva5% = " + iva5 + "\n"
                + "liq. iva10% = " + iva10 + "\n"
                + "liq. total = " + (iva5 + iva10) + "\n";
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
        byte[] bytes = ticket.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job;
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

    public static void imprimirTicketVenta(M_rol_usuario rol_usuario, M_facturaCabecera facturaCabecera, ArrayList<E_facturaDetalle> facturaDetalle) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String nombreImpresora = PREF_PRINT_TICKET.getNombreImpresora();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String ruc = "-";
        if (facturaCabecera.getCliente().getRuc() != null) {
            if (facturaCabecera.getCliente().getRucId() != null) {
                ruc = facturaCabecera.getCliente().getRuc() + "-" + facturaCabecera.getCliente().getRucId();
            } else {
                ruc = facturaCabecera.getCliente().getRuc();
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
        double total = 0;
        double iva5 = 0;
        double iva10 = 0;
        for (E_facturaDetalle fd : facturaDetalle) {
            double subtotal = fd.calcularSubTotal();
            if (fd.getProducto().getIdImpuesto() == 2) {
                iva5 = iva5 + subtotal;
            } else if (fd.getProducto().getIdImpuesto() == 3) {
                iva10 = iva10 + subtotal;
            }
            //agregar observacion si es que tiene
            String prodDescripcion = fd.getProducto().getDescripcion();
            if (null != fd.getObservacion()) {
                if (!fd.getObservacion().isEmpty()) {
                    prodDescripcion = prodDescripcion + "-(" + fd.getObservacion() + ")";
                }
            }
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + prodDescripcion + "\n" + fd.getCantidad() + " " + fd.getPrecioConDescuento() + "  " + subtotal + "\n";
        }
        iva5 = iva5 / 21;
        iva10 = iva10 / 11;
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n"
                + "liq. iva5% = " + iva5 + "\n"
                + "liq. iva10% = " + iva10 + "\n"
                + "liq. total = " + (iva5 + iva10) + "\n";
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
        byte[] bytes = ticket.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job;
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

    public static void imprimirTicketMesa(M_mesa mesaCabecera, ArrayList<M_mesa_detalle> detalles) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String nombreImpresora = PREF_PRINT_TICKET.getNombreImpresora();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String ruc = "-";
        if (mesaCabecera.getCliente().getRuc() != null) {
            if (mesaCabecera.getCliente().getRucId() != null) {
                ruc = mesaCabecera.getCliente().getRuc() + "-" + mesaCabecera.getCliente().getRucId();
            } else {
                ruc = mesaCabecera.getCliente().getRuc();
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
        int iva5 = 0;
        int iva10 = 0;
        for (M_mesa_detalle md : detalles) {
            double precio = md.getPrecio() - Math.round(Math.round(((md.getPrecio() * md.getDescuento()) / 100)));
            int subtotal = Math.round(Math.round(md.getCantidad() * precio));
            if (md.getProducto().getIdImpuesto() == 2) {
                iva5 = iva5 + subtotal;
            } else if (md.getProducto().getIdImpuesto() == 3) {
                iva10 = iva10 + subtotal;
            }
            //agregar observacion si es que tiene
            String prodDescripcion = md.getProducto().getDescripcion();
            if (null != md.getObservacion()) {
                if (!md.getObservacion().isEmpty()) {
                    prodDescripcion = prodDescripcion + "-(" + md.getObservacion() + ")";
                }
            }
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + prodDescripcion + "\n" + md.getCantidad() + " " + precio + "  " + subtotal + "\n";
        }
        iva5 = iva5 / 21;
        iva10 = iva10 / 11;
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n"
                + "liq. iva5% = " + iva5 + "\n"
                + "liq. iva10% = " + iva10 + "\n"
                + "liq. total = " + (iva5 + iva10) + "\n";
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
        byte[] bytes = ticket.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job;
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

    public static void imprimirTicketVentaMesa(M_mesa mesaCabecera, ArrayList<M_mesa_detalle> detalles) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String nombreImpresora = PREF_PRINT_TICKET.getNombreImpresora();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String ruc = "-";
        if (mesaCabecera.getCliente().getRuc() != null) {
            if (mesaCabecera.getCliente().getRucId() != null) {
                ruc = mesaCabecera.getCliente().getRuc() + "-" + mesaCabecera.getCliente().getRucId();
            } else {
                ruc = mesaCabecera.getCliente().getRuc();
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
        int iva5 = 0;
        int iva10 = 0;
        for (M_mesa_detalle md : detalles) {
            double precio = md.getPrecio() - Math.round(Math.round(((md.getPrecio() * md.getDescuento()) / 100)));
            int subtotal = Math.round(Math.round(md.getCantidad() * precio));
            if (md.getProducto().getIdImpuesto() == 2) {
                iva5 = iva5 + subtotal;
            } else if (md.getProducto().getIdImpuesto() == 3) {
                iva10 = iva10 + subtotal;
            }
            //agregar observacion si es que tiene
            String prodDescripcion = md.getProducto().getDescripcion();
            if (null != md.getObservacion()) {
                if (!md.getObservacion().isEmpty()) {
                    prodDescripcion = prodDescripcion + "-(" + md.getObservacion() + ")";
                }
            }
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + prodDescripcion + "\n" + md.getCantidad() + " " + precio + "  " + subtotal + "\n";
        }
        iva5 = iva5 / 21;
        iva10 = iva10 / 11;
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n"
                + "liq. iva5% = " + iva5 + "\n"
                + "liq. iva10% = " + iva10 + "\n"
                + "liq. total = " + (iva5 + iva10) + "\n";
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
        byte[] bytes = ticket.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job;
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

    public static void imprimirTicketCaja(Caja caja, int efectivoDepositado) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String nombreImpresora = PREF_PRINT_TICKET.getNombreImpresora();
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
        int ingresoContado = DB_Ingreso.obtenerTotalIngreso(ini, fi, 1, Estado.ACTIVO);
        int ingresoCretdito = DB_Ingreso.obtenerTotalIngreso(ini, fi, 2, Estado.ACTIVO);
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
        DocPrintJob job;
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

    public static void imprimirTicketCompra(M_egresoCabecera facturaCabecera, ArrayList<M_egreso_detalle> egresoDetalle) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String nombreImpresora = PREF_PRINT_TICKET.getNombreImpresora();
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        String ruc = "-";
        if (facturaCabecera.getProveedor().getRuc() != null) {
            if (facturaCabecera.getProveedor().getRuc_id() != null) {
                ruc = facturaCabecera.getProveedor().getRuc() + "-" + facturaCabecera.getProveedor().getRuc_id();
            } else {
                ruc = facturaCabecera.getProveedor().getRuc();
            }
        }
        String ventaCabecera = "Fecha y hora: " + fechaEntrega + "\n"
                + "Compra nro.: " + facturaCabecera.getId_cabecera() + "\n"
                + "Cajero: " + facturaCabecera.getFuncionario().getNombre() + "\n"
                + "Proveedor: " + facturaCabecera.getProveedor().getEntidad() + "\n"
                + "R.U.C.: " + ruc + "\n"
                + "---------------------------------\n";
        String COLUMNAS = "producto   cant  precio  subtotal\n";
        String DETALLE = "";
        int total = 0;
        int iva5 = 0;
        int iva10 = 0;
        for (M_egreso_detalle fd : egresoDetalle) {
            double precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
            int subtotal = Math.round(Math.round(fd.getCantidad() * precio));
            if (fd.getProducto().getIdImpuesto() == 2) {
                iva5 = iva5 + subtotal;
            } else if (fd.getProducto().getIdImpuesto() == 3) {
                iva10 = iva10 + subtotal;
            }
            //agregar observacion si es que tiene
            String prodDescripcion = fd.getProducto().getDescripcion();
            if (null != fd.getObservacion()) {
                if (!fd.getObservacion().isEmpty()) {
                    prodDescripcion = prodDescripcion + "-(" + fd.getObservacion() + ")";
                }
            }
            total = total + subtotal;
            DETALLE = DETALLE + "-> " + prodDescripcion + "\n" + fd.getCantidad() + " " + precio + "  " + subtotal + "\n";
        }
        iva5 = iva5 / 21;
        iva10 = iva10 / 11;
        String SUMATOTAL = "---------------------------------\n"
                + "Total= " + total + "\n"
                + "liq. iva5% = " + iva5 + "\n"
                + "liq. iva10% = " + iva10 + "\n"
                + "liq. total = " + (iva5 + iva10) + "\n";
        String cabecera = PREF_PRINT_TICKET.getCabecera();
        String pie = PREF_PRINT_TICKET.getPie();
        String ticket = cabecera + ventaCabecera + COLUMNAS + DETALLE + SUMATOTAL + pie;
        byte[] bytes = ticket.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job;
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

    /*
    IMPRIMIR FACTURA
     */
    public static void imprimirFacturaVenta(final M_facturaCabecera facturaCabecera, final ArrayList<E_facturaDetalle> facturaDetalle) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String nombreImpresora = PREF_PRINT_FACTURA.getNombreImpresora();
        int width = PREF_PRINT_FACTURA.getAnchoPagina();
        int height = PREF_PRINT_FACTURA.getLargoPagina();
        final List<M_campoImpresion> textoAImprimir = DB_manager.obtenerCampoImpresion(2, MyConstants.ACTIVO);

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        String nombreDoc = facturaCabecera.getCliente().getEntidad() + " " + facturaCabecera.getTiempo();
        //aset.add(MediaSizeName.ISO_A4);
        //aset.add(new PrinterResolution(300, 300, PrinterResolution.DPI));
        //aset.add(new MediaPrintableArea(2, 2, 210 - 4, 297 - 4, MediaPrintableArea.MM));
        aset.add(new JobName(nombreDoc, null));
        DocFlavor doc_flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_UTF_8;
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();
        switch (PREF_PRINT_FACTURA.getOrientacion().getId()) {
            case E_impresionOrientacion.PORTRAIT: {
                aset.add(OrientationRequested.PORTRAIT);
                paper.setSize(width, height);
                paper.setImageableArea(PREF_PRINT_FACTURA.getMargenX(), PREF_PRINT_FACTURA.getMargenY(), width, height);
                pageFormat.setOrientation(PageFormat.PORTRAIT);
                break;
            }
            case E_impresionOrientacion.LANDSCAPE: {
                aset.add(OrientationRequested.LANDSCAPE);
                paper.setSize(height, width);
                paper.setImageableArea(PREF_PRINT_FACTURA.getMargenX(), PREF_PRINT_FACTURA.getMargenY(), height, width);
                pageFormat.setOrientation(PageFormat.LANDSCAPE);
                break;
            }
            case E_impresionOrientacion.REVERSE_LANDSCAPE: {
                aset.add(OrientationRequested.LANDSCAPE);
                paper.setSize(height, width);
                paper.setImageableArea(PREF_PRINT_FACTURA.getMargenX(), PREF_PRINT_FACTURA.getMargenY(), height, width);
                pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
                break;
            }
        }
        pageFormat.setPaper(paper);
        VentaPrintable vp = new VentaPrintable(PREF_PRINT_FACTURA, facturaCabecera, facturaDetalle, textoAImprimir);
        job.setPrintable(vp, pageFormat);
        boolean existeImpresora = false;
        if (services.length > 0) {
            for (PrintService service : services) {
                if (service.getName().equals(nombreImpresora)) {
                    existeImpresora = true;
                    try {
                        if (job != null) {
                            job.setPrintService(service);
                            job.print(aset);
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (PrinterException ex) {
                        JOptionPane.showMessageDialog(null, "Ocurrio un error al tratar de imprimir la factura. Intente nuevamente.", "Error", JOptionPane.INFORMATION_MESSAGE);
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

    /*
    IMPRIMIR BOLETA
     */
    public static void imprimirBoletaVenta(M_facturaCabecera facturaCabecera, ArrayList<E_facturaDetalle> facturaDetalle) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrinterJob job = PrinterJob.getPrinterJob();
        String nombreImpresora = PREF_PRINT_BOLETA.getNombreImpresora();
        int width = PREF_PRINT_BOLETA.getAnchoPagina();
        int height = PREF_PRINT_BOLETA.getLargoPagina();
        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();
        paper.setSize(width, height);
        paper.setImageableArea(PREF_PRINT_BOLETA.getMargenX(), PREF_PRINT_BOLETA.getMargenY(), width, height);
        BoletaPrintable vp = new BoletaPrintable(PREF_PRINT_BOLETA, facturaCabecera, facturaDetalle);
        pageFormat.setPaper(paper);
        job.setPrintable(vp, pageFormat);
        boolean existeImpresora = false;
        if (services.length > 0) {
            for (PrintService service : services) {
                if (service.getName().equals(nombreImpresora)) {
                    existeImpresora = true;
                    try {
                        if (job != null) {
                            job.setPrintService(service);
                            job.print();
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (PrinterException ex) {
                        JOptionPane.showMessageDialog(null, "Ocurrio un error al tratar de imprimir la boleta. Intente nuevamente.", "Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                }
            }
        }
        if (!existeImpresora) {
            JOptionPane.showMessageDialog(null, "No se encontró la impresora " + nombreImpresora, "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void imprimirBoletaCompra(M_egresoCabecera facturaCabecera, ArrayList<M_egreso_detalle> facturaDetalle) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrinterJob job = PrinterJob.getPrinterJob();
        String nombreImpresora = PREF_PRINT_BOLETA.getNombreImpresora();
        int width = PREF_PRINT_BOLETA.getAnchoPagina();
        int height = PREF_PRINT_BOLETA.getLargoPagina();
        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();
        paper.setSize(width, height);
        paper.setImageableArea(PREF_PRINT_BOLETA.getMargenX(), PREF_PRINT_BOLETA.getMargenY(), width, height);
        CompraBoletaPrintable vp = new CompraBoletaPrintable(PREF_PRINT_BOLETA, facturaCabecera, facturaDetalle);
        pageFormat.setPaper(paper);
        job.setPrintable(vp, pageFormat);
        boolean existeImpresora = false;
        if (services.length > 0) {
            for (PrintService service : services) {
                if (service.getName().equals(nombreImpresora)) {
                    existeImpresora = true;
                    try {
                        if (job != null) {
                            job.setPrintService(service);
                            job.print();
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo imprimir", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (PrinterException ex) {
                        JOptionPane.showMessageDialog(null, "Ocurrio un error al tratar de imprimir la boleta. Intente nuevamente.", "Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                }
            }
        }
        if (!existeImpresora) {
            JOptionPane.showMessageDialog(null, "No se encontró la impresora " + nombreImpresora, "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
