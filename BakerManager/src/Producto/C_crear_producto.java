/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import Entities.M_producto;
import Entities.M_proveedor;
import Proveedor.Seleccionar_proveedor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class C_crear_producto implements ActionListener {

    public C_gestion_producto c_producto;
    private M_crear_producto modelo;
    public V_crear_producto vista;

    public C_crear_producto(M_crear_producto modelo, V_crear_producto vista, C_gestion_producto jifProductos) {
        this.c_producto = jifProductos;
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_crear_producto(JDialog c_inicio) {
        this.modelo = new M_crear_producto();
        this.vista = new V_crear_producto(c_inicio);
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        //this.vista.setSize(establecerTamañoPanel());
        this.vista.setSize(800, 600);
        this.vista.setLocationRelativeTo(this.vista.getParent());
        this.vista.setVisible(true);
    }

    private void inicializarVista() {
        this.vista.jtfCodigo.setText("");
        Vector impuesto = modelo.obtenerImpuesto();
        for (int i = 0; i < impuesto.size(); i++) {
            this.vista.jcbImpuesto.addItem(impuesto.get(i));
        }
        Vector rubro = modelo.obtenerRubro();
        for (int i = 0; i < rubro.size(); i++) {
            this.vista.jcbCategoria.addItem(rubro.get(i));
        }
        Vector marca = modelo.obtenerMarca();
        for (int i = 0; i < marca.size(); i++) {
            this.vista.jcbMarca.addItem(marca.get(i));
        }
    }

    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jckBProveedor.addActionListener(this);
        this.vista.jbProveedor.addActionListener(this);
    }

    private void creaProducto() {
        try {
            M_producto producto = new M_producto();
            if (this.vista.jtfProducto.getText().isEmpty() || this.vista.jtfProducto.getText().length() > 50) {
                javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el nombre del producto. Máximo 50 caracteres permitidos.", "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                return;
            }
            try {
                producto.setPrecioCosto(Integer.valueOf(this.vista.jtfPrecioCosto.getText()));
                if (producto.getPrecioCosto() > 999999999) {
                    JOptionPane.showMessageDialog(vista, "Precio de costo. Máximo 9 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                    this.vista.jtfPrecioCosto.setText("");
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, "Ingrese un precio de costo válido. Solo números enteros.", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                producto.setPrecioMayorista(Integer.valueOf(this.vista.jtfPrecioMayorista.getText()));
                if (producto.getPrecioMayorista() > 999999999) {
                    JOptionPane.showMessageDialog(vista, "Precio mayorista. Máximo 9 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                    this.vista.jtfPrecioMayorista.setText("");
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, "Ingrese un precio mayorista válido. Solo números enteros.", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                producto.setPrecioVenta(Integer.valueOf(this.vista.jtfPrecioVta.getText()));
                if (producto.getPrecioVenta() > 999999999) {
                    JOptionPane.showMessageDialog(vista, "Precio de venta. Máximo 9 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                    this.vista.jtfPrecioVta.setText("");
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, "Ingrese un precio de venta válido. Solo números enteros.", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
            producto.setCantActual(0.0);
            producto.setDescripcion(this.vista.jtfProducto.getText());
            //producto.setCodBarra(Integer.valueOf(this.vista.jtfCodigo.getText()));
            producto.setMarca((String) this.vista.jcbMarca.getSelectedItem());
            producto.setImpuesto((Integer.valueOf((String) this.vista.jcbImpuesto.getSelectedItem())));
            producto.setCategoria((String) this.vista.jcbCategoria.getSelectedItem());
            producto.setEstado("Activo");
            if (modelo.crearProducto(producto, this.vista.jckBProveedor.isSelected())) {
                JOptionPane.showMessageDialog(vista, "Producto creado", "Exito", JOptionPane.PLAIN_MESSAGE);
                cerrar();
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Uno de los datos ingresados es erróneo.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
        }
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            creaProducto();
        } else if (e.getSource() == this.vista.jckBProveedor) {
            checkBoxProveedorHandler();
        } else if (e.getSource() == this.vista.jbProveedor) {
            Seleccionar_proveedor sp = new Seleccionar_proveedor(this);
            sp.mostrarVista();
        } else if (e.getSource() == this.vista.jbCancelar) {
            cerrar();
        }
    }

    private void checkBoxProveedorHandler() {
        if (this.vista.jckBProveedor.isSelected()) {
            this.vista.jbProveedor.setEnabled(true);
        } else {
            this.vista.jbProveedor.setEnabled(false);
            this.modelo.proveedor = null;
            this.vista.jtfProveedor.setText("");
        }
    }

    public void recibirProveedor(M_proveedor proveedor) {
        this.modelo.proveedor = proveedor;
        String nombre = this.modelo.proveedor.getNombre();
        String entidad = this.modelo.proveedor.getEntidad();
        this.vista.jtfProveedor.setText(nombre + " (" + entidad + ")");
    }
}
