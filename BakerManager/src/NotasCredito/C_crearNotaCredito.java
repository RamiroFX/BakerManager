/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import Cliente.Seleccionar_cliente;
import Entities.E_NotaCreditoDetalle;
import Entities.E_facturaDetalle;
import Entities.E_facturaSinPago;
import Entities.M_cliente;
import Entities.M_producto;
import Interface.RecibirClienteCallback;
import Interface.RecibirFacturaSinPagoCallback;
import Interface.RecibirProductoCallback;
import Producto.SeleccionarCantidadProduducto;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearNotaCredito extends MouseAdapter implements ActionListener, KeyListener,
        RecibirClienteCallback, RecibirFacturaSinPagoCallback, RecibirProductoCallback {

    private static final String VALIDAR_CLIENTE_MSG = "Seleccione un cliente",
            VALIDAR_NRO_NOTA_CREDITO_MSG_1 = "Ingrese un Número de Nota de Crédito",
            VALIDAR_NRO_NOTA_CREDITO_MSG_2 = "Ingrese solo números enteros en Número de Nota de Crédito",
            VALIDAR_NRO_NOTA_CREDITO_MSG_3 = "Ingrese solo números enteros y positivos en Número de Nota de Crédito",
            VALIDAR_NRO_NOTA_CREDITO_MSG_4 = "El Número de Nota de Crédito ingresado ya se encuentra en uso.",
            VALIDAR_NRO_NOTA_CREDITO_MSG_5 = "Ingrese una cantidad superior a 0 (cero).",
            VALIDAR_NRO_NOTA_CREDITO_MSG_6 = "El total supera al saldo pendiente.",
            VALIDAR_FECHA_NOTA_CREDITO_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_CANT_DETALLE_MSG = "Nota de credito vacía.",
            VALIDAR_DETALLE_NOTA_CREDITO = "Existen detalles de Nota de Crédito pendiente. Vacíe la lista para seleccionar otro cliente",
            CONFIRMAR_SALIR_MSG = "¿Cancelar Nota de Crédito?",
            VALIDAR_TITULO = "Atención";
    public M_crearNotaCredito modelo;
    public V_crearNotaCredito vista;
    private C_inicio inicio;

    public C_crearNotaCredito(M_crearNotaCredito modelo, V_crearNotaCredito vista, C_inicio inicio) {
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
                if (modelo.getNotaCreditoDetalleTm().getList().isEmpty()) {
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
        this.vista.jtNotaCreditoDetalle.setModel(modelo.getNotaCreditoDetalleTm());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaotaCredito.setDate(calendar.getTime());
    }

    private void agregarListeners() {
        this.vista.jtNotaCreditoDetalle.addMouseListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbAgregarFactura.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbAgregarFactura.addKeyListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        this.vista.jtfNroNotaCredito.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private void eliminarDetalle() {
        int fila = this.vista.jtNotaCreditoDetalle.getSelectedRow();
        if (fila > -1) {
            modelo.eliminarDatos(fila);
            sumarTotal();
        }
    }

    public void modificarDetalle() {
        int fila = this.vista.jtNotaCreditoDetalle.getSelectedRow();
        if (fila > -1) {
            E_NotaCreditoDetalle notaCreditoDetalle = modelo.getNotaCreditoDetalleTm().getList().get(fila);
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(vista, notaCreditoDetalle.getProducto(), this, fila);
            scp.setVisible(true);
        }
    }

    private void guardar() {
        if (!validarCliente()) {
            return;
        }
        if (!validarNroNotaCredito()) {
            return;
        }
        if (!validarFechaUtilizacion()) {
            return;
        }
        if (!validarCantidadItems()) {
            return;
        }
        Date fechaNotaCredito = vista.jdcFechaotaCredito.getDate();
        int nroNotaCredito = Integer.valueOf(this.vista.jtfNroNotaCredito.getText().trim());
        modelo.getCabecera().setTiempo(fechaNotaCredito);
        modelo.getCabecera().setNroNotaCredito(nroNotaCredito);
        modelo.guardarNotaCredito();
        limpiarCampos();
        cerrar();
    }

    private boolean validarFechaUtilizacion() {
        Date entrega = null;
        try {
            entrega = vista.jdcFechaotaCredito.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_NOTA_CREDITO_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (entrega == null) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_NOTA_CREDITO_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
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
        if (!this.modelo.getNotaCreditoDetalleTm().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_DETALLE_NOTA_CREDITO, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarNroNotaCredito() {
        int nroNotaCredito = -1;
        if (this.vista.jtfNroNotaCredito.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_NOTA_CREDITO_MSG_1, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            nroNotaCredito = Integer.valueOf(this.vista.jtfNroNotaCredito.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_NOTA_CREDITO_MSG_2, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (nroNotaCredito < 0) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_NOTA_CREDITO_MSG_3, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!validadTotal()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_NOTA_CREDITO_MSG_5, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!validadMontoExcedido()) {
            int saldoPendiente = modelo.getFacturaSinPago().getSaldo();
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_NOTA_CREDITO_MSG_6 + " (" + decimalFormat.format(saldoPendiente) + ")", VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (modelo.existeNotaCredito(nroNotaCredito)) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_NOTA_CREDITO_MSG_4, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCantidadItems() {
        if (modelo.getNotaCreditoDetalleTm().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_CANT_DETALLE_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validadTotal() {
        return modelo.getTotal() > 0;
    }

    private boolean validadMontoExcedido() {
        if (modelo.getTotal() > modelo.getFacturaSinPago().getSaldo()) {
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        this.modelo.limpiarCampos();
        this.vista.jtfCliente.setText("");
        this.vista.jtfNroNotaCredito.setText("");
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaotaCredito.setDate(calendar.getTime());
    }

    private void invocarVistaSeleccionFactura() {
        if (!validarCliente()) {
            return;
        }
        SeleccionarVenta sc = new SeleccionarVenta(this.vista);
        sc.setCallback(this);
        sc.setCliente(modelo.getCabecera().getCliente());
        sc.mostrarVista();
    }

    private void invocarVistaSeleccionCliente() {
        if (!validarDetalleReciboVacio()) {
            return;
        }
        Seleccionar_cliente sc = new Seleccionar_cliente(inicio.vista);
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
            invocarVistaSeleccionFactura();
        } else if (source.equals(this.vista.jbCliente)) {
            invocarVistaSeleccionCliente();
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
        if (e.getSource().equals(this.vista.jtNotaCreditoDetalle)) {
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
                invocarVistaSeleccionCliente();
                break;
            }
            case KeyEvent.VK_F4: {
                invocarVistaSeleccionFactura();
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
    public void recibirCliente(M_cliente cliente) {
        modelo.getCabecera().setCliente(cliente);
        this.vista.jtfCliente.setText(cliente.getEntidad() + "(" + cliente.getRuc() + "-" + cliente.getRucId() + ")");
    }

    @Override
    public void recibirProducto(double cantidad, int precio, double descuento, M_producto producto, String observacion) {
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, int precio, double descuento, M_producto producto, String observacion) {
        E_NotaCreditoDetalle nd = new E_NotaCreditoDetalle();
        nd.setCantidad(cantidad);
        nd.setDescuento(descuento);
        nd.setObservacion(observacion);
        nd.setPrecio(precio);
        nd.setProducto(producto);
        if (modelo.cantidadNuevaMayorAActual(posicion, nd)) {
            JOptionPane.showMessageDialog(vista, "La cantidad ingresada no puede ser mayor a la disponible", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.modelo.modificarDetalle(posicion, nd);
        sumarTotal();
    }

    @Override
    public void recibirVentaPendientePago(E_facturaSinPago facturaCabecera, List<E_facturaDetalle> facturaDetalle) {
        this.modelo.establecerFacturaCabecera(facturaCabecera.getNroFactura());
        this.modelo.setFacturaSinPago(facturaCabecera);
        List<E_NotaCreditoDetalle> notaCreditoDetalle = new ArrayList<>();
        for (E_facturaDetalle fade : facturaDetalle) {
            //BUSCAR NOTAS DE CREDITO ANTERIORES PARA CONTROLAR LA CANTIDAD DEL DETALLE DE VENTA
            E_NotaCreditoDetalle aux = modelo.obtenerNotaCreditoDetalle(fade.getIdFacturaDetalle());
            if (aux != null) {
                double cantActual = fade.getCantidad() - aux.getCantidad();
                fade.setCantidad(cantActual);
                fade.getProducto().setPrecioVenta(fade.getPrecio());
                E_NotaCreditoDetalle nd = new E_NotaCreditoDetalle();
                nd.setCantidad(cantActual);
                nd.setDescuento(fade.getDescuento());
                nd.setFacturaDetalle(fade);
                nd.setObservacion(fade.getObservacion());
                nd.setPrecio(fade.getPrecio());
                nd.setProducto(fade.getProducto());
                notaCreditoDetalle.add(nd);
            } else {
                fade.getProducto().setPrecioVenta(fade.getPrecio());
                E_NotaCreditoDetalle nd = new E_NotaCreditoDetalle();
                nd.setCantidad(fade.getCantidad());
                nd.setDescuento(fade.getDescuento());
                nd.setFacturaDetalle(fade);
                nd.setObservacion(fade.getObservacion());
                nd.setPrecio(fade.getPrecio());
                nd.setProducto(fade.getProducto());
                notaCreditoDetalle.add(nd);
            }
        }
        this.modelo.getNotaCreditoDetalleTm().setList(notaCreditoDetalle);
        this.modelo.getDetalles().clear();
        this.modelo.getDetalles().addAll(facturaDetalle);
        sumarTotal();
    }

    @Override
    public void recibirFacturaCabeceraPendientePago(E_facturaSinPago facturaCabecera) {
    }

    @Override
    public void recibirFacturaDetallePendientePago(List<E_facturaDetalle> facturaDetalle) {
    }
}
