/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import DB.DB_manager;
import Entities.E_facturacionCabecera;
import Entities.E_tipoOperacion;
import java.util.ArrayList;

/**
 *
 * @author Ramiro
 */
public class M_historialFacturacion {

    public E_facturacionCabecera cabecera;

    public E_facturacionCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(E_facturacionCabecera cabecera) {
        this.cabecera = cabecera;
    }

    public ArrayList<E_tipoOperacion> obtenerTipoOperaciones() {
        E_tipoOperacion todos = new E_tipoOperacion();
        todos.setDescripcion("Todos");
        todos.setDuracion(0);
        todos.setId(0);
        ArrayList<E_tipoOperacion> condVenta = new ArrayList<>();
        condVenta.add(todos);
        condVenta.addAll(DB_manager.obtenerTipoOperaciones());
        return condVenta;
    }

    void borrarDatos() {
        setCabecera(new E_facturacionCabecera());
    }

    String obtenerNombreFuncionario() {
        String alias = this.getCabecera().getFuncionario().getAlias();
        String nombre = this.getCabecera().getFuncionario().getNombre();
        String apellido = this.getCabecera().getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

}
