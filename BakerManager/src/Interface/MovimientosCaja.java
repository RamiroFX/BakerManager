/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.E_cuentaCorrienteCabecera;
import Entities.E_facturaCabecera;
import Entities.E_reciboPagoCabecera;
import Entities.M_egreso_cabecera;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class MovimientosCaja {

    ArrayList<E_reciboPagoCabecera> movimientoPagos;
    ArrayList<E_cuentaCorrienteCabecera> movimientoCobros;
    ArrayList<E_facturaCabecera> movimientoVentas;
    ArrayList<M_egreso_cabecera> movimientoCompras;

    public MovimientosCaja() {
        this.movimientoVentas = new ArrayList<>();
        this.movimientoCompras = new ArrayList<>();
        this.movimientoCobros = new ArrayList<>();
        this.movimientoPagos = new ArrayList<>();
    }

    public void setMovimientoCobros(ArrayList<E_cuentaCorrienteCabecera> movimientoCobros) {
        this.movimientoCobros = movimientoCobros;
    }

    public void setMovimientoCompras(ArrayList<M_egreso_cabecera> movimientoCompras) {
        this.movimientoCompras = movimientoCompras;
    }

    public void setMovimientoPagos(ArrayList<E_reciboPagoCabecera> movimientoPagos) {
        this.movimientoPagos = movimientoPagos;
    }

    public void setMovimientoVentas(ArrayList<E_facturaCabecera> movimientoVentas) {
        this.movimientoVentas = movimientoVentas;
    }

    public ArrayList<E_cuentaCorrienteCabecera> getMovimientoCobros() {
        return movimientoCobros;
    }

    public ArrayList<M_egreso_cabecera> getMovimientoCompras() {
        return movimientoCompras;
    }

    public ArrayList<E_reciboPagoCabecera> getMovimientoPagos() {
        return movimientoPagos;
    }

    public ArrayList<E_facturaCabecera> getMovimientoVentas() {
        return movimientoVentas;
    }

}
