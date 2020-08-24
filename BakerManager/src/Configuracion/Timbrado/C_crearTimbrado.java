/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import com.nitido.utils.toaster.Toaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearTimbrado implements ActionListener, KeyListener {

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

    private M_crearTimbrado modelo;
    private V_CrearTimbrado vista;
    private DecimalFormat decimalFormat;

    public C_crearTimbrado(M_crearTimbrado modelo, V_CrearTimbrado vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
        loadData();
    }

    private void inicializarVista() {
        decimalFormat = new DecimalFormat("###,###");
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaVencimiento.setDate(calendar.getTime());
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    public void cerrar() {
        this.vista.dispose();
    }

    private void loadData() {
        int nroFactura = 22427;
        this.vista.jtfNroTimbrado.setText(nroFactura + "");
    }

    private void agregarListeners() {
        this.vista.jtfNroTimbrado.addActionListener(this);
        this.vista.jtfNroSucursal.addActionListener(this);
        this.vista.jtfPuntoVenta.addActionListener(this);
        this.vista.jtfBoletaInicial.addActionListener(this);
        this.vista.jtfBoletaFinal.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jtfNroTimbrado.addKeyListener(this);
        this.vista.jtfNroSucursal.addKeyListener(this);
        this.vista.jtfPuntoVenta.addKeyListener(this);
        this.vista.jtfBoletaInicial.addKeyListener(this);
        this.vista.jtfBoletaFinal.addKeyListener(this);
        //this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbCancelar.addKeyListener(this);
    }

    private boolean validarNroTimbrado() {
        int nroTimbrado = -1;
        if (this.vista.jtfNroTimbrado.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroTimbrado = Integer.valueOf(this.vista.jtfNroTimbrado.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroTimbrado < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarNroSucursal() {
        int nroSucursal = -1;
        if (this.vista.jtfNroSucursal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroSucursal = Integer.valueOf(this.vista.jtfNroSucursal.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroSucursal < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarNroPVTA() {
        int nroPVTA = -1;
        if (this.vista.jtfPuntoVenta.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroPVTA = Integer.valueOf(this.vista.jtfPuntoVenta.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroPVTA < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarNroBoletaInicial() {
        int nroBoletaInicial = -1;
        if (this.vista.jtfBoletaInicial.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroBoletaInicial = Integer.valueOf(this.vista.jtfBoletaInicial.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroBoletaInicial < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarNroBoletaFinal() {
        int nroBoletaFinal = -1;
        if (this.vista.jtfBoletaFinal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroBoletaFinal = Integer.valueOf(this.vista.jtfBoletaFinal.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroBoletaFinal < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarFecha() {
        Date today = Calendar.getInstance().getTime();
        Date fechaVencimiento = this.vista.jdcFechaVencimiento.getDate();
        return fechaVencimiento.after(today);
    }

    private boolean validarRangoFactura() {
        int nroBoletaInicial = Integer.valueOf(this.vista.jtfBoletaInicial.getText().trim());
        int nroBoletaFinal = Integer.valueOf(this.vista.jtfBoletaFinal.getText().trim());
        return nroBoletaInicial < nroBoletaFinal;
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    private void guardar() {
        if (!validarNroTimbrado()) {
            return;
        }
        if (validarNroSucursal()) {
            JOptionPane.showMessageDialog(vista, "La factura ingresada ya posee una retención existente", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validarNroPVTA()) {
            JOptionPane.showMessageDialog(vista, "La fecha de retención es menor a la fecha de venta", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validarNroBoletaInicial()) {
            JOptionPane.showMessageDialog(vista, "La fecha de retención es menor a la fecha de venta", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validarNroBoletaFinal()) {
            JOptionPane.showMessageDialog(vista, "La fecha de retención es menor a la fecha de venta", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validarRangoFactura()) {
            JOptionPane.showMessageDialog(vista, "La fecha de retención es menor a la fecha de venta", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validarFecha()) {
            JOptionPane.showMessageDialog(vista, "La fecha de retención es menor a la fecha de venta", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int nroTimbrado = Integer.valueOf(this.vista.jtfNroTimbrado.getText().trim());
        int nroSucursal = Integer.valueOf(this.vista.jtfNroSucursal.getText().trim());
        int nroPVTA = Integer.valueOf(this.vista.jtfPuntoVenta.getText().trim());
        int nroBoletaInicial = Integer.valueOf(this.vista.jtfBoletaInicial.getText().trim());
        int nroBoletaFinal = Integer.valueOf(this.vista.jtfBoletaFinal.getText().trim());
        Date fechaVencimiento = this.vista.jdcFechaVencimiento.getDate();
        modelo.getTimbrado().setNroTimbrado(nroTimbrado);
        modelo.getTimbrado().setNroSucursal(nroSucursal);
        modelo.getTimbrado().setNroPuntoVenta(nroPVTA);
        modelo.getTimbrado().setNroBoletaInicial(nroBoletaInicial);
        modelo.getTimbrado().setNroBoletaFinal(nroBoletaFinal);
        modelo.getTimbrado().setFechaVencimiento(fechaVencimiento);
        modelo.guardarTimbrado();
        mostrarMensaje(WITHHOLDING_TAX_SUCCESS);
        cerrar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(vista.jbAceptar)) {
            guardar();
        }
        if (source.equals(vista.jtfNroTimbrado)) {
            vista.jtfNroSucursal.requestFocusInWindow();
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
            case KeyEvent.VK_ENTER: {
                if (vista.jbAceptar.hasFocus()) {
                    guardar();
                }
                break;
            }
        }
    }

}
