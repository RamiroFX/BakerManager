/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import Entities.M_producto;
import Entities.SeleccionAjusteStockDetalle;
import Interface.RecibirAjusteStockDetalleCB;
import ModeloTabla.SeleccionarProductoTableModel;
import Producto.AjusteStock.SeleccionarCantidadAjuste.SeleccionarProductoAjusteStock;
import bauplast.SeleccionarProductoPorClasif;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearAjuste extends MouseAdapter implements ActionListener, KeyListener,
        RecibirAjusteStockDetalleCB {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un responsable de producción",
            VALIDAR_FECHA_PRODUCCION_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_CANT_PRODUCTOS_MSG = "Seleccione por lo menos un producto.",
            VALIDAR_TITULO = "Atención";

    public M_crearAjuste modelo;
    public V_crearAjuste vista;

    public C_crearAjuste(M_crearAjuste modelo, V_crearAjuste vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        vista.setVisible(true);
    }

    public void cerrar() {
        vista.dispose();
        System.runFinalization();
    }

    private void inicializarVista() {
        this.vista.jtfFuncionario.setText(modelo.obtenerFuncionario());
        this.vista.jdcFecha.setDate(modelo.getCabecera().getTiempoInicio());
        this.vista.jtDetalle.setModel(modelo.getTmDetalle());
        this.vista.jtfFuncionario.setEditable(false);
    }

    private void agregarListeners() {
        this.vista.jbSeleccionarProducto.addActionListener(this);
        this.vista.jbModificarProducto.addActionListener(this);
        this.vista.jbEliminarProducto.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
    }

    private void invocarSeleccionarDetalle() {
        SeleccionarProductoPorClasif sp = new SeleccionarProductoPorClasif(vista, SeleccionarProductoTableModel.DETALLE);
        sp.setAjusteStockCallback(this);
        sp.mostrarVista();
    }

    private void invocarModificarDetalle() {
        int fila = this.vista.jtDetalle.getSelectedRow();
        if (fila > -1) {
            SeleccionAjusteStockDetalle detalle = modelo.getTmDetalle().getList().get(fila);
            SeleccionarProductoAjusteStock scp = new SeleccionarProductoAjusteStock(vista);
            scp.setProductoCallback(this);
            scp.cargarDatos(detalle, fila);
            scp.setVisible(true);
        }
    }

    private void eliminarDetalle() {
        int index = vista.jtDetalle.getSelectedRow();
        if (index > -1) {
            int opcion = JOptionPane.showConfirmDialog(vista, "Confirmar", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                modelo.removerProducto(index);
            }
        }
    }

    private boolean validarFecha() {
        Date entrega = null;
        try {
            entrega = vista.jdcFecha.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_PRODUCCION_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (entrega.after(Calendar.getInstance().getTime())) {
            JOptionPane.showMessageDialog(vista, "La fecha del desperdicio no puede ser mayor al tiempo actual", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarObservacion() {
        String obs = vista.jtfObservacion.getText().trim();
        if (obs.length() > 200) {
            JOptionPane.showMessageDialog(vista, "Solo se permiten 200 caracteres en observación. Cant. actual: " + obs.length(), VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarFilas() {
        if (modelo.getTmDetalle().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese un desperdicio", "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void guardar() {
        if (!validarFilas()) {
            return;
        }
        if (!validarFecha()) {
            return;
        }
        if (!validarObservacion()) {
            return;
        }
        String observacion = vista.jtfObservacion.getText().trim();
        modelo.getCabecera().setTiempoInicio(vista.jdcFecha.getDate());
        modelo.guardar();
        cerrar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(vista.jbSeleccionarProducto)) {
            invocarSeleccionarDetalle();
        }
        if (source.equals(vista.jbModificarProducto)) {
            invocarModificarDetalle();
        }
        if (source.equals(vista.jbEliminarProducto)) {
            eliminarDetalle();
        }
        if (source.equals(vista.jbAceptar)) {
            guardar();
        }
        if (source.equals(vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void recibirAjusteStock(SeleccionAjusteStockDetalle ajusteStockDetalle) {
        modelo.recibirAjusteStock(ajusteStockDetalle);
        Utilities.c_packColumn.packColumns(vista.jtDetalle, 1);
    }

    @Override
    public void modificarAjusteStock(int index, SeleccionAjusteStockDetalle ajusteStockDetalle) {
        modelo.modificarAjusteStock(index, ajusteStockDetalle);
        Utilities.c_packColumn.packColumns(vista.jtDetalle, 1);
    }
}
