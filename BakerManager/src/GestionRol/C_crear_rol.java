/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionRol;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Ramiro
 */
public class C_crear_rol extends MouseAdapter implements ActionListener {

    M_crear_rol modelo;
    V_crear_rol vista;

    public C_crear_rol(M_crear_rol modelo, V_crear_rol vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
        //comentario nuevo
    }

    /**
     * Establece el tamaño, posicion y visibilidad de la vista.
     */
    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    /**
     * Elimina la vista.
     */
    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    /**
     * Agrega ActionListeners los controles.
     */
    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbAgregar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jbQuitar.addActionListener(this);
        this.vista.jtPermisosDisponibles.addMouseListener(this);
        this.vista.jtPermisosSeleccionados.addMouseListener(this);
    }

    /**
     * Agrega valores a los componentes.
     */
    private void inicializarVista() {
        this.vista.jbAgregar.setEnabled(false);
        this.vista.jbQuitar.setEnabled(false);
        this.vista.jtPermisosDisponibles.setModel(modelo.obtenerAccesosDispon());
        Utilities.c_packColumn.packColumns(this.vista.jtPermisosDisponibles, 1);
    }

    private void crearRol() {
        if (this.modelo.crearRol(this.vista.jtfNombreRol.getText())) {
            cerrar();
        }
    }

    private void agregarAcceso() {
        int fila = this.vista.jtPermisosDisponibles.getSelectedRow();
        int idMenu = (Integer.valueOf((String) this.vista.jtPermisosDisponibles.getValueAt(fila, 0)));
        String menuDescripcion = (String) this.vista.jtPermisosDisponibles.getValueAt(fila, 1);
        int idMenuItem = (Integer.valueOf((String) this.vista.jtPermisosDisponibles.getValueAt(fila, 2)));
        String itemDescripcion = (String) this.vista.jtPermisosDisponibles.getValueAt(fila, 3);
        this.modelo.agregarAcceso(idMenu, menuDescripcion, idMenuItem, itemDescripcion);
        this.vista.jtPermisosSeleccionados.setModel(this.modelo.obtenerAccesoSelecc());
        Utilities.c_packColumn.packColumns(this.vista.jtPermisosSeleccionados, 1);
        this.vista.jbAgregar.setEnabled(false);
        this.vista.jbQuitar.setEnabled(false);
    }

    private void quitarAcceso() {
        int fila = this.vista.jtPermisosSeleccionados.getSelectedRow();
        int idItem = (int) this.vista.jtPermisosSeleccionados.getValueAt(fila, 2);
        this.modelo.quitarAcceso(idItem);
        this.vista.jtPermisosSeleccionados.setModel(this.modelo.obtenerAccesoSelecc());
        Utilities.c_packColumn.packColumns(this.vista.jtPermisosSeleccionados, 1);
        this.vista.jbAgregar.setEnabled(false);
        this.vista.jbQuitar.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbCancelar)) {
            cerrar();
        }
        if (e.getSource().equals(this.vista.jbAceptar)) {
            crearRol();
        }
        if (e.getSource().equals(this.vista.jbAgregar)) {
            agregarAcceso();
        }
        if (e.getSource().equals(this.vista.jbQuitar)) {
            quitarAcceso();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtPermisosDisponibles)) {
            int fila = this.vista.jtPermisosDisponibles.rowAtPoint(e.getPoint());
            int columna = this.vista.jtPermisosDisponibles.columnAtPoint(e.getPoint());
            int idMenu = (Integer.valueOf((String) this.vista.jtPermisosDisponibles.getValueAt(fila, 0)));
            String menuDescripcion = (String) this.vista.jtPermisosDisponibles.getValueAt(fila, 1);
            int idMenuItem = (Integer.valueOf((String) this.vista.jtPermisosDisponibles.getValueAt(fila, 2)));
            String itemDescripcion = (String) this.vista.jtPermisosDisponibles.getValueAt(fila, 3);
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbAgregar.setEnabled(true);
            } else {
                this.vista.jbAgregar.setEnabled(false);
            }
            if (e.getClickCount() == 2) {
                this.modelo.agregarAcceso(idMenu, menuDescripcion, idMenuItem, itemDescripcion);
                this.vista.jtPermisosSeleccionados.setModel(this.modelo.obtenerAccesoSelecc());
                Utilities.c_packColumn.packColumns(this.vista.jtPermisosSeleccionados, 1);
                this.vista.jbAgregar.setEnabled(false);
                this.vista.jbQuitar.setEnabled(false);
            }
        }
        if (e.getSource().equals(this.vista.jtPermisosSeleccionados)) {
            int fila = this.vista.jtPermisosSeleccionados.rowAtPoint(e.getPoint());
            int columna = this.vista.jtPermisosSeleccionados.columnAtPoint(e.getPoint());
            int idMenuItem = (Integer.valueOf((String) this.vista.jtPermisosSeleccionados.getValueAt(fila, 2)));
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbQuitar.setEnabled(true);
            } else {
                this.vista.jbQuitar.setEnabled(false);
            }
            if (e.getClickCount() == 2) {
                this.modelo.quitarAcceso(idMenuItem);
                this.vista.jtPermisosSeleccionados.setModel(this.modelo.obtenerAccesoSelecc());
                Utilities.c_packColumn.packColumns(this.vista.jtPermisosSeleccionados, 1);
                this.vista.jbAgregar.setEnabled(false);
                this.vista.jbQuitar.setEnabled(false);
            }
        }
    }
}
