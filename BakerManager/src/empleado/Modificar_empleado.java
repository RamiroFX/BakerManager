/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Empleado;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
class Modificar_empleado {
    M_modificar_usuario modelo;
    V_modificar_usuario vista;
    C_modificar_usuario controlador;

    public Modificar_empleado(C_inicio c_inicio, int idEmpleado) {
        this.modelo= new M_modificar_usuario(idEmpleado);
        this.vista = new V_modificar_usuario(c_inicio.vista);
        this.controlador = new C_modificar_usuario(modelo, vista);
    }
    
    public void mostrarVista(){
        this.controlador.mostrarVista();
    }
}
