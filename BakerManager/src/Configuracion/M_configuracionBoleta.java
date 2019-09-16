/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import DB.DB_Preferencia;
import DB.DB_manager;
import Entities.M_campoImpresion;
import Entities.M_preferenciasImpresion;
import ModeloTabla.ImpresionTableModel;
import Utilities.MyConstants;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_configuracionBoleta {

    private static final String ERROR_MESSAGE_NAME_ALREADY_EXIST = "El parametro ya existe", ERROR_TITLE = "Atención";
    private final int MAX_PRODUCT = 20, MAX_LETTER_SIZE = 30;
    private ImpresionTableModel impresionBoletaTM;
    private boolean isVisible;
    private M_preferenciasImpresion preferenciasBoleta;
    private String[] formatoFechas;

    public M_configuracionBoleta() {
        impresionBoletaTM = new ImpresionTableModel();
        inicializarDatos();
        isVisible = true;
        formatoFechas = new String[]{"dd/MMMM/yy", "dd/MMMM/yyyy", "dd/MM/yyyy"};
    }

    private void inicializarDatos() {
        preferenciasBoleta = DB_Preferencia.obtenerPreferenciaImpresionBoleta();
        ArrayList<M_campoImpresion> campoImpresionLista = DB_manager.obtenerCampoImpresion(3, MyConstants.TODOS);
        impresionBoletaTM.setCampoImpresionList(campoImpresionLista);
    }

    public ImpresionTableModel getImpresionBoletaTM() {
        return impresionBoletaTM;
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
        ArrayList<M_campoImpresion> campoImpresionLista = DB_manager.obtenerCampoImpresion(3, MyConstants.TODOS);
        impresionBoletaTM.setCampoImpresionList(campoImpresionLista);
        this.impresionBoletaTM.updateTable();
    }

    public void habilitarDeshabilitarCampo(int row) {
        M_campoImpresion ci = getImpresionBoletaTM().getValueFromList(row);
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

    public void ocultarMostrarCampo() {
        if (isIsVisible()) {
            ArrayList<M_campoImpresion> campoImpresionLista = DB_manager.obtenerCampoImpresion(3, MyConstants.ACTIVO);
            impresionBoletaTM.setCampoImpresionList(campoImpresionLista);
            this.impresionBoletaTM.updateTable();
        } else {
            ArrayList<M_campoImpresion> campoImpresionLista = DB_manager.obtenerCampoImpresion(3, MyConstants.TODOS);
            impresionBoletaTM.setCampoImpresionList(campoImpresionLista);
            this.impresionBoletaTM.updateTable();
        }
    }

    /**
     * @return the isVisible
     */
    public boolean isIsVisible() {
        return isVisible;
    }

    /**
     * @param isVisible the isVisible to set
     */
    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public int getMaxProducts() {
        return MAX_PRODUCT;
    }

    public int getMaxLetterSize() {
        return MAX_LETTER_SIZE;
    }

    public String[] getFormatoFechas() {
        return formatoFechas;
    }

    public void guardarPreferencias(M_preferenciasImpresion prefImp) {
        DB_Preferencia.modificarPreferenciaImpresionBoleta(prefImp);
    }

    public M_preferenciasImpresion getPreferenciasImpresion() {
        return preferenciasBoleta;
    }

}
