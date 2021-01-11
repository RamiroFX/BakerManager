/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import Entities.M_egreso_cabecera;
import Entities.M_egreso_detalle;
import Entities.M_funcionario;
import Entities.M_proveedor;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.EgresoDetalleTableModel;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearEgresoPorFecha {

    M_proveedor proveedor;
    M_funcionario empleado;
    M_egreso_cabecera egresoCabecera;
    EgresoDetalleTableModel egresoDetalleTableModel;

    public M_crearEgresoPorFecha() {
        this.proveedor = new M_proveedor();
        this.empleado = DatosUsuario.getRol_usuario().getFuncionario();
        this.egresoCabecera = new M_egreso_cabecera();
        this.egresoDetalleTableModel = new EgresoDetalleTableModel();
    }

    public EgresoDetalleTableModel getTM() {
        return egresoDetalleTableModel;
    }

    public M_egreso_cabecera getEgresoCabecera() {
        return egresoCabecera;
    }

    public boolean existeProveedorNroFactura(int idProveedor, int nroFactura) {
        return DB_Egreso.existeProveedorNroFactura(idProveedor, nroFactura);
    }

    public void insertarEgreso() {
        //DB_Egreso.insertarEgresoTEMPORAL(getEgresoCabecera(), getTM().getList());
    }
}
