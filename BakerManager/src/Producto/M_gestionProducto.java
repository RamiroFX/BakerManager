/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import Entities.M_menu_item;
import MenuPrincipal.DatosUsuario;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionProducto {

    ArrayList<M_menu_item> accesos;

    public M_gestionProducto() {
        this.accesos = DatosUsuario.getRol_usuario().getAccesos();
    }

    public ArrayList<M_menu_item> getAccesos() {
        return accesos;
    }
}
