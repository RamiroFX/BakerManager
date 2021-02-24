package Entities;

import DB.DB_manager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class M_funcionario extends M_persona {

    private String ocupacion, observacion, alias, email, nro_celular,
            nro_telefono, direccion, password;
    private Date fechaIngreso;
    private Integer idFuncionario;

    private ArrayList rol;

    public M_funcionario() {
        super();
    }

    public M_funcionario(String ocupacion, String observacion, String alias, String email, String nro_celular, String nro_telefono, String direccion, String password, Date fechaIngreso, Integer id_funcionario, ArrayList rol, String nombre, String apellido, String sexo, String pais, String ciudad, Date fechaNacimiento, String estadoCivil, Integer idPersona, Integer ci) {
        super(nombre, apellido, sexo, pais, ciudad, fechaNacimiento, estadoCivil, idPersona, ci);
        this.ocupacion = ocupacion;
        this.observacion = observacion;
        this.alias = alias;
        this.email = email;
        this.nro_celular = nro_celular;
        this.nro_telefono = nro_telefono;
        this.direccion = direccion;
        this.password = password;
        this.fechaIngreso = fechaIngreso;
        this.idFuncionario = id_funcionario;
        this.rol = rol;
    }

    public boolean validarUsuario(String url, int dbms, String pass) throws SQLException {
        return DB_manager.conectarBD(dbms, url, getAlias(), pass);
    }

    public boolean validarUsuario(String pass) throws SQLException {
        return DB_manager.conectarBD(getAlias(), pass);
    }

    /**
     * @return the ocupacion
     */
    public String getOcupacion() {
        return ocupacion;
    }

    /**
     * @param ocupacion the ocupacion to set
     */
    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    /**
     * @return the fechaIngreso
     */
    public Date getFecha_ingreso() {
        return fechaIngreso;
    }

    /**
     * @param fechaIngreso the fechaIngreso to set
     */
    public void setFecha_ingreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    /**
     * @return the idUsuario
     */
    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    /**
     * @param idUsuario the idUsuario to set
     */
    public void setIdFuncionario(Integer id_funcionario) {
        this.idFuncionario = id_funcionario;
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

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the rol
     */
    public ArrayList getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(ArrayList rol) {
        this.rol = rol;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the nro_celular
     */
    public String getNro_celular() {
        return nro_celular;
    }

    /**
     * @param nro_celular the nro_celular to set
     */
    public void setNro_celular(String nro_celular) {
        this.nro_celular = nro_celular;
    }

    /**
     * @return the nro_telefono
     */
    public String getNro_telefono() {
        return nro_telefono;
    }

    /**
     * @param nro_telefono the nro_telefono to set
     */
    public void setNro_telefono(String nro_telefono) {
        this.nro_telefono = nro_telefono;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return getAlias();
    }

}
