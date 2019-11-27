/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import DB.DB_Ingreso;
import DB.DB_manager;
import Entities.E_facturacionCabecera;
import Entities.E_tipoOperacion;
import ModeloTabla.FacturaCabeceraTableModel;
import ModeloTabla.FacturacionCabeceraTableModel;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Ramiro
 */
public class M_historialFacturacion {

    public E_facturacionCabecera cabecera;

    public M_historialFacturacion() {
        this.cabecera = new E_facturacionCabecera();
    }

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

    public boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            }
        }
        return false;
    }

    public FacturaCabeceraTableModel obtenerVentasPorFacturacion(int idFacturacion) {
        FacturaCabeceraTableModel tm = new FacturaCabeceraTableModel();
        tm.setFacturaCabeceraList(DB_Ingreso.obtenerVentasPorFacturacion(idFacturacion));
        return tm;

    }

    public FacturacionCabeceraTableModel obtenerFacturacion(Date fechaInicio, Date fechaFinal, int idTipoOperacion) {
        FacturacionCabeceraTableModel tm = new FacturacionCabeceraTableModel();
        //TODO validar datos
        if(cabecera.getCliente().getIdCliente() != null){
            
        }else{
            
        }
        int idCliente = cabecera.getCliente().getIdCliente();
        int idFuncionario = cabecera.getFuncionario().getId_funcionario();
        int nroFactura = cabecera.getNroFactura();
        tm.setFacturacionCabeceraList(DB_Ingreso.obtenerFacturaciones(idCliente, idFuncionario, nroFactura, fechaInicio, fechaFinal, idTipoOperacion));
        return tm;
    }

}
