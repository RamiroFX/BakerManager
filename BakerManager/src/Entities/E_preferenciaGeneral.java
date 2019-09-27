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
public class E_preferenciaGeneral {

    int idPreferenciaGeneral, idImpresionTipo;

    public E_preferenciaGeneral() {
    }

    public E_preferenciaGeneral(int idPreferenciaGeneral, int idImpresionTipo) {
        this.idPreferenciaGeneral = idPreferenciaGeneral;
        this.idImpresionTipo = idImpresionTipo;
    }

    public int getIdImpresionTipo() {
        return idImpresionTipo;
    }

    public int getIdPreferenciaGeneral() {
        return idPreferenciaGeneral;
    }

    public void setIdImpresionTipo(int idImpresionTipo) {
        this.idImpresionTipo = idImpresionTipo;
    }

    public void setIdPreferenciaGeneral(int idPreferenciaGeneral) {
        this.idPreferenciaGeneral = idPreferenciaGeneral;
    }

    @Override
    public String toString() {
        return "IdImpresionTipo: " + getIdImpresionTipo();
    }

}
