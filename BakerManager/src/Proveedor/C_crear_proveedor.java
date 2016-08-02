/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import Contacto.AgregarContacto;
import Entities.M_contacto;
import Entities.M_sucursal;
import Entities.M_telefono;
import Entities.M_proveedor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crear_proveedor extends MouseAdapter implements ActionListener, KeyListener {

    private M_crear_proveedor modelo;
    public V_crear_proveedor vista;
    private C_gestion_proveedores padre;

    public C_crear_proveedor(M_crear_proveedor modelo, V_crear_proveedor vista,
            C_gestion_proveedores gestionProveedor) {
        this.modelo = modelo;
        this.vista = vista;
        this.padre = gestionProveedor;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setLocationRelativeTo(this.vista);
        this.vista.setVisible(true);
    }

    private void inicializarVista() {
        this.vista.jtContacto.setModel(modelo.dtmContacto);

        this.vista.jtSucursal.setModel(modelo.dtmSucursal);

        this.vista.jtTelefono.setModel(modelo.dtmTelefono);
        this.vista.jbQuitarSucursal.setEnabled(false);
        this.vista.jbQuitarTelefono.setEnabled(false);
        this.vista.jbModTelefono.setEnabled(false);
        this.vista.jbModSucursal.setEnabled(false);
        this.vista.jbModContacto.setEnabled(false);
        this.vista.jbQuitarContacto.setEnabled(false);
    }

    private void agregarListeners() {
        this.vista.jbAgregarSucursal.addActionListener(this);
        this.vista.jbAgregarTelefono.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jbQuitarSucursal.addActionListener(this);
        this.vista.jbQuitarTelefono.addActionListener(this);
        this.vista.jbModSucursal.addActionListener(this);
        this.vista.jbModTelefono.addActionListener(this);
        this.vista.jtSucursal.addMouseListener(this);
        this.vista.jtTelefono.addMouseListener(this);
        this.vista.jtContacto.addMouseListener(this);
        this.vista.jbAgregarContacto.addActionListener(this);
        this.vista.jbQuitarContacto.addActionListener(this);
        this.vista.jtfRazonSocial.addMouseListener(this);
        this.vista.jtfNombreFantasia.addMouseListener(this);
        this.vista.jtfRUC.addMouseListener(this);
        this.vista.jtfRUC_ID.addMouseListener(this);
        this.vista.jtfDescripcion.addMouseListener(this);
        this.vista.jtfPagWeb.addMouseListener(this);
        this.vista.jtfemail.addMouseListener(this);
        this.vista.jtfDireccion.addMouseListener(this);
        this.vista.jtaNota.addMouseListener(this);
    }

    public void recibirSucursal(String direccion, String telefono) {
        Object[] fila = {direccion, telefono};
        modelo.dtmSucursal.addRow(fila);
        this.vista.jtSucursal.updateUI();
        System.out.println("recibirSucursal");
    }

    public void modificarSucursal(String direccion, String telefono) {
        int row = this.vista.jtSucursal.getSelectedRow();
        modelo.dtmSucursal.setValueAt(direccion, row, 0);
        modelo.dtmSucursal.setValueAt(telefono, row, 1);
        this.vista.jbQuitarTelefono.setEnabled(false);
        this.vista.jbModTelefono.setEnabled(false);
    }

    private void quitarSucursal() {
        modelo.dtmSucursal.removeRow(this.vista.jtSucursal.getSelectedRow());
        this.vista.jbQuitarSucursal.setEnabled(false);
    }

    public void recibirTelefono(String tipoTelefono, String nroTelefono, String observacion) {
        if (modelo.existeTelefono(nroTelefono)) {
            JOptionPane.showMessageDialog(vista, "Telefono en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
        } else {
            Object[] fila = {nroTelefono, tipoTelefono, observacion};
            modelo.dtmTelefono.addRow(fila);
            this.vista.jtTelefono.updateUI();
        }
    }

    public void modificarTelefono(String tipoTelefono, String nroTelefono, String observacion) {
        if (modelo.existeTelefono(nroTelefono)) {
            JOptionPane.showMessageDialog(vista, "Telefono en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
        } else {
            int row = this.vista.jtTelefono.getSelectedRow();
            modelo.dtmTelefono.setValueAt(nroTelefono, row, 0);
            modelo.dtmTelefono.setValueAt(tipoTelefono, row, 1);
            modelo.dtmTelefono.setValueAt(observacion, row, 2);
            this.vista.jbQuitarTelefono.setEnabled(false);
            this.vista.jbModTelefono.setEnabled(false);
        }
    }

    private void quitarTelefono() {
        modelo.dtmTelefono.removeRow(this.vista.jtTelefono.getSelectedRow());
        this.vista.jbQuitarTelefono.setEnabled(false);
    }

    private void validarDatos() {
        int cantTel = modelo.dtmTelefono.getRowCount();
        M_telefono[] telefono = new M_telefono[cantTel];
        for (int i = 0; i < cantTel; i++) {
            telefono[i] = new M_telefono();
            telefono[i].setCategoria(modelo.dtmTelefono.getValueAt(i, 1).toString());
            telefono[i].setNumero(modelo.dtmTelefono.getValueAt(i, 0).toString());
            String obs = modelo.dtmTelefono.getValueAt(i, 2).toString();
            if (obs.isEmpty()) {
                telefono[i].setObservacion(null);
            } else {
                telefono[i].setObservacion(obs);
            }
        }
        int cantSuc = modelo.dtmSucursal.getRowCount();
        M_sucursal[] sucursal = new M_sucursal[cantSuc];
        for (int i = 0; i < cantSuc; i++) {
            sucursal[i] = new M_sucursal();
            sucursal[i].setDireccion(modelo.dtmSucursal.getValueAt(i, 0).toString());
            if (modelo.dtmSucursal.getValueAt(i, 1) != null) {
                String tel = modelo.dtmSucursal.getValueAt(i, 1).toString();
                if (tel.isEmpty()) {
                    sucursal[i].setTelefono(null);
                } else {
                    sucursal[i].setTelefono(tel);
                }
            }
        }
        /*
         * VALIDAR RAZON SOCIAL
         60 caracteres maximo
         */
        String entidad;
        if (this.vista.jtfRazonSocial.getText().isEmpty()) {
            this.vista.jtfRazonSocial.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo Razón social esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else if (this.vista.jtfRazonSocial.getText().length() > 60) {
            this.vista.jtfRazonSocial.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 60 para Razón social.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else {
            entidad = this.vista.jtfRazonSocial.getText().trim();
        }
        /*
         * VALIDAR NOMBRE FANTASIA
         */

        String nombreFantasia = this.vista.jtfNombreFantasia.getText().trim();
        if (nombreFantasia.length() > 120) {
            this.vista.jtfRazonSocial.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 60 para Razón social.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else if (nombreFantasia.isEmpty()) {
            nombreFantasia = null;
        }
        /*
         if (this.vista.jtfNombreFantasia.getText().isEmpty()) {
         this.vista.jtfNombreFantasia.setBackground(Color.red);
         javax.swing.JOptionPane.showMessageDialog(this.vista,
         "El campo Nombre fantasía esta vacio",
         "Parametros incorrectos",
         javax.swing.JOptionPane.OK_OPTION);
         this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
         return;
         } else {
         nombreFantasia = this.vista.jtfNombreFantasia.getText();
         }*/
        /*
         * VALIDAR R.U.C.
         */
        String ruc = this.vista.jtfRUC.getText().trim();
        if (ruc.length() > 30) {
            this.vista.jtfRUC.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 30 para R.U.C.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else if (ruc.isEmpty()) {
            ruc = null;
        }
        /*
         if (this.vista.jtfRUC.getText().isEmpty()) {
         this.vista.jtfRUC.setBackground(Color.red);
         javax.swing.JOptionPane.showMessageDialog(this.vista,
         "El campo R.U.C. esta vacio",
         "Parametros incorrectos",
         javax.swing.JOptionPane.OK_OPTION);
         this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
         return;
         } else {
         ruc = this.vista.jtfRUC.getText();
         }*/
        /*
         * VALIDAR R.U.C. ID
         */
        String rucId = this.vista.jtfRUC_ID.getText().trim();
        if (rucId.length() > 30) {
            this.vista.jtfRUC_ID.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 3 para Division R.U.C.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else if (rucId.isEmpty()) {
            rucId = null;
        }
        /*
         if (this.vista.jtfRUC_ID.getText().isEmpty()) {
         this.vista.jtfRUC_ID.setBackground(Color.red);
         javax.swing.JOptionPane.showMessageDialog(this.vista,
         "El campo R.U.C. División esta vacio",
         "Parametros incorrectos",
         javax.swing.JOptionPane.OK_OPTION);
         this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
         return;
         } else {
         rucId = this.vista.jtfRUC_ID.getText();

         }*/
        String descripcion = this.vista.jtfDescripcion.getText().trim();
        if (descripcion.length() > 120) {
            this.vista.jtfDescripcion.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 120 para descripción.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else if (descripcion.isEmpty()) {
            descripcion = null;
        }
        String direccion = this.vista.jtfDireccion.getText().trim();
        if (direccion.length() > 120) {
            this.vista.jtfDireccion.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 120 para dirección.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else if (direccion.isEmpty()) {
            direccion = null;
        }
        String email = this.vista.jtfemail.getText().trim();
        if (email.length() > 120) {
            this.vista.jtfemail.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 30 para e-mail.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else if (email.isEmpty()) {
            email = null;
        }
        String pagWeb = this.vista.jtfPagWeb.getText().trim();
        if (pagWeb.length() > 120) {
            this.vista.jtfPagWeb.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 30 para Web.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else if (pagWeb.isEmpty()) {
            pagWeb = null;
        }
        String obs = this.vista.jtaNota.getText().trim();
        if (obs.length() > 120) {
            this.vista.jtaNota.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 120 para Observacion.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else if (obs.isEmpty()) {
            obs = null;
        }
        M_proveedor proveedor = new M_proveedor();
        proveedor.setEntidad(entidad);
        proveedor.setNombre(nombreFantasia);
        proveedor.setRuc(ruc);
        proveedor.setRuc_id(rucId);
        proveedor.setDescripcion(descripcion);
        proveedor.setDireccion(direccion);
        proveedor.setEmail(email);
        proveedor.setPagWeb(pagWeb);
        proveedor.setObservacion(obs);
        boolean b = modelo.insertarProveedor(proveedor, sucursal, telefono);
        if (b) {
            actualizarTablaProveedores();
            cerrar();
        }
    }

    public void recibirContacto(M_contacto contacto) {
        modelo.contactos.add(contacto);
        int cantContactos = modelo.dtmContacto.getRowCount();
        for (int i = 0; i < cantContactos; i++) {
            modelo.dtmContacto.removeRow(0);
        }
        for (int i = 0; i < modelo.contactos.size(); i++) {
            Object[] fila = {modelo.contactos.get(i).getNombre(), modelo.contactos.get(i).getApellido(), modelo.contactos.get(i).getTelefono()};
            modelo.dtmContacto.addRow(fila);
        }
    }

    private void modificarContacto(String tipoTelefono, String nroTelefono, String observacion) {
        /*int row = this.vista.jtTelefono.getSelectedRow();
         dtmTelefono.setValueAt(nroTelefono, row, 0);
         dtmTelefono.setValueAt(tipoTelefono, row, 1);
         dtmTelefono.setValueAt(observacion, row, 2);
         this.vista.jbQuitarTelefono.setEnabled(false);
         this.vista.jbModTelefono.setEnabled(false);*/
    }

    private void quitarContacto() {
        modelo.contactos.remove(this.vista.jtContacto.getSelectedRow());
        modelo.dtmContacto.removeRow(this.vista.jtContacto.getSelectedRow());
        this.vista.jbQuitarContacto.setEnabled(false);
        this.vista.jbModContacto.setEnabled(false);
    }

    private void actualizarTablaProveedores() {
        this.padre.vista.jtProveedor.setModel(modelo.consultarProveedor("", true, true, false));
        Utilities.c_packColumn.packColumns(this.padre.vista.jtProveedor, 2);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbAgregarContacto)) {
            AgregarContacto crear_contacto = new AgregarContacto(this);
            crear_contacto.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbQuitarContacto)) {
            quitarContacto();
        }
        if (e.getSource().equals(this.vista.jbAgregarSucursal)) {
            C_crear_sucursal crear_sucursal = new C_crear_sucursal(this);
            crear_sucursal.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbQuitarSucursal)) {
            quitarSucursal();
        }
        if (e.getSource().equals(this.vista.jbAgregarTelefono)) {
            C_crear_telefono crear_telefono = new C_crear_telefono(this);
            crear_telefono.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbQuitarTelefono)) {
            quitarTelefono();
        }
        if (e.getSource().equals(this.vista.jbAceptar)) {
            validarDatos();
        }
        if (e.getSource().equals(this.vista.jbCancelar)) {
            cerrar();
        }
        if (e.getSource().equals(this.vista.jbModTelefono)) {
            C_modificar_telefono modificar_telefono = new C_modificar_telefono(this);
            modificar_telefono.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbModSucursal)) {
            C_modificar_sucursal modificar_sucursal = new C_modificar_sucursal(this);
            modificar_sucursal.mostrarVista();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.vista.jtfRazonSocial.hasFocus()) {
            this.vista.jtfRazonSocial.setBackground(Color.white);
        } else if (this.vista.jtfNombreFantasia.hasFocus()) {
            this.vista.jtfNombreFantasia.setBackground(Color.white);
        } else if (this.vista.jtfRUC.hasFocus()) {
            this.vista.jtfRUC.setBackground(Color.white);
        } else if (this.vista.jtfRUC_ID.hasFocus()) {
            this.vista.jtfRUC_ID.setBackground(Color.white);
        } else if (this.vista.jtfDescripcion.hasFocus()) {
            this.vista.jtfDescripcion.setBackground(Color.white);
        } else if (this.vista.jtfDireccion.hasFocus()) {
            this.vista.jtfDireccion.setBackground(Color.white);
        } else if (this.vista.jtfPagWeb.hasFocus()) {
            this.vista.jtfPagWeb.setBackground(Color.white);
        } else if (this.vista.jtfemail.hasFocus()) {
            this.vista.jtfemail.setBackground(Color.white);
        } else if (this.vista.jtaNota.hasFocus()) {
            this.vista.jtaNota.setBackground(Color.white);
        }

        if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jpDatosGenerales)) {
            int fila = this.vista.jtTelefono.rowAtPoint(e.getPoint());
            int columna = this.vista.jtTelefono.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbQuitarTelefono.setEnabled(true);
            } else {
                this.vista.jbQuitarTelefono.setEnabled(false);
            }
        }
        if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jpDatosSucursal)) {
            int fila = this.vista.jtSucursal.rowAtPoint(e.getPoint());
            int columna = this.vista.jtSucursal.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbQuitarSucursal.setEnabled(true);
            } else {
                this.vista.jbQuitarSucursal.setEnabled(false);
            }
        }
        if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jpDatosContacto)) {
            int fila = this.vista.jtContacto.rowAtPoint(e.getPoint());
            int columna = this.vista.jtContacto.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbQuitarContacto.setEnabled(true);
            } else {
                this.vista.jbQuitarContacto.setEnabled(false);
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
}
