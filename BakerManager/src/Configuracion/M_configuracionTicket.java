/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import DB.DB_Preferencia;
import Entities.E_ticketPreferencia;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_configuracionTicket {

    private E_ticketPreferencia ticketPreferencia;

    public M_configuracionTicket() {
        this.ticketPreferencia = DB_Preferencia.obtenerPreferenciaImpresionTicket();
    }

    public int modificarTicketPreferencia(E_ticketPreferencia ticketPreferencia) {
        return DB_Preferencia.modificarPreferenciaImpresionTicket(ticketPreferencia);
    }

    public E_ticketPreferencia getTicketPreferencia() {
        return ticketPreferencia;
    }

}
