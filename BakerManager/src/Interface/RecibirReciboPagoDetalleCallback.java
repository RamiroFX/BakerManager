/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.E_reciboPagoDetalle;

/**
 *
 * @author Ramiro Ferreira
 */
public interface RecibirReciboPagoDetalleCallback {

    public void recibirReciboPagoDetalle(E_reciboPagoDetalle detalle, int montoTotalPendiente);

    public void modificarReciboPagoDetalle(int index, E_reciboPagoDetalle detalle, int montoTotalPendiente);
}
