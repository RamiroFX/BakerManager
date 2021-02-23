/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import Cliente.SeleccionarCliente;
import Entities.E_facturaDetalle;
import Entities.E_impuesto;
import Entities.M_cliente;
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
public class C_crearPedido extends MouseAdapter implements ActionListener, KeyListener,
        RecibirClienteCallback, RecibirProductoCallback {

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
        if (!this.modelo.getDtm().getList().isEmpty()) {
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
        this.vista.jcbTipoVenta.setEnabled(false);
        this.vista.jtfNroFactura.setEnabled(false);
        this.modelo.getPedido().setIdCondVenta(TipoOperacion.CONTADO);
        this.modelo.getPedido().setEstado(PedidoEstado.PENDIENTE.getDescripcion());
        this.modelo.getPedido().setIdEstado(PedidoEstado.PENDIENTE.getId());
        this.vista.jtPedidoDetalle.setModel(this.modelo.getDtm());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbPagarPedido.setEnabled(false);
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
//        this.modelo.getDetalles().add(detalle);
//        this.modelo.getDtm().addRow(rowData);
//        this.vista.jtPedidoDetalle.updateUI();
//        Utilities.c_packColumn.packColumns(this.vista.jtPedidoDetalle, 1);
//        sumarTotal();
    }

    private void eliminarDetalle() {
        int row = this.vista.jtPedidoDetalle.getSelectedRow();
        if (row < 0) {
            return;
        }
        this.modelo.getDtm().quitarDetalle(row);
        this.vista.jtPedidoDetalle.updateUI();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        sumarTotal();
    }

    private void sumarTotal() {
        double exenta = 0;
        double total5 = 0;
        double total10 = 0;
        double totalIva5 = 0;
        double totalIva10 = 0;
        double total = 0;
        for (E_facturaDetalle fade : modelo.getDtm().getList()) {
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
        if (this.modelo.getDtm().getList().isEmpty()) {
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
                if (modelo.getDtm().getList().isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Seleccione por lo menos un producto.", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!establecerHoraEntrega()) {
                    return;
                }
                int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir el pedido?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    Impresora.imprimirTicketPedido(DatosUsuario.getRol_usuario(), modelo.getPedido(), modelo.getDtm().getList());
                }
            }
        });
    }

    private void modificarDetalle() {
        int row = vista.jtPedidoDetalle.getSelectedRow();
        if (row > -1) {
            M_producto producto = modelo.getDtm().getList().get(row).getProducto();
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(vista, producto, this, row);
            scp.setVisible(true);
        }
    }

    private void invocarSeleccionarCliente() {
        SeleccionarCliente sc = new SeleccionarCliente(this.gestionPedido.c_inicio.vista);
        sc.setCallback(this);
        sc.mostrarVista();
    }

    private void invocarSeleccionarProducto() {
        SeleccionarProducto sp = new SeleccionarProducto(this);
        sp.mostrarVista();
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
            invocarSeleccionarProducto();
        } else if (source.equals(this.vista.jbCliente)) {
            invocarSeleccionarCliente();
        } else if (source.equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        } else if (source.equals(this.vista.jbModificarDetalle)) {
            modificarDetalle();
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
                invocarSeleccionarCliente();
                break;
            }
            case KeyEvent.VK_F4: {
                invocarSeleccionarProducto();
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

    @Override
    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modificarProducto(int row, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        this.modelo.getDtm().modificarDetalle(row, cantidad, descuento, precio, observacion);
        this.vista.jtPedidoDetalle.updateUI();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        sumarTotal();
    }
}
