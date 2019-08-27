/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_ticketPreferencia {

    private int id;
    private String cabecera;
    private String pie;
    private String nombreImpresora;

    public E_ticketPreferencia() {
    }

    public E_ticketPreferencia(int id, String cabecera, String pie, String nombreImpresora) {
        this.id = id;
        this.cabecera = cabecera;
        this.pie = pie;
        this.nombreImpresora = nombreImpresora;
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
     * @return the cabecera
     */
    public String getCabecera() {
        return cabecera;
    }

    /**
     * @param cabecera the cabecera to set
     */
    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    /**
     * @return the pie
     */
    public String getPie() {
        return pie;
    }

    /**
     * @param pie the pie to set
     */
    public void setPie(String pie) {
        this.pie = pie;
    }

    /**
     * @return the nombreImpresora
     */
    public String getNombreImpresora() {
        return nombreImpresora;
    }

    /**
     * @param nombreImpresora the nombreImpresora to set
     */
    public void setNombreImpresora(String nombreImpresora) {
        this.nombreImpresora = nombreImpresora;
    }

}
