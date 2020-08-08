/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import Entities.M_cliente;
import Interface.RecibirFacturaSinPagoCallback;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarVenta {

    private M_seleccionarVenta modelo;
    private V_seleccionarVenta vista;
    private C_seleccionarVenta controlador;

    public SeleccionarVenta(JDialog dialog) {
        this.modelo = new M_seleccionarVenta();
        this.vista = new V_seleccionarVenta(dialog);
        this.controlador = new C_seleccionarVenta(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void setCallback(RecibirFacturaSinPagoCallback callback) {
        this.controlador.setCallback(callback);
    }

    public void setCliente(M_cliente cliente) {
        this.modelo.getCabecera().setCliente(cliente);
    }
}
