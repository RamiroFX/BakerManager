/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearRollo;

import DB.DB_Produccion;
import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_produccionFilm;
import Entities.E_productoClasificacion;
import Entities.Estado;
import Entities.M_producto;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearFilm {

    M_producto producto;

    public M_crearFilm() {
        this.producto = new M_producto();
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

    public boolean existeNroFilm(int nroFilm) {
        ArrayList<E_produccionFilm> filmes = DB_Produccion.consultarFilmDisponible(nroFilm + "", "Nro. Film", "Ascendente", "Nro. Film", "Todos", false, null, null);
        return !filmes.isEmpty();
    }

}
