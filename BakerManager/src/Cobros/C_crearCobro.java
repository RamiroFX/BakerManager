/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Cliente.Seleccionar_cliente;
import Empleado.Seleccionar_funcionario;
import Entities.E_cuentaCorrienteDetalle;
import Entities.M_cliente;
import Entities.M_funcionario;
import Interface.RecibirClienteCallback;
import Interface.RecibirCtaCteDetalleCallback;
import Interface.RecibirEmpleadoCallback;
import bakermanager.C_inicio;
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
public class C_crearCobro extends MouseAdapter implements ActionListener, KeyListener,
        RecibirEmpleadoCallback, RecibirClienteCallback, RecibirCtaCteDetalleCallback {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un cobrador",
            VALIDAR_CLIENTE_MSG = "Seleccione un cliente",
            VALIDAR_NRO_RECIBO_MSG_1 = "Ingrese un Número de recibo",
            VALIDAR_NRO_RECIBO_MSG_2 = "Ingrese solo números enteros en Número de recibo",
            VALIDAR_NRO_RECIBO_MSG_3 = "Ingrese solo números enteros y positivos en Número de recibo",
            VALIDAR_NRO_RECIBO_MSG_4 = "El Número de recibo ingresado ya se encuentra en uso.",
            VALIDAR_FECHA_RECIBO_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_MONTO_A_PAGAR = "El saldo a pagar no puede ser mayor al total",
            CONFIRMAR_SALIR_MSG = "¿Cancelar cobro?",
            VALIDAR_TITULO = "Atención";
    public M_crearCobro modelo;
    public V_crearCobro vista;
    private C_inicio inicio;

    public C_crearCobro(M_crearCobro modelo, V_crearCobro vista, C_inicio inicio) {
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
        /* EventQueue.invokeLater(new Runnable() {
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
        });*/
    }

    private void inicializarVista() {
        this.vista.jtReciboDetalle.setModel(modelo.getCtaCteDetalleTm());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaCobro.setDate(calendar.getTime());
    }

    private void agregarListeners() {
        this.vista.jtReciboDetalle.addMouseListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbAgregarFactura.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbFuncionario.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbAgregarFactura.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbFuncionario.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private void eliminarDetalle() {
        int fila = this.vista.jtReciboDetalle.getSelectedRow();
        if (fila > -1) {
            //modelo.removerDetalle(fila);
        }
    }

    public void modificarDetalle() {
        int fila = this.vista.jtReciboDetalle.getSelectedRow();
        if (fila > -1) {
            /*M_producto producto = modelo.getTm().getList().get(fila).getProducto();
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this.vista, producto, this, fila);
            scp.setVisible(true);*/
        }
    }

    private void guardar() {
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
        Date fechaUtilizacion = vista.jdcFechaCobro.getDate();
        int nroRecibo = Integer.valueOf(this.vista.jtfNroRecibo.getText().trim());
        /*modelo.getCabecera().setFechaCobro(fechaUtilizacion);
        modelo.getCabecera().setNroRecibo(nroRecibo);
        modelo.guardarRecibo();*/
        limpiarCampos();
        cerrar();
    }

    private boolean validarFechaUtilizacion() {
        //Date now = Calendar.getInstance().getTime();
        Date entrega = null;
        try {
            entrega = vista.jdcFechaCobro.getDate();
        } catch (Exception ex) {
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
        /*if (modelo.existeRecibo(nroRecibo)) {
            JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_4, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }*/
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
        this.vista.jtfNroRecibo.setText("");
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaCobro.setDate(calendar.getTime());
    }

    private void invocarVistaSeleccionFacturaPendiente() {
        if (!validarCliente()) {
            return;
        }
        int idCliente = modelo.getCabecera().getCliente().getIdCliente();
        SeleccionarFacturaPendiente sc = new SeleccionarFacturaPendiente(this.vista, idCliente);
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
        } else if (source.equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(inicio.vista);
            sc.setCallback(this);
            sc.mostrarVista();
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
                Seleccionar_cliente sc = new Seleccionar_cliente(inicio.vista);
                sc.setCallback(this);
                sc.mostrarVista();
                break;
            }
            case KeyEvent.VK_F5: {
                Seleccionar_funcionario sf = new Seleccionar_funcionario(this.vista);
                sf.setCallback(this);
                sf.mostrarVista();
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
    public void recibirFuncionario(M_funcionario funcionario) {
        //modelo.getCabecera().setFuncionarioProduccion(funcionario);
        this.vista.jtfFuncionario.setText(funcionario.getNombre());
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        modelo.getCabecera().setCliente(cliente);
        this.vista.jtfCliente.setText(cliente.getEntidad() + "(" + cliente.getRuc() + "-" + cliente.getRucId() + ")");
    }

    @Override
    public void recibirCtaCteDetalle(E_cuentaCorrienteDetalle detalle, int montoTotalPendiente) {
        if (this.modelo.controlarMontoIngresado(detalle.getIdFacturaCabecera(), (int) detalle.getMonto(), montoTotalPendiente)) {
            this.modelo.agregarDatos(detalle);
        } else {
            JOptionPane.showMessageDialog(vista, VALIDAR_MONTO_A_PAGAR, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
        }
        sumarTotal();
    }

    @Override
    public void modificarCtaCteDetalle(int index, E_cuentaCorrienteDetalle detalle, int montoTotalPendiente) {
        //this.modelo.getCtaCteDetalleTm().(detalle);
        sumarTotal();
    }
}
