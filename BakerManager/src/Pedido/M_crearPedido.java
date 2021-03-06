/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import DB.DB_Pedido;
import DB.DB_Preferencia;
import DB.DB_manager;
import Entities.E_estadoPedido;
import Entities.E_facturaDetalle;
import Entities.E_impresionTipo;
import Entities.E_tipoOperacion;
import Entities.M_pedidoCabecera;
import Entities.M_producto;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.FacturaDetalleTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearPedido {

    M_pedidoCabecera pedido;
    private FacturaDetalleTableModel dtm;

    public M_crearPedido() {
        this.pedido = new M_pedidoCabecera();
        this.pedido.setFuncionario(DatosUsuario.getRol_usuario().getFuncionario());
        this.pedido.setTipoOperacion(new E_tipoOperacion(E_tipoOperacion.CONTADO, 0, "Contado"));
        this.pedido.setEstadoPedido(new E_estadoPedido(E_estadoPedido.PENDIENTE, E_estadoPedido.PENDIENTE_STRING));
        this.dtm = new FacturaDetalleTableModel();
    }

    public M_pedidoCabecera getPedido() {
        return pedido;
    }

    public void setPedido(M_pedidoCabecera pedido) {
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
        DB_Pedido.insertarPedido(getPedido(), getDtm().getList());
    }

    public ArrayList<E_tipoOperacion> obtenerTipoOperacion() {
        ArrayList<E_tipoOperacion> list = new ArrayList<>(DB_manager.obtenerTipoOperaciones());
        return list;
    }

    public ArrayList<E_impresionTipo> obtenerTipoVenta() {
        return DB_Preferencia.obtenerImpresionTipo();
    }

    public void agregarDetalle(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        E_facturaDetalle fd = new E_facturaDetalle();
        fd.setCantidad(cantidad);
        fd.setPrecio(precio);
        fd.setDescuento(descuento);
        fd.setProducto(producto);
        fd.setObservacion(observacion);
        this.dtm.agregarDetalle(fd);
    }

    public void establecerCondicionVentaContado() {
        this.pedido.setTipoOperacion(new E_tipoOperacion(E_tipoOperacion.CONTADO, 0, "Contado"));
    }

    public void establecerCondicionVentaCredito() {
        this.pedido.setTipoOperacion(new E_tipoOperacion(E_tipoOperacion.CREDITO_30, 30, "Credito"));
    }
}
