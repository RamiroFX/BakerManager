/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_produccionCabecera;
import Entities.E_productoClasificacion;
import Entities.Estado;
import Entities.M_producto;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearDesperdicio {

    M_producto producto;
    E_produccionCabecera pc;

    public M_crearDesperdicio() {
        this.producto = new M_producto();
        this.pc = new E_produccionCabecera();
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

    public void setProduccionCabecera(E_produccionCabecera pc) {
        System.out.println("bauplast.desperdicio.M_crearDesperdicio.setProduccionCabecera()");
        this.pc = pc;
    }

    public String obtenerFuncionario() {
        return this.pc.getFuncionarioProduccion().getNombre();
    }

    public String obtenerOrdenTrabajo() {
        return this.pc.getNroOrdenTrabajo() + "";
    }

}
