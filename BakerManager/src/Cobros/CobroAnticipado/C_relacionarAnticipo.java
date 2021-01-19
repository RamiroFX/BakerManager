/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import Cliente.SeleccionarCliente;
import Cobros.C_seleccionarFacturaPendiente;
import Cobros.SeleccionarFacturaPendiente;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.M_cliente;
import Interface.RecibirClienteCallback;
import Interface.RecibirCtaCteCabeceraCallback;
import Interface.RecibirCtaCteDetalleCallback;
import Interface.RecibirEmpleadoCallback;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_relacionarAnticipo extends MouseAdapter implements ActionListener, KeyListener,
        RecibirClienteCallback, RecibirCtaCteCabeceraCallback, RecibirCtaCteDetalleCallback {

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
    M_relacionarAnticipo modelo;
    V_relacionarAnticipo vista;
    private C_inicio inicio;

    public C_relacionarAnticipo(M_relacionarAnticipo modelo, V_relacionarAnticipo vista, C_inicio inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.inicio = inicio;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
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
        this.vista.jtReciboDetalle.setModel(modelo.getTm());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbCliente.addActionListener(this);
        this.vista.jtfNroRecibo.addActionListener(this);
        this.vista.jtfNroFactura.addActionListener(this);
        this.vista.jbAgregarFactura.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbSeleccionarPago.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtReciboDetalle.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jtfCliente.addKeyListener(this);
        this.vista.jtfNroRecibo.addKeyListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        this.vista.jbAgregarFactura.addKeyListener(this);
        this.vista.jbEliminarDetalle.addKeyListener(this);
        this.vista.jbModificarDetalle.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private boolean validarDetalleReciboVacio() {
//        if (!this.modelo.getCtaCteDetalleTm().getList().isEmpty()) {
//            JOptionPane.showMessageDialog(vista, VALIDAR_DETALLE_RECIBO, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
//            return false;
//        }
        return true;
    }

    private boolean validarCliente() {
        int idCliente = modelo.getCliente().getIdCliente();
        if (idCliente < 1) {
            JOptionPane.showMessageDialog(vista, VALIDAR_CLIENTE_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void invocarVistaSeleccionCliente() {
        if (!validarDetalleReciboVacio()) {
            return;
        }
        SeleccionarCliente sc = new SeleccionarCliente(inicio.vista);
        sc.setCallback(this);
        sc.mostrarVista();
    }

    private void invocarVistaSeleccionPagoAdelantado() {
        if (!validarDetalleReciboVacio()) {
            return;
        }
        if (!validarCliente()) {
            return;
        }
        int idCliente = modelo.getCliente().getIdCliente();
        SeleccionarPagoAnticipado sc = new SeleccionarPagoAnticipado(inicio.vista, idCliente, this);
        sc.mostrarVista();
    }

    private void invocarVistaSeleccionFacturaPendiente() {
        if (!validarCliente()) {
            return;
        }
        int idCliente = modelo.getCliente().getIdCliente();
        SeleccionarFacturaPendiente sc = new SeleccionarFacturaPendiente(this.vista, idCliente, C_seleccionarFacturaPendiente.TIPO_COBRO);
        sc.setCallback(this);
        sc.mostrarVista();
    }

    private double obtenerSumaDetalle() {
        double total = 0;
        for (E_cuentaCorrienteDetalle unDetalle : modelo.getTm().getList()) {
            total = total + unDetalle.getMonto();
        }
        return total;
    }

    private void sumarDetalle() {
        double total = obtenerSumaDetalle();
        double totalPagado = modelo.getCabecera().getDebito();
        this.vista.jftTotal.setValue(total);
        this.vista.jftTotalPorAsignar.setValue(totalPagado - total);
    }

    private boolean validarSubTotalDetalle(double monto) {
        double saldoPendiente = modelo.getCabecera().getDebito();
        double subTotal = monto + obtenerSumaDetalle();
        if (subTotal > saldoPendiente) {
            JOptionPane.showMessageDialog(vista, "La suma de los detalles no puede superar al total del adelanto.", VALIDAR_TITULO, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarSubDetalle(int idVenta) {
        double montoPendiente = modelo.obtenerDetalleVenta(idVenta).getSaldo();
        double montoIngresado = modelo.obtenerSumaDetalle(idVenta);
        if (montoIngresado > montoPendiente) {
            JOptionPane.showMessageDialog(vista, "El total ingresado supera al monto pendiente.", VALIDAR_TITULO, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void guardar(){
        modelo.guardar();
        cerrar();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbAceptar)) {
            guardar();
        } else if (source.equals(this.vista.jbSeleccionarPago)) {
            invocarVistaSeleccionPagoAdelantado();
        } else if (source.equals(this.vista.jbAgregarFactura)) {
            invocarVistaSeleccionFacturaPendiente();
        } else if (source.equals(this.vista.jbCliente)) {
            invocarVistaSeleccionCliente();
        } else if (source.equals(this.vista.jbEliminarDetalle)) {
            //eliminarDetalle();
        } else if (source.equals(this.vista.jbModificarDetalle)) {
            //modificarDetalle();
        } else if (source.equals(this.vista.jbSalir)) {
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
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        modelo.setCliente(cliente);
        this.vista.jtfCliente.setText(cliente.getEntidad() + "(" + cliente.getRuc() + "-" + cliente.getRucId() + ")");
    }

    @Override
    public void recibirCtaCteCabecera(E_cuentaCorrienteCabecera cabecera) {
        this.modelo.setCabecera(cabecera);
        this.vista.jtfNroRecibo.setText(cabecera.getNroRecibo() + "");
        this.vista.jtfPagoAnticipado.setText(cabecera.getFechaPago() + "");
        this.vista.jftTotalPagado.setValue(cabecera.getDebito());
    }

    @Override
    public void modificarCtaCteCabecera(int index, E_cuentaCorrienteCabecera cabecera) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recibirCtaCteDetalle(E_cuentaCorrienteDetalle detalle, int montoTotalPendiente) {
        if (!validarSubTotalDetalle(detalle.getMonto())) {
            return;
        }
        if (!validarSubDetalle(detalle.getIdFacturaCabecera())) {
            return;
        }
        this.modelo.agregarDatos(detalle);
        sumarDetalle();
    }

    @Override
    public void modificarCtaCteDetalle(int index, E_cuentaCorrienteDetalle detalle, int montoTotalPendiente) {
        if (!validarSubTotalDetalle(detalle.getMonto())) {
            return;
        }
        sumarDetalle();
    }
}
