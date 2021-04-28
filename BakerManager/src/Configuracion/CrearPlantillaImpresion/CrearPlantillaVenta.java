/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.CrearPlantillaImpresion;

import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearPlantillaVenta {

    private M_crearPlantillaVenta modelo;
    private V_crearPlantillaVenta vista;
    private C_crearPlantillaVenta controlador;

    public CrearPlantillaVenta(JDialog dialog) {
        modelo = new M_crearPlantillaVenta();
        vista = new V_crearPlantillaVenta(dialog);
        controlador = new C_crearPlantillaVenta(vista, modelo);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
