/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.EstadoCuenta;

import ModeloTabla.CtaCteCabeceraTableModel;
import ModeloTabla.CtaCteDetalleAgrupadoTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_estadoCuenta {

    CtaCteCabeceraTableModel cabeceraTableModel;

    public M_estadoCuenta() {
        cabeceraTableModel = new CtaCteCabeceraTableModel();
    }

    public CtaCteCabeceraTableModel obtenerTableModel() {
        return cabeceraTableModel;
    }
}
