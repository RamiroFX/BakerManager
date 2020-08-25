/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.E_Timbrado;

/**
 *
 * @author Ramiro Ferreira
 */
public interface RecibirTimbradoVentaCallback {

    public void recibirTimbrado(E_Timbrado timbrado);

    public void recibirTimbradoNroFactura(E_Timbrado timbrado, int nroFactura);

}
