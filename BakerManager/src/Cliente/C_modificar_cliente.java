/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Contacto.AgregarContacto;
import DB_manager.DB_Cliente;
import Entities.M_cliente;
import Entities.M_cliente_contacto;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_modificar_cliente extends MouseAdapter implements ActionListener, KeyListener {

    public V_crear_cliente vista;
    C_gestion_cliente padre;
    public DefaultTableModel dtmSucursal, dtmTelefono, dtmContacto;
    M_cliente cliente;
    ArrayList<M_cliente_contacto> contactos;

    public C_modificar_cliente(C_gestion_cliente padre, M_cliente cliente) {
        this.padre = padre;
        this.cliente = cliente;
        this.vista = new V_crear_cliente(this.padre.c_inicio.vista, true);
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setLocationRelativeTo(this.vista);
        this.vista.setVisible(true);
    }

    private void inicializarVista() {
        this.vista.setTitle("Modificar cliente");
        contactos = new ArrayList<>();
        this.vista.jbQuitarSucursal.setEnabled(false);
        this.vista.jbQuitarTelefono.setEnabled(false);
        this.vista.jbModTelefono.setEnabled(false);
        this.vista.jbModSucursal.setEnabled(false);
        this.vista.jbModContacto.setEnabled(false);
        this.vista.jbQuitarContacto.setEnabled(false);
        Vector tipo_cliente = DB_Cliente.obtenerTipoCliente();
        for (int i = 0; i < tipo_cliente.size(); i++) {
            this.vista.jcbTipoCliente.addItem(tipo_cliente.get(i));
        }
        Vector categoria_cliente = DB_Cliente.obtenerCategoriaCliente();
        for (int i = 0; i < categoria_cliente.size(); i++) {
            this.vista.jcbCategoriaCliente.addItem(categoria_cliente.get(i));
        }
        completarCampos();
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
        this.vista.jbAgregarContacto.addActionListener(this);
        this.vista.jbQuitarContacto.addActionListener(this);
        this.vista.jtSucursal.addMouseListener(this);
        this.vista.jtTelefono.addMouseListener(this);
        this.vista.jtContacto.addMouseListener(this);
    }

    private void completarCampos() {
        this.vista.jtfRazonSocial.setText(cliente.getEntidad());
        this.vista.jtfNombreFantasia.setText(cliente.getNombre());
        this.vista.jtfRUC.setText(cliente.getRuc());
        this.vista.jtaNota.setText(cliente.getObservacion());
        this.vista.jtfemail.setText(cliente.getEmail());
        this.vista.jtfPaginaWeb.setText(cliente.getPaginaWeb());
        this.vista.jtfDireccion.setText(cliente.getDireccion());
        this.vista.jtfRUC_ID.setText(cliente.getRucId());
        this.vista.jtfDireccion.setText(cliente.getDireccion());
        this.vista.jtfemail.setText(cliente.getEmail());
        this.vista.jtfPaginaWeb.setText(cliente.getPaginaWeb());
        this.vista.jcbCategoriaCliente.setSelectedItem(cliente.getCategoria());
        this.vista.jcbTipoCliente.setSelectedItem(cliente.getTipo());
        this.vista.jtaNota.setText(cliente.getObservacion());

        this.vista.jtTelefono.setModel(DB_Cliente.obtenerClienteTelefono(cliente.getIdCliente()));
        this.vista.jtSucursal.setModel(DB_Cliente.obtenerSucursal(cliente.getIdCliente()));
        this.vista.jtContacto.setModel(DB_Cliente.obtenerClienteContacto(cliente.getIdCliente()));
    }

    public void recibirContacto(M_cliente_contacto contacto) {
        DB_Cliente.insertarContacto(cliente.getIdCliente(), contacto);
    }

    private void quitarContacto() {
        int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int idContacto = Integer.valueOf(String.valueOf(this.vista.jtContacto.getValueAt(this.vista.jtContacto.getSelectedRow(), 0)));
                M_cliente_contacto contacto = DB_Cliente.obtenerDatosClienteContactoID(idContacto);
                DB_Cliente.eliminarContacto(contacto.getId_persona(), contacto.getIdClienteContacto(), contacto.getIdCliente());
                this.vista.jbQuitarContacto.setEnabled(false);
                this.vista.jbModContacto.setEnabled(false);
                this.vista.jtContacto.setModel(DB_Cliente.obtenerClienteContacto(cliente.getIdCliente()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void validarDatos() {
        /*int cantTel = dtmTelefono.getRowCount();
         M_telefono[] telefono = new M_telefono[cantTel];
         for (int i = 0; i < cantTel; i++) {
         telefono[i] = new M_telefono();
         telefono[i].setCategoria(dtmTelefono.getValueAt(i, 1).toString());
         telefono[i].setNumero(dtmTelefono.getValueAt(i, 0).toString());
         telefono[i].setObservacion(dtmTelefono.getValueAt(i, 2).toString());
         }
         int cantSuc = dtmSucursal.getRowCount();
         M_sucursal[] sucursal = new M_sucursal[cantSuc];
         for (int i = 0; i < cantSuc; i++) {
         sucursal[i] = new M_sucursal();
         sucursal[i].setDireccion(dtmSucursal.getValueAt(i, 0).toString());
         sucursal[i].setTelefono(dtmSucursal.getValueAt(i, 1).toString());
         }
         int cantContacto = dtmContacto.getRowCount();
         M_cliente_contacto[] contacto = new M_cliente_contacto[cantContacto];
         for (int i = 0; i < cantContacto; i++) {
         contacto[i] = new M_cliente_contacto();
         contacto[i].setDireccion(dtmContacto.getValueAt(i, 0).toString());
         contacto[i].setTelefono(dtmContacto.getValueAt(i, 1).toString());
         }*/
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
            cliente.setEntidad(this.vista.jtfRazonSocial.getText());
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
            cliente.setNombre(this.vista.jtfNombreFantasia.getText());
        }
        cliente.setRuc(this.vista.jtfRUC.getText());
        if (this.vista.jtaNota.getText().isEmpty()) {
            cliente.setObservacion(null);
        } else {
            cliente.setObservacion(this.vista.jtaNota.getText());
        }
        if (this.vista.jtfemail.getText().isEmpty()) {
            cliente.setEmail(null);
        } else {
            cliente.setEmail(this.vista.jtfemail.getText());
        }
        if (this.vista.jtfPaginaWeb.getText().isEmpty()) {
            cliente.setPaginaWeb(null);
        } else {
            cliente.setPaginaWeb(this.vista.jtfPaginaWeb.getText());
        }
        if (this.vista.jtfDireccion.getText().isEmpty()) {
            cliente.setDireccion(null);
        } else {
            cliente.setDireccion(this.vista.jtfDireccion.getText());
        }
        cliente.setRucId(this.vista.jtfRUC_ID.getText());
        cliente.setDireccion(this.vista.jtfDireccion.getText());
        cliente.setEmail(this.vista.jtfemail.getText());
        cliente.setPaginaWeb(this.vista.jtfPaginaWeb.getText());
        cliente.setCategoria(this.vista.jcbCategoriaCliente.getSelectedItem().toString());
        cliente.setTipo(this.vista.jcbTipoCliente.getSelectedItem().toString());
        cliente.setObservacion(this.vista.jtaNota.getText());
        DB_Cliente.actualizarCliente(cliente);
        actualizarTablaClientes();
        cerrar();
    }

    private void actualizarTablaClientes() {
        this.padre.vista.jtCliente.setModel(DB_Cliente.consultarCliente("", false, true, true));
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
        /*
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
         }*/
        if (e.getSource().equals(this.vista.jbAceptar)) {
            validarDatos();
        }
        if (e.getSource().equals(this.vista.jbCancelar)) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /*if (this.vista.jftCedulaIdentidad.hasFocus()) {
         this.vista.jftCedulaIdentidad.setBackground(Color.white);
         } else */
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
                this.vista.jbModSucursal.setEnabled(true);
            } else {
                this.vista.jbQuitarTelefono.setEnabled(false);
                this.vista.jbModSucursal.setEnabled(false);
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
