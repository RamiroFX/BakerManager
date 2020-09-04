/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import Cliente.Seleccionar_cliente;
import Cobros.C_gestionCobro;
import Cobros.ReciboCobro;
import Cobros.SeleccionarFacturaPendiente;
import Empleado.Seleccionar_funcionario;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaSinPago;
import Entities.E_movimientoContable;
import Entities.E_reciboTipoPago;
import Entities.M_cliente;
import Entities.M_funcionario;
import Interface.RecibirClienteCallback;
import Interface.RecibirCtaCteDetalleCallback;
import Interface.RecibirEmpleadoCallback;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_cobroAnticipado extends MouseAdapter implements ActionListener, KeyListener,
        RecibirEmpleadoCallback, RecibirClienteCallback, RecibirCtaCteDetalleCallback {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un cobrador",
            VALIDAR_CLIENTE_MSG = "Seleccione un cliente",
            VALIDAR_NRO_RECIBO_MSG_1 = "Ingrese un Número de recibo",
            VALIDAR_NRO_RECIBO_MSG_2 = "Ingrese solo números enteros en Número de recibo",
            VALIDAR_NRO_RECIBO_MSG_3 = "Ingrese solo números enteros y positivos en Número de recibo",
            VALIDAR_NRO_RECIBO_MSG_4 = "El Número de recibo ingresado ya se encuentra en uso.",
            VALIDAR_FECHA_RECIBO_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_MONTO_A_PAGAR = "El saldo a pagar no puede ser mayor al total",
            VALIDAR_DETALLE_RECIBO = "Existen detalles de cobros pendiente. Vacíe la lista para seleccionar otro cliente",
            CONFIRMAR_SALIR_MSG = "¿Cancelar cobro?",
            VALIDAR_TITULO = "Atención";
    public M_cobroAnticipado modelo;
    public V_cobroAnticipado vista;
    private C_inicio inicio;

    public C_cobroAnticipado(M_cobroAnticipado modelo, V_cobroAnticipado vista, C_inicio inicio) {
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
                if (modelo.getCtaCteDetalleTm().getList().isEmpty()) {
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
        this.vista.jtReciboDetalle.setModel(modelo.getCtaCteDetalleTm());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaCobro.setDate(calendar.getTime());
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbCliente.addActionListener(this);
        this.vista.jtfNroRecibo.addActionListener(this);
        this.vista.jbFuncionario.addActionListener(this);
        this.vista.jbAgregarMonto.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtReciboDetalle.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jtfCliente.addKeyListener(this);
        this.vista.jtfNroRecibo.addKeyListener(this);
        this.vista.jbFuncionario.addKeyListener(this);
        this.vista.jtfFuncionario.addKeyListener(this);
        this.vista.jdcFechaCobro.addKeyListener(this);
        this.vista.jbAgregarMonto.addKeyListener(this);
        this.vista.jbEliminarDetalle.addKeyListener(this);
        this.vista.jbModificarDetalle.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jbImprimir.addKeyListener(this);
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
            E_cuentaCorrienteDetalle ctaCteDet = modelo.getCtaCteDetalleTm().getList().get(fila);
            E_facturaSinPago cabecera = new E_facturaSinPago();
            cabecera.setClienteEntidad(modelo.getCabecera().getCliente().getEntidad());
            cabecera.setIdCabecera(ctaCteDet.getIdFacturaCabecera());
            cabecera.setNroFactura(ctaCteDet.getNroFactura());
            cabecera.setMonto((int) ctaCteDet.getMonto());
            vista.jbAceptar.setEnabled(true);
            E_movimientoContable mc = new E_movimientoContable();
            mc.setVenta(cabecera);
            switch (ctaCteDet.getTipoPago().getId()) {
                case E_reciboTipoPago.TIPO_FACTURA: {
                    mc.setTipo(E_movimientoContable.TIPO_VENTA);
                    break;
                }
                case E_reciboTipoPago.TIPO_SALDO_INICIAL: {
                    mc.setTipo(E_movimientoContable.TIPO_SALDO_INICIAL);
                    break;
                }
            }
            ReciboCobro rp = new ReciboCobro(this.vista);
            rp.modificarDetalle(fila, mc, ctaCteDet);
            rp.setInterface(this);
            rp.mostrarVista();
        }
    }

    private void guardar() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!validarCliente()) {
                    return;
                }
                if (!validarNroRecibo()) {
                    return;
                }
                if (!validarResponsable()) {
                    return;
                }
                if (!validarFechaUtilizacion()) {
                    return;
                }
                if (!validarCantidadFacturas()) {
                    return;
                }
                Date fechaPago = vista.jdcFechaCobro.getDate();
                int nroRecibo = Integer.valueOf(vista.jtfNroRecibo.getText().trim());
                modelo.getCabecera().setFechaPago(fechaPago);
                modelo.getCabecera().setNroRecibo(nroRecibo);
                modelo.guardarCobro();
                limpiarCampos();
                cerrar();
            }
        });
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

    private boolean validarResponsable() {
        if (this.vista.jtfFuncionario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_RESPONSABLE_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCliente() {
        if (this.vista.jtfCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_CLIENTE_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarDetalleReciboVacio() {
        if (!this.modelo.getCtaCteDetalleTm().getList().isEmpty()) {
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
        this.vista.jtfFuncionario.setText("");
        this.vista.jtfCliente.setText("");
        this.vista.jtfNroRecibo.setText("");
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaCobro.setDate(calendar.getTime());
    }

    private void invocarVistaSeleccionFacturaPendiente() {
        if (!validarCliente()) {
            return;
        }
        E_movimientoContable cabecera = new E_movimientoContable();
        cabecera.setTipo(E_movimientoContable.TIPO_PAGO);
        vista.jbAceptar.setEnabled(true);
        ReciboCobro rp = new ReciboCobro(this.vista);
        rp.pagoAnticipado(modelo.getCabecera().getCliente());
        rp.setInterface(this);
        rp.mostrarVista();
    }

    private void invocarVistaSeleccionCliente() {
        if (!validarDetalleReciboVacio()) {
            return;
        }
        Seleccionar_cliente sc = new Seleccionar_cliente(inicio.vista);
        sc.setCallback(this);
        sc.mostrarVista();
    }

    private void invocarVistaSeleccionFuncionario() {
        Seleccionar_funcionario sf = new Seleccionar_funcionario(this.vista);
        sf.setCallback(this);
        sf.mostrarVista();
    }

    private void sumarTotal() {
        this.vista.jftTotal.setValue(modelo.getTotal());
    }

    private void esperar() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
            Logger.getLogger(C_gestionCobro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbAceptar)) {
            guardar();
        } else if (source.equals(this.vista.jbAgregarMonto)) {
            invocarVistaSeleccionFacturaPendiente();
        } else if (source.equals(this.vista.jbCliente)) {
            invocarVistaSeleccionCliente();
        } else if (source.equals(this.vista.jbFuncionario)) {
            invocarVistaSeleccionFuncionario();
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
                esperar();
                guardar();
                break;
            }
            case KeyEvent.VK_F3: {
                esperar();
                invocarVistaSeleccionCliente();
                break;
            }
            case KeyEvent.VK_F4: {
                esperar();
                invocarVistaSeleccionFacturaPendiente();
                break;
            }
            case KeyEvent.VK_F5: {
                esperar();
                invocarVistaSeleccionFuncionario();
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                esperar();
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        modelo.getCabecera().setCobrador(funcionario);
        this.vista.jtfFuncionario.setText(funcionario.getNombre());
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        modelo.getCabecera().setCliente(cliente);
        this.vista.jtfCliente.setText(cliente.getEntidad() + "(" + cliente.getRuc() + "-" + cliente.getRucId() + ")");
    }

    @Override
    public void recibirCtaCteDetalle(E_cuentaCorrienteDetalle detalle, int montoTotalPendiente) {
        this.modelo.agregarDatos(detalle);
        sumarTotal();
        Utilities.c_packColumn.packColumns(vista.jtReciboDetalle, 1);
    }

    @Override
    public void modificarCtaCteDetalle(int index, E_cuentaCorrienteDetalle detalle, int montoTotalPendiente) {
        this.modelo.modificarDetalle(index, detalle);
        sumarTotal();
        Utilities.c_packColumn.packColumns(vista.jtReciboDetalle, 1);
    }
}
