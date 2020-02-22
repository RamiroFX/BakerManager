/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Cliente;
import DB.DB_Egreso;
import DB.DB_Ingreso;
import DB.DB_Preferencia;
import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_impresionTipo;
import Entities.E_preferenciaGeneral;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_preferenciasImpresion;
import Entities.M_producto;
import Entities.M_telefono;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.FacturaDetalleTableModel;
import Interface.InterfaceFacturaDetalle;
import Parametros.TipoOperacion;
import Parametros.TipoVenta;
import Impresora.Impresora;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearVentaRapida {

    private static final String SELECCIONE_POR_LO_MENOS_UN_ARTICULO = "Seleccione por lo menos un artículo.";
    private static final String CONFIRMAR = "Confirmar";
    private static final String ATENCION = "Atención";
    private static final String IMPRIMIR_VENTA = "¿Desea imprimir la venta?";
    private static final String ESTA_SEGURO_QUE_DESEA_CONFIRMAR_LA_VENTA = "¿Está seguro que desea confirmar la venta?";

    private M_facturaCabecera cabecera;
    //private M_facturaDetalle detalle;
    private M_telefono telefono;
    private FacturaDetalleTableModel dtm;
    private E_impresionTipo tipoVenta;
    private Integer maxProdCant;
    private M_preferenciasImpresion pi;
    /*
    ventaMultiple:
        en caso de que la cantidad de filas de productos supere el maximo 
        permitido, se genera multiples facturaCabeceras
     */
    private boolean ventaMultiple = false;
    private ArrayList<Integer> idVentas;

    public M_crearVentaRapida(InterfaceFacturaDetalle interfaceFacturaDetalle) {
        this.cabecera = new M_facturaCabecera();
        this.cabecera.setCliente(DB_Cliente.obtenerDatosClienteID(1));//mostrador
        this.cabecera.setNroFactura(getNroFactura());
        this.cabecera.setIdCondVenta(TipoOperacion.CONTADO);
        try {
            this.telefono = DB_Cliente.obtenerTelefonoCliente(this.cabecera.getCliente().getIdCliente()).get(1);
        } catch (Exception e) {
            this.telefono = null;
        }
        //this.detalle = new M_facturaDetalle();
        dtm = new FacturaDetalleTableModel(interfaceFacturaDetalle);
        pi = DB_Preferencia.obtenerPreferenciaImpresionFactura();
        maxProdCant = pi.getMaxProducts();
        idVentas = new ArrayList<>();
    }

    public M_facturaCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(M_facturaCabecera cabecera) {
        this.cabecera = cabecera;
    }

    /*
    public M_facturaDetalle getDetalle() {
        return detalle;
    }

    public void setDetalle(M_facturaDetalle detalle) {
        this.detalle = detalle;
    }*/
    /**
     * @return the dtm
     */
    public FacturaDetalleTableModel getTableModel() {
        return dtm;
    }

    /**
     * @param dtm the dtm to set
     */
    public void setTableModel(FacturaDetalleTableModel dtm) {
        this.dtm = dtm;
    }

    /**
     * @return the telefono
     */
    public M_telefono getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(M_telefono telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the tipoVenta
     */
    public E_impresionTipo getTipoVenta() {
        return tipoVenta;
    }

    /**
     * @param tipoVenta the tipoVenta to set
     */
    public void setTipoVenta(E_impresionTipo tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    /**
     * @return the maxProdCant
     */
    public Integer getMaxProdCant() {
        return maxProdCant;
    }

    public void validarDatos() {
    }

    public void insertarVenta() {
    }

    public boolean isTableEmpty() {
        if (getTableModel().getFacturaDetalleList().isEmpty()) {
            return true;
        }
        return false;
    }

    public void guardarVenta() {
        System.out.println("Ventas.M_crearVentaRapida.guardarVenta()");
        //INICIO GUARDAR VENTA
        if (isVentaMultiple()) {
            int totalRows = getTableModel().getFacturaDetalleList().size();
            float maxProds = getMaxProdCant();
            int cantVentas = (int) Math.ceil(totalRows / maxProds);
            int currentIndex = 0;
            int lastPos = 0;
            ArrayList<M_facturaDetalle> totalList = (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList();
            ArrayList<M_facturaDetalle> currentList = new ArrayList();

            System.out.println("totalRows: " + totalRows);
            System.out.println("maxProds: " + maxProds);
            System.out.println("cantVentas: " + cantVentas);
            for (int i = 0; i < cantVentas; i++) {
                /*for (M_facturaDetalle m_facturaDetalle : totalList) {
                    if (currentIndex >= lastPos) {
                        currentList.add(m_facturaDetalle);
                        currentIndex++;
                    }
                    if (currentIndex >= getMaxProdCant()) {
                        System.out.println("breaking: " + currentIndex);
                        lastPos = currentIndex;
                        break;
                    }
                }*/
                int auxCurrentPos = 0, auxLastPos = 0, aux;

                for (int j = auxLastPos; j < totalRows; j++) {
                    System.out.println("j: " + j);
                    currentList.add(totalList.get(j));
                    auxCurrentPos++;
                    if (auxCurrentPos >= getMaxProdCant()) {
                        System.out.println("breaking: " + j);
                        auxLastPos = j;
                        break;
                    }
                }
                //int nroTicket = DB_Ingreso.insertarIngreso(getCabecera(), currentList);
                int nroTicket = 1;
                idVentas.add(nroTicket);
                getCabecera().setIdFacturaCabecera(nroTicket);
                auxCurrentPos = 0;
                currentList.clear();
            }
        } else {
            if (!"factura".equals(tipoVenta.getDescripcion())) {
                this.cabecera.setNroFactura(null);
            }
            int nroTicket = DB_Ingreso.insertarIngreso(getCabecera(), (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList());
            getCabecera().setIdFacturaCabecera(nroTicket);
        }
        Calendar c = Calendar.getInstance();
        getCabecera().setTiempo(new Timestamp(c.getTimeInMillis()));
        //FIN GUARDAR VENTA
    }

    public void imprimirVenta() {
        switch (getTipoVenta().getDescripcion()) {
            case "ticket": {
                this.cabecera.setNroFactura(null);
                Impresora.imprimirTicketVenta(DatosUsuario.getRol_usuario(), getCabecera(), (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList());
                break;
            }
            case "factura": {
                Impresora.imprimirFacturaVenta(getCabecera(), (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList());
                break;
            }
            case "boleta": {
                this.cabecera.setNroFactura(null);
                Impresora.imprimirBoletaVenta(getCabecera(), (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList());
                break;
            }
        }
    }

    public void limpiarCampos() {
        this.cabecera.setCliente(DB_Cliente.obtenerDatosClienteID(1));//mostrador
        this.cabecera.setIdCondVenta(TipoOperacion.CONTADO);
        this.cabecera.setNroFactura(getNroFactura());
        try {
            this.telefono = DB_Cliente.obtenerTelefonoCliente(this.cabecera.getCliente().getIdCliente()).get(1);
        } catch (Exception e) {
            this.telefono = null;
        }
//        this.detalle = new M_facturaDetalle();
        this.dtm.vaciarLista();
    }

    boolean existeProductoPorCodigo(String codigoProducto) {
        return DB_Producto.existeCodigo(codigoProducto);
    }

    public M_producto obtenerProductoPorCodigo(String codigoProducto) {
        M_producto unProducto = DB_Producto.obtenerProductoPorCodigo(codigoProducto);
        return unProducto;
    }

    public int getNroFactura() {
        int nroFactura = DB_Ingreso.obtenerUltimoNroFactura() + 1;
        int nroFacturacion = DB_Ingreso.obtenerUltimoNroFacturacion() + 1;
        if (nroFactura >= nroFacturacion) {
            return nroFactura;
        } else {
            return nroFacturacion;
        }
    }

    public boolean nroFacturaEnUso(int nroFactura) {
        return DB_Ingreso.nroFacturaEnUso(nroFactura);
    }

    public Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    public ArrayList<E_impresionTipo> obtenerTipoVenta() {
        return DB_Preferencia.obtenerImpresionTipo();
    }

    public void agregarDetalle(M_facturaDetalle detalle) {
        getTableModel().agregarDetalle(detalle);
    }

    public String obtenerRucCliente() {
        String ruc = "";
        String div = "";
        if (this.getCabecera().getCliente().getRuc() != null) {
            ruc = this.getCabecera().getCliente().getRuc();
        }
        if (this.getCabecera().getCliente().getRucId() != null) {
            div = "-" + this.getCabecera().getCliente().getRucId();
        }
        return ruc + div;
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

}
