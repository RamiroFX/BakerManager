/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Pedido.C_crearPedido;
import Pedido.C_gestionPedido;
import Pedido.C_verPedido;
import Ventas.C_crearVentaRapida;
import Ventas.C_gestionVentas;
import Ventas.C_verMesa;
import Ventas.ConfigurarMesa;

/**
 *
 * @author Ramiro Ferreira
 */
public class Seleccionar_cliente {

    V_seleccionar_cliente vista;
    C_seleccionar_cliente controlador;

    public Seleccionar_cliente(C_crearVentaRapida crearVenta) {
        vista = new V_seleccionar_cliente(crearVenta.vista);
        controlador = new C_seleccionar_cliente(vista, crearVenta);
    }

    public Seleccionar_cliente(C_gestionVentas gestion_venta) {
        vista = new V_seleccionar_cliente(gestion_venta.c_inicio.vista);
        controlador = new C_seleccionar_cliente(vista, gestion_venta);
    }

    public Seleccionar_cliente(ConfigurarMesa confMesa) {
        vista = new V_seleccionar_cliente(confMesa);
        controlador = new C_seleccionar_cliente(vista, confMesa);
    }

    public Seleccionar_cliente(C_verMesa verMesa) {
        vista = new V_seleccionar_cliente(verMesa.vista);
        controlador = new C_seleccionar_cliente(vista, verMesa);
    }

    public Seleccionar_cliente(C_gestionPedido gestionPedido) {
        vista = new V_seleccionar_cliente(gestionPedido.c_inicio.vista);
        controlador = new C_seleccionar_cliente(vista, gestionPedido);
    }

    public Seleccionar_cliente(C_crearPedido crearPedido) {
        vista = new V_seleccionar_cliente(crearPedido.vista);
        controlador = new C_seleccionar_cliente(vista, crearPedido);
    }

    public Seleccionar_cliente(C_verPedido verPedido) {
        vista = new V_seleccionar_cliente(verPedido.vista);
        controlador = new C_seleccionar_cliente(vista, verPedido);
    }

    public void mostrarVista() {
        controlador.mostrarVista();
    }
}
