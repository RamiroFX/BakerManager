/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import DB.DB_NotaCredito;
import DB.DB_manager;
import Entities.E_NotaCreditoCabecera;
import Entities.E_NotaCreditoDetalle;
import Entities.E_tipoOperacion;
import Entities.Estado;
import ModeloTabla.NotaCreditoCabeceraTableModel;
import ModeloTabla.NotaCreditoDetalleTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro
 */
public class M_gestionNotasCredito {

    private E_NotaCreditoCabecera cabecera;
    private NotaCreditoCabeceraTableModel tmCabecera;
    private NotaCreditoDetalleTableModel tmDetalle;

    public M_gestionNotasCredito() {
        this.cabecera = new E_NotaCreditoCabecera();
        this.cabecera.getFuncionario().setIdFuncionario(-1);
        this.cabecera.getCliente().setIdCliente(-1);
        this.tmCabecera = new NotaCreditoCabeceraTableModel();
        this.tmDetalle = new NotaCreditoDetalleTableModel();
    }

    public E_NotaCreditoCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(E_NotaCreditoCabecera cabecera) {
        this.cabecera = cabecera;
    }

    public NotaCreditoCabeceraTableModel getTm() {
        return tmCabecera;
    }

    public void setTm(NotaCreditoCabeceraTableModel tm) {
        this.tmCabecera = tm;
    }

    /**
     * @return the tmDetalle
     */
    public NotaCreditoDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    /**
     * @param tmDetalle the tmDetalle to set
     */
    public void setTmDetalle(NotaCreditoDetalleTableModel tmDetalle) {
        this.tmDetalle = tmDetalle;
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

    public void borrarDatos() {
        this.cabecera.getFuncionario().setNombre("");
        this.cabecera.getFuncionario().setIdFuncionario(-1);
        this.cabecera.getCliente().setEntidad("");
        this.cabecera.getCliente().setIdCliente(-1);
    }

    String obtenerNombreFuncionario() {
        String alias = this.getCabecera().getFuncionario().getAlias();
        String nombre = this.getCabecera().getFuncionario().getNombre();
        String apellido = this.getCabecera().getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
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

    public List<E_NotaCreditoCabecera> obtenerNotasCreditoCabecera(int idCliente, int idFuncionario, int nroNotaCredito, Date fechaInicio, Date fechaFinal,
            int idTipoOperacion, int idEstado) {
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(fechaFinal);
        calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
        calendarFinal.set(Calendar.MINUTE, 59);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);
        return DB_NotaCredito.obtenerNotasCredito(idCliente, idFuncionario, nroNotaCredito, 
                calendarInicio.getTime(), calendarFinal.getTime(), idTipoOperacion, idEstado);

    }

    public List<E_NotaCreditoDetalle> obtenerNotasCreditoDetalle(int idNotaCreditoCabecera) {
        return DB_NotaCredito.obtenerNotasCreditoDetalle(idNotaCreditoCabecera);
    }

    public ArrayList<Estado> obtenerEstados() {
        ArrayList<Estado> estados = DB_manager.obtenerEstados();
        estados.add(new Estado(-1, "Todos"));
        return estados;
    }

    public void anularNotaCredito(int idNotaCreditoCabecera, int idEstado, boolean recuperarNroNotaCredito) {
        DB_NotaCredito.anularNotaCredito(idNotaCreditoCabecera, idEstado, recuperarNroNotaCredito);
    }
}
