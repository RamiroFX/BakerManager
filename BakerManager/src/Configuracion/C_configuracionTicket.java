/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import Entities.Divisa;
import Entities.M_campoImpresion;
import Entities.M_preferenciasImpresion;
import Interface.crearModificarParametroCallback;
import Impresora.Impresora;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_configuracionTicket extends MouseAdapter implements ActionListener, KeyListener {

    private V_configuracionTicket vista;
    private M_configuracionTicket modelo;

    public C_configuracionTicket(V_configuracionTicket vista, M_configuracionTicket modelo) {
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
        this.vista.jbImprimirPaginaPrueba.addActionListener(this);
        this.vista.jbGuardar.addActionListener(this);
    }

    /**
     * Agrega valores a los componentes.
     */
    private void inicializarVista() {
        this.vista.jtaCabecera.setText("");
        this.vista.jtaPie.setText("");
    }

    private void modificarCampo() {
    }

    private void quitarCampo() {
    }

    private void guardarPreferencia() {
        //crear variable contenedora de preferencia de ticket
        String cabecera;
        if (this.vista.jtaCabecera.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo nombre esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else {
            if (this.vista.jtaCabecera.getText().length() > 150) {
                javax.swing.JOptionPane.showMessageDialog(this.vista,
                        "La cabecera sobrepasa el limite permitido(150) de caracteres",
                        "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                return;
            } else {
                cabecera = this.vista.jtaCabecera.getText();
            }
        }
        //modelo.guardarPreferencias();
    }

    private void imprimirPaginaPrueba() {
        Impresora.imprimirPaginaPrueba();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbCancelar) {
            cerrar();
        } else if (e.getSource() == this.vista.jbImprimirPaginaPrueba) {
            imprimirPaginaPrueba();
        } else if (e.getSource() == this.vista.jbGuardar) {
            guardarPreferencia();
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
}