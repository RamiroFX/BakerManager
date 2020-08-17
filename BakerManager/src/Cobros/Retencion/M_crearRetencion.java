/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import DB.DB_Cobro;
import DB.DB_Ingreso;
import Entities.E_facturaCabecera;
import Entities.E_facturaDetalle;
import Entities.E_facturaSinPago;
import Entities.E_impuesto;
import Entities.E_retencionVenta;
import MenuPrincipal.DatosUsuario;
import java.util.ArrayList;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearRetencion {

    private SpinnerModel spinnerModel;
    private E_facturaCabecera facturaCabecera;
    private ArrayList<E_facturaDetalle> facturaDetalles;
    private E_retencionVenta retencion;

    public M_crearRetencion() {
        facturaCabecera = new E_facturaCabecera();
        retencion = new E_retencionVenta();
        facturaDetalles = new ArrayList<>();
        spinnerModel = new SpinnerNumberModel(5.0, 0.0, 100, 1.0);
    }

    public SpinnerModel getSpinnerModel() {
        return spinnerModel;
    }

    public E_facturaCabecera getFacturaCabecera() {
        return facturaCabecera;
    }

    public E_retencionVenta getRetencion() {
        return retencion;
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

    public boolean existeNroFactura(int nroFactura) {
        return DB_Ingreso.nroFacturaEnUso(nroFactura);
    }

    public boolean existeNroRetencion(int nroRetencion) {
        return DB_Cobro.nroRetencionEnUso(nroRetencion);
    }

    public boolean validarMontoExistente() {
        return obtenerMontoConIva() > 0;
    }

    public double calcularMontoRetencion(double porcentajeRetencion) {
        double iva = obtenerMontoConIva() - obtenerMontoSinIva();
        return (iva * porcentajeRetencion) / 100;
    }

    public void guardarRetencion() {
        int idFuncionario = DatosUsuario.getRol_usuario().getFuncionario().getId_funcionario();
        getRetencion().getFuncionario().setId_funcionario(idFuncionario);
        DB_Cobro.insertarRetencion(retencion);
    }

    public boolean existeRetencion(int nroFactura) {
        return DB_Cobro.existeRetencion(nroFactura);
    }

    public boolean facturaPendientePago(int nroFactura) {
        return DB_Cobro.facturaPendientePago(nroFactura);
    }

    public E_facturaSinPago validarSaldoPendiente(int nroFactura) {
        return DB_Cobro.obtenerFacturaSinPago(nroFactura);
    }
}
