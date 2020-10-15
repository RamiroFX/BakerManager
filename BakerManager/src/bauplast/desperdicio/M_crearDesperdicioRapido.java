/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_produccionCabecera;
import Entities.E_produccionDesperdicioCabecera;
import Entities.E_produccionDetalle;
import Entities.E_productoClasificacion;
import Entities.Estado;
import Entities.M_producto;
import ModeloTabla.ProduccionDetalleTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearDesperdicioRapido {

    E_produccionDesperdicioCabecera produccionDesperdicioCabecera;
    ProduccionDetalleTableModel desperdicioTM;

    public M_crearDesperdicioRapido() {
        this.produccionDesperdicioCabecera = new E_produccionDesperdicioCabecera();
        this.desperdicioTM = new ProduccionDetalleTableModel();
    }

    public void setProduccionCabecera(E_produccionCabecera pc) {
        this.produccionDesperdicioCabecera.setProduccionCabecera(pc);
    }

    public E_produccionDesperdicioCabecera getProduccionCabecera() {
        return produccionDesperdicioCabecera;
    }

    public void setProduccionCabecera(E_produccionDesperdicioCabecera produccionCabecera) {
        this.produccionDesperdicioCabecera = produccionCabecera;
    }

    public ProduccionDetalleTableModel getDesperdicioTM() {
        return desperdicioTM;
    }

    public void setDesperdicioTM(ProduccionDetalleTableModel desperdicioTM) {
        this.desperdicioTM = desperdicioTM;
    }

    public ArrayList<E_productoClasificacion> obtenerTipoMateriaPrima() {
        return DB_Producto.obtenerProductoCategoriaBauplast();
    }

    public ArrayList<Estado> obtenerEstado() {
        return DB_manager.obtenerEstados();
    }

    public String obtenerFuncionario() {
        return this.produccionDesperdicioCabecera.getProduccionCabecera().getFuncionarioProduccion().getNombre();
    }

    public String obtenerOrdenTrabajo() {
        return this.produccionDesperdicioCabecera.getProduccionCabecera().getNroOrdenTrabajo() + "";
    }

    public int obtenerTipoProduccion() {
        return getProduccionCabecera().getProduccionCabecera().getTipo().getId();
    }

    public void agregarRecuperado(double cantidad, M_producto producto) {
        E_produccionDetalle pd = new E_produccionDetalle();
        pd.setCantidad(cantidad);
        pd.setProducto(producto);
        getDesperdicioTM().agregarDetalle(pd);
    }

    public void agregarRecuperadoPosterior(double cantidad, M_producto producto) {
        consultarProduccion();
    }

    public void modificarRecuperado(int posicion, double cantidad) {
        getDesperdicioTM().modificarCantidadDetalle(posicion, cantidad);
    }

    public void modificarRecuperadoPosterior(int posicion, double cantidad) {
        consultarProduccion();
    }

    public void removerRecuperado(int index) {
        getDesperdicioTM().quitarDetalle(index);
    }

    public void removerRecuperadoPosterior(int index) {
        consultarProduccion();
    }

    public void guardar() {
    }

    public void consultarProduccion() {
    }
}
