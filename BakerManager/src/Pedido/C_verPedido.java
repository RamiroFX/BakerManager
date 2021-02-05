/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import Cliente.SeleccionarCliente;
import DB.DB_Ingreso;
import Entities.E_impresionTipo;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_pedidoDetalle;
import Entities.M_producto;
import MenuPrincipal.DatosUsuario;
import Parametros.PedidoEstado;
import Parametros.TipoOperacion;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Impresora.Impresora;
import Interface.RecibirClienteCallback;
import Interface.RecibirProductoCallback;
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
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_verPedido extends MouseAdapter implements ActionListener, KeyListener, 
        RecibirClienteCallback, RecibirProductoCallback {

    public V_crearPedido vista;
    public M_verPedido modelo;
    public C_gestionPedido gestionPedido;

    public C_verPedido(V_crearPedido vista, M_verPedido modelo, C_gestionPedido gestionPedido) {
        this.vista = vista;
        this.vista.setTitle("Ver pedido");
        this.modelo = modelo;
        this.gestionPedido = gestionPedido;
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
        this.vista.jtfClieDireccion.setText(this.modelo.getPedido().getCliente().getDireccion());
        String ruc = this.modelo.getPedido().getCliente().getRuc();
        String rucDiv = this.modelo.getPedido().getCliente().getRucId();
        this.vista.jtfClieRuc.setText(ruc + "-" + rucDiv);
        String nombre = this.modelo.getPedido().getCliente().getNombre();
        String entidad = this.modelo.getPedido().getCliente().getEntidad();
        this.vista.jtfCliente.setText(nombre + " (" + entidad + ")");
        this.vista.jtfDireccionPedido.setText(this.modelo.getPedido().getDireccion());
        this.vista.jtfNroFactura.setText("");
        this.vista.jtfNroFactura.setEnabled(false);
        this.vista.jtfReferencia.setText(this.modelo.getPedido().getReferencia());
        this.vista.jdcFechaEntrega.setDate(this.modelo.getPedido().getTiempoEntrega());
        this.vista.jtPedidoDetalle.setModel(this.modelo.getRstm());
        Vector condCompra = modelo.obtenerTipoOperacion();
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondVenta.addItem(condCompra.get(i));
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
        int condVenta = this.modelo.getPedido().getIdCondVenta();
        this.vista.jcbCondVenta.setSelectedIndex(condVenta - 1);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        if (!this.modelo.getPedido().getEstado().equals(PedidoEstado.PENDIENTE.getDescripcion())) {
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
                int idFaca = modelo.getPedido().getIdFacturaCabecera();
                M_facturaCabecera faca = DB_Ingreso.obtenerIngresoCabeceraCompleto(idFaca);
                if (faca.getNroFactura() != null) {
                    if (faca.getNroFactura() > 0) {
                        this.vista.jtfNroFactura.setText(faca.getNroFactura() + "");
                        this.vista.jcbTipoVenta.setSelectedIndex(1);
                    } else {
                        this.vista.jcbTipoVenta.setSelectedIndex(0);
                    }
                }
            }
        }
        sumarTotal();
    }

    private void agregarListeners() {
        if (this.modelo.getPedido().getEstado().equals("Pendiente")) {
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
        }
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
        this.vista.jbImprimir.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private void establecerCondicionVenta() {
        if (this.vista.jcbCondVenta.getSelectedIndex() == 0) {
            this.modelo.getPedido().setIdCondVenta(TipoOperacion.CONTADO);
        } else {
            this.modelo.getPedido().setIdCondVenta(TipoOperacion.CREDITO);
        }
    }

    private void establecerTipoVenta() {
        E_impresionTipo tipoVenta = this.vista.jcbTipoVenta.getItemAt(this.vista.jcbTipoVenta.getSelectedIndex());
        switch (tipoVenta.getDescripcion()) {
            case "ticket": {
                this.vista.jtfNroFactura.setText("");
                this.vista.jtfNroFactura.setEnabled(false);
                this.modelo.setTipoVenta(tipoVenta);
                break;
            }
            case "factura": {
                this.vista.jtfNroFactura.setText(modelo.getNroFactura() + "");
                this.vista.jtfNroFactura.setEnabled(true);
                this.modelo.setTipoVenta(tipoVenta);
                break;
            }
            case "boleta": {
                this.vista.jtfNroFactura.setText("");
                this.vista.jtfNroFactura.setEnabled(false);
                this.modelo.setTipoVenta(tipoVenta);
                break;
            }
        }
    }

    public void recibirDetalle(M_pedidoDetalle detalle) {
        detalle.setIdPedido(modelo.getPedido().getIdPedido());
        Double Precio = detalle.getPrecio() - Math.round(Math.round(((detalle.getPrecio() * detalle.getDescuento()) / 100)));
        Double total = (detalle.getCantidad() * Precio);
        switch (detalle.getProducto().getImpuesto()) {
            case (0): {//exenta
                detalle.setIva_exenta(total);
                detalle.setIva_cinco(0.0);
                detalle.setIva_diez(0.0);
                break;
            }
            case (5): {//iva 5%
                detalle.setIva_exenta(0.0);
                detalle.setIva_cinco(total);
                detalle.setIva_diez(0.0);
                break;
            }
            case (10): {//iva10%
                detalle.setIva_exenta(0.0);
                detalle.setIva_cinco(0.0);
                detalle.setIva_diez(total);
                break;
            }
        }
        this.modelo.insertarPedidoDetalle(detalle);
        this.modelo.actualizarTablaPedidoDetalle();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jtPedidoDetalle.setModel(this.modelo.getRstm());
        Utilities.c_packColumn.packColumns(this.vista.jtPedidoDetalle, 1);
        sumarTotal();
    }

    private void eliminarDetalle() {
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            int row = this.vista.jtPedidoDetalle.getSelectedRow();
            int idPedidoDetalle = Integer.valueOf(String.valueOf(this.vista.jtPedidoDetalle.getValueAt(row, 0)));
            this.modelo.getDetalle().setIdPedioDetalle(idPedidoDetalle);
            this.modelo.eliminarPedidoDetalle();
            this.modelo.actualizarTablaPedidoDetalle();
            this.vista.jtPedidoDetalle.setModel(this.modelo.getRstm());
            this.vista.jbEliminarDetalle.setEnabled(false);
            this.vista.jbModificarDetalle.setEnabled(false);
            sumarTotal();
        }
    }

    private void jbModificarDetalleButtonHandler() {
        System.out.println("198-verPedido-jbModificarDetalleButtonHandler");
        int row = this.vista.jtPedidoDetalle.getSelectedRow();
        int idPedidoDetalle = Integer.valueOf(String.valueOf(this.vista.jtPedidoDetalle.getValueAt(row, 0)));
        int idProducto = Integer.valueOf(String.valueOf(this.vista.jtPedidoDetalle.getValueAt(row, 1)));
        M_producto producto = modelo.getDetalles().get(row).getProducto();
        SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(vista, producto,this, SeleccionarCantidadProduducto.PRECIO_VENTA_MINORISTA);
        scp.setVisible(true);
    }

    public void modificarDetalle(Double cantidad, Double precio, Double descuento, String observacion, int idDetalle) {
        this.modelo.getDetalle().setCantidad(cantidad);
        this.modelo.getDetalle().setPrecio(precio);
        this.modelo.getDetalle().setDescuento(descuento);
        this.modelo.getDetalle().setObservacion(observacion);
        this.modelo.getDetalle().setIdPedioDetalle(idDetalle);
        this.modelo.actualizarPedidoDetalle();
        this.modelo.actualizarTablaPedidoDetalle();
        this.vista.jtPedidoDetalle.setModel(this.modelo.getRstm());
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        sumarTotal();
    }

    private void sumarTotal() {
        Integer exenta = 0;
        Integer iva5 = 0;
        Integer iva10 = 0;
        Integer total = 0;
        for (int i = 0; i < this.modelo.getRstm().getRowCount(); i++) {
            exenta = exenta + Integer.valueOf(String.valueOf(this.modelo.getRstm().getValueAt(i, 6)));
            iva5 = iva5 + Integer.valueOf(String.valueOf(this.modelo.getRstm().getValueAt(i, 7)));
            iva10 = iva10 + Integer.valueOf(String.valueOf(this.modelo.getRstm().getValueAt(i, 8)));
        }
        total = exenta + iva5 + iva10;
        this.vista.jftExenta.setValue(exenta);
        this.vista.jftIva5.setValue(iva5);
        this.vista.jftIva10.setValue(iva10);
        this.vista.jftTotal.setValue(total);
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            this.modelo.getPedido().setCliente(cliente);
            String nombre = this.modelo.getPedido().getCliente().getNombre();
            String entidad = this.modelo.getPedido().getCliente().getEntidad();
            this.vista.jtfCliente.setText(nombre + " (" + entidad + ")");

            String ruc = this.modelo.getPedido().getCliente().getRuc();
            String ruc_id = this.modelo.getPedido().getCliente().getRucId();
            this.vista.jtfClieRuc.setText(ruc + "-" + ruc_id);
            String direccion = this.modelo.getPedido().getCliente().getDireccion();
            this.vista.jtfClieDireccion.setText(direccion);
            this.vista.jtfDireccionPedido.setText(direccion);
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
            Integer nroFactura = null;
            E_impresionTipo tipoVenta = this.vista.jcbTipoVenta.getItemAt(this.vista.jcbTipoVenta.getSelectedIndex());
            if (tipoVenta.getDescripcion().equals("factura")) {
                nroFactura = modelo.getNroFactura();
            }
            //GUARDAR VENTA
            int idFaca = this.modelo.pagarPedido(idPedido, nroFactura);
            ArrayList<M_facturaDetalle> fades = DB_Ingreso.obtenerVentaDetalles(idFaca);
            M_facturaCabecera faca = DB_Ingreso.obtenerIngresoCabeceraCompleto(idFaca);
            //IMPRIMIR VENTA
            int opcion2 = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir la venta?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcion2 == JOptionPane.YES_OPTION) {
                switch (tipoVenta.getDescripcion()) {
                    case "ticket": {
                        Impresora.imprimirTicketVenta(DatosUsuario.getRol_usuario(), faca, fades);
                        break;
                    }
                    case "factura": {
                        Impresora.imprimirFacturaVenta(faca, fades);
                        break;
                    }
                    case "boleta": {
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
                    Impresora.imprimirTicketPedido(DatosUsuario.getRol_usuario(), modelo.getPedido(), modelo.getDetalles());
                }
            }
        });
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
            SeleccionarProducto sp = new SeleccionarProducto(this);
            sp.mostrarVista();
        } else if (source.equals(this.vista.jbCliente)) {
            SeleccionarCliente sc = new SeleccionarCliente(this.gestionPedido.c_inicio.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        } else if (source.equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        } else if (source.equals(this.vista.jbModificarDetalle)) {
            jbModificarDetalleButtonHandler();
        } else if (source.equals(this.vista.jbImprimir)) {
            imprimir();
        } else if (source.equals(this.vista.jbPagarPedido)) {
            pagarPedido();
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
            SeleccionarProducto sp = new SeleccionarProducto(this);
            sp.mostrarVista();
        }
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            SeleccionarCliente sc = new SeleccionarCliente(this.gestionPedido.c_inicio.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
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
