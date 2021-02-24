/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UsoMateriaPrima;

import DB.DB_UtilizacionMateriaPrima;
import DB.DB_manager;
import Entities.E_utilizacionMateriaPrimaDetalle;
import Entities.Estado;
import Entities.M_funcionario;
import ModeloTabla.UtilizacionMPCabeceraTableModel;
import ModeloTabla.UtilizacionMPDetalleTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionMateriaPrima {

    M_funcionario funcionario;
    private UtilizacionMPCabeceraTableModel utilizacionMPCabeceraTM;
    private UtilizacionMPDetalleTableModel utilizacionMPDetalleTM;

    public M_gestionMateriaPrima() {
        this.funcionario = new M_funcionario();
        this.funcionario.setIdFuncionario(-1);
        this.utilizacionMPCabeceraTM = new UtilizacionMPCabeceraTableModel();
        this.utilizacionMPDetalleTM = new UtilizacionMPDetalleTableModel();
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public UtilizacionMPCabeceraTableModel getUtilizacionMPCabeceraTM() {
        return utilizacionMPCabeceraTM;
    }

    public void setUtilizacionMPCabeceraTM(UtilizacionMPCabeceraTableModel dtm) {
        this.utilizacionMPCabeceraTM = dtm;
    }

    public UtilizacionMPDetalleTableModel getUtilizacionMPDetalleTM() {
        return utilizacionMPDetalleTM;
    }

    public void setUtilizacionMPDetalleTM(UtilizacionMPDetalleTableModel produccionDetalleTM) {
        this.utilizacionMPDetalleTM = produccionDetalleTM;
    }

    public void borrarDatos() {
        this.funcionario.setIdFuncionario(-1);
        getUtilizacionMPDetalleTM().vaciarLista();
        getUtilizacionMPCabeceraTM().vaciarLista();

    }

    public ArrayList<Estado> obtenerEstados() {
        ArrayList<Estado> estados = DB_manager.obtenerEstados();
        estados.add(new Estado(-1, "Todos"));
        return estados;
    }

    public UtilizacionMPCabeceraTableModel consultarUtilizacionMP(Date fechaInicio, Date fechaFin, int nroPedido, Estado estado) {
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
        getUtilizacionMPCabeceraTM().setList(DB_UtilizacionMateriaPrima.consultarUtilizacionMateriaCabeceras(calendarInicio.getTime(), calendarFinal.getTime(), nroPedido, estado.getId(), funcionario.getIdFuncionario()));
        return getUtilizacionMPCabeceraTM();
    }

    public UtilizacionMPDetalleTableModel obtenerRegistroMateriaPrimaDetalle(Integer idUtilizacionMPCabecera) {
        getUtilizacionMPDetalleTM().setList(DB_UtilizacionMateriaPrima.consultarUtilizacionMateriaPrimaDetalle(idUtilizacionMPCabecera));
        return getUtilizacionMPDetalleTM();
    }

    public void anularUsoMateriaPrima(int idUtilizacionMPCabecera) {
        List<E_utilizacionMateriaPrimaDetalle> detalle = DB_UtilizacionMateriaPrima.consultarUtilizacionMateriaPrimaDetalle(idUtilizacionMPCabecera);
        DB_UtilizacionMateriaPrima.anularUtilizacionMateriaPrima(idUtilizacionMPCabecera, detalle);
    }

    public Estado getUsoMateriPrimaEstado(int idUtilizacionMPCabecera) {
        Estado estado = getUtilizacionMPCabeceraTM().getList().get(idUtilizacionMPCabecera).getEstado();
        return estado;
    }
}
