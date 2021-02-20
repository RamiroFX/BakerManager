/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import Entities.M_egresoCabecera;
import Entities.M_egreso_detalle;
import Entities.M_funcionario;
import Entities.M_proveedor;
import java.sql.Timestamp;

/**
 *
 * @author Usuario
 */
class M_Ver_Egresos {
    M_proveedor proveedor;
    M_funcionario empleado;
    M_egresoCabecera egreso_cabecera;
    M_egreso_detalle[] egreso_detalle;
    Timestamp tiempo;

    public M_Ver_Egresos() {
        this.proveedor = new M_proveedor();
        this.empleado = new M_funcionario();
        this.egreso_cabecera = new M_egresoCabecera();
        this.tiempo = null;
    }
}
