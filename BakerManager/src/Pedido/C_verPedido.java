/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import Cliente.SeleccionarCliente;
import Configuracion.Timbrado.SeleccionarNroFactura;
import DB.DB_Ingreso;
import Entities.E_Timbrado;
import Entities.E_facturaDetalle;
import Entities.E_impresionTipo;
import Entities.E_impuesto;
import Entities.E_tipoOperacion;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_producto;
import MenuPrincipal.DatosUsuario;
import Parametros.PedidoEstado;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Impresora.Impresora;
import Interface.RecibirClienteCallback;
import Interface.RecibirProductoCallback;
import Interface.RecibirTimbradoVentaCallback;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_verPedido extends MouseAdapter implements ActionListener, KeyListener,
        RecibirClienteCallback, RecibirProductoCallback, RecibirTimbradoVentaCallback {

    public V_crearPedido vista;
    public M_verPedido modelo;
    public C_gestionPedido gestionPedido;
    private boolean isJCBTrigger;

    public C_verPedido(V_crearPedido vista, M_verPedido modelo, C_gestionPedido gestionPedido) {
        this.vista = vista;
        this.vista.setTitle("Ver pedido");
        this.modelo = modelo;
        this.gestionPedido = gestionPedido;
        this.isJCBTrigger = true;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void cerrar() {
        this.vista.dispose();
    }

    private void inicializarVista() {
        this.vista.jtfClieDireccion.setText(this.modelo.obtenerDireccionCliente());
        this.vista.jtfClieRuc.setText(this.modelo.obtenerRucCliente());
        this.vista.jtfCliente.setText(this.modelo.obtenerNombreCliente());
        this.vista.jtfDireccionPedido.setText(this.modelo.getPedido().getDireccion());
        this.vista.jtfNroFactura.setText("");
        this.vista.jtfNroFactura.setEnabled(false);
        this.vista.jtfReferencia.setText(this.modelo.getPedido().getReferencia());
        this.vista.jdcFechaEntrega.setDate(this.modelo.getPedido().getTiempoEntrega());
        this.vista.jtPedidoDetalle.setModel(this.modelo.getTm());
        ArrayList<E_tipoOperacion> condVenta = modelo.obtenerTipoOperacion();
        for (int i = 0; i < condVenta.size(); i++) {
            this.vista.jcbCondVenta.addItem(condVenta.get(i));
        }
        ArrayList<E_impresionTipo> tipoVenta = modelo.obtenerTipoVenta();
        for (int i = 0; i < tipoVenta.size(); i++) {
            this.vista.jcbTipoVenta.addItem(tipoVenta.get(i));
        }
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(this.modelo.getPedido().getTiempoEntrega());
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        String _hora = "";
        String _minuto = "";
        if (hora < 10) {
            _hora = "0" + hora;
        } else {
            _hora = hora + "";
        }
        if (minuto < 10) {
            _minuto = "0" + minuto;
        } else {
            _minuto = minuto + "";
        }
        this.vista.jcbHora.setSelectedItem(_hora);
        this.vista.jcbMinuto.setSelectedItem(_minuto);
        this.vista.jcbCondVenta.setSelectedItem(this.modelo.getPedido().getTipoOperacion());
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        if (!this.modelo.getPedido().getEstadoPedido().getDescripcion().equals(PedidoEstado.PENDIENTE.getDescripcion())) {
            this.vista.jbAceptar.setEnabled(false);
            this.vista.jbCliente.setEnabled(false);
            this.vista.jbSeleccionarProducto.setEnabled(false);
            this.vista.jcbHora.setEnabled(false);
            this.vista.jcbMinuto.setEnabled(false);
            this.vista.jdcFechaEntrega.setEnabled(false);
            this.vista.jtfDireccionPedido.setEnabled(false);
            this.vista.jtfReferencia.setEnabled(false);
            this.vista.jcbCondVenta.setEnabled(false);
            this.vista.jcbTipoVenta.setEnabled(false);
            this.vista.jbPagarPedido.setEnabled(false);
            if (modelo.getPedido().getIdFacturaCabecera() != null) {
                this.vista.jtfNroFactura.setText(modelo.obtenerNroFactura());
                this.vista.jcbTipoVenta.setSelectedIndex(1);
            }
            this.vista.jcbTipoVenta.setSelectedIndex(0);
        }
        javax.swing.text.DefaultFormatterFactory dffTotal = new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##")));
        javax.swing.text.DefaultFormatterFactory dffIva = new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0")));

        this.vista.jftExenta.setFormatterFactory(dffIva);
        this.vista.jftIva5.setFormatterFactory(dffIva);
        this.vista.jftIva10.setFormatterFactory(dffIva);
        this.vista.jftTotal.setFormatterFactory(dffTotal);
        sumarTotal();
    }

    private void agregarListeners() {
        if (this.modelo.getPedido().getEstadoPedido().getDescripcion().equals("Pendiente")) {
            this.vista.jtPedidoDetalle.addMouseListener(this);
            this.vista.jcbCondVenta.addActionListener(this);
            this.vista.jcbTipoVenta.addActionListener(this);
            this.vista.jbAceptar.addActionListener(this);
            this.vista.jbSeleccionarProducto.addActionListener(this);
            this.vista.jbCliente.addActionListener(this);
            this.vista.jbEliminarDetalle.addActionListener(this);
            this.vista.jbModificarDetalle.addActionListener(this);
            this.vista.jbSeleccionarProducto.addKeyListener(this);
            this.vista.jbCliente.addKeyListener(this);
            this.vista.jbAceptar.addKeyListener(this);
            this.vista.jbPagarPedido.addActionListener(this);
            this.vista.jbPagarPedido.addKeyListener(this);
            this.vista.jbNroFactura.addActionListener(this);
            this.vista.jbNroFactura.addKeyListener(this);
        }
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
        this.vista.jbImprimir.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private void establecerCondicionVenta() {
        if (this.vista.jcbCondVenta.getSelectedIndex() == 0) {
            this.modelo.establecerCondicionVentaContado();
        } else {
            this.modelo.establecerCondicionVentaCredito();
        }
    }

    private void establecerTipoVenta() {
        E_impresionTipo tipoVenta = this.vista.jcbTipoVenta.getItemAt(this.vista.jcbTipoVenta.getSelectedIndex());
        switch (tipoVenta.getDescripcion()) {
            case E_impresionTipo.TICKET_STRING: {
                this.vista.jtfNroFactura.setText("");
                this.vista.jtfNroFactura.setEnabled(false);
                this.modelo.setIdTimbrado(1);
                this.modelo.setTipoVenta(tipoVenta);
                break;
            }
            case E_impresionTipo.FACTURA_STRING: {
                if (isJCBTrigger) {
                    this.modelo.setTipoVenta(tipoVenta);
                    invocarSeleccionarNroFactura();
                }
                break;
            }
            case E_impresionTipo.BOLETA_STRING: {
                this.vista.jtfNroFactura.setText("");
                this.vista.jtfNroFactura.setEnabled(false);
                this.modelo.setIdTimbrado(1);
                this.modelo.setTipoVenta(tipoVenta);
                break;
            }
        }
    }

    private void eliminarDetalle() {
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            int row = this.vista.jtPedidoDetalle.getSelectedRow();
            if (row < 0) {
                return;
            }
            int idPedidoDetalle = modelo.getTm().getList().get(row).getIdFacturaDetalle();
            this.modelo.eliminarPedidoDetalle(idPedidoDetalle);
            this.modelo.actualizarTablaPedidoDetalle();
            this.vista.jbEliminarDetalle.setEnabled(false);
            this.vista.jbModificarDetalle.setEnabled(false);
            sumarTotal();
        }
    }

    private void jbModificarDetalleButtonHandler() {
        int row = this.vista.jtPedidoDetalle.getSelectedRow();
        if (row < 0) {
            return;
        }
        E_facturaDetalle fade = modelo.getTm().getList().get(row);
        SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(vista, fade.getProducto(), this, SeleccionarCantidadProduducto.PRECIO_VENTA_MINORISTA, row);
        scp.loadData(fade.getCantidad(), fade.getDescuento(), fade.getPrecio(), fade.getObservacion());
        scp.setVisible(true);
    }

    private void sumarTotal() {
        double exenta = 0;
        double total5 = 0;
        double total10 = 0;
        double totalIva5 = 0;
        double totalIva10 = 0;
        double total = 0;
        for (E_facturaDetalle fade : modelo.getTm().getList()) {
            switch (fade.getProducto().getIdImpuesto()) {
                case E_impuesto.EXENTA: {
                    exenta = exenta + fade.calcularSubTotal();
                    break;
                }
                case E_impuesto.IVA5: {
                    total5 = total5 + fade.calcularSubTotal();
                    break;
                }
                case E_impuesto.IVA10: {
                    total10 = total10 + fade.calcularSubTotal();
                    break;
                }
            }
        }
        total = exenta + total5 + total10;
        totalIva5 = total5 / 21;
        totalIva10 = total10 / 11;
        this.vista.jftExenta.setValue(exenta);
        this.vista.jftIva5.setValue(totalIva5);
        this.vista.jftIva10.setValue(totalIva10);
        this.vista.jftTotal.setValue(total);
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            this.modelo.getPedido().setCliente(cliente);
            this.vista.jtfCliente.setText(modelo.obtenerNombreCliente());
            this.vista.jtfClieRuc.setText(modelo.obtenerRucCliente());
            this.vista.jtfClieDireccion.setText(modelo.obtenerDireccionCliente());
            this.vista.jtfDireccionPedido.setText(modelo.obtenerDireccionCliente());
            //String telefono = this.modelo.getPedido().getCliente().getDireccion();
            this.vista.jtfClieTelefono.setText("");
            this.modelo.actualizarCliente();
        }
    }

    private void guardarVenta() {
        Date today = Calendar.getInstance().getTime();
        Date entrega = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String fechaEntrega = sdf.format(vista.jdcFechaEntrega.getDate()) + " " + vista.jcbHora.getSelectedItem() + ":" + vista.jcbMinuto.getSelectedItem() + ":00";
        try {
            entrega = sdfs.parse(fechaEntrega);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vista, "La fecha de entrega debe ser mayor que la fecha fecha actual (" + sdfs.format(today) + ").", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (today.before(entrega)) {
            String direccion = this.vista.jtfDireccionPedido.getText().trim();
            String referencia = this.vista.jtfReferencia.getText().trim();
            if (direccion.length() > 150) {
                JOptionPane.showMessageDialog(vista, "El campo dirección sobrepasa el máximo de 150 caracteres.", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (direccion.isEmpty()) {
                direccion = null;
            }
            if (referencia.length() > 150) {
                JOptionPane.showMessageDialog(vista, "El campo referncia sobrepasa el máximo de 150 caracteres.", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (referencia.isEmpty()) {
                referencia = null;
            }
            this.modelo.getPedido().setDireccion(direccion);
            this.modelo.getPedido().setReferencia(referencia);
            this.modelo.getPedido().setTiempoEntrega(new Timestamp(entrega.getTime()));
            this.modelo.actualizarPedido();
            cerrar();
        } else {
            JOptionPane.showMessageDialog(vista, "La fecha de entrega debe ser mayor que la fecha fecha actual (" + sdfs.format(today) + ").", "Atención", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void pagarPedido() {
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea confirmas esta operación?\n Venta tipo: " + vista.jcbTipoVenta.getSelectedItem(), "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            Integer idPedido = modelo.getPedido().getIdPedido();
            int nroFactura = -1;
            E_impresionTipo tipoVenta = this.vista.jcbTipoVenta.getItemAt(this.vista.jcbTipoVenta.getSelectedIndex());
            if (tipoVenta.getDescripcion().equals(E_impresionTipo.FACTURA_STRING)) {
                if (!modelo.validarNroFactura()) {
                    JOptionPane.showMessageDialog(vista, "Seleccione un nro de factura", "Atención", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                nroFactura = modelo.getNroFactura();
            }
            //GUARDAR VENTA
            int idFaca = this.modelo.pagarPedido(idPedido, nroFactura);
            ArrayList<E_facturaDetalle> fades = DB_Ingreso.obtenerVentaDetalles(idFaca);
            M_facturaCabecera faca = DB_Ingreso.obtenerIngresoCabeceraCompleto(idFaca);
            //IMPRIMIR VENTA
            int opcion2 = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir la venta?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcion2 == JOptionPane.YES_OPTION) {
                switch (tipoVenta.getDescripcion()) {
                    case E_impresionTipo.TICKET_STRING: {
                        Impresora.imprimirTicketVenta(DatosUsuario.getRol_usuario(), faca, fades);
                        break;
                    }
                    case E_impresionTipo.FACTURA_STRING: {
                        Impresora.imprimirFacturaVenta(faca, fades);
                        break;
                    }
                    case E_impresionTipo.BOLETA_STRING: {
                        Impresora.imprimirBoletaVenta(faca, fades);
                        break;
                    }
                }
            }
            cerrar();
        }
    }

    private void imprimir() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir el pedido?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    Impresora.imprimirTicketPedido(DatosUsuario.getRol_usuario(), modelo.getPedido(), modelo.getTm().getList());
                }
            }
        });
    }

    public void invocarSeleccionarProducto() {
        SeleccionarProducto sp = new SeleccionarProducto(this.vista, this);
        sp.mostrarVista();
    }

    public void invocarSeleccionarCliente() {
        SeleccionarCliente sc = new SeleccionarCliente(this.gestionPedido.c_inicio.vista);
        sc.setCallback(this);
        sc.mostrarVista();
    }

    public void invocarSeleccionarNroFactura() {
        SeleccionarNroFactura st = new SeleccionarNroFactura(this.vista, this, modelo.obtenerTimbradoPredeterminado());
        st.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbAceptar)) {
            guardarVenta();
        } else if (source.equals(this.vista.jcbCondVenta)) {
            establecerCondicionVenta();
        } else if (source.equals(this.vista.jcbTipoVenta)) {
            establecerTipoVenta();
        } else if (source.equals(this.vista.jbSeleccionarProducto)) {
            invocarSeleccionarProducto();
        } else if (source.equals(this.vista.jbCliente)) {
            invocarSeleccionarCliente();
        } else if (source.equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        } else if (source.equals(this.vista.jbModificarDetalle)) {
            jbModificarDetalleButtonHandler();
        } else if (source.equals(this.vista.jbImprimir)) {
            imprimir();
        } else if (source.equals(this.vista.jbPagarPedido)) {
            pagarPedido();
        } else if (source.equals(this.vista.jbNroFactura)) {
            invocarSeleccionarNroFactura();
        } else if (source.equals(this.vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtPedidoDetalle)) {
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
            imprimir();
        }
        if (e.getKeyCode() == KeyEvent.VK_F5) {
            pagarPedido();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrar();
        }
        if (e.getKeyCode() == KeyEvent.VK_F4) {
            invocarSeleccionarProducto();
        }
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            invocarSeleccionarCliente();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        this.modelo.insertarPedidoDetalle(cantidad, precio, descuento, producto, observacion);
        this.modelo.actualizarTablaPedidoDetalle();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        Utilities.c_packColumn.packColumns(this.vista.jtPedidoDetalle, 1);
        sumarTotal();
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        this.modelo.actualizarPedidoDetalle(posicion, cantidad, precio, descuento, producto, observacion);
        this.modelo.actualizarTablaPedidoDetalle();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.modelo.actualizarTablaPedidoDetalle();
        sumarTotal();
    }

    @Override
    public void recibirTimbrado(E_Timbrado timbrado) {
    }

    @Override
    public void recibirTimbradoNroFactura(E_Timbrado timbrado, int nroFactura) {
        E_impresionTipo tipoVenta = new E_impresionTipo(E_impresionTipo.FACTURA, E_impresionTipo.FACTURA_STRING);
        modelo.getPedido().setTimbrado(timbrado);
        modelo.getPedido().setNroFactura(nroFactura);
        modelo.setTipoVenta(tipoVenta);
        isJCBTrigger = false;
        this.vista.jcbTipoVenta.setSelectedItem(tipoVenta);
        this.vista.jtfNroFactura.setText(modelo.obtenerNroFactura());
        this.vista.jtfNroFactura.setEnabled(true);
        isJCBTrigger = true;
    }
}
