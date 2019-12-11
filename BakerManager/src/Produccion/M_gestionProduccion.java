/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import DB.DB_Egreso;
import DB.DB_manager;
import Entities.Estado;
import ModeloTabla.ProduccionTableModel;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionProduccion {

    private ProduccionTableModel tableModel;

    public M_gestionProduccion() {
        //this.tableModel = new ProduccionTableModel(interfaceFacturaDetalle);
    }

    public ProduccionTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(ProduccionTableModel dtm) {
        this.tableModel = dtm;
    }

    public void borrarDatos() {

    }

    Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    public ArrayList<Estado> obtenerEstados() {
        return DB_manager.obtenerEstados();
    }
}
