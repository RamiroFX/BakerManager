/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Ingreso;
import Entities.E_facturaCabeceraFX;
import Entities.M_facturaDetalle;
import ModeloTabla.SeleccionVentaCabecera;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ramiro
 */
public class M_facturacion {

    private int entidad;
    private String inicio, fin;
    private String condVenta;
    private boolean agregarTodos;

    M_facturacion(int idCliente, String fechaInicio, String fechaFin, String condVenta) {
        this.entidad = idCliente;
        this.inicio = fechaInicio;
        this.fin = fechaFin;
        this.condVenta = condVenta;
        this.agregarTodos = false;
    }

    public List<E_facturaCabeceraFX> obtenerVentasCabecera() {
        return DB_Ingreso.obtenerVentasCabeceras(entidad, inicio, fin, condVenta);
    }

    public List<M_facturaDetalle> obtenerVentasDetalle(ArrayList<SeleccionVentaCabecera> ventaCabecera) {
        return DB_Ingreso.obtenerVentaDetalles(ventaCabecera);
    }

    public boolean isAgregarTodos() {
        return agregarTodos;
    }

    public void setAgregarTodos(boolean agregarTodos) {
        this.agregarTodos = agregarTodos;
    }
}