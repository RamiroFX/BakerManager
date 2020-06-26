/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import DB.DB_Caja;
import DB.DB_Cobro;
import DB.DB_Egreso;
import DB.DB_Ingreso;
import DB.DB_Pago;
import Entities.ArqueoCajaDetalle;
import Entities.Caja;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaCabecera;
import Entities.E_formaPago;
import Entities.E_reciboPagoCabecera;
import Entities.E_reciboPagoDetalle;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_egreso_cabecera;
import Excel.ExportarCaja;
import Impresora.Impresora;
import Interface.MovimientosCaja;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerCaja implements ActionListener, KeyListener {

    private static final String TT_EFECTIVO_RENDIR = "Efectivo a rendir: suma de ventas y cobros en efectivo menos compras y pagos en efectivo";
    private ArqueoCajaTableModel tbmFondoApertura, tbmFondoCierre, tbmDepositar;
    //LOGIC VARIABLES
    private ArrayList<ArqueoCajaDetalle> acdApertura;
    private ArrayList<ArqueoCajaDetalle> acdCierre;
    private ArrayList<ArqueoCajaDetalle> acdDepositar;
    private Caja caja;
    private V_SaldarCaja vista;
    private MovimientosCaja movimientosCaja;

    public VerCaja(C_inicio inicio, int idCaja) {
        this.vista = new V_SaldarCaja(inicio);
        initializeVariables();
        addListeners();
        initializeLogic(idCaja);
    }

    private void initializeVariables() {
        this.vista.printButton.setVisible(true);
        this.vista.jbExportar.setVisible(true);
        this.vista.saveButton.setVisible(false);
        this.vista.jbFondoAnterior.setVisible(false);
        this.vista.setTitle("Ver caja");
        //ARQUEO CAJA 
        tbmFondoApertura = new ArqueoCajaTableModel();
        tbmFondoCierre = new ArqueoCajaTableModel();
        tbmDepositar = new ArqueoCajaTableModel();
        vista.jtFondoCierre.setModel(tbmFondoCierre);
        vista.jtFondoApertura.setModel(tbmFondoApertura);
        vista.jtDepositar.setModel(tbmDepositar);
        this.vista.jftEfectivoRendir.setToolTipText(TT_EFECTIVO_RENDIR);
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void addListeners() {
        this.vista.jbExportar.addActionListener(this);
        this.vista.printButton.addActionListener(this);
        this.vista.jbDetalle.addActionListener(this);
        this.vista.cancelButton.addActionListener(this);
        this.vista.jtFondoApertura.addKeyListener(this);
    }

    private void initializeLogic(int idCaja) {
        movimientosCaja = new MovimientosCaja();
        acdApertura = DB_Caja.obtenerArqueoCaja(idCaja, 1);
        acdCierre = DB_Caja.obtenerArqueoCaja(idCaja, 2);
        acdDepositar = DB_Caja.obtenerArqueoCaja(idCaja, 3);
        caja = DB_Caja.obtenerCaja(idCaja);

        this.tbmFondoApertura.setArqueoCajaList(acdApertura);
        this.tbmFondoApertura.updateTable();
        Utilities.c_packColumn.packColumns(this.vista.jtFondoApertura, 1);

        this.tbmFondoCierre.setArqueoCajaList(acdCierre);
        this.tbmFondoCierre.updateTable();
        Utilities.c_packColumn.packColumns(this.vista.jtFondoCierre, 1);

        this.tbmDepositar.setArqueoCajaList(acdDepositar);
        this.tbmDepositar.updateTable();
        Utilities.c_packColumn.packColumns(this.vista.jtDepositar, 1);

        this.vista.jddInicio.setDate(caja.getTiempoApertura());
        this.vista.jddInicio.setEnabled(false);
        this.vista.jddFinal.setDate(caja.getTiempoCierre());
        this.vista.jddFinal.setEnabled(false);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(caja.getTiempoCierre());
        Date currentTime = calendar.getTime();
        String horaFin = sdf.format(currentTime).substring(0, 2);
        int horasFin = Integer.valueOf(horaFin);
        if (horasFin >= 0 && horasFin < 10) {
            this.vista.jcbHoraFin.setSelectedItem("" + horasFin);
        } else {
            this.vista.jcbHoraFin.setSelectedItem("" + horasFin);
        }
        int minutoFin = calendar.get(Calendar.MINUTE);
        if (minutoFin < 10) {
            this.vista.jcbMinutoFin.setSelectedItem("0" + minutoFin);
        } else {
            this.vista.jcbMinutoFin.setSelectedItem("" + minutoFin);
        }
        calendar.setTime(caja.getTiempoApertura());
        currentTime = calendar.getTime();
        String horaInicio = sdf.format(currentTime).substring(0, 2);
        int horasInicio = Integer.valueOf(horaInicio);
        if (horasInicio >= 0 && horasFin < 10) {
            this.vista.jcbHoraInicio.setSelectedItem("" + horasInicio);
        } else {
            this.vista.jcbHoraInicio.setSelectedItem("" + horasInicio);
        }
        int minutoInicio = calendar.get(Calendar.MINUTE);
        if (minutoInicio < 10) {
            this.vista.jcbMinutoInicio.setSelectedItem("0" + minutoInicio);
        } else {
            this.vista.jcbMinutoInicio.setSelectedItem("" + minutoInicio);
        }
        this.vista.jcbHoraInicio.setEnabled(false);
        this.vista.jcbMinutoInicio.setEnabled(false);
        this.vista.jcbHoraFin.setEnabled(false);
        this.vista.jcbMinutoFin.setEnabled(false);
        sumarFondoCierre();
        sumarFondoApertura();
        arqueoDepositar();
        actualizarMovimientos();
    }

    private void actualizarMovimientos() {
        movimientosCaja.setMovimientoVentas((ArrayList<E_facturaCabecera>) DB_Ingreso.obtenerMovimientoVentasCabeceras(this.caja.getIdCaja()));
        movimientosCaja.setMovimientoCompras((ArrayList<M_egreso_cabecera>) DB_Egreso.obtenerMovimientoComprasCabeceras(this.caja.getIdCaja()));
        movimientosCaja.setMovimientoCobros((ArrayList<E_cuentaCorrienteCabecera>) DB_Cobro.obtenerMovimientoCobrosCabeceras(this.caja.getIdCaja()));
        movimientosCaja.setMovimientoPagos((ArrayList<E_reciboPagoCabecera>) DB_Pago.obtenerMovimientoPagosCabeceras(this.caja.getIdCaja()));
        int totalVenta = 0, totalVentaContado = 0, totalVentaCredito = 0;
        int totalPago = 0, totalPagoEfectivo = 0, totalPagoCheque = 0;
        int totalCompra = 0, totalCompraContado = 0, totalCompraCredito = 0;
        int totalCobro = 0, totalCobroEfectivo = 0, totalCobroCheque = 0;
        /*
        MOVIMIENTO DE COBROS
         */
        for (E_cuentaCorrienteCabecera cobro : movimientosCaja.getMovimientoCobros()) {
            totalCobro = totalCobro + cobro.getDebito();
            for (E_cuentaCorrienteDetalle e_cuentaCorrienteDetalle : DB_Cobro.obtenerCobroDetalle(cobro.getId())) {
                switch (e_cuentaCorrienteDetalle.calcularFormaPago().getId()) {
                    case E_formaPago.EFECTIVO: {
                        totalCobroEfectivo = (int) (totalCobroEfectivo + e_cuentaCorrienteDetalle.getMonto());
                        break;
                    }
                    case E_formaPago.CHEQUE: {
                        totalCobroCheque = (int) (totalCobroCheque + e_cuentaCorrienteDetalle.getMonto());
                        break;
                    }
                }

            }
        }
        /*
        MOVIMIENTO DE PAGOS        
         */
        for (E_reciboPagoCabecera pago : movimientosCaja.getMovimientoPagos()) {
            totalPago = totalPago + pago.getMonto();
            for (E_reciboPagoDetalle reciboPagoDetalle : DB_Pago.obtenerPagoDetalle(pago.getId())) {
                switch (reciboPagoDetalle.calcularFormaPago().getId()) {
                    case E_formaPago.EFECTIVO: {
                        totalPagoEfectivo = (int) (totalPagoEfectivo + reciboPagoDetalle.getMonto());
                        break;
                    }
                    case E_formaPago.CHEQUE: {
                        totalPagoCheque = (int) (totalPagoCheque + reciboPagoDetalle.getMonto());
                        break;
                    }
                }

            }
        }
        /*
        MOVIMIENTO DE COMPRAS
         */
        for (M_egreso_cabecera compra : movimientosCaja.getMovimientoCompras()) {
            totalCompra = totalCompra + compra.getTotal();
            switch (compra.getId_condVenta()) {
                case E_tipoOperacion.CONTADO: {
                    totalCompraContado = totalCompraContado + compra.getTotal();
                    break;
                }
                case E_tipoOperacion.CREDITO_30: {
                    totalCompraCredito = totalCompraCredito + compra.getTotal();
                    break;
                }
            }
        }
        /*
        MOVIMIENTO DE VENTAS
         */
        for (E_facturaCabecera venta : movimientosCaja.getMovimientoVentas()) {
            totalVenta = totalVenta + venta.getTotal();
            switch (venta.getTipoOperacion().getId()) {
                case E_tipoOperacion.CONTADO: {
                    totalVentaContado = totalVentaContado + venta.getTotal();
                    break;
                }
                case E_tipoOperacion.CREDITO_30: {
                    totalVentaCredito = totalVentaCredito + venta.getTotal();
                    break;
                }
            }
        }
        //COBROS
        this.vista.jtfTotalCobrado.setValue(totalCobro);
        this.vista.jtfTotalCobradoEfectivo.setValue(totalCobroEfectivo);
        this.vista.jtfTotalCobradoCheque.setValue(totalCobroCheque);
        //PAGOS
        this.vista.jtfTotalPagado.setValue(totalPago);
        this.vista.jtfTotalPagadoCheque.setValue(totalPagoCheque);
        this.vista.jtfTotalPagadoEfectivo.setValue(totalPagoEfectivo);
        //VENTAS
        this.vista.jtfIngresoTotal.setValue(totalVenta);
        this.vista.jtfIngresoContado.setValue(totalVentaContado);
        this.vista.jtfIngresoCredito.setValue(totalVentaCredito);
        //COMPRAS
        this.vista.jtfEgresoTotal.setValue(totalCompra);
        this.vista.jtfEgresoContado.setValue(totalCompraContado);
        this.vista.jtfEgresoCredito.setValue(totalCompraCredito);
        Integer aDepositar = totalCobroEfectivo + totalVentaContado - totalCompraContado - totalPagoEfectivo;
        this.vista.jftEfectivoRendir.setValue(aDepositar);
    }

    private void imprimirCaja() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Desea imprimir la caja?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    int depositado = 0;
                    for (ArqueoCajaDetalle arquDeta : tbmDepositar.arqueoCajaDetalleList) {
                        depositado = depositado + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
                    }
                    //se agrego la cantidad de efectivo depositado como ultimo
                    Impresora.imprimirTicketCaja(caja, depositado);
                }
            }
        });
    }

    private void cerrar() {
        this.vista.dispose();
    }

    private void sumarFondoApertura() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmFondoApertura.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.vista.jtfFondoApertura.setValue(total);
        calcularDiferenciaCaja();
    }

    private void sumarFondoCierre() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmFondoCierre.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.vista.jtfFondoCierre.setValue(total);
        calcularDiferenciaCaja();
    }

    private void calcularDiferenciaCaja() {
        int fondoApertura = 0;
        int fondoCierre = 0;
        if (null != this.vista.jtfFondoApertura.getValue()) {
            try {
                fondoApertura = Integer.valueOf(String.valueOf(this.vista.jtfFondoApertura.getValue()));
            } catch (Exception e) {
                fondoApertura = 0;
            }
        }
        if (null != this.vista.jtfFondoCierre.getValue()) {
            try {
                fondoCierre = Integer.valueOf(String.valueOf(this.vista.jtfFondoCierre.getValue()));
            } catch (Exception e) {
                fondoCierre = 0;
            }
        }
    }

    private void arqueoDepositar() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmDepositar.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.vista.jtfDepositar.setValue(total);
    }

    private void verDetalle() {
        CajaDetalle cd = new CajaDetalle(vista, movimientosCaja);
        cd.mostrarVista();
    }

    private void exportarCaja() {
        ExportarCaja ec = new ExportarCaja();
        ec.exportarCajaMovimientos(caja, movimientosCaja);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == this.vista.cancelButton) {
            cerrar();
        } else if (src.equals(this.vista.printButton)) {
            imprimirCaja();
        } else if (src.equals(this.vista.jbDetalle)) {
            verDetalle();
        } else if (src.equals(this.vista.jbExportar)) {
            exportarCaja();
        }
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
}
