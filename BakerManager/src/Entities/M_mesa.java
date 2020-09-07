/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_mesa extends E_facturaCabecera {

    private Integer idMesa, numeroMesa;
    private Date tiempo;

    public Integer getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(Integer idMesa) {
        this.idMesa = idMesa;
    }

    public Integer getIdCondVenta() {
        return getTipoOperacion().getId();
    }

    public void setIdCondVenta(Integer idCondVenta) {
        this.getTipoOperacion().setId(idCondVenta);
    }

    public Integer getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(Integer numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

}
