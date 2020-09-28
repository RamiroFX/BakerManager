/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import Entities.E_productoClasificacion;
import Entities.ProductoCategoria;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.InterfaceRecibirProduccionTerminados;
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

    public void setProductoClasificacion(ProductoCategoria pc) {
        this.modelo.setPc(pc);
    }

    public void setCallback(InterfaceRecibirProduccionFilm callback) {
        this.controlador.setRolloCallback(callback);
    }

    public void setProductoTerminadoCallback(InterfaceRecibirProduccionTerminados callback) {
        this.controlador.setProductoTerminadoCallback(callback);
    }

    public void setProductoCallback(RecibirProductoCallback callback) {
        this.controlador.setProductoCallback(callback);
    }
    
}
