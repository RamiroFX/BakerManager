/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import bauplast.*;
import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_facturaCabecera;
import Entities.E_productoClasificacion;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.ProductoCategoria;
import ModeloTabla.FacturaCabeceraTableModel;
import ModeloTabla.SeleccionarProductoRolloTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarVenta {

    private FacturaCabeceraTableModel tm;
    private E_facturaCabecera cabecera;

    public M_seleccionarVenta() {
        this.tm = new FacturaCabeceraTableModel();
        this.cabecera = new E_facturaCabecera();
    }

    public FacturaCabeceraTableModel getTm() {
        return tm;
    }

    public void setTm(FacturaCabeceraTableModel tm) {
        this.tm = tm;
    }

    public void setPc(E_facturaCabecera pc) {
        this.cabecera = pc;
    }

    public ArrayList<E_tipoOperacion> obtenerTipoOperacion() {
        return DB_manager.obtenerTipoOperaciones();
    }

    public void consultarVenta(E_tipoOperacion tiop) {
    }
}
