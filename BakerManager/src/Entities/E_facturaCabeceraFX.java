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
public class E_facturaCabeceraFX {

    private Integer idFacturaCabecera, total, nroFactura;
    private String clienteEntidad, condVenta;
    private Date tiempo;

    public Integer getIdFacturaCabecera() {
        return idFacturaCabecera;
    }

    public void setIdFacturaCabecera(Integer idFacturaCabecera) {
        this.idFacturaCabecera = idFacturaCabecera;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return the clienteEntidad
     */
    public String getClienteEntidad() {
        return clienteEntidad;
    }

    /**
     * @param clienteEntidad the clienteEntidad to set
     */
    public void setClienteEntidad(String clienteEntidad) {
        this.clienteEntidad = clienteEntidad;
    }

    /**
     * @return the tiempo
     */
    public Date getTiempo() {
        return tiempo;
    }

    /**
     * @param tiempo the tiempo to set
     */
    public void setTiempo(Date tiempo) {
        this.tiempo = tiempo;
    }

    public Integer getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(Integer nroFactura) {
        this.nroFactura = nroFactura;
    }

    public String getCondVenta() {
        return condVenta;
    }

    public void setCondVenta(String condVenta) {
        this.condVenta = condVenta;
    }
}
