/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import Entities.M_campoImpresion;
import Interface.crearModificarParametroCallback;
import Utilities.Impresora;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Ramiro
 */
public class C_configuracion extends MouseAdapter implements ActionListener, KeyListener, MouseListener, crearModificarParametroCallback {

    private static final int CREAR_PARAMETRO = 1, MODIFICAR_PARAMETRO = 2;
    private V_configuracion vista;
    private M_configuracion modelo;

    public C_configuracion(V_configuracion vista, M_configuracion modelo) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarVista();
        agregarListeners();
    }

    /**
     * Establece el tamaño, posicion y visibilidad de la vista.
     */
    public void mostrarVista() {
        this.vista.setSize(800, 350);
        this.vista.setLocationRelativeTo(this.vista.getOwner());
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
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jbAgregarCampo.addActionListener(this);
        this.vista.jbModificarCampo.addActionListener(this);
        this.vista.jbHabilitarDeshabilitarCampo.addActionListener(this);
        this.vista.jbImprimirPaginaPrueba.addActionListener(this);
        this.vista.jbOcultarMostrarCampo.addActionListener(this);
        this.vista.jtFactura.addMouseListener(this);
    }

    /**
     * Agrega valores a los componentes.
     */
    private void inicializarVista() {
        this.vista.jbModificarCampo.setEnabled(false);
        this.vista.jbHabilitarDeshabilitarCampo.setEnabled(false);
        this.vista.jtFactura.setModel(modelo.getImpresionFacturaTM());
    }

    private void agregarCampo() {
        V_crearModificarCampoImpresion cmci = new V_crearModificarCampoImpresion(CREAR_PARAMETRO, this.vista);
        cmci.setCallback(this);
        cmci.setVisible(true);
        this.modelo.updateTable();
    }

    private void modificarCampo() {
        int row = vista.jtFactura.getSelectedRow();
        if (row > -1) {
            M_campoImpresion ci = modelo.getImpresionFacturaTM().getValueFromList(row);
            V_crearModificarCampoImpresion cmci = new V_crearModificarCampoImpresion(MODIFICAR_PARAMETRO, this.vista, ci);
            cmci.setCallback(this);
            cmci.setVisible(true);
            this.modelo.updateTable();
            this.vista.jbModificarCampo.setEnabled(false);
            this.vista.jbHabilitarDeshabilitarCampo.setEnabled(false);
        }
    }

    private void quitarCampo() {
        int row = vista.jtFactura.getSelectedRow();
        if (row > -1) {
            modelo.habilitarDeshabilitarCampo(row);
            this.modelo.updateTable();
            this.vista.jbModificarCampo.setEnabled(false);
            this.vista.jbHabilitarDeshabilitarCampo.setEnabled(false);
        }
    }

    private void ocultarMostrarCampo() {
        modelo.ocultarMostrarCampo();
        this.vista.jbModificarCampo.setEnabled(false);
        this.vista.jbHabilitarDeshabilitarCampo.setEnabled(false);
        if (modelo.isIsVisible()) {
            modelo.setIsVisible(false);
        } else {
            modelo.setIsVisible(true);
        }
    }

    private void imprimirPaginaPrueba() {
        Impresora.imprimirPaginaPrueba();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbCancelar) {
            cerrar();
        } else if (e.getSource() == this.vista.jbAgregarCampo) {
            agregarCampo();
        } else if (e.getSource() == this.vista.jbModificarCampo) {
            modificarCampo();
        } else if (e.getSource() == this.vista.jbHabilitarDeshabilitarCampo) {
            quitarCampo();
        } else if (e.getSource() == this.vista.jbImprimirPaginaPrueba) {
            imprimirPaginaPrueba();
        } else if (e.getSource() == this.vista.jbOcultarMostrarCampo) {
            ocultarMostrarCampo();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtFactura)) {
            int fila = this.vista.jtFactura.rowAtPoint(e.getPoint());
            int columna = this.vista.jtFactura.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbModificarCampo.setEnabled(true);
                this.vista.jbHabilitarDeshabilitarCampo.setEnabled(true);
                if (e.getClickCount() == 2) {
                    int row = vista.jtFactura.getSelectedRow();
                    M_campoImpresion ci = modelo.getImpresionFacturaTM().getValueFromList(row);
                    modificarParametroImpresion(ci);
                }
            } else {
                this.vista.jbModificarCampo.setEnabled(false);
                this.vista.jbHabilitarDeshabilitarCampo.setEnabled(false);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void recibirParametroImpresion(M_campoImpresion ci) {
        modelo.crearParametro(ci);
    }

    @Override
    public void modificarParametroImpresion(M_campoImpresion ci) {
        modelo.modificarParametro(ci);
    }
}
