/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import Entities.E_produccionCabecera;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.InterfaceRecibirProduccionTerminados;
import Interface.RecibirProductoCallback;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarProduccion {

    private M_seleccionarProduccion modelo;
    private V_seleccionarProduccion vista;
    private C_seleccionarProduccion controlador;

    public SeleccionarProduccion(JDialog dialog, boolean esModoCreacion) {
        this.modelo = new M_seleccionarProduccion();
        this.vista = new V_seleccionarProduccion(dialog);
        this.controlador = new C_seleccionarProduccion(this.modelo, this.vista, esModoCreacion);
    }

    public void establecerProduccionCabecera(E_produccionCabecera produccionCabecera) {
        this.modelo.setProduccionCabecera(produccionCabecera);
    }

    public void setRolloCallback(InterfaceRecibirProduccionFilm callback) {
        this.controlador.setRolloCallback(callback);
    }

    public void setProductoTerminadoCallback(InterfaceRecibirProduccionTerminados callback) {
        this.controlador.setProductoTerminadoCallback(callback);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
