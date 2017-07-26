/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class CierreCaja {

    private Caja caja;
    private ArrayList<ArqueoCajaDetalle> apertura, cierre, deposito;

    public CierreCaja() {
        this.caja = new Caja();
        this.apertura = new ArrayList<>();
        this.cierre = new ArrayList<>();
        this.deposito = new ArrayList<>();
    }

    public CierreCaja(Caja caja, ArrayList<ArqueoCajaDetalle> apertura, ArrayList<ArqueoCajaDetalle> cierre, ArrayList<ArqueoCajaDetalle> deposito) {
        this.caja = caja;
        this.apertura = apertura;
        this.cierre = cierre;
        this.deposito = deposito;
    }

    public ArrayList<ArqueoCajaDetalle> getApertura() {
        return apertura;
    }

    public void setApertura(ArrayList<ArqueoCajaDetalle> apertura) {
        this.apertura = apertura;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public ArrayList<ArqueoCajaDetalle> getCierre() {
        return cierre;
    }

    public void setCierre(ArrayList<ArqueoCajaDetalle> cierre) {
        this.cierre = cierre;
    }

    public ArrayList<ArqueoCajaDetalle> getDeposito() {
        return deposito;
    }

    public void setDeposito(ArrayList<ArqueoCajaDetalle> deposito) {
        this.deposito = deposito;
    }

}
