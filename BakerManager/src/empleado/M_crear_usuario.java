/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Empleado;

import DB.DB_Funcionario;
import DB.DB_manager;
import DB.DB_rol;
import DB.ResultSetTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import Entities.M_funcionario;
import Entities.M_rol;
import Utilities.ArrayListTableModel;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class M_crear_usuario {

    M_funcionario m_funcionario;
    ArrayList<M_rol> roles;
    M_rol rol;

    public M_crear_usuario() {
        m_funcionario = new M_funcionario();
        roles = new ArrayList();
    }

    public boolean establecerRoles() {
        if (roles.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Escoja un rol como mínimo", "Parametros incompletos", JOptionPane.ERROR_MESSAGE, null);
            return false;
        }
        return true;
    }

    public boolean isPasswordCorrect(char[] charPasswordTemp, char[] charPasswordTemp2) {
        if (charPasswordTemp.length == 0 || charPasswordTemp2.length == 0) {
            return false;
        }
        return Arrays.equals(charPasswordTemp, charPasswordTemp2);
    }

    public boolean crearUsuario(M_funcionario funcionario, char[] charPasswordTemp, char[] charPasswordTemp2) {
        if (isPasswordCorrect(charPasswordTemp, charPasswordTemp2) && establecerRoles()) {
            String password = String.copyValueOf(charPasswordTemp);
            if (!verificarDatosEmpleado(funcionario)) {
                DB_Funcionario.insertarFuncionarioFX(funcionario, roles, password);
                JOptionPane.showMessageDialog(null, "Nuevo usuario creado", "Atención", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "El número de cedula o el Alias seleccionado se encuentra en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden", "Atención", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public Vector obtenerGenero() {
        return DB_manager.obtenerGenero();
    }

    public Vector obtenerPais() {
        return DB_manager.obtenerPais();
    }

    public Vector obtenerCiudad() {
        return DB_manager.obtenerCiudad();
    }

    public Vector obtenerEstadoCivil() {
        return DB_manager.obtenerEstadoCivil();
    }

    public Vector obtenerRoles() {
        return DB_manager.obtenerRoles();
    }

    public ArrayListTableModel obtenerRolesSelecc() {
        ArrayListTableModel model = new ArrayListTableModel();
        model.addColumn("Id");
        model.addColumn("Rol");
        for (int i = 0; i < roles.size(); i++) {
            Object[] fila = {roles.get(i).getId(),
                roles.get(i).getDescripcion()};
            model.addRow(fila);
        }
        return model;
    }

    void agregarRol(int idRol, String descripcion) {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == idRol) {
                JOptionPane.showMessageDialog(null, "El Rol seleccionado ya se ha encuentra", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        M_rol menu_item = new M_rol(idRol, descripcion);
        roles.add(menu_item);
    }

    void quitarRol(int idRol) {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == idRol) {
                roles.remove(i);
                return;
            }
        }
    }

    public ResultSetTableModel obtenerRolesDisp() {
        return DB_rol.consultarRoles("");
    }

    private boolean verificarDatosEmpleado(M_funcionario funcionario) {
        return DB_Funcionario.existeEmpleado(funcionario);
    }
}
