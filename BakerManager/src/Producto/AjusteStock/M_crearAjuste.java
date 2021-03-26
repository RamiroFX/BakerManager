/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import DB.DB_Inventario;
import Entities.E_ajusteStockCabecera;
import Entities.SeleccionAjusteStockDetalle;
import ModeloTabla.SeleccionAjusteStockDetalleTM;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearAjuste {

    private E_ajusteStockCabecera cabecera;
    private SeleccionAjusteStockDetalleTM tmDetalle;
    private boolean esTemporal;

    public M_crearAjuste(int idAjusteCabecera, boolean esTemporal) {
        this.esTemporal = esTemporal;
        this.cabecera = DB_Inventario.obtenerAjusteStockCabecera(idAjusteCabecera, esTemporal);
        this.tmDetalle = new SeleccionAjusteStockDetalleTM();
        if (esTemporal) {
            this.tmDetalle.setList(DB_Inventario.consultarAjusteStockDetalleTemporal(idAjusteCabecera));
        }else{
            this.tmDetalle.setList(DB_Inventario.consultarAjusteStockDetalle(idAjusteCabecera));            
        }            
    }

    public E_ajusteStockCabecera getCabecera() {
        return cabecera;
    }

    public SeleccionAjusteStockDetalleTM getTmDetalle() {
        return tmDetalle;
    }

    public void consultarConteo() {
        this.tmDetalle.setList(DB_Inventario.consultarAjusteStockDetalleTemporal(getCabecera().getId()));
    }

    public String obtenerFuncionario() {
        return this.cabecera.getResponsable().getNombreCompleto();
    }

    public void recibirAjusteStock(SeleccionAjusteStockDetalle ajusteStockDetalle) {
        SeleccionAjusteStockDetalle detalle = ajusteStockDetalle;
        detalle.setIdCabecera(getCabecera().getId());
        DB_Inventario.insertarAjusteStockDetalleTemporal(detalle);
        consultarConteo();
    }

    public void modificarAjusteStock(int index, SeleccionAjusteStockDetalle ajusteStockDetalle) {
        SeleccionAjusteStockDetalle detalle = ajusteStockDetalle;
        detalle.setId(tmDetalle.getList().get(index).getId());
        detalle.setIdCabecera(getCabecera().getId());
        DB_Inventario.actualizarAjusteStockDetalleTemporal(ajusteStockDetalle);
        consultarConteo();
    }

    public void removerProducto(int index) {
        int idDetalle = tmDetalle.getList().get(index).getId();
        DB_Inventario.eliminarAjusteStockDetalleTemporal(idDetalle);
        consultarConteo();
    }

    public void guardar() {
        DB_Inventario.transferirInventarioTemporalAPermanente(cabecera, tmDetalle.getList());
    }

    void establecerFechaInicio(Date dateInicio) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void establecerFechaFin(Date dateFin) {
        DB_Inventario.establecerAjusteStockFechaFin(cabecera.getId(), dateFin);
    }

    public boolean getEsTemporal() {
        return esTemporal;
    }

    public void setEsTemporal(boolean esTemporal) {
        this.esTemporal = esTemporal;
    }

}
