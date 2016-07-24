/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionRol;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class C_gestion_rol extends MouseAdapter implements ActionListener {

    private V_gestion_rol vista;
    private M_gestion_rol modelo;

    public C_gestion_rol(M_gestion_rol modelo, V_gestion_rol vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    private void agregarListeners() {
        this.vista.jbCrearRol.addActionListener(this);
        this.vista.jbModificarRol.addActionListener(this);
        this.vista.jbEliminarRol.addActionListener(this);
        this.vista.jtRoles.addMouseListener(this);
    }

    private void inicializarVista() {
        this.vista.jbModificarRol.setEnabled(false);
        this.vista.jbEliminarRol.setEnabled(false);
        this.vista.jtRoles.setModel(this.modelo.consultarRoles(""));
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void mostrarPermisos(int idRol) {
        this.vista.jtPermisos.setModel(this.modelo.consultarPermisos(idRol));
        Utilities.c_packColumn.packColumns(this.vista.jtPermisos, 1);
    }

    private void eliminarRol() {
        int idRol = Integer.valueOf(String.valueOf(this.vista.jtRoles.getValueAt(this.vista.jtRoles.getSelectedRow(), 0)));
        this.modelo.eliminarRol(idRol);
        inicializarVista();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object e = ae.getSource();
        if (e.equals(this.vista.jbCrearRol)) {
            Crear_rol crear_rol = new Crear_rol(this.vista);
            crear_rol.mostrarVista();
            inicializarVista();
        } else if (e.equals(this.vista.jbModificarRol)) {
            int idRol = (Integer.valueOf((String) this.vista.jtRoles.getValueAt(this.vista.jtRoles.getSelectedRow(), 0)));
            if (idRol == 1) {
                JOptionPane.showMessageDialog(vista, "El rol administrador no puede ser modificado.", "Atención", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Modificar_rol modificar_rol = new Modificar_rol(this.vista, idRol);
                modificar_rol.mostrarVista();
                inicializarVista();
            }
        } else if (e.equals(this.vista.jbEliminarRol)) {
            int idRol = (Integer.valueOf((String) this.vista.jtRoles.getValueAt(this.vista.jtRoles.getSelectedRow(), 0)));
            if (idRol == 1) {
                JOptionPane.showMessageDialog(vista, "El rol administrador no puede ser eliminado.", "Atención", JOptionPane.INFORMATION_MESSAGE);
            } else {
                eliminarRol();
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent me) {
        int fila = this.vista.jtRoles.rowAtPoint(me.getPoint());
        int columna = this.vista.jtRoles.columnAtPoint(me.getPoint());
        int idRol = (Integer.valueOf((String) this.vista.jtRoles.getValueAt(fila, 0)));
        mostrarPermisos(idRol);
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbModificarRol.setEnabled(true);
            this.vista.jbEliminarRol.setEnabled(true);
        } else {
            this.vista.jbModificarRol.setEnabled(false);
            this.vista.jbEliminarRol.setEnabled(false);
        }
        if (me.getClickCount() == 2) {
            if (idRol == 1) {
                JOptionPane.showMessageDialog(vista, "El rol administrador no puede ser modificado.", "Atención", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Modificar_rol modificar_rol = new Modificar_rol(this.vista, idRol);
                modificar_rol.mostrarVista();
                inicializarVista();
            }
        }
    }
}
