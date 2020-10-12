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
import java.util.Calendar;

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
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.set(Calendar.MONTH, 0);
        calendarInicio.set(Calendar.DAY_OF_MONTH, 1);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.set(Calendar.MONTH, Calendar.DECEMBER);
        int res = calendarFinal.getActualMaximum(Calendar.DATE);
        calendarFinal.set(Calendar.DAY_OF_MONTH, res);
        calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
        calendarFinal.set(Calendar.MINUTE, 59);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);

        ArrayList<E_produccionFilm> filmes = DB_Produccion.consultarFilmDisponible(nroFilm + "", "Nro. Film", "Ascendente", "Nro. Film", "Todos", true, calendarInicio.getTime(), calendarFinal.getTime());
        return !filmes.isEmpty();
    }

}
