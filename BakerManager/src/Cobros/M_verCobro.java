/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Cobro;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import ModeloTabla.CtaCteDetalleTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verCobro {

    private E_cuentaCorrienteCabecera cabecera;
    private CtaCteDetalleTableModel tmDetalle;

    public M_verCobro() {
        this.cabecera = new E_cuentaCorrienteCabecera();
        this.tmDetalle = new CtaCteDetalleTableModel();
    }

    /**
     * @return the cabecera
     */
    public E_cuentaCorrienteCabecera getCabecera() {
        return cabecera;
    }

    /**
     * @param cabecera the cabecera to set
     */
    public void setCabecera(E_cuentaCorrienteCabecera cabecera) {
        this.cabecera = cabecera;
    }

    /**
     * @return the tmDetalle
     */
    public CtaCteDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    /**
     * @param tmDetalle the tmDetalle to set
     */
    public void setTmDetalle(CtaCteDetalleTableModel tmDetalle) {
        this.tmDetalle = tmDetalle;
    }

    public void actualizarDetalle(Integer idCabecera) {
        getTmDetalle().setList(DB_Cobro.obtenerCobroDetalle(idCabecera));
    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < getTmDetalle().getList().size(); i++) {
            E_cuentaCorrienteDetalle get = getTmDetalle().getList().get(i);
            total = total + (int) get.getMonto();
        }
        return total;
    }
}
