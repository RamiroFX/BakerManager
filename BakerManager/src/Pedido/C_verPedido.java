/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import Cliente.Seleccionar_cliente;
import Entities.M_cliente;
import Entities.M_pedidoDetalle;
import MenuPrincipal.DatosUsuario;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Utilities.Impresora;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_verPedido extends MouseAdapter implements ActionListener {

    public V_crearPedido vista;
    public M_verPedido modelo;
    public C_gestionPedido gestionPedido;

    public C_verPedido(V_crearPedido vista, M_verPedido modelo, C_gestionPedido gestionPedido) {
        this.vista = vista;
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
        //this.vista.jtfClieTelefono.setText(this.modelo.getPedido().getCliente().getDireccion());
        String nombre = this.modelo.getPedido().getCliente().getNombre();
        String entidad = this.modelo.getPedido().getCliente().getEntidad();
        this.vista.jtfCliente.setText(nombre + " (" + entidad + ")");
        this.vista.jtfDireccionPedido.setText(this.modelo.getPedido().getDireccion());
        this.vista.jtfFuncionario.setText(this.modelo.getPedido().getFuncionario().getAlias());
        this.vista.jtfReferencia.setText(this.modelo.getPedido().getReferencia());
        this.vista.jdcFechaEntrega.setDate(this.modelo.getPedido().getTiempoEntrega());
        this.vista.jtPedidoDetalle.setModel(this.modelo.getRstm());
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(this.modelo.getPedido().getTiempoEntrega());
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        this.vista.jcbHora.setSelectedItem(hora);
        this.vista.jcbMinuto.setSelectedItem(minuto);
        switch (this.modelo.getPedido().getIdCondVenta()) {
            case (1): {
                this.vista.jrbContado.setSelected(true);
                break;
            }
            case (2): {
                this.vista.jrbCredito.setSelected(true);
                break;
            }
        }
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        if (!this.modelo.getPedido().getEstado().equals("Pendiente")) {
            this.vista.jbAceptar.setEnabled(false);
            this.vista.jbCliente.setEnabled(false);
            this.vista.jbSeleccionarProducto.setEnabled(false);
            this.vista.jcbHora.setEnabled(false);
            this.vista.jcbMinuto.setEnabled(false);
            this.vista.jdcFechaEntrega.setEnabled(false);
            this.vista.jtfDireccionPedido.setEnabled(false);
            this.vista.jtfReferencia.setEnabled(false);
            this.vista.jrbContado.setEnabled(false);
            this.vista.jrbCredito.setEnabled(false);
        }
        sumarTotal();
    }

    private void agregarListeners() {
        if (this.modelo.getPedido().getEstado().equals("Pendiente")) {
            this.vista.jtPedidoDetalle.addMouseListener(this);
            this.vista.jrbContado.addActionListener(this);
            this.vista.jrbCredito.addActionListener(this);
            this.vista.jbAceptar.addActionListener(this);
            this.vista.jbSeleccionarProducto.addActionListener(this);
            this.vista.jbCliente.addActionListener(this);
            this.vista.jbEliminarDetalle.addActionListener(this);
            this.vista.jbModificarDetalle.addActionListener(this);
        }
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
    }

    private void establecerCondicionVenta() {
        if (this.vista.jrbContado.isSelected()) {
            this.modelo.getPedido().setIdCondVenta(1);
        } else {
            this.modelo.getPedido().setIdCondVenta(2);
        }
    }

    public void recibirDetalle(M_pedidoDetalle detalle) {
        detalle.setIdPedido(modelo.getPedido().getIdPedido());
        Integer Precio = detalle.getPrecio() - Math.round(Math.round(((detalle.getPrecio() * detalle.getDescuento()) / 100)));
        Integer total = Math.round(Math.round((detalle.getCantidad() * Precio)));
        switch (detalle.getProducto().getImpuesto()) {
            case (0): {//exenta
                detalle.setIva_exenta(total);
                detalle.setIva_cinco(0);
                detalle.setIva_diez(0);
                break;
            }
            case (5): {//iva 5%
                detalle.setIva_exenta(0);
                detalle.setIva_cinco(total);
                detalle.setIva_diez(0);
                break;
            }
            case (10): {//iva10%
                detalle.setIva_exenta(0);
                detalle.setIva_cinco(0);
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
        SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, idProducto, idPedidoDetalle);
        scp.setVisible(true);
    }

    public void modificarDetalle(Double cantidad, Integer precio, Double descuento, String observacion, int idDetalle) {
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

    private void imprimir() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir el pedido?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    Impresora.imprimirPedidoGuardado(DatosUsuario.getRol_usuario(), modelo.getPedido());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbAceptar)) {
            guardarVenta();
        } else if (source.equals(this.vista.jrbContado)) {
            establecerCondicionVenta();
        } else if (source.equals(this.vista.jrbCredito)) {
            establecerCondicionVenta();
        } else if (source.equals(this.vista.jbSeleccionarProducto)) {
            SeleccionarProducto sp = new SeleccionarProducto(this);
            sp.mostrarVista();
        } else if (source.equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this);
            sc.mostrarVista();
        } else if (source.equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        } else if (source.equals(this.vista.jbModificarDetalle)) {
            jbModificarDetalleButtonHandler();
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
}
