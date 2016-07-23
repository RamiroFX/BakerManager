/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

import Entities.M_rol_usuario;

/**
 *
 * @author Ramiro
 */
public class M_inicio {

    private M_rol_usuario rol_usuario;

    public M_inicio() {
        this.rol_usuario = new M_rol_usuario();
    }

    /**
     * @return the rol_usuario
     */
    public M_rol_usuario getRol_usuario() {
        return rol_usuario;
    }

    /**
     * @param rol_usuario the rol_usuario to set
     */
    public void setRol_usuario(M_rol_usuario rol_usuario) {
        this.rol_usuario = rol_usuario;
    }

}
