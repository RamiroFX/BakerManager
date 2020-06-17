/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import DB.DB_Egreso;
import DB.DB_Ingreso;
import Entities.E_facturaCabeceraFX;
import Entities.M_egreso_cabecera;
import Entities.M_funcionario;
import ModeloTabla.SeleccionCompraCabecera;
import ModeloTabla.SeleccionCompraCabeceraTableModel;
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

    private M_funcionario funcionario;
    private SeleccionVentaCabeceraTableModel movVentasTM;
    private SeleccionCompraCabeceraTableModel movCompraTM;

    public M_cajaDetalle() {
        this.funcionario = new M_funcionario();
        this.movVentasTM = new SeleccionVentaCabeceraTableModel();
        this.movCompraTM = new SeleccionCompraCabeceraTableModel();
        Calendar calendarInicio = Calendar.getInstance();
        obtenerVentasCabecera(-1, -1, -1, calendarInicio.getTime(), calendarInicio.getTime());
        obtenerComprasCabecera(-1, -1, -1, calendarInicio.getTime(), calendarInicio.getTime());
    }

    public SeleccionVentaCabeceraTableModel getMovVentasTM() {
        return this.movVentasTM;
    }

    public SeleccionCompraCabeceraTableModel getMovComprasTM() {
        return this.movCompraTM;
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
        this.funcionario.setId_funcionario(-1);
        this.funcionario.setCedula(-1);
    }

    public void obtenerVentasCabecera(int idFuncionario, int idCliente, int idTipoOperacion, Date fechaInicio, Date fechaFin) {
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
        ArrayList<SeleccionVentaCabecera> lista = new ArrayList<>();
        List<E_facturaCabeceraFX> list = DB_Ingreso.obtenerMovimientoVentasCabeceras(idFuncionario, idCliente, calendarInicio.getTime(), calendarFinal.getTime(), idTipoOperacion);
        for (E_facturaCabeceraFX ventaCabecera : list) {
            lista.add(new SeleccionVentaCabecera(ventaCabecera, true));
        }
        this.getMovVentasTM().setList(lista);
    }

    public void obtenerComprasCabecera(int idFuncionario, int idProveedor, int idTipoOperacion, Date fechaInicio, Date fechaFin) {
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

        ArrayList<SeleccionCompraCabecera> lista = new ArrayList<>();
        List<M_egreso_cabecera> list = DB_Egreso.obtenerMovimientoVentasCabeceras(idFuncionario, idProveedor, idTipoOperacion, calendarInicio.getTime(), calendarFinal.getTime());
        for (M_egreso_cabecera egresoCabecera : list) {
            lista.add(new SeleccionCompraCabecera(egresoCabecera, true));
        }
        this.getMovComprasTM().setList(lista);
    }
}
