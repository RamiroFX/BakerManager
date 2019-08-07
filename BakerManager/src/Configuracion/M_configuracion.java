/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import DB.DB_manager;
import DB.ResultSetTableModel;
import Entities.M_campoImpresion;
import ModeloTabla.ImpresionTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_configuracion {

    private ImpresionTableModel impresionFacturaTM;

    public M_configuracion() {
        impresionFacturaTM = new ImpresionTableModel();
        inicializarDatos();
    }

    private void inicializarDatos() {
        ArrayList<M_campoImpresion> campoImpresionLista = DB_manager.obtenerCampoImpresion(2);
        impresionFacturaTM.setCampoImpresionList(campoImpresionLista);
    }

    public ResultSetTableModel obtenerCamposTicket() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ResultSetTableModel obtenerCamposFactura() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ImpresionTableModel getImpresionFacturaTM() {
        return impresionFacturaTM;
    }

}
