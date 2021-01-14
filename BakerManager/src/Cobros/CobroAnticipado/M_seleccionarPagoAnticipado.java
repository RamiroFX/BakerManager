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

    public M_seleccionarPagoAnticipado() {
        this.tmCabecera = new CtaCteCabeceraTableModel();
    }

    public CtaCteCabeceraTableModel getTM() {
        return tmCabecera;
    }
    
    public void consultarAdelantos() {
        int idCliente = this.cabecera.getCliente().getIdCliente();
        //getTM().setList(DB_Cobro.consultarPagosPendiente(null, null, idCliente, -1, false));
    }
    
}
