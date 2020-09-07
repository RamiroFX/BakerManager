/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import bakermanager.C_inicio;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerIngreso {

    private M_verIngreso modelo;
    public V_crearVentaRapida vista;
    public C_ver_ingreso controlador;

    public VerIngreso(C_inicio c_inicio, Integer idIngresoCabecera, boolean esModoCreacion) {
        this.modelo = new M_verIngreso();
        this.vista = new V_crearVentaRapida(c_inicio.vista, esModoCreacion);
        this.controlador = new C_ver_ingreso(this.modelo, this.vista, idIngresoCabecera);
    }

    public VerIngreso(JDialog dialog, Integer idIngresoCabecera, boolean esModoCreacion) {
        this.modelo = new M_verIngreso();
        this.vista = new V_crearVentaRapida(dialog, esModoCreacion);
        this.controlador = new C_ver_ingreso(this.modelo, this.vista, idIngresoCabecera);
    }

    /*
    public Ver_ingreso(JDialog c_inicio, Integer idIngresoCabecera) {
        this.modelo = new M_verIngreso();
        this.vista = new V_crearVentaRapida(c_inicio);
        this.controlador = new C_ver_ingreso(this.modelo, this.vista, idIngresoCabecera);
    }

    public Ver_ingreso(Integer nroFactura, int idTimbrado, JDialog c_inicio) {
        this.modelo = new M_verIngreso();
        this.vista = new V_crearVentaRapida(c_inicio);
        this.controlador = new C_ver_ingreso(this.modelo, this.vista, nroFactura, idTimbrado);
    }
     */
    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
