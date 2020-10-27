/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import DB.DB_Ingreso;
import Entities.E_facturaDetalle;
import Entities.E_impuesto;
import Entities.E_retencionVenta;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verRetencion {

    private E_retencionVenta retencion;
    private ArrayList<E_facturaDetalle> detalleVentas;

    public M_verRetencion() {
        this.retencion = new E_retencionVenta();
        this.detalleVentas = new ArrayList();
    }

    public E_retencionVenta getRetencion() {
        return retencion;
    }

    public void cargarDatos(E_retencionVenta retencion) {
        this.retencion = retencion;
        consultarNroFactura(retencion.getVenta().getNroFactura());
    }

    public void consultarNroFactura(int nroFactura) {
        retencion.setVenta(DB_Ingreso.obtenerFacturaCabeceraNroFactura(nroFactura));
        detalleVentas.clear();
        detalleVentas.addAll(0, DB_Ingreso.obtenerVentaDetalles2(retencion.getVenta().getIdFacturaCabecera()));
    }

    public double obtenerMontoConIva() {
        double total = 0;
        for (E_facturaDetalle facturaDetalle : detalleVentas) {
            total = total + facturaDetalle.calcularSubTotal();
        }
        return total;
    }

    public double obtenerMontoSinIva() {
        double iva5 = 0, iva10 = 0;
        for (E_facturaDetalle facturaDetalle : detalleVentas) {
            switch (facturaDetalle.getProducto().getIdImpuesto()) {
                case E_impuesto.IVA5: {
                    iva5 = iva5 + (facturaDetalle.calcularSubTotal() / 1.05);
                    break;
                }
                case E_impuesto.IVA10: {
                    iva10 = iva10 + (facturaDetalle.calcularSubTotal() / 1.1);
                    break;
                }
            }
        }
        return iva5 + iva10;
    }

    public double calcularMontoRetencion(double porcentajeRetencion) {
        double iva = obtenerMontoConIva() - obtenerMontoSinIva();
        return (iva * porcentajeRetencion) / 100;
    }
}
