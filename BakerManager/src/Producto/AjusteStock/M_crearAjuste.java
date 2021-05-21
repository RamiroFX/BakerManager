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
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        } else {
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

    public String obtenerObsInventario() {
        if (cabecera.getObservacion() != null) {
            if (cabecera.getObservacion().isEmpty()) {
                return "";
            } else {
                return this.cabecera.getObservacion();
            }
        } else {

            return "";
        }
    }

    public int existeProducto(int idProducto) {
        int index = -1;
        for (int i = 0; i < getTmDetalle().getList().size(); i++) {
            SeleccionAjusteStockDetalle get = getTmDetalle().getList().get(i);
            if (get.getProducto().getId() == idProducto) {
                index = i;
                return index;
            }
        }
        return index;
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
        System.out.println("Producto.AjusteStock.M_crearAjuste.guardar()");
        long startTime = System.nanoTime();
        DB_Inventario.transferirInventarioTemporalAPermanente(cabecera, tmDetalle.getList());
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Inventario: Tiempo total in millis: " + elapsedTime / 1000000);
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

    public String getTituloInventario() {
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaRegistroInicio = sdfs.format(cabecera.getTiempoRegistroInicio());
        String fechaRegistroFin = sdfs.format(cabecera.getTiempoRegistroFin());
        String funcionario = obtenerFuncionario();
        String titulo = "Ver detalle de Inventario" + " (Tiempo de registro inicio: " + fechaRegistroInicio + ")" + " (Tiempo de registro fin: " + fechaRegistroFin + ") - (Registrado por: " + funcionario + ")";
        return titulo;
    }

    public int validarFechaInicio(Date dateInicio) {
        for (SeleccionAjusteStockDetalle unDetalle : getTmDetalle().getList()) {
            if (setTimeToMidnight(unDetalle.getTiempoRegistro()).before(setTimeToMidnight(dateInicio))) {
                return unDetalle.getProducto().getId();
            }
        }
        return -1;
    }

    public int validarFechaFin(Date fechaFin) {
        for (SeleccionAjusteStockDetalle unDetalle : getTmDetalle().getList()) {
            if (setTimeToMidnight(unDetalle.getTiempoRegistro()).after(setTimeToMidnight(fechaFin))) {
                return unDetalle.getProducto().getId();
            }
        }
        return -1;
    }

    public SeleccionAjusteStockDetalle obtenerProducto(int idProducto) {
        for (SeleccionAjusteStockDetalle unDetalle : getTmDetalle().getList()) {
            if (unDetalle.getProducto().getId() == idProducto) {
                return unDetalle;
            }
        }
        return null;
    }

    public Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
