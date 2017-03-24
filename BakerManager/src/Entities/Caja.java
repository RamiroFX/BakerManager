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

    private int idCaja, idEmpleadoApertura, idEmpleadoCierre, montoInicial, montoFinal, totalEgreso, totalIngreso;
    private Date fechaApertura, fechaCierre;
    private Date horaApertura, horaCierre;

    public Caja() {
    }

    public Caja(int idCaja, int idEmpleadoApertura, int idEmpleadoCierre, int montoInicial, int montoFinal, int totalEgreso, int totalIngreso, Date fechaApertura, Date fechaCierre, Date horaApertura, Date horaCierre) {
        this.idCaja = idCaja;
        this.idEmpleadoApertura = idEmpleadoApertura;
        this.idEmpleadoCierre = idEmpleadoCierre;
        this.montoInicial = montoInicial;
        this.montoFinal = montoFinal;
        this.totalEgreso = totalEgreso;
        this.totalIngreso = totalIngreso;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
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

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
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

    public int getTotalEgreso() {
        return totalEgreso;
    }

    public int getTotalIngreso() {
        return totalIngreso;
    }

    public void setTotalEgreso(int totalEgreso) {
        this.totalEgreso = totalEgreso;
    }

    public void setTotalIngreso(int totalIngreso) {
        this.totalIngreso = totalIngreso;
    }

    public int totalEgresoIngreso() {
        return getTotalEgreso() + getTotalIngreso();
    }

    public Date getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(Date horaApertura) {
        this.horaApertura = horaApertura;
    }

    public Date getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(Date horaCierre) {
        this.horaCierre = horaCierre;
    }
}
