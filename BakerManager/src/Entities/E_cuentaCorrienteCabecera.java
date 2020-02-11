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
public class E_cuentaCorrienteCabecera {

    private int id;
    private int debito;
    private int credito;
    private int nroRecibo;
    private M_cliente cliente;
    private M_funcionario cobrador;
    private M_funcionario funcionario;
    private E_cuentaCorrienteConcepto concepto;
    private Date fechaPago;
    private Date fechaOperacion;

    public E_cuentaCorrienteCabecera() {
        this.cliente = new M_cliente();
        this.funcionario = new M_funcionario();
        this.cobrador = new M_funcionario();
        this.concepto = new E_cuentaCorrienteConcepto();
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the debito
     */
    public int getDebito() {
        return debito;
    }

    /**
     * @param debito the debito to set
     */
    public void setDebito(int debito) {
        this.debito = debito;
    }

    /**
     * @return the credito
     */
    public int getCredito() {
        return credito;
    }

    /**
     * @param credito the credito to set
     */
    public void setCredito(int credito) {
        this.credito = credito;
    }

    /**
     * @return the cliente
     */
    public M_cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(M_cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the funcionario
     */
    public M_funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return the concepto
     */
    public E_cuentaCorrienteConcepto getConcepto() {
        return concepto;
    }

    /**
     * @param concepto the concepto to set
     */
    public void setConcepto(E_cuentaCorrienteConcepto concepto) {
        this.concepto = concepto;
    }

    /**
     * @return the fechaPago
     */
    public Date getFechaPago() {
        return fechaPago;
    }

    /**
     * @param fechaPago the fechaPago to set
     */
    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    /**
     * @return the fechaOperacion
     */
    public Date getFechaOperacion() {
        return fechaOperacion;
    }

    /**
     * @param fechaOperacion the fechaOperacion to set
     */
    public void setFechaOperacion(Date fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    /**
     * @return the nroRecibo
     */
    public int getNroRecibo() {
        return nroRecibo;
    }

    /**
     * @param nroRecibo the nroRecibo to set
     */
    public void setNroRecibo(int nroRecibo) {
        this.nroRecibo = nroRecibo;
    }

    /**
     * @return the cobrador
     */
    public M_funcionario getCobrador() {
        return cobrador;
    }

    /**
     * @param cobrador the cobrador to set
     */
    public void setCobrador(M_funcionario cobrador) {
        this.cobrador = cobrador;
    }

}
