/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.EstadoCuenta;

import DB.DB_Cliente;
import ModeloTabla.EstadoCuentaClienteTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_estadoCuenta {

    EstadoCuentaClienteTableModel cabeceraTableModel;

    public M_estadoCuenta() {
        cabeceraTableModel = new EstadoCuentaClienteTableModel();
    }

    public EstadoCuentaClienteTableModel obtenerTableModel() {
        return cabeceraTableModel;
    }

    public void obtenerEstadoCuenta(int idCliente) {
        this.cabeceraTableModel.setList(DB_Cliente.obtenerEstadoCuenta(idCliente));
    }
}
