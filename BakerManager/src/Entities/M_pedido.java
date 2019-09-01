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
public class M_pedido {

    private Integer idPedido, idCondVenta, idEstado, idFacturaCabecera;
    private M_cliente cliente;
    private M_funcionario funcionario;
    private Timestamp tiempoRecepcion, tiempoEntrega;
    private String direccion, referencia, estado;

    public M_pedido() {
        this.cliente = new M_cliente();
        this.funcionario = new M_funcionario();
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Integer getIdFacturaCabecera() {
        return idFacturaCabecera;
    }

    public void setIdFacturaCabecera(Integer idFacturaCabecera) {
        this.idFacturaCabecera = idFacturaCabecera;
    }

    public Integer getIdCondVenta() {
        return idCondVenta;
    }

    public void setIdCondVenta(Integer idCondVenta) {
        this.idCondVenta = idCondVenta;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Timestamp getTiempoRecepcion() {
        return tiempoRecepcion;
    }

    public void setTiempoRecepcion(Timestamp tiempoRecepcion) {
        this.tiempoRecepcion = tiempoRecepcion;
    }

    public Timestamp getTiempoEntrega() {
        return tiempoEntrega;
    }

    public void setTiempoEntrega(Timestamp tiempoEntrega) {
        this.tiempoEntrega = tiempoEntrega;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
