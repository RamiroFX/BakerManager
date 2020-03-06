/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import DB.DB_Pago;
import Entities.E_reciboPagoCabecera;
import Entities.E_reciboPagoDetalle;
import ModeloTabla.ReciboPagoDetalleTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verPago {

    private E_reciboPagoCabecera cabecera;
    private ReciboPagoDetalleTableModel tmDetalle;

    public M_verPago() {
        this.cabecera = new E_reciboPagoCabecera();
        this.tmDetalle = new ReciboPagoDetalleTableModel();
    }

    /**
     * @return the cabecera
     */
    public E_reciboPagoCabecera getCabecera() {
        return cabecera;
    }

    /**
     * @param cabecera the cabecera to set
     */
    public void setCabecera(E_reciboPagoCabecera cabecera) {
        this.cabecera = cabecera;
    }

    /**
     * @return the tmDetalle
     */
    public ReciboPagoDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    /**
     * @param tmDetalle the tmDetalle to set
     */
    public void setTmDetalle(ReciboPagoDetalleTableModel tmDetalle) {
        this.tmDetalle = tmDetalle;
    }

    public void actualizarDetalle(Integer idCabecera) {
        getTmDetalle().setList(DB_Pago.obtenerPagoDetalle(idCabecera));
    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < getTmDetalle().getList().size(); i++) {
            E_reciboPagoDetalle get = getTmDetalle().getList().get(i);
            total = total + (int) get.getMonto();
        }
        return total;
    }
}
