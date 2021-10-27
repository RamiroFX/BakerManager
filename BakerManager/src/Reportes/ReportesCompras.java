/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class ReportesCompras {

    public static void compras(JFrame frame) {
        FiltroCompras fc = new FiltroCompras(frame, 1, FiltroCliente.VENCIDAS);
        fc.setVisible(true);
    }
}
