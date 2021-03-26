/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import DB.DB_Inventario;
import Entities.E_ajusteStockCabecera;
import ModeloTabla.AjusteStockCabeceraTableModel;
import ModeloTabla.AjusteStockDetalleTableModel;
import ModeloTabla.SeleccionAjusteStockDetalleTM;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionAjusteStock {

    private E_ajusteStockCabecera cabecera;
    private AjusteStockCabeceraTableModel tmCabecera;
    private SeleccionAjusteStockDetalleTM tmDetalle;

    public M_gestionAjusteStock() {
        this.cabecera = new E_ajusteStockCabecera();
        this.cabecera.setId(-1);
        this.cabecera.getResponsable().setIdFuncionario(-1);
        this.tmCabecera = new AjusteStockCabeceraTableModel();
        this.tmDetalle = new SeleccionAjusteStockDetalleTM();
    }

    public E_ajusteStockCabecera getCabecera() {
        return cabecera;
    }

    public AjusteStockCabeceraTableModel getTmCabecera() {
        return tmCabecera;
    }

    public SeleccionAjusteStockDetalleTM getTmDetalle() {
        return tmDetalle;
    }

    public String obtenerNombreFuncionario() {
        String alias = this.getCabecera().getResponsable().getAlias();
        String nombre = this.getCabecera().getResponsable().getNombre();
        String apellido = this.getCabecera().getResponsable().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    void borrarDatos() {
        this.cabecera.setId(-1);
        this.cabecera.getResponsable().setIdFuncionario(-1);
    }

    public void consultarInventarios(Date fechaDesde, Date fechaHasta) {
        int idResponsable = cabecera.getResponsable().getIdFuncionario();
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaDesde);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(fechaHasta);
        calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
        calendarFinal.set(Calendar.MINUTE, 59);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);
        this.tmCabecera.setList(DB_Inventario.consultarAjusteStockCabecera(idResponsable, -1, true, calendarInicio.getTime(), calendarFinal.getTime(), 0, false));
    }

    public void consultarInventarioDetalle(int idCabecera) {
        this.tmDetalle.setList(DB_Inventario.consultarAjusteStockDetalle(idCabecera));
    }

}
