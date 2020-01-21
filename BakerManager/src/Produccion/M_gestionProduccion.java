/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import DB.DB_Egreso;
import DB.DB_Produccion;
import DB.DB_manager;
import Entities.E_produccionCabecera;
import Entities.E_produccionTipo;
import Entities.Estado;
import Entities.M_funcionario;
import ModeloTabla.ProduccionCabeceraTableModel;
import ModeloTabla.ProduccionDetalleTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionProduccion {

    M_funcionario funcionario;
    private ProduccionCabeceraTableModel tableModel;

    public M_gestionProduccion() {
        this.funcionario = new M_funcionario();
        this.funcionario.setId_funcionario(-1);
        this.tableModel = new ProduccionCabeceraTableModel();
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public ProduccionCabeceraTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(ProduccionCabeceraTableModel dtm) {
        this.tableModel = dtm;
    }

    public void borrarDatos() {

    }

    Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    public ArrayList<Estado> obtenerEstados() {
        return DB_manager.obtenerEstados();
    }

    public ProduccionCabeceraTableModel consultarProduccion(Date fechaInicio, Date fechaFin, E_produccionTipo prodTipo, int nroPedido, Estado estado) {
        
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
        getTableModel().setList(DB_Produccion.consultarProduccion(calendarInicio.getTime(), calendarFinal.getTime(), prodTipo.getId(), nroPedido, estado.getId(), funcionario.getId_funcionario()));
        return getTableModel();
    }

    public ArrayList<E_produccionTipo> obtenerProduccionTipo() {
        return DB_Produccion.obtenerTipoProduccion();
    }

}
