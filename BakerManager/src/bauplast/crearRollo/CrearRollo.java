/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearRollo;

import Entities.E_produccionCabecera;
import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearRollo {

    private V_crearRollo vista;
    private M_crearRollo modelo;
    private C_crearRollo controlador;

    public CrearRollo(C_inicio c_inicio) {
        this.modelo = new M_crearRollo();
        this.vista = new V_crearRollo(c_inicio.vista);
        this.controlador = new C_crearRollo(modelo, vista);
    }

    public void cargarDatos(E_produccionCabecera pc) {
        this.controlador.cargarDatos(pc);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
