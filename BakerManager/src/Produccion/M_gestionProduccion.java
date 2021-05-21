/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import DB.DB_Egreso;
import DB.DB_Produccion;
import DB.DB_manager;
import Entities.E_produccionDesperdicioCabecera;
import Entities.E_produccionDetalle;
import Entities.E_produccionTipo;
import Entities.Estado;
import Entities.M_funcionario;
import Entities.M_menu_item;
import MenuPrincipal.DatosUsuario;
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
public class M_gestionProduccion {

    M_funcionario funcionario;
    private ProduccionCabeceraTableModel produccionCabeceraTM;
    private ProduccionDetalleTableModel produccionDetalleTM;
    private ArrayList<M_menu_item> accesos;

    public M_gestionProduccion() {
        this.funcionario = new M_funcionario();
        this.funcionario.setIdFuncionario(-1);
        this.produccionCabeceraTM = new ProduccionCabeceraTableModel();
        this.produccionDetalleTM = new ProduccionDetalleTableModel(ProduccionDetalleTableModel.SIMPLE);
        this.accesos = DatosUsuario.getRol_usuario().getAccesos();
    }

    public ArrayList<M_menu_item> getAccesos() {
        return accesos;
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
        this.funcionario.setIdFuncionario(-1);
        getProduccionDetalleTM().vaciarLista();
        getProduccionCabeceraTM().vaciarLista();

    }

    Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    public ArrayList<Estado> obtenerEstados() {
        ArrayList<Estado> estados = DB_manager.obtenerEstados();
        estados.add(new Estado(-1, "Todos"));
        return estados;
    }

    public void consultarProduccion(Date fechaInicio, Date fechaFin, E_produccionTipo prodTipo, int nroPedido, Estado estado, boolean conFecha) {
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
        getProduccionCabeceraTM().setList(DB_Produccion.consultarProduccion(calendarInicio.getTime(), calendarFinal.getTime(), prodTipo.getId(), nroPedido, estado.getId(), funcionario.getIdFuncionario(), conFecha));
    }

    public ArrayList<E_produccionTipo> obtenerProduccionTipo() {
        ArrayList<E_produccionTipo> produccionTipos = new ArrayList<>();
        produccionTipos.add(new E_produccionTipo(-1, "Todos"));
        produccionTipos.addAll(DB_Produccion.obtenerTipoProduccion());
        return produccionTipos;
    }

    public void obtenerProduccionDetalle(Integer idProduccion) {
        getProduccionDetalleTM().setList(DB_Produccion.consultarProduccionDetalle(idProduccion));
    }

    public void anularProduccion(int idProduccion) {
        List<E_produccionDetalle> detalle = DB_Produccion.consultarProduccionDetalle(idProduccion);
        DB_Produccion.anularProduccion(idProduccion, detalle);
        DB_Produccion.anularProduccionFilm(idProduccion);
    }

    public Estado getProduccionEstado(int idProduccion) {
        Estado estado = getProduccionCabeceraTM().getList().get(idProduccion).getEstado();
        return estado;
    }
    
    public boolean existeProduccionDesperdicio(int idProduccion){
        E_produccionDesperdicioCabecera pdc = DB_Produccion.obtenerProduccionCabeceraDesperdicio(idProduccion);
        return pdc != null;
    }
    
    public E_produccionDesperdicioCabecera obtenerProduccionDesperdicioCabecera(int idProduccion){
         return DB_Produccion.obtenerProduccionCabeceraDesperdicio(idProduccion);
    }
}
