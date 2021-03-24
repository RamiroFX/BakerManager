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
public class E_ajusteStockCabecera {

    private int id;
    private M_funcionario responsable, registradoPor;
    private Estado estado;
    private String observacion;
    private Date tiempoInicio, tiempoFin;

    public E_ajusteStockCabecera() {
        this.responsable = new M_funcionario();
        this.registradoPor = new M_funcionario();
        this.estado = new Estado();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public M_funcionario getResponsable() {
        return responsable;
    }

    public void setResponsable(M_funcionario responsable) {
        this.responsable = responsable;
    }

    public M_funcionario getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(M_funcionario registradoPor) {
        this.registradoPor = registradoPor;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setTiempoFin(Date tiempoFin) {
        this.tiempoFin = tiempoFin;
    }

    public Date getTiempoFin() {
        return tiempoFin;
    }

    public void setTiempoInicio(Date tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public Date getTiempoInicio() {
        return tiempoInicio;
    }

    @Override
    public String toString() {
        return "AjusteStockCabecera{"
                + "id: " + getId() + ","
                + "responsable: " + getResponsable() + ","
                + "registradoPor: " + getRegistradoPor() + ","
                + "tiempoInicio: " + getTiempoInicio()+ ","
                + "tiempoFin: " + getTiempoFin()+ ","
                + "Estado: " + getEstado().getDescripcion()
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true   
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof E_ajusteStockCabecera)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members  
        E_ajusteStockCabecera c = (E_ajusteStockCabecera) o;

        // Compare the data members and return accordingly  
        return this.getId() == c.getId();
    }
}
