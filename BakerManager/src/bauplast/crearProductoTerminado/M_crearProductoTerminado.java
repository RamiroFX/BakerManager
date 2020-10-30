/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearProductoTerminado;

import DB.DB_Produccion;
import Entities.E_produccionCabecera;
import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
import Entities.M_menu_item;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.ProduccionDetalleTableModel;
import ModeloTabla.ProduccionRolloTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearProductoTerminado {

    private E_produccionCabecera produccionCabecera;
    private ProduccionDetalleTableModel productosTerminadosTM;
    private ProduccionRolloTableModel rolloUtilizadoTM;
    private ArrayList<M_menu_item> accesos;

    public M_crearProductoTerminado() {
        this.produccionCabecera = new E_produccionCabecera();
        this.produccionCabecera.setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.productosTerminadosTM = new ProduccionDetalleTableModel(ProduccionDetalleTableModel.SIMPLE);
        this.rolloUtilizadoTM = new ProduccionRolloTableModel();
        this.accesos = DatosUsuario.getRol_usuario().getAccesos();
    }

    public E_produccionCabecera getProduccionCabecera() {
        return produccionCabecera;
    }

    public void setProduccionCabecera(E_produccionCabecera produccionCabecera) {
        this.produccionCabecera = produccionCabecera;
    }

    public void setRolloUtilizadoTm(ProduccionRolloTableModel tm) {
        this.rolloUtilizadoTM = tm;
    }

    public ProduccionRolloTableModel getRolloUtilizadoTm() {
        return rolloUtilizadoTM;
    }

    public ProduccionDetalleTableModel getProductosTerminadosTM() {
        return productosTerminadosTM;
    }

    public void setProductosTerminadosTM(ProduccionDetalleTableModel productosTerminadosTM) {
        this.productosTerminadosTM = productosTerminadosTM;
    }

    public void agregarRolloUtilizado(E_produccionFilm producto) {
        getRolloUtilizadoTm().agregarDatos(producto);
    }

    public void agregarRolloUtilizadoPosterior(E_produccionFilm detalle) {
        DB_Produccion.insertarProduccionRolloPosterior(getProduccionCabecera(), detalle);
        consultarProduccion();
    }

    public void modificarRolloUtilizado(int index, E_produccionFilm producto) {
        getRolloUtilizadoTm().modificarDatos(index, producto);
    }

    public void modificarRolloUtilizadoPosterior(int index, E_produccionFilm newPF) {
        E_produccionFilm currentPF = getRolloUtilizadoTm().getList().get(index);
        DB_Produccion.actualizarRolloUtilizadoPosterior(currentPF, newPF);
        consultarProduccion();
    }

    public void removerRolloUtilizado(int index) {
        getRolloUtilizadoTm().quitarDatos(index);
    }

    public void removerRolloUtilizadoPosterior(int index) {
        E_produccionFilm currentPF = getRolloUtilizadoTm().getList().get(index);
        DB_Produccion.eliminarRolloUtilizadoPosterior(currentPF);
        consultarProduccion();
    }

    public void agregarProductoTerminado(E_produccionDetalle produccion) {
        getProductosTerminadosTM().agregarDetalle(produccion);
    }

    public void agregarProductoTerminadoPosterior(E_produccionDetalle produccion) {
        DB_Produccion.insertarProduccionProdTerminadoPosterior(getProduccionCabecera(), produccion);
        consultarProduccion();
    }

    public void modificarProductoTerminado(int index, double peso) {
        getProductosTerminadosTM().modificarCantidadDetalle(index, peso);
    }

    public void modificarProductoTerminadoPosterior(int index, double peso) {
        E_produccionDetalle produccion = getProductosTerminadosTM().getList().get(index);
        DB_Produccion.actualizarProductoTerminadoPosterior(produccion, peso);
        consultarProduccion();
    }

    public void removerProductoTerminado(int index) {
        getProductosTerminadosTM().quitarDetalle(index);
    }

    public void removerProductoTerminadoPosterior(int index) {
        E_produccionDetalle currentProd = getProductosTerminadosTM().getList().get(index);
        DB_Produccion.eliminarProductoTerminadoPosterior(currentProd);
        consultarProduccion();
    }

    boolean existeOrdenTrabajoPorProduccion(int ordenTrabajo, E_produccionTipo tipoProduccion) {
        return DB_Produccion.existeOrdenTrabajo(ordenTrabajo, tipoProduccion);
    }

    public ArrayList<E_produccionTipo> obtenerProduccionTipo() {
        return DB_Produccion.obtenerTipoProduccion();
    }

    public void guardarProduccion() {
        DB_Produccion.insertarProduccionProdTerminados(getProduccionCabecera(), getProductosTerminadosTM().getList(), getRolloUtilizadoTm().getList());
    }

    public void limpiarCampos() {
        setProduccionCabecera(new E_produccionCabecera());
        getRolloUtilizadoTm().vaciarLista();
        getProductosTerminadosTM().vaciarLista();
    }

    public void consultarProduccion() {
        getProductosTerminadosTM().setList(DB_Produccion.consultarProduccionDetalle(getProduccionCabecera().getId()));
        getRolloUtilizadoTm().setList(DB_Produccion.consultarProduccionFilmBaja(getProduccionCabecera().getId()));
    }

    public String getFechaProduccionFormateada() {
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdfs.format(getProduccionCabecera().getFechaRegistro());
    }

    public ArrayList<M_menu_item> getAccesos() {
        return accesos;
    }

    public E_produccionFilm obtenerRollo(int index) {
        E_produccionFilm currentPF = getRolloUtilizadoTm().getList().get(index);
        return DB_Produccion.obtenerFilm(currentPF.getOrdenTrabajoDetalle());
    }

}
