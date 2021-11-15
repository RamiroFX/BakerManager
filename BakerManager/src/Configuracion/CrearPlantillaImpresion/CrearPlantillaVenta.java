/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.CrearPlantillaImpresion;

import Interface.InterfaceNotificarCambio;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearPlantillaVenta {

    private M_crearPlantillaVenta modelo;
    private V_crearPlantillaVenta vista;
    private C_crearPlantillaVenta controlador;

    public CrearPlantillaVenta(JDialog dialog, int tipoPlantilla) {
        modelo = new M_crearPlantillaVenta(tipoPlantilla);
        vista = new V_crearPlantillaVenta(dialog);
        controlador = new C_crearPlantillaVenta(vista, modelo);
    }

    public void setInterface(InterfaceNotificarCambio avisarCambioInterface){
        this.modelo.setAvisarCambioInterface(avisarCambioInterface);
    }
    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
