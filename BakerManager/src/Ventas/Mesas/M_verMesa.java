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
import Entities.E_facturaDetalle;
import Entities.E_impresionTipo;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Entities.M_preferenciasImpresion;
import Entities.M_producto;
import Parametros.TipoOperacion;
import Impresora.Impresora;
import ModeloTabla.FacturaDetalleTableModel;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verMesa {

    private M_mesa mesa;
    private FacturaDetalleTableModel fdm;
    private NumberFormat nfSmall, nfLarge;
    private E_impresionTipo tipoVenta;
    private int maxProdCant;
    private M_preferenciasImpresion preferenciaFactura;
    private ArrayList<Integer> cabeceraMultiple;
    /*
    ventaMultiple:
        en caso de que la cantidad de filas de productos supere el maximo 
        permitido, se genera multiples facturaCabeceras
     */
    private boolean ventaMultiple = false;
    
    public M_verMesa(M_cliente c, M_funcionario f, int nroMesa) {
        this.mesa = new M_mesa();
        this.mesa.setNumeroMesa(nroMesa);
        this.mesa.setFuncionario(f);
        this.mesa.setCliente(c);
        this.mesa.setIdCondVenta(TipoOperacion.CONTADO);
        this.mesa.getTimbrado().setId(1);
        fdm = new FacturaDetalleTableModel();
        preferenciaFactura = DB_Preferencia.obtenerPreferenciaImpresionFactura();
        maxProdCant = preferenciaFactura.getMaxProducts();
        cabeceraMultiple = new ArrayList<>();
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
//        int nroTicket = DB_Ingreso.transferirMesaAVenta(getMesa(), obtenerListaDetalleMesa());
//        getMesa().setIdMesa(nroTicket);
//        int opcion = JOptionPane.showConfirmDialog(null, "¿Desea imprimir el ticket?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
//        if (opcion == JOptionPane.YES_OPTION) {
//            Impresora.imprimirTicketVentaMesa(getMesa(), obtenerListaDetalleMesa());
//        }

        //INICIO GUARDAR VENTA
        if (isVentaMultiple()) {
            //TIPO DE IMPRESION
            E_impresionTipo tipoImpresion = new E_impresionTipo();
            tipoImpresion.setId(getTipoVenta().getId());
            tipoImpresion.setDescripcion(getTipoVenta().getDescripcion());
            int totalRows = getTM().getList().size();
            float maxProdsAux = getMaxProdCant();
            int cantVentas = (int) Math.ceil(totalRows / maxProdsAux);
            ArrayList<E_facturaDetalle> totalList = (ArrayList<E_facturaDetalle>) getTM().getList();
            ArrayList<E_facturaDetalle> currentList;
            int index1 = 0;
            int index2 = getMaxProdCant();
            for (int i = 0; i < cantVentas; i++) {
                currentList = new ArrayList<>(totalList.subList(index1, index2));
                if (tipoImpresion.getId() == E_impresionTipo.FACTURA) {
                    getMesa().setNroFactura(obtenerUltimoNroFactura());//para la siguiente venta
                } else {
                    this.getMesa().setNroFactura(null);
                }
                int nroTicket = DB_Ingreso.insertarIngreso(getMesa().toMFacturaCabecera(), currentList);
                getMesa().setIdFacturaCabecera(nroTicket);
                M_facturaCabecera faca = new M_facturaCabecera();
                faca.setIdFacturaCabecera(nroTicket);
                cabeceraMultiple.add(nroTicket);
                currentList.clear();
                index1 = index2;
                if ((index2 + getMaxProdCant()) > totalRows) {
                    index2 = index2 + (totalRows % getMaxProdCant());
                } else {
                    index2 = index2 + getMaxProdCant();
                }
            }
        } else {
            if (!"factura".equals(tipoVenta.getDescripcion())) {
                this.getMesa().setNroFactura(null);
            }
            int nroTicket = DB_Ingreso.insertarIngreso(getMesa().toMFacturaCabecera(), (ArrayList<E_facturaDetalle>) getTM().getList());
            getMesa().setIdFacturaCabecera(nroTicket);
        }
        Calendar c = Calendar.getInstance();
        getMesa().setTiempo(new Timestamp(c.getTimeInMillis()));
        //FIN GUARDAR VENTA
    }

    boolean existeProductoPorCodigo(String codigoProducto) {
        return DB_Producto.existeCodigo(codigoProducto);
    }

    public M_producto obtenerProductoPorCodigo(String codigoProducto) {
        M_producto unProducto = DB_Producto.obtenerProductoPorCodigo(codigoProducto);
        return unProducto;
    }

    public void guardarVentaDetalle2(E_facturaDetalle detalle) {
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
        for (E_facturaDetalle facturaDetalle : getTM().getList()) {
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

    /**
     * @return the ventaMultiple
     */
    public boolean isVentaMultiple() {
        return ventaMultiple;
    }

    /**
     * @param ventaMultiple the ventaMultiple to set
     */
    public void setVentaMultiple(boolean ventaMultiple) {
        this.ventaMultiple = ventaMultiple;
    }
    public int getMaxProdCant() {
        return maxProdCant;
    }
    public int obtenerUltimoNroFactura() {
        int idTimbrado = getMesa().getTimbrado().getId();
        int ultimoNroFactura = DB_Ingreso.obtenerUltimoNroFactura(idTimbrado) + 1;
        int ultimoNroFacturacion = DB_Ingreso.obtenerUltimoNroFacturacion(idTimbrado) + 1;
        if (ultimoNroFactura >= ultimoNroFacturacion) {
            return ultimoNroFactura;
        } else {
            return ultimoNroFacturacion;
        }
    }
}
