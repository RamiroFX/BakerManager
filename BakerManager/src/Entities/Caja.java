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

    private int idCaja, idEmpleadoApertura, idEmpleadoCierre, montoInicial,
            montoFinal, ingresoContado, ingresoCredito,
            egresoContado, egresoCredito;
    private Date tiempoApertura, tiempoCierre;

    public Caja() {
    }

    public Caja(int idCaja, int idEmpleadoApertura, int idEmpleadoCierre, int montoInicial, int montoFinal, int ingresoContado, int ingresoCredito, int egresoContado, int egresoCredito, Date tiempoApertura, Date tiempoCierre) {
        this.idCaja = idCaja;
        this.idEmpleadoApertura = idEmpleadoApertura;
        this.idEmpleadoCierre = idEmpleadoCierre;
        this.montoInicial = montoInicial;
        this.montoFinal = montoFinal;
        this.ingresoContado = ingresoContado;
        this.ingresoCredito = ingresoCredito;
        this.egresoContado = egresoContado;
        this.egresoCredito = egresoCredito;
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

    public int getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(int montoInicial) {
        this.montoInicial = montoInicial;
    }

    public int getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(int montoFinal) {
        this.montoFinal = montoFinal;
    }

    public int totalEgresoIngreso() {
        return getEgresoContado() + getEgresoCredito() + getIngresoContado() + getIngresoCredito();
    }

    public int getIngresoContado() {
        return ingresoContado;
    }

    public void setIngresoContado(int ingresoContado) {
        this.ingresoContado = ingresoContado;
    }

    public int getIngresoCredito() {
        return ingresoCredito;
    }

    public void setIngresoCredito(int ingresoCredito) {
        this.ingresoCredito = ingresoCredito;
    }

    public int getEgresoContado() {
        return egresoContado;
    }

    public void setEgresoContado(int egresoContado) {
        this.egresoContado = egresoContado;
    }

    public int getEgresoCredito() {
        return egresoCredito;
    }

    public void setEgresoCredito(int egresoCredito) {
        this.egresoCredito = egresoCredito;
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
        return "{Id: " + getIdCaja() + ", tiempoCierre:" + getIdEmpleadoCierre() + "}";
    }

}
