/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import DB.DB_Ingreso;
import Entities.E_facturaCabecera;
import Entities.E_facturaDetalle;
import Entities.E_impuesto;
import Entities.M_facturaDetalle;
import java.util.ArrayList;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearRetencion {

    private SpinnerModel value;
    private E_facturaCabecera facturaCabecera;
    private ArrayList<E_facturaDetalle> facturaDetalles;

    public M_crearRetencion() {
        facturaCabecera = new E_facturaCabecera();
        facturaDetalles = new ArrayList<>();
        value = new SpinnerNumberModel(5, 0, 100, 1);
    }

    public SpinnerModel getSpinnerModel() {
        return value;
    }

    public E_facturaCabecera getFacturaCabecera() {
        return facturaCabecera;
    }

    public void consultarNroFactura(int nroFactura) {
        facturaCabecera = DB_Ingreso.obtenerFacturaCabeceraNroFactura(nroFactura);
        facturaDetalles.clear();
        facturaDetalles.addAll(0, DB_Ingreso.obtenerVentaDetalles2(facturaCabecera.getIdFacturaCabecera()));
    }

    public int obtenerMontoConIva() {
        int total = 0;
        for (E_facturaDetalle facturaDetalle : facturaDetalles) {
            total = total + facturaDetalle.calcularSubTotal();
        }
        return total;
    }

    public double obtenerMontoSinIva() {
        double iva5 = 0, iva10 = 0;
        for (E_facturaDetalle facturaDetalle : facturaDetalles) {
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

}
