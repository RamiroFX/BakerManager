/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import DB.DB_Inventario;
import Entities.E_ajusteStockCabecera;
import Entities.M_producto;
import ModeloTabla.AjusteStockDetalleTableModel;

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

    void removerProducto(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void agregarProducto(double cantidad, M_producto producto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void modificarProducto(int posicion, double cantidad, M_producto producto, String observacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
