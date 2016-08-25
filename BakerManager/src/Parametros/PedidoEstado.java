/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Parametros;

/**
 *
 * @author Ramiro Ferreira
 */
public enum PedidoEstado {

    PENDIENTE(1, "Pendiente"),
    ENTREGADO(2, "Entregado"),
    CANCELADO(3, "Cancelado");
    private int id;
    private String descripcion;

    private PedidoEstado(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
