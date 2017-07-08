package Caja;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionCaja {

    C_gestionCaja controlador;
    M_gestionCaja modelo;
    V_gestionCaja vista;

    public GestionCaja() {
        this.modelo = new M_gestionCaja();
        this.vista = new V_gestionCaja();
        this.controlador = new C_gestionCaja(vista, modelo);
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }
}
