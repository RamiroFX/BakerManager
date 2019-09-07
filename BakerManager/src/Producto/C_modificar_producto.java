/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

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
    private M_modificar_producto modelo;
    V_modificar_producto vista;

    public C_modificar_producto(M_modificar_producto modelo, V_modificar_producto vista, C_gestion_producto gestionProducto) {
        this.controlador = gestionProducto;
        this.modelo = modelo;
        this.vista = vista;
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
        Vector impuesto = modelo.obtenerImpuesto();
        for (int i = 0; i < impuesto.size(); i++) {
            this.vista.jcbImpuesto.addItem(impuesto.get(i));
        }
        Vector rubro = modelo.obtenerRubro();
        for (int i = 0; i < rubro.size(); i++) {
            this.vista.jcbRubro.addItem(rubro.get(i));
        }
        Vector marca = modelo.obtenerMarca();
        for (int i = 0; i < marca.size(); i++) {
            this.vista.jcbMarca.addItem(marca.get(i));
        }
        Vector estado = modelo.obtenerEstado();
        for (int i = 0; i < estado.size(); i++) {
            this.vista.jcbEstado.addItem(estado.get(i));
        }
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private java.awt.Dimension establecerTamañoPanel() {
        return new java.awt.Dimension((int) (this.controlador.vista.getWidth() * 0.8), (int) (this.controlador.vista.getHeight() * 0.8));
    }

    private void completarCampos() {
        this.vista.jlTituloProducto.setText(this.modelo.producto.getDescripcion());
        this.vista.jtfProducto.setText(this.modelo.producto.getDescripcion());
        this.vista.jtfCodigo.setText(String.valueOf(this.modelo.producto.getCodBarra()));
        this.vista.jtfPrecioCosto.setText(String.valueOf(this.modelo.producto.getPrecioCosto()));
        this.vista.jtfPrecioMayorista.setText(String.valueOf(this.modelo.producto.getPrecioMayorista()));
        this.vista.jtfPrecioVta.setText(String.valueOf(this.modelo.producto.getPrecioVenta()));
        this.vista.jcbRubro.setSelectedItem(this.modelo.producto.getCategoria());
        this.vista.jcbImpuesto.setSelectedItem(String.valueOf(this.modelo.producto.getImpuesto()));
        this.vista.jcbMarca.setSelectedItem(this.modelo.producto.getMarca());
        this.vista.jtfCantActual.setText(String.valueOf(this.modelo.producto.getCantActual()));
        this.vista.jcbEstado.setSelectedItem(this.modelo.producto.getEstado());
        this.vista.jtfObservacion.setText(this.modelo.producto.getObservacion());
    }

    private void modificarProductos() {
        String codigo = this.vista.jtfCodigo.getText().trim();
        if (codigo.length() > 30) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el código del producto. Máximo 30 caracteres permitidos.", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        if (codigo.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el código del producto. Campo vacío.", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        int precioCosto = 0;
        try {
            precioCosto = Integer.valueOf(this.vista.jtfPrecioCosto.getText());
            if (precioCosto < 0 || precioCosto > 999999999) {
                JOptionPane.showMessageDialog(vista, "Precio de costo. Máximo 9 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                this.vista.jtfPrecioCosto.setText("");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un precio de costo válido. Solo números enteros.", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int precioMayorista = 0;
        try {
            precioMayorista = Integer.valueOf(this.vista.jtfPrecioMayorista.getText());
            if (precioMayorista < 0 || precioMayorista > 999999999) {
                JOptionPane.showMessageDialog(vista, "Precio mayorista. Máximo 9 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                this.vista.jtfPrecioMayorista.setText("");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un precio mayorista válido. Solo números enteros.", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int precioVenta = 0;
        try {
            precioVenta = Integer.valueOf(this.vista.jtfPrecioVta.getText());
            if (precioVenta < 0 || precioVenta > 999999999) {
                JOptionPane.showMessageDialog(vista, "Precio de venta. Máximo 9 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                this.vista.jtfPrecioVta.setText("");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un precio de venta válido. Solo números enteros.", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Double cantActual = null;
        try {
            cantActual = Double.valueOf(this.vista.jtfCantActual.getText());
            if (cantActual > 999999) {
                JOptionPane.showMessageDialog(vista, "Cantidad actual. Máximo 6 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                this.vista.jtfCantActual.setText("");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese una cantidad válida.", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String observacion = this.vista.jtfObservacion.getText().trim();
        if (observacion.length() > 250) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique la observación del producto. Máximo 250 caracteres permitidos.", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        if (observacion.isEmpty()) {
            observacion = null;
        }
        M_producto producto = new M_producto();
        producto.setCantActual(cantActual);
        producto.setCodBarra(codigo);
        producto.setPrecioVenta(precioVenta);
        producto.setPrecioCosto(precioCosto);
        producto.setPrecioMayorista(precioMayorista);
        producto.setCategoria((String) this.vista.jcbRubro.getSelectedItem());
        producto.setImpuesto((Integer.valueOf((String) this.vista.jcbImpuesto.getSelectedItem())));
        producto.setMarca((String) this.vista.jcbMarca.getSelectedItem());
        producto.setEstado((String) this.vista.jcbEstado.getSelectedItem());
        producto.setObservacion(observacion);
        if (modelo.actualizarProducto(producto)) {
            cerrar();
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
        }
    }
}
