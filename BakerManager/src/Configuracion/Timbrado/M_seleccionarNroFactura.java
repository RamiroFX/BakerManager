/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import Entities.E_Timbrado;
import Interface.RecibirTimbradoVentaCallback;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarNroFactura {

    private RecibirTimbradoVentaCallback callback;
    private E_Timbrado timbrado;

    public M_seleccionarNroFactura() {
        this.timbrado = new E_Timbrado();
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

}
