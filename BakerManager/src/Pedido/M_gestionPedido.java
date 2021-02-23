/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import DB.DB_Egreso;
import DB.DB_Pedido;
import DB.ResultSetTableModel;
import Entities.E_facturaDetalle;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
import ModeloTabla.FacturaDetalleTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionPedido {

    private M_pedido pedido;
    private ResultSetTableModel rstmPedido;
    private FacturaDetalleTableModel pedidoDetalleTM;

    public M_gestionPedido() {
        this.pedido = new M_pedido();
        this.rstmPedido = DB_Pedido.obtenerPedidosPendientes(true);
        this.pedidoDetalleTM = new FacturaDetalleTableModel();
    }

    public String obtenerNombreFuncionario() {
        String alias = this.getPedido().getFuncionario().getAlias();
        String nombre = this.getPedido().getFuncionario().getNombre();
        String apellido = this.getPedido().getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    public M_pedido getPedido() {
        return pedido;
    }

    public void setPedido(M_pedido pedido) {
        this.pedido = pedido;
    }

    public ResultSetTableModel getRstmPedido() {
        return rstmPedido;
    }

    public ResultSetTableModel getPedidosPendientes() {
        return this.rstmPedido = DB_Pedido.obtenerPedidosPendientes(true);
    }

    public void setRstmPedido(ResultSetTableModel rstmPedido) {
        this.rstmPedido = rstmPedido;
    }

    public FacturaDetalleTableModel getPedidoDetalleTM() {
        return pedidoDetalleTM;
    }

    public void borrarDatos() {
        getPedido().setCliente(new M_cliente());
        getPedido().setFuncionario(new M_funcionario());
    }

    boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            }
        }
        return false;
    }

    public ResultSetTableModel obtenerPedidos(boolean b, String inicio, String fin, String condVenta, String nroPedido, String estado) {
        return DB_Pedido.obtenerPedidos(b, inicio, fin, condVenta, nroPedido, estado, getPedido(), true);
    }

    public void cancelarPedido(int idPedido) {
        DB_Pedido.cancelarPedido(idPedido);
        borrarDatos();
    }

    public void pagarPedido(int idPedido) {
        M_pedido p = DB_Pedido.obtenerPedido(idPedido);
        DB_Pedido.pagarPedido(p, (ArrayList<E_facturaDetalle>) DB_Pedido.obtenerPedidoDetalle(idPedido), null);
    }

    Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    Vector obtenerEstado() {
        return DB_Pedido.obtenerEstado();
    }

    M_pedido obtenerPedido(Integer idPedido) {
        return DB_Pedido.obtenerPedido(idPedido);
    }

    public void obtenerPedidoDetalle(int idPedido) {
        this.pedidoDetalleTM.setFacturaDetalleList(DB_Pedido.obtenerPedidoDetalle(idPedido));
    }

}
