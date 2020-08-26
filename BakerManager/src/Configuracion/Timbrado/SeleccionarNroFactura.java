/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import Entities.E_Timbrado;
import Interface.RecibirTimbradoVentaCallback;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarNroFactura {

    private M_seleccionarNroFactura modelo;
    private V_selecionarNroFactura vista;
    private C_seleccionarNroFactura controlador;

    public SeleccionarNroFactura(JDialog dialog, RecibirTimbradoVentaCallback callback, E_Timbrado timbrado) {
        this.modelo = new M_seleccionarNroFactura();
        this.vista = new V_selecionarNroFactura(dialog);
        this.controlador = new C_seleccionarNroFactura(modelo, vista, callback, timbrado);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
