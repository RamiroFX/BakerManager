/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import DB.DB_Produccion;
import Entities.E_produccionCabecera;
import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
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
public class M_crearProductoTerminado {

    private E_produccionCabecera produccionCabecera;
    private ProduccionDetalleTableModel productosTerminadosTM;
    private ProduccionRolloTableModel rolloUtilizadoTM;

    public M_crearProductoTerminado() {
        this.produccionCabecera = new E_produccionCabecera();
        this.produccionCabecera.setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.productosTerminadosTM = new ProduccionDetalleTableModel();
        this.rolloUtilizadoTM = new ProduccionRolloTableModel();
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

    public void modifacarRolloUtilizado(int index, E_produccionFilm producto) {
        getRolloUtilizadoTm().modificarDatos(index, producto);
    }

    public void removerRolloUtilizado(int index) {
        getRolloUtilizadoTm().quitarDatos(index);
    }

    public void agregarProductoTerminado(double cantidad, M_producto producto) {
        E_produccionDetalle produccion = new E_produccionDetalle();
        produccion.setCantidad(cantidad);
        produccion.setProducto(producto);
        getProductosTerminadosTM().agregarDetalle(produccion);
    }

    public void modificarProductoTerminado(int index, double peso) {
        getProductosTerminadosTM().modificarCantidadDetalle(index, peso);
    }

    public void removerProductoTerminado(int index) {
        getProductosTerminadosTM().quitarDetalle(index);
    }

    boolean existeOrdenTrabajo(int ordenTrabajo) {
        return DB_Produccion.existeOrdenTrabajo(ordenTrabajo);
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
}
