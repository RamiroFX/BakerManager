/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Ingreso;
import DB.ResultSetTableModel;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M0_gestionVentas {

    M_facturaCabecera cabecera;
    M_facturaDetalle detalle;
    ArrayList<M_facturaDetalle> detalles;

    public M0_gestionVentas() {
        this.cabecera = new M_facturaCabecera();
        this.detalle = new M_facturaDetalle();
        this.detalles = new ArrayList<>();
    }

    public M_facturaCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(M_facturaCabecera cabecera) {
        this.cabecera = cabecera;
    }

    public M_facturaDetalle getDetalle() {
        return detalle;
    }

    public void setDetalle(M_facturaDetalle detalle) {
        this.detalle = detalle;
    }

    public ArrayList<M_facturaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<M_facturaDetalle> detalles) {
        this.detalles = detalles;
    }

    public boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            }
        }
        return false;
    }

    public ResultSetTableModel obtenerVentas(final Date f_inicio, final Date f_final, final String condVenta) {
        String fechaInicio = "";
        String fechaFinal = "";
        try {
            java.util.Date dateInicio = f_inicio;
            fechaInicio = new Timestamp(dateInicio.getTime()).toString().substring(0, 11);
            fechaInicio = fechaInicio + "00:00:00.000";
        } catch (Exception e) {
            fechaInicio = "Todos";
        }
        try {
            java.util.Date dateFinal = f_final;
            fechaFinal = new Timestamp(dateFinal.getTime()).toString().substring(0, 11);
            fechaFinal = fechaFinal + "23:59:59.000";
        } catch (Exception e) {
            fechaFinal = "Todos";
        }
        return DB_Ingreso.obtenerIngreso(fechaInicio, fechaFinal, condVenta, getCabecera());
    }

    public void borrarDatos() {
        setCabecera(new M_facturaCabecera());
        setDetalle(new M_facturaDetalle());
        setDetalles(new ArrayList<M_facturaDetalle>());
    }
}
