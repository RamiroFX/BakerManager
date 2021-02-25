/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Resumen;

import ModeloTabla.PedidoCabeceraTableModel;
import Pedido.C_gestionPedido;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class Resumen {

    M_resumen modelo;
    V_resumen vista;
    C_resumen controlador;

    public Resumen(C_gestionPedido controlador, PedidoCabeceraTableModel tm, Date fechaInicio, Date fechaFin) {
        this.modelo = new M_resumen(tm, fechaInicio, fechaFin);
        this.vista = new V_resumen(controlador.c_inicio.vista);
        this.controlador = new C_resumen(this.modelo, this.vista, controlador.c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostraVista();
    }
}
