/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

import DB.DB_manager;
import Entities.M_rol;
import Entities.M_rol_usuario;
import Login.Login;
import Login.M_login;
import MenuPrincipal.DatosUsuario;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class Inicio {

    M_inicio modelo;
    V_inicio vista;
    C_inicio controlador;

    public Inicio() {
        vista = new V_inicio();
        modelo = new M_inicio();
        controlador = new C_inicio(vista, modelo);
        controlador.mostrarVista();
    }

    /*
    Se utiliza solo para testeos
     */
    public void conectarBD() {
        try {
            DB_manager.conectarBD("postgres", "postgres");
        } catch (SQLException ex) {
            Logger.getLogger(M_login.class.getName()).log(Level.SEVERE, null, ex);
        }
        M_rol_usuario ru = new M_rol_usuario();
        ru.setRolActual(new M_rol(1, "Administrador"));
        DatosUsuario.setRol_usuario(ru);
    }

    public void mostrarLogin() {
        Login login = new Login(controlador);
        login.mostrarVista();
    }
}
