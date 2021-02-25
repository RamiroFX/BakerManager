/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Resumen;

import DB.DB_Pedido;
import ModeloTabla.FacturaDetalleTableModel;
import ModeloTabla.PedidoCabeceraTableModel;
import java.util.Date;

/**
 *
 * @author Ramiro
 */
public class M_resumen {

    private Date fechaInicio, fechaFin;
    private PedidoCabeceraTableModel tm;
    private FacturaDetalleTableModel tmDetalle;

    public M_resumen(PedidoCabeceraTableModel tm, Date fechaInicio, Date fechaFin) {
        this.tm = tm;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tmDetalle = new FacturaDetalleTableModel();
        this.tmDetalle.setFacturaDetalleList(DB_Pedido.consultarDetalleAgrupado(tm.getList()));
    }

    public PedidoCabeceraTableModel getTm() {
        return tm;
    }

    public void setTm(PedidoCabeceraTableModel tm) {
        this.tm = tm;
    }

    public FacturaDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

}
