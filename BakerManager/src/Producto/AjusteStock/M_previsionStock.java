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
public class M_previsionStock {

    private AjusteStockCabeceraTableModel tmCabecera;
    private AjusteStockDetalleTableModel tmDetalle;

    public M_previsionStock() {
        this.tmCabecera = new AjusteStockCabeceraTableModel();
        this.tmDetalle = new AjusteStockDetalleTableModel();
    }

    public AjusteStockCabeceraTableModel getTmCabecera() {
        return tmCabecera;
    }

    public AjusteStockDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    
}
