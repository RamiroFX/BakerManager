/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import Cliente.C_crear_cliente;
import DB_manager.DB_Proveedor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class C_modificar_telefono implements ActionListener {

    private static final int CREAR_CLIENTE = 1;
    private static final int MODIFICAR_CLIENTE = 2;
    private static final int CREAR_PROVEEDOR = 3;
    private static final int MODIFICAR_PROVEEDOR = 4;
    private int telefono = 0;
    V_crear_telefono vista;
    C_crear_proveedor crearProveedor;
    C_modificar_proveedor modiciarProveedor;
    C_crear_cliente crearCliente;
    

    public C_modificar_telefono(C_crear_proveedor crearTelefono) {
        this.crearProveedor = crearTelefono;
        this.vista = new V_crear_telefono(crearTelefono.vista);
        agregarListeners();
        telefono = CREAR_PROVEEDOR;
    }

    public C_modificar_telefono(C_modificar_proveedor modiciarProveedor) {
        this.modiciarProveedor = modiciarProveedor;
        this.vista = new V_crear_telefono(modiciarProveedor.vista);
        this.vista.setTitle("Modificar teléfono");
        agregarListeners();
        telefono = MODIFICAR_PROVEEDOR;
    }

    public C_modificar_telefono(C_crear_cliente crearCliente) {
        this.crearCliente = crearCliente;
        this.vista = new V_crear_telefono(modiciarProveedor.vista);
        this.vista.setTitle("Modificar teléfono");
        agregarListeners();
        telefono = CREAR_CLIENTE;
    }

    public void mostrarVista() {
        this.vista.setLocationRelativeTo(vista.getOwner());
        this.vista.setVisible(true);
    }

    public void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void controlarDatos() {
        if (this.vista.jtfTipoTelefono.getText().isEmpty()) {
            this.vista.jtfTipoTelefono.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo Tipo telefono esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        if (this.vista.jtfTelefono.getText().isEmpty()) {
            this.vista.jtfTelefono.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo telefono esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        String tipoTelefono = this.vista.jtfTipoTelefono.getText();
        String nroTelefono = this.vista.jtfTelefono.getText();
        String observacion = this.vista.jtfObservacion.getText();
        switch (telefono) {
            case (CREAR_PROVEEDOR): {
                this.crearProveedor.modificarTelefono(tipoTelefono, nroTelefono, observacion);
            }
            break;
            case (MODIFICAR_PROVEEDOR): {
                int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        this.modiciarProveedor.modificarTelefono(tipoTelefono, nroTelefono, observacion);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case (CREAR_CLIENTE): {
                this.crearCliente.modificarTelefono(tipoTelefono, nroTelefono, observacion);
            }
            break;
            default: {
                cerrar();
            }
            break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbAceptar)) {
            controlarDatos();
            cerrar();
        } else if (e.getSource().equals(this.vista.jbCancelar)) {
            cerrar();
        }
    }

    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
    }
}
