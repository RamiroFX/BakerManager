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
public class E_Timbrado {

    private int id;
    private int nroTimbrado;
    private int nroSucursal;
    private int nroPuntoVenta;
    private int nroBoletaInicial;
    private int nroBoletaFinal;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaVencimiento;
    private M_funcionario creador;
    private Estado estado;
    private E_impresionPlantilla plantillaImpresion;

    public E_Timbrado() {
        creador = new M_funcionario();
        estado = new Estado();
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
     * @return the nroTimbrado
     */
    public int getNroTimbrado() {
        return nroTimbrado;
    }

    /**
     * @param nroTimbrado the nroTimbrado to set
     */
    public void setNroTimbrado(int nroTimbrado) {
        this.nroTimbrado = nroTimbrado;
    }

    /**
     * @return the nroSucursal
     */
    public int getNroSucursal() {
        return nroSucursal;
    }

    /**
     * @param nroSucursal the nroSucursal to set
     */
    public void setNroSucursal(int nroSucursal) {
        this.nroSucursal = nroSucursal;
    }

    /**
     * @return the nroPuntoVenta
     */
    public int getNroPuntoVenta() {
        return nroPuntoVenta;
    }

    /**
     * @param nroPuntoVenta the nroPuntoVenta to set
     */
    public void setNroPuntoVenta(int nroPuntoVenta) {
        this.nroPuntoVenta = nroPuntoVenta;
    }

    /**
     * @return the nroBoletaInicial
     */
    public int getNroBoletaInicial() {
        return nroBoletaInicial;
    }

    /**
     * @param nroBoletaInicial the nroBoletaInicial to set
     */
    public void setNroBoletaInicial(int nroBoletaInicial) {
        this.nroBoletaInicial = nroBoletaInicial;
    }

    /**
     * @return the nroBoletaFinal
     */
    public int getNroBoletaFinal() {
        return nroBoletaFinal;
    }

    /**
     * @param nroBoletaFinal the nroBoletaFinal to set
     */
    public void setNroBoletaFinal(int nroBoletaFinal) {
        this.nroBoletaFinal = nroBoletaFinal;
    }

    /**
     * @return the fechaCreacion
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @param fechaCreacion the fechaCreacion to set
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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
     * @return the creador
     */
    public M_funcionario getCreador() {
        return creador;
    }

    /**
     * @param creador the creador to set
     */
    public void setCreador(M_funcionario creador) {
        this.creador = creador;
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

    public String descripcionNumerica() {
        return getNroTimbrado() + "-" + getNroSucursal() + "-" + getNroPuntoVenta();
    }

    @Override
    public String toString() {
        return getNroTimbrado() + "";
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public E_impresionPlantilla getPlantillaImpresion() {
        return plantillaImpresion;
    }

    public void setPlantillaImpresion(E_impresionPlantilla plantillaImpresion) {
        this.plantillaImpresion = plantillaImpresion;
    }

}
