/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro
 */
public class V_gestion_reporte extends JInternalFrame {

    //JTabbedPane jtpReportes;
    JPanel jpCompras;
    JButton jbRVSC, jbRCSC;
    //JButton jbRCS;

    public V_gestion_reporte() {
        super("Reportes", true, true, true, true);;
        setSize(1200, 600);
        setName("jifReportes");
        inicializarVista();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpCompras, BorderLayout.CENTER);
    }

    private void inicializarVista() {
        jpCompras = new JPanel(new MigLayout());
        jbRVSC = new JButton("Resumen de ventas simple por categorías");
        //jbRCS = new JButton("Resumen de compras simple");
        jbRCSC = new JButton("Resumen de compras simple por categorías");
        jpCompras.add(jbRVSC, "height 50:80:100, spanx, growx, pushx, wrap");
        //jpCompras.add(jbRCS, "height 50:80:100, growx, wrap");
        jpCompras.add(jbRCSC, "height 50:80:100, growx");
        /*jtpReportes = new JTabbedPane();
        jtpReportes.add("Compras", jpCompras);*/
    }
}
