/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import DB.DB_Egreso;
import DB.DB_Pedido;
import DB.DB_manager;
import DB.ResultSetTableModel;
import Entities.E_estadoPedido;
import Entities.E_facturaDetalle;
import Entities.E_tipoOperacion;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_pedidoCabecera;
import Entities.M_pedidoDetalle;
import ModeloTabla.FacturaDetalleTableModel;
import ModeloTabla.PedidoCabeceraTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionPedido {

    private M_pedidoCabecera pedido;
    private ResultSetTableModel rstmPedido;
    private FacturaDetalleTableModel pedidoDetalleTM;
    private PedidoCabeceraTableModel pedidoCabeceraTM;

    public M_gestionPedido() {
        this.pedido = new M_pedidoCabecera();
        this.pedido.getFuncionario().setIdFuncionario(-1);
        this.pedido.getCliente().setIdCliente(-1);
        this.rstmPedido = DB_Pedido.obtenerPedidosPendientes(true);
        this.pedidoDetalleTM = new FacturaDetalleTableModel();
        this.pedidoCabeceraTM = new PedidoCabeceraTableModel(PedidoCabeceraTableModel.SIMPLE);
    }

    public String obtenerNombreFuncionario() {
        String alias = this.getPedido().getFuncionario().getAlias();
        String nombre = this.getPedido().getFuncionario().getNombre();
        String apellido = this.getPedido().getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    public M_pedidoCabecera getPedido() {
        return pedido;
    }

    public void setPedido(M_pedidoCabecera pedido) {
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

    public PedidoCabeceraTableModel getPedidoCabeceraTM() {
        return pedidoCabeceraTM;
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

    public void obtenerPedidos(boolean b, boolean a, Date fechaInicio, Date fechaFin, int idCondVenta, int idPedido, int idEstado) {
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(fechaFin);
        calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
        calendarFinal.set(Calendar.MINUTE, 59);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);
        int idFuncionario = this.pedido.getFuncionario().getIdFuncionario();
        int idCliente = this.pedido.getCliente().getIdCliente();
        this.pedidoCabeceraTM.setList(DB_Pedido.obtenerPedidos(b, a, calendarInicio.getTime(), calendarFinal.getTime(), idCondVenta, idPedido, idEstado, idFuncionario, idCliente, true));
    }

    public void cancelarPedido(int idPedido) {
        DB_Pedido.cancelarPedido(idPedido);
        borrarDatos();
    }

    public void pagarPedido(int idPedido) {
        M_pedidoCabecera p = DB_Pedido.obtenerPedido(idPedido);
        DB_Pedido.pagarPedido(p, (ArrayList<E_facturaDetalle>) DB_Pedido.obtenerPedidoDetalle(idPedido), null);
    }

    public ArrayList<E_tipoOperacion> obtenerTipoOperacion() {
        ArrayList<E_tipoOperacion> list = new ArrayList<>();
        list.add(new E_tipoOperacion(-1, 0, "Todos"));
        list.addAll(DB_manager.obtenerTipoOperaciones());
        return list;
    }

    public ArrayList<E_estadoPedido> obtenerEstado() {
        ArrayList<E_estadoPedido> list = new ArrayList<>();
        list.add(new E_estadoPedido(-1, "Todos"));
        list.addAll(DB_manager.obtenerPedidoEstados());
        return list;
    }

    M_pedidoCabecera obtenerPedido(Integer idPedido) {
        return DB_Pedido.obtenerPedido(idPedido);
    }

    public void obtenerPedidoDetalle(int idPedido) {
        this.pedidoDetalleTM.setFacturaDetalleList(DB_Pedido.obtenerPedidoDetalle(idPedido));
    }

}
