/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Impresora;

import Entities.M_campoImpresion;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_preferenciasImpresion;
import Parametros.TipoOperacion;
import Utilities.MyConstants;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
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
public class BoletaPrintable implements Printable {

    List<M_campoImpresion> campoImpresiones;
    M_facturaCabecera facturaCabecera;
    ArrayList<M_facturaDetalle> facturaDetalle;
    M_preferenciasImpresion preferencia;

    public BoletaPrintable(M_preferenciasImpresion preferencia, M_facturaCabecera facturaCabecera, ArrayList<M_facturaDetalle> facturaDetalle, List<M_campoImpresion> impresions) {
        this.preferencia = preferencia;
        this.facturaCabecera = facturaCabecera;
        this.facturaDetalle = facturaDetalle;
        this.campoImpresiones = impresions;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
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
            }
        }
        final String ruc = rucAux;
        g.setFont(new Font(preferencia.getLetterFont(), Font.PLAIN, preferencia.getLetterSize()));
        int ancho = preferencia.getAnchoPagina();
        g.drawRoundRect(10, 10, ancho-20, 130, 30, 30);//CABECERA
        g.drawRoundRect(10, 150, ancho-20, 115, 30, 30);//DETALLE
        g.drawRoundRect(10, 270, ancho-20, 15, 30, 30);//SUBTOTAL
        g.drawRoundRect(10, 290, ancho-20, 15, 30, 30);//TOTAL
        g.drawRoundRect(10, 310, ancho-20, 15, 30, 30);//LIQUIDACION
        Integer exenta = 0;
        Integer iva5 = 0;
        Integer iva10 = 0;
        Integer liquidacionIva5 = 0;
        Integer liquidacionIva10 = 0;
        if (pi == 0) {
            for (M_campoImpresion object : campoImpresiones) {
                int posY = object.getY().intValue();
                int posX = object.getX().intValue();
                if (object.getCampo().equals(MyConstants.DATE_FULL)) {
                    g.drawString(fechaEntrega, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(fechaEntrega, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(fechaEntrega, posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.DATE_DAY)) {
                    g.drawString(fechas[0], posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(fechas[0], posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(fechas[0], posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.DATE_MONTH)) {
                    g.drawString(fechas[1], posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(fechas[1], posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(fechas[1], posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.DATE_YEAR_MIN)) {
                    g.drawString(fechas[2], posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(fechas[2], posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(fechas[2], posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.DATE_YEAR_FULL)) {
                    g.drawString(fechas[2], posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(fechas[2], posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(fechas[2], posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.NRO_FACTURA)) {
                    String nroFactura = facturaCabecera.getNroFactura() + "";
                    g.drawString(nroFactura, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(nroFactura, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(nroFactura, posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.TIOP_CONTADO)) {
                    if (facturaCabecera.getIdCondVenta() == TipoOperacion.CONTADO) {
                        g.drawString("X", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                            g.drawString("X", posX, duplicadoDist);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                            g.drawString("X", posX, triplicadoDist);
                        }
                    }
                }
                if (object.getCampo().equals(MyConstants.TIOP_CREDITO)) {
                    if (facturaCabecera.getIdCondVenta() == TipoOperacion.CREDITO) {
                        g.drawString("X", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                            g.drawString("X", posX, duplicadoDist);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                            g.drawString("X", posX, triplicadoDist);
                        }
                    }
                }
                if (object.getCampo().equals(MyConstants.RS)) {
                    g.drawString(facturaCabecera.getCliente().getEntidad(), posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(facturaCabecera.getCliente().getEntidad(), posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(facturaCabecera.getCliente().getEntidad(), posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.RUC)) {
                    g.drawString(ruc, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(ruc, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(ruc, posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.DIR)) {
                    String dir = "";
                    if (facturaCabecera.getCliente().getDireccion() != null) {
                        dir = facturaCabecera.getCliente().getDireccion();
                    }
                    g.drawString(dir, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(dir, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(dir, posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.REMISION)) {
                    String remision = "";
                    if (facturaCabecera.getIdNotaRemision() != null) {
                        remision = facturaCabecera.getIdNotaRemision() + "";
                    }
                    g.drawString(remision, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(remision, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(remision, posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_CANT)) {
                    for (M_facturaDetalle fd : facturaDetalle) {
                        g.drawString(fd.getCantidad() + "", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                            g.drawString(fd.getCantidad() + "", posX, duplicadoDist);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                            g.drawString(fd.getCantidad() + "", posX, triplicadoDist);
                        }
                        posY = posY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_COD)) {
                    for (M_facturaDetalle fd : facturaDetalle) {
                        g.drawString(fd.getProducto().getCodBarra() + "", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                            g.drawString(fd.getProducto().getCodBarra() + "", posX, duplicadoDist);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                            g.drawString(fd.getProducto().getCodBarra() + "", posX, triplicadoDist);
                        }
                        posY = posY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_PROD)) {
                    for (M_facturaDetalle fd : facturaDetalle) {
                        g.drawString(fd.getProducto().getDescripcion() + "", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                            g.drawString(fd.getProducto().getDescripcion() + "", posX, duplicadoDist);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                            g.drawString(fd.getProducto().getDescripcion() + "", posX, triplicadoDist);
                        }
                        posY = posY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_PRECIO)) {
                    for (M_facturaDetalle fd : facturaDetalle) {
                        Integer precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                        //int precio = fd.getPrecio();
                        g.drawString(precio + "", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                            g.drawString(precio + "", posX, duplicadoDist);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                            g.drawString(precio + "", posX, triplicadoDist);
                        }
                        posY = posY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_DESC)) {
                    for (M_facturaDetalle fd : facturaDetalle) {
                        double descuento = fd.getDescuento();
                        g.drawString(descuento + "", posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                            g.drawString(descuento + "", posX, duplicadoDist);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                            g.drawString(descuento + "", posX, triplicadoDist);
                        }
                        posY = posY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.DETAIL_EXENTA)) {
                    for (M_facturaDetalle fd : facturaDetalle) {
                        if (fd.getProducto().getIdImpuesto() == 1) {
                            Integer precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                            int subtotal = Math.round(Math.round(fd.getCantidad() * precio));
                            g.drawString(subtotal + "", posX, posY);
                            if (preferencia.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                                g.drawString(subtotal + "", posX, duplicadoDist);
                            }
                            if (preferencia.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
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
                            Integer precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                            int subtotal = Math.round(Math.round(fd.getCantidad() * precio));
                            g.drawString(subtotal + "", posX, posY);
                            if (preferencia.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                                g.drawString(subtotal + "", posX, duplicadoDist);
                            }
                            if (preferencia.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
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
                            Integer precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                            int subtotal = Math.round(Math.round(fd.getCantidad() * precio));
                            g.drawString(subtotal + "", posX, posY);
                            if (preferencia.getIdDuplicado() == 1) {
                                int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                                g.drawString(subtotal + "", posX, duplicadoDist);
                            }
                            if (preferencia.getIdTriplicado() == 1) {
                                int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                                g.drawString(subtotal + "", posX, triplicadoDist);
                            }
                            iva10 = iva10 + subtotal;
                        }
                        posY = posY + espaciadorY;
                    }
                }
                if (object.getCampo().equals(MyConstants.SUB_TOTAL_EXENTA)) {
                    g.drawString(exenta + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(exenta + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(exenta + "", posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.SUB_TOTAL_IVA5)) {
                    g.drawString(iva5 + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(iva5 + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(iva5 + "", posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.SUB_TOTAL_IVA10)) {
                    g.drawString(iva10 + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(iva10 + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
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
                    if (preferencia.getImprimirMoneda() == 1) {
                        totalLetra = totalLetra + preferencia.getDivisa().getDescripcion();
                    }
                    g.drawString(totalLetra + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(totalLetra + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
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
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(total + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(total + "", posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.LIQUIDACION_IVA5)) {
                    //Calcular liquidacion de iva5 si es que hay productos con impuesto de 5%
                    if (iva5 > 0) {
                        liquidacionIva5 = iva5 / 21;
                    }
                    g.drawString(liquidacionIva5 + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(liquidacionIva5 + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(liquidacionIva5 + "", posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.LIQUIDACION_IVA10)) {
                    //Calcular liquidacion de iva5 si es que hay productos con impuesto de 10%
                    if (iva10 > 0) {
                        liquidacionIva10 = iva10 / 11;
                    }
                    g.drawString(liquidacionIva10 + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(liquidacionIva10 + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(liquidacionIva10 + "", posX, triplicadoDist);
                    }
                }
                if (object.getCampo().equals(MyConstants.LIQUIDACION_TOTAL)) {
                    int liquidTotal = liquidacionIva5 + liquidacionIva10;
                    g.drawString(liquidTotal + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(liquidTotal + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(liquidTotal + "", posX, triplicadoDist);
                    }
                }
            }
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }

}
