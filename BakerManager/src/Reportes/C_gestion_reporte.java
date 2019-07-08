/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import bakermanager.C_inicio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;

/**
 *
 * @author Ramiro
 */
public class C_gestion_reporte implements ActionListener, KeyListener {

    private V_gestion_reporte vista;
    public C_inicio c_inicio;

    public C_gestion_reporte(V_gestion_reporte vista, C_inicio c_inicio) {
        this.vista = vista;
        this.c_inicio = c_inicio;
        concederPermisos();
    }

    public void mostrarVista() {
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        this.c_inicio.agregarVentana(this.vista);
    }

    private void concederPermisos() {
        this.vista.jbRVSC.addActionListener(this);
        //this.vista.jbRCS.addActionListener(this);
        this.vista.jbRCSC.addActionListener(this);
        /*
        KEYLISTENERS
         */
        this.vista.jbRVSC.addKeyListener(this);
        //this.vista.jbRCS.addKeyListener(this);
        this.vista.jbRCSC.addKeyListener(this);
        //this.vista.jtpReportes.addKeyListener(this);
    }

    private void cerrar() {
        try {
            this.vista.setClosed(true);
            System.runFinalization();
        } catch (PropertyVetoException ex) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbRVSC)) {
            FiltroReporte_Cate p = new FiltroReporte_Cate(c_inicio, 1);
            p.setVisible(true);
        }
        if (src.equals(this.vista.jbRCSC)) {
            FiltroReporte_Cate p = new FiltroReporte_Cate(c_inicio, 2);
            p.setVisible(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
