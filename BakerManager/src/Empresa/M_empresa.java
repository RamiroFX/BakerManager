/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Empresa;

import DB.DB_manager;
import Entities.E_Empresa;

/**
 *
 * @author Ramiro
 */
public class M_empresa {

    private E_Empresa empresa;

    public M_empresa() {
        this.empresa = DB_manager.obtenerDatosEmpresa();
    }

    public E_Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(E_Empresa empresa) {
        this.empresa = empresa;
    }

    public int modificarDatosEmpresa(E_Empresa empresa) {
        return DB_manager.modificarDatosEmpresa(empresa);
    }

    public String getDireccionEmpresa() {
        String dir = "";
        if (getEmpresa().getDireccion() != null) {
            dir = getEmpresa().getDireccion();
        }
        return dir;
    }
}
