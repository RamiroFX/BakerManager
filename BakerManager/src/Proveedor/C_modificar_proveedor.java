/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import Contacto.AgregarContacto;
import Contacto.ModificarContacto;
import DB_manager.DB_Proveedor;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_modificar_proveedor extends MouseAdapter implements ActionListener, KeyListener {

    public V_crear_proveedor vista;
    C_gestion_proveedores padre;
    DefaultTableModel dtmSucursal, dtmTelefono;
    M_proveedor proveedor;
    int idTelefono, idContacto;

    public C_modificar_proveedor(C_gestion_proveedores padre, int idProveedor) {
        this.padre = padre;
        this.vista = new V_crear_proveedor(this.padre.c_inicio.vista, true, false);
        inicializarVista(idProveedor);
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setLocationRelativeTo(this.vista);
        this.vista.setVisible(true);
    }

    private void inicializarVista(int idProveedor) {
        proveedor = DB_Proveedor.obtenerDatosProveedorID(idProveedor);
        this.vista.jtfRazonSocial.setText(proveedor.getEntidad());
        this.vista.jtfNombreFantasia.setText(proveedor.getNombre());
        this.vista.jtfDireccion.setText(proveedor.getDireccion());
        this.vista.jtfRUC.setText(proveedor.getRuc());
        this.vista.jtfRUC_ID.setText(proveedor.getRuc_id());
        this.vista.jtfPagWeb.setText(proveedor.getPagWeb());
        this.vista.jtfemail.setText(proveedor.getEmail());
        this.vista.jtfDescripcion.setText(proveedor.getDescripcion());
        this.vista.jtaNota.setText(proveedor.getObservacion());
        this.vista.jbModContacto.setEnabled(false);
        this.vista.jbQuitarContacto.setEnabled(false);
        inicializarTablaTelefono(idProveedor);
        inicializarTablaSucursal(idProveedor);
        inicializarTablaContacto(idProveedor);
    }

    private void inicializarTablaTelefono(int idProveedor) {
        dtmTelefono = new DefaultTableModel();
        dtmTelefono.addColumn("ID");
        dtmTelefono.addColumn("Telefono");
        dtmTelefono.addColumn("Tipo Telefono");
        dtmTelefono.addColumn("Observacion");
        TableModel tm = DB_Proveedor.obtenerProveedorTelefonoCompleto(idProveedor);
        int cantFila = tm.getRowCount();
        int cantCol = tm.getColumnCount();
        for (int i = 0; i < cantFila; i++) {
            Object[] o = new Object[cantCol];
            for (int x = 0; x < cantCol; x++) {
                o[x] = tm.getValueAt(i, x);
            }
            dtmTelefono.addRow(o);
        }
        this.vista.jtTelefono.setModel(dtmTelefono);

        this.vista.jbQuitarSucursal.setEnabled(false);
        this.vista.jbQuitarTelefono.setEnabled(false);
        this.vista.jbModTelefono.setEnabled(false);
        this.vista.jbModSucursal.setEnabled(false);
    }

    private void inicializarTablaSucursal(int idProveedor) {
        dtmSucursal = new DefaultTableModel();
        dtmSucursal.addColumn("ID");
        dtmSucursal.addColumn("Direccion");
        dtmSucursal.addColumn("Telefono");

        TableModel tm = DB_Proveedor.obtenerSucursal(idProveedor);
        int cantFila = tm.getRowCount();
        int cantCol = tm.getColumnCount();
        for (int i = 0; i < cantFila; i++) {
            Object[] o = new Object[cantCol];
            for (int x = 0; x < cantCol; x++) {
                o[x] = tm.getValueAt(i, x);
            }
            dtmSucursal.addRow(o);
        }
        this.vista.jtSucursal.setModel(dtmSucursal);
    }

    private void inicializarTablaContacto(int idProveedor) {
        this.vista.jtContacto.setModel(DB_Proveedor.obtenerProveedorContacto(idProveedor));
    }

    public void recibirTelefono(String tipoTelefono, String nroTelefono, String observacion) {
        DB_Proveedor.insertarTelefono(proveedor.getId(), tipoTelefono, nroTelefono, observacion);
        this.vista.jtTelefono.setModel(DB_Proveedor.obtenerProveedorTelefonoCompleto(proveedor.getId()));
        Utilities.c_packColumn.packColumns(this.vista.jtTelefono, 1);
    }

    public void modificarTelefono(String tipoTelefono, String nroTelefono, String observacion) {
        int id_telefono = Integer.valueOf(String.valueOf(this.vista.jtTelefono.getValueAt(this.vista.jtTelefono.getSelectedRow(), 0)));
        DB_Proveedor.modificarTelefono(id_telefono, tipoTelefono, nroTelefono, observacion);
        this.vista.jtTelefono.setModel(DB_Proveedor.obtenerProveedorTelefonoCompleto(proveedor.getId()));
        Utilities.c_packColumn.packColumns(this.vista.jtTelefono, 1);
        this.vista.jbQuitarTelefono.setEnabled(false);
        this.vista.jbModTelefono.setEnabled(false);
    }

    private void quitarTelefono() {
        int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int id_telefono = Integer.valueOf(String.valueOf(this.vista.jtTelefono.getValueAt(this.vista.jtTelefono.getSelectedRow(), 0)));
                DB_Proveedor.eliminarTelefonoProveedor(id_telefono);
                this.vista.jbQuitarTelefono.setEnabled(false);
                this.vista.jbModTelefono.setEnabled(false);
                this.vista.jtTelefono.setModel(DB_Proveedor.obtenerProveedorTelefonoCompleto(proveedor.getId()));
                Utilities.c_packColumn.packColumns(this.vista.jtTelefono, 1);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void recibirSucursal(String direccion, String telefono) {
        DB_Proveedor.insertarSucursal(proveedor.getId(), direccion, telefono);
        this.vista.jtSucursal.setModel(DB_Proveedor.obtenerSucursal(proveedor.getId()));
        Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
    }

    public void modificarSucursal(String direccion, String telefono) {
        this.vista.jbQuitarSucursal.setEnabled(false);
        this.vista.jbModSucursal.setEnabled(false);
        int id_sucursal = Integer.valueOf(String.valueOf(this.vista.jtSucursal.getValueAt(this.vista.jtSucursal.getSelectedRow(), 0)));
        DB_Proveedor.modificarSucursal(id_sucursal, direccion, telefono);
        this.vista.jtSucursal.setModel(DB_Proveedor.obtenerSucursal(proveedor.getId()));
        Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
    }

    private void quitarSucursal() {
        int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int id_sucursal = Integer.valueOf(String.valueOf(this.vista.jtSucursal.getValueAt(this.vista.jtSucursal.getSelectedRow(), 0)));
                DB_Proveedor.eliminarSucursal(id_sucursal);
                this.vista.jbQuitarSucursal.setEnabled(false);
                this.vista.jbModSucursal.setEnabled(false);
                this.vista.jtSucursal.setModel(DB_Proveedor.obtenerSucursal(proveedor.getId()));
                Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void recibirContacto(M_contacto contacto) {
        DB_Proveedor.insertarProveedorContacto(proveedor.getId(), contacto);
        this.vista.jtContacto.setModel(DB_Proveedor.obtenerProveedorContacto(proveedor.getId()));
        Utilities.c_packColumn.packColumns(this.vista.jtContacto, 1);
    }

    public void modificarContacto(M_contacto contacto) {
        this.vista.jbQuitarContacto.setEnabled(false);
        this.vista.jbModContacto.setEnabled(false);
        DB_Proveedor.modificarProveedorContacto(proveedor.getId(), contacto);
        this.vista.jtContacto.setModel(DB_Proveedor.obtenerProveedorContacto(proveedor.getId()));
        Utilities.c_packColumn.packColumns(this.vista.jtContacto, 1);
    }

    private void quitarContacto() {
        int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int idContacto = Integer.valueOf(String.valueOf(this.vista.jtContacto.getValueAt(this.vista.jtContacto.getSelectedRow(), 0)));
                M_contacto contacto_temp = DB_Proveedor.obtenerDatosContactoIdContacto(idContacto);
                DB_Proveedor.eliminarProveedorContacto(contacto_temp);
                this.vista.jbQuitarContacto.setEnabled(false);
                this.vista.jbModContacto.setEnabled(false);
                this.vista.jtContacto.setModel(DB_Proveedor.obtenerProveedorContacto(proveedor.getId()));
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
         */
        if (this.vista.jtfRazonSocial.getText().isEmpty()) {
            this.vista.jtfRazonSocial.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo Razón social esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else {
            proveedor.setEntidad(this.vista.jtfRazonSocial.getText());
        }
        /*
         * VALIDAR NOMBRE FANTASIA
         */
        if (this.vista.jtfNombreFantasia.getText().isEmpty()) {
            this.vista.jtfNombreFantasia.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo Nombre fantasía esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else {
            proveedor.setNombre(this.vista.jtfNombreFantasia.getText());
        }
        /*
         * VALIDAR R.U.C.
         */
        if (this.vista.jtfRUC.getText().isEmpty()) {
            this.vista.jtfRUC.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo R.U.C. esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else {
            proveedor.setRuc(this.vista.jtfRUC.getText());
        }
        /*
         * VALIDAR R.U.C. ID
         */
        if (this.vista.jtfRUC_ID.getText().isEmpty()) {
            this.vista.jtfRUC_ID.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo R.U.C. División esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosGenerales);
            return;
        } else {
            proveedor.setRuc_id(this.vista.jtfRUC_ID.getText());
        }
        proveedor.setDescripcion(this.vista.jtfDescripcion.getText());
        proveedor.setDireccion(this.vista.jtfDireccion.getText());
        proveedor.setEmail(this.vista.jtfemail.getText());
        proveedor.setPagWeb(this.vista.jtfPagWeb.getText());
        proveedor.setObservacion(this.vista.jtaNota.getText());
        DB_Proveedor.modificarProveedor(proveedor);
        cerrar();
    }

    private void actualizarTablaProveedores() {
        this.padre.vista.jtProveedor.setModel(DB_Proveedor.consultarProveedor(" ", true, true, false));
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
