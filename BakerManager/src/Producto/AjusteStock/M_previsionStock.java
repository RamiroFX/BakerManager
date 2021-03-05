/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import DB.DB_Inventario;
import Entities.E_ajusteStockCabecera;
import Entities.Estado;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.AjusteStockCabeceraTableModel;
import ModeloTabla.AjusteStockDetalleTableModel;
import java.util.Calendar;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_previsionStock {

    private AjusteStockCabeceraTableModel tmCabecera;
    private AjusteStockDetalleTableModel tmDetalle;

    public M_previsionStock() {
        this.tmCabecera = new AjusteStockCabeceraTableModel();
        this.tmDetalle = new AjusteStockDetalleTableModel();
    }

    public AjusteStockCabeceraTableModel getTmCabecera() {
        return tmCabecera;
    }

    public AjusteStockDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    public int crearAjusteStock() {
        E_ajusteStockCabecera cabecera = new E_ajusteStockCabecera();
        cabecera.setEstado(new Estado(Estado.ACTIVO, Estado.ACTIVO_STR));
        cabecera.setResponsable(DatosUsuario.getRol_usuario().getFuncionario());
        cabecera.setRegistradoPor(DatosUsuario.getRol_usuario().getFuncionario());
        cabecera.setTiempo(Calendar.getInstance().getTime());
        return (int) DB_Inventario.insertarAjusteStockCabecera(cabecera);
    }

    public void eliminarAjusteStock(int idCabecera) {
        DB_Inventario.eliminarAjusteStockCabecera(idCabecera);
    }

    public void actualizarTablaCabecera() {
        this.tmCabecera.setList(DB_Inventario.consultarAjusteStockCabecera(-1, -1, false, null, null, -1));
    }

}
