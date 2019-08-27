/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import Entities.E_ticketPreferencia;
import Impresora.Impresora;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import javax.swing.JOptionPane;

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
        this.vista.jtaCabecera.setText(modelo.getTicketPreferencia().getCabecera());
        this.vista.jtaPie.setText(modelo.getTicketPreferencia().getPie());
        this.vista.jtfNombreImpresora.setText(modelo.getTicketPreferencia().getNombreImpresora());
    }

    private void guardarPreferencia() {
        String cabecera;
        if (this.vista.jtaCabecera.getText().length() > 500) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "La cabecera sobrepasa el limite permitido(500) de caracteres",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else {
            cabecera = this.vista.jtaCabecera.getText();
        }
        String pie;
        if (this.vista.jtaPie.getText().length() > 500) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El pié sobrepasa el limite permitido(500) de caracteres",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else {
            pie = this.vista.jtaPie.getText();
        }
        String nombreImpresora;
        if (this.vista.jtfNombreImpresora.getText().trim().length() > 30) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El nombre de impresora sobrepasa el limite permitido(30) de caracteres",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else {
            nombreImpresora = this.vista.jtfNombreImpresora.getText().trim();
        }
        E_ticketPreferencia tp = new E_ticketPreferencia();
        tp.setCabecera(cabecera);
        tp.setPie(pie);
        tp.setNombreImpresora(nombreImpresora);
        if (modelo.modificarTicketPreferencia(tp) > -1) {
            JOptionPane.showMessageDialog(vista, "Cambios guardados", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            modelo.actualizarPreferenciasTicket();
        }
    }

    private void imprimirPaginaPrueba() {
        Impresora.imprimirTicketPrueba();
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
