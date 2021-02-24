/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import DB.DB_Egreso;
import DB.DB_Ingreso;
import DB.DB_Pedido;
import DB.DB_Preferencia;
import DB.DB_manager;
import DB.ResultSetTableModel;
import Entities.E_impresionTipo;
import Entities.E_tipoOperacion;
import Entities.M_pedidoCabecera;
import Entities.M_pedidoDetalle;
import ModeloTabla.FacturaDetalleTableModel;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verPedido {

    private M_pedidoCabecera pedido;
    private FacturaDetalleTableModel tm;
    private E_impresionTipo tipoVenta;

    public M_verPedido(int idPedido) {
        this.pedido = DB_Pedido.obtenerPedido(idPedido);
        this.tm = new FacturaDetalleTableModel();
        this.tm.setFacturaDetalleList(DB_Pedido.obtenerPedidoDetalle(idPedido));
    }

    public FacturaDetalleTableModel getTm() {
        return tm;
    }

    /**
     * @return the tipoVenta
     */
    public E_impresionTipo getTipoVenta() {
        return tipoVenta;
    }

    /**
     * @param tipoVenta the tipoVenta to set
     */
    public void setTipoVenta(E_impresionTipo tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public M_pedidoCabecera getPedido() {
        return pedido;
    }

    public void setPedido(M_pedidoCabecera pedido) {
        this.pedido = pedido;
    }

    public void actualizarCliente() {
        DB_Pedido.actualizarPedidoCliente(getPedido());
    }

    public void actualizarPedidoDetalle() {
//        DB_Pedido.actualizarPedidoDetalle(tm.getList());
//        borrarDatos();
    }

    public void actualizarTablaPedidoDetalle() {
        //this.rstm = DB_Pedido.obtenerPedidoDetalle(getPedido().getIdPedido());
    }

    public void eliminarPedidoDetalle() {
        //DB_Pedido.eliminarPedidoDetalle(getDetalle().getIdPedioDetalle());
    }

    public void insertarPedidoDetalle(M_pedidoDetalle detalle) {
        DB_Pedido.insertarPedidoDetalle(detalle);
    }

    public void actualizarPedido() {
        DB_Pedido.actualizarPedido(getPedido());
    }

    public int pagarPedido(int idPedido, Integer nroFactura) {
//        M_pedido p = DB_Pedido.obtenerPedido(idPedido);
//        setDetalles(DB_Pedido.obtenerPedidoDetalles(p.getIdPedido()));
//        return DB_Pedido.pagarPedido(p, getDetalles(), nroFactura);
        return 0;
    }

    public int getNroFactura() {
        int nroFactura;
        nroFactura = DB_Ingreso.obtenerUltimoNroFactura() + 1;
        return nroFactura;
    }

    public boolean nroFacturaEnUso(int nroFactura) {
        return DB_Ingreso.nroFacturaEnUso(nroFactura);
    }

    public Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    public ArrayList<E_impresionTipo> obtenerTipoVenta() {
        return DB_Preferencia.obtenerImpresionTipo();
    }
    
    public void establecerCondicionVentaContado() {
        this.pedido.setTipoOperacion(new E_tipoOperacion(E_tipoOperacion.CONTADO, 0, "Contado"));
    }

    public void establecerCondicionVentaCredito() {
        this.pedido.setTipoOperacion(new E_tipoOperacion(E_tipoOperacion.CREDITO_30, 30, "Credito"));
    }
}
