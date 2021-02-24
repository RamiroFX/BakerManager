/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import Egresos.C_crear_egreso;
import Interface.RecibirProductoCallback;
import Pedido.C_crearPedido;
import Pedido.C_verPedido;
import Ventas.C_crearVentaRapida;
import Ventas.Mesas.C_verMesa;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarProducto {

    V_seleccionarProducto vista;
    C_seleccionarProducto controlador;

    public SeleccionarProducto(JDialog main, C_crear_egreso egresos) {
        vista = new V_seleccionarProducto(main);
        controlador = new C_seleccionarProducto(vista, egresos);
    }

    public SeleccionarProducto(C_verMesa verMesa) {
        vista = new V_seleccionarProducto(verMesa.vista);
        controlador = new C_seleccionarProducto(vista, verMesa);
    }

    public SeleccionarProducto(JDialog main, RecibirProductoCallback callback) {
        vista = new V_seleccionarProducto(main);
        controlador = new C_seleccionarProducto(vista, callback);
    }

    public SeleccionarProducto(JDialog main, RecibirProductoCallback callback, int tipo) {
        vista = new V_seleccionarProducto(main);
        controlador = new C_seleccionarProducto(vista, callback, tipo);
    }

    public void mostrarVista() {
        controlador.mostrarVista();
    }

    public void activarModoCreacion() {
        this.controlador.activarModoCreacion();
    }
}
