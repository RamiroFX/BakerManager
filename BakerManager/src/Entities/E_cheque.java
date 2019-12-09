/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author Ramiro
 */
public class E_cheque {

    private int id;
    private E_banco banco;
    private Double importe;
    private M_cliente cliente;
    private Date fecha;
    private Date fechaDiferida;
    private int numeroCheque;
    private E_tipoCheque tipo;

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
     * @return the banco
     */
    public E_banco getBanco() {
        return banco;
    }

    /**
     * @param banco the banco to set
     */
    public void setBanco(E_banco banco) {
        this.banco = banco;
    }

    /**
     * @return the importe
     */
    public Double getImporte() {
        return importe;
    }

    /**
     * @param importe the importe to set
     */
    public void setImporte(Double importe) {
        this.importe = importe;
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
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the fechaDiferida
     */
    public Date getFechaDiferida() {
        return fechaDiferida;
    }

    /**
     * @param fechaDiferida the fechaDiferida to set
     */
    public void setFechaDiferida(Date fechaDiferida) {
        this.fechaDiferida = fechaDiferida;
    }

    /**
     * @return the numeroCheque
     */
    public int getNumeroCheque() {
        return numeroCheque;
    }

    /**
     * @param numeroCheque the numeroCheque to set
     */
    public void setNumeroCheque(int numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    /**
     * @return the tipo
     */
    public E_tipoCheque getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(E_tipoCheque tipo) {
        this.tipo = tipo;
    }
}
