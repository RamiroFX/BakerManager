/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Empleado;

import DB.DB_Funcionario;
import DB.ResultSetTableModel;
import Entities.M_funcionario;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class M_gestion_usuario {

    private M_funcionario funcionario;

    public M_gestion_usuario() {
    }

    /**
     * @return the funcionario
     */
    public M_funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Vector obtenerRolFuncionario() {
        return DB_Funcionario.obtenerRolFuncionario(getFuncionario());
    }

    public ResultSetTableModel consultarFuncionario(String busqueda, boolean exclusivo, boolean entidad, boolean ruc) {
        return DB_Funcionario.consultarFuncionario(busqueda, exclusivo, entidad, ruc);
    }

    public M_funcionario obtenerDatosFuncionarioID(int idFuncionario) {
        return DB_Funcionario.obtenerDatosFuncionarioID(idFuncionario);
    }

    public void eliminarUsuario(int idEmpleado) {
        if (idEmpleado == 1) {
            JOptionPane.showMessageDialog(null, "El usuario admin no puede ser eliminado.", "Atención", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea continuar? Esta acción es irreversible.", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                M_funcionario funcionario = DB_Funcionario.obtenerDatosFuncionarioID(idEmpleado);
                DB_Funcionario.eliminarFuncionarioFX(funcionario);
                JOptionPane.showMessageDialog(null, "Usuario eliminado", "Atención", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

}
