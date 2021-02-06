/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas.Mesas;

import Cliente.SeleccionarCliente;
import Configuracion.Timbrado.SeleccionarTimbrado;
import DB.DB_Cliente;
import Entities.E_Timbrado;
import Entities.E_impresionTipo;
import Entities.E_impuesto;
import Entities.M_cliente;
import Entities.M_facturaDetalle;
import Entities.M_mesa_detalle;
import Entities.M_producto;
import Entities.M_telefono;
import Interface.RecibirClienteCallback;
import Interface.RecibirProductoCallback;
import Interface.RecibirTimbradoVentaCallback;
import Parametros.TipoOperacion;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Ventas.C_crearVentas;
import Ventas.V_crearVentaRapida;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_verMesa extends MouseAdapter implements ActionListener, KeyListener, RecibirClienteCallback, RecibirTimbradoVentaCallback, RecibirProductoCallback {

    private static final String TITULO_ERROR = "Error";
    private static final String PRODUCTO_NO_EXISTE = "El producto no existe";

    public M_verMesa modelo;
    public V_crearVentaRapida vista;
    public C_crearVentas crearVentas;
    private boolean isJCBTrigger;

    public C_verMesa(M_verMesa modelo, V_crearVentaRapida vista, C_crearVentas crearVentas) {
        this.modelo = modelo;
        this.vista = vista;
        this.crearVentas = crearVentas;
        this.isJCBTrigger = true;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void inicializarVista() {
        this.vista.setTitle("Mesa " + this.modelo.getMesa().getNumeroMesa());
        this.vista.jtfCliente.setText(modelo.obtenerNombreCliente());
        this.vista.jtfClieDireccion.setText(modelo.obtenerDireccionCliente());
        this.vista.jtfClieRuc.setText(modelo.obtenerRUCCliente());

        Vector condCompra = modelo.obtenerTipoOperacion();
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondVenta.addItem(condCompra.get(i));
        }
        ArrayList<E_impresionTipo> tipoVenta = modelo.obtenerTipoVenta();
        for (int i = 0; i < tipoVenta.size(); i++) {
            this.vista.jcbTipoVenta.addItem(tipoVenta.get(i));
        }
        switch (this.modelo.getMesa().getIdCondVenta()) {
            case TipoOperacion.CONTADO: {
                this.vista.jcbCondVenta.setSelectedIndex(0);
                break;
            }
            case TipoOperacion.CREDITO: {
                this.vista.jcbCondVenta.setSelectedIndex(1);
                break;
            }
            default: {
                this.vista.jcbCondVenta.setSelectedIndex(1);
                break;
            }
        }
        this.vista.jtFacturaDetalle.setModel(modelo.getTM());
        sumarTotal();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        java.awt.Font fuente = new java.awt.Font("Times New Roman", 0, 18);
        javax.swing.text.DefaultFormatterFactory dff = new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance()));
        this.vista.jftExenta.setFormatterFactory(dff);
        this.vista.jftExenta.setFont(fuente); // NOI18N
        this.vista.jftIva5.setFormatterFactory(dff);
        this.vista.jftIva5.setFont(fuente); // NOI18N
        this.vista.jftImpIva5.setFormatterFactory(dff);
        this.vista.jftImpIva5.setFont(fuente); // NOI18N
        this.vista.jftIva10.setFormatterFactory(dff);
        this.vista.jftIva10.setFont(fuente); // NOI18N
        this.vista.jftImpIva10.setFormatterFactory(dff);
        this.vista.jftImpIva10.setFont(fuente); // NOI18N
        this.vista.jftTotal.setFormatterFactory(dff);
        this.vista.jftTotal.setFont(fuente); // NOI18N
        this.vista.jftIvaTotal.setFormatterFactory(dff);
        this.vista.jftIvaTotal.setFont(fuente); // NOI18N
    }

    private void agregarListeners() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbAgregarProducto.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jtfCodProd.addActionListener(this);
        this.vista.jtFacturaDetalle.addMouseListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jcbCondVenta.addActionListener(this);
        this.vista.jcbTipoVenta.addActionListener(this);
        this.vista.jbNroFactura.addActionListener(this);
        this.vista.jtfCodProd.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbImprimir.addKeyListener(this);
        this.vista.jbAgregarProducto.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jbNroFactura.addKeyListener(this);
    }

    public void cerrar() {
        this.vista.dispose();
    }

    private void establecerTipoVenta() {
        E_impresionTipo tipoVenta = this.vista.jcbTipoVenta.getItemAt(this.vista.jcbTipoVenta.getSelectedIndex());
        switch (tipoVenta.getDescripcion()) {
            case E_impresionTipo.TICKET_STRING: {
                this.vista.jtfNroFactura.setText("");
                this.vista.jtfNroFactura.setEnabled(false);
                this.modelo.getMesa().getTimbrado().setId(1);
                this.modelo.setTipoVenta(tipoVenta);
                break;
            }
            case E_impresionTipo.FACTURA_STRING: {
                if (isJCBTrigger) {
                    this.modelo.setTipoVenta(tipoVenta);
                    SeleccionarTimbrado st = new SeleccionarTimbrado(this.vista, this);
                    st.mostrarVista();
                }
                break;
            }
            case E_impresionTipo.BOLETA_STRING: {
                this.vista.jtfNroFactura.setText("");
                this.vista.jtfNroFactura.setEnabled(false);
                this.modelo.getMesa().getTimbrado().setId(1);
                this.modelo.setTipoVenta(tipoVenta);
                break;
            }
        }
    }

    public void recibirDetalle(M_facturaDetalle detalle) {
        this.modelo.guardarVentaDetalle2(detalle);
        this.modelo.actualizarTabla();
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
        sumarTotal();
        actualizarMesas();
    }

    private void eliminarDetalle() {
        int row = this.vista.jtFacturaDetalle.getSelectedRow();
        if (row < 0) {
            return;
        }
        int response = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar el detalle?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            int idMesaDetalle = modelo.getTM().getFacturaDetalleList().get(row).getIdFacturaDetalle();
            this.modelo.eliminarVenta(idMesaDetalle);
            this.modelo.actualizarTabla();
            this.vista.jbEliminarDetalle.setEnabled(false);
            this.vista.jbModificarDetalle.setEnabled(false);
            Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
            sumarTotal();
            actualizarMesas();
        }
    }

    public void modificarDetalle(M_producto producto, Double cantidad, Double precio, Double descuento, String observacion, int idMesaDetalle) {
        M_mesa_detalle unDetalle = new M_mesa_detalle();
        unDetalle.setIdMesaDetalle(idMesaDetalle);
        unDetalle.setProducto(producto);
        unDetalle.setCantidad(cantidad);
        unDetalle.setDescuento(descuento);
        unDetalle.setPrecio(precio);
        unDetalle.setObservacion(observacion);
        this.modelo.modificarMesaDetalle(unDetalle);
        this.modelo.actualizarTabla();
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        sumarTotal();
        actualizarMesas();
    }

    private void sumarTotal() {
        double exenta = 0;
        double total5 = 0;
        double total10 = 0;
        for (M_facturaDetalle unDetalle : modelo.getTM().getFacturaDetalleList()) {
            switch (unDetalle.getProducto().getIdImpuesto()) {
                case E_impuesto.EXENTA: {
                    exenta = exenta + unDetalle.calcularSubTotal();
                    break;
                }
                case E_impuesto.IVA5: {
                    total5 = total5 + unDetalle.calcularSubTotal();
                    break;
                }
                case E_impuesto.IVA10: {
                    total10 = total10 + unDetalle.calcularSubTotal();
                    break;
                }
            }
        }
        double total = exenta + total5 + total10;
        double totalIva5 = total5 / 21;
        double totalIva10 = total10 / 11;
        this.vista.jftExenta.setValue(exenta);
        this.vista.jftIva5.setValue(total5);
        this.vista.jftImpIva5.setValue(totalIva5);
        this.vista.jftIva10.setValue(total10);
        this.vista.jftImpIva10.setValue(totalIva10);
        this.vista.jftTotal.setValue(total);
        this.vista.jftIvaTotal.setValue(totalIva5 + totalIva10);
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        this.modelo.actualizarDatosMesa(cliente);
        this.vista.jtfCliente.setText(modelo.obtenerNombreCliente());
        this.vista.jtfClieDireccion.setText(modelo.obtenerDireccionCliente());
        this.vista.jtfClieRuc.setText(modelo.obtenerRUCCliente());
        ArrayList<M_telefono> telefono = DB_Cliente.obtenerTelefonoCliente(this.modelo.getMesa().getCliente().getIdCliente());
        if (!telefono.isEmpty()) {
            this.vista.jtfClieTelefono.setText(telefono.get(0).getNumero());
        } else {
            this.vista.jtfClieTelefono.setText("");
        }
        crearVentas.actualizarTablaMesa();
    }

    private void guardarVenta() {
        if (!this.modelo.getTM().getFacturaDetalleList().isEmpty()) {
            int option = JOptionPane.showConfirmDialog(this.vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                this.modelo.guardarVenta();
                actualizarMesas();
                cerrar();
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Mesa vacía. Agregue un producto", "Atención", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void establecerCondicionVenta() {
        String currentItem = this.vista.jcbCondVenta.getSelectedItem().toString();
        if (currentItem.equals("Contado")) {
            this.modelo.getMesa().setIdCondVenta(TipoOperacion.CONTADO);
        } else {
            this.modelo.getMesa().setIdCondVenta(TipoOperacion.CREDITO);
        }
    }

    private void actualizarMesas() {
        this.crearVentas.actualizarTablaMesa();
    }

    private void agregarProductoPorCodigo() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                agregarProductoPorCodigoAux();
            }
        });
    }

    private void agregarProductoPorCodigoAux() {
        //OBTENER CODIGO DESDE LA PISTOLA DE COD DE BARRAS
        String codigoProducto = vista.jtfCodProd.getText().trim();
        //VERIFICAR SI EXISTE EL PRODUCTO EN LA BD
        boolean existeProd = modelo.existeProductoPorCodigo(codigoProducto);
        if (existeProd) {
            //SELECCIONAR CANTIDAD DE PRODUCTO
            M_producto unProducto = modelo.obtenerProductoPorCodigo(codigoProducto);
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(vista, unProducto, this, SeleccionarCantidadProduducto.PRECIO_VENTA_MINORISTA);
            scp.setVisible(true);
            vista.jtfCodProd.setText("");
        } else {
            JOptionPane.showMessageDialog(vista, PRODUCTO_NO_EXISTE, TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void imprimirTicket() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (modelo.getTM().getFacturaDetalleList().isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "No hay productos cargados", "Atención", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir el ticket?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        modelo.imprimir();
                    }
                }
            }
        });
    }

    private void invocarModificarDetalle() {
        int row = this.vista.jtFacturaDetalle.getSelectedRow();
        if (row < 0) {
            return;
        }
        M_producto idProducto = modelo.getTM().getFacturaDetalleList().get(row).getProducto();
        int idMesaDetalle = modelo.getTM().getFacturaDetalleList().get(row).getIdFacturaDetalle();
        SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(vista, idProducto, this, SeleccionarCantidadProduducto.PRECIO_VENTA_MINORISTA, idMesaDetalle);
        scp.setVisible(true);
    }

    private void invocarSeleccionTimbrado() {
        SeleccionarTimbrado st = new SeleccionarTimbrado(this.vista, this);
        st.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        }
        if (e.getSource().equals(this.vista.jbAceptar)) {
            guardarVenta();
        }
        if (e.getSource().equals(this.vista.jbAgregarProducto)) {
            SeleccionarProducto sp = new SeleccionarProducto(this);
            sp.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jtfCodProd)) {
            agregarProductoPorCodigo();
        }
        if (e.getSource().equals(this.vista.jbCliente)) {
            SeleccionarCliente sp = new SeleccionarCliente(this.crearVentas.gestionVentas.c_inicio.vista);
            sp.setCallback(this);
            sp.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        }
        if (e.getSource().equals(this.vista.jbImprimir)) {
            imprimirTicket();
        }
        if (e.getSource().equals(this.vista.jcbCondVenta)) {
            establecerCondicionVenta();
        }
        if (e.getSource().equals(this.vista.jbModificarDetalle)) {
            invocarModificarDetalle();
        }
        if (e.getSource().equals(this.vista.jbNroFactura)) {
            invocarSeleccionTimbrado();
        }
        if (e.getSource().equals(this.vista.jcbTipoVenta)) {
            establecerTipoVenta();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtFacturaDetalle)) {
            this.vista.jbModificarDetalle.setEnabled(true);
            this.vista.jbEliminarDetalle.setEnabled(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            guardarVenta();
        }
        if (e.getKeyCode() == KeyEvent.VK_F2) {
            imprimirTicket();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrar();
        }
        if (e.getKeyCode() == KeyEvent.VK_F4) {
            SeleccionarProducto sp = new SeleccionarProducto(this);
            sp.mostrarVista();
        }
        if (e.getKeyCode() == KeyEvent.VK_F5) {
            invocarSeleccionTimbrado();
        }
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            SeleccionarCliente sp = new SeleccionarCliente(this.crearVentas.gestionVentas.c_inicio.vista);
            sp.setCallback(this);
            sp.mostrarVista();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void recibirTimbrado(E_Timbrado timbrado) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recibirTimbradoNroFactura(E_Timbrado timbrado, int nroFactura) {
        E_impresionTipo tipoVenta = new E_impresionTipo(E_impresionTipo.FACTURA, E_impresionTipo.FACTURA_STRING);
        modelo.getMesa().getTimbrado().setId(timbrado.getId());
        modelo.getMesa().setNroFactura(nroFactura);
        modelo.setTipoVenta(tipoVenta);
        isJCBTrigger = false;
        this.vista.jcbTipoVenta.setSelectedItem(tipoVenta);
        String nroTimbrado = modelo.getNfLarge().format(timbrado.getNroTimbrado());
        String nroSucursal = modelo.getNfSmall().format(timbrado.getNroSucursal());
        String nroPuntoVenta = modelo.getNfSmall().format(timbrado.getNroPuntoVenta());
        String nroFacturaString = modelo.getNfLarge().format(nroFactura);
        String nroFacturaCompleto = nroTimbrado + "-" + nroSucursal + "-" + nroPuntoVenta + "-" + nroFacturaString;
        this.vista.jtfNroFactura.setText(nroFacturaCompleto);
        this.vista.jtfNroFactura.setEnabled(true);
        isJCBTrigger = true;
    }

    @Override
    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
