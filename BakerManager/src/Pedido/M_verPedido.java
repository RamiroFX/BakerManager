/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import DB.DB_Egreso;
import DB.DB_Ingreso;
import DB.DB_Pedido;
import DB.DB_manager;
import DB.ResultSetTableModel;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verPedido {

    private M_pedido pedido;
    private M_pedidoDetalle detalle;
    private ArrayList<M_pedidoDetalle> detalles;
    private ResultSetTableModel rstm;

    public M_verPedido(int idPedido) {
        this.pedido = DB_Pedido.obtenerPedido(idPedido);
        this.detalle = new M_pedidoDetalle();
        this.rstm = DB_Pedido.obtenerPedidoDetalle(idPedido);
        this.detalles = DB_Pedido.obtenerPedidoDetalles(idPedido);
    }

    public M_pedido getPedido() {
        return pedido;
    }

    public void setPedido(M_pedido pedido) {
        this.pedido = pedido;
    }

    public M_pedidoDetalle getDetalle() {
        return detalle;
    }

    public void setDetalle(M_pedidoDetalle detalle) {
        this.detalle = detalle;
    }

    public ResultSetTableModel getRstm() {
        return rstm;
    }

    public void setRstm(ResultSetTableModel rstm) {
        this.rstm = rstm;
    }

    public void borrarDatos() {
        this.detalle = new M_pedidoDetalle();
    }

    public void actualizarCliente() {
        DB_Pedido.actualizarPedidoCliente(getPedido());
    }

    public void actualizarPedidoDetalle() {
        DB_Pedido.actualizarPedidoDetalle(getDetalle());
        borrarDatos();
    }

    public void actualizarTablaPedidoDetalle() {
        this.rstm = DB_Pedido.obtenerPedidoDetalle(getPedido().getIdPedido());
    }

    public void eliminarPedidoDetalle() {
        DB_Pedido.eliminarPedidoDetalle(getDetalle().getIdPedioDetalle());
    }

    public void insertarPedidoDetalle(M_pedidoDetalle detalle) {
        DB_Pedido.insertarPedidoDetalle(detalle);
    }

    public void actualizarPedido() {
        DB_Pedido.actualizarPedido(getPedido());
    }

    public ArrayList<M_pedidoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<M_pedidoDetalle> detalles) {
        this.detalles = detalles;
    }

    public int pagarPedido(int idPedido, Integer nroFactura) {
        M_pedido p = DB_Pedido.obtenerPedido(idPedido);
        setDetalles(DB_Pedido.obtenerPedidoDetalles(p.getIdPedido()));
        return DB_Pedido.pagarPedido(p, getDetalles(), nroFactura);
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

    public Vector obtenerTipoVenta() {
        return DB_manager.obtenerTipoVenta();
    }
}
