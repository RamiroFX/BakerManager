/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Interface.RecibirClienteCallback;
import Pedido.C_crearPedido;
import Pedido.C_gestionPedido;
import Pedido.C_verPedido;
import Ventas.C_crearVentaRapida;
import Ventas.C_gestionVentas;
import Ventas.C_buscar_venta_detalle;
import Ventas.C_verMesa;
import Ventas.ConfigurarMesa;
import bakermanager.C_inicio;
import bakermanager.V_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class Seleccionar_cliente {

    V_seleccionar_cliente vista;
    C_seleccionar_cliente controlador;

    public Seleccionar_cliente(V_inicio v_inicio) {
        this.vista = new V_seleccionar_cliente(v_inicio);
        this.controlador = new C_seleccionar_cliente(vista);
    }

    public void setCallback(RecibirClienteCallback rccb) {
        this.controlador.setCallback(rccb);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
