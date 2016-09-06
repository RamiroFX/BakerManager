/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Empleado;

import Egresos.C_buscar_detalle;
import Egresos.C_gestionEgresos;
import Pedido.C_gestionPedido;
import Ventas.C0_gestionVentas;
import Ventas.C_buscar_venta_detalle;

/**
 *
 * @author Ramiro Ferreira
 */
public class Seleccionar_funcionario {

    private V_seleccionar_funcionario vista;
    private C_seleccionar_funcionario controlador;

    public Seleccionar_funcionario(C0_gestionVentas gestionVenta) {
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
    
    public Seleccionar_funcionario(C_buscar_venta_detalle buscarVentaDetalle) {
        this.vista = new V_seleccionar_funcionario(buscarVentaDetalle.c_inicio.vista);
        this.controlador = new C_seleccionar_funcionario(vista, buscarVentaDetalle);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
