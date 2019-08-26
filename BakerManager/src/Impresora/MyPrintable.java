/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Impresora;

import Entities.M_campoImpresion;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_rol_usuario;
import Utilities.MyConstants;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ramiro
 */
public class MyPrintable implements Printable {

    private final SimpleDateFormat SDFS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<M_campoImpresion> impresions;
    M_rol_usuario rol_usuario;
    M_facturaCabecera facturaCabecera;
    ArrayList<M_facturaDetalle> facturaDetalle;

    public MyPrintable(List<M_campoImpresion> impresions) {
        this.impresions = impresions;
    }

    public MyPrintable(M_rol_usuario rol_usuario, M_facturaCabecera facturaCabecera, ArrayList<M_facturaDetalle> facturaDetalle, List<M_campoImpresion> impresions) {
        this.rol_usuario = rol_usuario;
        this.facturaCabecera = facturaCabecera;
        this.facturaDetalle = facturaDetalle;
        this.impresions = impresions;
    }

    public int print(Graphics g, PageFormat f, int pageIndex) {
        if (pageIndex == 0) {
            for (M_campoImpresion object : impresions) {
                if (object.getEstado().getId() == MyConstants.ACTIVO) {
                    int posX = object.getX().intValue();
                    int posY = object.getY().intValue();
                    g.drawString(object.getCampo() + "(" + posX + "," + posY + ")", posX, posY);
                }
            }
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }
}
