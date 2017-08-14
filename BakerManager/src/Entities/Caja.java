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
public class Caja {

    private int idCaja, idEmpleadoApertura, idEmpleadoCierre;
    private Date tiempoApertura, tiempoCierre;

    public Caja() {
    }

    public Caja(int idCaja, int idEmpleadoApertura, int idEmpleadoCierre, Date tiempoApertura, Date tiempoCierre) {
        this.idCaja = idCaja;
        this.idEmpleadoApertura = idEmpleadoApertura;
        this.idEmpleadoCierre = idEmpleadoCierre;
        this.tiempoApertura = tiempoApertura;
        this.tiempoCierre = tiempoCierre;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public int getIdEmpleadoApertura() {
        return idEmpleadoApertura;
    }

    public void setIdEmpleadoApertura(int idEmpleadoApertura) {
        this.idEmpleadoApertura = idEmpleadoApertura;
    }

    public int getIdEmpleadoCierre() {
        return idEmpleadoCierre;
    }

    public void setIdEmpleadoCierre(int idEmpleadoCierre) {
        this.idEmpleadoCierre = idEmpleadoCierre;
    }

    public Date getTiempoApertura() {
        return tiempoApertura;
    }

    public void setTiempoApertura(Date tiempoApertura) {
        this.tiempoApertura = tiempoApertura;
    }

    public Date getTiempoCierre() {
        return tiempoCierre;
    }

    public void setTiempoCierre(Date tiempoCierre) {
        this.tiempoCierre = tiempoCierre;
    }

    @Override
    public String toString() {
        return "Caja:{Id: " + getIdCaja() + ", tiempoCierre:" + getTiempoCierre() + "}";
    }

}
