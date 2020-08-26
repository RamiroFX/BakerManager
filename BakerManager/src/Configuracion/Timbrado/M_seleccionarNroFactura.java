/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import DB.DB_Ingreso;
import Entities.E_Timbrado;
import Interface.RecibirTimbradoVentaCallback;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarNroFactura {

    private RecibirTimbradoVentaCallback callback;
    private int nroFactura;
    private E_Timbrado timbrado;

    public M_seleccionarNroFactura() {
        this.timbrado = new E_Timbrado();
        this.nroFactura = -1;
    }

    public int getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(int nroFactura) {
        this.nroFactura = nroFactura;
    }

    public RecibirTimbradoVentaCallback getCallback() {
        return callback;
    }

    public void setCallback(RecibirTimbradoVentaCallback callback) {
        this.callback = callback;
    }

    public E_Timbrado getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(E_Timbrado timbrado) {
        this.timbrado = timbrado;
    }

    public int obtenerUltimoNroFactura() {
        int idTimbrado = getTimbrado().getId();
        int ultimoNroFactura = DB_Ingreso.obtenerUltimoNroFactura(idTimbrado) + 1;
        int ultimoNroFacturacion = DB_Ingreso.obtenerUltimoNroFacturacion(idTimbrado) + 1;
        if (ultimoNroFactura >= ultimoNroFacturacion) {
            return ultimoNroFactura;
        } else {
            return ultimoNroFacturacion;
        }
    }

    public boolean nroFacturaEnUso(int nroFactura) {
        return DB_Ingreso.nroFacturaEnUso(nroFactura, getTimbrado().getId());
    }
}
