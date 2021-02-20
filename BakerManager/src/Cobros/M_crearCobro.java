/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Cobro;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_formaPago;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.CtaCteDetalleTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearCobro {

    private E_cuentaCorrienteCabecera ctaCteCabecera;
    private CtaCteDetalleTableModel ctaCteDetalleTm;

    public M_crearCobro() {
        this.ctaCteCabecera = new E_cuentaCorrienteCabecera();
        this.ctaCteCabecera.getCobrador().setId(-1);
        this.ctaCteCabecera.setFuncionario(DatosUsuario.getRol_usuario().getFuncionario());
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
        getCabecera().getCobrador().setId(-1);
        getCtaCteDetalleTm().vaciarLista();
    }

    public void agregarDatos(E_cuentaCorrienteDetalle data) {
        for (int i = 0; i < getCtaCteDetalleTm().getList().size(); i++) {
            E_cuentaCorrienteDetalle get = getCtaCteDetalleTm().getList().get(i);
            if (get.getIdFacturaCabecera() == data.getIdFacturaCabecera()) {
                if (get.getFormaPago().getId() == data.getFormaPago().getId()
                        && data.getFormaPago().getId() == E_formaPago.EFECTIVO
                        && get.getFormaPago().getId() == E_formaPago.EFECTIVO) {
                    getCtaCteDetalleTm().modificarMontoPagar((int) (get.getMonto() + data.getMonto()), i);
                    return;
                }
            }
        }
        getCtaCteDetalleTm().agregarDatos(data);
    }

    public void eliminarDatos(int index) {
        getCtaCteDetalleTm().quitarDatos(index);
    }

    public boolean controlarMontoIngresado(int idFacturaCabecera, int aPagar, int totalPendiente) {
        //OBTENER EL TOTAL DE LA FACTURA ACUMULADO EN LA OPERACION DE COBRO
        int totalFactura = 0;
        for (E_cuentaCorrienteDetalle ctaCteDetalle : getCtaCteDetalleTm().getList()) {
            if (ctaCteDetalle.getIdFacturaCabecera() == idFacturaCabecera) {
                totalFactura = totalFactura + (int) ctaCteDetalle.getMonto();
            }
        }
        //VERIFICAR QUE EL NUEVO MONTO INGRESADO PARA LA MISMA FACTURA NO PASE
        //EL MONTO TOTAL
        for (int i = 0; i < getCtaCteDetalleTm().getList().size(); i++) {
            E_cuentaCorrienteDetalle get = getCtaCteDetalleTm().getList().get(i);
            if (get.getIdFacturaCabecera() == idFacturaCabecera) {
                int subTotalApagar = totalFactura + aPagar;
                if (subTotalApagar > totalPendiente) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean controlarMontoModificado(int idFacturaCabecera, int aPagar, int totalPendiente) {
        //OBTENER EL TOTAL DE LA FACTURA ACUMULADO EN LA OPERACION DE COBRO
        int totalFactura = 0;
        for (E_cuentaCorrienteDetalle ctaCteDetalle : getCtaCteDetalleTm().getList()) {
            if (ctaCteDetalle.getIdFacturaCabecera() == idFacturaCabecera) {
                totalFactura = totalFactura + (int) ctaCteDetalle.getMonto();
            }
        }
        //VERIFICAR QUE EL NUEVO MONTO INGRESADO PARA LA MISMA FACTURA NO PASE
        //EL MONTO TOTAL
        for (int i = 0; i < getCtaCteDetalleTm().getList().size(); i++) {
            E_cuentaCorrienteDetalle get = getCtaCteDetalleTm().getList().get(i);
            if (get.getIdFacturaCabecera() == idFacturaCabecera) {
                int subTotalApagar = totalFactura + aPagar;
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

    public void modificarDetalle(int index, E_cuentaCorrienteDetalle detalle) {
        getCtaCteDetalleTm().modificarDatos(index, detalle);
    }

    boolean existeRecibo(int nroRecibo) {
        return DB_Cobro.existeNroRecibo(nroRecibo);
    }

    public void guardarCobro() {
        ArrayList<E_cuentaCorrienteDetalle> detalles = new ArrayList<>(getCtaCteDetalleTm().getList());
        DB_Cobro.guardarCobro(getCabecera(), detalles);
    }
}
