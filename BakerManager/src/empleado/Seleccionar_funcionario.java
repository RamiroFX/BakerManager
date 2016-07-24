/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package empleado;

import Egresos.C_buscar_detalle;
import Egresos.C_gestionEgresos;
import Pedido.C_gestionPedido;
import Ventas.C_gestionVentas;

/**
 *
 * @author Ramiro Ferreira
 */
public class Seleccionar_funcionario {

    private V_seleccionar_funcionario vista;
    private C_seleccionar_funcionario controlador;

    public Seleccionar_funcionario(C_gestionVentas gestionVenta) {
        this.vista = new V_seleccionar_funcionario(gestionVenta.c_inicio.vista);
        this.controlador = new C_seleccionar_funcionario(vista, gestionVenta);
    }
    
    public Seleccionar_funcionario(C_gestionEgresos gestionEgresos) {
        this.vista = new V_seleccionar_funcionario(gestionEgresos.c_inicio.vista);
        this.controlador = new C_seleccionar_funcionario(vista, gestionEgresos);
    }
    
    public Seleccionar_funcionario(C_buscar_detalle buscarDetalleEgreso) {
        this.vista = new V_seleccionar_funcionario(buscarDetalleEgreso.c_inicio.vista);
        this.controlador = new C_seleccionar_funcionario(vista, buscarDetalleEgreso);
    }
    
    public Seleccionar_funcionario(C_gestionPedido gestionPedido) {
        this.vista = new V_seleccionar_funcionario(gestionPedido.c_inicio.vista);
        this.controlador = new C_seleccionar_funcionario(vista, gestionPedido);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
