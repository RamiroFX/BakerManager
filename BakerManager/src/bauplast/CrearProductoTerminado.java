/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import Entities.E_produccionCabecera;
import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearProductoTerminado {

    private V_crearProductoTerminado vista;
    private M_crearProductoTerminado modelo;
    private C_crearProductoTerminado controlador;

    public CrearProductoTerminado(C_inicio c_inicio) {
        this.modelo = new M_crearProductoTerminado();
        this.vista = new V_crearProductoTerminado(c_inicio.vista);
        this.controlador = new C_crearProductoTerminado(modelo, vista);
    }

    public void cargarDatos(E_produccionCabecera pc) {
        this.controlador.cargarDatos(pc);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
