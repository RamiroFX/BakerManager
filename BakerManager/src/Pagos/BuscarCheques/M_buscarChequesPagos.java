/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos.BuscarCheques;

import DB.DB_Pago;
import DB.DB_manager;
import Entities.Estado;
import Entities.M_proveedor;
import ModeloTabla.ChequesPendienteTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_buscarChequesPagos {

    private ChequesPendienteTableModel chequesTM;
    private M_proveedor proveedor;

    public M_buscarChequesPagos() {
        this.chequesTM = new ChequesPendienteTableModel(ChequesPendienteTableModel.PROVEEDOR);
        this.proveedor = new M_proveedor();
        this.proveedor.setId(-1);
    }

    public M_proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(M_proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public ChequesPendienteTableModel getTm() {
        return chequesTM;
    }

    public void setTm(ChequesPendienteTableModel tm) {
        this.chequesTM = tm;
    }

    public ArrayList<Estado> obtenerEstado() {
        return DB_manager.obtenerEstados();
    }

    public void consultarCheques(String nroCheque, boolean porFecha, Date fechaInicio, Date fechaFinal, String acomodarPor, String ordenarPor) {
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(fechaFinal);
        calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
        calendarFinal.set(Calendar.MINUTE, 59);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);
        this.chequesTM.setList(DB_Pago.consultarChequesEmitidos(proveedor.getId(), nroCheque, porFecha, calendarInicio.getTime(), calendarFinal.getTime(), acomodarPor, ordenarPor));
    }
}
