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
import Entities.E_preferenciaGeneral;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_preferenciasImpresion;
import Entities.M_producto;
import Entities.M_telefono;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.FacturaDetalleTableModel;
import ModeloTabla.InterfaceFacturaDetalle;
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
    private M_facturaDetalle detalle;
    private M_telefono telefono;
    private FacturaDetalleTableModel dtm;
    private Integer tipoVenta;
    private Integer maxProdCant;
    private M_preferenciasImpresion pi;

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
        this.detalle = new M_facturaDetalle();
        dtm = new FacturaDetalleTableModel(interfaceFacturaDetalle);
        pi = DB_Preferencia.obtenerPreferenciaImpresionFactura();
        maxProdCant = pi.getMaxProducts();
    }

    public M_facturaCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(M_facturaCabecera cabecera) {
        this.cabecera = cabecera;
    }

    public M_facturaDetalle getDetalle() {
        return detalle;
    }

    public void setDetalle(M_facturaDetalle detalle) {
        this.detalle = detalle;
    }

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
    public int getTipoVenta() {
        return tipoVenta;
    }

    /**
     * @param tipoVenta the tipoVenta to set
     */
    public void setTipoVenta(int tipoVenta) {
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

    public boolean guardarVenta() {
        E_preferenciaGeneral pg1 = DB_Preferencia.obtenerPreferenciaGeneral();
        System.out.println("Ventas.M_crearVentaRapida.obtenerPreferenciaGeneral()" + pg1);
        if (getTableModel().getFacturaDetalleList().isEmpty()) {
            JOptionPane.showConfirmDialog(null, SELECCIONE_POR_LO_MENOS_UN_ARTICULO, ATENCION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        } else {
            int response = JOptionPane.showConfirmDialog(null, ESTA_SEGURO_QUE_DESEA_CONFIRMAR_LA_VENTA, CONFIRMAR, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                if (tipoVenta == TipoVenta.TICKET) {
                    this.cabecera.setNroFactura(null);
                }
                int nroTicket = DB_Ingreso.insertarIngreso(getCabecera(), (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList());
                getCabecera().setIdFacturaCabecera(nroTicket);
                Calendar c = Calendar.getInstance();
                getCabecera().setTiempo(new Timestamp(c.getTimeInMillis()));
                int opcion = JOptionPane.showConfirmDialog(null, IMPRIMIR_VENTA, ATENCION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    if (tipoVenta == TipoVenta.TICKET) {
                        //TODO elejir tipo de comprobate impreso(ticket o boleta)
                        E_preferenciaGeneral pg = DB_Preferencia.obtenerPreferenciaGeneral();
                        if (pg != null) {
                            switch (pg.getIdImpresionTipo()) {
                                case TipoVenta.TICKET: {
                                    //Ticket
                                    Impresora.imprimirTicketVenta(DatosUsuario.getRol_usuario(), getCabecera(), (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList());
                                    break;
                                }
                                case TipoVenta.BOLETA: {
                                    //Boleta
                                    Impresora.imprimirBoletaVenta(getCabecera(), (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList());
                                    break;
                                }
                                default:
                                    javax.swing.JCheckBox check = new javax.swing.JCheckBox("Recordar");
                                    Object[] options = {"Ticket",
                                        "Boleta", check};
                                    int n = JOptionPane.showOptionDialog(null,
                                            "Eliga tipo de impresion",
                                            "Atención",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null, //do not use a custom Icon
                                            options, //the titles of buttons
                                            options[0]); //default button title
                                    if (check.isSelected()) {
                                        recordarPreferencia(tipoVenta);
                                    }
                                    switch (n) {
                                        case 0: {
                                            //Ticket
                                            Impresora.imprimirTicketVenta(DatosUsuario.getRol_usuario(), getCabecera(), (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList());
                                            break;
                                        }
                                        case 1: {
                                            //Boleta
                                            Impresora.imprimirBoletaVenta(getCabecera(), (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList());
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                    } else if (tipoVenta == TipoVenta.FACTURA) {
                        Impresora.imprimirVentaFactura(getCabecera(), (ArrayList<M_facturaDetalle>) getTableModel().getFacturaDetalleList());
                    }
                }
                return true;
            }
        }
        return false;
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
        this.detalle = new M_facturaDetalle();
        this.dtm.vaciarLista();
    }

    private void recordarPreferencia(int tipoVenta) {
        DB_manager.recordarSeleccionImpresion(tipoVenta);
    }

    boolean existeProductoPorCodigo(String codigoProducto) {
        return DB_Producto.existeCodigo(codigoProducto);
    }

    public M_producto obtenerProductoPorCodigo(String codigoProducto) {
        M_producto unProducto = DB_Producto.obtenerProductoPorCodigo(codigoProducto);
        return unProducto;
    }

    public int getNroFactura() {
        int nroFactura;
        nroFactura = DB_Ingreso.obtenerUltimoNroFactura() + 1;
        return nroFactura;
    }

    public boolean nroFacturaEnUso(int nroFactura) {
        return DB_Ingreso.nroFacturaEnUso(nroFactura);
    }

    public Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    public Vector obtenerTipoVenta() {
        return DB_manager.obtenerTipoVenta();
    }

    public void agregarDetalle(M_facturaDetalle detalle) {
        getTableModel().agregarDetalle(detalle);
    }

}
