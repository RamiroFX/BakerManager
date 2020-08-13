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
public class E_retencionVenta {

    private int id;
    private int nroRetencion;
    private int monto;
    private double porcentaje;
    private E_facturaCabecera venta;
    private M_funcionario funcionario;
    private Date tiempo;
    private Estado estado;

    public E_retencionVenta() {
        this.venta = new E_facturaCabecera();
        this.funcionario = new M_funcionario();
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
     * @return the nroRetencion
     */
    public int getNroRetencion() {
        return nroRetencion;
    }

    /**
     * @param nroRetencion the nroRetencion to set
     */
    public void setNroRetencion(int nroRetencion) {
        this.nroRetencion = nroRetencion;
    }

    /**
     * @return the monto
     */
    public int getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(int monto) {
        this.monto = monto;
    }

    /**
     * @return the porcentaje
     */
    public double getPorcentaje() {
        return porcentaje;
    }

    /**
     * @param porcentaje the porcentaje to set
     */
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    /**
     * @return the venta
     */
    public E_facturaCabecera getVenta() {
        return venta;
    }

    /**
     * @param venta the venta to set
     */
    public void setVenta(E_facturaCabecera venta) {
        this.venta = venta;
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
     * @return the estado
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
