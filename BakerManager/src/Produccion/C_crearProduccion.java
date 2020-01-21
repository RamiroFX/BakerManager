/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Empleado.Seleccionar_funcionario;
import Entities.E_produccionTipo;
import Entities.M_funcionario;
import Entities.M_producto;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProductoCallback;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
class C_crearProduccion extends MouseAdapter implements ActionListener, KeyListener, RecibirEmpleadoCallback, RecibirProductoCallback {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un responsable de producción",
            VALIDAR_ORDEN_TRABAJO_MSG_1 = "Ingrese una orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_2 = "Ingrese solo números enteros en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_3 = "Ingrese solo números enteros y positivos en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_4 = "El número de orden de trabajo ingresado ya se encuentra en uso.",
            VALIDAR_FECHA_PRODUCCION_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_CANT_PRODUCTOS_MSG = "Seleccione por lo menos un producto.",
            CONFIRMAR_SALIR_MSG = "¿Cancelar producción?",
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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (modelo.getTm().getList().isEmpty()) {
                    vista.dispose();
                } else {
                    int opcion = JOptionPane.showConfirmDialog(vista, CONFIRMAR_SALIR_MSG, VALIDAR_TITULO, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        vista.dispose();
                    }
                }
            }
        });
    }

    private void inicializarVista() {
        this.vista.jtProduccionDetalle.setModel(modelo.getTm());
        //E_productoClasificacion pc1 = new E_productoClasificacion(E_productoClasificacion.MATERIA_PRIMA, "Productos terminados");
        //E_productoClasificacion pc2 = new E_productoClasificacion(E_productoClasificacion.PRODUCTO_TERMINADO, "Rollos");
        ArrayList<E_produccionTipo> tipoProduccion = modelo.obtenerProduccionTipo();
        for (int i = 0; i < tipoProduccion.size(); i++) {
            this.vista.jcbTipoProduccion.addItem(tipoProduccion.get(i));
        }
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaEntrega.setDate(calendar.getTime());
        establecerTipoProduccion();
    }

    private void agregarListeners() {
        this.vista.jtProduccionDetalle.addMouseListener(this);
        this.vista.jcbTipoProduccion.addActionListener(this);
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
        this.vista.jcbTipoProduccion.addKeyListener(this);
    }

    private void establecerTipoProduccion() {
        int selectedIndex = this.vista.jcbTipoProduccion.getSelectedIndex();
        E_produccionTipo tipo = this.vista.jcbTipoProduccion.getItemAt(selectedIndex);
        modelo.getProduccionCabecera().setTipo(tipo);
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

    private void guardarProduccion() {
        if (!validarOrdenTrabajo()) {
            return;
        }
        if (!validarResponsable()) {
            return;
        }
        if (!validarFechaProduccion()) {
            return;
        }
        if (!validarCantidadProductos()) {
            return;
        }
        Date fechaProduccion = vista.jdcFechaEntrega.getDate();
        int ordenTrabajo = Integer.valueOf(this.vista.jtfNroOrdenTrabajo.getText().trim());
        modelo.getProduccionCabecera().setFechaProduccion(fechaProduccion);
        modelo.getProduccionCabecera().setNroOrdenTrabajo(ordenTrabajo);
        modelo.guardarProduccion();
        limpiarCampos();
        cerrar();
    }

    private boolean validarFechaProduccion() {
        //Date now = Calendar.getInstance().getTime();
        Date entrega = null;
        try {
            entrega = vista.jdcFechaEntrega.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_PRODUCCION_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarResponsable() {
        if (this.vista.jtfFuncionario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_RESPONSABLE_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarOrdenTrabajo() {
        int ordenTrabajo = -1;
        if (this.vista.jtfNroOrdenTrabajo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_1, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            ordenTrabajo = Integer.valueOf(this.vista.jtfNroOrdenTrabajo.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_2, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (ordenTrabajo < 0) {
            JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_3, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (modelo.existeOrdenTrabajo(ordenTrabajo)) {
            JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_4, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCantidadProductos() {
        if (modelo.getTm().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_CANT_PRODUCTOS_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        this.modelo.limpiarCampos();
        this.vista.jtfFuncionario.setText("");
        this.vista.jtfNroOrdenTrabajo.setText("");
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaEntrega.setDate(calendar.getTime());
    }

    private void imprimir() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbAceptar)) {
            guardarProduccion();
        } else if (source.equals(this.vista.jcbTipoProduccion)) {
            establecerTipoProduccion();
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
                guardarProduccion();
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
        this.modelo.getProduccionCabecera().setFuncionarioProduccion(funcionario);
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, int precio, double descuento, M_producto producto, String observacion) {
        modelo.modificarDetalle(posicion, cantidad);
    }
}
