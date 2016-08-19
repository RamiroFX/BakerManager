/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import Cliente.C_crear_cliente;
import Cliente.C_modificar_cliente;
import DB.DB_manager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class C_crear_telefono implements ActionListener {

    private static final int CREAR_CLIENTE = 1;
    private static final int MODIFICAR_CLIENTE = 2;
    private static final int CREAR_PROVEEDOR = 3;
    private static final int MODIFICAR_PROVEEDOR = 4;
    private int telefono = 0;
    V_crear_telefono vista;
    C_crear_proveedor crearProveedor;
    C_modificar_proveedor modiciarProveedor;
    C_crear_cliente crearCliente;
    C_modificar_cliente modificarCliente;

    public C_crear_telefono(C_crear_proveedor crearTelefono) {
        this.crearProveedor = crearTelefono;
        this.vista = new V_crear_telefono(crearTelefono.vista);
        inicializarVista();
        agregarListeners();
        telefono = CREAR_PROVEEDOR;
    }

    public C_crear_telefono(C_modificar_proveedor modiciarProveedor) {
        this.modiciarProveedor = modiciarProveedor;
        this.vista = new V_crear_telefono(modiciarProveedor.vista);
        inicializarVista();
        agregarListeners();
        telefono = MODIFICAR_PROVEEDOR;
    }

    public C_crear_telefono(C_crear_cliente crearCliente) {
        this.crearCliente = crearCliente;
        this.vista = new V_crear_telefono(crearCliente.vista);
        inicializarVista();
        agregarListeners();
        telefono = CREAR_CLIENTE;
    }

    public C_crear_telefono(C_modificar_cliente modificarCliente) {
        this.modificarCliente = modificarCliente;
        this.vista = new V_crear_telefono(modificarCliente.vista);
        inicializarVista();
        agregarListeners();
        telefono = MODIFICAR_CLIENTE;
    }

    private void inicializarVista() {
        Vector telefonoCategoria = DB_manager.obtenerTelefonoCategoria();
        for (int i = 0; i < telefonoCategoria.size(); i++) {
            this.vista.jcbCategoria.addItem(telefonoCategoria.get(i));
        }
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
        String nroTelefono = this.vista.jtfTelefono.getText().trim();
        if (nroTelefono.isEmpty()) {
            this.vista.jtfTelefono.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo telefono esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else if (nroTelefono.length() > 30) {
            this.vista.jtfTelefono.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 30 para telefono.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        String observacion = this.vista.jtfObservacion.getText().trim();
        if (nroTelefono.length() > 120) {
            this.vista.jtfObservacion.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 120 para observación-telefono.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        String tipoTelefono = this.vista.jcbCategoria.getSelectedItem().toString();
        switch (telefono) {
            case (CREAR_PROVEEDOR): {
                this.crearProveedor.recibirTelefono(tipoTelefono, nroTelefono, observacion);
            }
            break;
            case (MODIFICAR_PROVEEDOR): {
                this.modiciarProveedor.recibirTelefono(tipoTelefono, nroTelefono, observacion);
            }
            break;
            case (CREAR_CLIENTE): {
                this.crearCliente.recibirTelefono(tipoTelefono, nroTelefono, observacion);
            }
            break;
            case (MODIFICAR_CLIENTE): {
                this.modificarCliente.recibirTelefono(tipoTelefono, nroTelefono, observacion);
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
