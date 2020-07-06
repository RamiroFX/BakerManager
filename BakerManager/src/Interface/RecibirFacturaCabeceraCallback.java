/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.E_facturaCabecera;
import Entities.E_facturaDetalle;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public interface RecibirFacturaCabeceraCallback {

    public void recibirVenta(E_facturaCabecera facturaCabecera, List<E_facturaDetalle> facturaDetalle);

    public void recibirFacturaCabecera(E_facturaCabecera facturaCabecera);

    public void recibirFacturaDetalle(List<E_facturaDetalle> facturaDetalle);

}
