/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import DB_manager.DB_Pedido;
import DB_manager.ResultSetTableModel;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verPedido {

    private M_pedido pedido;
    private M_pedidoDetalle detalle;
    private ResultSetTableModel rstm;

    public M_verPedido(int idPedido) {
        this.pedido = DB_Pedido.obtenerPedido(idPedido);
        this.detalle = new M_pedidoDetalle();
        this.rstm = DB_Pedido.obtenerPedidoDetalle(idPedido);
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
}
