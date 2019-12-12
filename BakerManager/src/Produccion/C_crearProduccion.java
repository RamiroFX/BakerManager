/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Empleado.Seleccionar_funcionario;
import Entities.M_funcionario;
import Entities.M_producto;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProductoCallback;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
class C_crearProduccion extends MouseAdapter implements ActionListener, KeyListener, RecibirEmpleadoCallback, RecibirProductoCallback {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un responsable de producción",
            VALIDAR_ORDEN_TRABAJO_MSG_1 = "Ingrese una orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_2 = "Ingrese solo números enteros en orden de trabajo",
            VALIDAR_TITULO = "Atención";

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
        //TODO controlar que no existan productos cargados antes de cerrar la ventana
        this.vista.dispose();
    }

    private void inicializarVista() {
        this.vista.jtProduccionDetalle.setModel(modelo.getTm());
        E_productoClasificacion pc1 = new E_productoClasificacion(E_productoClasificacion.MATERIA_PRIMA, "Productos terminados");
        E_productoClasificacion pc2 = new E_productoClasificacion(E_productoClasificacion.PRODUCTO_TERMINADO, "Rollos");
        this.vista.jcbCondVenta.addItem(pc1);
        this.vista.jcbCondVenta.addItem(pc2);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
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

    private void eliminarDetalle() {
        int fila = this.vista.jtProduccionDetalle.getSelectedRow();
        if (fila > -1) {
            modelo.removerDetalle(fila);
        }
    }

    public void modificarDetalle() {
        int fila = this.vista.jtProduccionDetalle.getSelectedRow();
        if (fila > -1) {
            M_producto producto = modelo.getTm().getList().get(fila).getProducto();
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this.vista, producto, this, fila);
            scp.setVisible(true);
        }
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

    private boolean validarResponsable() {
        if (this.vista.jtfFuncionario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_RESPONSABLE_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarOrdenTrabajo() {
        if (this.vista.jtfNroOrdenTrabajo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_1, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        //TODO validar numeros enteros y en la bd
        return true;
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
            modificarDetalle();
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
                Seleccionar_funcionario sf = new Seleccionar_funcionario(this.vista);
                sf.setCallback(this);
                sf.mostrarVista();
                break;
            }
            case KeyEvent.VK_F4: {
                SeleccionarProducto sp = new SeleccionarProducto(vista, this);
                sp.mostrarVista();
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

    @Override
    public void modificarProducto(int posicion, double cantidad, int precio, double descuento, M_producto producto, String observacion) {
        modelo.modificarDetalle(posicion, cantidad);
    }
}
