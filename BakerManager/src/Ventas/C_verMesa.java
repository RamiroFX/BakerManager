/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Cliente.Seleccionar_cliente;
import DB.DB_Cliente;
import Entities.E_impresionTipo;
import Entities.M_cliente;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_mesa_detalle;
import Entities.M_producto;
import Entities.M_telefono;
import Interface.RecibirClienteCallback;
import Parametros.TipoOperacion;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
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
public class C_verMesa extends MouseAdapter implements ActionListener, KeyListener, RecibirClienteCallback {

    private static final String TITULO_ERROR = "Error";
    private static final String PRODUCTO_NO_EXISTE = "El producto no existe";

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
        this.vista.jtfNroFactura.setText(f.getAlias());

        Vector condCompra = modelo.obtenerTipoOperacion();
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondVenta.addItem(condCompra.get(i));
        }
        ArrayList<E_impresionTipo> tipoVenta = modelo.obtenerTipoVenta();
        for (int i = 0; i < tipoVenta.size(); i++) {
            this.vista.jcbTipoVenta.addItem(tipoVenta.get(i));
        }
        if (this.modelo.getMesa().getIdCondVenta() == TipoOperacion.CONTADO) {
            this.vista.jcbCondVenta.setSelectedIndex(0);
        } else {
            this.vista.jcbCondVenta.setSelectedIndex(1);
        }
        /*switch (this.modelo.getMesa().getIdCondVenta()) {
            
            case (TipoOperacion.CONTADO): {
                //contado
                this.vista.jrbContado.setSelected(true);
                this.vista.jrbCredito.setSelected(false);
                break;
            }
            case (TipoOperacion.CREDITO): {
                //credito
                this.vista.jrbContado.setSelected(false);
                this.vista.jrbCredito.setSelected(true);
                break;
            }
        }*/
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
        this.vista.jbImprimir.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jtfCodProd.addActionListener(this);
        this.vista.jtFacturaDetalle.addMouseListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jcbCondVenta.addActionListener(this);
        this.vista.jcbTipoVenta.addActionListener(this);
        /*this.vista.jrbContado.addActionListener(this);
        this.vista.jrbCredito.addActionListener(this);*/
        this.vista.jtfCodProd.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbImprimir.addKeyListener(this);
        this.vista.jbAgregarProducto.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    public void cerrar() {
        this.vista.dispose();
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
        if (null != detalle.getObservacion()) {
            if (!detalle.getObservacion().isEmpty()) {
                String aux = detalle.getProducto().getDescripcion();
                detalle.getProducto().setDescripcion(aux + "-(" + detalle.getObservacion() + ")");
            }
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
        detalle.setProducto(producto);
        detalle.setDescuento(descuento);
        detalle.setExenta(impExenta);
        detalle.setIva10(imp10);
        detalle.setIva5(imp5);
        if (observacion != null) {
            detalle.setObservacion(observacion);
        } else {
            detalle.setObservacion("");
        }
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

    @Override
    public void recibirCliente(M_cliente cliente) {
        this.modelo.actualizarDatosMesa(cliente);
        String nombre = this.modelo.getMesa().getCliente().getNombre();
        String entidad = this.modelo.getMesa().getCliente().getEntidad();
        String client = entidad;
        if (this.modelo.getMesa().getCliente().getNombre() != null) {
            client = client + " (" + nombre + ")";
        }
        String ruc = "";
        if (this.modelo.getMesa().getCliente().getRuc() != null) {
            ruc = this.modelo.getMesa().getCliente().getRuc();
            if (this.modelo.getMesa().getCliente().getRucId() != null) {
                ruc = ruc + "-" + this.modelo.getMesa().getCliente().getRucId();
            }
        }
        String direccion = this.modelo.getMesa().getCliente().getDireccion();
        ArrayList<M_telefono> telefono = DB_Cliente.obtenerTelefonoCliente(this.modelo.getMesa().getCliente().getIdCliente());
        this.vista.jtfCliente.setText(client);
        this.vista.jtfClieRuc.setText(ruc);
        this.vista.jtfClieDireccion.setText(direccion);
        if (!telefono.isEmpty()) {
            this.vista.jtfClieTelefono.setText(telefono.get(0).getNumero());
        } else {
            this.vista.jtfClieTelefono.setText("");
        }
        crearVentas.actualizarTablaMesa();
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
        String currentItem = this.vista.jcbCondVenta.getSelectedItem().toString();
        if (currentItem.equals("Contado")) {
            this.modelo.getMesa().setIdCondVenta(TipoOperacion.CONTADO);
        } else {
            this.modelo.getMesa().setIdCondVenta(TipoOperacion.CREDITO);
        }
        /*
        if (this.vista.jrbContado.isSelected()) {
            this.modelo.getMesa().setIdCondVenta(TipoOperacion.CONTADO);
        } else {
            this.modelo.getMesa().setIdCondVenta(TipoOperacion.CREDITO);
        }*/
    }

    private void actualizarMesas() {
        this.crearVentas.actualizarTablaMesa();
    }

    private void agregarProductoPorCodigo() {
        final C_verMesa aThis = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //OBTENER CODIGO DESDE LA PISTOLA DE COD DE BARRAS
                String codigoProducto = vista.jtfCodProd.getText().trim();
                //VERIFICAR SI EXISTE EL PRODUCTO EN LA BD
                boolean existeProd = modelo.existeProductoPorCodigo(codigoProducto);
                if (existeProd) {
                    //SELECCIONAR CANTIDAD DE PRODUCTO
                    M_producto unProducto = modelo.obtenerProductoPorCodigo(codigoProducto);
                    SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(aThis, unProducto);
                    scp.setVisible(true);
                    vista.jtfCodProd.setText("");
                } else {
                    JOptionPane.showMessageDialog(vista, PRODUCTO_NO_EXISTE, TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void imprimirTicket() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (modelo.getRstm().getRowCount() < 1) {
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
            //Seleccionar_cliente sp = new Seleccionar_cliente(this);
            Seleccionar_cliente sp = new Seleccionar_cliente(this.crearVentas.gestionVentas.c_inicio.vista);
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
        /*if (e.getSource().equals(this.vista.jrbContado)) {
            establecerCondicionVenta();
        }
        if (e.getSource().equals(this.vista.jrbCredito)) {
            establecerCondicionVenta();
        }*/
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
            Seleccionar_cliente sp = new Seleccionar_cliente(this.crearVentas.gestionVentas.c_inicio.vista);
            sp.setCallback(this);
            sp.mostrarVista();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
