/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import DB.DB_Produccion;
import Entities.E_produccionCabecera;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.ProduccionRolloTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearRollo {

    E_produccionCabecera produccionCabecera;
    ProduccionRolloTableModel tm;

    public M_crearRollo() {
        this.produccionCabecera = new E_produccionCabecera();
        this.produccionCabecera.setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.tm = new ProduccionRolloTableModel();
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

    public ProduccionRolloTableModel getTm() {
        return tm;
    }

    public void agregarDetalle(E_produccionFilm producto) {
        getTm().agregarDatos(producto);
    }

    public void modificarDetalle(int index, E_produccionFilm pf) {
        getTm().modificarDatos(index, pf);
    }

    public void removerDetalle(int index) {
        getTm().quitarDatos(index);
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
        DB_Produccion.insertarProduccionFilm(getProduccionCabecera(), getTm().getList());
    }

    public void limpiarCampos() {
        setProduccionCabecera(new E_produccionCabecera());
        getTm().vaciarLista();
    }

    public String getFechaProduccionFormateada() {
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdfs.format(getProduccionCabecera().getFechaRegistro());
    }

    public void consultarProduccion() {
        getTm().setList(DB_Produccion.consultarProduccionFilm(getProduccionCabecera().getId()));
    }

}
