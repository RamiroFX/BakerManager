/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas.Mesas;

import DB.DB_Egreso;
import DB.DB_Ingreso;
import DB.DB_Preferencia;
import DB.DB_Producto;
import Entities.E_Timbrado;
import Entities.E_impresionTipo;
import Entities.M_cliente;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Entities.M_producto;
import Parametros.TipoOperacion;
import Impresora.Impresora;
import ModeloTabla.FacturaDetalleTableModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verMesa {

    private M_mesa mesa;
    private FacturaDetalleTableModel fdm;
    private NumberFormat nfSmall, nfLarge;
    private E_impresionTipo tipoVenta;

    public M_verMesa(M_cliente c, M_funcionario f, int nroMesa) {
        this.mesa = new M_mesa();
        this.mesa.setNumeroMesa(nroMesa);
        this.mesa.setFuncionario(f);
        this.mesa.setCliente(c);
        this.mesa.setIdCondVenta(TipoOperacion.CONTADO);
        fdm = new FacturaDetalleTableModel();
        establecerCabeceraVenta();
        inicializarVariables();
    }

    public M_verMesa(int idMesa) {
        this.mesa = DB_Ingreso.obtenerMesaID(idMesa);
        fdm = new FacturaDetalleTableModel();
        fdm.setFacturaDetalleList(DB_Ingreso.obtenerMesaDetalle2(idMesa));
        establecerCabeceraVenta();
        inicializarVariables();
    }

    private void establecerCabeceraVenta() {
        E_Timbrado timbrado = new E_Timbrado();
        timbrado.setId(1);
        getMesa().setTimbrado(timbrado);
        getMesa().setNroFactura(-1);
    }

    private void inicializarVariables() {
        this.setNfSmall(new DecimalFormat("000"));
        this.setNfLarge(new DecimalFormat("0000000"));
    }

    public E_impresionTipo getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(E_impresionTipo tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public FacturaDetalleTableModel getTM() {
        return fdm;
    }

    public M_mesa getMesa() {
        return mesa;
    }

    public void setMesa(M_mesa cabecera) {
        this.mesa = cabecera;
    }

    public void setMesa(int idMesa) {
        this.mesa = DB_Ingreso.obtenerMesaID(idMesa);
    }

    public Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    public ArrayList<E_impresionTipo> obtenerTipoVenta() {
        return DB_Preferencia.obtenerImpresionTipo();
    }

    public String obtenerNombreCliente() {
        String nombre = this.getMesa().getCliente().getNombre();
        String entidad = this.getMesa().getCliente().getEntidad();
        return entidad + " (" + nombre + ")";
    }

    public String obtenerDireccionCliente() {
        return this.getMesa().getCliente().getDireccion();
    }

    public String obtenerRUCCliente() {
        return this.getMesa().getCliente().getRucCompleto();
    }

    public void actualizarTabla() {
        this.getTM().setFacturaDetalleList(DB_Ingreso.obtenerMesaDetalle2(getMesa().getIdMesa()));
    }

    public void guardarVenta() {
        int nroTicket = DB_Ingreso.transferirMesaAVenta(getMesa(), obtenerListaDetalleMesa());
        getMesa().setIdMesa(nroTicket);
        int opcion = JOptionPane.showConfirmDialog(null, "¿Desea imprimir el ticket?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            Impresora.imprimirTicketVentaMesa(getMesa(), obtenerListaDetalleMesa());
        }
    }

    boolean existeProductoPorCodigo(String codigoProducto) {
        return DB_Producto.existeCodigo(codigoProducto);
    }

    public M_producto obtenerProductoPorCodigo(String codigoProducto) {
        M_producto unProducto = DB_Producto.obtenerProductoPorCodigo(codigoProducto);
        return unProducto;
    }

    public void guardarVentaDetalle2(M_facturaDetalle detalle) {
        M_mesa_detalle mesaDetalle = new M_mesa_detalle(detalle);
        DB_Ingreso.insertarMesaDetalle(getMesa().getIdMesa(), mesaDetalle);
    }

    public void modificarMesaDetalle(M_mesa_detalle detalle) {
        DB_Ingreso.actualizarMesaDetalle(detalle);
    }

    public void eliminarVenta(int idMesaDetalle) {
        DB_Ingreso.eliminarMesaDetalle(idMesaDetalle);
    }

    public void imprimir() {
        Impresora.imprimirTicketMesa(mesa, obtenerListaDetalleMesa());
    }

    public void actualizarDatosMesa(M_cliente cliente) {
        getMesa().setCliente(cliente);
        DB_Ingreso.actualizarMesa(mesa);
    }

    private ArrayList<M_mesa_detalle> obtenerListaDetalleMesa() {
        ArrayList<M_mesa_detalle> detalles = new ArrayList<>();
        for (M_facturaDetalle facturaDetalle : getTM().getFacturaDetalleList()) {
            detalles.add(new M_mesa_detalle(facturaDetalle));
        }
        return detalles;
    }

    /**
     * @return the nfSmall
     */
    public NumberFormat getNfSmall() {
        return nfSmall;
    }

    /**
     * @param nfSmall the nfSmall to set
     */
    public void setNfSmall(NumberFormat nfSmall) {
        this.nfSmall = nfSmall;
    }

    /**
     * @return the nfLarge
     */
    public NumberFormat getNfLarge() {
        return nfLarge;
    }

    /**
     * @param nfLarge the nfLarge to set
     */
    public void setNfLarge(NumberFormat nfLarge) {
        this.nfLarge = nfLarge;
    }
}
