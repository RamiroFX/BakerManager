/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UsoMateriaPrima;

import DB.DB_Egreso;
import DB.DB_Produccion;
import DB.DB_manager;
import Entities.E_produccionDetalle;
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

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionMateriaPrima {

    M_funcionario funcionario;
    private ProduccionCabeceraTableModel produccionCabeceraTM;
    private ProduccionDetalleTableModel produccionDetalleTM;

    public M_gestionMateriaPrima() {
        this.funcionario = new M_funcionario();
        this.funcionario.setId_funcionario(-1);
        this.produccionCabeceraTM = new ProduccionCabeceraTableModel();
        this.produccionDetalleTM = new ProduccionDetalleTableModel();
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public ProduccionCabeceraTableModel getProduccionCabeceraTM() {
        return produccionCabeceraTM;
    }

    public void setProduccionCabeceraTM(ProduccionCabeceraTableModel dtm) {
        this.produccionCabeceraTM = dtm;
    }

    public ProduccionDetalleTableModel getProduccionDetalleTM() {
        return produccionDetalleTM;
    }

    public void setProduccionDetalleTM(ProduccionDetalleTableModel produccionDetalleTM) {
        this.produccionDetalleTM = produccionDetalleTM;
    }

    public void borrarDatos() {
        this.funcionario.setId_funcionario(-1);
        getProduccionDetalleTM().vaciarLista();
        getProduccionCabeceraTM().vaciarLista();

    }

    public ArrayList<Estado> obtenerEstados() {
        ArrayList<Estado> estados = DB_manager.obtenerEstados();
        estados.add(new Estado(-1, "Todos"));
        return estados;
    }

    public ProduccionCabeceraTableModel consultarProduccion(Date fechaInicio, Date fechaFin, int nroPedido, Estado estado) {
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
        //getProduccionCabeceraTM().setList(DB_Produccion.consultarProduccion(calendarInicio.getTime(), calendarFinal.getTime(), prodTipo.getId(), nroPedido, estado.getId(), funcionario.getId_funcionario()));
        return getProduccionCabeceraTM();
    }

    public ProduccionDetalleTableModel obtenerRegistroMateriaPrimaDetalle(Integer idProduccion) {
        getProduccionDetalleTM().setList(DB_Produccion.consultarProduccionDetalle(idProduccion));
        return getProduccionDetalleTM();
    }

    public void anularUsoMateriaPrima(int idProduccion) {
        /*List<E_produccionDetalle> detalle = DB_Produccion.consultarProduccionDetalle(idProduccion);
        DB_Produccion.anularUsoMateriaPrima(idProduccion, detalle);*/
    }

    public Estado getUsoMateriPrimaEstado(int idProduccion) {
        Estado estado = getProduccionCabeceraTM().getList().get(idProduccion).getEstado();
        return estado;
    }
}
