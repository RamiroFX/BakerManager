/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import Entities.M_egreso_cabecera;
import Entities.M_egreso_detalle;
import Entities.M_funcionario;
import Entities.M_proveedor;
import MenuPrincipal.DatosUsuario;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class M_Egresos {

    M_proveedor proveedor;
    M_funcionario empleado;
    M_egreso_cabecera egreso_cabecera;
    M_egreso_detalle[] egreso_detalle;
    ArrayList<M_egreso_detalle> egresoDetalles;
    Timestamp tiempo;

    public M_Egresos() {
        this.proveedor = new M_proveedor();
        this.empleado = DatosUsuario.getRol_usuario().getFuncionario();
        this.egreso_cabecera = new M_egreso_cabecera();
        this.egresoDetalles = new ArrayList<>();
        this.tiempo = null;
    }
//
//    public boolean existeProveedorNroFactura(int idProveedor, Integer nroFactura) {
//        return DB_Egreso.existeProveedorNroFactura(idProveedor, nroFactura);
//    }
}
