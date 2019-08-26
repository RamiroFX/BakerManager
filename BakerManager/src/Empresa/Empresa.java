/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Empresa;

import Configuracion.C_configuracion;
import Configuracion.M_configuracion;
import Configuracion.V_configuracion;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro
 */
public class Empresa {

    private V_empresa vista;
    private C_empresa controlador;
    private M_empresa modelo;

    public Empresa(JFrame frame) {
        this.modelo = new M_empresa();
        this.vista = new V_empresa(frame);
        this.controlador = new C_empresa(vista, modelo);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
