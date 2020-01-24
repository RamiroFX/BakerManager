/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsoMateriaPrima;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerUtilizacion {

    private V_utilizarMateriaPrima vista;
    private M_verUtilizacion modelo;
    private C_verUtilizacion controlador;

    public VerUtilizacion(C_inicio c_inicio) {
        this.modelo = new M_verUtilizacion();
        this.vista = new V_utilizarMateriaPrima(c_inicio.vista);
        this.controlador = new C_verUtilizacion(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void verRegistro(int idUtilizacionMP) {
        this.controlador.verRegistro(idUtilizacionMP);
    }
}
