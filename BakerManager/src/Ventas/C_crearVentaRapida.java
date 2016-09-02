/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Cliente.Seleccionar_cliente;
import DB.DB_Cliente;
import Entities.M_cliente;
import Entities.M_facturaDetalle;
import Entities.M_producto;
import Entities.M_telefono;
import Interface.Gestion;
import MenuPrincipal.DatosUsuario;
import Parametros.TipoOperacion;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Utilities.Impresora;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearVentaRapida implements Gestion {

    public M_crearVentaRapida modelo;
    public V_crearVentaRapida vista;
    private C0_gestionVentas gestionVentas;

    public C_crearVentaRapida(M_crearVentaRapida modelo, V_crearVentaRapida vista, C0_gestionVentas gestionVentas) {
        this.modelo = modelo;
        this.vista = vista;
        this.gestionVentas = gestionVentas;
        inicializarVista();
        concederPermisos();
    }

    @Override
    public final void inicializarVista() {
        this.modelo.getCabecera().setFuncionario(this.gestionVentas.c_inicio.modelo.getRol_usuario().getFuncionario());
        this.vista.jtfFuncionario.setText(this.modelo.getCabecera().getFuncionario().getAlias());
        this.vista.jtfClieDireccion.setText(this.modelo.getCabecera().getCliente().getDireccion());
        this.vista.jtfCliente.setText(this.modelo.getCabecera().getCliente().getEntidad() + "(" + this.modelo.getCabecera().getCliente().getNombre() + ")");
        try {
            this.vista.jtfClieTelefono.setText(this.modelo.getTelefono().getNumero());
        } catch (Exception e) {
            this.vista.jtfClieTelefono.setText("");
        }
        this.vista.jtfClieRuc.setText(this.modelo.getCabecera().getCliente().getRuc() + "-" + this.modelo.getCabecera().getCliente().getRucId());
        this.vista.jrbContado.setSelected(true);
        this.vista.jtFacturaDetalle.setModel(this.modelo.getDtm());
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
        establecerCondicionVenta();
    }

    @Override
    public final void concederPermisos() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbAgregarProducto.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jtFacturaDetalle.addMouseListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jrbContado.addActionListener(this);
        this.vista.jrbCredito.addActionListener(this);
        this.vista.jbAgregarProducto.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbImprimir.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jbModificarDetalle.addKeyListener(this);
        this.vista.jbEliminarDetalle.addKeyListener(this);
    }

    @Override
    public final void mostrarVista() {
        this.vista.setVisible(true);
    }

    @Override
    public final void cerrar() {
        this.vista.dispose();
    }

    private void imprimirTicket() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (modelo.getDetalles().isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "No hay productos cargados", "Atención", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir el ticket?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        Impresora.imprimirVenta(DatosUsuario.getRol_usuario(), modelo.getCabecera(), modelo.getDetalles());
                    }
                }
            }
        });
    }

    public void recibirDetalle(M_facturaDetalle detalle) {
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
            if (!detalle.getObservacion().isEmpty()) {
                String aux = detalle.getProducto().getDescripcion();
                detalle.getProducto().setDescripcion(aux + "-(" + detalle.getObservacion() + ")");
            }
        }
        Object[] rowData = {detalle.getProducto().getId(), detalle.getCantidad(), detalle.getProducto().getDescripcion(), detalle.getPrecio(), detalle.getDescuento(), impExenta, imp5, imp10};
        detalle.setExenta(impExenta);
        detalle.setIva5(imp5);
        detalle.setIva10(imp10);
        this.modelo.getDetalles().add(detalle);
        this.modelo.getDtm().addRow(rowData);
        this.vista.jtFacturaDetalle.updateUI();
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
        sumarTotal();
    }

    private void eliminarDetalle() {
        int row = this.vista.jtFacturaDetalle.getSelectedRow();
        this.modelo.getDetalles().remove(row);
        this.modelo.getDtm().removeRow(row);
        this.vista.jtFacturaDetalle.updateUI();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        sumarTotal();
    }

    public void modificarDetalle(Double cantidad, Integer precio, Double descuento, String observacion, int row) {
        this.modelo.getDtm().setValueAt(cantidad, row, 1);
        this.modelo.getDtm().setValueAt(precio, row, 3);
        this.modelo.getDtm().setValueAt(descuento, row, 4);
        M_producto prod = this.modelo.getDetalles().get(row).getProducto();
        String producto = prod.getDescripcion();
        if (null != observacion) {
            if (!observacion.isEmpty()) {
                producto = producto + "- (" + observacion + ")";
            }
        }
        this.modelo.getDtm().setValueAt(producto, row, 2);
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
        M_facturaDetalle detalle = this.modelo.getDetalles().get(row);
        detalle.setCantidad(cantidad);
        detalle.setPrecio(precio);
        detalle.setDescuento(descuento);
        detalle.setObservacion(observacion);
        this.vista.jtFacturaDetalle.updateUI();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        sumarTotal();
    }

    private void sumarTotal() {
        Integer exenta = 0;
        Integer iva5 = 0;
        Integer iva10 = 0;
        Integer total = 0;
        for (int i = 0; i < this.modelo.getDtm().getRowCount(); i++) {
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
        this.modelo.getCabecera().setCliente(cliente);
        String nombre = this.modelo.getCabecera().getCliente().getNombre();
        String entidad = this.modelo.getCabecera().getCliente().getEntidad();
        String client = entidad;
        if (this.modelo.getCabecera().getCliente().getNombre() != null) {
            client = client + " (" + nombre + ")";
        }
        String ruc = "";
        if (this.modelo.getCabecera().getCliente().getRuc() != null) {
            ruc = this.modelo.getCabecera().getCliente().getRuc();
            if (this.modelo.getCabecera().getCliente().getRucId() != null) {
                ruc = ruc + "-" + this.modelo.getCabecera().getCliente().getRucId();
            }
        }
        String direccion = this.modelo.getCabecera().getCliente().getDireccion();
        ArrayList<M_telefono> telefono = DB_Cliente.obtenerTelefonoCliente(this.modelo.getCabecera().getIdCliente());
        this.vista.jtfCliente.setText(client);
        this.vista.jtfClieRuc.setText(ruc);
        this.vista.jtfClieDireccion.setText(direccion);
        if (!telefono.isEmpty()) {
            this.vista.jtfClieTelefono.setText(telefono.get(0).getNumero());
        }
    }

    private void guardarVenta() {
        if (this.modelo.guardarVenta()) {
            cerrar();
        }
    }

    private void establecerCondicionVenta() {
        if (this.vista.jrbContado.isSelected()) {
            this.modelo.getCabecera().setIdCondVenta(TipoOperacion.CONTADO);
        } else {
            this.modelo.getCabecera().setIdCondVenta(TipoOperacion.CREDITO);
        }
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
        if (e.getSource().equals(this.vista.jbImprimir)) {
            imprimirTicket();
        }
        if (e.getSource().equals(this.vista.jbCliente)) {
            Seleccionar_cliente sp = new Seleccionar_cliente(this);
            sp.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        }
        if (e.getSource().equals(this.vista.jrbContado)) {
            establecerCondicionVenta();
        }
        if (e.getSource().equals(this.vista.jrbCredito)) {
            establecerCondicionVenta();
        }
        if (e.getSource().equals(this.vista.jbModificarDetalle)) {
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, this.vista.jtFacturaDetalle.getSelectedRow());
            scp.setVisible(true);
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
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
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
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            Seleccionar_cliente sp = new Seleccionar_cliente(this);
            sp.mostrarVista();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
