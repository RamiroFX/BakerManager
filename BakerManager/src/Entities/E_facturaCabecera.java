/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_facturaCabecera {

    private Integer idFacturaCabecera, nroFactura, total;
    private M_funcionario funcionario;
    private M_cliente cliente;
    private E_tipoOperacion tipoOperacion;
    private Date tiempo;
    private E_formaPago formaPago;
    private Estado estado;
    private List<E_facturaDetalle> detalle;

    public E_facturaCabecera() {
        //detalle = new ArrayList<>();
        cliente = new M_cliente();
        funcionario = new M_funcionario();
        tipoOperacion = new E_tipoOperacion();
        formaPago = new E_formaPago();
        estado = new Estado();
    }

    public Integer getIdFacturaCabecera() {
        return idFacturaCabecera;
    }

    public void setIdFacturaCabecera(Integer idFacturaCabecera) {
        this.idFacturaCabecera = idFacturaCabecera;
    }

    public Integer getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(Integer nroFactura) {
        this.nroFactura = nroFactura;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public M_cliente getCliente() {
        return cliente;
    }

    public void setCliente(M_cliente cliente) {
        this.cliente = cliente;
    }

    public E_tipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(E_tipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public Date getTiempo() {
        return tiempo;
    }

    public void setTiempo(Date tiempo) {
        this.tiempo = tiempo;
    }

    public E_formaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(E_formaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<E_facturaDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<E_facturaDetalle> detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return getIdFacturaCabecera() + "|" + getNroFactura() + "|" + getCliente() + "|" + getTiempo();
    }

    /**
     * @return the total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

}
