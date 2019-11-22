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

    M_facturacion(int idCliente, String fechaInicio, String fechaFin, String condVenta) {
        this.entidad = idCliente;
        this.inicio = fechaInicio;
        this.fin = fechaFin;
        this.condVenta = condVenta;
    }

    public List<E_facturaCabeceraFX> obtenerVentasCabecera() {
        return DB_Ingreso.obtenerVentasCabeceras(entidad, inicio, fin, condVenta);
    }

    public List<M_facturaDetalle> obtenerVentasDetalle(ArrayList<SeleccionVentaCabecera> ventaCabecera) {
        String cadenaCabeceras = "";
        for (int i = 0; i < ventaCabecera.size(); i++) {
            SeleccionVentaCabecera get = ventaCabecera.get(i);
            cadenaCabeceras = cadenaCabeceras + get.getFacturaCabecera().getIdFacturaCabecera() + ",";
        }
        cadenaCabeceras = cadenaCabeceras.substring(0, cadenaCabeceras.length() - 1);
        System.out.println("Ventas.M_facturacion.obtenerVentasDetalle()");
        System.out.println(cadenaCabeceras);
        return DB_Ingreso.obtenerVentaDetalles(ventaCabecera);
    }
}
