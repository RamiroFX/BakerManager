/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import Entities.E_clienteProducto;
import Entities.E_cuentaCorrienteCabecera;
import Entities.M_cliente;
import ModeloTabla.CtaCteDetalleTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_relacionarAnticipo {

    M_cliente cliente;
    E_cuentaCorrienteCabecera cabecera;
    CtaCteDetalleTableModel tm;

    public M_relacionarAnticipo() {
        this.cliente = new M_cliente();
        this.cliente.setIdCliente(-1);
        this.tm = new CtaCteDetalleTableModel();
    }

    public M_cliente getCliente() {
        return cliente;
    }

    public E_cuentaCorrienteCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(E_cuentaCorrienteCabecera cabecera) {
        this.cabecera = cabecera;
    }

    public CtaCteDetalleTableModel getTm() {
        return tm;
    }

    public void setCliente(M_cliente cliente) {
        this.cliente = cliente;
    }

}
