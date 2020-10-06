/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import Entities.E_produccionCabecera;
import Entities.E_produccionDesperdicioCabecera;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearDesperdicio {

    private M_crearDesperdicio modelo;
    private V_crearDesperdicio vista;
    private C_crearDesperdicio controlador;

    public CrearDesperdicio(JFrame frame) {
        this.modelo = new M_crearDesperdicio();
        this.vista = new V_crearDesperdicio(frame);
        this.controlador = new C_crearDesperdicio(this.modelo, this.vista);
    }

    public void setProduccion(E_produccionCabecera pc) {
        this.modelo.setProduccionCabecera(pc);
    }

    public void setProduccionCabeceraDesperdicio(E_produccionDesperdicioCabecera pc) {
        this.modelo.setProduccionCabecera(pc);
    }
    
    public void establecerModoActualizacion(){
        this.controlador.esablecerModoActualizacion();
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
