/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Egreso;
import DB.DB_Ingreso;
import DB.DB_Preferencia;
import DB.DB_Producto;
import DB.DB_manager;
import DB.ResultSetTableModel;
import Entities.E_impresionTipo;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Entities.M_producto;
import Parametros.TipoOperacion;
import Impresora.Impresora;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verMesa {

    private M_mesa mesa;
    private M_mesa_detalle detalle;
    private ArrayList<M_mesa_detalle> detalles;
    private ResultSetTableModel rstm;

    public M_verMesa(M_cliente c, M_funcionario f, int nroMesa) {
        this.mesa = new M_mesa();
        this.mesa.setNumeroMesa(nroMesa);
        this.mesa.setFuncionario(f);
        this.mesa.setCliente(c);
        this.mesa.setIdCondVenta(TipoOperacion.CONTADO);
        this.detalle = new M_mesa_detalle();
        this.detalles = new ArrayList<>();
    }

    public M_verMesa(int idMesa) {
        this.mesa = DB_Ingreso.obtenerMesaID(idMesa);
        this.detalle = new M_mesa_detalle();
        //this.detalles = DB_Ingreso.obtenerMesaDetalles(idMesa);
        this.detalles = new ArrayList<>();
        rstm = DB_Ingreso.obtenerMesaDetalle(idMesa);
    }

    public M_mesa getMesa() {
        return mesa;
    }

    public void setMesa(M_mesa cabecera) {
        this.mesa = cabecera;
    }

    public void setMesa(int idMesa) {
        this.mesa = DB_Ingreso.obtenerMesaID(idMesa);
    }

    public M_mesa_detalle getMesaDetalle() {
        return detalle;
    }

    public void setMesaDetalle(M_mesa_detalle detalle) {
        this.detalle = detalle;
    }

    public ArrayList<M_mesa_detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<M_mesa_detalle> detalles) {
        this.detalles = detalles;
    }

    public ResultSetTableModel getRstm() {
        return rstm;
    }

    public void setRstm(ResultSetTableModel rstm) {
        this.rstm = rstm;
    }

    public void guardarVenta() {
        preparaVenta();
        int nroTicket = DB_Ingreso.transferirMesaAVenta(getMesa(), getDetalles());
        getMesa().setIdMesa(nroTicket);
        int opcion = JOptionPane.showConfirmDialog(null, "¿Desea imprimir el ticket?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            Impresora.imprimirTicketVentaMesa(getMesa(), getDetalles());
        }
    }

    boolean existeProductoPorCodigo(String codigoProducto) {
        return DB_Producto.existeCodigo(codigoProducto);
    }

    public M_producto obtenerProductoPorCodigo(String codigoProducto) {
        M_producto unProducto = DB_Producto.obtenerProductoPorCodigo(codigoProducto);
        return unProducto;
    }

    public void guardarVentaDetalle() {
        DB_Ingreso.insertarMesaDetalle(getMesa().getIdMesa(), getMesaDetalle());
    }

    public void modificarMesaDetalle() {
        DB_Ingreso.actualizarMesaDetalle(getMesaDetalle());
    }

    public void eliminarVenta(int idMesaDetalle) {
        DB_Ingreso.eliminarMesaDetalle(idMesaDetalle);
    }

    void borrarMesaDetalle() {
        getMesaDetalle().setCantidad(null);
        getMesaDetalle().setDescuento(null);
        getMesaDetalle().setExenta(null);
        getMesaDetalle().setIva10(null);
        getMesaDetalle().setIva5(null);
        getMesaDetalle().setObservacion(null);
        getMesaDetalle().setPrecio(null);
        getMesaDetalle().setProducto(null);
        getMesaDetalle().setTotal(null);
    }

    public void actualizarVentaDetalle() {
        rstm = DB_Ingreso.obtenerMesaDetalle(mesa.getIdMesa());
    }

    private void preparaVenta() {
        int row = getRstm().getRowCount();
        getDetalles().clear();
        for (int i = 0; i < row; i++) {
            M_mesa_detalle fd = new M_mesa_detalle();
            fd.setProducto(new M_producto());
            fd.getProducto().setId(Integer.valueOf(getRstm().getValueAt(i, 1).toString()));
            fd.getProducto().setDescripcion(String.valueOf(getRstm().getValueAt(i, 2).toString()));
            fd.setCantidad(Double.valueOf(getRstm().getValueAt(i, 3).toString()));
            fd.setPrecio(Integer.valueOf(getRstm().getValueAt(i, 4).toString()));
            fd.setDescuento(Double.valueOf(getRstm().getValueAt(i, 5).toString()));
            int exenta = Integer.valueOf(getRstm().getValueAt(i, 6).toString());
            int iva5 = Integer.valueOf(getRstm().getValueAt(i, 7).toString());
            int iva10 = Integer.valueOf(getRstm().getValueAt(i, 8).toString());
            fd.setExenta(exenta);
            fd.setIva5(iva5);
            fd.setIva10(iva10);
            Object obs = getRstm().getValueAt(i, 9);
            if (obs != null) {
                fd.setObservacion(String.valueOf(obs.toString()));
            } else {
                fd.setObservacion(null);
            }
            if (exenta > 0) {
                fd.getProducto().setIdImpuesto(1);
            }
            if (iva5 > 0) {
                fd.getProducto().setIdImpuesto(2);
            }
            if (iva10 > 0) {
                fd.getProducto().setIdImpuesto(3);
            }
            getDetalles().add(fd);
        }
    }

    public void imprimir() {
        getDetalles().clear();
        preparaVenta();
        Impresora.imprimirTicketMesa(mesa, detalles);
    }

    public void actualizarDatosMesa(M_cliente cliente) {
        getMesa().setCliente(cliente);
        DB_Ingreso.actualizarMesa(mesa);
    }

    public Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    public ArrayList<E_impresionTipo> obtenerTipoVenta() {
        return DB_Preferencia.obtenerImpresionTipo();
    }

    public int getNroFactura() {
        int nroFactura = DB_Ingreso.obtenerUltimoNroFactura() + 1;
        int nroFacturacion = DB_Ingreso.obtenerUltimoNroFacturacion() + 1;
        if (nroFactura >= nroFacturacion) {
            return nroFactura;
        } else {
            return nroFacturacion;
        }
    }
}
