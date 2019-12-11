/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Cliente.Seleccionar_cliente;
import Empleado.Seleccionar_funcionario;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_pedidoDetalle;
import Entities.M_producto;
import Impresora.Impresora;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProductoCallback;
import MenuPrincipal.DatosUsuario;
import Parametros.PedidoEstado;
import Parametros.TipoOperacion;
import Pedido.C_gestionPedido;
import Pedido.M_crearPedido;
import Pedido.V_crearPedido;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import com.nitido.utils.toaster.Toaster;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
class C_crearProduccion extends MouseAdapter implements ActionListener, KeyListener, RecibirEmpleadoCallback, RecibirProductoCallback {

    public M_crearProduccion modelo;
    public V_crearProduccion vista;

    public C_crearProduccion(M_crearProduccion modelo, V_crearProduccion vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        vista.setVisible(true);
    }

    private void cerrar() {
        /*if (!this.modelo.getDetalles().isEmpty()) {
            int opcion = JOptionPane.showConfirmDialog(vista, "Hay producto cargados, ¿Desea cancelar?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                this.vista.dispose();
            }
        }*/
        this.vista.dispose();
    }

    private void inicializarVista() {
        this.vista.jtProduccionDetalle.setModel(modelo.getTm());
        /*Vector condCompra = modelo.obtenerTipoOperacion();
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondVenta.addItem(condCompra.get(i));
        }*/
 /*Vector tipoVenta = modelo.obtenerTipoVenta();
        for (int i = 0; i < tipoVenta.size(); i++) {
            this.vista.jcbTipoVenta.addItem(tipoVenta.get(i));
        }*/
        this.vista.jtfNroOrdenTrabajo.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        java.awt.Font fuente = new java.awt.Font("Times New Roman", 0, 18);
        javax.swing.text.DefaultFormatterFactory dff = new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance()));

        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaEntrega.setDate(calendar.getTime());
        establecerCondicionVenta();
    }

    private void agregarListeners() {
        this.vista.jtProduccionDetalle.addMouseListener(this);
        this.vista.jcbCondVenta.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSeleccionarProducto.addActionListener(this);
        this.vista.jbFuncionario.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbSeleccionarProducto.addKeyListener(this);
        this.vista.jbFuncionario.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jcbCondVenta.addKeyListener(this);
    }

    private void establecerCondicionVenta() {
    }

    public void recibirDetalle(M_pedidoDetalle detalle) {
    }

    private void eliminarDetalle() {
    }

    public void modificarDetalle(Double cantidad, Integer precio, Double descuento, String observacion, int row) {
    }

    private void sumarTotal() {
    }

    private void guardarPedido() {
    }

    private boolean establecerHoraEntrega() {
        return false;
    }

    private void imprimir() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbAceptar)) {
            guardarPedido();
        } else if (source.equals(this.vista.jcbCondVenta)) {
            establecerCondicionVenta();
        } else if (source.equals(this.vista.jbSeleccionarProducto)) {
            SeleccionarProducto sp = new SeleccionarProducto(vista, this);
            sp.mostrarVista();
        } else if (source.equals(this.vista.jbFuncionario)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (source.equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        } else if (source.equals(this.vista.jbModificarDetalle)) {
            /*SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, this.vista.jtPedidoDetalle.getSelectedRow());
            scp.setVisible(true);*/
        } else if (source.equals(this.vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtProduccionDetalle)) {
            this.vista.jbModificarDetalle.setEnabled(true);
            this.vista.jbEliminarDetalle.setEnabled(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1: {
                guardarPedido();
                break;
            }
            case KeyEvent.VK_F2: {
                imprimir();
                break;
            }
            case KeyEvent.VK_F3: {
                /*Seleccionar_cliente sc = new Seleccionar_cliente(this.gestionPedido.c_inicio.vista);
                sc.setCallback(this);
                sc.mostrarVista();*/
                break;
            }
            case KeyEvent.VK_F4: {
                /*SeleccionarProducto sp = new SeleccionarProducto(this);
                sp.mostrarVista();*/
                break;
            }
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
        modelo.agregarDetalle(cantidad, producto);
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.vista.jtfFuncionario.setText(funcionario.getNombre());
    }
}
