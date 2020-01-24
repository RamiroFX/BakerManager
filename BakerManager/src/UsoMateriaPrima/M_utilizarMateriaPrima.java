/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsoMateriaPrima;

import DB.DB_UtilizacionMateriaPrima;
import Entities.E_utilizacionMateriaPrimaCabecera;
import Entities.E_utilizacionMateriaPrimaDetalle;
import Entities.M_producto;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.UtilizacionMPDetalleTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
class M_utilizarMateriaPrima {

    private E_utilizacionMateriaPrimaCabecera cabecera;
    private UtilizacionMPDetalleTableModel tm;

    public M_utilizarMateriaPrima() {
        this.cabecera = new E_utilizacionMateriaPrimaCabecera();
        this.cabecera.setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.tm = new UtilizacionMPDetalleTableModel();
    }

    /**
     * @return the cabecera
     */
    public E_utilizacionMateriaPrimaCabecera getCabecera() {
        return cabecera;
    }

    /**
     * @param cabecera the cabecera to set
     */
    public void setCabecera(E_utilizacionMateriaPrimaCabecera cabecera) {
        this.cabecera = cabecera;
    }

    public void setTm(UtilizacionMPDetalleTableModel tm) {
        this.tm = tm;
    }

    public UtilizacionMPDetalleTableModel getTm() {
        return tm;
    }

    public void agregarDetalle(double cantidad, M_producto producto) {
        E_utilizacionMateriaPrimaDetalle detalle = new E_utilizacionMateriaPrimaDetalle();
        detalle.setCantidad(cantidad);
        detalle.setProducto(producto);
        getTm().agregarDetalle(detalle);
    }

    public void modificarDetalle(int index, double cantidad) {
        getTm().modificarCantidadDetalle(index, cantidad);
    }

    public void removerDetalle(int index) {
        getTm().quitarDetalle(index);
    }

    boolean existeOrdenTrabajo(int ordenTrabajo) {
        return DB_UtilizacionMateriaPrima.existeOrdenTrabajo(ordenTrabajo);
    }

    public void guardarUtilizacionMP() {
        DB_UtilizacionMateriaPrima.insertarUtilizcionMateriaPrima(getCabecera(), getTm().getList());
    }

    public void limpiarCampos() {
        setCabecera(new E_utilizacionMateriaPrimaCabecera());
        getTm().vaciarLista();
    }

}
