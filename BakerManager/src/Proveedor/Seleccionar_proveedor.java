/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import Egresos.C_buscar_detalle;
import Egresos.C_crear_egreso;
import Egresos.C_gestionEgresos;
import Producto.C_crear_producto;
import Producto.C_gestion_producto;
import Producto.C_seleccionarProducto;
import Producto.Crear_producto_proveedor;
import javax.swing.JDialog;

/**
 *
 * @author RamiroFX
 */
public class Seleccionar_proveedor {

    V_seleccionar_proveedor vista;
    C_seleccionar_proveedor controlador;

    public Seleccionar_proveedor(JDialog main, C_crear_egreso egresos) {
        vista = new V_seleccionar_proveedor(main);
        controlador = new C_seleccionar_proveedor(vista, egresos);
    }

    public Seleccionar_proveedor(Crear_producto_proveedor main, Crear_producto_proveedor productoProveedor) {
        vista = new V_seleccionar_proveedor(main);
        controlador = new C_seleccionar_proveedor(vista, productoProveedor);
    }

    public Seleccionar_proveedor(C_gestionEgresos gestionEgreso) {
        vista = new V_seleccionar_proveedor(gestionEgreso.c_inicio.vista);
        controlador = new C_seleccionar_proveedor(vista, gestionEgreso);
    }

    public Seleccionar_proveedor(C_buscar_detalle buscarDetalleEgreso) {
        vista = new V_seleccionar_proveedor(buscarDetalleEgreso.c_inicio.vista);
        controlador = new C_seleccionar_proveedor(vista, buscarDetalleEgreso);
    }

    public Seleccionar_proveedor(C_gestion_producto gestionProducto) {
        vista = new V_seleccionar_proveedor(gestionProducto.c_inicio.vista);
        controlador = new C_seleccionar_proveedor(vista, gestionProducto);
    }

    public Seleccionar_proveedor(C_crear_producto crearProducto) {
        try {

            vista = new V_seleccionar_proveedor(crearProducto.c_producto.c_inicio.vista);
        } catch (Exception e) {
            vista = new V_seleccionar_proveedor(crearProducto.vista);
        }
        controlador = new C_seleccionar_proveedor(vista, crearProducto);
    }

    public Seleccionar_proveedor(C_seleccionarProducto seleccionarProducto) {
        vista = new V_seleccionar_proveedor(seleccionarProducto.vista);
        controlador = new C_seleccionar_proveedor(vista, seleccionarProducto);
    }

    public void mostrarVista() {
        controlador.mostrarVista();
    }
}
