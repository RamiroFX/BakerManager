/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import Entities.E_productoClasificacion;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.RecibirProductoCallback;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarProductoPorClasif {

    M_seleccionarProductoPorClasif modelo;
    V_seleccionarProductoPorClasif vista;
    C_seleccionarProductoPorClasif controlador;

    public SeleccionarProductoPorClasif(JDialog dialog) {
        this.modelo = new M_seleccionarProductoPorClasif();
        this.vista = new V_seleccionarProductoPorClasif(dialog);
        this.controlador = new C_seleccionarProductoPorClasif(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void setProductoClasificacion(E_productoClasificacion pc) {
        this.modelo.setPc(pc);
    }

    public void setCallback(InterfaceRecibirProduccionFilm callback) {
        this.controlador.setCallback(callback);
    }

    public void setProductoCallback(RecibirProductoCallback callback) {
        this.controlador.setProductoCallback(callback);
    }
}
