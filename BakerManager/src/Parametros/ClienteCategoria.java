/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Parametros;

/**
 *
 * @author Ramiro Ferreira
 */
public enum ClienteCategoria {

    MINORISTA(1, "Minorista"),
    MAYORISTA(2, "Mayorista");
    private int id;
    private String descripcion;

    private ClienteCategoria(int id, String descripcion) {
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
