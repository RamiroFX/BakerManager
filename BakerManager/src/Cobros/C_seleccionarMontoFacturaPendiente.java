/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Producto;
import Egresos.C_crear_egreso;
import Entities.E_facturaSinPago;
import Entities.M_facturaDetalle;
import Entities.M_menu_item;
import Entities.M_pedidoDetalle;
import Entities.M_producto;
import Interface.RecibirCtaCteDetalleCallback;
import Interface.RecibirProductoCallback;
import MenuPrincipal.DatosUsuario;
import Pedido.C_crearPedido;
import Pedido.C_verPedido;
import Producto.C_seleccionarProducto;
import Ventas.C_crearVentaRapida;
import Ventas.C_verMesa;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarMontoFacturaPendiente implements ActionListener, KeyListener {

    private static final String PAGO_ERROR_MESSAGE_1 = "Asegurese de colocar un numero valido en el campo Pago.",
            PAGO_ERROR_MESSAGE_2 = "El monto a pagar debe ser mayor a 0.",
            PAGO_ERROR_MESSAGE_3 = "El monto a pagar no debe ser mayor al Saldo.";

    M_seleccionarMontoFacturaPendiente modelo;
    V_seleccionarMontoFacturaPendiente vista;
    RecibirCtaCteDetalleCallback callback;

    public C_seleccionarMontoFacturaPendiente(M_seleccionarMontoFacturaPendiente modelo, V_seleccionarMontoFacturaPendiente vista) {
        this.modelo = modelo;
        this.vista = vista;
        agregarListeners();
    }

    private void agregarListeners() {
        this.vista.jbOK.addActionListener(this);
        this.vista.jbCancel.addActionListener(this);
        this.vista.jtfPago.addActionListener(this);
        this.vista.jbCancel.addKeyListener(this);
        this.vista.jbOK.addKeyListener(this);
        this.vista.jtfCliente.addKeyListener(this);
        this.vista.jtfIdFactura.addKeyListener(this);
        this.vista.jtfMontoTotal.addKeyListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        this.vista.jtfPago.addKeyListener(this);
        this.vista.jtfSaldo.addKeyListener(this);
    }

    public void setCallback(RecibirCtaCteDetalleCallback callback) {
        this.callback = callback;
    }

    public void inicializarVista(E_facturaSinPago fsp) {
        this.modelo.setE_facturaSinPago(fsp);
        this.vista.jtfIdFactura.setText(fsp.getIdCabecera() + "");
        this.vista.jtfNroFactura.setText(fsp.getNroFactura() + "");
        this.vista.jtfCliente.setText(fsp.getClienteEntidad() + "");
        this.vista.jtfMontoTotal.setText(fsp.getMonto() + "");
        this.vista.jtfSaldo.setText(fsp.getSaldo() + "");
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    public void cerrar() {
        this.vista.dispose();
    }

    public void enviarCantidad() {
        if (!checkearMonto()) {
            return;
        }
        String cantidadAux = this.vista.jtfPago.getText().trim();
        Integer monto = Integer.valueOf(cantidadAux);
        modelo.getCorrienteDetalle().setMonto(monto);
        modelo.getCorrienteDetalle().setNroFactura(modelo.getE_facturaSinPago().getNroFactura());
        modelo.getCorrienteDetalle().setIdFacturaCabecera(modelo.getE_facturaSinPago().getIdCabecera());
        callback.recibirCtaCteDetalle(modelo.getCorrienteDetalle(), modelo.getE_facturaSinPago().getMonto());
        cerrar();
    }

    private boolean checkearMonto() {
        Integer d = null;
        if (this.vista.jtfPago.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, PAGO_ERROR_MESSAGE_1,
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPago.setText("0");
            this.vista.jtfPago.requestFocusInWindow();
            return false;
        }
        try {
            String cantidadAux = this.vista.jtfPago.getText().trim();
            d = Integer.valueOf(cantidadAux);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, PAGO_ERROR_MESSAGE_1,
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPago.setText("0");
            this.vista.jtfPago.requestFocusInWindow();
            return false;
        }
        if (d < 1) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, PAGO_ERROR_MESSAGE_2,
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPago.requestFocusInWindow();
            return false;
        }
        if (d > modelo.getE_facturaSinPago().getSaldo()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, PAGO_ERROR_MESSAGE_3,
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfPago.requestFocusInWindow();
            return false;

        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbOK)) {
            enviarCantidad();
        } else if (e.getSource().equals(this.vista.jbCancel)) {
            this.vista.dispose();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if (this.vista.jtfIdFactura.hasFocus()
                || this.vista.jtfNroFactura.hasFocus()
                || this.vista.jtfMontoTotal.hasFocus()
                || this.vista.jtfPago.hasFocus()
                || this.vista.jtfSaldo.hasFocus()) {
            if (ke.getKeyChar() == '\n') {
                enviarCantidad();
            }
            if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
                cerrar();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }
}
