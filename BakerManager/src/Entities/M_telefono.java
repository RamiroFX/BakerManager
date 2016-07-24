/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Usuario
 */
public class M_telefono {
    private Integer id_telefono;
    private String numero;
    private String categoria;
    private String observacion;

    /**
     * @return the id_telefono
     */
    public Integer getId_telefono() {
        return id_telefono;
    }

    /**
     * @param id_telefono the id_telefono to set
     */
    public void setId_telefono(Integer id_telefono) {
        this.id_telefono = id_telefono;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the categoria
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the observacion
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * @param observacion the observacion to set
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
