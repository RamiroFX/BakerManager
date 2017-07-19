/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros_Pagos;

import DB.DB_Ingreso;
import DB.ResultSetTableModel;
import Entities.M_funcionario;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionCobroPago {

    M_funcionario funcionario;

    public M_gestionCobroPago() {
        this.funcionario = new M_funcionario();
    }

    public M_gestionCobroPago(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void borrarDatos() {
        this.funcionario = null;
    }

    ResultSetTableModel consultarCajas(int idFuncionario, String fecha_inicio, String fecha_fin) {
        //TODO
        return null;
    }

}
