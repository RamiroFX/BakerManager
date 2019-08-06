/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class C_configuracion extends MouseAdapter implements ActionListener, KeyListener {

    private V_configuracion vista;

    public C_configuracion(V_configuracion vista) {
        this.vista = vista;
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
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbAgregarCampo.addActionListener(this);
        this.vista.jbModificarCampo.addActionListener(this);
        this.vista.jbQuitarCampo.addActionListener(this);
    }

    /**
     * Agrega valores a los componentes.
     */
    private void inicializarVista() {
        this.vista.jbAgregarCampo.setEnabled(false);
        this.vista.jbModificarCampo.setEnabled(false);
        this.vista.jbQuitarCampo.setEnabled(false);
        //this.vista.jtCampos.setModel(modelo.obtenerRolesDisp());
    }

    private boolean isValidDataEntry() {
        return true;
    }

    private void crearUsuario() {
        if (isValidDataEntry()) {

            /*if (modelo.crearUsuario(funcionario, password1, password2)) {
                cerrar();
            }*/
        }
    }

    private void agregarCampo() {
        JOptionPane.showMessageDialog(vista, "agregarCampo");
    }

    private void modificarCampo() {
        JOptionPane.showMessageDialog(vista, "modificarCampo");
    }

    private void quitarCampo() {
        JOptionPane.showMessageDialog(vista, "quitarCampo");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbCancelar) {
            cerrar();
        } else if (e.getSource() == this.vista.jbAceptar) {
            crearUsuario();
        } else if (e.getSource() == this.vista.jbAgregarCampo) {
            agregarCampo();
        } else if (e.getSource() == this.vista.jbModificarCampo) {
            modificarCampo();
        } else if (e.getSource() == this.vista.jbQuitarCampo) {
            quitarCampo();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
