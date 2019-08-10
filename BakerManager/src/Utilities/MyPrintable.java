/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import DB.DB_manager;
import Entities.M_campoImpresion;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.util.List;

/**
 *
 * @author Ramiro
 */
public class MyPrintable implements Printable {

    List<M_campoImpresion> impresions;

    public MyPrintable(List<M_campoImpresion> impresions) {
        this.impresions = impresions;
    }

    public int print(Graphics g, PageFormat f, int pageIndex) {
        if (pageIndex == 0) {
            // Imprime "Hola mundo" en la primera pagina, en la posicion 100,100
            for (M_campoImpresion object : impresions) {
                g.drawString(object.getCampo(), object.getX().intValue(), object.getY().intValue());
            }
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }
}
