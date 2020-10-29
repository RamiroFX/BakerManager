/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import DB.DB_Cobro;
import DB.DB_Ingreso;
import DB.DB_manager;
import Entities.E_facturaDetalle;
import Entities.E_facturaSinPago;
import Entities.E_tipoOperacion;
import Entities.M_facturaDetalle;
import ModeloTabla.FacturaSinPagoTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarVenta {

    private FacturaSinPagoTableModel tm;
    private E_facturaSinPago cabecera;

    public M_seleccionarVenta() {
        this.tm = new FacturaSinPagoTableModel();
        this.cabecera = new E_facturaSinPago();
    }

    public FacturaSinPagoTableModel getTm() {
        return tm;
    }

    public void setTm(FacturaSinPagoTableModel tm) {
        this.tm = tm;
    }

    public void setPc(E_facturaSinPago faca) {
        this.cabecera = faca;
    }

    public E_facturaSinPago getCabecera() {
        return cabecera;
    }

    public ArrayList<E_tipoOperacion> obtenerTipoOperacion() {
        E_tipoOperacion todos = new E_tipoOperacion();
        todos.setDescripcion("Todos");
        todos.setDuracion(0);
        todos.setId(-1);
        ArrayList<E_tipoOperacion> condVenta = new ArrayList<>();
        condVenta.add(todos);
        condVenta.addAll(DB_manager.obtenerTipoOperaciones());
        return condVenta;
    }

    public void consultarVenta(Date fechaInicio, Date fechaFinal, E_tipoOperacion tiop, boolean conFechas) {
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(fechaFinal);
        calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
        calendarFinal.set(Calendar.MINUTE, 59);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);
        this.tm.setList(DB_Cobro.consultarPagosPendiente(null, null, getCabecera().getCliente().getIdCliente(), -1, false));
    }

    public List<E_facturaDetalle> obtenerFacturaDetalle(int nroFactura) {
        List<E_facturaDetalle> detalles = new ArrayList<>();
        for (M_facturaDetalle ventaDetalle : DB_Ingreso.obtenerVentaDetallesNroFactura(nroFactura)) {
            E_facturaDetalle fd = new E_facturaDetalle();
            fd.setProducto(ventaDetalle.getProducto());
            fd.setDescuento(ventaDetalle.getDescuento());
            fd.setObservacion(ventaDetalle.getObservacion());
            fd.setPrecio(ventaDetalle.getPrecio());
            fd.setIdFacturaDetalle(ventaDetalle.getIdFacturaDetalle());
            fd.setCantidad(ventaDetalle.getCantidad());
            detalles.add(fd);
        }
        return detalles;
    }
}
