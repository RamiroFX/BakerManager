/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import DB.DB_Pago;
import Entities.E_formaPago;
import Entities.E_reciboPagoCabecera;
import Entities.E_reciboPagoDetalle;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.ReciboPagoDetalleTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearPago {

    private E_reciboPagoCabecera cabecera;
    private ReciboPagoDetalleTableModel detalleTM;

    public M_crearPago() {
        this.cabecera = new E_reciboPagoCabecera();
        this.cabecera.setFuncionario(DatosUsuario.getRol_usuario().getFuncionario());
        this.cabecera.getProveedor().setId(-1);
        this.detalleTM = new ReciboPagoDetalleTableModel();
    }

    public E_reciboPagoCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(E_reciboPagoCabecera ctaCteCabecera) {
        this.cabecera = ctaCteCabecera;
    }

    public void setDetalleTm(ReciboPagoDetalleTableModel ctaCteDetalleTm) {
        this.detalleTM = ctaCteDetalleTm;
    }

    public ReciboPagoDetalleTableModel getDetalleTm() {
        return detalleTM;
    }

    public void limpiarCampos() {
        getCabecera().getProveedor().setId(-1);
        getDetalleTm().vaciarLista();
    }

    public void agregarDatos(E_reciboPagoDetalle data) {
        for (int i = 0; i < getDetalleTm().getList().size(); i++) {
            E_reciboPagoDetalle get = getDetalleTm().getList().get(i);
            if (get.getIdFacturaCabecera() == data.getIdFacturaCabecera()) {
                if (get.getFormaPago().getId() == data.getFormaPago().getId()
                        && data.getFormaPago().getId() == E_formaPago.EFECTIVO
                        && get.getFormaPago().getId() == E_formaPago.EFECTIVO) {
                    getDetalleTm().modificarMontoPagar((int) (get.getMonto() + data.getMonto()), i);
                    return;
                }
            }
        }
        getDetalleTm().agregarDatos(data);
    }

    public void eliminarDatos(int index) {
        getDetalleTm().quitarDatos(index);
    }

    public boolean controlarMontoIngresado(int idFacturaCabecera, int aPagar, int totalPendiente) {
        //OBTENER EL TOTAL DE LA FACTURA ACUMULADO EN LA OPERACION DE COBRO
        int totalFactura = 0;
        for (E_reciboPagoDetalle detalle : getDetalleTm().getList()) {
            if (detalle.getIdFacturaCabecera() == idFacturaCabecera) {
                totalFactura = totalFactura + (int) detalle.getMonto();
            }
        }
        //VERIFICAR QUE EL NUEVO MONTO INGRESADO PARA LA MISMA FACTURA NO PASE
        //EL MONTO TOTAL
        for (int i = 0; i < getDetalleTm().getList().size(); i++) {
            E_reciboPagoDetalle get = getDetalleTm().getList().get(i);
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
        for (int i = 0; i < getDetalleTm().getList().size(); i++) {
            E_reciboPagoDetalle get = getDetalleTm().getList().get(i);
            total = total + (int) get.getMonto();
        }
        return total;
    }

    public void modificarDetalle(int index, E_reciboPagoDetalle detalle) {
        getDetalleTm().modificarDatos(index, detalle);
    }

    boolean existeRecibo(int nroRecibo) {
        return DB_Pago.existeNroRecibo(nroRecibo, getCabecera().getProveedor().getId());
    }

    public void guardarCobro() {
        ArrayList<E_reciboPagoDetalle> detalles = new ArrayList<>(getDetalleTm().getList());
        DB_Pago.guardarPago(getCabecera(), detalles);
    }
}
