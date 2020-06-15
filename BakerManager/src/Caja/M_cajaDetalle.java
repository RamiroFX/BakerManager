/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import Entities.M_funcionario;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_cajaDetalle {

    private M_funcionario funcionario;

    public M_cajaDetalle() {
        this.funcionario = new M_funcionario();
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public String obtenerNombreFuncionario() {
        return getFuncionario().getNombre() + " " + getFuncionario().getApellido();
    }

    public void borrarDatos() {
        this.funcionario.setAlias("");
        this.funcionario.setApellido("");
        this.funcionario.setEmail("");
        this.funcionario.setId_funcionario(-1);
        this.funcionario.setCedula(-1);
    }
}
