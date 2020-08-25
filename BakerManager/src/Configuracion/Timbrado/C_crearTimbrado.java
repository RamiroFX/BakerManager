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

    private static final String VALIDAR_NRO_TIMBRADO_1 = "Ingrese solo números enteros en número de Timbrado",
            VALIDAR_NRO_TIMBRADO_2 = "Ingrese solo números enteros y positivos en número de Timbrado",
            VALIDAR_NRO_TIMBRADO_3 = "Ingrese un número de Timbrado",
            VALIDAR_NRO_SUCURSAL_1 = "Ingrese solo números enteros en número de Sucursal",
            VALIDAR_NRO_SUCURSAL_2 = "Ingrese solo números enteros y positivos en número de Sucursal",
            VALIDAR_NRO_SUCURSAL_3 = "Ingrese un número de Sucursal",
            VALIDAR_NRO_PVTA_1 = "Ingrese solo números enteros en número de Punto de venta",
            VALIDAR_NRO_PVTA_2 = "Ingrese solo números enteros y positivos en número de Punto de venta",
            VALIDAR_NRO_PVTA_3 = "Ingrese un número de Punto de venta",
            VALIDAR_NRO_BOLETA_INICIAL_1 = "Ingrese solo números enteros en número de Boleta inicial",
            VALIDAR_NRO_BOLETA_INICIAL_2 = "Ingrese solo números enteros y positivos en número de Boleta inicial",
            VALIDAR_NRO_BOLETA_INICIAL_3 = "Ingrese un número de Boleta inicial",
            VALIDAR_NRO_BOLETA_FINAL_1 = "Ingrese solo números enteros en número de Boleta final",
            VALIDAR_NRO_BOLETA_FINAL_2 = "Ingrese solo números enteros y positivos en número de Boleta final",
            VALIDAR_NRO_BOLETA_FINAL_3 = "Ingrese un número de Boleta final",
            TIMBRADO_SUCCESS = "Timbrado creado";

    private M_crearTimbrado modelo;
    private V_crearTimbrado vista;

    public C_crearTimbrado(M_crearTimbrado modelo, V_crearTimbrado vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
        loadData();
    }

    private void inicializarVista() {
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
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_TIMBRADO_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroTimbrado = Integer.valueOf(this.vista.jtfNroTimbrado.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_TIMBRADO_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroTimbrado < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_TIMBRADO_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarNroSucursal() {
        int nroSucursal = -1;
        if (this.vista.jtfNroSucursal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_SUCURSAL_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroSucursal = Integer.valueOf(this.vista.jtfNroSucursal.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_SUCURSAL_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroSucursal < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_SUCURSAL_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarNroPVTA() {
        int nroPVTA = -1;
        if (this.vista.jtfPuntoVenta.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_PVTA_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroPVTA = Integer.valueOf(this.vista.jtfPuntoVenta.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_PVTA_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroPVTA < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_PVTA_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarNroBoletaInicial() {
        int nroBoletaInicial = -1;
        if (this.vista.jtfBoletaInicial.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_BOLETA_INICIAL_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroBoletaInicial = Integer.valueOf(this.vista.jtfBoletaInicial.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_BOLETA_INICIAL_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroBoletaInicial < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_BOLETA_INICIAL_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarNroBoletaFinal() {
        int nroBoletaFinal = -1;
        if (this.vista.jtfBoletaFinal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_BOLETA_FINAL_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroBoletaFinal = Integer.valueOf(this.vista.jtfBoletaFinal.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_BOLETA_FINAL_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroBoletaFinal < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_BOLETA_FINAL_2, "Atención", JOptionPane.WARNING_MESSAGE);
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
        if (nroBoletaFinal < nroBoletaInicial) {
            JOptionPane.showMessageDialog(vista, "El numero de boleta final es menor al inicial", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private boolean validarTimbradoExistente() {
        int nroTimbrado = Integer.valueOf(this.vista.jtfNroTimbrado.getText().trim());
        int nroSucursal = Integer.valueOf(this.vista.jtfNroSucursal.getText().trim());
        int nroPVTA = Integer.valueOf(this.vista.jtfPuntoVenta.getText().trim());
        int nroBoletaInicial = Integer.valueOf(this.vista.jtfBoletaInicial.getText().trim());
        int nroBoletaFinal = Integer.valueOf(this.vista.jtfBoletaFinal.getText().trim());
        return modelo.comprobarTimbradoExistente(nroTimbrado, nroSucursal, nroPVTA, nroBoletaInicial, nroBoletaFinal);
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    private void guardar() {
        if (!validarNroTimbrado()) {
            return;
        }
        if (!validarNroSucursal()) {
            return;
        }
        if (!validarNroPVTA()) {
            return;
        }
        if (!validarNroBoletaInicial()) {
            return;
        }
        if (!validarNroBoletaFinal()) {
            return;
        }
        if (!validarRangoFactura()) {
            return;
        }
        if (validarTimbradoExistente()) {
            JOptionPane.showMessageDialog(vista, "El timbrado ingresado ya se encuentra registrado", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validarFecha()) {
            JOptionPane.showMessageDialog(vista, "La fecha de vencimiento es menor a la fecha actual", "Atención", JOptionPane.ERROR_MESSAGE);
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
        mostrarMensaje(TIMBRADO_SUCCESS);
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
        if (source.equals(vista.jtfNroSucursal)) {
            vista.jtfPuntoVenta.requestFocusInWindow();
        }
        if (source.equals(vista.jtfPuntoVenta)) {
            vista.jtfBoletaInicial.requestFocusInWindow();
        }
        if (source.equals(vista.jtfBoletaInicial)) {
            vista.jtfBoletaFinal.requestFocusInWindow();
        }
        if (source.equals(vista.jtfBoletaFinal)) {
            vista.jdcFechaVencimiento.requestFocusInWindow();
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
