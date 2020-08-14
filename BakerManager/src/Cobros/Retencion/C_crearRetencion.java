/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import com.nitido.utils.toaster.Toaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearRetencion implements ActionListener, KeyListener, ChangeListener {

    private static final String VALIDAR_NRO_FACTURA_1 = "Ingrese solo números enteros en número de factura",
            VALIDAR_NRO_FACTURA_2 = "Ingrese solo números enteros y positivos en número de factura",
            VALIDAR_NRO_FACTURA_3 = "Ingrese un número de factura",
            VALIDAR_PORCENTAJE_RETENCION_1 = "Ingrese solo números en porcentaje de retención",
            VALIDAR_PORCENTAJE_RETENCION_2 = "Ingrese solo números positivos en porcentaje de retención",
            VALIDAR_PORCENTAJE_RETENCION_3 = "Ingrese un número en porcentaje de retención",
            VALIDAR_NRO_RETENCION_1 = "Ingrese solo números enteros en número de retención",
            VALIDAR_NRO_RETENCION_2 = "Ingrese solo números enteros y positivos en número de retención",
            VALIDAR_NRO_RETENCION_3 = "Ingrese un número de retención",
            VALIDAR_NRO_RETENCION_4 = "El número de retención ingresado se encuentra en uso.",
            WITHHOLDING_TAX_SUCCESS = "Retención creada";

    private M_crearRetencion modelo;
    private V_crearRetencion vista;
    private DecimalFormat decimalFormat;

    public C_crearRetencion(M_crearRetencion modelo, V_crearRetencion vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
        loadData();
    }

    private void inicializarVista() {
        decimalFormat = new DecimalFormat("###,###");
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaRetencion.setDate(calendar.getTime());
        this.vista.jsPorcentaje.setModel(modelo.getSpinnerModel());
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    public void cerrar() {
        this.vista.dispose();
    }

    private void loadData() {
        int nroFactura = 22427;
        this.vista.jtfNroFactura.setText(nroFactura + "");
    }

    private void agregarListeners() {
        this.modelo.getSpinnerModel().addChangeListener(this);
        this.vista.jtfNroFactura.addActionListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        this.vista.jtfNroRetencion.addActionListener(this);
        this.vista.jtfNroRetencion.addKeyListener(this);
        this.vista.jftPorcentajeRetencion.addActionListener(this);
        this.vista.jftPorcentajeRetencion.addKeyListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
    }

    private boolean validarNroFactura() {
        int nroFactura = -1;
        if (this.vista.jtfNroFactura.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroFactura = Integer.valueOf(this.vista.jtfNroFactura.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroFactura < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarNroRetencion() {
        int nroRetencion = -1;
        if (this.vista.jtfNroRetencion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RETENCION_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroRetencion = Integer.valueOf(this.vista.jtfNroRetencion.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RETENCION_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (modelo.existeNroRetencion(nroRetencion)) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RETENCION_4, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroRetencion < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RETENCION_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarMontoExistente() {
        return modelo.validarMontoExistente();
    }

    private boolean validarMontoRetencion() {
        if (validarPorcentaje()) {
            return false;
        }
        double porcentajeRetencion = (double) this.vista.jftPorcentajeRetencion.getValue();
        double montoRetencion = modelo.calcularMontoRetencion(porcentajeRetencion);
        System.err.println("montoRetencion: " + montoRetencion);
        return montoRetencion > 0;
    }

    private boolean validarPorcentaje() {
        double porcentajeRetencion = -1;
        if (this.vista.jftPorcentajeRetencion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_PORCENTAJE_RETENCION_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                porcentajeRetencion = (double) this.vista.jftPorcentajeRetencion.getValue();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_PORCENTAJE_RETENCION_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (porcentajeRetencion < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_PORCENTAJE_RETENCION_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarRetencionExistente() {
        if (!validarNroFactura()) {
            return false;
        }
        int nroFactura = Integer.valueOf(this.vista.jtfNroFactura.getText().trim());
        return modelo.existeRetencion(nroFactura);
    }

    private boolean validarFecha() {
        Date fechaVenta = modelo.getFacturaCabecera().getTiempo();
        Date fechaRetencion = this.vista.jdcFechaRetencion.getDate();
        return fechaRetencion.after(fechaVenta);
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    private void consultarNroFactura() {
        if (!validarNroFactura()) {
            return;
        }
        int nroFactura = Integer.valueOf(this.vista.jtfNroFactura.getText().trim());
        if (modelo.existeNroFactura(nroFactura)) {
            JOptionPane.showMessageDialog(vista, "No existe el numero de factura ingresado", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        modelo.consultarNroFactura(nroFactura);
        this.vista.jtfCliente.setText(modelo.getFacturaCabecera().getCliente().getEntidad());
        this.vista.jtfMontoConIVA.setText(decimalFormat.format(modelo.obtenerMontoConIva()));
        this.vista.jtfIVA.setText(decimalFormat.format(modelo.obtenerMontoConIva() - modelo.obtenerMontoSinIva()));
        this.vista.jtfMontoSinIVA.setText(decimalFormat.format(modelo.obtenerMontoSinIva()));
    }

    private void calcularMontoRetencion() {
        if (!validarMontoExistente()) {
            JOptionPane.showMessageDialog(vista, "Ingrese una factura válida antes de calcular la retención", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarPorcentaje()) {
            JOptionPane.showMessageDialog(vista, "Ingrese un porcentaje de retención válido", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double porcentajeRetencion = (double) this.vista.jftPorcentajeRetencion.getValue();
        double montoRetencion = modelo.calcularMontoRetencion(porcentajeRetencion);
        this.vista.jftMontoRetencion.setText(decimalFormat.format(montoRetencion));
        this.vista.jsPorcentaje.setValue(porcentajeRetencion);
    }

    private void handleSpinnerModel() {
        Double currentValue = Double.valueOf(modelo.getSpinnerModel().getValue() + "");
        this.vista.jftPorcentajeRetencion.setValue(currentValue);
        if (!validarMontoExistente()) {
            JOptionPane.showMessageDialog(vista, "Ingrese una factura válida antes de calcular la retención", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarPorcentaje()) {
            JOptionPane.showMessageDialog(vista, "Ingrese un porcentaje de retención válido", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Double porcentajeRetencion = currentValue;
        Double montoRetencion = modelo.calcularMontoRetencion(porcentajeRetencion);
        this.vista.jftMontoRetencion.setText(decimalFormat.format(montoRetencion));
        this.vista.jsPorcentaje.setValue(porcentajeRetencion);
    }

    private void guardar() {
        if (!validarNroFactura()) {
            return;
        }
        if (!validarNroRetencion()) {
            return;
        }
        if (!validarMontoExistente()) {
            return;
        }
        if (validarRetencionExistente()) {
            JOptionPane.showMessageDialog(vista, "La factura ingresada ya posee una retención existente", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (validarMontoRetencion()) {
            JOptionPane.showMessageDialog(vista, "Ingrese el monto de retención", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validarFecha()) {
            JOptionPane.showMessageDialog(vista, "La fecha de retención es menor a la fecha de venta", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int nroRetencion = Integer.valueOf(this.vista.jtfNroRetencion.getText().trim());
        double porcentajeRetencion = (double) this.vista.jftPorcentajeRetencion.getValue();
        int montoRetencion = Integer.valueOf(this.vista.jftMontoRetencion.getText().trim().replace(".", ""));
        Date fecha = this.vista.jdcFechaRetencion.getDate();
        modelo.getRetencion().setNroRetencion(nroRetencion);
        modelo.getRetencion().setPorcentaje(porcentajeRetencion);
        modelo.getRetencion().setMonto(montoRetencion);
        modelo.getRetencion().setTiempo(fecha);
        modelo.getRetencion().getVenta().setIdFacturaCabecera(modelo.getFacturaCabecera().getIdFacturaCabecera());
        modelo.guardarRetencion();
        mostrarMensaje(WITHHOLDING_TAX_SUCCESS);
        cerrar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(vista.jbAceptar)) {
            guardar();
        }
        if (source.equals(vista.jtfNroFactura)) {
            consultarNroFactura();
        }
        if (source.equals(vista.jftPorcentajeRetencion)) {
            calcularMontoRetencion();
        }
        if (source.equals(vista.jbCancelar)) {
            cerrar();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        handleSpinnerModel();
    }

}
