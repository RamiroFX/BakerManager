/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import DB.DB_Egreso;
import DB.DB_Ingreso;
import DB.DB_Pedido;
import DB.DB_Preferencia;
import Entities.E_impresionTipo;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
import ModeloTabla.FacturaDetalleTableModel;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearPedido {

    M_pedido pedido;
    private FacturaDetalleTableModel dtm;

    public M_crearPedido() {
        this.pedido = new M_pedido();
        this.dtm = new FacturaDetalleTableModel();
    }

    public M_pedido getPedido() {
        return pedido;
    }

    public void setPedido(M_pedido pedido) {
        this.pedido = pedido;
    }


    public FacturaDetalleTableModel getDtm() {
        return dtm;
    }

    public void borrarDatos() {
        getPedido().getCliente().setIdCliente(-1);
        getDtm().vaciarLista();
    }

    public void insertarPedido() {
        DB_Pedido.insertarPedido(getPedido(), getDtm().getFacturaDetalleList());
    }

    public Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    public ArrayList<E_impresionTipo> obtenerTipoVenta() {
        return DB_Preferencia.obtenerImpresionTipo();
    }
}
