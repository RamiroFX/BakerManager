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
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_configuracion {

    private static final String ERROR_MESSAGE_NAME_ALREADY_EXIST = "El parametro ya existe", ERROR_TITLE = "Atención";

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

    void crearParametro(M_campoImpresion ci) {
        if (DB_manager.existeCampoParametro(ci.getCampo())) {
            JOptionPane.showMessageDialog(null, ERROR_MESSAGE_NAME_ALREADY_EXIST, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        } else {
            DB_manager.insertarCampoImpresion(2, ci);
        }
    }

    void modificarParametro(M_campoImpresion ci) {
        if (DB_manager.existeCampoParametro(ci.getCampo())) {
            M_campoImpresion ciAux = DB_manager.obtenerCampoParametro(ci.getCampo());
            if (ciAux.getId() == ci.getId()) {
                DB_manager.modificarCampoImpresion(ci);
            } else {
                JOptionPane.showMessageDialog(null, ERROR_MESSAGE_NAME_ALREADY_EXIST, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            }
        } else {
            DB_manager.modificarCampoImpresion(ci);
        }
    }

    public void updateTable() {
        ArrayList<M_campoImpresion> campoImpresionLista = DB_manager.obtenerCampoImpresion(2);
        impresionFacturaTM.setCampoImpresionList(campoImpresionLista);
        this.impresionFacturaTM.updateTable();
    }

    void habilitarDeshabilitarCampo(int row) {
        M_campoImpresion ci = getImpresionFacturaTM().getValueFromList(row);
        int estado = ci.getEstado().getId();
        switch (estado) {
            case 1: {
                DB_manager.habilitarDeshabilitarCampoImpresion(ci.getId(), 2);
                break;
            }
            case 2: {
                DB_manager.habilitarDeshabilitarCampoImpresion(ci.getId(), 1);
                break;
            }
        }
    }
}
