/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Empleado;

import DB.DB_Funcionario;
import DB.DB_manager;
import DB.DB_rol;
import DB.ResultSetTableModel;
import Entities.M_funcionario;
import Entities.M_rol;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
class M_modificar_usuario {

    private M_funcionario funcionario;

    public M_modificar_usuario(int idEmpleado) {
        this.funcionario = DB_Funcionario.obtenerDatosFuncionarioID(idEmpleado);
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

    public ResultSetTableModel obtenerRolesDisp() {
        return DB_rol.consultarRoles("");
    }

    public void quitarRol(int idRol) {
        int opcion = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea quitar el rol seleccionado?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            DB_Funcionario.quitarRol(getFuncionario().getIdFuncionario(), idRol);
        }
    }

    public ResultSetTableModel obtenerRolesSelecc() {
        return DB_Funcionario.consultarRolUsuario(getFuncionario().getIdFuncionario());
    }

    public void agregarRol(int idRol, String rolDescripcion) {
        Vector roles = DB_Funcionario.obtenerRolFuncionario(getFuncionario());
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).toString().equals(rolDescripcion)) {
                JOptionPane.showMessageDialog(null, "El Rol seleccionado ya se ha encuentra", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        M_rol rol = new M_rol(idRol, rolDescripcion);
        DB_Funcionario.insertarRolUsuario(getFuncionario(), rol);
    }

    public boolean modificarUsuario(String nombre, String apellido, Integer cedula, Date fechaNacimiento, String alias, String telefono, String celular, Date fechaIngreso, String estadoCivil, String observacion, String direccion, String email, String ciudad, String sexo, String pais) {
        M_funcionario nuevo = new M_funcionario(null, observacion, alias, email, celular, telefono, direccion, null, fechaIngreso, null, null, nombre, apellido, sexo, pais, ciudad, fechaNacimiento, estadoCivil, null, cedula);
        nuevo.setId_persona(funcionario.getId_persona());
        nuevo.setIdFuncionario(funcionario.getIdFuncionario());
        //pregunta si el alias y el numero de cedula son iguales, que son los valores unicos de cada funcionario
        System.out.println("funcionario.getAlias()" + funcionario.getAlias());
        System.out.println("nuevo.getAlias()" + nuevo.getAlias());
        System.out.println("compare" + funcionario.getAlias().compareTo(nuevo.getAlias()));
        System.out.println("funcionario.getCedula()" + funcionario.getCedula());
        System.out.println("nuevo.getCedula()" + nuevo.getCedula());
        boolean b = funcionario.getAlias().compareTo(nuevo.getAlias()) == 0;
        System.out.println("alias: " + b);
        b = funcionario.getCedula().intValue() == nuevo.getCedula().intValue();
        System.out.println("cedula: " + b);
        if ((funcionario.getAlias().compareTo(nuevo.getAlias()) == 0) && (funcionario.getCedula().intValue() == nuevo.getCedula().intValue())) {
            System.out.println("if ((funcionario.getAlias().compareTo(nuevo.getAlias()) == 0) && (funcionario.getCedula() == nuevo.getCedula()))");
            DB_Funcionario.actualizarFuncionario(nuevo);
            JOptionPane.showMessageDialog(null, "Usuario modificado", "Atención", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            //PREGUNTAMOS SI CAMBIO EL ALIAS
            if (funcionario.getAlias().compareTo(nuevo.getAlias()) != 0) {
                System.out.println("if (funcionario.getAlias().compareTo(nuevo.getAlias()) != 0)");
                //SE PREGUNTA SI ES REPETIDO
                if (!DB_Funcionario.verificarAlias(nuevo)) {
                    System.out.println("if (DB_Funcionario.verificarAlias(nuevo))");
                    //SE PREGUNTA SI CAMBIO LA CEDULA
                    if (funcionario.getCedula().intValue() != nuevo.getCedula().intValue()) {
                        //SE PREGUNTA SI ES REPETIDO
                        if (!DB_Funcionario.verificarCI(nuevo)) {
                            System.out.println("DB_Funcionario.verificarCI(nuevo)");
                            DB_Funcionario.actualizarFuncionario(nuevo);
                            JOptionPane.showMessageDialog(null, "Usuario modificado", "Atención", JOptionPane.INFORMATION_MESSAGE);
                            return true;
                        } else {
                            JOptionPane.showMessageDialog(null, "El número de C.I. seleccionado se encuentra en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    } else {
                        DB_Funcionario.actualizarFuncionario(nuevo);
                        JOptionPane.showMessageDialog(null, "Usuario modificado", "Atención", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El Alias seleccionado se encuentra en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if (funcionario.getCedula().intValue() != nuevo.getCedula().intValue()) {
                System.out.println("if (funcionario.getCedula().intValue() != nuevo.getCedula().intValue())");
                //SE PREGUNTA SI CAMBIO LA CEDULA
                if (!DB_Funcionario.verificarCI(nuevo)) {
                    System.out.println("DB_Funcionario.verificarCI(nuevo)");
                    if (!DB_Funcionario.verificarAlias(nuevo)) {
                        System.out.println("if (DB_Funcionario.verificarAlias(nuevo))");
                        DB_Funcionario.actualizarFuncionario(nuevo);
                        JOptionPane.showMessageDialog(null, "Usuario modificado", "Atención", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "El Alias seleccionado se encuentra en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El número de C.I. seleccionado se encuentra en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "El número de cedula y el Alias seleccionado se encuentra en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
