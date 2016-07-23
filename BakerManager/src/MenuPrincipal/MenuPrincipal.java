/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MenuPrincipal;

import bakermanager.C_inicio;

/**
 *
 * @author Usuario
 */
public class MenuPrincipal {

    public V_MenuPrincipal vista;
    public C_MenuPrincipal controlador;

    public MenuPrincipal(C_inicio inicio) {
        this.vista = new V_MenuPrincipal();
        this.controlador = new C_MenuPrincipal(this.vista, inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
