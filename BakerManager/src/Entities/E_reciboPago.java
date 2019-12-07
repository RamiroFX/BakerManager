/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_reciboPago {

    private int id;
    private int idFacturaCabecera;
    private int nroRecibo;
    private int importe;
    private Date fechaPago;
    private E_formaPago formaPago;
    private M_cliente cliente;
    private M_funcionario funcionario;
    private E_banco banco;
    private String observacion;

    public E_reciboPago() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFacturaCabecera() {
        return idFacturaCabecera;
    }

    public void setIdFacturaCabecera(int idFacturaCabecera) {
        this.idFacturaCabecera = idFacturaCabecera;
    }

    public int getNroRecibo() {
        return nroRecibo;
    }

    public void setNroRecibo(int nroRecibo) {
        this.nroRecibo = nroRecibo;
    }

    public int getImporte() {
        return importe;
    }

    public void setImporte(int importe) {
        this.importe = importe;
    }

    public void setFormaPago(E_formaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public M_cliente getCliente() {
        return cliente;
    }

    public void setCliente(M_cliente cliente) {
        this.cliente = cliente;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public E_formaPago getFormaPago() {
        return formaPago;
    }

    public void setBanco(E_banco banco) {
        this.banco = banco;
    }

    public E_banco getBanco() {
        return banco;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

}
