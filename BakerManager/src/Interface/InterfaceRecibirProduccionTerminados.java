/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.E_produccionDetalle;

/**
 *
 * @author Ramiro Ferreira
 */
public interface InterfaceRecibirProduccionTerminados {

    public void recibirProductoTerminado(E_produccionDetalle detalle);

    public void modificarFilm(int index, E_produccionDetalle detalle);

}
