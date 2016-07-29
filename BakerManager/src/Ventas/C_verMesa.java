/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Cliente.Seleccionar_cliente;
import Entities.M_cliente;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_mesa_detalle;
import Entities.M_producto;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_verMesa extends MouseAdapter implements ActionListener {

    public M_verMesa modelo;
    public V_crearVentaRapida vista;
    private C_crearVentas crearVentas;

    public C_verMesa(M_verMesa modelo, V_crearVentaRapida vista, C_crearVentas crearVentas) {
        this.modelo = modelo;
        this.vista = vista;
        this.crearVentas = crearVentas;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void inicializarVista() {
        this.vista.setTitle("Mesa " + this.modelo.getMesa().getNumeroMesa());
        String nombre = this.modelo.getMesa().getCliente().getNombre();
        String entidad = this.modelo.getMesa().getCliente().getEntidad();
        M_funcionario f = this.modelo.getMesa().getFuncionario();
        this.vista.jtfCliente.setText(nombre + " (" + entidad + ")");
        this.vista.jtfClieDireccion.setText(this.modelo.getMesa().getCliente().getDireccion());
        this.vista.jtfClieRuc.setText(this.modelo.getMesa().getCliente().getRuc() + "-" + this.modelo.getMesa().getCliente().getRucId());
        this.vista.jtfClieTelefono.setText("");
        this.vista.jtfFuncionario.setText(f.getAlias());
        switch (this.modelo.getMesa().getIdCondVenta()) {
            case (1): {
                //contado
                this.vista.jrbContado.setSelected(true);
                this.vista.jrbCredito.setSelected(false);
                break;
            }
            case (2): {
                //credito
                this.vista.jrbContado.setSelected(false);
                this.vista.jrbCredito.setSelected(true);
                break;
            }
        }
        if (null != this.modelo.getRstm()) {
            this.vista.jtFacturaDetalle.setModel(this.modelo.getRstm());
            Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
            sumarTotal();
        }
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
    }

    private void agregarListeners() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbAgregarProducto.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jtFacturaDetalle.addMouseListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jrbContado.addActionListener(this);
        this.vista.jrbCredito.addActionListener(this);
    }

    public void cerrar() {
        this.vista.dispose();
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
            int row = this.vista.jtFacturaDetalle.getSelectedRow();
            int idProducto = Integer.valueOf(this.vista.jtFacturaDetalle.getValueAt(row, 1).toString());
            int idMesaDetalle = Integer.valueOf(this.vista.jtFacturaDetalle.getValueAt(row, 0).toString());
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, idProducto, idMesaDetalle);
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

    public void recibirDetalle(M_facturaDetalle detalle) {
        M_mesa_detalle mesaDetalle = new M_mesa_detalle();
        Integer impExenta = 0;
        Integer imp5 = 0;
        Integer imp10 = 0;
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
        if (!detalle.getObservacion().isEmpty()) {
            String aux = detalle.getProducto().getDescripcion();
            detalle.getProducto().setDescripcion(aux + "-(" + detalle.getObservacion() + ")");
        }
        //Object[] rowData = {detalle.getProducto().getId(), detalle.getCantidad(), detalle.getProducto().getDescripcion(), detalle.getPrecio(), detalle.getDescuento(), impExenta, imp5, imp10};
        detalle.setExenta(impExenta);
        detalle.setIva5(imp5);
        detalle.setIva10(imp10);
        mesaDetalle.setCantidad(detalle.getCantidad());
        mesaDetalle.setDescuento(detalle.getDescuento());
        mesaDetalle.setExenta(impExenta);
        mesaDetalle.setIva10(imp10);
        mesaDetalle.setIva5(imp5);
        mesaDetalle.setObservacion(detalle.getObservacion());
        mesaDetalle.setPrecio(detalle.getPrecio());
        mesaDetalle.setProducto(detalle.getProducto());
        mesaDetalle.setTotal(total);
        this.modelo.setMesaDetalle(mesaDetalle);
        this.modelo.guardarVentaDetalle();
        this.modelo.borrarMesaDetalle();
        this.modelo.actualizarVentaDetalle();
        this.vista.jtFacturaDetalle.setModel(this.modelo.getRstm());
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
        sumarTotal();
        actualizarMesas();
    }

    private void eliminarDetalle() {
        int response = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar el detalle?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            int row = this.vista.jtFacturaDetalle.getSelectedRow();
            int idMesaDetalle = Integer.valueOf(this.vista.jtFacturaDetalle.getValueAt(row, 0).toString());
            this.modelo.eliminarVenta(idMesaDetalle);
            this.modelo.actualizarVentaDetalle();
            this.vista.jtFacturaDetalle.setModel(this.modelo.getRstm());
            this.vista.jbEliminarDetalle.setEnabled(false);
            this.vista.jbModificarDetalle.setEnabled(false);
            Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
            sumarTotal();
            actualizarMesas();
        }
    }

    public void modificarDetalle(M_producto producto, Double cantidad, Integer precio, Double descuento, String observacion, int idMesaDetalle) {
        Integer impExenta = null;
        Integer imp5 = null;
        Integer imp10 = null;
        Integer Precio = precio - Math.round(Math.round(((precio * descuento) / 100)));
        Integer total = Math.round(Math.round((cantidad * Precio)));
        switch (producto.getImpuesto()) {
            case (0): {
                impExenta = total;
                imp5 = 0;
                imp10 = 0;
                break;
            }
            case (5): {
                impExenta = 0;
                imp5 = total;
                imp10 = 0;
                break;
            }
            case (10): {
                impExenta = 0;
                imp5 = 0;
                imp10 = total;
                break;
            }
        }
        M_mesa_detalle detalle = new M_mesa_detalle();
        detalle.setIdMesaDetalle(idMesaDetalle);
        detalle.setCantidad(cantidad);
        detalle.setDescuento(descuento);
        detalle.setExenta(impExenta);
        detalle.setIva10(imp10);
        detalle.setIva5(imp5);
        detalle.setObservacion(observacion);
        detalle.setPrecio(precio);
        this.modelo.setMesaDetalle(detalle);
        this.modelo.modificarMesaDetalle();
        this.modelo.actualizarVentaDetalle();
        this.modelo.borrarMesaDetalle();
        this.vista.jtFacturaDetalle.setModel(this.modelo.getRstm());
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        sumarTotal();
        actualizarMesas();
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
        this.modelo.getMesa().setCliente(cliente);
        String nombre = this.modelo.getMesa().getCliente().getNombre();
        String entidad = this.modelo.getMesa().getCliente().getEntidad();
        this.vista.jtfCliente.setText(nombre + " (" + entidad + ")");
    }

    private void guardarVenta() {
        if (this.modelo.getRstm().getRowCount() > 0) {
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
        if (this.vista.jrbContado.isSelected()) {
            this.modelo.getMesa().setIdCondVenta(1);
        } else {
            this.modelo.getMesa().setIdCondVenta(2);
        }
    }

    private void actualizarMesas() {
        this.crearVentas.actualizarTablaMesa();
    }
}
