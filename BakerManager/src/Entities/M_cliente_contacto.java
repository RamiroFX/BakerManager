/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Usuario
 */
public class M_cliente_contacto extends M_persona {

    private Integer idClienteContacto, idCliente;
    private String direccion, telefono, email, observacion;

    public M_cliente_contacto() {
    }

    public Integer getIdClienteContacto() {
        return idClienteContacto;
    }

    public void setIdClienteContacto(Integer idClienteContacto) {
        this.idClienteContacto = idClienteContacto;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    
}
