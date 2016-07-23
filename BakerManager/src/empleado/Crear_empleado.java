/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empleado;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro
 */
class Crear_empleado {

    M_crear_usuario modelo;
    V_crear_usuario vista;
    C_crear_usuario controlador;

    public Crear_empleado(C_inicio C_inicio) {
        this.modelo= new M_crear_usuario();
        this.vista = new V_crear_usuario(C_inicio.vista);
        this.controlador = new C_crear_usuario(this.modelo,this.vista);
    }
    
    public void mostrarVista(){
        controlador.mostrarVista();
    }

}
