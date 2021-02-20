/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Charts;

import Egresos.C_gestionCompras;


/**
 *
 * @author Ramiro Ferreira
 */
public class GraficoEgresos {
    
    M_graficoEgresos modelo;
    V_graficoEgresos vista;
    C_graficoEgresos controlador;

    public GraficoEgresos(C_gestionCompras gestionEgresos) {
        this.modelo = new M_graficoEgresos();
        this.vista = new V_graficoEgresos(gestionEgresos.c_inicio.vista);
        this.controlador = new C_graficoEgresos(this.modelo,this.vista);
    }

    public void mostrarVista(){
        this.controlador.mostrarVista();
    }
    
    
}
