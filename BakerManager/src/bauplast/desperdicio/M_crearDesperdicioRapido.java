/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import DB.DB_Produccion;
import DB.DB_manager;
import Entities.E_produccionCabecera;
import Entities.E_produccionDesperdicioCabecera;
import Entities.E_produccionFilm;
import Entities.E_produccionTipoBaja;
import Entities.Estado;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.ProduccionRolloTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearDesperdicioRapido {

    E_produccionDesperdicioCabecera produccionDesperdicioCabecera;
    ProduccionRolloTableModel desperdicioTM;

    public M_crearDesperdicioRapido() {
        this.produccionDesperdicioCabecera = new E_produccionDesperdicioCabecera();
        this.produccionDesperdicioCabecera.getProduccionCabecera().setFuncionarioSistema(DatosUsuario.getRol_usuario().getFuncionario());
        this.desperdicioTM = new ProduccionRolloTableModel();
    }

    public void setProduccionCabecera(E_produccionCabecera pc) {
        this.produccionDesperdicioCabecera.setProduccionCabecera(pc);
    }

    public E_produccionDesperdicioCabecera getProduccionCabecera() {
        return produccionDesperdicioCabecera;
    }

    public void setProduccionCabecera(E_produccionDesperdicioCabecera produccionCabecera) {
        this.produccionDesperdicioCabecera = produccionCabecera;
    }

    public ProduccionRolloTableModel getDesperdicioTM() {
        return desperdicioTM;
    }

    public void setDesperdicioTM(ProduccionRolloTableModel desperdicioTM) {
        this.desperdicioTM = desperdicioTM;
    }

    public ArrayList<Estado> obtenerEstado() {
        return DB_manager.obtenerEstados();
    }

    public ArrayList<E_produccionTipoBaja> obtenerTipoBajas() {
        ArrayList<E_produccionTipoBaja> tipoBajas = new ArrayList<>();
        tipoBajas.add(new E_produccionTipoBaja(E_produccionTipoBaja.VENTA, "Ventas"));
        //return DB_Produccion.obtenerProduccionTipoBaja();
        return tipoBajas;
    }

    public String obtenerFuncionario() {
        return this.produccionDesperdicioCabecera.getProduccionCabecera().getFuncionarioSistema().getNombre();
    }

    public void removerBaja(int index) {
        getDesperdicioTM().quitarDatos(index);
    }

    public void removerBajaPosterior(int index) {
        consultarProduccion();
    }

    public void guardar() {
        DB_Produccion.insertarBajaFilmPorVenta(getDesperdicioTM().getList());
    }

    public void consultarProduccion() {
    }

    public void agregarBajaFilm(E_produccionFilm detalle) {
        getDesperdicioTM().agregarDatos(detalle);
    }

    void modificarBajaFilm(int index, E_produccionFilm detalle) {
        getDesperdicioTM().modificarDatos(index, detalle);
    }
}
