/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import ModeloTabla.AjusteStockCabeceraTableModel;
import ModeloTabla.AjusteStockDetalleTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionAjusteStock {

    private AjusteStockCabeceraTableModel tmCabecera;
    private AjusteStockDetalleTableModel tmDetalle;

    public M_gestionAjusteStock() {
        this.tmCabecera = new AjusteStockCabeceraTableModel();
        this.tmDetalle = new AjusteStockDetalleTableModel();
    }

    public AjusteStockCabeceraTableModel getTmCabecera() {
        return tmCabecera;
    }

    public AjusteStockDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    void borrarDatos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
