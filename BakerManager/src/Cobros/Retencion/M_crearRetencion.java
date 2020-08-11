/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearRetencion {

    private SpinnerModel value;

    public M_crearRetencion() {
        value = new SpinnerNumberModel(5, 0, 100, 1);
    }

    public SpinnerModel getSpinnerModel() {
        return value;
    }

}
