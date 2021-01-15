/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import DB.DB_Cobro;
import Entities.E_cuentaCorrienteCabecera;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_funcionario;
import ModeloTabla.CtaCteCabeceraTableModel;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarPagoAnticipado {

    private CtaCteCabeceraTableModel tmCabecera;
    private E_cuentaCorrienteCabecera cabecera;

    public M_seleccionarPagoAnticipado(int idCliente) {
        this.tmCabecera = new CtaCteCabeceraTableModel();
        this.cabecera = new E_cuentaCorrienteCabecera();
        this.cabecera.getCliente().setIdCliente(idCliente);
    }

    public CtaCteCabeceraTableModel getTM() {
        return tmCabecera;
    }

    public E_cuentaCorrienteCabecera getCabecera() {
        return cabecera;
    }

    public void consultarAdelantos() {
        int idCliente = this.cabecera.getCliente().getIdCliente();
        getTM().setList(DB_Cobro.consultarAdelantosSinAsignar(idCliente));
    }

}
