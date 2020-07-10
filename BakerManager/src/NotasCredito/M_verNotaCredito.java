/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import DB.DB_NotaCredito;
import Entities.E_NotaCreditoCabecera;
import Entities.E_NotaCreditoDetalle;
import ModeloTabla.NotaCreditoDetalleTableModel;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verNotaCredito {

    private E_NotaCreditoCabecera notaCreditoCabecera;
    private NotaCreditoDetalleTableModel notaCreditoDetalleTm;

    public M_verNotaCredito() {
        this.notaCreditoCabecera = new E_NotaCreditoCabecera();
        this.notaCreditoDetalleTm = new NotaCreditoDetalleTableModel();
    }

    public E_NotaCreditoCabecera getCabecera() {
        return notaCreditoCabecera;
    }

    public void setCabecera(E_NotaCreditoCabecera notaCreditoCabecera) {
        this.notaCreditoCabecera = notaCreditoCabecera;
    }

    public void inicializarDatos(E_NotaCreditoCabecera notaCreditoCabecera) {
        setCabecera(notaCreditoCabecera);
        this.getNotaCreditoDetalleTm().setList(obtenerNotasCreditoDetalle(notaCreditoCabecera.getId()));

    }

    public NotaCreditoDetalleTableModel getNotaCreditoDetalleTm() {
        return notaCreditoDetalleTm;
    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < getNotaCreditoDetalleTm().getList().size(); i++) {
            E_NotaCreditoDetalle get = getNotaCreditoDetalleTm().getList().get(i);
            total = total + (int) get.getSubTotal();
        }
        return total;
    }

    public List<E_NotaCreditoDetalle> obtenerNotasCreditoDetalle(int idNotaCreditoCabecera) {
        return DB_NotaCredito.obtenerNotasCreditoDetalle(idNotaCreditoCabecera);
    }
}
