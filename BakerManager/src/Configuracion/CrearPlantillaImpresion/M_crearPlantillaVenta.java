/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.CrearPlantillaImpresion;

import DB.DB_Preferencia;
import DB.DB_manager;
import DB.ResultSetTableModel;
import Entities.E_impresionOrientacion;
import Entities.E_impresionPlantilla;
import Entities.M_campoImpresion;
import Entities.M_preferenciasImpresion;
import Interface.InterfaceNotificarCambio;
import ModeloTabla.ImpresionTableModel;
import Parametros.TipoVenta;
import Utilities.MyConstants;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearPlantillaVenta {

    private static final String ERROR_MESSAGE_NAME_ALREADY_EXIST = "El parametro ya existe", ERROR_TITLE = "Atención";
    private final int MAX_PRODUCT = 20, MAX_LETTER_SIZE = 30;
    private ImpresionTableModel impresionFacturaTM;
    private boolean isVisible;
    private M_preferenciasImpresion preferenciasImpresion;
    private String[] formatoFechas;
    private InterfaceNotificarCambio avisarCambioInterface;

    public M_crearPlantillaVenta() {
        impresionFacturaTM = new ImpresionTableModel();
        inicializarDatos();
        isVisible = true;
        formatoFechas = new String[]{"dd/MMMM/yy", "dd/MMMM/yyyy", "dd/MM/yyyy"};
    }

    public void setAvisarCambioInterface(InterfaceNotificarCambio avisarCambioInterface) {
        this.avisarCambioInterface = avisarCambioInterface;
    }

    public InterfaceNotificarCambio getAvisarCambioInterface() {
        return avisarCambioInterface;
    }

    private void inicializarDatos() {
        preferenciasImpresion = DB_Preferencia.obtenerPreferenciaImpresion(1);
        ArrayList<M_campoImpresion> campoImpresionLista = DB_manager.obtenerCampoImpresionPorPlantilla(1, MyConstants.TODOS);
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
        if (DB_manager.existeCampoParametro(TipoVenta.FACTURA, ci.getCampo())) {
            JOptionPane.showMessageDialog(null, ERROR_MESSAGE_NAME_ALREADY_EXIST, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        } else {
            DB_manager.insertarCampoImpresion(2, ci);
        }
    }

    void modificarParametro(M_campoImpresion ci) {
        if (DB_manager.existeCampoParametro(TipoVenta.FACTURA, ci.getCampo())) {
            M_campoImpresion ciAux = DB_manager.obtenerCampoParametro(TipoVenta.FACTURA, ci.getCampo());
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
        ArrayList<M_campoImpresion> campoImpresionLista = DB_manager.obtenerCampoImpresionPorPlantilla(2, MyConstants.TODOS);
        impresionFacturaTM.setCampoImpresionList(campoImpresionLista);
        this.impresionFacturaTM.updateTable();
    }

    public void habilitarDeshabilitarCampo(int row) {
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

    public void ocultarMostrarCampo() {
        if (isIsVisible()) {
            ArrayList<M_campoImpresion> campoImpresionLista = DB_manager.obtenerCampoImpresionPorPlantilla(2, MyConstants.ACTIVO);
            impresionFacturaTM.setCampoImpresionList(campoImpresionLista);
            this.impresionFacturaTM.updateTable();
        } else {
            ArrayList<M_campoImpresion> campoImpresionLista = DB_manager.obtenerCampoImpresionPorPlantilla(2, MyConstants.TODOS);
            impresionFacturaTM.setCampoImpresionList(campoImpresionLista);
            this.impresionFacturaTM.updateTable();
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

    public void guardarPlantilla(E_impresionPlantilla plantilla, List<M_campoImpresion> campos, M_preferenciasImpresion prefImp) {
        ///prefImp.setId(getPreferenciasImpresion().getId());
        DB_Preferencia.insertarPlantilla(plantilla, prefImp, campos);
    }

    public M_preferenciasImpresion getPreferenciasImpresion() {
        return preferenciasImpresion;
    }

    public ArrayList<E_impresionOrientacion> getOrientations() {
        return DB_Preferencia.obtenerImpresionOrientacion();
    }

    boolean existeNombrePlantilla(String nombrePlantilla, int idImpresionTipo) {
        return DB_Preferencia.existePlantilla(nombrePlantilla, idImpresionTipo);
    }

}
