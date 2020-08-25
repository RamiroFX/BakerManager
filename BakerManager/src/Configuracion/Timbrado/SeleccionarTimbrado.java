/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import Interface.RecibirTimbradoVentaCallback;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarTimbrado {

    M_seleccionarTimbrado modelo;
    V_seleccionarTimbrado vista;
    C_seleccionarTimbrado controlador;

    public SeleccionarTimbrado(JDialog dialog, RecibirTimbradoVentaCallback callback) {
        this.modelo = new M_seleccionarTimbrado();
        this.vista = new V_seleccionarTimbrado(dialog);
        this.controlador = new C_seleccionarTimbrado(this.modelo, this.vista, callback);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
