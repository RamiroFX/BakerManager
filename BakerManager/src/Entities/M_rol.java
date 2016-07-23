/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Entities;

/**
 *
 * @author Administrador
 */
public class M_rol {
    private Integer id;
    private String descripcion;

    public M_rol() {
    }

    public M_rol(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
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

}
