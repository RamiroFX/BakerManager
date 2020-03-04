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
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearProductoTerminado {

    E_produccionCabecera produccionCabecera;
    private ProduccionDetalleTableModel productosTerminadosTM;
    ProduccionRolloTableModel rolloUtilizadoTM;

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

    public void agregarRolloUtilizado(E_produccionFilm producto) {
        getRolloUtilizadoTm().agregarDatos(producto);
    }

    public void modificarDetalle(int index, E_produccionFilm pf) {
        getRolloUtilizadoTm().modificarDatos(index, pf);
    }

    public void removerDetalle(int index) {
        getRolloUtilizadoTm().quitarDatos(index);
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
    }

    /**
     * @return the productosTerminadosTM
     */
    public ProduccionDetalleTableModel getProductosTerminadosTM() {
        return productosTerminadosTM;
    }

    /**
     * @param productosTerminadosTM the productosTerminadosTM to set
     */
    public void setProductosTerminadosTM(ProduccionDetalleTableModel productosTerminadosTM) {
        this.productosTerminadosTM = productosTerminadosTM;
    }

    public void agregarProductoTerminado(double cantidad, M_producto producto) {
        E_produccionDetalle produccion = new E_produccionDetalle();
        produccion.setCantidad(cantidad);
        produccion.setProducto(producto);
        getProductosTerminadosTM().agregarDetalle(produccion);
    }
}
