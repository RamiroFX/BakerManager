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
        if (!controlarNombreProducto()) {
            return;
        }
        if (!controlarCodigo()) {
            return;
        }
        if (!controlarPrecioCosto()) {
            return;
        }
        if (!controlarPrecioMayorista()) {
            return;
        }
        if (!controlarPrecioVenta()) {
            return;
        }
        if (!controlarCantActual()) {
            return;
        }
        if (!controlarObservacion()) {
            return;
        }
        String observacion = this.vista.jtfObservacion.getText().trim();
        if (observacion.isEmpty()) {
            observacion = null;
        }
        String descripcion = this.vista.jtfProducto.getText().trim();
        String codigo = this.vista.jtfCodigo.getText().trim();
        double precioCosto = Double.valueOf(this.vista.jtfPrecioCosto.getText());
        double precioMayorista = Double.valueOf(this.vista.jtfPrecioMayorista.getText());
        double precioVenta = Double.valueOf(this.vista.jtfPrecioVta.getText());
        double cantActual = Double.valueOf(this.vista.jtfCantActual.getText());
        M_producto producto = new M_producto();
        producto.setDescripcion(descripcion);
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

    private boolean controlarNombreProducto() {
        String nombreActual = this.modelo.producto.getDescripcion();
        String nombreNuevo = this.vista.jtfProducto.getText().trim();
        if (nombreActual.equals(nombreNuevo)) {
            System.out.println("Los nombre son iguales");
            return true;
        }
        System.out.println("Los nombre NO son iguales");
        if (nombreNuevo.isEmpty() || nombreNuevo.length() > 50) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el nombre del producto. Máximo 50 caracteres permitidos.", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        System.out.println("Longitud valida");
        if (modelo.existeNombreProducto(nombreNuevo)) {
            javax.swing.JOptionPane.showMessageDialog(null, "El nombre del producto se encuentra en uso. Verifique el nombre del producto", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        System.out.println("Nombre nuevo");
        //PENDIENTE
        boolean b = modelo.productoEnUso(this.modelo.producto.getId());
        if (b) {
            Object[] options = {"Si",
                "No"};
            int n = JOptionPane.showOptionDialog(null,
                    "El nombre del producto ya se encuentra registrado en una venta. ¿Desea continuar?",
                    "Atención",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, //do not use a custom Icon
                    options, //the titles of buttons
                    options[0]); //default button title
            switch (n) {
                case 0: {
                    //Completo
                    System.out.println("PRIMERA OPCION");
                    //return true;
                    break;
                }
                case 1: {
                    return false;
                }
            }
        }
        System.out.println("TODO BIEN");
        return true;
    }

    private boolean controlarCodigo() {
        String codigo = this.vista.jtfCodigo.getText().trim();
        if (codigo.length() > 30) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el código del producto. Máximo 30 caracteres permitidos.", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (codigo.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el código del producto. Campo vacío.", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private boolean controlarPrecioCosto() {
        double precioCosto = 0;
        try {
            precioCosto = Double.valueOf(this.vista.jtfPrecioCosto.getText());
            if (precioCosto < 0 || precioCosto > 999999999) {
                JOptionPane.showMessageDialog(vista, "Precio de costo. Máximo 9 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                this.vista.jtfPrecioCosto.setText("");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un precio de costo válido. Solo números enteros.", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean controlarPrecioMayorista() {
        double precioMayorista = 0;
        try {
            precioMayorista = Double.valueOf(this.vista.jtfPrecioMayorista.getText());
            if (precioMayorista < 0 || precioMayorista > 999999999) {
                JOptionPane.showMessageDialog(vista, "Precio mayorista. Máximo 9 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                this.vista.jtfPrecioMayorista.setText("");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un precio mayorista válido. Solo números enteros.", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean controlarPrecioVenta() {
        double precioVenta = 0;
        try {
            precioVenta = Double.valueOf(this.vista.jtfPrecioVta.getText());
            if (precioVenta < 0 || precioVenta > 999999999) {
                JOptionPane.showMessageDialog(vista, "Precio de venta. Máximo 9 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                this.vista.jtfPrecioVta.setText("");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un precio de venta válido. Solo números enteros.", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean controlarCantActual() {
        double cantActual = 0.0;
        try {
            cantActual = Double.valueOf(this.vista.jtfCantActual.getText());
            if (cantActual > 999999) {
                JOptionPane.showMessageDialog(vista, "Cantidad actual. Máximo 6 dígitos permitido", "Atención", JOptionPane.ERROR_MESSAGE);
                this.vista.jtfCantActual.setText("");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese una cantidad válida.", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean controlarObservacion() {
        String observacion = this.vista.jtfObservacion.getText().trim();
        if (observacion.length() > 250) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique la observación del producto. Máximo 250 caracteres permitidos.", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        return true;
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
