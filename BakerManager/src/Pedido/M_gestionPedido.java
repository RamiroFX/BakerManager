/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import DB_manager.DB_Egreso;
import DB_manager.DB_Pedido;
import DB_manager.ResultSetTableModel;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
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
    private ResultSetTableModel rstmPedido, rstmPedidoDetalle;
    private DefaultTableModel dtm;
    private ArrayList<M_pedidoDetalle> detalles;

    public M_gestionPedido() {
        this.pedido = new M_pedido();
        this.dtm = new DefaultTableModel();
        this.dtm.addColumn("ID art.");
        this.dtm.addColumn("Producto");
        this.dtm.addColumn("Cantidad");
        this.dtm.addColumn("Precio");
        this.dtm.addColumn("Descuento");
        this.dtm.addColumn("Exenta");
        this.dtm.addColumn("IVA 5%");
        this.dtm.addColumn("IVA 10%");
        this.dtm.addColumn("Obs.");
        this.rstmPedido = DB_Pedido.obtenerPedidosPendientes(true);
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

    public ResultSetTableModel getRstmPedidoDetalle() {
        return rstmPedidoDetalle;
    }

    public void setRstmPedidoDetalle(ResultSetTableModel rstmPedidoDetalle) {
        this.rstmPedidoDetalle = rstmPedidoDetalle;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }

    public ArrayList<M_pedidoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<M_pedidoDetalle> detalles) {
        this.detalles = detalles;
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

    public ResultSetTableModel obtenerVentas(boolean b, String inicio, String fin, String condVenta, String nroPedido, String estado) {
        return DB_Pedido.obtenerPedidos(b, inicio, fin, condVenta, nroPedido, estado, getPedido(), true);
    }

    public void cancelarPedido() {
        DB_Pedido.cancelarPedido(getPedido().getIdPedido());
        borrarDatos();
    }

    public void pagarPedido() {
        setDetalles(DB_Pedido.obtenerPedidoDetalles(getPedido().getIdPedido()));
        DB_Pedido.pagarPedido(getPedido(), getDetalles());
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

    ResultSetTableModel obtenerPedidoDetalle(Integer idPedido) {
        return DB_Pedido.obtenerPedidoDetalle(idPedido);
    }
}
