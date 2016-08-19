/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Resumen;

import DB.DB_Egreso;
import DB.DB_Pedido;
import DB.ResultSetTableModel;
import Entities.M_egreso_cabecera;
import Entities.M_egreso_detalleFX;
import Entities.M_pedido;
import Pedido.C_gestionPedido;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro
 */
public class M_resumen {

    public static final int RESUMEN_EGRESO = 1;
    public static final int RESUMEN_PEDIDO = 2;
    public int tipo;
    private Date inicio, fin;
    private M_egreso_cabecera egresoCabecera;
    private ArrayList<M_egreso_detalleFX> egresoDetalles;
    private ResultSetTableModel rstm;
    private M_pedido pedido;

    public M_resumen(M_egreso_cabecera egresoCabecera, Date inicio, Date fin) {
        this.tipo = RESUMEN_EGRESO;
        this.egresoCabecera = egresoCabecera;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fin);
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);
        this.fin = cal.getTime();
        cal.setTime(inicio);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 250);
        this.inicio = cal.getTime();
        this.rstm = DB_Egreso.consultarResumenEgreso(new Timestamp(this.inicio.getTime()), new Timestamp(this.fin.getTime()));
        this.egresoDetalles = DB_Egreso.obtenerEgresosDetalle(this.egresoCabecera.getProveedor().getEntidad(), this.egresoCabecera.getId_cabecera(), this.egresoCabecera.getFuncionario().getId_funcionario().toString(), new Timestamp(this.inicio.getTime()).toString(), new Timestamp(this.fin.getTime()).toString(), this.egresoCabecera.getCondVenta());
    }

    public M_resumen(C_gestionPedido gestionPedido) {
        this.tipo = RESUMEN_PEDIDO;
        Calendar cal = Calendar.getInstance();
        cal.setTime(gestionPedido.vista.jddFinal.getDate());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 250);
        this.fin = cal.getTime();
        cal.setTime(gestionPedido.vista.jddInicio.getDate());
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);
        this.pedido = gestionPedido.modelo.getPedido();
        this.inicio = cal.getTime();
        this.rstm = gestionPedido.modelo.getRstmPedido();
        System.out.println("cliente:"+this.pedido.getCliente().getNombre());
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public M_egreso_cabecera getEgresoCabecera() {
        return egresoCabecera;
    }

    public void setEgresoCabecera(M_egreso_cabecera egresoCabecera) {
        this.egresoCabecera = egresoCabecera;
    }

    public ResultSetTableModel getRstm() {
        return rstm;
    }

    public void setRstm(ResultSetTableModel rstm) {
        this.rstm = rstm;
    }

    /**
     * @return the egresoDetalles
     */
    public ArrayList<M_egreso_detalleFX> getEgresoDetalles() {
        return egresoDetalles;
    }

    /**
     * @param egresoDetalles the egresoDetalles to set
     */
    public void setEgresoDetalles(ArrayList<M_egreso_detalleFX> egresoDetalles) {
        this.egresoDetalles = egresoDetalles;
    }

    public ResultSetTableModel obtenerDetallePedido() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inicio);
        int mes = cal.get(Calendar.MONTH) + 1;
        String fechaInicio = cal.get(Calendar.DAY_OF_MONTH) + "/" + mes + "/" + cal.get(Calendar.YEAR);
        cal.setTime(fin);
        mes = cal.get(Calendar.MONTH) + 1;
        String fechaFin = cal.get(Calendar.DAY_OF_MONTH) + "/" + mes + "/" + cal.get(Calendar.YEAR);
        return DB_Pedido.obtenerPedidoDetalleAgrupado(pedido.getCliente().getIdCliente(), fechaInicio, fechaFin);
    }
}
