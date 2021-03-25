/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.SeleccionAjusteStockDetalle;

/**
 *
 * @author Ramiro
 */
public interface RecibirAjusteStockDetalleCB {

    public void recibirAjusteStock(SeleccionAjusteStockDetalle ajusteStockDetalle);

    public void modificarAjusteStock(int index, SeleccionAjusteStockDetalle ajusteStockDetalle);
    
}
