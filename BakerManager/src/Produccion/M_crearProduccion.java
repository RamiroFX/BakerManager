/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import DB.DB_Produccion;
import Entities.E_produccionCabecera;
import Entities.E_produccionDetalle;
import Entities.E_produccionTipo;
import Entities.M_producto;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.ProduccionDetalleTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearProduccion {

    E_produccionCabecera produccionCabecera;
    ProduccionDetalleTableModel tm;

    public M_crearProduccion() {
        this.produccionCabecera = new E_produccionCabecera();
        this.produccionCabecera.setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.tm = new ProduccionDetalleTableModel();
    }

    public E_produccionCabecera getProduccionCabecera() {
        return produccionCabecera;
    }

    public void setProduccionCabecera(E_produccionCabecera produccionCabecera) {
        this.produccionCabecera = produccionCabecera;
    }

    public void setTm(ProduccionDetalleTableModel tm) {
        this.tm = tm;
    }

    public ProduccionDetalleTableModel getTm() {
        return tm;
    }

    public void agregarDetalle(double cantidad, M_producto producto) {
        E_produccionDetalle produccion = new E_produccionDetalle();
        produccion.setCantidad(cantidad);
        produccion.setProducto(producto);
        getTm().agregarDetalle(produccion);
    }

    public void modificarDetalle(int index, double cantidad) {
        getTm().modificarCantidadDetalle(index, cantidad);
    }

    public void removerDetalle(int index) {
        getTm().quitarDetalle(index);
    }

    boolean existeOrdenTrabajo(int ordenTrabajo) {
        return DB_Produccion.existeOrdenTrabajo(ordenTrabajo);
    }

    public ArrayList<E_produccionTipo> obtenerProduccionTipo() {
        return DB_Produccion.obtenerTipoProduccion();
    }

    public void guardarProduccion() {
        DB_Produccion.insertarProduccion(getProduccionCabecera(), getTm().getList());
    }

    public void limpiarCampos() {
        setProduccionCabecera(new E_produccionCabecera());
        getTm().vaciarLista();
    }
}
