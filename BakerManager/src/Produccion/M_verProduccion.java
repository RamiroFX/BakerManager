/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import DB.DB_Produccion;
import Entities.E_produccionCabecera;
import Entities.E_produccionTipo;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.ProduccionDetalleTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verProduccion {

    E_produccionCabecera produccionCabecera;
    ProduccionDetalleTableModel tm;

    public M_verProduccion() {
        this.produccionCabecera = new E_produccionCabecera();
        this.produccionCabecera.setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.tm = new ProduccionDetalleTableModel(ProduccionDetalleTableModel.SIMPLE);
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

    public ArrayList<E_produccionTipo> obtenerProduccionTipo() {
        return DB_Produccion.obtenerTipoProduccion();
    }

    public void obtenerProduccionCabecera(int idProduccion) {
        setProduccionCabecera(DB_Produccion.obtenerProduccionCabecera(idProduccion));
        getTm().setList(DB_Produccion.consultarProduccionDetalle(idProduccion));
    }

    public String getFechaProduccionFormateada() {
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdfs.format(getProduccionCabecera().getFechaRegistro());
    }
}
