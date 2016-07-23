/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

import Entities.M_funcionario;
import Entities.M_rol_usuario;
import Login.Login;

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

    public void mostrarLogin() {
        Login login = new Login(controlador);
        login.mostrarVista();
    }
}
