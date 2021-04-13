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
public class ReportesPagos {

    public static void estadoCuentaProveedores(JFrame frame) {
        FiltroReportesProveedores fc = new FiltroReportesProveedores(frame, 1, FiltroCliente.VENCIDAS);
        fc.setVisible(true);
    }
}
