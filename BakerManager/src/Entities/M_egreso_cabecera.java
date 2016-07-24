/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Timestamp;

/**
 *
 * @author Usuario
 */
public class M_egreso_cabecera {

    private Integer id_cabecera;
    private Integer id_proveedor;
    private Integer id_empleado;
    private Integer id_condVenta;
    private String condVenta;
    private Integer nro_factura;
    private Timestamp tiempo;
    private M_proveedor proveedor;
    private M_funcionario funcionario;

    public M_egreso_cabecera() {
    }

    /**
     * @return the id_cabecera
     */
    public Integer getId_cabecera() {
        return id_cabecera;
    }

    /**
     * @param id_cabecera the id_cabecera to set
     */
    public void setId_cabecera(Integer id_cabecera) {
        this.id_cabecera = id_cabecera;
    }

    /**
     * @return the id_proveedor
     */
    public Integer getId_proveedor() {
        return id_proveedor;
    }

    /**
     * @param id_proveedor the id_proveedor to set
     */
    public void setId_proveedor(Integer id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    /**
     * @return the id_empleado
     */
    public Integer getId_empleado() {
        return id_empleado;
    }

    /**
     * @param id_empleado the id_empleado to set
     */
    public void setId_empleado(Integer id_empleado) {
        this.id_empleado = id_empleado;
    }

    /**
     * @return the id_condVenta
     */
    public Integer getId_condVenta() {
        return id_condVenta;
    }

    /**
     * @param id_condVenta the id_condVenta to set
     */
    public void setId_condVenta(Integer id_condVenta) {
        this.id_condVenta = id_condVenta;
    }

    /**
     * @return the tiempo
     */
    public Timestamp getTiempo() {
        return tiempo;
    }

    /**
     * @param tiempo the tiempo to set
     */
    public void setTiempo(Timestamp tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * @return the nro_factura
     */
    public Integer getNro_factura() {
        return nro_factura;
    }

    /**
     * @param nro_factura the nro_factura to set
     */
    public void setNro_factura(Integer nro_factura) {
        this.nro_factura = nro_factura;
    }

    public M_proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(M_proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return the condVenta
     */
    public String getCondVenta() {
        return condVenta;
    }

    /**
     * @param condVenta the condVenta to set
     */
    public void setCondVenta(String condVenta) {
        this.condVenta = condVenta;
    }

}
