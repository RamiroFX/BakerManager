/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Cliente.Seleccionar_cliente;
import Configuracion.Timbrado.SeleccionarTimbrado;
import DB.DB_Cliente;
import Entities.E_Timbrado;
import Entities.E_impresionTipo;
import Entities.M_cliente;
import Entities.M_facturaDetalle;
import Entities.M_producto;
import Entities.M_telefono;
import Interface.GestionInterface;
import MenuPrincipal.DatosUsuario;
import Interface.InterfaceFacturaDetalle;
import Parametros.TipoOperacion;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Impresora.Impresora;
import Interface.InterfaceNotificarCambio;
import Interface.RecibirClienteCallback;
import Interface.RecibirTimbradoVentaCallback;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearVentaRapida implements GestionInterface, InterfaceFacturaDetalle, RecibirClienteCallback, InterfaceNotificarCambio, RecibirTimbradoVentaCallback {

    private static final String TITULO_ERROR = "Error";
    private static final String PRODUCTO_NO_EXISTE = "El producto no existe";
    private static final String VENTA_VACIA = "Seleccione por lo menos un artículo.";
    private static final String CONFIRMAR = "Confirmar";
    private static final String ATENCION = "Atención";
    private static final String IMPRIMIR_VENTA = "¿Desea imprimir la venta?";
    private static final String CONFIRMAR_VENTA = "¿Está seguro que desea confirmar la venta?";

    public M_crearVentaRapida modelo;
    public V_crearVentaRapida vista;
    private C_gestionVentas gestionVentas;
    private boolean isJCBTrigger;

    public C_crearVentaRapida(V_crearVentaRapida vista, C_gestionVentas gestionVentas) {
        this.modelo = new M_crearVentaRapida(this);
        this.vista = vista;
        this.gestionVentas = gestionVentas;
        this.isJCBTrigger = true;
        inicializarVista();
        concederPermisos();
    }

    @Override
    public final void inicializarVista() {
        this.vista.jtfNroFactura.setEditable(false);
        //this.vista.jtfNroFactura.setText(this.modelo.getNroFactura() + "");
        this.vista.jtfClieDireccion.setText(this.modelo.getCabecera().getCliente().getDireccion());
        this.vista.jtfCliente.setText(this.modelo.getCabecera().getCliente().getEntidad() + "(" + this.modelo.getCabecera().getCliente().getNombre() + ")");
        try {
            this.vista.jtfClieTelefono.setText(this.modelo.getTelefono().getNumero());
        } catch (Exception e) {
            this.vista.jtfClieTelefono.setText("");
        }
        this.vista.jtfClieRuc.setText(modelo.obtenerRucCliente());
        Vector condCompra = modelo.obtenerTipoOperacion();
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondVenta.addItem(condCompra.get(i));
        }
        ArrayList<E_impresionTipo> tipoVenta = modelo.obtenerTipoVenta();
        for (int i = 0; i < tipoVenta.size(); i++) {
            this.vista.jcbTipoVenta.addItem(tipoVenta.get(i));
        }
        this.vista.jtFacturaDetalle.setModel(this.modelo.getTableModel());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
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
        establecerCondicionVenta();
        establecerTipoVenta();
    }

    @Override
    public final void concederPermisos() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jtfCodProd.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbAgregarProducto.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbNroFactura.addActionListener(this);
        this.vista.jtFacturaDetalle.addMouseListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jcbCondVenta.addActionListener(this);
        this.vista.jcbTipoVenta.addActionListener(this);
        this.vista.jtfCodProd.addKeyListener(this);
        this.vista.jbAgregarProducto.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbImprimir.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jbModificarDetalle.addKeyListener(this);
        this.vista.jbEliminarDetalle.addKeyListener(this);
        this.vista.jtFacturaDetalle.addKeyListener(this);
        this.vista.jcbCondVenta.addKeyListener(this);
        this.vista.jcbTipoVenta.addKeyListener(this);
        this.vista.jbNroFactura.addKeyListener(this);
    }

    @Override
    public final void mostrarVista() {
        this.vista.setVisible(true);
        //this.vista.jtfCodProd.requestFocus();
        /*SwingUtilities.invokeLater(new Runnable() {//if we remove this block it wont work also (no matter when we call requestFocusInWindow)
            @Override
            public void run() {
            }
        });*/
    }

    @Override
    public final void cerrar() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (modelo.getTableModel().getFacturaDetalleList().isEmpty()) {
                    vista.dispose();
                } else {
                    int opcion = JOptionPane.showConfirmDialog(vista, "¿Cancelar venta?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        vista.dispose();
                    }
                }
            }
        });
    }

    private void imprimirTicket() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (modelo.getTableModel().getFacturaDetalleList().isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "No hay productos cargados", "Atención", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir la venta?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        Impresora.imprimirTicketVenta(DatosUsuario.getRol_usuario(), modelo.getCabecera(), (ArrayList<M_facturaDetalle>) modelo.getTableModel().getFacturaDetalleList());
                    }
                }
            }
        });
    }

    public void recibirDetalle(M_facturaDetalle detalle) {
        int rowCount = this.vista.jtFacturaDetalle.getRowCount();
        if (rowCount >= modelo.getMaxProdCant()) {
            if (rowCount % modelo.getMaxProdCant() == 0) {
                float maxProdsAux = modelo.getMaxProdCant();
                int cantVentas = (int) Math.ceil((rowCount + 1) / maxProdsAux);
                javax.swing.JOptionPane.showMessageDialog(this.vista, "La cantidad de productos(" + (rowCount + 1) + ") supera el máximo de "
                        + modelo.getMaxProdCant() + " productos por venta. Se generará otra factura adicional (" + cantVentas + ")\n",
                        "Atención",
                        javax.swing.JOptionPane.OK_OPTION);
            }
            modelo.setVentaMultiple(true);
        } else {
            modelo.setVentaMultiple(false);
        }
        this.modelo.agregarDetalle(detalle);
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
        sumarTotal();
    }

    private void eliminarDetalle() {
        int row = this.vista.jtFacturaDetalle.getSelectedRow();
        this.modelo.getTableModel().quitarDetalle(row);
        //this.vista.jtFacturaDetalle.updateUI();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
        sumarTotal();
    }

    public void modificarDetalle(Double cantidad, Integer precio, Double descuento, String observacion, int row) {
        this.modelo.getTableModel().setValueAt(cantidad, row, 1);
        this.modelo.getTableModel().setValueAt(precio, row, 3);
        this.modelo.getTableModel().setValueAt(descuento, row, 4);
        M_producto prod = this.modelo.getTableModel().getFacturaDetalleList().get(row).getProducto();
        String producto = prod.getDescripcion();
        if (null != observacion) {
            if (!observacion.isEmpty()) {
                producto = producto + "- (" + observacion + ")";
            }
        }
        this.modelo.getTableModel().setValueAt(producto, row, 2);
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
        } else if (prod.getImpuesto().equals(10)) {
            impExenta = 0;
            imp5 = 0;
            imp10 = total;
        }
        this.modelo.getTableModel().setValueAt(impExenta, row, 5);
        this.modelo.getTableModel().setValueAt(imp5, row, 6);
        this.modelo.getTableModel().setValueAt(imp10, row, 7);
        M_facturaDetalle detalle = this.modelo.getTableModel().getFacturaDetalleList().get(row);
        detalle.setCantidad(cantidad);
        detalle.setPrecio(precio);
        detalle.setDescuento(descuento);
        detalle.setObservacion(observacion);
        //this.vista.jtFacturaDetalle.updateUI();
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
        sumarTotal();
    }

    private void sumarTotal() {
        Integer exenta = 0;
        Integer total5 = 0;
        Integer total10 = 0;
        Integer totalIva5 = 0;
        Integer totalIva10 = 0;
        Integer total = 0;
        for (int i = 0; i < this.modelo.getTableModel().getRowCount(); i++) {
            exenta = exenta + Integer.valueOf(String.valueOf(this.modelo.getTableModel().getValueAt(i, 5)));
            total5 = total5 + Integer.valueOf(String.valueOf(this.modelo.getTableModel().getValueAt(i, 6)));
            total10 = total10 + Integer.valueOf(String.valueOf(this.modelo.getTableModel().getValueAt(i, 7)));
        }
        total = exenta + total5 + total10;
        totalIva5 = total5 / 21;
        totalIva10 = total10 / 11;
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
        } else {
            this.vista.jtfClieTelefono.setText("");
        }
    }

    private void guardarVenta() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                modelo.guardarVenta();
                int opcion = JOptionPane.showConfirmDialog(vista, IMPRIMIR_VENTA, ATENCION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    modelo.imprimirVenta();
                }
                modelo.limpiarCampos();
                vista.jtFacturaDetalle.setModel(modelo.getTableModel());
                vista.jcbTipoVenta.setSelectedIndex(0);
                recibirCliente(modelo.getCabecera().getCliente());
                establecerNroFactura();
                establecerCondicionVenta();
                establecerTipoVenta();
                sumarTotal();
                vista.jtfCodProd.setText("");
            }
        });
    }

    private void confirmarVenta() {
        if (modelo.isTableEmpty()) {
            JOptionPane.showConfirmDialog(vista, VENTA_VACIA, ATENCION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            return;
        }
        /*if (!checkearNroFactura()) {
            return;
        }*/
        ConfirmarVenta cv = new ConfirmarVenta(vista);
        cv.inicializarVista(this.vista.jftTotal.getValue() + "");
        cv.setInterface(this);
        cv.mostrarVista();
    }

    private void establecerCondicionVenta() {
        String currentItem = this.vista.jcbCondVenta.getSelectedItem().toString();
        if (currentItem.equals("Contado")) {
            this.modelo.getCabecera().setIdCondVenta(TipoOperacion.CONTADO);
        } else {
            this.modelo.getCabecera().setIdCondVenta(TipoOperacion.CREDITO);
        }
    }

    private void establecerTipoVenta() {
        E_impresionTipo tipoVenta = this.vista.jcbTipoVenta.getItemAt(this.vista.jcbTipoVenta.getSelectedIndex());
        switch (tipoVenta.getDescripcion()) {
            case E_impresionTipo.TICKET_STRING: {
                this.vista.jtfNroFactura.setText("");
                this.vista.jtfNroFactura.setEnabled(false);
                this.modelo.getCabecera().setIdTimbrado(1);
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
                this.modelo.getCabecera().setIdTimbrado(1);
                this.modelo.setTipoVenta(tipoVenta);
                break;
            }
        }
    }

    private void agregarProductoPorCodigo() {
        final C_crearVentaRapida aThis = this;
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
                    scp.mostrarPrecioAdicional();
                    scp.setVisible(true);
                    vista.jtfCodProd.setText("");
                } else {
                    JOptionPane.showMessageDialog(vista, PRODUCTO_NO_EXISTE, TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
/*
    private boolean checkearNroFactura() {
        Integer nroFactura = null;
        if (this.vista.jtfNroFactura.getText().trim().isEmpty()) {
            return true;
        }
        try {
            String cantidad = this.vista.jtfNroFactura.getText().trim();
            nroFactura = Integer.valueOf(cantidad);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Nro. factura.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            //this.vista.jtfNroFactura.setText(modelo.getNroFactura() + "");
            return false;
        }
        if (!modelo.nroFacturaEnUso(nroFactura)) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "El número de factura introducido ya\n"
                    + "se encuentra en uso.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            //this.vista.jtfNroFactura.setText(modelo.getNroFactura() + "");
            return false;
        }
        modelo.getCabecera().setNroFactura(nroFactura);
        return true;
    }*/

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        }
        if (e.getSource().equals(this.vista.jbAceptar)) {
            confirmarVenta();
        }
        if (e.getSource().equals(this.vista.jtfCodProd)) {
            agregarProductoPorCodigo();
        }
        if (e.getSource().equals(this.vista.jbAgregarProducto)) {
            SeleccionarProducto sp = new SeleccionarProducto(this);
            sp.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbImprimir)) {
            imprimirTicket();
        }
        if (e.getSource().equals(this.vista.jbCliente)) {
            Seleccionar_cliente sp = new Seleccionar_cliente(this.gestionVentas.c_inicio.vista);
            sp.setCallback(this);
            sp.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        }
        if (e.getSource().equals(this.vista.jcbCondVenta)) {
            establecerCondicionVenta();
        }
        if (e.getSource().equals(this.vista.jcbTipoVenta)) {
            establecerTipoVenta();
        }
        if (e.getSource().equals(this.vista.jbNroFactura)) {
            SeleccionarTimbrado st = new SeleccionarTimbrado(this.vista, this);
            st.mostrarVista();
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                e.consume();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1: {
                if (modelo.isTableEmpty()) {
                    e.consume();
                    return;
                }
                confirmarVenta();
                break;
            }
            case KeyEvent.VK_F2: {
                imprimirTicket();
                break;
            }
            case KeyEvent.VK_F3: {
                Seleccionar_cliente sp = new Seleccionar_cliente(this.gestionVentas.c_inicio.vista);
                sp.setCallback(this);
                sp.mostrarVista();
                break;
            }
            case KeyEvent.VK_F4: {
                SeleccionarProducto sp = new SeleccionarProducto(this);
                sp.mostrarVista();
                break;
            }
            case KeyEvent.VK_F5: {
                SeleccionarTimbrado st = new SeleccionarTimbrado(this.vista, this);
                st.mostrarVista();
                break;
            }
        }
    }

    @Override
    public void notificarCambioFacturaDetalle() {
        sumarTotal();
    }

    private void establecerNroFactura() {
        this.vista.jtfNroFactura.setText(this.modelo.obtenerUltimoNroFactura() + "");
    }

    @Override
    public void notificarCambio() {
        guardarVenta();
    }

    @Override
    public void recibirTimbrado(E_Timbrado timbrado) {
    }

    @Override
    public void recibirTimbradoNroFactura(E_Timbrado timbrado, int NroFactura) {
        E_impresionTipo tipoVenta = new E_impresionTipo(E_impresionTipo.FACTURA, E_impresionTipo.FACTURA_STRING);
        modelo.getCabecera().setIdTimbrado(timbrado.getId());
        modelo.getCabecera().setNroFactura(NroFactura);
        modelo.setTipoVenta(tipoVenta);
        isJCBTrigger = false;
        this.vista.jcbTipoVenta.setSelectedItem(tipoVenta);
        String nroTimbrado = modelo.getNfLarge().format(timbrado.getNroTimbrado());
        String nroSucursal = modelo.getNfSmall().format(timbrado.getNroSucursal());
        String nroPuntoVenta = modelo.getNfSmall().format(timbrado.getNroPuntoVenta());
        String nroFactura = modelo.getNfLarge().format(NroFactura);
        String nroFacturaCompleto = nroTimbrado + "-" + nroSucursal + "-" + nroPuntoVenta + "-" + nroFactura;
        this.vista.jtfNroFactura.setText(nroFacturaCompleto);
        this.vista.jtfNroFactura.setEnabled(true);
        isJCBTrigger = true;
    }
}
