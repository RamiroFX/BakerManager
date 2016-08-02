/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import Contacto.AgregarContacto;
import Contacto.ModificarContacto;
import Entities.M_contacto;
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
public class C_modificar_proveedor extends MouseAdapter implements ActionListener, KeyListener {

    public V_crear_proveedor vista;
    private M_modificar_proveedor modelo;
    C_gestion_proveedores padre;
    int idContacto;

    public C_modificar_proveedor(M_modificar_proveedor modelo, V_crear_proveedor vista, C_gestion_proveedores padre) {
        this.padre = padre;
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setLocationRelativeTo(this.vista);
        this.vista.setVisible(true);
    }

    private void inicializarVista() {
        this.vista.jtfRazonSocial.setText(modelo.proveedor.getEntidad());
        this.vista.jtfNombreFantasia.setText(modelo.proveedor.getNombre());
        this.vista.jtfDireccion.setText(modelo.proveedor.getDireccion());
        this.vista.jtfRUC.setText(modelo.proveedor.getRuc());
        this.vista.jtfRUC_ID.setText(modelo.proveedor.getRuc_id());
        this.vista.jtfPagWeb.setText(modelo.proveedor.getPagWeb());
        this.vista.jtfemail.setText(modelo.proveedor.getEmail());
        this.vista.jtfDescripcion.setText(modelo.proveedor.getDescripcion());
        this.vista.jtaNota.setText(modelo.proveedor.getObservacion());
        this.vista.jbModContacto.setEnabled(false);
        this.vista.jbQuitarContacto.setEnabled(false);
        inicializarTablaTelefono();
        inicializarTablaSucursal();
        inicializarTablaContacto();
    }

    private void inicializarTablaTelefono() {
        this.vista.jtTelefono.setModel(modelo.dtmTelefono);
        this.vista.jbQuitarSucursal.setEnabled(false);
        this.vista.jbQuitarTelefono.setEnabled(false);
        this.vista.jbModTelefono.setEnabled(false);
        this.vista.jbModSucursal.setEnabled(false);
    }

    private void inicializarTablaSucursal() {
        this.vista.jtSucursal.setModel(modelo.dtmSucursal);
    }

    private void inicializarTablaContacto() {
        this.vista.jtContacto.setModel(modelo.obtenerProveedorContacto());
    }

    public void recibirTelefono(String tipoTelefono, String nroTelefono, String observacion) {
        modelo.insertarTelefono(tipoTelefono, nroTelefono, observacion);
        this.vista.jtTelefono.setModel(modelo.obtenerProveedorTelefonoCompleto());
        Utilities.c_packColumn.packColumns(this.vista.jtTelefono, 1);
    }

    public void modificarTelefono(String tipoTelefono, String nroTelefono, String observacion) {
        int id_telefono = Integer.valueOf(String.valueOf(this.vista.jtTelefono.getValueAt(this.vista.jtTelefono.getSelectedRow(), 0)));
        modelo.modificarTelefono(id_telefono, tipoTelefono, nroTelefono, observacion);
        this.vista.jtTelefono.setModel(modelo.obtenerProveedorTelefonoCompleto());
        Utilities.c_packColumn.packColumns(this.vista.jtTelefono, 1);
        this.vista.jbQuitarTelefono.setEnabled(false);
        this.vista.jbModTelefono.setEnabled(false);
    }

    private void quitarTelefono() {
        int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int id_telefono = Integer.valueOf(String.valueOf(this.vista.jtTelefono.getValueAt(this.vista.jtTelefono.getSelectedRow(), 0)));
                modelo.eliminarTelefonoProveedor(id_telefono);
                this.vista.jbQuitarTelefono.setEnabled(false);
                this.vista.jbModTelefono.setEnabled(false);
                this.vista.jtTelefono.setModel(modelo.obtenerProveedorTelefonoCompleto());
                Utilities.c_packColumn.packColumns(this.vista.jtTelefono, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void recibirSucursal(String direccion, String telefono) {
        modelo.insertarSucursal(direccion, telefono);
        this.vista.jtSucursal.setModel(modelo.obtenerSucursal());
        Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
    }

    public void modificarSucursal(String direccion, String telefono) {
        this.vista.jbQuitarSucursal.setEnabled(false);
        this.vista.jbModSucursal.setEnabled(false);
        int id_sucursal = Integer.valueOf(String.valueOf(this.vista.jtSucursal.getValueAt(this.vista.jtSucursal.getSelectedRow(), 0)));
        modelo.modificarSucursal(id_sucursal, direccion, telefono);
        this.vista.jtSucursal.setModel(modelo.obtenerSucursal());
        Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
    }

    private void quitarSucursal() {
        int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int id_sucursal = Integer.valueOf(String.valueOf(this.vista.jtSucursal.getValueAt(this.vista.jtSucursal.getSelectedRow(), 0)));
                modelo.eliminarSucursal(id_sucursal);
                this.vista.jbQuitarSucursal.setEnabled(false);
                this.vista.jbModSucursal.setEnabled(false);
                this.vista.jtSucursal.setModel(modelo.obtenerSucursal());
                Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void recibirContacto(M_contacto contacto) {
        modelo.insertarProveedorContacto(contacto);
        this.vista.jtContacto.setModel(modelo.obtenerProveedorContacto());
        Utilities.c_packColumn.packColumns(this.vista.jtContacto, 1);
    }

    public void modificarContacto(M_contacto contacto) {
        this.vista.jbQuitarContacto.setEnabled(false);
        this.vista.jbModContacto.setEnabled(false);
        modelo.modificarProveedorContacto(contacto);
        this.vista.jtContacto.setModel(modelo.obtenerProveedorContacto());
        Utilities.c_packColumn.packColumns(this.vista.jtContacto, 1);
    }

    private void quitarContacto() {
        int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int idContacto = Integer.valueOf(String.valueOf(this.vista.jtContacto.getValueAt(this.vista.jtContacto.getSelectedRow(), 0)));
                M_contacto contacto_temp = modelo.obtenerDatosContactoIdContacto(idContacto);
                modelo.eliminarProveedorContacto(contacto_temp);
                this.vista.jbQuitarContacto.setEnabled(false);
                this.vista.jbModContacto.setEnabled(false);
                this.vista.jtContacto.setModel(modelo.obtenerProveedorContacto());
                Utilities.c_packColumn.packColumns(this.vista.jtContacto, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void agregarListeners() {
        this.vista.jbAgregarSucursal.addActionListener(this);
        this.vista.jbModSucursal.addActionListener(this);
        this.vista.jbQuitarSucursal.addActionListener(this);
        this.vista.jbAgregarTelefono.addActionListener(this);
        this.vista.jbModTelefono.addActionListener(this);
        this.vista.jbQuitarTelefono.addActionListener(this);
        this.vista.jbAgregarContacto.addActionListener(this);
        this.vista.jbModContacto.addActionListener(this);
        this.vista.jbQuitarContacto.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jtTelefono.addMouseListener(this);
        this.vista.jtSucursal.addMouseListener(this);
        this.vista.jtContacto.addMouseListener(this);
    }

    private void validarDatos() {
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
        boolean b = modelo.modificarProveedor(proveedor);
        if (b) {
            actualizarTablaProveedores();
            cerrar();
        }
    }

    private void actualizarTablaProveedores() {
        this.padre.vista.jtProveedor.setModel(modelo.consultarProveedor("", true, true, false));
        Utilities.c_packColumn.packColumns(this.padre.vista.jtProveedor, 2);
    }

    private void cerrar() {
        actualizarTablaProveedores();
        this.vista.dispose();
        System.runFinalization();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbAgregarSucursal)) {
            C_crear_sucursal crear_sucursal = new C_crear_sucursal(this);
            crear_sucursal.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbModSucursal)) {
            C_modificar_sucursal modificar_sucursal = new C_modificar_sucursal(this);
            modificar_sucursal.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbQuitarSucursal)) {
            quitarSucursal();
        }
        if (e.getSource().equals(this.vista.jbAgregarTelefono)) {
            C_crear_telefono crear_telefono = new C_crear_telefono(this);
            crear_telefono.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbModTelefono)) {
            C_modificar_telefono modificar_telefono = new C_modificar_telefono(this);
            modificar_telefono.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbQuitarTelefono)) {
            quitarTelefono();
        }
        if (e.getSource().equals(this.vista.jbAgregarContacto)) {
            AgregarContacto crear_contacto = new AgregarContacto(this);
            crear_contacto.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbModContacto)) {
            ModificarContacto modContacto = new ModificarContacto(this, idContacto);
            modContacto.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbQuitarContacto)) {
            quitarContacto();
        }
        if (e.getSource().equals(this.vista.jbAceptar)) {
            validarDatos();
        }
        if (e.getSource().equals(this.vista.jbCancelar)) {
            cerrar();
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
        }
        if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jpDatosGenerales)) {
            int fila = this.vista.jtTelefono.rowAtPoint(e.getPoint());
            int columna = this.vista.jtTelefono.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbQuitarTelefono.setEnabled(true);
                this.vista.jbModTelefono.setEnabled(true);
            } else {
                this.vista.jbQuitarTelefono.setEnabled(false);
                this.vista.jbModTelefono.setEnabled(false);
            }
        }
        if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jpDatosSucursal)) {
            int fila = this.vista.jtSucursal.rowAtPoint(e.getPoint());
            int columna = this.vista.jtSucursal.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbQuitarSucursal.setEnabled(true);
                this.vista.jbModSucursal.setEnabled(true);
            } else {
                this.vista.jbQuitarSucursal.setEnabled(false);
                this.vista.jbModSucursal.setEnabled(false);
            }
        }
        if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jpDatosContacto)) {
            int fila = this.vista.jtContacto.rowAtPoint(e.getPoint());
            int columna = this.vista.jtContacto.columnAtPoint(e.getPoint());
            idContacto = Integer.valueOf(this.vista.jtContacto.getValueAt(fila, 0).toString());
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbQuitarContacto.setEnabled(true);
                this.vista.jbModContacto.setEnabled(true);
            } else {
                this.vista.jbQuitarContacto.setEnabled(false);
                this.vista.jbModContacto.setEnabled(false);
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
