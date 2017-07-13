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
public class Moneda {

    int idMoneda, valor;
    String descripcion;

    public Moneda() {
    }

    public Moneda(int idMoneda, int valor, String descripcion) {
        this.idMoneda = idMoneda;
        this.valor = valor;
        this.descripcion = descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setIdMoneda(int idMoneda) {
        this.idMoneda = idMoneda;
    }

    public int getIdMoneda() {
        return idMoneda;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return getValor() + " - " + getDescripcion();
    }

}
