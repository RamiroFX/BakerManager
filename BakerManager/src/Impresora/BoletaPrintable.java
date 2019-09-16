/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Impresora;

import Entities.E_Empresa;
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

    E_Empresa empresa;
    List<M_campoImpresion> campoImpresiones;
    M_facturaCabecera facturaCabecera;
    ArrayList<M_facturaDetalle> facturaDetalle;
    M_preferenciasImpresion preferencia;

    public BoletaPrintable(M_preferenciasImpresion preferencia, M_facturaCabecera facturaCabecera, ArrayList<M_facturaDetalle> facturaDetalle, List<M_campoImpresion> impresions, E_Empresa empresa) {
        this.preferencia = preferencia;
        this.facturaCabecera = facturaCabecera;
        this.facturaDetalle = facturaDetalle;
        this.campoImpresiones = impresions;
        this.empresa = empresa;
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
        int espacio = 20;
        int ancho = preferencia.getAnchoPagina();
        int alturaCabecera = 130;
        int alturaDetalle = 130;
        int alturaSubtotal = 20;
        int alturaTotal = 20;
        int alturaLiquidacion = 20;
        g.drawRoundRect(10, 10, ancho - 20, alturaCabecera, 0, 0);//CABECERA
        g.drawRoundRect(10, alturaCabecera + espacio, ancho - 20, alturaDetalle, 0, 0);//DETALLE
        g.drawRoundRect(10, alturaCabecera + alturaDetalle + espacio, ancho - 20, alturaSubtotal, 0, 0);//SUBTOTAL
        g.drawRoundRect(10, alturaCabecera + alturaDetalle + alturaSubtotal + espacio, ancho - 20, alturaTotal, 0, 0);//TOTAL
        g.drawRoundRect(10, alturaCabecera + alturaDetalle + alturaSubtotal + alturaTotal + espacio, ancho - 20, alturaLiquidacion, 0, 0);//LIQUIDACION
        if (preferencia.getIdDuplicado() == 1) {
            int duplicadoDist = preferencia.getDistanceBetweenCopies();
            g.drawRoundRect(10, duplicadoDist + 10, ancho - 20, alturaCabecera, 0, 0);//CABECERA
            g.drawRoundRect(10, duplicadoDist + alturaCabecera + espacio, ancho - 20, alturaDetalle, 0, 0);//DETALLE
            g.drawRoundRect(10, duplicadoDist + alturaCabecera + alturaDetalle + espacio, ancho - 20, alturaSubtotal, 0, 0);//SUBTOTAL
            g.drawRoundRect(10, duplicadoDist + alturaCabecera + alturaDetalle + alturaSubtotal + espacio, ancho - 20, alturaTotal, 0, 0);//TOTAL
            g.drawRoundRect(10, duplicadoDist + alturaCabecera + alturaDetalle + alturaSubtotal + alturaTotal + espacio, ancho - 20, alturaLiquidacion, 0, 0);//LIQUIDACION
        }
        if (preferencia.getIdTriplicado() == 1) {
            int triplicadoDist = preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
            g.drawRoundRect(10, triplicadoDist + 10, ancho - 20, alturaCabecera, 0, 0);//CABECERA
            g.drawRoundRect(10, triplicadoDist + alturaCabecera + espacio, ancho - 20, alturaDetalle, 0, 0);//DETALLE
            g.drawRoundRect(10, triplicadoDist + alturaCabecera + alturaDetalle + espacio, ancho - 20, alturaSubtotal, 0, 0);//SUBTOTAL
            g.drawRoundRect(10, triplicadoDist + alturaCabecera + alturaDetalle + alturaSubtotal + espacio, ancho - 20, alturaTotal, 0, 0);//TOTAL
            g.drawRoundRect(10, triplicadoDist + alturaCabecera + alturaDetalle + alturaSubtotal + alturaTotal + espacio, ancho - 20, alturaLiquidacion, 0, 0);//LIQUIDACION
        }
        Integer exenta = 0;
        Integer iva5 = 0;
        Integer iva10 = 0;
        Integer liquidacionIva5 = 0;
        Integer liquidacionIva10 = 0;
        if (pi == 0) {
            for (M_campoImpresion object : campoImpresiones) {
                int posY = object.getY().intValue();
                int posX = object.getX().intValue();
                /*
                RAZON SOCIAL EMPRESA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_RAZON_EMPRESA)) {
                    String razonSocialEmpresa = "";
                    if (empresa.getEntidad() != null) {
                        razonSocialEmpresa = empresa.getEntidad();
                    }
                    g.drawString(razonSocialEmpresa, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(razonSocialEmpresa, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(razonSocialEmpresa, posX, triplicadoDist);
                    }
                }
                /*
                NOMBRE FANTASIA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_NOMBRE_EMPRESA)) {
                    String nombreFantasia = "";
                    if (empresa.getNombre() != null) {
                        nombreFantasia = empresa.getNombre();
                    }
                    g.drawString(nombreFantasia, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(nombreFantasia, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(nombreFantasia, posX, triplicadoDist);
                    }
                }
                /*
                DIRECCION EMPRESA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DIRECCION_EMPRESA)) {
                    String direccion = "";
                    if (empresa.getDireccion() != null) {
                        direccion = empresa.getDireccion();
                    }
                    g.drawString(direccion, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(direccion, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(direccion, posX, triplicadoDist);
                    }
                }
                /*
                EMAIL EMPRESA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_EMAIL_EMPRESA)) {
                    String email = "";
                    if (empresa.getEmail() != null) {
                        email = empresa.getEmail();
                    }
                    g.drawString(email, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(email, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(email, posX, triplicadoDist);
                    }
                }
                /*
                RUC EMPRESA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_RUC_EMPRESA)) {
                    String rucEmpresa = "";
                    if (empresa.getRuc() != null) {
                        rucEmpresa = empresa.getRuc();
                    }
                    g.drawString(rucEmpresa, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(rucEmpresa, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(rucEmpresa, posX, triplicadoDist);
                    }
                }
                /*
                PAGINA WEB EMPRESA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_PAG_WEB_EMPRESA)) {
                    String pagWebEmpresa = "";
                    if (empresa.getPagWeb() != null) {
                        pagWebEmpresa = empresa.getPagWeb();
                    }
                    g.drawString(pagWebEmpresa, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(pagWebEmpresa, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(pagWebEmpresa, posX, triplicadoDist);
                    }
                }
                /*
                DESCRIPCION EMPRESA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DESCRIPCION_EMPRESA)) {
                    String descripcionEmpresa = "";
                    if (empresa.getDescripcion() != null) {
                        descripcionEmpresa = empresa.getDescripcion();
                    }
                    g.drawString(descripcionEmpresa, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(descripcionEmpresa, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(descripcionEmpresa, posX, triplicadoDist);
                    }
                }
                /*
                TELEFONO EMPRESA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_TELEFONO_EMPRESA)) {
                    String telefonoEmpresa = "";
                    if (empresa.getTelefono() != null) {
                        telefonoEmpresa = empresa.getTelefono();
                    }
                    g.drawString(telefonoEmpresa, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(telefonoEmpresa, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(telefonoEmpresa, posX, triplicadoDist);
                    }
                }
                /*
                FECHA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_FECHA)) {
                    g.drawString(MyConstants.BOLETA_FECHA + ": " + fechaEntrega, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(MyConstants.BOLETA_FECHA + ": " + fechaEntrega, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(MyConstants.BOLETA_FECHA + ": " + fechaEntrega, posX, triplicadoDist);
                    }
                }
                /*
                CONDICION DE VENTA CONTADO
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_COND_VENTA_CONTADO)) {
                    if (facturaCabecera.getIdCondVenta() == TipoOperacion.CONTADO) {
                        g.drawString(MyConstants.BOLETA_COND_VENTA_CONTADO, posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                            g.drawString(MyConstants.BOLETA_COND_VENTA_CONTADO, posX, duplicadoDist);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                            g.drawString(MyConstants.BOLETA_COND_VENTA_CONTADO, posX, triplicadoDist);
                        }
                    }
                }
                /*
                CONDICION DE VENTA CREDITO
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_COND_VENTA_CREDITO)) {
                    if (facturaCabecera.getIdCondVenta() == TipoOperacion.CREDITO) {
                        g.drawString(MyConstants.BOLETA_COND_VENTA_CREDITO, posX, posY);
                        if (preferencia.getIdDuplicado() == 1) {
                            int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                            g.drawString(MyConstants.BOLETA_COND_VENTA_CREDITO, posX, duplicadoDist);
                        }
                        if (preferencia.getIdTriplicado() == 1) {
                            int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                            g.drawString(MyConstants.BOLETA_COND_VENTA_CREDITO, posX, triplicadoDist);
                        }
                    }
                }
                /*
                RAZON SOCIAL
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_RAZON_SOCIAL)) {
                    g.drawString(MyConstants.BOLETA_RAZON_SOCIAL + ": " + facturaCabecera.getCliente().getEntidad(), posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(MyConstants.BOLETA_RAZON_SOCIAL + ": " + facturaCabecera.getCliente().getEntidad(), posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(MyConstants.BOLETA_RAZON_SOCIAL + ": " + facturaCabecera.getCliente().getEntidad(), posX, triplicadoDist);
                    }
                }
                /*
                R.U.C.
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_RUC)) {
                    g.drawString("R.U.C.: " + ruc, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString("R.U.C.: " + ruc, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString("R.U.C.: " + ruc, posX, triplicadoDist);
                    }
                }
                /*
                DIRECCION
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DIRECCION)) {
                    String dir = "";
                    if (facturaCabecera.getCliente().getDireccion() != null) {
                        dir = facturaCabecera.getCliente().getDireccion();
                    }
                    g.drawString("Dirección: " + dir, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString("Dirección: " + dir, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString("Dirección: " + dir, posX, triplicadoDist);
                    }
                }
                /*
                NOTA DE REMISION
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_REMISION)) {
                    String remision = "";
                    if (facturaCabecera.getIdNotaRemision() != null) {
                        remision = facturaCabecera.getIdNotaRemision() + "";
                    }
                    g.drawString("Remisión: " + remision, posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString("Remisión: " + remision, posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString("Remisión: " + remision, posX, triplicadoDist);
                    }
                }
                /*
                DETALLE CANTIDAD
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DET_CANT_PROD)) {
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
                /*
                DETALLE CODIGO
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DET_COD_PROD)) {
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
                /*
                DETALLE PRODUCTO
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DET_DESCRIPCION_PROD)) {
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
                /*
                DETALLE PRECIO
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DET_PRECIO_PROD)) {
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
                /*
                DETALLE DESCUENTO
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DET_DESC_PROD)) {
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
                /*
                DETALLE EXENTA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DET_EXENTA_PROD)) {
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
                /*
                DETALLE IVA5%
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DET_IVA5_PROD)) {
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
                /*
                DETALLE IVA10%
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_DET_IVA10_PROD)) {
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
                /*
                SUBTOTAL EXENTA
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_SUB_TOTAL)) {
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
                /*
                SUBTOTAL IVA5%
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_SUB_TOTAL_IVA5)) {
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
                /*
                SUBTOTAL IVA10%
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_SUB_TOTAL_IVA10)) {
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
                /*
                TOTAL EN LETRAS
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_TOTAL_LETRA)) {
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
                    g.drawString(MyConstants.BOLETA_TOTAL_LETRA + ": " + totalLetra + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString(MyConstants.BOLETA_TOTAL_LETRA + ": " + totalLetra + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString(MyConstants.BOLETA_TOTAL_LETRA + ": " + totalLetra + "", posX, triplicadoDist);
                    }
                }
                /*
                TOTAL NUMERO
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_TOTAL_NUMERO)) {
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
                /*
                LIQUIDACION IVA5%
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_LIQ_IVA5)) {
                    //Calcular liquidacion de iva5 si es que hay productos con impuesto de 5%
                    if (iva5 > 0) {
                        liquidacionIva5 = iva5 / 21;
                    }
                    g.drawString("(5%): " + liquidacionIva5 + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString("(5%): " + liquidacionIva5 + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString("(5%): " + liquidacionIva5 + "", posX, triplicadoDist);
                    }
                }
                /*
                LIQUIDACION IVA10%
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_LIQ_IVA10)) {
                    //Calcular liquidacion de iva5 si es que hay productos con impuesto de 10%
                    if (iva10 > 0) {
                        liquidacionIva10 = iva10 / 11;
                    }
                    g.drawString("(10%): " + liquidacionIva10 + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString("(10%): " + liquidacionIva10 + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString("(10%): " + liquidacionIva10 + "", posX, triplicadoDist);
                    }
                }
                /*
                LIQUIDACION TOTAL
                 */
                if (object.getCampo().equals(MyConstants.BOLETA_LIQ_TOTAL)) {
                    int liquidTotal = liquidacionIva5 + liquidacionIva10;
                    g.drawString("Total I.V.A.: " + liquidTotal + "", posX, posY);
                    if (preferencia.getIdDuplicado() == 1) {
                        int duplicadoDist = posY + preferencia.getDistanceBetweenCopies();
                        g.drawString("Total I.V.A.: " + liquidTotal + "", posX, duplicadoDist);
                    }
                    if (preferencia.getIdTriplicado() == 1) {
                        int triplicadoDist = posY + preferencia.getDistanceBetweenCopies() + preferencia.getDistanceBetweenCopies();
                        g.drawString("Total I.V.A.: " + liquidTotal + "", posX, triplicadoDist);
                    }
                }
            }
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }

}
