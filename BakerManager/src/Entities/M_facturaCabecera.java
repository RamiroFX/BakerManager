/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Timestamp;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_facturaCabecera {

    private Integer idFacturaCabecera, idCliente, idFuncionario, idCondVenta, idNotaRemision;
    private Integer nroFactura, total;
    private Timestamp tiempo;
    private M_cliente cliente;
    private M_funcionario funcionario;
    private E_tipoOperacion condVenta;

    public M_facturaCabecera() {
        this.funcionario = new M_funcionario();
        this.cliente = new M_cliente();
        this.condVenta = new E_tipoOperacion();
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
        if (funcionario != null) {
            if (funcionario.getId_funcionario() != null) {
                setIdFuncionario(funcionario.getId_funcionario());
            }
        }
    }

    public Integer getIdFacturaCabecera() {
        return idFacturaCabecera;
    }

    public void setIdFacturaCabecera(Integer idFacturaCabecera) {
        this.idFacturaCabecera = idFacturaCabecera;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public M_cliente getCliente() {
        return cliente;
    }

    public void setCliente(M_cliente cliente) {
        this.cliente = cliente;
        if (cliente != null) {
            if (cliente.getIdCliente() != null) {
                setIdCliente(cliente.getIdCliente());
            }
        }
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Integer getIdCondVenta() {
        return idCondVenta;
    }

    public void setIdCondVenta(Integer idCondVenta) {
        this.idCondVenta = idCondVenta;
    }

    public Integer getIdNotaRemision() {
        return idNotaRemision;
    }

    public void setIdNotaRemision(Integer idNotaRemision) {
        this.idNotaRemision = idNotaRemision;
    }

    public Timestamp getTiempo() {
        return tiempo;
    }

    public void setTiempo(Timestamp tiempo) {
        this.tiempo = tiempo;
    }

    public Integer getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(Integer nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTotalFromDouble(Double total) {
        this.total = Math.round(Math.round(total));
    }

    @Override
    public String toString() {
        return getClass().getName() + "{"
                + "cliente:" + getCliente().getEntidad() + ","
                + "funcionario:" + getFuncionario().getAlias() + ","
                + "idCondVenta:" + getIdCondVenta() + ","
                + "nroFactura:" + getNroFactura() + ","
                + "tiempo:" + getTiempo()
                + "}";
    }

    /**
     * @return the condVenta
     */
    public E_tipoOperacion getCondVenta() {
        return condVenta;
    }

    /**
     * @param condVenta the condVenta to set
     */
    public void setCondVenta(E_tipoOperacion condVenta) {
        this.condVenta = condVenta;
    }

}
