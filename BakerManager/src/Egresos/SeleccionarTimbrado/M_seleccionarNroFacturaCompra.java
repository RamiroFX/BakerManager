/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos.SeleccionarTimbrado;

import DB.DB_Egreso;
import Entities.E_Timbrado;
import Interface.RecibirTimbradoVentaCallback;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarNroFacturaCompra {

    private RecibirTimbradoVentaCallback callback;
    private int idProveedor;
    private int nroFactura;
    private E_Timbrado timbrado;

    public M_seleccionarNroFacturaCompra(int idProveedor) {
        this.timbrado = new E_Timbrado();
        this.timbrado.setNroSucursal(-1);
        this.timbrado.setNroPuntoVenta(-1);
        this.timbrado.setNroTimbrado(-1);
        this.idProveedor = idProveedor;
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

    public boolean nroFacturaEnUso(int nroFactura) {
        return DB_Egreso.existeProveedorNroFactura(idProveedor, getTimbrado().getNroTimbrado(),
                getTimbrado().getNroSucursal(), getTimbrado().getNroPuntoVenta(), nroFactura);
    }
}
