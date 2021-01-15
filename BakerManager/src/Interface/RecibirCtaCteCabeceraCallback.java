/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.E_cuentaCorrienteCabecera;

/**
 *
 * @author Ramiro Ferreira
 */
public interface RecibirCtaCteCabeceraCallback {

    public void recibirCtaCteCabecera(E_cuentaCorrienteCabecera cabecera);

    public void modificarCtaCteCabecera(int index, E_cuentaCorrienteCabecera cabecera);
}
