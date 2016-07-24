/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB_manager.DB_Producto;
import DB_manager.DB_manager;
import Entities.M_producto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class C_modificar_producto implements ActionListener {

    C_gestion_producto controlador;
    private M_producto producto;
    V_modificar_producto vista;

    public C_modificar_producto(C_gestion_producto c_jifProductos, M_producto producto) {
        this.controlador = c_jifProductos;
        this.producto = c_jifProductos.getProducto();
        this.vista = new V_modificar_producto(c_jifProductos.c_inicio);
        inicializarVista();
        agregarListeners();
        completarCampos();
    }

    public void mostrarVista() {
        this.vista.setSize(establecerTamañoPanel());
        this.vista.setLocationRelativeTo(this.controlador.c_inicio.vista);
        this.vista.setVisible(true);
    }

    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
    }

    private void inicializarVista() {
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
        Vector estado = DB_manager.obtenerEstado();
        for (int i = 0; i < estado.size(); i++) {
            this.vista.jcbSuspendido.addItem(estado.get(i));
        }
        /*        Vector respuesta = DB_manager.consultarRespuesta();
         for (int i = 0; i < respuesta.size(); i++) {
         this.jdModProd.jcbCalcularStock.addItem(respuesta.get(i));
         }*/
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private java.awt.Dimension establecerTamañoPanel() {
        return new java.awt.Dimension((int) (this.controlador.vista.getWidth() * 0.8), (int) (this.controlador.vista.getHeight() * 0.8));
    }

    private void completarCampos() {
        this.vista.jlTituloProducto.setText(this.producto.getDescripcion());
        this.vista.jtfProducto.setText(this.producto.getDescripcion());
        this.vista.jtfCodigo.setText(String.valueOf(this.producto.getId()));
        this.vista.jtfPrecioCosto.setText(String.valueOf(this.producto.getPrecioCosto()));
        this.vista.jtfPrecioMayorista.setText(String.valueOf(this.producto.getPrecioMayorista()));
        this.vista.jtfPrecioVta.setText(String.valueOf(this.producto.getPrecioVenta()));
        this.vista.jcbRubro.setSelectedItem(this.producto.getRubro());
        this.vista.jcbImpuesto.setSelectedItem(String.valueOf(this.producto.getImpuesto()));
        this.vista.jcbMarca.setSelectedItem(this.producto.getMarca());
        this.vista.jtfCantActual.setText(String.valueOf(this.producto.getCantActual()));
        this.vista.jcbSuspendido.setSelectedItem(this.producto.getEstado());
    }

    private void actualizarProducto() {
        DB_Producto.modificarProducto(producto);
    }

    private void modificarProductos() {
        try {
            producto.setDescripcion(this.vista.jtfProducto.getText());
            producto.setId(Integer.valueOf(this.vista.jtfCodigo.getText()));
            producto.setCodBarra(Integer.valueOf(this.vista.jtfCodigo.getText()));
            producto.setPrecioCosto(Integer.valueOf(this.vista.jtfPrecioCosto.getText()));
            producto.setPrecioMayorista(Integer.valueOf(this.vista.jtfPrecioMayorista.getText()));
            producto.setPrecioVenta(Integer.valueOf(this.vista.jtfPrecioVta.getText()));
            producto.setRubro((String) this.vista.jcbRubro.getSelectedItem());
            producto.setImpuesto((Integer.valueOf((String) this.vista.jcbImpuesto.getSelectedItem())));
            producto.setCantActual(Double.valueOf(this.vista.jtfCantActual.getText()));
            producto.setMarca((String) this.vista.jcbMarca.getSelectedItem());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Un parametro es incorrecto", "Atención", JOptionPane.ERROR_MESSAGE);
        }
    }

    public java.awt.Point centrarPantalla(javax.swing.JInternalFrame i) {
        /*con este codigo centramos el panel en el centro del contenedor
         la anchura del contenedor menos la anchura de nuestro componente divido a 2
         lo mismo con la altura.*/
        return new java.awt.Point((this.vista.getWidth() - i.getWidth()) / 2, (this.vista.getHeight() - i.getHeight() - 45) / 2);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbCancelar) {
            cerrar();
        }
        if (e.getSource() == this.vista.jbAceptar) {
            modificarProductos();
            actualizarProducto();
            cerrar();
        }
    }
}
