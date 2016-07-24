/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import Cliente.C_crear_cliente;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class C_crear_sucursal implements ActionListener {

    private static final int CREAR_CLIENTE = 1;
    private static final int MODIFICAR_CLIENTE = 2;
    private static final int CREAR_PROVEEDOR = 3;
    private static final int MODIFICAR_PROVEEDOR = 4;
    private int sucursal = 0;
    V_crear_sucursal vista;
    C_crear_proveedor crearProveedor;
    C_crear_cliente crearCliente;
    C_modificar_proveedor modificarProveedor;

    public C_crear_sucursal(C_crear_proveedor crearProveedor) {
        this.crearProveedor = crearProveedor;
        this.vista = new V_crear_sucursal(crearProveedor.vista);
        agregarListeners();
        this.sucursal = CREAR_PROVEEDOR;
    }

    public C_crear_sucursal(C_modificar_proveedor modificarProveedor) {
        this.modificarProveedor = modificarProveedor;
        this.vista = new V_crear_sucursal(modificarProveedor.vista);
        agregarListeners();
        this.sucursal = MODIFICAR_PROVEEDOR;
    }

    public C_crear_sucursal(C_crear_cliente crearCliente) {
        this.crearCliente = crearCliente;
        this.vista = new V_crear_sucursal(crearCliente.vista);
        agregarListeners();
        this.sucursal = CREAR_CLIENTE;
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
        if (this.vista.jtfDireccion.getText().isEmpty()) {
            this.vista.jtfDireccion.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo dirección esta vacio",
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
//        int crearClienteCantFila = this.crearCliente.dtmSucursal.getRowCount();
//        for (int i = 0; i < crearClienteCantFila; i++) {
//            String sucursal = this.crearCliente.dtmSucursal.getValueAt(i, 1).toString();
//            String sucursalActual=this.vista.jtfDireccion.getText();
//        }
        switch (sucursal) {
            case (CREAR_CLIENTE): {
                int option = JOptionPane.showConfirmDialog(this.vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        this.crearCliente.recibirSucursal(this.vista.jtfDireccion.getText(), this.vista.jtfTelefono.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case (MODIFICAR_CLIENTE): {
                //this.modificarCliente.recibirSucursal(this.vista.jtfDireccion.getText(), this.vista.jtfTelefono.getText());
            }
            break;
            case (MODIFICAR_PROVEEDOR): {
                int option = JOptionPane.showConfirmDialog(this.vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        this.modificarProveedor.recibirSucursal(this.vista.jtfDireccion.getText(), this.vista.jtfTelefono.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case (CREAR_PROVEEDOR): {
                this.crearProveedor.recibirSucursal(this.vista.jtfDireccion.getText(), this.vista.jtfTelefono.getText());
            }
            break;
            default: {
                cerrar();
            }
            break;
        }
        /*
         if (null == this.crearProveedor) {
         int option = JOptionPane.showConfirmDialog(this.vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
         if (option == JOptionPane.YES_OPTION) {
         try {
         this.modificarProveedor.recibirSucursal(this.vista.jtfDireccion.getText(), this.vista.jtfTelefono.getText());
         } catch (Exception e) {
         e.printStackTrace();
         }
         }
         } else if (null == this.modificarProveedor) {
         this.crearProveedor.recibirSucursal(this.vista.jtfDireccion.getText(), this.vista.jtfTelefono.getText());
         }*/
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
