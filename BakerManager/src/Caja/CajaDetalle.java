/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import Interface.InterfaceCajaMovimientos;
import Interface.MovimientosCaja;
import java.util.Date;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class CajaDetalle {

    private V_cajaDetalle vista;
    private M_cajaDetalle modelo;
    private C_cajaDetalle controlador;

    public CajaDetalle(JDialog owner) {
        this.vista = new V_cajaDetalle(owner);
        this.modelo = new M_cajaDetalle();
        this.controlador = new C_cajaDetalle(vista, modelo);
    }

    public CajaDetalle(JDialog owner, MovimientosCaja movimientosCaja) {
        this.vista = new V_cajaDetalle(owner);
        this.modelo = new M_cajaDetalle();
        this.controlador = new C_cajaDetalle(vista, modelo, movimientosCaja);
    }

    public void setInterface(InterfaceCajaMovimientos interfaceCajaMovimientos) {
        this.controlador.setInterface(interfaceCajaMovimientos);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void setRangoTiempo(Date tiempoInicio, Date tiempoFinal) {
        this.modelo.setRangoTiempo(tiempoInicio, tiempoFinal);
    }
}
