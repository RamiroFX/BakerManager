/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_movimientoProduccion {

    public static final int TIPO_PRODUCCION = 1, TIPO_DESPERDICIO = 2, TIPO_VENTA = 3,
            TIPO_COMPRA = 4, TIPO_INVENTARIO = 5, TIPO_UTILIZACION = 6;
    public static final String STR_TIPO_PRODUCCION = "Produccion", STR_TIPO_DESPERDICIO = "Desperdicio",
            STR_TIPO_VENTA = "Venta", STR_TIPO_COMPRA = "Compra", STR_TIPO_INVENTARIO = "Inventario",
            STR_TIPO_UTILIZACION = "Utilización";

    private int tipo;
    private String tipoDescripcion;
    private E_facturaCabecera venta;
    private E_facturaDetalle ventaDetalle;
    private E_produccionCabecera produccion;
    private E_produccionDetalle produccionDetalle;
    private E_produccionDesperdicioCabecera desperdicio;
    private E_produccionDesperdicioDetalle desperdicioDetalle;
    private E_ajusteStockCabecera inventario;
    private E_ajusteStockDetalle inventarioDetalle;
    private M_egresoCabecera compra;
    private M_egreso_detalle compraDetalle;
    private E_produccionFilmBaja produccionFilmBaja;

    private double entrada;
    private double salida;
    private double balance;

    public E_movimientoProduccion() {
    }

    public String getMovDescripcion() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
        switch (getTipo()) {
            case E_movimientoProduccion.TIPO_VENTA: {
                int nroFactura = getVenta().getNroFactura();
                String sNroFactura = decimalFormat.format(nroFactura);
                return getTipoDescripcion() + " N° " + sNroFactura;
            }
            case E_movimientoProduccion.TIPO_COMPRA: {
                int nroFactura = getCompra().getNro_factura();
                String sNroFactura = decimalFormat.format(nroFactura);
                return getTipoDescripcion() + " N° " + sNroFactura;
            }
            case E_movimientoProduccion.TIPO_PRODUCCION: {
                int nroOT = getProduccion().getNroOrdenTrabajo();
                String sNroOT = decimalFormat.format(nroOT);
                return getTipoDescripcion() + " OT° " + sNroOT;
            }
            case E_movimientoProduccion.TIPO_DESPERDICIO: {
                int nroID = getDesperdicio().getProduccionCabecera().getNroOrdenTrabajo();
                String sNroID = decimalFormat.format(nroID);
                return getTipoDescripcion() + " OT° " + sNroID;
            }
            case E_movimientoProduccion.TIPO_INVENTARIO: {
                int nroID = getInventario().getId();
                String sNroID = decimalFormat.format(nroID);
                return getTipoDescripcion() + " ID° " + sNroID;
            }
            case E_movimientoProduccion.TIPO_UTILIZACION: {
                int nroID = getProduccionFilmBaja().getProduccionCabecera().getNroOrdenTrabajo();
                String sNroID = decimalFormat.format(nroID);
                return getTipoDescripcion() + " OT° " + sNroID;
            }
        }
        return "no data";
    }

    public Date getMovFecha() {
        switch (getTipo()) {
            case E_movimientoProduccion.TIPO_VENTA: {
                return getVenta().getTiempo();
            }
            case E_movimientoProduccion.TIPO_COMPRA: {
                return getCompra().getTiempo();
            }
            case E_movimientoProduccion.TIPO_PRODUCCION: {
                return getProduccion().getFechaProduccion();
            }
            case E_movimientoProduccion.TIPO_DESPERDICIO: {
                return getDesperdicio().getTiempo();
            }
            case E_movimientoProduccion.TIPO_INVENTARIO: {
                return getInventarioDetalle().getTiempoRegistro();
            }
            case E_movimientoProduccion.TIPO_UTILIZACION: {
                return getProduccionFilmBaja().getFechaUtilizacion();
            }
        }
        System.out.println("Entities.E_movimientoProduccion.getMovFecha()");
        System.err.println("retornando fecha por defecto");
        return Calendar.getInstance().getTime();
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getTipoDescripcion() {
        return tipoDescripcion;
    }

    public void setTipoDescripcion(String tipoDescripcion) {
        this.tipoDescripcion = tipoDescripcion;
    }

    public E_facturaCabecera getVenta() {
        return venta;
    }

    public void setVenta(E_facturaCabecera venta) {
        this.venta = venta;
    }

    public E_produccionCabecera getProduccion() {
        return produccion;
    }

    public void setProduccion(E_produccionCabecera produccion) {
        this.produccion = produccion;
    }

    public E_produccionDesperdicioCabecera getDesperdicio() {
        return desperdicio;
    }

    public void setDesperdicio(E_produccionDesperdicioCabecera desperdicio) {
        this.desperdicio = desperdicio;
    }

    public M_egresoCabecera getCompra() {
        return compra;
    }

    public void setCompra(M_egresoCabecera compra) {
        this.compra = compra;
    }

    public double getEntrada() {
        return entrada;
    }

    public void setEntrada(double entrada) {
        this.entrada = entrada;
    }

    public double getSalida() {
        return salida;
    }

    public void setSalida(double salida) {
        this.salida = salida;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public E_facturaDetalle getVentaDetalle() {
        return ventaDetalle;
    }

    public void setVentaDetalle(E_facturaDetalle ventaDetalle) {
        this.ventaDetalle = ventaDetalle;
    }

    public E_produccionDetalle getProduccionDetalle() {
        return produccionDetalle;
    }

    public void setProduccionDetalle(E_produccionDetalle produccionDetalle) {
        this.produccionDetalle = produccionDetalle;
    }

    public E_produccionDesperdicioDetalle getDesperdicioDetalle() {
        return desperdicioDetalle;
    }

    public void setDesperdicioDetalle(E_produccionDesperdicioDetalle desperdicioDetalle) {
        this.desperdicioDetalle = desperdicioDetalle;
    }

    public M_egreso_detalle getCompraDetalle() {
        return compraDetalle;
    }

    public void setCompraDetalle(M_egreso_detalle compraDetalle) {
        this.compraDetalle = compraDetalle;
    }

    public E_ajusteStockCabecera getInventario() {
        return inventario;
    }

    public void setInventario(E_ajusteStockCabecera inventario) {
        this.inventario = inventario;
    }

    public E_ajusteStockDetalle getInventarioDetalle() {
        return inventarioDetalle;
    }

    public void setInventarioDetalle(E_ajusteStockDetalle inventarioDetalle) {
        this.inventarioDetalle = inventarioDetalle;
    }

    public E_produccionFilmBaja getProduccionFilmBaja() {
        return produccionFilmBaja;
    }

    public void setProduccionFilmBaja(E_produccionFilmBaja produccionFilmBaja) {
        this.produccionFilmBaja = produccionFilmBaja;
    }

}
