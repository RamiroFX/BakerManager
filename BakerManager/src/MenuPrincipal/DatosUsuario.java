/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MenuPrincipal;

import Entities.M_rol_usuario;

/**
 *
 * @author Ramiro
 */
public class DatosUsuario {
    
    public static M_rol_usuario rol_usuario;

    public DatosUsuario() {
    }

    public static void setRol_usuario(M_rol_usuario rol_usuario) {
        DatosUsuario.rol_usuario = rol_usuario;
    }

    public static M_rol_usuario getRol_usuario() {
        return rol_usuario;
    }
    
    
}
