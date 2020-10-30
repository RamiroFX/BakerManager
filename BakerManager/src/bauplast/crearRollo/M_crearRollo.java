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
    private ProduccionRolloTableModel rollosTM;
    private ProduccionDetalleTableModel materiaPrimaTM;
    private ArrayList<M_menu_item> accesos;

    public M_crearRollo() {
        this.produccionCabecera = new E_produccionCabecera();
        this.produccionCabecera.setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.rollosTM = new ProduccionRolloTableModel();
        this.materiaPrimaTM = new ProduccionDetalleTableModel(ProduccionDetalleTableModel.SIMPLE);
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

    public void setRollosTM(ProduccionRolloTableModel rollosTM) {
        this.rollosTM = rollosTM;
    }

    public ProduccionRolloTableModel getRollosTM() {
        return rollosTM;
    }

    public ProduccionDetalleTableModel getMateriaPrimaTM() {
        return materiaPrimaTM;
    }

    public void setMateriaPrimaTM(ProduccionDetalleTableModel materiaPrimaTM) {
        this.materiaPrimaTM = materiaPrimaTM;
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

    public void modificarDetallePosterior(int index, E_produccionFilm newPF) {
        E_produccionFilm currentPF = getRollosTM().getList().get(index);
        currentPF.getProducto().setId(obtenerRollo(index).getProducto().getId());
        DB_Produccion.actualizarRolloPosterior(currentPF, newPF);
        consultarProduccion();
    }

    public void removerDetalle(int index) {
        getRollosTM().quitarDatos(index);
    }

    public void removerDetallePosterior(int index) {
        E_produccionFilm currentPF = getRollosTM().getList().get(index);
        currentPF.getProducto().setId(obtenerRollo(index).getProducto().getId());
        DB_Produccion.eliminarRolloPosterior(currentPF);
        consultarProduccion();
    }

    public boolean rolloEnUso(int index) {
        E_produccionFilm currentPF = getRollosTM().getList().get(index);
        E_produccionFilm PFdetail = DB_Produccion.obtenerFilm(currentPF.getId());
        return PFdetail.getPesoUtilizado() > 0;
    }

    public E_produccionFilm obtenerRollo(int index) {
        E_produccionFilm currentPF = getRollosTM().getList().get(index);
        return DB_Produccion.obtenerFilm(currentPF.getId());
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

    public boolean existeFilm(int nroFilm) {
        for (E_produccionFilm unFilm : getRollosTM().getList()) {
            int nroFilmActual = unFilm.getNroFilm();
            if (nroFilmActual == nroFilm) {
                return true;
            }
        }
        return false;
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
