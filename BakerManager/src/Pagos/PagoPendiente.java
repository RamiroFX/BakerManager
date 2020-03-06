/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class PagoPendiente {

    M_pagoPendiente modelo;
    V_pagoPendiente vista;
    C_pagoPendiente controlador;

    public PagoPendiente(JFrame frame) {
        this.modelo = new M_pagoPendiente();
        this.vista = new V_pagoPendiente(frame);
        this.controlador = new C_pagoPendiente(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostraVista();
    }
}
