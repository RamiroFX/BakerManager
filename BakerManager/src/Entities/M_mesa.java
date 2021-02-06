/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Timestamp;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_mesa extends E_facturaCabecera {

    private int idMesa, numeroMesa;

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getIdCondVenta() {
        return getTipoOperacion().getId();
    }

    public void setIdCondVenta(int idCondVenta) {
        this.getTipoOperacion().setId(idCondVenta);
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }
    
    public M_facturaCabecera toMFacturaCabecera(){
        M_facturaCabecera faca = new M_facturaCabecera();
        faca.setCliente(getCliente());
        faca.setCondVenta(getTipoOperacion());
        faca.setEstado(getEstado());
        faca.setFuncionario(getFuncionario());
        faca.setNroFactura(getNroFactura());
        faca.setTiempo(new Timestamp(getTiempo().getTime()));
        faca.setTimbrado(getTimbrado());
        return faca;
    }

}
