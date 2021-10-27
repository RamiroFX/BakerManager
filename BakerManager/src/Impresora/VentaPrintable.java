/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Impresora;

import DB.DB_Cliente;
import Entities.E_facturaDetalle;
import Entities.E_impresionOrientacion;
import Entities.M_campoImpresion;
import Entities.M_facturaCabecera;
import Entities.M_preferenciasImpresion;
import Entities.M_telefono;
import Parametros.TipoOperacion;
import Utilities.MyConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.bolivia.qulqi.Qulqi;

/**
 *
 * @author Ramiro
 */
public class VentaPrintable implements Printable {

    List<M_campoImpresion> campoImpresiones;
    M_facturaCabecera facturaCabecera;
    ArrayList<E_facturaDetalle> facturaDetalle;
    M_preferenciasImpresion preferencia;
    DecimalFormat decimalFormat;

    public VentaPrintable(M_preferenciasImpresion preferencia, M_facturaCabecera facturaCabecera, ArrayList<E_facturaDetalle> facturaDetalle, List<M_campoImpresion> impresions) {
        this.preferencia = preferencia;
        this.facturaCabecera = facturaCabecera;
        this.facturaDetalle = facturaDetalle;
        this.campoImpresiones = impresions;
        this.decimalFormat = new DecimalFormat("###,###.###");
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        Graphics2D g2d = (Graphics2D) g;
        //g2d.translate(pf.getImageableX(), pf.getImageableY());
        g2d.setColor(Color.black);
        final int espaciadorY = 10;
        SimpleDateFormat dateFormat = new SimpleDateFormat(preferencia.getFormatoFecha());
        Date fecha = Calendar.getInstance().getTime();
        fecha.setTime(facturaCabecera.getTiempo().getTime());
        final String fechaEntrega = dateFormat.format(fecha);
        final String[] fechas = fechaEntrega.split("/");
        String rucAux = "";
        if (facturaCabecera.getCliente().getRuc() != null) {
            if (facturaCabecera.getCliente().getRucId() != null) {
                rucAux = facturaCabecera.getCliente().getRuc() + "-" + facturaCabecera.getCliente().getRucId();
            } else {
                rucAux = facturaCabecera.getCliente().getRuc();
            }
        }
        final String ruc = rucAux;
        g.setFont(new Font(preferencia.getLetterFont(), Font.PLAIN, preferencia.getLetterSize()));
        Integer exenta = 0;
        Integer iva5 = 0;
        Integer iva10 = 0;
        Integer liquidacionIva5 = 0;
        Integer liquidacionIva10 = 0;
        if (pi == 0) {
            for (M_campoImpresion object : campoImpresiones) {
                int posY = object.getY().intValue();
                int posX = object.getX().intValue();
                int duplicadoDistY = 0;
                int duplicadoDistX = 0;
                int triplicadoDistY = 0;
                int triplicadoDistX = 0;
                switch (preferencia.getOrientacion().getId()) {
                    case E_impresionOrientacion.PORTRAIT: {
                        duplicadoDistY = posY + preferencia.getDistanceBetweenCopies();
                        duplicadoDistX = posX;
                        triplicadoDistY = posY + preferencia.getDistanceForTriplicate();
                        triplicadoDistX = posX;
                        break;
                    }
                    case E_impresionOrientacion.LANDSCAPE: {
                        duplicadoDistY = posY;
                        duplicadoDistX = posX + preferencia.getDistanceBetweenCopies();
                        triplicadoDistY = posY;
                        triplicadoDistX = posX + preferencia.getDistanceForTriplicate();
                        break;
                    }
                    case E_impresionOrientacion.REVERSE_LANDSCAPE: {
                        duplicadoDistY = posY;
                        duplicadoDistX = posX + preferencia.getDistanceBetweenCopies();
                        triplicadoDistY = posY;
                        triplicadoDistX = posX + preferencia.getDistanceForTriplicate();
                        break;
                    }
                }
                if (object.getCampo().equals(MyConstants.DATE_FULL)) {
                    g.drawString(fechaEntrega, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(fechaEntrega, duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(fechaEntrega, triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.DATE_DAY)) {
                    g.drawString(fechas[0], posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(fechas[0], duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(fechas[0], triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.DATE_MONTH)) {
                    g.drawString(fechas[1], posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(fechas[1], duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(fechas[1], triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.DATE_YEAR_MIN)) {
                    g.drawString(fechas[2], posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(fechas[2], duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(fechas[2], triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.DATE_YEAR_FULL)) {
                    g.drawString(fechas[2], posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(fechas[2], duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(fechas[2], triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.NRO_FACTURA)) {
                    String nroFactura = facturaCabecera.getNroFactura() + "";
                    g.drawString(nroFactura, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(nroFactura, duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(nroFactura, triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.TIOP_CONTADO)) {
                    if (facturaCabecera.getIdCondVenta() == TipoOperacion.CONTADO) {
                        g.drawString("X", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            g.drawString("X", duplicadoDistX, duplicadoDistY);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            g.drawString("X", triplicadoDistX, triplicadoDistY);
                        }
                    }
                }
                if (object.getCampo().equals(MyConstants.TIOP_CREDITO)) {
                    if (facturaCabecera.getIdCondVenta() == TipoOperacion.CREDITO) {
                        g.drawString("X", posX, posY);
                        g.drawString("30 días", posX - 10, posY + 10);
                        if (preferencia.getIdDuplicado() == 1) {
                            g.drawString("X", duplicadoDistX, duplicadoDistY);
                            g.drawString("30 días", duplicadoDistX - 10, duplicadoDistY + 10);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            g.drawString("X", triplicadoDistX, triplicadoDistY);
                        }
                    }
                }
                if (object.getCampo().equals(MyConstants.RS)) {
                    g.drawString(facturaCabecera.getCliente().getEntidad(), posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(facturaCabecera.getCliente().getEntidad(), duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(facturaCabecera.getCliente().getEntidad(), triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.RUC)) {
                    g.drawString(ruc, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(ruc, duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(ruc, triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.DIR)) {
                    String dir = "";
                    if (facturaCabecera.getCliente().getDireccion() != null) {
                        dir = facturaCabecera.getCliente().getDireccion();
                    }
                    g.drawString(dir, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(dir, duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(dir, triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.TELEFONO)) {
                    String stringTelefono = "";
                    ArrayList<M_telefono> telefono = DB_Cliente.obtenerTelefonoCliente(facturaCabecera.getCliente().getIdCliente());
                    if (!telefono.isEmpty()) {
                        stringTelefono = telefono.get(0).getNumero();
                    }
                    g.drawString(stringTelefono, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(stringTelefono, duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(stringTelefono, triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.REMISION)) {
                    String remision = "";
                    if (facturaCabecera.getIdNotaRemision() != null) {
                        remision = facturaCabecera.getIdNotaRemision() + "";
                    }
                    g.drawString(remision, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(remision, duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(remision, triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.VENDEDOR)) {
                    String vendedor = "";
                    if (facturaCabecera.getVendedor().getNombreCompleto() != null) {
                        vendedor = facturaCabecera.getVendedor().getNombreCompleto() + "";
                    }
                    g.drawString(vendedor, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(vendedor, duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(vendedor, triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_CANT)) {
                    for (E_facturaDetalle fd : facturaDetalle) {
                        g.drawString(decimalFormat.format(fd.getCantidad()) + "", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            g.drawString(decimalFormat.format(fd.getCantidad()) + "", duplicadoDistX, duplicadoDistY);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            g.drawString(decimalFormat.format(fd.getCantidad()) + "", triplicadoDistX, triplicadoDistY);
                        }
                        posY = posY + espaciadorY;
                        duplicadoDistY = duplicadoDistY + espaciadorY;
                        triplicadoDistY = triplicadoDistY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_COD)) {
                    for (E_facturaDetalle fd : facturaDetalle) {
                        g.drawString(fd.getProducto().getCodBarra() + "", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            g.drawString(fd.getProducto().getCodBarra() + "", duplicadoDistX, duplicadoDistY);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            g.drawString(fd.getProducto().getCodBarra() + "", triplicadoDistX, triplicadoDistY);
                        }
                        posY = posY + espaciadorY;
                        duplicadoDistY = duplicadoDistY + espaciadorY;
                        triplicadoDistY = triplicadoDistY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_PROD)) {
                    for (E_facturaDetalle fd : facturaDetalle) {
                        g.drawString(fd.getProducto().getDescripcion() + "", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            g.drawString(fd.getProducto().getDescripcion() + "", duplicadoDistX, duplicadoDistY);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            g.drawString(fd.getProducto().getDescripcion() + "", triplicadoDistX, triplicadoDistY);
                        }
                        posY = posY + espaciadorY;
                        duplicadoDistY = duplicadoDistY + espaciadorY;
                        triplicadoDistY = triplicadoDistY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_PRECIO)) {
                    for (E_facturaDetalle fd : facturaDetalle) {
                        double precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                        g.drawString(decimalFormat.format(precio) + "", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            g.drawString(decimalFormat.format(precio) + "", duplicadoDistX, duplicadoDistY);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            g.drawString(decimalFormat.format(precio) + "", triplicadoDistX, triplicadoDistY);
                        }
                        posY = posY + espaciadorY;
                        duplicadoDistY = duplicadoDistY + espaciadorY;
                        triplicadoDistY = triplicadoDistY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_DESC)) {
                    for (E_facturaDetalle fd : facturaDetalle) {
                        double descuento = fd.getDescuento();
                        g.drawString(descuento + "", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            g.drawString(descuento + "", duplicadoDistX, duplicadoDistY);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            g.drawString(descuento + "", triplicadoDistX, triplicadoDistY);
                        }
                        posY = posY + espaciadorY;
                        duplicadoDistY = duplicadoDistY + espaciadorY;
                        triplicadoDistY = triplicadoDistY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_EXENTA)) {
                    for (E_facturaDetalle fd : facturaDetalle) {
                        if (fd.getProducto().getIdImpuesto() == 1) {
                            double precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                            int subtotal = Math.round(Math.round(fd.getCantidad() * precio));
                            g.drawString(decimalFormat.format(subtotal) + "", posX, posY);
                            if (preferencia.getIdDuplicado() == 1) {
                                g.drawString(decimalFormat.format(subtotal) + "", duplicadoDistX, duplicadoDistY);
                            }
                            if (preferencia.getIdTriplicado() == 1) {
                                g.drawString(decimalFormat.format(subtotal) + "", triplicadoDistX, triplicadoDistY);
                            }
                            exenta = exenta + subtotal;
                        }
                        posY = posY + espaciadorY;
                        duplicadoDistY = duplicadoDistY + espaciadorY;
                        triplicadoDistY = triplicadoDistY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_IVA5)) {
                    for (E_facturaDetalle fd : facturaDetalle) {
                        if (fd.getProducto().getIdImpuesto() == 2) {
                            double precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                            int subtotal = Math.round(Math.round(fd.getCantidad() * precio));
                            g.drawString(decimalFormat.format(subtotal) + "", posX, posY);
                            if (preferencia.getIdDuplicado() == 1) {
                                g.drawString(decimalFormat.format(subtotal) + "", duplicadoDistX, duplicadoDistY);
                            }
                            if (preferencia.getIdTriplicado() == 1) {
                                g.drawString(decimalFormat.format(subtotal) + "", triplicadoDistX, triplicadoDistY);
                            }
                            iva5 = iva5 + subtotal;
                        }
                        posY = posY + espaciadorY;
                        duplicadoDistY = duplicadoDistY + espaciadorY;
                        triplicadoDistY = triplicadoDistY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_IVA10)) {
                    for (E_facturaDetalle fd : facturaDetalle) {
                        if (fd.getProducto().getIdImpuesto() == 3) {
                            double precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                            int subtotal = Math.round(Math.round(fd.getCantidad() * precio));
                            g.drawString(decimalFormat.format(subtotal) + "", posX, posY);
                            if (preferencia.getIdDuplicado() == 1) {
                                g.drawString(decimalFormat.format(subtotal) + "", duplicadoDistX, duplicadoDistY);
                            }
                            if (preferencia.getIdTriplicado() == 1) {
                                g.drawString(decimalFormat.format(subtotal) + "", triplicadoDistX, triplicadoDistY);
                            }
                            iva10 = iva10 + subtotal;
                        }
                        posY = posY + espaciadorY;
                        duplicadoDistY = duplicadoDistY + espaciadorY;
                        triplicadoDistY = triplicadoDistY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.SUB_TOTAL_EXENTA)) {
                    g.drawString(decimalFormat.format(exenta) + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(decimalFormat.format(exenta) + "", duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(decimalFormat.format(exenta) + "", triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.SUB_TOTAL_IVA5)) {
                    g.drawString(decimalFormat.format(iva5) + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(decimalFormat.format(iva5) + "", duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(decimalFormat.format(iva5) + "", triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.SUB_TOTAL_IVA10)) {
                    g.drawString(decimalFormat.format(iva10) + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(decimalFormat.format(iva10) + "", duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(decimalFormat.format(iva10) + "", triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.TOTAL_LETRA)) {
                    //Calcular el total
                    int total_letra = 0;
                    for (E_facturaDetalle fd : facturaDetalle) {
                        int subtotal = Math.round(Math.round(fd.getCantidad() * fd.getPrecio()));
                        total_letra = total_letra + subtotal;
                    }
                    Qulqi qulqi = new Qulqi();
                    qulqi.setDecimalPartVisible(false);
                    String totalLetra = qulqi.showMeTheMoney(total_letra + "");
                    if (preferencia.getImprimirMoneda() == 1) {
                        totalLetra = totalLetra + preferencia.getDivisa().getDescripcion();
                    }
                    g.drawString(totalLetra + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(totalLetra + "", duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(totalLetra + "", triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.TOTAL_NUMERO)) {
                    //Calcular el total
                    int total = 0;
                    for (E_facturaDetalle fd : facturaDetalle) {
                        int subtotal = Math.round(Math.round(fd.getCantidad() * fd.getPrecio()));
                        total = total + subtotal;
                    }
                    g.drawString(decimalFormat.format(total) + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(decimalFormat.format(total) + "", duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(decimalFormat.format(total) + "", triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.LIQUIDACION_IVA5)) {
                    //Calcular liquidacion de iva5 si es que hay productos con impuesto de 5%
                    if (iva5 > 0) {
                        liquidacionIva5 = iva5 / 21;
                    }
                    g.drawString(decimalFormat.format(liquidacionIva5) + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(decimalFormat.format(liquidacionIva5) + "", duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(decimalFormat.format(liquidacionIva5) + "", triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.LIQUIDACION_IVA10)) {
                    //Calcular liquidacion de iva5 si es que hay productos con impuesto de 10%
                    if (iva10 > 0) {
                        liquidacionIva10 = iva10 / 11;
                    }
                    g.drawString(decimalFormat.format(liquidacionIva10) + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(decimalFormat.format(liquidacionIva10) + "", duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(decimalFormat.format(liquidacionIva10) + "", triplicadoDistX, triplicadoDistY);
                    }
                }
                if (object.getCampo().equals(MyConstants.LIQUIDACION_TOTAL)) {
                    int liquidTotal = liquidacionIva5 + liquidacionIva10;
                    g.drawString(decimalFormat.format(liquidTotal) + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        g.drawString(decimalFormat.format(liquidTotal) + "", duplicadoDistX, duplicadoDistY);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        g.drawString(decimalFormat.format(liquidTotal) + "", triplicadoDistX, triplicadoDistY);
                    }
                }
            }
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }

}
