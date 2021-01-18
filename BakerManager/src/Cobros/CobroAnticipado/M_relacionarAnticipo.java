/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import DB.DB_Cobro;
import Entities.E_clienteProducto;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaSinPago;
import Entities.E_formaPago;
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

    public void agregarDatos(E_cuentaCorrienteDetalle data) {
        for (int i = 0; i < getTm().getList().size(); i++) {
            E_cuentaCorrienteDetalle get = getTm().getList().get(i);
            if (get.getIdFacturaCabecera() == data.getIdFacturaCabecera()) {
                if (get.getFormaPago().getId() == data.getFormaPago().getId()
                        && data.getFormaPago().getId() == E_formaPago.EFECTIVO
                        && get.getFormaPago().getId() == E_formaPago.EFECTIVO) {
                    getTm().modificarMontoPagar((int) (get.getMonto() + data.getMonto()), i);
                    return;
                }
            }
        }
        getTm().agregarDatos(data);
    }

    public E_facturaSinPago obtenerDetalleVenta(int idFacturaCabecera) {
        return DB_Cobro.obtenerFacturaSinPagoPorId(idFacturaCabecera);
    }

    public double obtenerSumaDetalle(int idFacturaCabecera) {
        for (E_cuentaCorrienteDetalle unDetalle : getTm().getList()) {
            if (unDetalle.getIdFacturaCabecera() == idFacturaCabecera) {
                return unDetalle.getMonto();
            }
        }
        return -1;
    }
}
