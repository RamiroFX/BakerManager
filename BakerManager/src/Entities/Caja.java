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
    private M_funcionario funcionarioApertura, funcionarioCierre;
    private int montoApertura, montoCierre, montoDepositado;
    private Date tiempoApertura, tiempoCierre;
    private Estado estado;

    public Caja() {
        funcionarioApertura = new M_funcionario();
        funcionarioCierre = new M_funcionario();
        estado = new Estado();
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

    public M_funcionario getFuncionarioApertura() {
        return funcionarioApertura;
    }

    public void setFuncionarioApertura(M_funcionario funcionarioApertura) {
        this.funcionarioApertura = funcionarioApertura;
    }

    public M_funcionario getFuncionarioCierre() {
        return funcionarioCierre;
    }

    public void setFuncionarioCierre(M_funcionario funcionarioCierre) {
        this.funcionarioCierre = funcionarioCierre;
    }

    public int getMontoApertura() {
        return montoApertura;
    }

    public void setMontoApertura(int montoApertura) {
        this.montoApertura = montoApertura;
    }

    public int getMontoCierre() {
        return montoCierre;
    }

    public void setMontoCierre(int montoCierre) {
        this.montoCierre = montoCierre;
    }

    public int getMontoDepositado() {
        return montoDepositado;
    }

    public void setMontoDepositado(int montoDepositado) {
        this.montoDepositado = montoDepositado;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

}
