/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import Entities.SeleccionAjusteStockDetalle;
import Interface.RecibirAjusteStockDetalleCB;
import ModeloTabla.SeleccionarProductoTableModel;
import Producto.AjusteStock.SeleccionarCantidadAjuste.SeleccionarProductoAjusteStock;
import bauplast.SeleccionarProductoPorClasif;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
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
        this.vista.jdcFechaInicio.setDate(modelo.getCabecera().getTiempoInicio());
        this.vista.jdcFechaFin.setDate(modelo.getCabecera().getTiempoFin());
        this.vista.jdcFechaFin.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // If the 'date' property was changed...
                if ("date".equals(evt.getPropertyName())) {
                    JDateChooser aDateChooser = (JDateChooser) evt.getSource();
                    boolean isDateSelectedByUser = false;
                    // Get the otherwise unaccessible JDateChooser's 'dateSelected' field.
                    try {
                        // Get the desired field using reflection
                        Field dateSelectedField = JDateChooser.class.getDeclaredField("dateSelected");
                        // This line makes the value accesible (can be read and/or modified)
                        dateSelectedField.setAccessible(true);
                        isDateSelectedByUser = dateSelectedField.getBoolean(aDateChooser);
                    } catch (Exception ignoreOrNot) {
                    }

                    // Do some important stuff depending on wether value was changed by user
                    if (isDateSelectedByUser) {
                        establecerFechaFin();
                    }

                    // Reset the value to false
                    try {
                        Field dateSelectedField = JDateChooser.class.getDeclaredField("dateSelected");
                        dateSelectedField.setAccessible(true);
                        isDateSelectedByUser = false;
                        dateSelectedField.setBoolean(aDateChooser, false);
                    } catch (Exception ignoreOrNot) {
                    }
                }
            }
        });
        this.vista.jtDetalle.setModel(modelo.getTmDetalle());
        this.vista.jtfFuncionario.setEditable(false);
        Utilities.c_packColumn.packColumns(vista.jtDetalle, 1);
        if(!this.modelo.getEsTemporal()){
            this.vista.jbFuncionario.setEnabled(false);
            this.vista.jdcFechaFin.setEnabled(false);
            this.vista.jdcFechaInicio.setEnabled(false);
            this.vista.jbSeleccionarProducto.setEnabled(false);
            this.vista.jbModificarProducto.setEnabled(false);
            this.vista.jbEliminarProducto.setEnabled(false);
            this.vista.jbAceptar.setEnabled(false);
        }
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
        Date dateInicio = null, dateFin = null;
        try {
            dateInicio = vista.jdcFechaInicio.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_PRODUCCION_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            dateFin = vista.jdcFechaFin.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_PRODUCCION_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dateInicio.after(dateFin)) {
            JOptionPane.showMessageDialog(vista, "La fecha de inicio no puede ser mayor a la fecha de finalización", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarObservacion() {
        String obs = vista.jtfObservacion.getText().trim();
        if (obs.length() > 150) {
            JOptionPane.showMessageDialog(vista, "Solo se permiten 150 caracteres en observación. Cant. actual: " + obs.length(), VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
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
        int opcion = JOptionPane.showConfirmDialog(vista, "Confirmar", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        String observacion = vista.jtfObservacion.getText().trim();
        modelo.getCabecera().setTiempoInicio(vista.jdcFechaInicio.getDate());
        modelo.getCabecera().setTiempoFin(vista.jdcFechaFin.getDate());
        modelo.getCabecera().setObservacion(observacion);
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

    private void establecerFechaInicio() {
        Date dateInicio = null;
        try {
            dateInicio = vista.jdcFechaInicio.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_PRODUCCION_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modelo.establecerFechaInicio(dateInicio);
    }

    private void establecerFechaFin() {
        System.out.println("Producto.AjusteStock.C_crearAjuste.establecerFechaFin()");
        Date dateFin = null;
        try {
            dateFin = vista.jdcFechaFin.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_PRODUCCION_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modelo.establecerFechaFin(dateFin);
    }
}
