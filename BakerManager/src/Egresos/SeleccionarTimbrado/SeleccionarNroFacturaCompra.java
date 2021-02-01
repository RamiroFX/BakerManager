/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos.SeleccionarTimbrado;

import Entities.E_Timbrado;
import Interface.RecibirTimbradoVentaCallback;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarNroFacturaCompra {

    private M_seleccionarNroFacturaCompra modelo;
    private V_selecionarNroFacturaCompra vista;
    private C_seleccionarNroFacturaCompra controlador;

    public SeleccionarNroFacturaCompra(JDialog dialog, int idProveedor, RecibirTimbradoVentaCallback callback) {
        this.modelo = new M_seleccionarNroFacturaCompra(idProveedor);
        this.vista = new V_selecionarNroFacturaCompra(dialog);
        this.controlador = new C_seleccionarNroFacturaCompra(modelo, vista, callback);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
