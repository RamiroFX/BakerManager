/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.Movimientos;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class MovimientoProducto {
    private M_movimientoProducto modelo;
    private V_movimientoProducto vista;
    private C_movimientoProducto controlador;

    public MovimientoProducto(JFrame frame) {
        this.modelo = new M_movimientoProducto();
        this.vista = new V_movimientoProducto(frame);
        this.controlador = new C_movimientoProducto(modelo, vista);
    }
    
    public void mostrarVista(){
        this.controlador.mostrarVista();
    }
}
