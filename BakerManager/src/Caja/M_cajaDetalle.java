/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import DB.DB_Cobro;
import DB.DB_Egreso;
import DB.DB_Ingreso;
import DB.DB_Pago;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaCabecera;
import Entities.E_reciboPagoCabecera;
import Entities.E_reciboPagoDetalle;
import Entities.M_egresoCabecera;
import Entities.M_funcionario;
import Interface.MovimientosCaja;
import ModeloTabla.SeleccionCobroCabecera;
import ModeloTabla.SeleccionCobroCabeceraTableModel;
import ModeloTabla.SeleccionCompraCabecera;
import ModeloTabla.SeleccionCompraCabeceraTableModel;
import ModeloTabla.SeleccionPagoCabecera;
import ModeloTabla.SeleccionPagoCabeceraTableModel;
import ModeloTabla.SeleccionVentaCabecera;
import ModeloTabla.SeleccionVentaCabeceraTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_cajaDetalle {

    private Date fechaInicial, fechaFinal;
    private M_funcionario funcionario;
    private SeleccionVentaCabeceraTableModel movVentasTM;
    private SeleccionCompraCabeceraTableModel movCompraTM;
    private SeleccionCobroCabeceraTableModel movCobroTM;
    private SeleccionPagoCabeceraTableModel movPagoTM;
    private MovimientosCaja movimientosCaja;//para ver datos
    private boolean editingMode;

    public M_cajaDetalle() {
        this.editingMode = false;
        this.funcionario = new M_funcionario();
        this.funcionario.setId(-1);
        this.movVentasTM = new SeleccionVentaCabeceraTableModel();
        this.movCompraTM = new SeleccionCompraCabeceraTableModel();
        this.movCobroTM = new SeleccionCobroCabeceraTableModel();
        this.movPagoTM = new SeleccionPagoCabeceraTableModel();
    }

    private void initializeTableModels() {
        obtenerVentasCabecera(-1, -1, -1, fechaInicial, fechaFinal);
        obtenerComprasCabecera(-1, -1, -1, fechaInicial, fechaFinal);
        obtenerCobrosCabecera(-1, -1, fechaInicial, fechaFinal);
        obtenerPagosCabecera(-1, -1, fechaInicial, fechaFinal);
    }

    public void buscarMovimientos(int idFuncionario, Date fechaInicio, Date fechaFin) {
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(fechaFin);
        calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
        calendarFinal.set(Calendar.MINUTE, 59);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);
        obtenerVentasCabecera(idFuncionario, -1, -1, calendarInicio.getTime(), calendarInicio.getTime());
        obtenerComprasCabecera(idFuncionario, -1, -1, calendarInicio.getTime(), calendarInicio.getTime());
        obtenerCobrosCabecera(idFuncionario, -1, calendarInicio.getTime(), calendarInicio.getTime());
        obtenerPagosCabecera(idFuncionario, -1, calendarInicio.getTime(), calendarInicio.getTime());

    }

    public MovimientosCaja getMovimientosCaja() {
        return movimientosCaja;
    }

    public void setMovimientosCaja(MovimientosCaja movimientosCaja) {
        this.movimientosCaja = movimientosCaja;
        ArrayList<SeleccionVentaCabecera> listaVentas = new ArrayList<>();
        List<E_facturaCabecera> ventas = this.movimientosCaja.getMovimientoVentas();
        for (E_facturaCabecera unaVenta : ventas) {
            listaVentas.add(new SeleccionVentaCabecera(unaVenta, true));
        }
        this.getMovVentasTM().setList(listaVentas);
        ArrayList<SeleccionCompraCabecera> listaCompras = new ArrayList<>();
        List<M_egresoCabecera> compras = this.movimientosCaja.getMovimientoCompras();
        for (M_egresoCabecera egresoCabecera : compras) {
            listaCompras.add(new SeleccionCompraCabecera(egresoCabecera, true));
        }
        this.getMovComprasTM().setList(listaCompras);
        ArrayList<SeleccionCobroCabecera> listaCobros = new ArrayList<>();
        List<E_cuentaCorrienteCabecera> cobros = this.movimientosCaja.getMovimientoCobros();
        for (E_cuentaCorrienteCabecera unCobro : cobros) {
            listaCobros.add(new SeleccionCobroCabecera(unCobro, true));
        }
        this.getMovCobroTM().setList(listaCobros);
        ArrayList<SeleccionPagoCabecera> listaPagos = new ArrayList<>();
        List<E_reciboPagoCabecera> pagos = this.movimientosCaja.getMovimientoPagos();
        for (E_reciboPagoCabecera unPago : pagos) {
            listaPagos.add(new SeleccionPagoCabecera(unPago, true));
        }
        this.getMovPagoTM().setList(listaPagos);
    }

    public SeleccionVentaCabeceraTableModel getMovVentasTM() {
        return this.movVentasTM;
    }

    public SeleccionCompraCabeceraTableModel getMovComprasTM() {
        return this.movCompraTM;
    }

    public SeleccionCobroCabeceraTableModel getMovCobroTM() {
        return movCobroTM;
    }

    public SeleccionPagoCabeceraTableModel getMovPagoTM() {
        return movPagoTM;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public String obtenerNombreFuncionario() {
        return getFuncionario().getNombre() + " " + getFuncionario().getApellido();
    }

    public void borrarDatos() {
        this.funcionario.setAlias("");
        this.funcionario.setApellido("");
        this.funcionario.setEmail("");
        this.funcionario.setId(-1);
        this.funcionario.setCedula(-1);
    }

    public void obtenerVentasCabecera(int idFuncionario, int idCliente, int idTipoOperacion, Date fechaInicio, Date fechaFin) {
        ArrayList<SeleccionVentaCabecera> lista = new ArrayList<>();
        List<E_facturaCabecera> list = DB_Ingreso.obtenerMovimientoVentasCabeceras(idFuncionario, idCliente, fechaInicio, fechaFin, idTipoOperacion);
        for (E_facturaCabecera ventaCabecera : list) {
            lista.add(new SeleccionVentaCabecera(ventaCabecera, true));
        }
        this.getMovVentasTM().setList(lista);
    }

    public void obtenerComprasCabecera(int idFuncionario, int idProveedor, int idTipoOperacion, Date fechaInicio, Date fechaFin) {
        ArrayList<SeleccionCompraCabecera> lista = new ArrayList<>();
        List<M_egresoCabecera> list = DB_Egreso.obtenerMovimientoComprasCabeceras(idFuncionario, idProveedor, idTipoOperacion, fechaInicio, fechaFin);
        for (M_egresoCabecera egresoCabecera : list) {
            lista.add(new SeleccionCompraCabecera(egresoCabecera, true));
        }
        this.getMovComprasTM().setList(lista);
    }

    public void obtenerCobrosCabecera(int idFuncionario, int idCliente, Date fechaInicio, Date fechaFin) {
        ArrayList<SeleccionCobroCabecera> lista = new ArrayList<>();
        List<E_cuentaCorrienteCabecera> list = DB_Cobro.obtenerMovimientoCobrosCabeceras(idFuncionario, idCliente, fechaInicio, fechaFin);
        for (E_cuentaCorrienteCabecera egresoCabecera : list) {
            lista.add(new SeleccionCobroCabecera(egresoCabecera, true));
        }
        this.getMovCobroTM().setList(lista);
    }

    public List<E_cuentaCorrienteDetalle> obtenerDetalleCobro(int idReciboCobro) {
        return DB_Cobro.obtenerCobroDetalle(idReciboCobro);
    }

    public void obtenerPagosCabecera(int idFuncionario, int idProveedor, Date fechaInicio, Date fechaFin) {
        ArrayList<SeleccionPagoCabecera> lista = new ArrayList<>();
        List<E_reciboPagoCabecera> list = DB_Pago.obtenerMovimientoPagosCabeceras(idFuncionario, idProveedor, fechaInicio, fechaFin);
        for (E_reciboPagoCabecera reciboPagoCabecera : list) {
            lista.add(new SeleccionPagoCabecera(reciboPagoCabecera, true));
        }
        this.getMovPagoTM().setList(lista);
    }

    public List<E_reciboPagoDetalle> obtenerDetallePago(int idReciboPago) {
        return DB_Pago.obtenerPagoDetalle(idReciboPago);
    }

    public void setRangoTiempo(Date tiempoInicio, Date tiempoFinal) {
        this.fechaInicial = tiempoInicio;
        this.fechaFinal = tiempoFinal;
        initializeTableModels();
    }

    /**
     * @return the editingMode
     */
    public boolean isEditingMode() {
        return editingMode;
    }

    /**
     * @param editingMode the editingMode to set
     */
    public void setEditingMode(boolean editingMode) {
        this.editingMode = editingMode;
    }
}
