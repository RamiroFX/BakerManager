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
public class E_produccionFilm {

    private int id;
    private int nroFilm;
    private int ordenTrabajoCabecera;
    private int ordenTrabajoDetalle;
    private int cono;
    private int medida;
    private int micron;
    private Double peso;
    private Date fechaCreacion;
    private M_producto producto;
    private M_funcionario responsable;
    private E_productoClasificacion productoClasificacion;
    private Estado estado;

    public E_produccionFilm() {
        this.responsable = new M_funcionario();
        this.producto = new M_producto();
        this.productoClasificacion = new E_productoClasificacion();
        this.estado = new Estado();
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
     * @return the nroFilm
     */
    public int getNroFilm() {
        return nroFilm;
    }

    /**
     * @param nroFilm the nroFilm to set
     */
    public void setNroFilm(int nroFilm) {
        this.nroFilm = nroFilm;
    }

    /**
     * @return the ordenTrabajoCabecera
     */
    public int getOrdenTrabajoCabecera() {
        return ordenTrabajoCabecera;
    }

    /**
     * @param ordenTrabajoCabecera the ordenTrabajoCabecera to set
     */
    public void setOrdenTrabajoCabecera(int ordenTrabajoCabecera) {
        this.ordenTrabajoCabecera = ordenTrabajoCabecera;
    }

    /**
     * @return the ordenTrabajoDetalle
     */
    public int getOrdenTrabajoDetalle() {
        return ordenTrabajoDetalle;
    }

    /**
     * @param ordenTrabajoDetalle the ordenTrabajoDetalle to set
     */
    public void setOrdenTrabajoDetalle(int ordenTrabajoDetalle) {
        this.ordenTrabajoDetalle = ordenTrabajoDetalle;
    }

    /**
     * @return the cono
     */
    public int getCono() {
        return cono;
    }

    /**
     * @param cono the cono to set
     */
    public void setCono(int cono) {
        this.cono = cono;
    }

    /**
     * @return the medida
     */
    public int getMedida() {
        return medida;
    }

    /**
     * @param medida the medida to set
     */
    public void setMedida(int medida) {
        this.medida = medida;
    }

    /**
     * @return the micron
     */
    public int getMicron() {
        return micron;
    }

    /**
     * @param micron the micron to set
     */
    public void setMicron(int micron) {
        this.micron = micron;
    }

    /**
     * @return the peso
     */
    public Double getPeso() {
        return peso;
    }

    /**
     * @param peso the peso to set
     */
    public void setPeso(Double peso) {
        this.peso = peso;
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
     * @return the responsable
     */
    public M_funcionario getResponsable() {
        return responsable;
    }

    /**
     * @param responsable the responsable to set
     */
    public void setResponsable(M_funcionario responsable) {
        this.responsable = responsable;
    }

    /**
     * @return the productoClasificacion
     */
    public E_productoClasificacion getProductoClasificacion() {
        return productoClasificacion;
    }

    /**
     * @param productoClasificacion the productoClasificacion to set
     */
    public void setProductoClasificacion(E_productoClasificacion productoClasificacion) {
        this.productoClasificacion = productoClasificacion;
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

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    public M_producto getProducto() {
        return producto;
    }

}
