/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import DB.DB_Timbrado;
import DB.DB_manager;
import Entities.E_Timbrado;
import Entities.E_impresionPlantilla;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verTimbrado {

    private E_Timbrado cabecera;

    public M_verTimbrado() {
        this.cabecera = new E_Timbrado();
    }

    public E_Timbrado getCabecera() {
        return cabecera;
    }

    public void cargarDatos(E_Timbrado timbrado) {
        consultarTimbrado(timbrado.getId());
    }

    public void consultarTimbrado(int idTimbrado) {
        this.cabecera = DB_Timbrado.obtenerTimbrado(idTimbrado);
    }

    public ArrayList<E_impresionPlantilla> getPlantillas() {
        return DB_manager.obtenerImpresionPlantillas(E_impresionPlantilla.FACTURA);
    }

    public void actualizarPlantillaImpresion(int idImpresionPlantilla) {
        DB_Timbrado.actualizarPlantillaImpresion(idImpresionPlantilla, cabecera.getId());
    }
}
