/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Ingreso;
import DB.DB_manager;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_menu_item;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.FacturaCabeceraTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionVentas {

    M_facturaCabecera cabecera;
    M_facturaDetalle detalle;
    ArrayList<M_facturaDetalle> detalles;
    private FacturaCabeceraTableModel tm;
    private ArrayList<M_menu_item> accesos;

    public M_gestionVentas() {
        this.cabecera = new M_facturaCabecera();
        this.cabecera.getFuncionario().setId_funcionario(-1);
        this.cabecera.getCliente().setIdCliente(-1);
        this.detalle = new M_facturaDetalle();
        this.detalles = new ArrayList<>();
        this.tm = new FacturaCabeceraTableModel(FacturaCabeceraTableModel.COMPLETO);
        this.accesos = DatosUsuario.getRol_usuario().getAccesos();
    }

    public M_facturaCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(M_facturaCabecera cabecera) {
        this.cabecera = cabecera;
    }

    public M_facturaDetalle getDetalle() {
        return detalle;
    }

    public void setDetalle(M_facturaDetalle detalle) {
        this.detalle = detalle;
    }

    public ArrayList<M_facturaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<M_facturaDetalle> detalles) {
        this.detalles = detalles;
    }

    public boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            }
        }
        return false;
    }

    public List<M_facturaCabecera> obtenerVentas(M_cliente cliente, M_funcionario funcionario, Date fechaInicio, Date fechaFin, E_tipoOperacion condCompra, int nroFactura, Estado estado, boolean conFecha) {
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
        return DB_Ingreso.obtenerIngreso2(calendarInicio.getTime(), calendarFinal.getTime(), cliente.getIdCliente(), funcionario.getId_funcionario(), condCompra.getId(), nroFactura, estado.getId(), conFecha);
    }

    public void borrarDatos() {
        this.cabecera = new M_facturaCabecera();
        this.cabecera.getFuncionario().setId_funcionario(-1);
        this.cabecera.getCliente().setIdCliente(-1);
        this.detalles = new ArrayList<>();
        setDetalles(new ArrayList<M_facturaDetalle>());
    }

    public ArrayList<Estado> getEstados() {
        ArrayList<Estado> estados = DB_manager.obtenerEstados();
        estados.add(new Estado(-1, "Todos"));
        return estados;
    }

    public ArrayList<E_tipoOperacion> obtenerTipoOperaciones() {
        E_tipoOperacion todos = new E_tipoOperacion();
        todos.setDescripcion("Todos");
        todos.setDuracion(0);
        todos.setId(-1);
        ArrayList<E_tipoOperacion> condVenta = new ArrayList<>();
        condVenta.add(todos);
        condVenta.addAll(DB_manager.obtenerTipoOperaciones());
        return condVenta;
    }

    public void anularVenta(int idVenta, boolean recuperarNroFact) {
        //id 2 es inactivo
        DB_Ingreso.anularVenta(idVenta, 2, recuperarNroFact);
    }

    public String obtenerNombreFuncionario() {
        String alias = this.getCabecera().getFuncionario().getAlias();
        String nombre = this.getCabecera().getFuncionario().getNombre();
        String apellido = this.getCabecera().getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    /**
     * @return the tm
     */
    public FacturaCabeceraTableModel getTm() {
        return tm;
    }

    /**
     * @param tm the tm to set
     */
    public void setTm(FacturaCabeceraTableModel tm) {
        this.tm = tm;
    }

    public ArrayList<M_menu_item> getAccesos() {
        return accesos;
    }

    public void setAccesos(ArrayList<M_menu_item> accesos) {
        this.accesos = accesos;
    }
}
