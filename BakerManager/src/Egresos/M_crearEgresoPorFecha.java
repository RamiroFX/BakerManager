/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import DB.DB_manager;
import Entities.E_Timbrado;
import Entities.E_tipoOperacion;
import Entities.M_egreso_cabecera;
import Entities.M_egreso_detalle;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.EgresoDetalleTableModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearEgresoPorFecha {

    private M_egreso_cabecera egresoCabecera;
    private EgresoDetalleTableModel egresoDetalleTableModel;
    private NumberFormat nfSmall, nfLarge;

    public M_crearEgresoPorFecha() {
        this.egresoCabecera = new M_egreso_cabecera();
        this.egresoCabecera.getProveedor().setId(-1);
        this.egresoCabecera.getTimbrado().setNroTimbrado(-1);
        this.egresoCabecera.getTimbrado().setNroSucursal(-1);
        this.egresoCabecera.getTimbrado().setNroPuntoVenta(-1);
        this.egresoCabecera.setNro_factura(-1);
        this.egresoCabecera.setFuncionario(DatosUsuario.getRol_usuario().getFuncionario());
        this.egresoDetalleTableModel = new EgresoDetalleTableModel();
        this.nfSmall = new DecimalFormat("000");
        this.nfLarge = new DecimalFormat("0000000");
    }

    public NumberFormat getNfLarge() {
        return nfLarge;
    }

    public NumberFormat getNfSmall() {
        return nfSmall;
    }

    public EgresoDetalleTableModel getTM() {
        return egresoDetalleTableModel;
    }

    public ArrayList<E_tipoOperacion> obtenerTipoOperaciones() {
        return DB_manager.obtenerTipoOperaciones();
    }

    public M_egreso_cabecera getEgresoCabecera() {
        return egresoCabecera;
    }

    public void insertarEgreso() {
        DB_Egreso.insertarEgreso(getEgresoCabecera(), (ArrayList<M_egreso_detalle>) getTM().getList());
    }

    public void establecerNroFactura(E_Timbrado timbrado, int nroFactura) {
        getEgresoCabecera().setNro_factura(nroFactura);
        getEgresoCabecera().setTimbrado(timbrado);
    }

    public boolean validarNroFacturaEnUso() {
        return DB_Egreso.existeProveedorNroFactura(getEgresoCabecera().getProveedor().getId(),
                getEgresoCabecera().getTimbrado().getNroTimbrado(),
                getEgresoCabecera().getTimbrado().getNroSucursal(),
                getEgresoCabecera().getTimbrado().getNroPuntoVenta(),
                getEgresoCabecera().getNro_factura());
    }
}
