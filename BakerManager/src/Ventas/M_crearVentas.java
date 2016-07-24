/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB_manager.DB_Ingreso;
import DB_manager.ResultSetTableModel;
import Entities.M_funcionario;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearVentas {

    private M_funcionario funcionario;
    private M_mesa mesa;
    private M_mesa_detalle mesaDetalle;
    private ArrayList<M_mesa_detalle> mesaDetalles;
    private ArrayList<M_mesa> mesas;
    private ResultSetTableModel rstmMesa;
    private ResultSetTableModel rstmMesaDetalle;
    private DefaultTableModel dtm;

    public M_crearVentas() {
        this.funcionario = new M_funcionario();
        this.mesas = new ArrayList<>();
        String tipoOperacion = "Contado";
        java.util.Date date = new java.util.Date();
        /*java.util.Date date = new java.util.Date();
         * String fechaInicio = new Timestamp(date.getTime()).toString().substring(0, 11);
         * fechaInicio = fechaInicio + "00:00:00.000";
         * String fechaFinal = new Timestamp(date.getTime()).toString().substring(0, 11);
         * fechaFinal = fechaFinal + "23:59:59.000";*/
        String fechaInicio = "";
        String fechaFinal = "";
        this.rstmMesa = DB_Ingreso.obtenerMesa(fechaInicio, fechaFinal, tipoOperacion);
        this.dtm = new DefaultTableModel();
        this.dtm.addColumn("ID");
        this.dtm.addColumn("ID art.");
        this.dtm.addColumn("Producto");
        this.dtm.addColumn("Cantidad");
        this.dtm.addColumn("Precio");
        this.dtm.addColumn("Descuento");
        this.dtm.addColumn("Exenta");
        this.dtm.addColumn("IVA 5%");
        this.dtm.addColumn("IVA 10%");
        this.dtm.addColumn("Obs.");
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public ArrayList<M_mesa> getMesas() {
        return mesas;
    }

    public void setMesas(ArrayList<M_mesa> mesas) {
        this.mesas = mesas;
    }

    public M_mesa getMesa() {
        return mesa;
    }

    public void setMesa(M_mesa mesa) {
        this.mesa = mesa;
    }

    public void setMesa(int idMesa) {
        this.mesa = DB_Ingreso.obtenerMesaID(idMesa);
    }

    public M_mesa_detalle getMesaDetalle() {
        return mesaDetalle;
    }

    public void setMesaDetalle(M_mesa_detalle mesaDetalle) {
        this.mesaDetalle = mesaDetalle;
    }

    public ArrayList<M_mesa_detalle> getMesaDetalles() {
        return mesaDetalles;
    }

    public void setMesaDetalles(ArrayList<M_mesa_detalle> mesaDetalles) {
        this.mesaDetalles = mesaDetalles;
    }

    public ResultSetTableModel getRstmMesa() {
        return rstmMesa;
    }

    public void setRstmMesa(ResultSetTableModel rstmMesa) {
        this.rstmMesa = rstmMesa;
    }

    public ResultSetTableModel getRstmMesaDetalle() {
        return rstmMesaDetalle;

    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    public void setRstmMesaDetalle(int idMesa) {
        this.rstmMesaDetalle = DB_Ingreso.obtenerMesaDetalle(idMesa);
    }

    public void insertarMesa() {
        DB_Ingreso.insertarMesa(getMesa(), getMesaDetalles());
    }

    public void actualizarMesa() {
        DB_Ingreso.actualizarMesa(getMesa());
    }

    public void actualizarMesaDetalle() {
        DB_Ingreso.actualizarMesaDetalle(getMesaDetalle());
    }

    public void eliminarMesa(int idMesa) {
        DB_Ingreso.eliminarMesa(idMesa);
    }

    public void eliminarMesaDetalle() {
        DB_Ingreso.eliminarMesaDetalle(getMesaDetalle().getIdMesaDetalle());
    }

    public void actualizarTablaMesa() {
        String tipoOperacion = "Todos";//no se usa
        /*java.util.Date date = new java.util.Date();
         * String fechaInicio = new Timestamp(date.getTime()).toString().substring(0, 11);
         * fechaInicio = fechaInicio + "00:00:00.000";
         * String fechaFinal = new Timestamp(date.getTime()).toString().substring(0, 11);
         * fechaFinal = fechaFinal + "23:59:59.000";*/
        String fechaInicio = "";
        String fechaFinal = "";
        this.rstmMesa = DB_Ingreso.obtenerMesa(fechaInicio, fechaFinal, tipoOperacion);
    }
}
