/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearRollo;

import DB.DB_Produccion;
import Entities.E_produccionCabecera;
import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
import Entities.M_menu_item;
import Entities.M_producto;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.ProduccionDetalleTableModel;
import ModeloTabla.ProduccionRolloTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearRollo {

    private E_produccionCabecera produccionCabecera;
    private ProduccionRolloTableModel tm;
    private ProduccionDetalleTableModel produccionDetalleTM;
    private ArrayList<M_menu_item> accesos;

    public M_crearRollo() {
        this.produccionCabecera = new E_produccionCabecera();
        this.produccionCabecera.setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.tm = new ProduccionRolloTableModel();
        this.produccionDetalleTM = new ProduccionDetalleTableModel();
        this.accesos = DatosUsuario.getRol_usuario().getAccesos();
    }

    public ArrayList<M_menu_item> getAccesos() {
        return accesos;
    }

    public E_produccionCabecera getProduccionCabecera() {
        return produccionCabecera;
    }

    public void setProduccionCabecera(E_produccionCabecera produccionCabecera) {
        this.produccionCabecera = produccionCabecera;
    }

    public void setTm(ProduccionRolloTableModel tm) {
        this.tm = tm;
    }

    public ProduccionRolloTableModel getRollosTM() {
        return tm;
    }

    public ProduccionDetalleTableModel getMateriaPrimaTM() {
        return produccionDetalleTM;
    }

    public void setProduccionDetalleTM(ProduccionDetalleTableModel produccionDetalleTM) {
        this.produccionDetalleTM = produccionDetalleTM;
    }

    public void agregarDetalle(E_produccionFilm producto) {
        getRollosTM().agregarDatos(producto);
    }

    public void agregarDetallePosterior(E_produccionFilm producto) {
        DB_Produccion.insertarProdTerminadoPosterior(getProduccionCabecera(), producto);
        consultarProduccion();
    }

    public void modificarDetalle(int index, E_produccionFilm pf) {
        getRollosTM().modificarDatos(index, pf);
    }

    public void removerDetalle(int index) {
        getRollosTM().quitarDatos(index);
    }

    public void agregarMPDetalle(double cantidad, M_producto producto) {
        E_produccionDetalle mp = new E_produccionDetalle();
        mp.setCantidad(cantidad);
        mp.setProducto(producto);
        getMateriaPrimaTM().agregarDetalle(mp);
    }

    public void agregarMPDetallePosterior(double cantidad, M_producto producto) {
        E_produccionDetalle produccion = new E_produccionDetalle();
        produccion.setCantidad(cantidad);
        produccion.setProducto(producto);
        DB_Produccion.insertarUsoMateriaPrimaPosterior(getProduccionCabecera(), produccion);
        consultarProduccion();
    }

    public void modificarMPDetalle(int index, double cantidad) {
        getMateriaPrimaTM().modificarCantidadDetalle(index, cantidad);
    }

    public void modificarMPDetallePosterior(int index, double cantidad) {
        E_produccionDetalle pd = getMateriaPrimaTM().getList().get(index);
        DB_Produccion.actualizarUsoMateriaPrimaPosterior(pd, cantidad);
        consultarProduccion();
    }

    public void removerMPDetalle(int index) {
        getMateriaPrimaTM().quitarDetalle(index);
    }

    public void removerMPDetallePosterior(int index) {
        E_produccionDetalle pd = getMateriaPrimaTM().getList().get(index);
        DB_Produccion.eliminarUsoMateriaPrimaDetalle(pd.getId());
        consultarProduccion();
    }

    boolean existeOrdenTrabajo(int ordenTrabajo) {
        return DB_Produccion.existeOrdenTrabajo(ordenTrabajo);
    }

    boolean existeOrdenTrabajoPorProduccion(int ordenTrabajo, E_produccionTipo tipoProduccion) {
        return DB_Produccion.existeOrdenTrabajo(ordenTrabajo, tipoProduccion);
    }

    public ArrayList<E_produccionTipo> obtenerProduccionTipo() {
        return DB_Produccion.obtenerTipoProduccion();
    }

    public void guardarProduccion() {
        DB_Produccion.insertarProduccionFilm(getProduccionCabecera(), getRollosTM().getList(), getMateriaPrimaTM().getList());
    }

    public void limpiarCampos() {
        setProduccionCabecera(new E_produccionCabecera());
        getRollosTM().vaciarLista();
    }

    public String getFechaProduccionFormateada() {
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdfs.format(getProduccionCabecera().getFechaRegistro());
    }

    public void consultarProduccion() {
        getRollosTM().setList(DB_Produccion.consultarProduccionFilm(getProduccionCabecera().getId()));
        getMateriaPrimaTM().setList(DB_Produccion.consultarUtilizacionMP(getProduccionCabecera().getId()));
    }

}
