/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Cobros.EstadoCuenta.C_estadoCuenta;
import Cobros.EstadoCuenta.M_estadoCuenta;
import Cobros.EstadoCuenta.V_estadoCuenta;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class EstadoCuentaCliente {

    private M_estadoCuenta modelo;
    private V_estadoCuenta vista;
    private C_estadoCuenta controlador;

    public EstadoCuentaCliente(JFrame frame) {
        this.modelo = new M_estadoCuenta();
        this.vista = new V_estadoCuenta(frame);
        this.controlador = new C_estadoCuenta(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
