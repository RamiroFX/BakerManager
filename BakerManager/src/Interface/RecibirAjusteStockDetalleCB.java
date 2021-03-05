/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.E_ajusteStockMotivo;
import Entities.M_producto;
import java.util.Date;

/**
 *
 * @author Ramiro
 */
public interface RecibirAjusteStockDetalleCB {

    public void recibirAjusteStock(M_producto producto, double cantidadVieja, double cantidadNueva, E_ajusteStockMotivo motivo, Date tiempo, String observacion);

    public void modificarAjusteStock(int index, M_producto producto, double cantidadVieja, double cantidadNueva, E_ajusteStockMotivo motivo, Date tiempo, String observacion);

}
