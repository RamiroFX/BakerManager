/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Ingreso;
import DB.ResultSetTableModel;
import Entities.M_cliente;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Entities.M_producto;
import java.util.ArrayList;

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
        this.mesa.setIdCondVenta(1);//contado
        this.detalle = new M_mesa_detalle();
        this.detalles = new ArrayList<>();
    }

    public M_verMesa(int idMesa) {
        this.mesa = DB_Ingreso.obtenerMesaID(idMesa);
        this.detalle = new M_mesa_detalle();
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
        DB_Ingreso.transferirMesaAVenta(getMesa(), getDetalles());
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
        for (int i = 0; i < row; i++) {
            M_mesa_detalle fd = new M_mesa_detalle();
            fd.setCantidad(Double.valueOf(getRstm().getValueAt(i, 3).toString()));
            fd.setDescuento(Double.valueOf(getRstm().getValueAt(i, 5).toString()));
            fd.setExenta(Integer.valueOf(getRstm().getValueAt(i, 6).toString()));
            fd.setProducto(new M_producto());
            fd.getProducto().setId(Integer.valueOf(getRstm().getValueAt(i, 1).toString()));
            fd.setIva10(Integer.valueOf(getRstm().getValueAt(i, 8).toString()));
            fd.setIva5(Integer.valueOf(getRstm().getValueAt(i, 7).toString()));
            fd.setObservacion(String.valueOf(getRstm().getValueAt(i, 9).toString()));
            fd.setPrecio(Integer.valueOf(getRstm().getValueAt(i, 4).toString()));
//            System.out.println(fd.getIdProducto() + "-" + "producto" + "-" + fd.getCantidad()
//                    + "-" + fd.getPrecio() + "-" + fd.getDescuento() + "-" + fd.getExenta() + "-"
//                    + fd.getIva5() + "-" + fd.getIva10() + "-" + fd.getObservacion());
            getDetalles().add(fd);
        }
    }
}
