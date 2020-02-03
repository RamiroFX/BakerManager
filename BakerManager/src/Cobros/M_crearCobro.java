/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import ModeloTabla.CtaCteDetalleTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearCobro {

    private E_cuentaCorrienteCabecera ctaCteCabecera;
    private CtaCteDetalleTableModel ctaCteDetalleTm;

    public M_crearCobro() {
        this.ctaCteCabecera = new E_cuentaCorrienteCabecera();
        this.ctaCteCabecera.getFuncionario().setId_funcionario(-1);
        this.ctaCteCabecera.getCliente().setIdCliente(-1);
        this.ctaCteDetalleTm = new CtaCteDetalleTableModel();
    }

    /**
     * @return the ctaCteCabecera
     */
    public E_cuentaCorrienteCabecera getCabecera() {
        return ctaCteCabecera;
    }

    /**
     * @param ctaCteCabecera the ctaCteCabecera to set
     */
    public void setCabecera(E_cuentaCorrienteCabecera ctaCteCabecera) {
        this.ctaCteCabecera = ctaCteCabecera;
    }

    public E_cuentaCorrienteCabecera getCtaCteCabecera() {
        return ctaCteCabecera;
    }

    public void setCtaCteDetalleTm(CtaCteDetalleTableModel ctaCteDetalleTm) {
        this.ctaCteDetalleTm = ctaCteDetalleTm;
    }

    public CtaCteDetalleTableModel getCtaCteDetalleTm() {
        return ctaCteDetalleTm;
    }

    public void limpiarCampos() {
        getCabecera().getCliente().setIdCliente(-1);
        getCabecera().getFuncionario().setId_funcionario(-1);
    }

    public void agregarDatos(E_cuentaCorrienteDetalle data) {
        for (int i = 0; i < getCtaCteDetalleTm().getList().size(); i++) {
            E_cuentaCorrienteDetalle get = getCtaCteDetalleTm().getList().get(i);
            if (get.getIdFacturaCabecera() == data.getIdFacturaCabecera()) {
                getCtaCteDetalleTm().modificarMontoPagar((int) (get.getMonto() + data.getMonto()), i);
                return;
            }
        }
        getCtaCteDetalleTm().agregarDatos(data);
    }

    public boolean controlarMontoIngresado(int idFacturaCabecera, int aPagar, int totalPendiente) {
        for (int i = 0; i < getCtaCteDetalleTm().getList().size(); i++) {
            E_cuentaCorrienteDetalle get = getCtaCteDetalleTm().getList().get(i);
            if (get.getIdFacturaCabecera() == idFacturaCabecera) {
                int subTotalApagar = (int) get.getMonto() + aPagar;
                if (subTotalApagar > totalPendiente) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < getCtaCteDetalleTm().getList().size(); i++) {
            E_cuentaCorrienteDetalle get = getCtaCteDetalleTm().getList().get(i);
            total = total + (int) get.getMonto();
        }
        return total;
    }

}
