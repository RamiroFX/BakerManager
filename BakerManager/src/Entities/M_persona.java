/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_persona {

    private String nombre;
    private String apellido;
    private String sexo;
    private String pais;
    private String ciudad;
    private Date fechaNacimiento;
    private String estadoCivil;
    private Integer id_persona, id_pais, id_ciudad, id_estado_civil, id_sexo, cedula;

    public M_persona() {
        super();
    }

    public M_persona(String nombre, String apellido, String sexo, String pais, String ciudad, Date fechaNacimiento, String estadoCivil, Integer idPersona, Integer ci) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.sexo = sexo;
        this.pais = pais;
        this.ciudad = ciudad;
        this.fechaNacimiento = fechaNacimiento;
        this.estadoCivil = estadoCivil;
        this.id_persona = idPersona;
        this.cedula = ci;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido the apellido to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * @return the nacionalidad
     */
    public String getPais() {
        return pais;
    }

    /**
     * @param nacionalidad the nacionalidad to set
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * @return the ciudad
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * @param ciudad the ciudad to set
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * @return the fechaNacimiento
     */
    public Date getFecha_nacimiento() {
        return fechaNacimiento;
    }

    /**
     * @param fechaNacimiento the fechaNacimiento to set
     */
    public void setFecha_nacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * @return the estadoCivil
     */
    public String getEstado_civil() {
        return estadoCivil;
    }

    /**
     * @param estadoCivil the estadoCivil to set
     */
    public void setEstado_civil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    /**
     * @return the id
     */
    public Integer getId_persona() {
        return id_persona;
    }

    /**
     * @param id the id to set
     */
    public void setId_persona(Integer id) {
        this.id_persona = id;
    }

    /**
     * @return the ci
     */
    public Integer getCedula() {
        return cedula;
    }

    /**
     * @param ci the ci to set
     */
    public void setCedula(Integer ci) {
        this.cedula = ci;
    }

    /**
     * @return the idNacion
     */
    public int getId_pais() {
        return id_pais;
    }

    /**
     * @param idNacion the idNacion to set
     */
    public void setId_pais(int idNacion) {
        this.id_pais = idNacion;
    }

    /**
     * @return the idCiudad
     */
    public int getId_ciudad() {
        return id_ciudad;
    }

    /**
     * @param idCiudad the idCiudad to set
     */
    public void setId_ciudad(int idCiudad) {
        this.id_ciudad = idCiudad;
    }

    /**
     * @return the id_estado_civil
     */
    public Integer getId_estado_civil() {
        return id_estado_civil;
    }

    /**
     * @param id_estado_civil the id_estado_civil to set
     */
    public void setId_estado_civil(Integer id_estado_civil) {
        this.id_estado_civil = id_estado_civil;
    }

    /**
     * @return the id_sexo
     */
    public Integer getId_sexo() {
        return id_sexo;
    }

    /**
     * @param id_sexo the id_sexo to set
     */
    public void setId_sexo(Integer id_sexo) {
        this.id_sexo = id_sexo;
    }
}
