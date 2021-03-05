/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import DB.DB_Inventario;
import Entities.E_ajusteStockCabecera;
import Entities.E_ajusteStockDetalle;
import Entities.E_ajusteStockMotivo;
import Entities.M_producto;
import ModeloTabla.AjusteStockDetalleTableModel;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearAjuste {

    private E_ajusteStockCabecera cabecera;
    private AjusteStockDetalleTableModel tmDetalle;

    public M_crearAjuste(int idAjusteCabecera) {
        this.cabecera = DB_Inventario.obtenerAjusteStockCabecera(idAjusteCabecera);
        this.tmDetalle = new AjusteStockDetalleTableModel();
        this.tmDetalle.setList(DB_Inventario.consultarAjusteStockDetalle(idAjusteCabecera));
    }

    public E_ajusteStockCabecera getCabecera() {
        return cabecera;
    }

    public AjusteStockDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    public void consultarConteo() {
        this.tmDetalle.setList(DB_Inventario.consultarAjusteStockDetalle(getCabecera().getId()));
    }

    public String obtenerFuncionario() {
        return this.cabecera.getResponsable().getNombreCompleto();
    }

    public void recibirAjusteStock(M_producto producto, double cantidadVieja, double cantidadNueva, E_ajusteStockMotivo motivo, Date tiempo, String observacion) {
        E_ajusteStockDetalle detalle = new E_ajusteStockDetalle();
        detalle.setIdCabecera(getCabecera().getId());
        detalle.setCantidadNueva(cantidadNueva);
        detalle.setCantidadVieja(cantidadVieja);
        detalle.setMotivo(motivo);
        detalle.setObservacion(observacion);
        detalle.setProducto(producto);
        detalle.setTiempoRegistro(tiempo);
        DB_Inventario.insertarAjusteStockDetalle(detalle);
        consultarConteo();
    }

    public void modificarAjusteStock(int index, M_producto producto, double cantidadVieja, double cantidadNueva, E_ajusteStockMotivo motivo, Date tiempo, String observacion) {
        E_ajusteStockDetalle detalle = new E_ajusteStockDetalle();
        detalle.setId(tmDetalle.getList().get(index).getId());
        detalle.setIdCabecera(getCabecera().getId());
        detalle.setCantidadNueva(cantidadNueva);
        detalle.setCantidadVieja(cantidadVieja);
        detalle.setMotivo(motivo);
        detalle.setObservacion(observacion);
        detalle.setProducto(producto);
        detalle.setTiempoRegistro(tiempo);
        DB_Inventario.actualizarAjusteStockDetalle(detalle);
        consultarConteo();
    }

    void removerProducto(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
