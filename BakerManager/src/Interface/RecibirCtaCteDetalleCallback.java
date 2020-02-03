/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.E_cuentaCorrienteDetalle;

/**
 *
 * @author Ramiro Ferreira
 */
public interface RecibirCtaCteDetalleCallback {

    public void recibirCtaCteDetalle(E_cuentaCorrienteDetalle detalle, int montoTotalPendiente);

    public void modificarCtaCteDetalle(int index,E_cuentaCorrienteDetalle detalle, int montoTotalPendiente);
}
