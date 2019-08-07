/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import DB.DB_manager;
import DB.ResultSetTableModel;
import ModeloTabla.ImpresionTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_configuracion {

    private ImpresionTableModel tm;
    
    public M_configuracion() {
        tm = new ImpresionTableModel();
    }

    
    public ResultSetTableModel obtenerCamposTicket() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ResultSetTableModel obtenerCamposFactura() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
