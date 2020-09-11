/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.pamela;

import Cliente.SeleccionarCliente;
import Entities.E_clienteproducto;
import Entities.E_impuesto;
import Entities.M_cliente;
import Entities.M_producto;
import Interface.RecibirClienteCallback;
import Interface.RecibirClienteProductoPreferenciaCallback;
import Interface.RecibirProductoCallback;
import Producto.SeleccionarProducto;
import com.nitido.utils.toaster.Toaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearProducto implements ActionListener, KeyListener, RecibirProductoCallback, RecibirClienteCallback, RecibirClienteProductoPreferenciaCallback {

    private M_crearProducto modelo;
    public V_crearProducto vista;

    public C_crearProducto(M_crearProducto modelo, V_crearProducto vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setSize(800, 300);
        this.vista.setLocationRelativeTo(this.vista.getParent());
        this.vista.setVisible(true);
    }

    private void inicializarVista() {
        this.vista.jtCliente.setModel(modelo.getTableModel());
        this.vista.jtfCodigo.setText(this.modelo.obtenerUltimoIdProducto() + 1 + "");
        for (E_impuesto impuesto : modelo.obtenerImpuesto()) {
            this.vista.jcbImpuesto.addItem(impuesto);
        }
        this.vista.jcbImpuesto.setSelectedIndex(2);
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
        this.vista.jbCopiar.addActionListener(this);
        this.vista.jbAgregarCliente.addActionListener(this);
        /*
        KEYLISTENER
         */
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbCancelar.addKeyListener(this);
        this.vista.jbCopiar.addKeyListener(this);
        this.vista.jtfCodigo.addKeyListener(this);
        this.vista.jtfPrecioCosto.addKeyListener(this);
        this.vista.jtfPrecioMayorista.addKeyListener(this);
        this.vista.jtfPrecioVta.addKeyListener(this);
        this.vista.jtfProducto.addKeyListener(this);
    }

    private boolean validarPrecio() {
        int precio = -1;
        if (this.vista.jtfPrecioVta.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPrecioVta.requestFocusInWindow();
            return false;
        }
        try {
            precio = Integer.valueOf(String.valueOf(this.vista.jtfPrecioVta.getText().trim()));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPrecioVta.requestFocusInWindow();
            return false;
        }
        if (precio < 1) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPrecioVta.requestFocusInWindow();
            return false;
        }
        return true;
    }

    private void creaProducto() {
        try {
            String descripcion = this.vista.jtfProducto.getText().trim();
            if (descripcion.isEmpty() || descripcion.length() > 80) {
                javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el nombre del producto. Máximo 50 caracteres permitidos.", "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                return;
            }
            String codigo = this.vista.jtfCodigo.getText().trim();
            if (codigo.length() > 30) {
                javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el código del producto. Máximo 30 caracteres permitidos.", "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                return;
            }
            if (codigo.isEmpty()) {
                codigo = null;
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
            M_producto producto = new M_producto();
            producto.setCantActual(0.0);
            producto.setDescripcion(descripcion);
            producto.setCodBarra(codigo);
            producto.setPrecioVenta(precioVenta);
            producto.setPrecioCosto(precioCosto);
            producto.setPrecioMayorista(precioMayorista);
            producto.setMarca((String) this.vista.jcbMarca.getSelectedItem());
            producto.setImpuesto((Integer.valueOf((String) this.vista.jcbImpuesto.getSelectedItem())));
            producto.setCategoria((String) this.vista.jcbCategoria.getSelectedItem());
            producto.setEstado("Activo");
//            if (modelo.crearProducto(producto, this.vista.jckBProveedor.isSelected())) {
//                mostrarMensaje("El Producto se registró con éxito");
//                cerrar();
//            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Uno de los datos ingresados es erróneo.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
        }
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    private void seleccionarProducto() {
        SeleccionarProducto sp = new SeleccionarProducto(vista, this);
        sp.activarModoCreacion();
        sp.mostrarVista();
    }

    private void seleccionarCliente() {
        SeleccionarCliente sc = new SeleccionarCliente(vista);
        sc.setCallback(this);
        sc.mostrarVista();
    }

    private void copiarDatosDeProducto(M_producto producto) {
        this.vista.jtfProducto.setText(producto.getDescripcion());
        this.vista.jtfCodigo.setText(String.valueOf(producto.getCodBarra()));
        this.vista.jtfPrecioCosto.setText(producto.getPrecioCosto() + "");
        this.vista.jtfPrecioMayorista.setText(producto.getPrecioMayorista() + "");
        this.vista.jtfPrecioVta.setText(producto.getPrecioVenta() + "");
        this.vista.jcbCategoria.setSelectedItem(producto.getCategoria());
        this.vista.jcbImpuesto.setSelectedItem(producto.getImpuesto() + "");
        this.vista.jcbMarca.setSelectedItem(producto.getMarca());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            creaProducto();
        } else if (e.getSource() == this.vista.jbCancelar) {
            cerrar();
        } else if (e.getSource() == this.vista.jbCopiar) {
            seleccionarProducto();
        } else if (e.getSource() == this.vista.jbAgregarCliente) {
            seleccionarCliente();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void recibirProducto(double cantidad, int precio, double descuento, M_producto producto, String observacion) {
        copiarDatosDeProducto(producto);
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, int precio, double descuento, M_producto producto, String observacion) {
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        int precio = 1;
        if (validarPrecio()) {
            precio = Integer.valueOf(this.vista.jtfPrecioVta.getText().trim());
        }
        SeleccionarPreferenciaProducto spc = new SeleccionarPreferenciaProducto(vista);
        spc.cargarDatos(precio, vista.jcbImpuesto.getItemAt(vista.jcbImpuesto.getSelectedIndex()), cliente);
        spc.setCallback(this);
        spc.mostrarVista();
    }

    @Override
    public void recibirClienteProducto(E_clienteproducto cp) {
        this.modelo.getTableModel().agregarDatos(cp);
    }
}
