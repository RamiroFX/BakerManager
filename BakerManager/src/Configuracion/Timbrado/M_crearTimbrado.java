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
import MenuPrincipal.DatosUsuario;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearTimbrado {

    private E_Timbrado timbrado;

    public M_crearTimbrado() {
        timbrado = new E_Timbrado();
    }

    /**
     * @return the timbrado
     */
    public E_Timbrado getTimbrado() {
        return timbrado;
    }

    /**
     * @param timbrado the timbrado to set
     */
    public void setTimbrado(E_Timbrado timbrado) {
        this.timbrado = timbrado;
    }

    public void guardarTimbrado() {
        int idFuncionario = DatosUsuario.getRol_usuario().getFuncionario().getIdFuncionario();
        getTimbrado().getCreador().setIdFuncionario(idFuncionario);
        DB_Timbrado.insertarTimbrado(getTimbrado());
    }

    public boolean comprobarTimbradoExistente(int nroTimbrado, int nroSucursal, int nroPVTA, int nroBoletaInicial, int nroBoletaFinal) {
        E_Timbrado unTimbrado = DB_Timbrado.obtenerTimbrado(nroTimbrado, nroSucursal, nroPVTA, nroBoletaInicial, nroBoletaFinal);
        return unTimbrado != null;
    }
    
    public ArrayList<E_impresionPlantilla> getPlantillas() {
        return DB_manager.obtenerImpresionPlantillas(E_impresionPlantilla.FACTURA);
    }
}
