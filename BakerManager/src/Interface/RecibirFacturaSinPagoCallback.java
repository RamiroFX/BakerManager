/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.E_facturaDetalle;
import Entities.E_facturaSinPago;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public interface RecibirFacturaSinPagoCallback {

    public void recibirVentaPendientePago(E_facturaSinPago facturaCabecera, List<E_facturaDetalle> facturaDetalle);

    public void recibirFacturaCabeceraPendientePago(E_facturaSinPago facturaCabecera);

    public void recibirFacturaDetallePendientePago(List<E_facturaDetalle> facturaDetalle);
}
