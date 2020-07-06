/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import DB.DB_Ingreso;
import bauplast.*;
import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_facturaCabecera;
import Entities.E_facturaDetalle;
import Entities.E_productoClasificacion;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_facturaDetalle;
import Entities.ProductoCategoria;
import ModeloTabla.FacturaCabeceraTableModel;
import ModeloTabla.SeleccionarProductoRolloTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarVenta {

    private FacturaCabeceraTableModel tm;
    private E_facturaCabecera cabecera;

    public M_seleccionarVenta() {
        this.tm = new FacturaCabeceraTableModel();
        this.cabecera = new E_facturaCabecera();
    }

    public FacturaCabeceraTableModel getTm() {
        return tm;
    }

    public void setTm(FacturaCabeceraTableModel tm) {
        this.tm = tm;
    }

    public void setPc(E_facturaCabecera faca) {
        this.cabecera = faca;
    }

    public E_facturaCabecera getCabecera() {
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
        this.tm.setFacturaCabeceraList(DB_Ingreso.obtenerIngreso2(
                calendarInicio.getTime(), calendarFinal.getTime(), getCabecera().getCliente().getIdCliente(), -1, tiop.getId(), -1, -1, conFechas));
    }

    public List<E_facturaDetalle> obtenerFacturaDetalle(Integer idFacturaCabecera) {
        List<E_facturaDetalle> detalles = new ArrayList<>();
        for (M_facturaDetalle ventaDetalle : DB_Ingreso.obtenerVentaDetalles(idFacturaCabecera)) {
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
