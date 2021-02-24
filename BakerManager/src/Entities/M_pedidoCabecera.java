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
public class M_pedidoCabecera extends E_facturaCabecera {

    private int idPedido;
    private Date tiempoRecepcion, tiempoEntrega;
    private String direccion, referencia;
    private E_estadoPedido estadoPedido;

    public M_pedidoCabecera() {
        this.estadoPedido = new E_estadoPedido();
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Date getTiempoRecepcion() {
        return tiempoRecepcion;
    }

    public void setTiempoRecepcion(Date tiempoRecepcion) {
        this.tiempoRecepcion = tiempoRecepcion;
    }

    public Date getTiempoEntrega() {
        return tiempoEntrega;
    }

    public void setTiempoEntrega(Date tiempoEntrega) {
        this.tiempoEntrega = tiempoEntrega;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void setEstadoPedido(E_estadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public E_estadoPedido getEstadoPedido() {
        return estadoPedido;
    }

}
