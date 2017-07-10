package Caja;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionCaja {

    C_gestionCaja controlador;
    M_gestionCaja modelo;
    V_gestionCaja vista;

    public GestionCaja(C_inicio inicio) {
        this.modelo = new M_gestionCaja();
        this.vista = new V_gestionCaja();
        this.controlador = new C_gestionCaja(vista, modelo, inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
