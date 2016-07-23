/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_rol_usuario {

    private M_funcionario funcionario;
    private M_rol rolActual;
    private ArrayList<M_rol> roles;
    private ArrayList<M_menu_item>  accesos;

    public M_rol_usuario() {
        this.funcionario = new M_funcionario();
        this.rolActual = new M_rol();
        this.roles = new ArrayList<>();
        this.accesos = new ArrayList<>();
    }

    public M_rol_usuario(M_funcionario funcionario, M_rol rol, ArrayList<M_rol> roles) {
        this.funcionario = funcionario;
        this.rolActual = rol;
        this.roles = roles;
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

    /**
     * @return the rol
     */
    public M_rol getRolActual() {
        return rolActual;
    }

    /**
     * @param rol the rol to set
     */
    public void setRolActual(M_rol rol) {
        this.rolActual = rol;
    }

    /**
     * @return the roles
     */
    public ArrayList<M_rol> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(ArrayList<M_rol> roles) {
        this.roles = roles;
    }

    /**
     * @return the accesos
     */
    public ArrayList<M_menu_item> getAccesos() {
        return accesos;
    }

    /**
     * @param accesos the accesos to set
     */
    public void setAccesos(ArrayList<M_menu_item> accesos) {
        this.accesos = accesos;
    }
}
