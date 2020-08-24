/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import Entities.E_Timbrado;
import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerTimbrado {

    private M_verTimbrado modelo;
    private V_crearTimbrado vista;
    private C_verTimbrado controlador;

    public VerTimbrado(C_inicio inicio) {
        this.modelo = new M_verTimbrado();
        this.vista = new V_crearTimbrado(inicio.vista);
        this.controlador = new C_verTimbrado(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void cargarDatos(E_Timbrado timbrado) {
        this.controlador.cargarDatos(timbrado);
    }
}
