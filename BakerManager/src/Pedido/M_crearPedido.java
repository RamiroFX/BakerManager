/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import DB.DB_Egreso;
import DB.DB_Ingreso;
import DB.DB_Pedido;
import DB.DB_Preferencia;
import DB.DB_manager;
import Entities.E_impresionTipo;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearPedido {

    M_pedido pedido;
    M_pedidoDetalle detalle;
    ArrayList<M_pedidoDetalle> detalles;
    private DefaultTableModel dtm;

    public M_crearPedido() {
        this.pedido = new M_pedido();
        this.detalle = new M_pedidoDetalle();
        this.detalles = new ArrayList<>();
        this.dtm = new DefaultTableModel();
        this.dtm.addColumn("ID art.");
        this.dtm.addColumn("Producto");
        this.dtm.addColumn("Cantidad");
        this.dtm.addColumn("Precio");
        this.dtm.addColumn("Descuento");
        this.dtm.addColumn("Exenta");
        this.dtm.addColumn("IVA 5%");
        this.dtm.addColumn("IVA 10%");
        this.dtm.addColumn("Obs.");
    }

    public M_pedido getPedido() {
        return pedido;
    }

    public void setPedido(M_pedido pedido) {
        this.pedido = pedido;
    }

    public M_pedidoDetalle getDetalle() {
        return detalle;
    }

    public void setDetalle(M_pedidoDetalle detalle) {
        this.detalle = detalle;
    }

    public ArrayList<M_pedidoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<M_pedidoDetalle> detalles) {
        this.detalles = detalles;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    public void borrarDatos() {
        setPedido(new M_pedido());
        setDetalle(new M_pedidoDetalle());
        getDetalles().clear();
    }

    public void insertarPedido() {
        DB_Pedido.insertarPedido(getPedido(), getDetalles());
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

    public ArrayList<E_impresionTipo> obtenerTipoVenta() {
        return DB_Preferencia.obtenerImpresionTipo();
    }
}
