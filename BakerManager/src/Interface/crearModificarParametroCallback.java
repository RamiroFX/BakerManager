/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.M_campoImpresion;

/**
 *
 * @author Ramiro Ferreira
 */
public interface crearModificarParametroCallback {

    public void recibirParametroImpresion(M_campoImpresion ci);

    public void modificarParametroImpresion(M_campoImpresion ci);
}
