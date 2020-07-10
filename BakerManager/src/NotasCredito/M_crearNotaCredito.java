/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import DB.DB_NotaCredito;
import Entities.E_NotaCreditoCabecera;
import Entities.E_NotaCreditoDetalle;
import Entities.E_facturaDetalle;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.NotaCreditoDetalleTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearNotaCredito {

    private E_NotaCreditoCabecera notaCreditoCabecera;
    private NotaCreditoDetalleTableModel notaCreditoDetalleTm;
    private ArrayList<E_facturaDetalle> detalles;

    public M_crearNotaCredito() {
        this.notaCreditoCabecera = new E_NotaCreditoCabecera();
        this.notaCreditoCabecera.setFuncionario(DatosUsuario.getRol_usuario().getFuncionario());
        this.notaCreditoCabecera.getCliente().setIdCliente(-1);
        this.notaCreditoDetalleTm = new NotaCreditoDetalleTableModel();
        this.detalles = new ArrayList<>();
    }

    public ArrayList<E_facturaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<E_facturaDetalle> detalles) {
        this.detalles = detalles;
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

    public NotaCreditoDetalleTableModel getNotaCreditoDetalleTm() {
        return notaCreditoDetalleTm;
    }

    public void limpiarCampos() {
        getCabecera().getCliente().setIdCliente(-1);
        getNotaCreditoDetalleTm().vaciarLista();
    }

    public void agregarDatos(E_NotaCreditoDetalle data) {
        getNotaCreditoDetalleTm().agregarDatos(data);
    }

    public void eliminarDatos(int index) {
        getNotaCreditoDetalleTm().quitarDatos(index);
    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < getNotaCreditoDetalleTm().getList().size(); i++) {
            E_NotaCreditoDetalle get = getNotaCreditoDetalleTm().getList().get(i);
            total = total + (int) get.getSubTotal();
        }
        return total;
    }

    public boolean cantidadNuevaMayorAActual(int index, E_NotaCreditoDetalle detalle) {
        return (detalle.getCantidad() > getDetalles().get(index).getCantidad());
    }

    public void modificarDetalle(int index, E_NotaCreditoDetalle detalle) {
        getNotaCreditoDetalleTm().modificarCantidadDetalle(index, detalle.getCantidad());
    }

    boolean existeNotaCredito(int nroNotaCredito) {
        return DB_NotaCredito.existeNroNotaCredito(nroNotaCredito);
    }

    public E_NotaCreditoDetalle obtenerNotaCreditoDetalle(int idFacturaDetalle) {
        E_NotaCreditoDetalle detalle = null;
        detalle = DB_NotaCredito.obtenerNotaCreditoDetalle(idFacturaDetalle);
        return detalle;
    }

    public void guardarNotaCredito() {
        ArrayList<E_NotaCreditoDetalle> listaDetalles = new ArrayList<>(getNotaCreditoDetalleTm().getList());
        DB_NotaCredito.guardarNotaCredito(getCabecera(), listaDetalles);
    }
}
