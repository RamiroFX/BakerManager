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

    public M_crearRollo() {
        this.produccionCabecera = new E_produccionCabecera();
        this.produccionCabecera.setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.tm = new ProduccionRolloTableModel();
        this.produccionDetalleTM = new ProduccionDetalleTableModel();
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

    public void modificarMPDetalle(int index, double cantidad) {
        getMateriaPrimaTM().modificarCantidadDetalle(index, cantidad);
    }

    public void removerMPDetalle(int index) {
        getMateriaPrimaTM().quitarDetalle(index);
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
    }

}
