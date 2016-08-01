/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

/**
 *
 * @author Ramiro Ferreira
 */
public class Modificar_proveedor {

    private M_modificar_proveedor modelo;
    private V_crear_proveedor vista;
    private C_modificar_proveedor controlador;

    public Modificar_proveedor(C_gestion_proveedores gestionProveedores, int idProveedor) {
        this.modelo = new M_modificar_proveedor(idProveedor);
        this.vista = new V_crear_proveedor(gestionProveedores.c_inicio.vista, false);
        this.controlador = new C_modificar_proveedor(this.modelo, this.vista, gestionProveedores);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
