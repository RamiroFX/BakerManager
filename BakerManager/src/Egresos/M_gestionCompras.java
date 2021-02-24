/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import DB.DB_manager;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_egresoCabecera;
import Entities.M_funcionario;
import Entities.M_proveedor;
import ModeloTabla.EgresoCabeceraTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionCompras {

    private M_egresoCabecera egresoCabecera;
    private EgresoCabeceraTableModel tm;

    public M_gestionCompras() {
        this.egresoCabecera = new M_egresoCabecera();
        this.egresoCabecera.getFuncionario().setIdFuncionario(-1);
        this.egresoCabecera.getProveedor().setId(-1);
        this.egresoCabecera.setNroFactura(-1);
        this.tm = new EgresoCabeceraTableModel(EgresoCabeceraTableModel.SIMPLE);
    }

    public M_egresoCabecera getCabecera() {
        return egresoCabecera;
    }

    public EgresoCabeceraTableModel getTm() {
        return tm;
    }

    public ArrayList<E_tipoOperacion> obtenerTipoOperacion() {
        ArrayList<E_tipoOperacion> list = new ArrayList<>();
        list.add(new E_tipoOperacion(-1, 0, "Todos"));
        list.addAll(DB_manager.obtenerTipoOperaciones());
        return list;
    }

    public ArrayList<Estado> obtenerEstados() {
        ArrayList<Estado> list = new ArrayList<>();
        list.add(new Estado(-1, "Todos"));
        list.addAll(DB_manager.obtenerEstados());
        return list;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.egresoCabecera.setFuncionario(funcionario);
    }

    public String obtenerNombreCompletoFuncionario() {
        String alias = this.egresoCabecera.getFuncionario().getAlias();
        String apellido = this.egresoCabecera.getFuncionario().getApellido();
        String nombre = this.egresoCabecera.getFuncionario().getNombre();
        return nombre + " " + apellido + " (" + alias + ")";
    }

    public void setProveedor(M_proveedor proveedor) {
        this.egresoCabecera.setProveedor(proveedor);
    }

    public String obtenerDescripcionProveedor() {
        String descripcion = "";
        String entidad = this.egresoCabecera.getProveedor().getEntidad();
        String nombre = this.egresoCabecera.getProveedor().getNombre();
        if (nombre != null) {
            if (!nombre.isEmpty()) {
                descripcion = entidad + " (" + nombre + ")";
            }
        } else {
            descripcion = entidad;
        }
        return descripcion;
    }

    public void borrarParametros() {
        this.egresoCabecera.getFuncionario().setIdFuncionario(-1);
        this.egresoCabecera.getProveedor().setId(-1);
        this.egresoCabecera.setNroFactura(-1);
        this.egresoCabecera.getEstado().setId(-1);
    }

    public void actualizarTabla(Date fechaInicio, Date fechaFin, boolean conFecha) {
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
        int idEmpleado = getCabecera().getFuncionario().getIdFuncionario();
        int idProveedor = getCabecera().getProveedor().getId();
        int idTiop = getCabecera().getCondCompra().getId();
        int nroFactura = getCabecera().getNro_factura();
        int idEstado = getCabecera().getEstado().getId();
        getTm().setList(DB_Egreso.obtenerComprasCabecera(idProveedor, nroFactura, idEmpleado, idTiop, idEstado, calendarInicio.getTime(), calendarFinal.getTime(), conFecha));
    }
}
