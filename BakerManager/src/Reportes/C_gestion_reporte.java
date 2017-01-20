/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import bakermanager.C_inicio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Ramiro
 */
public class C_gestion_reporte implements ActionListener {

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
        this.vista.jbRCD.addActionListener(this);
        this.vista.jbRCS.addActionListener(this);
        this.vista.jbRCSC.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbRCSC)) {
            FiltroReporte_Cate p = new FiltroReporte_Cate(c_inicio);
            p.setVisible(true);
        }
    }
}
