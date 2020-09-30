/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import DB.DB_Produccion;
import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_produccionCabecera;
import Entities.E_produccionDesperdicioCabecera;
import Entities.E_produccionDesperdicioDetalle;
import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
import Entities.E_productoClasificacion;
import Entities.Estado;
import Entities.M_producto;
import ModeloTabla.ProduccionDetalleTableModel;
import ModeloTabla.ProduccionRolloTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearDesperdicio {

    E_produccionDesperdicioCabecera produccionCabecera;
    ProduccionDetalleTableModel produccionTerminadosTM, produccionRecuperadosTM;
    ProduccionRolloTableModel produccionRollosTM;

    public M_crearDesperdicio() {
        this.produccionCabecera = new E_produccionDesperdicioCabecera();
        this.produccionTerminadosTM = new ProduccionDetalleTableModel();
        this.produccionRecuperadosTM = new ProduccionDetalleTableModel();
        this.produccionRollosTM = new ProduccionRolloTableModel();
    }

    public void setProduccionCabecera(E_produccionCabecera pc) {
        this.produccionCabecera.setProduccionCabecera(pc);
    }

    public E_produccionDesperdicioCabecera getProduccionCabecera() {
        return produccionCabecera;
    }

    public void setProduccionCabecera(E_produccionDesperdicioCabecera produccionCabecera) {
        this.produccionCabecera = produccionCabecera;
    }

    public ProduccionRolloTableModel getProduccionRollosTM() {
        return produccionRollosTM;
    }

    public void setProduccionRollosTM(ProduccionRolloTableModel produccionRollosTM) {
        this.produccionRollosTM = produccionRollosTM;
    }

    public ProduccionDetalleTableModel getProduccionTerminadosTM() {
        return produccionTerminadosTM;
    }

    public void setProduccionTerminadosTM(ProduccionDetalleTableModel produccionTerminadosTM) {
        this.produccionTerminadosTM = produccionTerminadosTM;
    }

    public void setProduccionRecuperadosTM(ProduccionDetalleTableModel produccionRecuperadosTM) {
        this.produccionRecuperadosTM = produccionRecuperadosTM;
    }

    public ProduccionDetalleTableModel getProduccionRecuperadosTM() {
        return produccionRecuperadosTM;
    }

    public ArrayList<E_productoClasificacion> obtenerTipoMateriaPrima() {
        return DB_Producto.obtenerProductoCategoriaBauplast();
    }

    public ArrayList<Estado> obtenerEstado() {
        return DB_manager.obtenerEstados();
    }

    public String obtenerFuncionario() {
        return this.produccionCabecera.getProduccionCabecera().getFuncionarioProduccion().getNombre();
    }

    public String obtenerOrdenTrabajo() {
        return this.produccionCabecera.getProduccionCabecera().getNroOrdenTrabajo() + "";
    }

    public int obtenerTipoProduccion() {
        return getProduccionCabecera().getProduccionCabecera().getTipo().getId();
    }

    /*
    INICIO ROLLOS CRUD
     */
    public void agregarFilm(E_produccionFilm detalle) {
        detalle.setPesoActual(obtenerRollo(detalle.getId()).getPesoActual());
        getProduccionRollosTM().agregarDatos(detalle);
    }

    public void agregarFilmPosterior(E_produccionFilm detalle) {
        //TODO
    }

    public void modificarFilm(int index, E_produccionFilm detalle) {
        getProduccionRollosTM().modificarDatos(index, detalle);
    }

    public void modificarFilmPosterior(int index, E_produccionFilm detalle) {
        //TODO
    }

    public void removerFilm(int index) {
        getProduccionRollosTM().quitarDatos(index);
    }

    public void removerFilmPosterior(int index) {
        //TODO
    }

    /*
    FIN ROLLOS CRUD
     */
 /*
    INICIO PRODUCTOS TERMINADOS CRUD
     */
    public void agregarTerminados(E_produccionDetalle detalle) {
        getProduccionTerminadosTM().agregarDetalle(detalle);
    }

    public void agregarTerminadosPosterior(E_produccionDetalle detalle) {
        //TODO
    }

    public void modificarTerminados(int index, E_produccionDetalle detalle) {
        getProduccionTerminadosTM().modificarCantidadDetalle(index, detalle.getCantidad());
    }

    public void modificarTerminadosPosterior(int index, E_produccionDetalle detalle) {
        //TODO
    }

    public void removerTerminado(int index) {
        getProduccionTerminadosTM().quitarDetalle(index);
    }

    public void removerTerminadoPosterior(int index) {
        //TODO
    }

    /*
    FIN PRODUCTOS TERMINADOS CRUD
     */

 /*
    INICIO MATERIA RECUPERADA CRUD
     */
    public void agregarRecuperado(double cantidad, M_producto producto) {
        E_produccionDetalle pd = new E_produccionDetalle();
        pd.setCantidad(cantidad);
        pd.setProducto(producto);
        getProduccionRecuperadosTM().agregarDetalle(pd);
    }

    public void agregarRecuperadoPosterior(double cantidad, M_producto producto) {
        //TODO
    }

    public void modificarRecuperado(int posicion, double cantidad) {
        getProduccionRecuperadosTM().modificarCantidadDetalle(posicion, cantidad);
    }

    public void modificarRecuperadoPosterior(int posicion, double cantidad) {
        //TODO
    }

    public void removerRecuperado(int index) {
        getProduccionRecuperadosTM().quitarDetalle(index);
    }

    public void removerRecuperadoPosterior(int index) {
        //TODO
    }

    /*
    FIN MATERIA RECUPERADA CRUD
     */
    
    public E_produccionFilm obtenerRollo(int idFilm) {
        return DB_Produccion.obtenerFilm(idFilm);
    }

    public void guardar() {
        List<E_produccionDesperdicioDetalle> recuperados = new ArrayList();
        for (E_produccionDetalle unRecuperado : getProduccionRecuperadosTM().getList()) {
            recuperados.add(new E_produccionDesperdicioDetalle(unRecuperado));
        }
        List<E_produccionDesperdicioDetalle> desperdicios = new ArrayList();
        switch (obtenerTipoProduccion()) {
            case E_produccionTipo.PRODUCTO_TERMINADO: {
                for (E_produccionDetalle unTerminado : getProduccionTerminadosTM().getList()) {
                    desperdicios.add(new E_produccionDesperdicioDetalle(unTerminado));
                }
                DB_Produccion.insertarProduccionTerminadosDesperdicio(produccionCabecera, desperdicios, recuperados);
                break;
            }
            case E_produccionTipo.ROLLO: {
                DB_Produccion.insertarProduccionRollosDesperdicio(produccionCabecera, getProduccionRollosTM().getList(), recuperados);
                break;
            }
        }
    }
}