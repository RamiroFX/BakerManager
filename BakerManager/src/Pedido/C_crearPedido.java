/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import Cliente.Seleccionar_cliente;
import Entities.M_cliente;
import Entities.M_pedidoDetalle;
import Entities.M_producto;
import MenuPrincipal.DatosUsuario;
import Parametros.PedidoEstado;
import Parametros.TipoOperacion;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Impresora.Impresora;
import com.nitido.utils.toaster.Toaster;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearPedido extends MouseAdapter implements ActionListener, KeyListener {

    public M_crearPedido modelo;
    public V_crearPedido vista;
    public C_gestionPedido gestionPedido;

    public C_crearPedido(M_crearPedido modelo, V_crearPedido vista, C_gestionPedido gestionPedido) {
        this.modelo = modelo;
        this.vista = vista;
        this.gestionPedido = gestionPedido;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        vista.setVisible(true);
    }

    private void cerrar() {
        if (!this.modelo.getDetalles().isEmpty()) {
            int opcion = JOptionPane.showConfirmDialog(vista, "Hay producto cargados, ¿Desea cancelar?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                this.vista.dispose();
            }
        }
        this.vista.dispose();
    }

    private void inicializarVista() {
        Vector condCompra = modelo.obtenerTipoOperacion();
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondVenta.addItem(condCompra.get(i));
        }
        Vector tipoVenta = modelo.obtenerTipoVenta();
        for (int i = 0; i < tipoVenta.size(); i++) {
            this.vista.jcbTipoVenta.addItem(tipoVenta.get(i));
        }
        this.modelo.getPedido().setIdCondVenta(TipoOperacion.CONTADO);
        this.modelo.getPedido().setEstado(PedidoEstado.PENDIENTE.getDescripcion());
        this.modelo.getPedido().setIdEstado(PedidoEstado.PENDIENTE.getId());
        this.vista.jtfNroFactura.setEditable(false);
        this.vista.jtPedidoDetalle.setModel(this.modelo.getDtm());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        java.awt.Font fuente = new java.awt.Font("Times New Roman", 0, 18);
        javax.swing.text.DefaultFormatterFactory dff = new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance()));
        this.vista.jftExenta.setFormatterFactory(dff);
        this.vista.jftExenta.setFont(fuente); // NOI18N
        this.vista.jftIva5.setFormatterFactory(dff);
        this.vista.jftIva5.setFont(fuente); // NOI18N
        this.vista.jftIva10.setFormatterFactory(dff);
        this.vista.jftIva10.setFont(fuente); // NOI18N
        this.vista.jftTotal.setFormatterFactory(dff);
        this.vista.jftTotal.setFont(fuente); // NOI18N
        //////////////////////
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaEntrega.setDate(calendar.getTime());
        Date currentTime = calendar.getTime();
        String horaT = sdf.format(currentTime).substring(0, 2);
        int horas = Integer.valueOf(horaT);
        //Se suma una hora para que la hora de entrega tenga una hora mas que la hora actual
        int horaAux = horas + 1;
        if (horaAux >= 0 && horaAux < 10) {
            this.vista.jcbHora.setSelectedItem("0" + horaAux);
        } else {
            this.vista.jcbHora.setSelectedItem("" + horaAux);
        }
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, Integer.valueOf(vista.jcbHora.getSelectedItem() + ""));
        now.set(Calendar.MINUTE, Integer.valueOf(vista.jcbMinuto.getSelectedItem() + ""));
        this.modelo.getPedido().setTiempoEntrega(new Timestamp(now.getTimeInMillis()));
        establecerCondicionVenta();
    }

    private void agregarListeners() {
        this.vista.jtPedidoDetalle.addMouseListener(this);
        this.vista.jcbCondVenta.addActionListener(this);
        this.vista.jcbTipoVenta.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSeleccionarProducto.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jcbHora.addActionListener(this);
        this.vista.jcbMinuto.addActionListener(this);
        this.vista.jbSeleccionarProducto.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbImprimir.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jcbCondVenta.addKeyListener(this);
        this.vista.jcbTipoVenta.addKeyListener(this);
        this.vista.jcbHora.addKeyListener(this);
        this.vista.jcbMinuto.addKeyListener(this);
    }

    private void establecerCondicionVenta() {
        if (this.vista.jcbCondVenta.getSelectedIndex() == 0) {
            this.modelo.getPedido().setIdCondVenta(TipoOperacion.CONTADO);
        } else {
            this.modelo.getPedido().setIdCondVenta(TipoOperacion.CREDITO);
        }
    }

    public void recibirDetalle(M_pedidoDetalle detalle) {
        Integer impExenta = null;
        Integer imp5 = null;
        Integer imp10 = null;
        Integer Precio = detalle.getPrecio() - Math.round(Math.round(((detalle.getPrecio() * detalle.getDescuento()) / 100)));
        Integer total = Math.round(Math.round((detalle.getCantidad() * Precio)));
        if (detalle.getProducto().getImpuesto().equals(0)) {
            impExenta = total;
            imp5 = 0;
            imp10 = 0;
        } else if (detalle.getProducto().getImpuesto().equals(5)) {
            impExenta = 0;
            imp5 = total;
            imp10 = 0;
        } else {
            impExenta = 0;
            imp5 = 0;
            imp10 = total;
        }
        if (null != detalle.getObservacion()) {
            String aux = detalle.getProducto().getDescripcion();
            detalle.getProducto().setDescripcion(aux + "-(" + detalle.getObservacion() + ")");
        }
        Object[] rowData = {detalle.getProducto().getId(), detalle.getProducto().getDescripcion(), detalle.getCantidad(), detalle.getPrecio(), detalle.getDescuento(), impExenta, imp5, imp10};
        detalle.setIva_exenta(impExenta);
        detalle.setIva_cinco(imp5);
        detalle.setIva_diez(imp10);
        this.modelo.getDetalles().add(detalle);
        this.modelo.getDtm().addRow(rowData);
        this.vista.jtPedidoDetalle.updateUI();
        Utilities.c_packColumn.packColumns(this.vista.jtPedidoDetalle, 1);
        sumarTotal();
    }

    private void eliminarDetalle() {
        int row = this.vista.jtPedidoDetalle.getSelectedRow();
        this.modelo.getDetalles().remove(row);
        this.modelo.getDtm().removeRow(row);
        this.vista.jtPedidoDetalle.updateUI();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        sumarTotal();
    }

    public void modificarDetalle(Double cantidad, Integer precio, Double descuento, String observacion, int row) {
        this.modelo.getDtm().setValueAt(cantidad, row, 2);//producto
        this.modelo.getDtm().setValueAt(precio, row, 3);//precio
        this.modelo.getDtm().setValueAt(descuento, row, 4);//descuento
        M_producto prod = this.modelo.getDetalles().get(row).getProducto();
        String producto = prod.getDescripcion();
        if (null != observacion) {
            if (!observacion.isEmpty()) {
                producto = producto + "- (" + observacion + ")";
            }
        }
        this.modelo.getDtm().setValueAt(producto, row, 1);
        Integer impExenta = null;
        Integer imp5 = null;
        Integer imp10 = null;
        Integer Precio = precio - Math.round(Math.round(((precio * descuento) / 100)));
        Integer total = Math.round(Math.round((cantidad * Precio)));
        if (prod.getImpuesto().equals(0)) {
            impExenta = total;
            imp5 = 0;
            imp10 = 0;
        } else if (prod.getImpuesto().equals(5)) {
            impExenta = 0;
            imp5 = total;
            imp10 = 0;
        } else {
            impExenta = 0;
            imp5 = 0;
            imp10 = total;
        }
        this.modelo.getDtm().setValueAt(impExenta, row, 5);
        this.modelo.getDtm().setValueAt(imp5, row, 6);
        this.modelo.getDtm().setValueAt(imp10, row, 7);
        M_pedidoDetalle detalle = this.modelo.getDetalles().get(row);
        detalle.setCantidad(cantidad);
        detalle.setPrecio(precio);
        detalle.setDescuento(descuento);
        detalle.setObservacion(observacion);
        this.vista.jtPedidoDetalle.updateUI();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        sumarTotal();
    }

    private void sumarTotal() {
        Integer exenta = 0;
        Integer iva5 = 0;
        Integer iva10 = 0;
        Integer total = 0;
        int cantFilas = this.modelo.getDtm().getRowCount();
        for (int i = 0; i < cantFilas; i++) {
            exenta = exenta + Integer.valueOf(String.valueOf(this.modelo.getDtm().getValueAt(i, 5)));
            iva5 = iva5 + Integer.valueOf(String.valueOf(this.modelo.getDtm().getValueAt(i, 6)));
            iva10 = iva10 + Integer.valueOf(String.valueOf(this.modelo.getDtm().getValueAt(i, 7)));
        }
        total = exenta + iva5 + iva10;
        this.vista.jftExenta.setValue(exenta);
        this.vista.jftIva5.setValue(iva5);
        this.vista.jftIva10.setValue(iva10);
        this.vista.jftTotal.setValue(total);
    }

    public void recibirCliente(M_cliente cliente) {
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
    }

    private void guardarPedido() {
        if (null == this.modelo.getPedido().getCliente().getIdCliente()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un Cliente.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (this.modelo.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione por lo menos un producto.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Date now = Calendar.getInstance().getTime();
        Date entrega = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaEntrega = sdf.format(vista.jdcFechaEntrega.getDate()) + " " + vista.jcbHora.getSelectedItem() + ":" + vista.jcbMinuto.getSelectedItem() + ":00";
        try {
            entrega = sdfs.parse(fechaEntrega);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vista, "La fecha de entrega debe ser mayor que la fecha fecha actual (" + sdfs.format(now) + ").", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (now.before(entrega)) {
            this.modelo.getPedido().setTiempoEntrega(new Timestamp(entrega.getTime()));
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
            this.modelo.insertarPedido();
            mostrarMensaje("El pedido se registró con éxito.");
        } else {
            vista.jdcFechaEntrega.setDate(now);
            vista.jdcFechaEntrega.updateUI();
            JOptionPane.showMessageDialog(vista, "La fecha de entrega debe ser mayor que la fecha fecha actual (" + sdfs.format(now) + ").", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.vista.dispose();
    }

    private boolean establecerHoraEntrega() {
        Date now = Calendar.getInstance().getTime();
        Date entrega = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaEntrega = sdf.format(vista.jdcFechaEntrega.getDate()) + " " + vista.jcbHora.getSelectedItem() + ":" + vista.jcbMinuto.getSelectedItem() + ":00";
        try {
            entrega = sdfs.parse(fechaEntrega);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vista, "La fecha de entrega debe ser mayor que la fecha fecha actual (" + sdfs.format(now) + ").", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (now.before(entrega)) {
            this.modelo.getPedido().setTiempoEntrega(new Timestamp(entrega.getTime()));
            return true;
        } else {
            JOptionPane.showMessageDialog(vista, "La fecha de entrega debe ser mayor que la fecha fecha actual (" + sdfs.format(now) + ").", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            String horaT = sdf.format(now).substring(0, 2);
            int horas = Integer.valueOf(horaT);
            //Se suma una hora para que la hora de entrega tenga una hora mas que la hora actual
            int horaAux = horas + 1;
            if (horaAux >= 0 && horaAux < 10) {
                this.vista.jcbHora.setSelectedItem("0" + horaAux);
            } else {
                this.vista.jcbHora.setSelectedItem("" + horaAux);
            }
        }
        return false;
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    private void imprimir() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (null == modelo.getPedido().getCliente().getIdCliente()) {
                    JOptionPane.showMessageDialog(vista, "Seleccione un Cliente.", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (modelo.getDetalles().isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Seleccione por lo menos un producto.", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!establecerHoraEntrega()) {
                    return;
                }
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
            guardarPedido();
        } else if (source.equals(this.vista.jcbCondVenta)) {
            establecerCondicionVenta();
        } else if (source.equals(this.vista.jcbTipoVenta)) {
            //establecerCondicionVenta();
        } else if (source.equals(this.vista.jcbHora)) {
            establecerHoraEntrega();
        } else if (source.equals(this.vista.jcbMinuto)) {
            establecerHoraEntrega();
        } else if (source.equals(this.vista.jbSeleccionarProducto)) {
            SeleccionarProducto sp = new SeleccionarProducto(this);
            sp.mostrarVista();
        } else if (source.equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this);
            sc.mostrarVista();
        } else if (source.equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        } else if (source.equals(this.vista.jbModificarDetalle)) {
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, this.vista.jtPedidoDetalle.getSelectedRow());
            scp.setVisible(true);
        } else if (source.equals(this.vista.jbImprimir)) {
            imprimir();
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1: {
                guardarPedido();
                break;
            }
            case KeyEvent.VK_F2: {
                imprimir();
                break;
            }
            case KeyEvent.VK_F3: {
                Seleccionar_cliente sp = new Seleccionar_cliente(this);
                sp.mostrarVista();
                break;
            }
            case KeyEvent.VK_F4: {
                SeleccionarProducto sp = new SeleccionarProducto(this);
                sp.mostrarVista();
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
