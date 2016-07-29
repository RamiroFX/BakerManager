/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB_manager.DB_Producto;
import DB_manager.DB_Proveedor;
import DB_manager.DB_manager;
import Entities.M_producto;
import Entities.M_proveedor;
import Proveedor.Seleccionar_proveedor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JDialog;

/**
 *
 * @author Administrador
 */
public class C_crear_producto implements ActionListener {

    public C_gestion_producto c_producto;
    private M_producto producto;
    private M_proveedor proveedor;
    public V_crear_producto vista;

    public C_crear_producto(C_gestion_producto jifProductos) {
        this.c_producto = jifProductos;
        this.producto = new M_producto();
        this.proveedor = new M_proveedor();
        this.vista = new V_crear_producto(jifProductos.c_inicio.vista);
        inicializarVista();
        agregarListeners();
    }

    public C_crear_producto(JDialog c_inicio) {
        this.producto = new M_producto();
        this.proveedor = new M_proveedor();
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
        Vector impuesto = DB_manager.obtenerImpuesto();
        for (int i = 0; i < impuesto.size(); i++) {
            this.vista.jcbImpuesto.addItem(impuesto.get(i));
        }
        Vector rubro = DB_manager.obtenerRubro();
        for (int i = 0; i < rubro.size(); i++) {
            this.vista.jcbRubro.addItem(rubro.get(i));
        }
        Vector marca = DB_manager.obtenerMarca();
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
            if (this.vista.jtfProducto.getText().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el nombre del producto", "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                return;
            }

            String prodDescripcion = this.vista.jtfProducto.getText();
            if (DB_Producto.existeProducto(prodDescripcion)) {
                javax.swing.JOptionPane.showMessageDialog(this.vista, "El nombre del producto se encuentra en uso. Verifique el nombre del producto", "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                return;
            }
            //producto.setId(Integer.valueOf(this.vista.jtfCodigo.getText()));
            producto.setDescripcion(prodDescripcion);
            //producto.setCodBarra(Integer.valueOf(this.vista.jtfCodigo.getText()));
            producto.setMarca((String) this.vista.jcbMarca.getSelectedItem());
            producto.setImpuesto((Integer.valueOf((String) this.vista.jcbImpuesto.getSelectedItem())));
            producto.setRubro((String) this.vista.jcbRubro.getSelectedItem());
            producto.setEstado("1");
            producto.setPrecioCosto(Integer.valueOf(this.vista.jtfPrecioCosto.getText()));
            producto.setPrecioMayorista(Integer.valueOf(this.vista.jtfPrecioMayorista.getText()));
            producto.setPrecioVenta(Integer.valueOf(this.vista.jtfPrecioVta.getText()));
            producto.setCantActual(0.0);
            int idCategoria = DB_manager.obtenerIdProductoCategoria((String) this.vista.jcbRubro.getSelectedItem());
            int idMarca = DB_manager.obtenerIdMarca((String) this.vista.jcbMarca.getSelectedItem());
            int idImpuesto = DB_manager.obtenerIdImpuesto((String) this.vista.jcbImpuesto.getSelectedItem());
            producto.setIdCategoria(idCategoria);
            producto.setIdEstado(1);//Activo
            producto.setIdImpuesto(idImpuesto);
            producto.setIdMarca(idMarca);
            long id_producto = DB_Producto.insertarProducto(producto);
            DB_Producto.insertarCodigoProducto(id_producto);
            if (this.vista.jckBProveedor.isSelected() && !this.vista.jtfProveedor.getText().isEmpty()) {
                DB_Proveedor.insertarProveedorProducto(proveedor.getId(), (int) id_producto);
            }
            cerrar();
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
        }
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private java.awt.Dimension establecerTamañoPanel() {
        return new java.awt.Dimension((int) (this.c_producto.vista.getWidth() * 0.8), (int) (this.c_producto.vista.getHeight() * 0.8));
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
            this.proveedor = null;
            this.vista.jtfProveedor.setText("");
        }
    }

    public void recibirProveedor(M_proveedor proveedor) {
        this.proveedor = proveedor;
        String nombre = this.proveedor.getNombre();
        String entidad = this.proveedor.getEntidad();
        this.vista.jtfProveedor.setText(nombre + " (" + entidad + ")");
    }
}
