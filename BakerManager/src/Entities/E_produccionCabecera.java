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
public class E_produccionCabecera {

    private int id;
    private int nroOrdenTrabajo;
    private Date fechaProduccion;
    private Date fechaRegistro;
    private Date fechaVencimiento;
    private M_funcionario funcionarioSistema;
    private M_funcionario funcionarioProduccion;
    private E_produccionTipo tipo;
    private Estado estado;
    
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
     * @return the nroOrdenTrabajo
     */
    public int getNroOrdenTrabajo() {
        return nroOrdenTrabajo;
    }

    /**
     * @param nroOrdenTrabajo the nroOrdenTrabajo to set
     */
    public void setNroOrdenTrabajo(int nroOrdenTrabajo) {
        this.nroOrdenTrabajo = nroOrdenTrabajo;
    }

    /**
     * @return the fechaProduccion
     */
    public Date getFechaProduccion() {
        return fechaProduccion;
    }

    /**
     * @param fechaProduccion the fechaProduccion to set
     */
    public void setFechaProduccion(Date fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    /**
     * @return the fechaRegistro
     */
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * @param fechaRegistro the fechaRegistro to set
     */
    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * @return the fechaVencimiento
     */
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fechaVencimiento to set
     */
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the funcionarioSistema
     */
    public M_funcionario getFuncionarioSistema() {
        return funcionarioSistema;
    }

    /**
     * @param funcionarioSistema the funcionarioSistema to set
     */
    public void setFuncionarioSistema(M_funcionario funcionarioSistema) {
        this.funcionarioSistema = funcionarioSistema;
    }

    /**
     * @return the funcionarioProduccion
     */
    public M_funcionario getFuncionarioProduccion() {
        return funcionarioProduccion;
    }

    /**
     * @param funcionarioProduccion the funcionarioProduccion to set
     */
    public void setFuncionarioProduccion(M_funcionario funcionarioProduccion) {
        this.funcionarioProduccion = funcionarioProduccion;
    }

    /**
     * @return the tipo
     */
    public E_produccionTipo getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(E_produccionTipo tipo) {
        this.tipo = tipo;
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
