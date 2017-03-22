/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import Interface.GestionInterface;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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

    JTabbedPane jtpReportes;
    JPanel jpCompras;
    JButton jbRCD, jbRCS, jbRCSC;

    public V_gestion_reporte() {
        super("Reportes", true, true, true, true);;
        setSize(1200, 600);
        setName("jifReportes");
        inicializarVista();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jtpReportes, BorderLayout.CENTER);
    }

    private void inicializarVista() {
        jpCompras = new JPanel(new MigLayout());
        jbRCD = new JButton("Resumen de compras detallado");
        jbRCS = new JButton("Resumen de compras simple");
        jbRCSC = new JButton("Resumen de compras simple por categorías");
        jpCompras.add(jbRCD, "height 50:80:100, spanx, growx, pushx, wrap");
        jpCompras.add(jbRCS, "height 50:80:100, growx, wrap");
        jpCompras.add(jbRCSC, "height 50:80:100, growx");
        jtpReportes = new JTabbedPane();
        jtpReportes.add("Compras", jpCompras);
    }
}
