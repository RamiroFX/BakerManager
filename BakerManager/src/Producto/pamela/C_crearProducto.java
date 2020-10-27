/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.pamela;

import Cliente.SeleccionarCliente;
import Entities.E_Marca;
import Entities.E_clienteProducto;
import Entities.E_impuesto;
import Entities.M_cliente;
import Entities.M_producto;
import Entities.ProductoCategoria;
import Interface.RecibirClienteCallback;
import Interface.RecibirClienteProductoPreferenciaCallback;
import Interface.RecibirProductoCallback;
import Interface.RecibirProductoCategoriaCallback;
import Producto.Categoria.SeleccionarCategoria;
import Producto.SeleccionarProducto;
import com.nitido.utils.toaster.Toaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearProducto implements ActionListener, KeyListener, RecibirProductoCallback, RecibirClienteCallback,
        RecibirClienteProductoPreferenciaCallback, RecibirProductoCategoriaCallback {

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
        for (E_Marca marca : modelo.obtenerMarca()) {
            this.vista.jcbMarca.addItem(marca);
        }
    }

    private void agregarListeners() {
        this.vista.jbCategoria.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jbCopiar.addActionListener(this);
        this.vista.jbAgregarCliente.addActionListener(this);
        /*
        KEYLISTENER
         */
        this.vista.jbCategoria.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbCancelar.addKeyListener(this);
        this.vista.jbCopiar.addKeyListener(this);
        this.vista.jtfCodigo.addKeyListener(this);
        this.vista.jtfPrecioCosto.addKeyListener(this);
        this.vista.jtfPrecioMayorista.addKeyListener(this);
        this.vista.jtfPrecioVta.addKeyListener(this);
        this.vista.jtfProducto.addKeyListener(this);
    }

    private boolean validarDescripcion() {
        String descripcion = this.vista.jtfProducto.getText().trim();
        if (descripcion.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Ingrese un nombre para el producto.", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (descripcion.length() > 80) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el nombre del producto. Máximo 80 caracteres permitidos.", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (modelo.existeProducto(descripcion)) {
            javax.swing.JOptionPane.showMessageDialog(null, "El nombre del producto se encuentra en uso. Verifique el nombre del producto", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private boolean validarCodigo() {
        String codigo = this.vista.jtfCodigo.getText().trim();
        if (codigo.length() > 30) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique el código del producto. Máximo 30 caracteres permitidos.", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (codigo.isEmpty()) {
            codigo = null;
        } else if (modelo.existeCodigo(codigo)) {
            javax.swing.JOptionPane.showMessageDialog(null, "El codigo del producto se encuentra en uso. Verifique el codigo del producto", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private boolean validarPrecioVenta() {
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

    private boolean validarPrecioCosto() {
        int precio = -1;
        if (this.vista.jtfPrecioCosto.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio Costo.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPrecioCosto.requestFocusInWindow();
            return false;
        }
        try {
            precio = Integer.valueOf(String.valueOf(this.vista.jtfPrecioCosto.getText().trim()));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio Costo.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPrecioCosto.requestFocusInWindow();
            return false;
        }
        if (precio < 1) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio Costo.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPrecioCosto.requestFocusInWindow();
            return false;
        }
        return true;
    }

    private boolean validarPrecioMayorista() {
        int precio = -1;
        if (this.vista.jtfPrecioMayorista.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio Mayorista.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPrecioMayorista.requestFocusInWindow();
            return false;
        }
        try {
            precio = Integer.valueOf(String.valueOf(this.vista.jtfPrecioMayorista.getText().trim()));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio Mayorista.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPrecioMayorista.requestFocusInWindow();
            return false;
        }
        if (precio < 1) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio Mayorista.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPrecioMayorista.requestFocusInWindow();
            return false;
        }
        return true;
    }

    private boolean validarProductoCategoria() {
        if (modelo.getProductoCategoria().getId() < 1) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Seleccione una categoría para el producto",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private void creaProducto() {
        if (!validarDescripcion()) {
            return;
        }
        if (!validarCodigo()) {
            return;
        }
        if (!validarPrecioVenta()) {
            return;
        }
        if (!validarPrecioCosto()) {
            return;
        }
        if (!validarPrecioMayorista()) {
            return;
        }
        if (!validarProductoCategoria()) {
            return;
        }
        String descripcion = vista.jtfProducto.getText().trim();
        String codigo = vista.jtfCodigo.getText().trim();
        double precioMayorista = Double.valueOf(vista.jtfPrecioMayorista.getText().trim());
        double precioVenta = Double.valueOf(vista.jtfPrecioVta.getText().trim());
        double precioCosto = Double.valueOf(vista.jtfPrecioCosto.getText().trim());
        E_Marca marca = vista.jcbMarca.getItemAt(vista.jcbMarca.getSelectedIndex());
        E_impuesto impuesto = vista.jcbImpuesto.getItemAt(vista.jcbImpuesto.getSelectedIndex());
        M_producto producto = new M_producto();
        producto.setCantActual(0.0);
        producto.setDescripcion(descripcion);
        producto.setCodBarra(codigo);
        producto.setPrecioVenta(precioVenta);
        producto.setPrecioCosto(precioCosto);
        producto.setPrecioMayorista(precioMayorista);
        producto.setIdMarca(marca.getId());
        producto.setIdImpuesto(impuesto.getId());
        producto.setProductoCategoria(modelo.getProductoCategoria());
        modelo.crearProducto(producto);
        cerrar();
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

    private void seleccionarProductoCategoria() {
        SeleccionarCategoria spc = new SeleccionarCategoria(vista);
        spc.setCallback(this);
        spc.mostrarVista();
    }

    private void copiarDatosDeProducto(M_producto producto) {
        this.vista.jtfProducto.setText(producto.getDescripcion());
        this.vista.jtfCodigo.setText(String.valueOf(producto.getCodBarra()));
        this.vista.jtfPrecioCosto.setText(producto.getPrecioCosto() + "");
        this.vista.jtfPrecioMayorista.setText(producto.getPrecioMayorista() + "");
        this.vista.jtfPrecioVta.setText(producto.getPrecioVenta() + "");
        this.vista.jtfCategoria.setText(producto.getCategoria());
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
        } else if (e.getSource() == this.vista.jbCategoria) {
            seleccionarProductoCategoria();
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
    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        copiarDatosDeProducto(producto);
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        int precio = 1;
        if (validarPrecioVenta()) {
            precio = Integer.valueOf(this.vista.jtfPrecioVta.getText().trim());
        }
        SeleccionarPreferenciaProducto spc = new SeleccionarPreferenciaProducto(vista);
        spc.cargarDatos(precio, vista.jcbImpuesto.getItemAt(vista.jcbImpuesto.getSelectedIndex()), cliente);
        spc.setCallback(this);
        spc.mostrarVista();
    }

    @Override
    public void recibirClienteProducto(E_clienteProducto cp) {
        this.modelo.getTableModel().agregarDatos(cp);
    }

    @Override
    public void recibirProductoCategoria(ProductoCategoria pc) {
        this.modelo.setProductoCategoria(pc);
        this.vista.jtfCategoria.setText(pc.getDescripcion());
    }
}
