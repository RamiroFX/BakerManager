/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsoMateriaPrima;

import DB.DB_UtilizacionMateriaPrima;
import Entities.E_utilizacionMateriaPrimaCabecera;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.UtilizacionMPDetalleTableModel;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verUtilizacion {

    E_utilizacionMateriaPrimaCabecera produccionCabecera;
    UtilizacionMPDetalleTableModel tm;

    public M_verUtilizacion() {
        this.produccionCabecera = new E_utilizacionMateriaPrimaCabecera();
        this.produccionCabecera.setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.tm = new UtilizacionMPDetalleTableModel();
    }

    public E_utilizacionMateriaPrimaCabecera getCabecera() {
        return produccionCabecera;
    }

    public void setCabecera(E_utilizacionMateriaPrimaCabecera produccionCabecera) {
        this.produccionCabecera = produccionCabecera;
    }

    public void setTm(UtilizacionMPDetalleTableModel tm) {
        this.tm = tm;
    }

    public UtilizacionMPDetalleTableModel getTm() {
        return tm;
    }

    public void obtenerUtilizacionCabecera(int idUtilizacionMP) {
        setCabecera(DB_UtilizacionMateriaPrima.obtenerUtilizacionMateriaCabecera(idUtilizacionMP));
        getTm().setList(DB_UtilizacionMateriaPrima.consultarUtilizacionMateriaPrimaDetalle(idUtilizacionMP));
    }

    public String getFechaRegistroFormateada() {
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdfs.format(getCabecera().getFechaRegistro());
    }
}
