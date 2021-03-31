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
import ModeloTabla.SeleccionAjusteStockDetalleTM;
import java.util.Calendar;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_previsionStock {

    private AjusteStockCabeceraTableModel tmCabecera;
    private SeleccionAjusteStockDetalleTM tmDetalle;

    public M_previsionStock() {
        this.tmCabecera = new AjusteStockCabeceraTableModel();
        this.tmDetalle = new SeleccionAjusteStockDetalleTM();
    }

    public AjusteStockCabeceraTableModel getTmCabecera() {
        return tmCabecera;
    }

    public SeleccionAjusteStockDetalleTM getTmDetalle() {
        return tmDetalle;
    }

    public int crearAjusteStock() {
        E_ajusteStockCabecera cabecera = new E_ajusteStockCabecera();
        cabecera.setEstado(new Estado(Estado.ACTIVO, Estado.ACTIVO_STR));
        cabecera.setResponsable(DatosUsuario.getRol_usuario().getFuncionario());
        cabecera.setRegistradoPor(DatosUsuario.getRol_usuario().getFuncionario());
        cabecera.setTiempoInicio(Calendar.getInstance().getTime());
        cabecera.setTiempoFin(Calendar.getInstance().getTime());
        cabecera.setTiempoRegistroInicio(Calendar.getInstance().getTime());
        return (int) DB_Inventario.insertarAjusteStockCabeceraTemporal(cabecera);
    }

    public void eliminarAjusteStock(int idCabecera) {
        DB_Inventario.eliminarAjusteStockCabeceraTemporal(idCabecera);
    }

    public void actualizarTablaCabecera() {
        this.tmCabecera.setList(DB_Inventario.consultarAjusteStockCabecera(-1, -1, false, null, null, -1, true, -1));
        this.tmDetalle.vaciarLista();
    }

    public void actualizarTablaDetalle(int idCabecera) {
        this.tmDetalle.setList(DB_Inventario.consultarAjusteStockDetalleTemporal(idCabecera));
    }

}
