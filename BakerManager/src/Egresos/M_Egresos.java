/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import Entities.M_egresoCabecera;
import Entities.M_egreso_detalle;
import Entities.M_funcionario;
import Entities.M_proveedor;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.EgresoDetalleTableModel;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class M_Egresos {

    M_proveedor proveedor;
    M_funcionario empleado;
    M_egresoCabecera egreso_cabecera;
    M_egreso_detalle[] egreso_detalle;
    ArrayList<M_egreso_detalle> egresoDetalles;
    EgresoDetalleTableModel tableModel;
    Timestamp tiempo;

    public M_Egresos() {
        this.proveedor = new M_proveedor();
        this.empleado = DatosUsuario.getRol_usuario().getFuncionario();
        this.egreso_cabecera = new M_egresoCabecera();
        this.egresoDetalles = new ArrayList<>();
        this.tableModel = new EgresoDetalleTableModel();
        this.tiempo = null;
    }
    
//
//    public boolean existeProveedorNroFactura(int idProveedor, Integer nroFactura) {
//        return DB_Egreso.existeProveedorNroFactura(idProveedor, nroFactura);
//    }

    public EgresoDetalleTableModel getTableModel() {
        return tableModel;
    }
    
    public void establecerModeloTabla(int idEgresoCabecera){
        this.tableModel.setList(DB_Egreso.obtenerEgresoDetalles(idEgresoCabecera));
    }
}
