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
public class E_utilizacionMateriaPrimaCabecera {

    private int id;
    private int nroOrdenTrabajo;
    private Date fechaUtilizacion;
    private Date fechaRegistro;
    private M_funcionario funcionarioSistema;
    private M_funcionario funcionarioProduccion;
    private Estado estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNroOrdenTrabajo() {
        return nroOrdenTrabajo;
    }

    public void setNroOrdenTrabajo(int nroOrdenTrabajo) {
        this.nroOrdenTrabajo = nroOrdenTrabajo;
    }

    public Date getFechaUtilizacion() {
        return fechaUtilizacion;
    }

    public void setFechaUtilizacion(Date fechaUtilizacion) {
        this.fechaUtilizacion = fechaUtilizacion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public M_funcionario getFuncionarioSistema() {
        return funcionarioSistema;
    }

    public void setFuncionarioSistema(M_funcionario funcionarioSistema) {
        this.funcionarioSistema = funcionarioSistema;
    }

    public M_funcionario getFuncionarioProduccion() {
        return funcionarioProduccion;
    }

    public void setFuncionarioProduccion(M_funcionario funcionarioProduccion) {
        this.funcionarioProduccion = funcionarioProduccion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    
}
