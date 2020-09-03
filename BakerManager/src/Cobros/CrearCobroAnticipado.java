/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Cobros.CobroAnticipado.C_cobroAnticipado;
import Cobros.CobroAnticipado.M_cobroAnticipado;
import Cobros.CobroAnticipado.V_cobroAnticipado;
import bakermanager.C_inicio;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearCobroAnticipado {

    private M_cobroAnticipado modelo;
    private V_cobroAnticipado vista;
    private C_cobroAnticipado controlador;

    public CrearCobroAnticipado(JFrame frame, C_inicio inicio) {
        this.modelo = new M_cobroAnticipado();
        this.vista = new V_cobroAnticipado(frame);
        this.controlador = new C_cobroAnticipado(this.modelo, this.vista, inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
