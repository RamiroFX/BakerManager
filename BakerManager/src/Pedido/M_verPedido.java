/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import DB.DB_Ingreso;
import DB.DB_Pedido;
import DB.DB_Preferencia;
import DB.DB_Timbrado;
import DB.DB_manager;
import Entities.E_Timbrado;
import Entities.E_impresionTipo;
import Entities.E_tipoOperacion;
import Entities.M_pedidoCabecera;
import Entities.M_pedidoDetalle;
import Entities.M_producto;
import ModeloTabla.FacturaDetalleTableModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verPedido {

    private M_pedidoCabecera pedido;
    private FacturaDetalleTableModel tm;
    private E_impresionTipo tipoVenta;
    NumberFormat nfSmall, nfLarge;

    public M_verPedido(int idPedido) {
        this.pedido = DB_Pedido.obtenerPedido(idPedido);
        this.pedido.setTimbrado(new E_Timbrado());
        this.pedido.getTimbrado().setId(1);
        this.pedido.setNroFactura(-1);
        this.tm = new FacturaDetalleTableModel();
        this.tm.setFacturaDetalleList(DB_Pedido.obtenerPedidoDetalle(idPedido));
        this.nfSmall = new DecimalFormat("000");
        this.nfLarge = new DecimalFormat("0000000");
    }

    public FacturaDetalleTableModel getTm() {
        return tm;
    }

    public E_impresionTipo getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(E_impresionTipo tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public M_pedidoCabecera getPedido() {
        return pedido;
    }

    public void setPedido(M_pedidoCabecera pedido) {
        this.pedido = pedido;
    }

    public void actualizarCliente() {
        DB_Pedido.actualizarPedidoCliente(getPedido());
    }

    public void actualizarPedidoDetalle() {
//        DB_Pedido.actualizarPedidoDetalle(tm.getList());
//        borrarDatos();
    }

    public void actualizarTablaPedidoDetalle() {
        this.getTm().setFacturaDetalleList(DB_Pedido.obtenerPedidoDetalle(this.pedido.getIdPedido()));
    }

    public void eliminarPedidoDetalle(int idDetalle) {
        DB_Pedido.eliminarPedidoDetalle(idDetalle);
    }

    public void insertarPedidoDetalle(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        M_pedidoDetalle detalle = new M_pedidoDetalle();
        detalle.setCantidad(cantidad);
        detalle.setPrecio(precio);
        detalle.setDescuento(descuento);
        detalle.setProducto(producto);
        detalle.setObservacion(observacion);
        DB_Pedido.insertarPedidoDetalle(detalle);
    }

    public void actualizarPedido() {
        DB_Pedido.actualizarPedido(getPedido());
    }

    public int pagarPedido(int idPedido, Integer nroFactura) {
//        M_pedido p = DB_Pedido.obtenerPedido(idPedido);
//        setDetalles(DB_Pedido.obtenerPedidoDetalles(p.getIdPedido()));
        return DB_Pedido.pagarPedido(pedido, getTm().getList(), nroFactura, pedido.getTimbrado().getId());
    }

    public ArrayList<E_tipoOperacion> obtenerTipoOperacion() {
        ArrayList<E_tipoOperacion> list = new ArrayList<>(DB_manager.obtenerTipoOperaciones());
        return list;
    }

    public ArrayList<E_impresionTipo> obtenerTipoVenta() {
        return DB_Preferencia.obtenerImpresionTipo();
    }

    public void establecerCondicionVentaContado() {
        this.pedido.setTipoOperacion(new E_tipoOperacion(E_tipoOperacion.CONTADO, 0, "Contado"));
    }

    public void establecerCondicionVentaCredito() {
        this.pedido.setTipoOperacion(new E_tipoOperacion(E_tipoOperacion.CREDITO_30, 30, "Credito"));
    }

    public String obtenerDireccionCliente() {
        return getPedido().getCliente().getDireccion();
    }

    public String obtenerRucCliente() {
        String ruc = "";
        if (getPedido().getCliente().getRuc() != null) {
            ruc = this.getPedido().getCliente().getRuc();
        }
        if (getPedido().getCliente().getRucId() != null) {
            ruc = ruc + "-" + this.getPedido().getCliente().getRucId();
        }
        return ruc;
    }

    public String obtenerNombreCliente() {
        String entidad = this.getPedido().getCliente().getEntidad();
        String nombre = this.getPedido().getCliente().getNombre();
        if (getPedido().getCliente().getNombre() != null) {
            if (!getPedido().getCliente().getNombre().isEmpty()) {
                entidad = entidad + " (" + nombre + ")";
            }
        }
        return entidad;
    }

    public int getNroFactura() {
        return pedido.getNroFactura();
    }

    public String obtenerNroFactura() {
        String nroTimbrado = nfLarge.format(getPedido().getTimbrado().getNroTimbrado());
        String nroSucursal = nfSmall.format(getPedido().getTimbrado().getNroSucursal());
        String nroPuntoVenta = nfSmall.format(getPedido().getTimbrado().getNroPuntoVenta());
        String nroFactura = nfLarge.format(getPedido().getNroFactura());
        return nroTimbrado + "-" + nroSucursal + "-" + nroPuntoVenta + "-" + nroFactura;
    }

    public E_Timbrado obtenerTimbradoPredeterminado() {
        int idTimbrado = DB_Preferencia.obtenerPreferenciaGeneral().getIdTimbradoVenta();
        return DB_Timbrado.obtenerTimbrado(idTimbrado);
    }

    public void setIdTimbrado(int idTimbrado) {
        this.pedido.getTimbrado().setId(idTimbrado);
    }

    public boolean validarNroFactura() {
        return pedido.getNroFactura() > 0;
    }
}
