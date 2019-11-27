/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_facturacionCabecera {

    private int id;
    private int nroFactura;
    private Integer total;
    private M_cliente cliente;
    private M_funcionario funcionario;
    private E_tipoOperacion condVenta;
    private Date tiempo;

    public E_facturacionCabecera() {
        this.cliente = new M_cliente();
        this.funcionario = new M_funcionario();
        this.condVenta = new E_tipoOperacion();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(int nroFactura) {
        this.nroFactura = nroFactura;
    }

    public M_cliente getCliente() {
        return cliente;
    }

    public void setCliente(M_cliente cliente) {
        this.cliente = cliente;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getTiempo() {
        return tiempo;
    }

    public void setTiempo(Date tiempo) {
        this.tiempo = tiempo;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public E_tipoOperacion getCondVenta() {
        return condVenta;
    }

    public void setCondVenta(E_tipoOperacion condVenta) {
        this.condVenta = condVenta;
    }
}
