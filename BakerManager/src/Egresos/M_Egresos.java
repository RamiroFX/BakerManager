/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import Entities.M_egreso_cabecera;
import Entities.M_egreso_detalle;
import Entities.M_funcionario;
import Entities.M_proveedor;
import java.sql.Timestamp;

/**
 *
 * @author Usuario
 */
public class M_Egresos {

    M_proveedor proveedor;
    M_funcionario empleado;
    M_egreso_cabecera egreso_cabecera;
    M_egreso_detalle[] egreso_detalle;
    Timestamp tiempo;

    public M_Egresos() {
        this.proveedor = new M_proveedor();
        this.empleado = new M_funcionario();
        this.egreso_cabecera = new M_egreso_cabecera();
        this.tiempo = null;
    }
}
