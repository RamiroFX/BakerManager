/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_movimientoContable {

    public static final int TIPO_COMPRA = 1, TIPO_VENTA = 2, TIPO_PAGO = 3,
            TIPO_COBRO = 4, TIPO_SALDO_INICIAL = 5;
    private int id;
    private int tipo;
    private String tipoDescripcion;
    private E_facturaSinPago venta;
    private M_egreso_cabecera compra;
    private E_cuentaCorrienteCabecera cobro;
    private E_reciboPagoCabecera pago;
    private M_cliente clienteSaldoInicial;

    public E_movimientoContable() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the tipo
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the tipoDescripcion
     */
    public String getTipoDescripcion() {
        return tipoDescripcion;
    }

    /**
     * @param tipoDescripcion the tipoDescripcion to set
     */
    public void setTipoDescripcion(String tipoDescripcion) {
        this.tipoDescripcion = tipoDescripcion;
    }

    /**
     * @return the venta
     */
    public E_facturaSinPago getVenta() {
        return venta;
    }

    /**
     * @param venta the venta to set
     */
    public void setVenta(E_facturaSinPago venta) {
        this.venta = venta;
    }

    /**
     * @return the compra
     */
    public M_egreso_cabecera getCompra() {
        return compra;
    }

    /**
     * @param compra the compra to set
     */
    public void setCompra(M_egreso_cabecera compra) {
        this.compra = compra;
    }

    /**
     * @return the cobro
     */
    public E_cuentaCorrienteCabecera getCobro() {
        return cobro;
    }

    /**
     * @param cobro the cobro to set
     */
    public void setCobro(E_cuentaCorrienteCabecera cobro) {
        this.cobro = cobro;
    }

    /**
     * @return the pago
     */
    public E_reciboPagoCabecera getPago() {
        return pago;
    }

    /**
     * @param pago the pago to set
     */
    public void setPago(E_reciboPagoCabecera pago) {
        this.pago = pago;
    }

    public M_cliente getClienteSaldoInicial() {
        return clienteSaldoInicial;
    }

    public void setClienteSaldoInicial(M_cliente clienteSaldoInicial) {
        this.clienteSaldoInicial = clienteSaldoInicial;
    }

}
