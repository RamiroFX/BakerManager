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

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearAjuste {

    private E_ajusteStockCabecera cabecera;
    private SeleccionAjusteStockDetalleTM tmDetalle;

    public M_crearAjuste(int idAjusteCabecera) {
        this.cabecera = DB_Inventario.obtenerAjusteStockCabecera(idAjusteCabecera, true);
        this.tmDetalle = new SeleccionAjusteStockDetalleTM();
        this.tmDetalle.setList(DB_Inventario.consultarAjusteStockDetalleTemporal(idAjusteCabecera));
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

    void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
