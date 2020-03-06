/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import Entities.E_egresoSinPago;
import Entities.E_reciboPagoDetalle;
import Entities.M_proveedor;
import Interface.RecibirProveedorCallback;
import Interface.RecibirReciboPagoDetalleCallback;
import Proveedor.Seleccionar_proveedor;
import bakermanager.C_inicio;
import java.awt.EventQueue;
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
public class C_crearPago extends MouseAdapter implements ActionListener, KeyListener,
        RecibirProveedorCallback, RecibirReciboPagoDetalleCallback {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un cobrador",
            VALIDAR_CLIENTE_MSG = "Seleccione un proveedor",
            VALIDAR_NRO_RECIBO_MSG_1 = "Ingrese un Número de recibo",
            VALIDAR_NRO_RECIBO_MSG_2 = "Ingrese solo números enteros en Número de recibo",
            VALIDAR_NRO_RECIBO_MSG_3 = "Ingrese solo números enteros y positivos en Número de recibo",
            VALIDAR_NRO_RECIBO_MSG_4 = "El Número de recibo ingresado ya se encuentra en uso.",
            VALIDAR_FECHA_RECIBO_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_MONTO_A_PAGAR = "El saldo a pagar no puede ser mayor al total",
            VALIDAR_DETALLE_RECIBO = "Existen detalles de cobros pendiente. Vacíe la lista para seleccionar otro proveedor",
            CONFIRMAR_SALIR_MSG = "¿Cancelar pago?",
            VALIDAR_TITULO = "Atención";
    public M_crearPago modelo;
    public V_crearPago vista;
    private C_inicio inicio;

    public C_crearPago(M_crearPago modelo, V_crearPago vista, C_inicio inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.inicio = inicio;
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
                if (modelo.getDetalleTm().getList().isEmpty()) {
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
        this.vista.jtReciboDetalle.setModel(modelo.getDetalleTm());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaCobro.setDate(calendar.getTime());
    }

    private void agregarListeners() {
        this.vista.jtReciboDetalle.addMouseListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbAgregarFactura.addActionListener(this);
        this.vista.jbProveedor.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbAgregarFactura.addKeyListener(this);
        this.vista.jbProveedor.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private void eliminarDetalle() {
        int fila = this.vista.jtReciboDetalle.getSelectedRow();
        if (fila > -1) {
            modelo.eliminarDatos(fila);
            sumarTotal();
        }
    }

    public void modificarDetalle() {
        int fila = this.vista.jtReciboDetalle.getSelectedRow();
        if (fila > -1) {
            E_reciboPagoDetalle pagoDetalle = modelo.getDetalleTm().getList().get(fila);
            E_egresoSinPago cabecera = new E_egresoSinPago();
            cabecera.setProveedorEntidad(modelo.getCabecera().getProveedor().getEntidad());
            cabecera.setIdCabecera(pagoDetalle.getIdFacturaCabecera());
            cabecera.setNroFactura(pagoDetalle.getNroFactura());
            cabecera.setMonto((int) pagoDetalle.getMonto());
            vista.jbAceptar.setEnabled(true);
            ReciboPago rp = new ReciboPago(this.vista);
            rp.modificarDetalle(fila, cabecera, pagoDetalle);
            rp.setInterface(this);
            rp.mostrarVista();
        }
    }

    private void guardar() {
        if (!validarProveedor()) {
            return;
        }
        if (!validarNroRecibo()) {
            return;
        }
        if (!validarFechaUtilizacion()) {
            return;
        }
        if (!validarCantidadFacturas()) {
            return;
        }
        Date fechaPago = vista.jdcFechaCobro.getDate();
        int nroRecibo = Integer.valueOf(this.vista.jtfNroRecibo.getText().trim());
        modelo.getCabecera().setFechaPago(fechaPago);
        modelo.getCabecera().setNroRecibo(nroRecibo);
        modelo.guardarCobro();
        limpiarCampos();
        cerrar();
    }

    private boolean validarFechaUtilizacion() {
        Date entrega = null;
        try {
            entrega = vista.jdcFechaCobro.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_RECIBO_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (entrega == null) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_RECIBO_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarProveedor() {
        if (this.vista.jtfProveedor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_CLIENTE_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarDetalleReciboVacio() {
        if (!this.modelo.getDetalleTm().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_DETALLE_RECIBO, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarNroRecibo() {
        int nroRecibo = -1;
        if (this.vista.jtfNroRecibo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RECIBO_MSG_1, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            nroRecibo = Integer.valueOf(this.vista.jtfNroRecibo.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RECIBO_MSG_2, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (nroRecibo < 0) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RECIBO_MSG_3, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (modelo.existeRecibo(nroRecibo)) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RECIBO_MSG_4, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCantidadFacturas() {
        /*if (modelo.getTm().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_CANT_PRODUCTOS_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }*/
        return true;
    }

    private void limpiarCampos() {
        this.modelo.limpiarCampos();
        this.vista.jtfProveedor.setText("");
        this.vista.jtfNroRecibo.setText("");
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaCobro.setDate(calendar.getTime());
    }

    private void invocarVistaSeleccionFacturaPendiente() {
        if (!validarProveedor()) {
            return;
        }
        int idProveedor = modelo.getCabecera().getProveedor().getId();
        SeleccionarPagoPendiente spp = new SeleccionarPagoPendiente(this.vista, idProveedor);
        spp.setCallback(this);
        spp.mostrarVista();
    }

    private void invocarVistaSeleccionProveedor() {
        if (!validarDetalleReciboVacio()) {
            return;
        }
        Seleccionar_proveedor sc = new Seleccionar_proveedor(this.vista);
        sc.setCallback(this);
        sc.mostrarVista();
    }

    private void sumarTotal() {
        this.vista.jftTotal.setValue(modelo.getTotal());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbAceptar)) {
            guardar();
        } else if (source.equals(this.vista.jbAgregarFactura)) {
            invocarVistaSeleccionFacturaPendiente();
        } else if (source.equals(this.vista.jbProveedor)) {
            invocarVistaSeleccionProveedor();
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
        if (e.getSource().equals(this.vista.jtReciboDetalle)) {
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
                guardar();
                break;
            }
            case KeyEvent.VK_F3: {
                invocarVistaSeleccionProveedor();
                break;
            }
            case KeyEvent.VK_F4: {

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
    public void recibirProveedor(M_proveedor proveedor) {
        modelo.getCabecera().setProveedor(proveedor);
        this.vista.jtfProveedor.setText(proveedor.getEntidad() + "(" + proveedor.getRuc() + "-" + proveedor.getRuc_id() + ")");
    }

    @Override
    public void recibirReciboPagoDetalle(E_reciboPagoDetalle detalle, int montoTotalPendiente) {
        if (this.modelo.controlarMontoIngresado(detalle.getIdFacturaCabecera(), (int) detalle.getMonto(), montoTotalPendiente)) {
            this.modelo.agregarDatos(detalle);
        } else {
            JOptionPane.showMessageDialog(vista, VALIDAR_MONTO_A_PAGAR, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
        }
        sumarTotal();
        Utilities.c_packColumn.packColumns(vista.jtReciboDetalle, 1);
    }

    @Override
    public void modificarReciboPagoDetalle(int index, E_reciboPagoDetalle detalle, int montoTotalPendiente) {
        if (this.modelo.controlarMontoIngresado(detalle.getIdFacturaCabecera(), (int) detalle.getMonto(), montoTotalPendiente)) {
            this.modelo.modificarDetalle(index, detalle);
        } else {
            JOptionPane.showMessageDialog(vista, VALIDAR_MONTO_A_PAGAR, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
        }
        sumarTotal();
        Utilities.c_packColumn.packColumns(vista.jtReciboDetalle, 1);
    }
}
