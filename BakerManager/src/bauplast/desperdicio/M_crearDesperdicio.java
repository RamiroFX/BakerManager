/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_produccionCabecera;
import Entities.E_produccionCabeceraDesperdicio;
import Entities.E_productoClasificacion;
import Entities.Estado;
import Entities.M_producto;
import ModeloTabla.ProduccionDetalleTableModel;
import ModeloTabla.ProduccionRolloTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearDesperdicio {

    M_producto producto;
    E_produccionCabeceraDesperdicio produccionCabecera;
    ProduccionDetalleTableModel produccionTerminadosTM;
    ProduccionRolloTableModel produccionRollosTM;

    public M_crearDesperdicio() {
        this.producto = new M_producto();
        this.produccionCabecera = new E_produccionCabeceraDesperdicio();
        this.produccionTerminadosTM = new ProduccionDetalleTableModel();
        this.produccionRollosTM = new ProduccionRolloTableModel();
    }

    public void setProduccionCabecera(E_produccionCabecera pc) {
        this.produccionCabecera.setProduccionCabecera(pc);
    }

    public E_produccionCabeceraDesperdicio getProduccionCabecera() {
        return produccionCabecera;
    }

    public void setProduccionCabecera(E_produccionCabeceraDesperdicio produccionCabecera) {
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

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    public M_producto getProducto() {
        return producto;
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

}
