/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import DB.DB_Cobro;
import Entities.E_NotaCreditoCabecera;
import Entities.E_NotaCreditoDetalle;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_formaPago;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.CtaCteDetalleTableModel;
import ModeloTabla.NotaCreditoDetalleTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearNotaCredito {

    private E_NotaCreditoCabecera notaCreditoCabecera;
    private NotaCreditoDetalleTableModel notaCreditoDetalleTm;

    public M_crearNotaCredito() {
        this.notaCreditoCabecera = new E_NotaCreditoCabecera();
        this.notaCreditoCabecera.setFuncionario(DatosUsuario.getRol_usuario().getFuncionario());
        this.notaCreditoCabecera.getCliente().setIdCliente(-1);
        this.notaCreditoDetalleTm = new NotaCreditoDetalleTableModel();
    }

    /**
     * @return the notaCreditoCabecera
     */
    public E_NotaCreditoCabecera getCabecera() {
        return notaCreditoCabecera;
    }

    /**
     * @param notaCreditoCabecera the notaCreditoCabecera to set
     */
    public void setCabecera(E_NotaCreditoCabecera notaCreditoCabecera) {
        this.notaCreditoCabecera = notaCreditoCabecera;
    }

    public E_NotaCreditoCabecera getCtaCteCabecera() {
        return notaCreditoCabecera;
    }

    public void setCtaCteDetalleTm(NotaCreditoDetalleTableModel ctaCteDetalleTm) {
        this.notaCreditoDetalleTm = ctaCteDetalleTm;
    }

    public NotaCreditoDetalleTableModel getNotaCreditoDetalleTm() {
        return notaCreditoDetalleTm;
    }

    public void limpiarCampos() {
        getCabecera().getCliente().setIdCliente(-1);
        getNotaCreditoDetalleTm().vaciarLista();
    }

    public void agregarDatos(E_NotaCreditoDetalle data) {
        /*for (int i = 0; i < getNotaCreditoDetalleTm().getList().size(); i++) {
            E_NotaCreditoDetalle get = getNotaCreditoDetalleTm().getList().get(i);
            if (get.getId() == data.getId()) {
                if (get.getFormaPago().getId() == data.getFormaPago().getId()
                        && data.getFormaPago().getId() == E_formaPago.EFECTIVO
                        && get.getFormaPago().getId() == E_formaPago.EFECTIVO) {
                    getNotaCreditoDetalleTm().modificarMontoPagar((int) (get.getMonto() + data.getMonto()), i);
                    return;
                }
            }
        }*/
        getNotaCreditoDetalleTm().agregarDatos(data);
    }

    public void eliminarDatos(int index) {
        getNotaCreditoDetalleTm().quitarDatos(index);
    }

    public boolean controlarMontoIngresado(int idFacturaCabecera, int aPagar, int totalPendiente) {
        /*//OBTENER EL TOTAL DE LA FACTURA ACUMULADO EN LA OPERACION DE COBRO
        int totalFactura = 0;
        for (E_cuentaCorrienteDetalle ctaCteDetalle : getNotaCreditoDetalleTm().getList()) {
            if (ctaCteDetalle.getIdFacturaCabecera() == idFacturaCabecera) {
                totalFactura = totalFactura + (int) ctaCteDetalle.getMonto();
            }
        }
        //VERIFICAR QUE EL NUEVO MONTO INGRESADO PARA LA MISMA FACTURA NO PASE
        //EL MONTO TOTAL
        for (int i = 0; i < getNotaCreditoDetalleTm().getList().size(); i++) {
            E_cuentaCorrienteDetalle get = getNotaCreditoDetalleTm().getList().get(i);
            if (get.getIdFacturaCabecera() == idFacturaCabecera) {
                int subTotalApagar = totalFactura + aPagar;
                if (subTotalApagar > totalPendiente) {
                    return false;
                }
            }
        }*/
        return true;
    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < getNotaCreditoDetalleTm().getList().size(); i++) {
            E_NotaCreditoDetalle get = getNotaCreditoDetalleTm().getList().get(i);
            total = total + (int) get.getSubTotal();
        }
        return total;
    }

    public void modificarDetalle(int index, E_NotaCreditoDetalle detalle) {
        //getNotaCreditoDetalleTm().modificarDatos(index, detalle);
    }

    boolean existeRecibo(int nroRecibo) {
        return DB_Cobro.existeNroRecibo(nroRecibo);
    }

    public void guardarCobro() {
        /*ArrayList<E_cuentaCorrienteDetalle> detalles = new ArrayList<>(getNotaCreditoDetalleTm().getList());
        DB_Cobro.guardarCobro(getCabecera(), detalles);*/
    }
}
