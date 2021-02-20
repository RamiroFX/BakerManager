/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Charts;

import Egresos.C_gestionCompras;
import Pedido.C_gestionPedido;
import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class Diagramas {

    C_inicio c_inicio;
    M_diagramas modelo;
    V_diagramas vista;
    C_diagramas controlador;

    public Diagramas(C_gestionPedido gestionPedido) {
        this.c_inicio = gestionPedido.c_inicio;
        this.modelo = new M_diagramas(gestionPedido.modelo);
        this.vista = new V_diagramas(c_inicio.vista);
        this.controlador = new C_diagramas(this.c_inicio, this.modelo, this.vista);
    }

    public Diagramas(C_inicio c_inicio, C_gestionCompras gestionEgreso) {
        this.c_inicio = c_inicio;
        this.modelo = new M_diagramas();
        this.vista = new V_diagramas(c_inicio.vista);
        this.controlador = new C_diagramas(this.modelo, this.vista, this.c_inicio, gestionEgreso);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
