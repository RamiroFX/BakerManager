/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import DB.DB_Produccion;
import Entities.E_produccionCabecera;
import Entities.E_produccionTipo;
import ModeloTabla.ProduccionDetalleTableModel;
import ModeloTabla.ProduccionRolloTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarProduccion {

    E_produccionCabecera produccionCabecera;
    ProduccionDetalleTableModel produccionTerminadosTM;
    ProduccionRolloTableModel produccionRollosTM;

    public M_seleccionarProduccion() {
        this.produccionCabecera = new E_produccionCabecera();
        this.produccionTerminadosTM = new ProduccionDetalleTableModel();
        this.produccionRollosTM = new ProduccionRolloTableModel();
    }

    public E_produccionCabecera getProduccionCabecera() {
        return produccionCabecera;
    }

    public void setProduccionCabecera(E_produccionCabecera produccionCabecera) {
        this.produccionCabecera = produccionCabecera;
    }

    public ProduccionDetalleTableModel getProduccionTerminadosTM() {
        return produccionTerminadosTM;
    }

    public void setProduccionTerminadosTM(ProduccionDetalleTableModel produccionTerminadosTM) {
        this.produccionTerminadosTM = produccionTerminadosTM;
    }

    public ProduccionRolloTableModel getProduccionRollosTM() {
        return produccionRollosTM;
    }

    public void setProduccionRollosTM(ProduccionRolloTableModel produccionRollosTM) {
        this.produccionRollosTM = produccionRollosTM;
    }

    public int obtenerTipoProduccion() {
        return getProduccionCabecera().getTipo().getId();
    }

    public void consultarProduccion() {
        int tipoProduccion = getProduccionCabecera().getTipo().getId();
        int idProduccion = getProduccionCabecera().getId();
        switch (tipoProduccion) {
            case E_produccionTipo.PRODUCTO_TERMINADO: {
                getProduccionTerminadosTM().setList(DB_Produccion.consultarProduccionDetalle(idProduccion));
                break;
            }
            case E_produccionTipo.ROLLO: {
                getProduccionRollosTM().setList(DB_Produccion.consultarProduccionFilm(idProduccion));
                break;
            }
        }
    }

}
