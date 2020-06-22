/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import Empleado.Seleccionar_funcionario;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_formaPago;
import Entities.E_reciboPagoDetalle;
import Entities.E_tipoOperacion;
import Entities.M_funcionario;
import Interface.GestionInterface;
import Interface.InterfaceSeleccionCobroCabecera;
import Interface.InterfaceSeleccionCompraCabecera;
import Interface.InterfaceSeleccionPagoCabecera;
import Interface.InterfaceSeleccionVentaCabecera;
import Interface.RecibirEmpleadoCallback;
import ModeloTabla.SeleccionCobroCabecera;
import ModeloTabla.SeleccionCompraCabecera;
import ModeloTabla.SeleccionPagoCabecera;
import ModeloTabla.SeleccionVentaCabecera;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_cajaDetalle implements GestionInterface, RecibirEmpleadoCallback, InterfaceSeleccionVentaCabecera,
        InterfaceSeleccionCompraCabecera, InterfaceSeleccionCobroCabecera, InterfaceSeleccionPagoCabecera {

    V_cajaDetalle vista;
    M_cajaDetalle modelo;

    public C_cajaDetalle(V_cajaDetalle vista, M_cajaDetalle modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.modelo.getMovVentasTM().setInterface(this);
        this.modelo.getMovComprasTM().setInterface(this);
        this.modelo.getMovCobroTM().setInterface(this);
        this.modelo.getMovPagoTM().setInterface(this);
        this.vista.setLocationRelativeTo(null);
        callMethods();
    }

    private void callMethods() {
        inicializarVista();
        concederPermisos();
        actualizarSumaVentas();
        actualizarSumaCompras();
        actualizarSumaCobros();
        actualizarSumaPagos();
    }

    @Override
    public void inicializarVista() {
        this.vista.jtVentas.setModel(modelo.getMovVentasTM());
        this.vista.jtCompras.setModel(modelo.getMovComprasTM());
        this.vista.jtCobros.setModel(modelo.getMovCobroTM());
        this.vista.jtPagos.setModel(modelo.getMovPagoTM());
        Utilities.c_packColumn.packColumns(this.vista.jtVentas, 1);
        Utilities.c_packColumn.packColumns(this.vista.jtCompras, 1);
        Utilities.c_packColumn.packColumns(this.vista.jtCobros, 1);
        Utilities.c_packColumn.packColumns(this.vista.jtPagos, 1);
        Date date = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(date);
        this.vista.jddFinal.setDate(date);
    }

    @Override
    public void concederPermisos() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jbBuscar.setEnabled(true);
        this.vista.jddFinal.setEnabled(true);
        this.vista.jddInicio.setEnabled(true);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        //this.vista.jtCobros.addMouseListener(this);
        //this.vista.jtCobros.addKeyListener(this);
        this.vista.jbAgregarVentas.addActionListener(this);
        this.vista.jbQuitarVentas.addActionListener(this);
        this.vista.jbAgregarCompras.addActionListener(this);
        this.vista.jbQuitarCompras.addActionListener(this);
        this.vista.jbAgregarCobro.addActionListener(this);
        this.vista.jbQuitarCobro.addActionListener(this);
        this.vista.jbAgregarPago.addActionListener(this);
        this.vista.jbQuitarPago.addActionListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbCancelar.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbAgregarVentas.addKeyListener(this);
        this.vista.jbQuitarVentas.addKeyListener(this);
        this.vista.jbAgregarCompras.addKeyListener(this);
        this.vista.jbQuitarCompras.addKeyListener(this);
        this.vista.jbAgregarCobro.addKeyListener(this);
        this.vista.jbQuitarCobro.addKeyListener(this);
        this.vista.jbAgregarPago.addKeyListener(this);
        this.vista.jbQuitarPago.addKeyListener(this);
    }

    @Override
    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    @Override
    public void cerrar() {
        this.vista.dispose();
    }

    private void consultarCajas() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Date inicio = vista.jddInicio.getDate();
                Date fin = vista.jddFinal.getDate();
                if (validarFechas(inicio, fin)) {
                    Calendar calendarioInicio = Calendar.getInstance();
                    calendarioInicio.setTime(inicio);
                    calendarioInicio.set(Calendar.HOUR_OF_DAY, 0);
                    calendarioInicio.set(Calendar.MINUTE, 0);
                    Calendar calendarioFin = Calendar.getInstance();
                    calendarioFin.setTime(fin);
                    calendarioFin.set(Calendar.HOUR_OF_DAY, 23);
                    calendarioFin.set(Calendar.MINUTE, 59);
                    Timestamp ini = new Timestamp(calendarioInicio.getTimeInMillis());
                    Timestamp fi = new Timestamp(calendarioFin.getTimeInMillis());
                    int idFuncionario = -1;
                    if (modelo.getFuncionario() != null && modelo.getFuncionario().getId_funcionario() != null) {
                        idFuncionario = modelo.getFuncionario().getId_funcionario();
                    }
                    //vista.jtCaja.setModel(modelo.consultarCajas(idFuncionario, ini, fi));
                    //Utilities.c_packColumn.packColumns(vista.jtCaja, 1);
                    //vista.jbDetalle.setEnabled(false);
                } else {
                    vista.jddFinal.setDate(vista.jddInicio.getDate());
                    vista.jddFinal.updateUI();
                    JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.setFuncionario(funcionario);
        this.vista.jtfEmpleado.setText(this.modelo.obtenerNombreFuncionario());
    }

    private void borrarDatos() {
        Date date = Calendar.getInstance().getTime();
        this.modelo.borrarDatos();
        this.vista.jtfEmpleado.setText("");
        this.vista.jddInicio.setDate(date);
        this.vista.jddFinal.setDate(date);
    }

    private boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            }
        }
        return false;
    }

    private void actualizarSumaVentas() {
        int totalContado = 0, totalCredito = 0;
        for (SeleccionVentaCabecera seleccionVentaCabecera : modelo.getMovVentasTM().getList()) {
            if (seleccionVentaCabecera.isEstaSeleccionado()) {
                switch (seleccionVentaCabecera.getFacturaCabecera().getTipoOperacion().getId()) {
                    case E_tipoOperacion.CONTADO: {
                        totalContado = totalContado + seleccionVentaCabecera.getFacturaCabecera().getTotal();
                        break;
                    }
                    case E_tipoOperacion.CREDITO_30: {
                        totalCredito = totalCredito + seleccionVentaCabecera.getFacturaCabecera().getTotal();
                        break;
                    }
                }
            }
        }
        this.vista.jftTotalVentaContado.setValue(totalContado);
        this.vista.jftTotalVentaCredito.setValue(totalCredito);
        this.vista.jftTotalVenta.setValue(totalContado + totalCredito);
    }

    private void agregarTodoVentas() {
        int rows = this.vista.jtVentas.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.vista.jtVentas.getModel().setValueAt(true, i, 7);
        }
        actualizarSumaVentas();
    }

    private void quitarTodoVentas() {
        int rows = this.vista.jtVentas.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.vista.jtVentas.getModel().setValueAt(false, i, 7);
        }
        actualizarSumaVentas();
    }

    private void actualizarSumaCompras() {
        int totalContado = 0, totalCredito = 0;
        for (SeleccionCompraCabecera seleccionCompraCabecera : modelo.getMovComprasTM().getList()) {
            if (seleccionCompraCabecera.isEstaSeleccionado()) {
                switch (seleccionCompraCabecera.getFacturaCabecera().getId_condVenta()) {
                    case E_tipoOperacion.CONTADO: {
                        totalContado = totalContado + seleccionCompraCabecera.getFacturaCabecera().getTotal();
                        break;
                    }
                    case E_tipoOperacion.CREDITO_30: {
                        totalCredito = totalCredito + seleccionCompraCabecera.getFacturaCabecera().getTotal();
                        break;
                    }
                }
            }
        }
        this.vista.jftTotalCompraContado.setValue(totalContado);
        this.vista.jftTotalCompraCredito.setValue(totalCredito);
        this.vista.jftTotalCompra.setValue(totalContado + totalCredito);
    }

    private void agregarTodoCompras() {
        int rows = this.vista.jtCompras.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.vista.jtCompras.getModel().setValueAt(true, i, 7);
        }
        actualizarSumaCompras();
    }

    private void quitarTodoCompras() {
        int rows = this.vista.jtCompras.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.vista.jtCompras.getModel().setValueAt(false, i, 7);
        }
        actualizarSumaCompras();
    }

    private void actualizarSumaCobros() {
        int totalEfectivo = 0, totalCheque = 0;
        for (SeleccionCobroCabecera seleccionCobroCabecera : modelo.getMovCobroTM().getList()) {
            if (seleccionCobroCabecera.isSeleccionado()) {
                for (E_cuentaCorrienteDetalle e_cuentaCorrienteDetalle : modelo.obtenerDetalleCobro(seleccionCobroCabecera.getCobro().getId())) {
                    switch (e_cuentaCorrienteDetalle.calcularFormaPago().getId()) {
                        case E_formaPago.EFECTIVO: {
                            totalEfectivo = (int) (totalEfectivo + e_cuentaCorrienteDetalle.getMonto());
                            break;
                        }
                        case E_formaPago.CHEQUE: {
                            totalCheque = (int) (totalCheque + e_cuentaCorrienteDetalle.getMonto());
                            break;
                        }
                    }

                }
            }
        }
        this.vista.jftTotalCobroEfectivo.setValue(totalEfectivo);
        this.vista.jftTotalCobroCheque.setValue(totalCheque);
        this.vista.jftTotalCobro.setValue(totalEfectivo + totalCheque);
    }

    private void agregarTodoCobros() {
        int rows = this.vista.jtCobros.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.vista.jtCobros.getModel().setValueAt(true, i, 6);
        }
        actualizarSumaCobros();
    }

    private void quitarTodoCobros() {
        int rows = this.vista.jtCobros.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.vista.jtCobros.getModel().setValueAt(false, i, 6);
        }
        actualizarSumaCobros();
    }

    private void actualizarSumaPagos() {
        int totalEfectivo = 0, totalCheque = 0;
        for (SeleccionPagoCabecera seleccionPagoCabecera : modelo.getMovPagoTM().getList()) {
            if (seleccionPagoCabecera.isSeleccionado()) {
                for (E_reciboPagoDetalle reciboPagoDetalle : modelo.obtenerDetallePago(seleccionPagoCabecera.getPago().getId())) {
                    switch (reciboPagoDetalle.calcularFormaPago().getId()) {
                        case E_formaPago.EFECTIVO: {
                            totalEfectivo = (int) (totalEfectivo + reciboPagoDetalle.getMonto());
                            break;
                        }
                        case E_formaPago.CHEQUE: {
                            totalCheque = (int) (totalCheque + reciboPagoDetalle.getMonto());
                            break;
                        }
                    }

                }
            }
        }
        this.vista.jftTotalPagoEfectivo.setValue(totalEfectivo);
        this.vista.jftTotalPagoCheque.setValue(totalCheque);
        this.vista.jftTotalPago.setValue(totalEfectivo + totalCheque);
    }

    private void agregarTodoPagos() {
        int rows = this.vista.jtPagos.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.vista.jtPagos.getModel().setValueAt(true, i, 6);
        }
        actualizarSumaPagos();
    }

    private void quitarTodoPagos() {
        int rows = this.vista.jtPagos.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.vista.jtPagos.getModel().setValueAt(false, i, 6);
        }
        actualizarSumaPagos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbAceptar)) {
        } else if (src.equals(this.vista.jbBuscar)) {
            consultarCajas();
        } else if (src.equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (src.equals(this.vista.jbBorrar)) {
            borrarDatos();
        } else if (src.equals(this.vista.jbAgregarVentas)) {
            agregarTodoVentas();
        } else if (src.equals(this.vista.jbQuitarVentas)) {
            quitarTodoVentas();
        } else if (src.equals(this.vista.jbAgregarCompras)) {
            agregarTodoCompras();
        } else if (src.equals(this.vista.jbQuitarCompras)) {
            quitarTodoCompras();
        } else if (src.equals(this.vista.jbAgregarCobro)) {
            agregarTodoCobros();
        } else if (src.equals(this.vista.jbQuitarCobro)) {
            quitarTodoCobros();
        } else if (src.equals(this.vista.jbAgregarPago)) {
            agregarTodoPagos();
        } else if (src.equals(this.vista.jbQuitarPago)) {
            quitarTodoPagos();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
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
    public void notificarCambioSeleccion() {
        actualizarSumaVentas();
    }

    @Override
    public void notificarCambioSeleccionCompraCabecera() {
        actualizarSumaCompras();
    }

    @Override
    public void notificarCambioSeleccionCobroCabecera() {
        actualizarSumaCobros();
    }

    @Override
    public void notificarCambioSeleccionPagoCabecera() {
        actualizarSumaPagos();
    }

}
