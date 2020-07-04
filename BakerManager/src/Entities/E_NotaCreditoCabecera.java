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
public class E_NotaCreditoCabecera {

    private int id;
    private int nroNotaCredito;
    private int total;
    private int saldo;
    private E_facturaCabecera facturaCabecera;
    private M_cliente cliente;
    private M_funcionario funcionario;
    private Date tiempo;
    private Estado estado;

    public E_NotaCreditoCabecera() {
        this.facturaCabecera = new E_facturaCabecera();
        this.cliente = new M_cliente();
        this.funcionario = new M_funcionario();
        this.tiempo = new Date();
        this.estado = new Estado();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNroNotaCredito() {
        return nroNotaCredito;
    }

    public void setNroNotaCredito(int nroNotaCredito) {
        this.nroNotaCredito = nroNotaCredito;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public E_facturaCabecera getFacturaCabecera() {
        return facturaCabecera;
    }

    public void setFacturaCabecera(E_facturaCabecera facturaCabecera) {
        this.facturaCabecera = facturaCabecera;
    }

    public M_cliente getCliente() {
        return cliente;
    }

    public void setCliente(M_cliente cliente) {
        this.cliente = cliente;
    }

    public Date getTiempo() {
        return tiempo;
    }

    public void setTiempo(Date tiempo) {
        this.tiempo = tiempo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
