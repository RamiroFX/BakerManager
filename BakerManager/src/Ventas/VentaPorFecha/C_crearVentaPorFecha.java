/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas.VentaPorFecha;

import Cliente.SeleccionarCliente;
import Configuracion.Timbrado.SeleccionarNroFactura;
import DB.DB_Cliente;
import Empleado.SeleccionarFuncionario;
import Entities.E_Timbrado;
import Entities.E_impresionTipo;
import Entities.E_impuesto;
import Entities.M_cliente;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Entities.M_producto;
import Entities.M_telefono;
import Impresora.Impresora;
import Interface.GestionInterface;
import Interface.InterfaceFacturaDetalle;
import Interface.InterfaceNotificarCambio;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProductoCallback;
import Interface.RecibirTimbradoVentaCallback;
import MenuPrincipal.DatosUsuario;
import Parametros.TipoOperacion;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Ventas.ConfirmarVenta;
import Ventas.M_crearVentaRapida;
import bakermanager.V_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearVentaPorFecha implements GestionInterface, InterfaceFacturaDetalle,
        RecibirClienteCallback, InterfaceNotificarCambio, RecibirTimbradoVentaCallback,
        RecibirProductoCallback, RecibirEmpleadoCallback {

    private static final String TITULO_ERROR = "Error";
    private static final String PRODUCTO_NO_EXISTE = "El producto no existe";
    private static final String VENTA_VACIA = "Seleccione por lo menos un artículo.";
    private static final String CONFIRMAR = "Confirmar";
    public static final String ATENCION = "Atención";
    public static final String IMPRIMIR_VENTA = "¿Desea imprimir la venta?";
    private static final String CONFIRMAR_VENTA = "¿Está seguro que desea confirmar la venta?";

    public M_crearVentaRapida modelo;
    public V_crearVentaPorFecha vista;
    private V_inicio gestionVentas;
    private boolean isJCBTrigger;

    public C_crearVentaPorFecha(M_crearVentaRapida modelo, V_crearVentaPorFecha vista) {
        this.modelo = new M_crearVentaRapida(this);
        this.modelo = modelo;
        this.vista = vista;
        this.isJCBTrigger = true;
        inicializarVista();
        concederPermisos();
    }

    protected void guardarVenta() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Timestamp tiempo = new Timestamp(vista.jdcFecha.getDate().getTime());
                modelo.getCabecera().setTiempo(tiempo);
                modelo.guardarVentaConFecha();
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

    @Override
    public final void inicializarVista() {
        Calendar calendar = Calendar.getInstance();
        vista.jdcFecha.setDate(calendar.getTime());
        this.vista.jtfVendedor.setText(modelo.getCabecera().getVendedor().getAlias());
        this.vista.jtfClieDireccion.setText(this.modelo.getCabecera().getCliente().getDireccion());
        this.vista.jtfCliente.setText(this.modelo.getCabecera().getCliente().getEntidad() + "(" + this.modelo.getCabecera().getCliente().getNombre() + ")");
        try {
            this.vista.jtfClieTelefono.setText(this.modelo.getTelefono().getNumero());
        } catch (Exception e) {
            this.vista.jtfClieTelefono.setText("");
        }
        this.vista.jtfClieRuc.setText(modelo.obtenerRucCliente());
        Vector condVenta = modelo.obtenerTipoOperacion();
        for (int i = 0; i < condVenta.size(); i++) {
            this.vista.jcbCondVenta.addItem(condVenta.get(i));
        }
        ArrayList<E_impresionTipo> tipoVenta = modelo.obtenerTipoVenta();
        for (int i = 0; i < tipoVenta.size(); i++) {
            this.vista.jcbTipoVenta.addItem(tipoVenta.get(i));
        }
        this.vista.jtFacturaDetalle.setModel(this.modelo.getTableModel());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        java.awt.Font fuente = new java.awt.Font("Times New Roman", 0, 18);
        javax.swing.text.DefaultFormatterFactory dff = new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##")));
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
        this.vista.jbVendedor.addActionListener(this);
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
        this.vista.jbVendedor.addKeyListener(this);
        //AGREGAR ACCESOS
        for (M_menu_item acceso : DatosUsuario.getRol_usuario().getAccesos()) {
            if (acceso.getItemDescripcion().equals(vista.jdcFecha.getName())) {
                this.vista.jdcFecha.setEnabled(true);
            }
            if (acceso.getItemDescripcion().equals(vista.jbVendedor.getName())) {
                this.vista.jbVendedor.setEnabled(true);
            }
        }
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

    public void modificarDetalle(Double cantidad, Double precio, Double descuento, String observacion, int row) {
        modelo.modificarDetalle(row, cantidad, descuento, precio, observacion);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
        sumarTotal();
    }

    protected void sumarTotal() {
        double exenta = 0;
        double total5 = 0;
        double total10 = 0;
        double totalIva5 = 0;
        double totalIva10 = 0;
        double total = 0;
        for (M_facturaDetalle fade : modelo.getTableModel().getFacturaDetalleList()) {
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

    protected void establecerCondicionVenta() {
        String currentItem = this.vista.jcbCondVenta.getSelectedItem().toString();
        if (currentItem.equals("Contado")) {
            this.modelo.getCabecera().setIdCondVenta(TipoOperacion.CONTADO);
        } else {
            this.modelo.getCabecera().setIdCondVenta(TipoOperacion.CREDITO);
        }
    }

    protected void establecerTipoVenta() {
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
                    invocarSeleccionNroFactura();
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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                obtenerCodigoProducto();
            }
        });
    }

    private void obtenerCodigoProducto() {
        //OBTENER CODIGO DESDE LA PISTOLA DE COD DE BARRAS
        String codigoProducto = vista.jtfCodProd.getText().trim();
        //VERIFICAR SI EXISTE EL PRODUCTO EN LA BD
        boolean existeProd = modelo.existeProductoPorCodigo(codigoProducto);
        if (existeProd) {
            //SELECCIONAR CANTIDAD DE PRODUCTO
            M_producto unProducto = modelo.obtenerProductoPorCodigo(codigoProducto);
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this.vista, unProducto, this, -1);
            scp.mostrarPrecioAdicional();
            scp.setVisible(true);
            vista.jtfCodProd.setText("");
        } else {
            JOptionPane.showMessageDialog(vista, PRODUCTO_NO_EXISTE, TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void invocarModificarDetalle() {
        int row = this.vista.jtFacturaDetalle.getSelectedRow();
        if (row < 0) {
            return;
        }
        M_facturaDetalle fd = modelo.getTableModel().getFacturaDetalleList().get(row);
        SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this.vista, fd.getProducto(), this, row);
        scp.cargarDatos(fd);
        scp.setVisible(true);
    }

    private void invocarSeleccionNroFactura() {
        E_Timbrado timbradoPred = modelo.obtenerTimbradoPredeterminado();
        SeleccionarNroFactura st = new SeleccionarNroFactura(this.vista, this, timbradoPred);
        st.mostrarVista();
    }

    private void invocarSeleccionVendedor() {
        SeleccionarFuncionario sf = new SeleccionarFuncionario(vista);
        sf.setCallback(this);
        sf.mostrarVista();
    }

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
            SeleccionarProducto sp = new SeleccionarProducto(this.vista, this);
            sp.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbImprimir)) {
            imprimirTicket();
        }
        if (e.getSource().equals(this.vista.jbCliente)) {
            SeleccionarCliente sp = new SeleccionarCliente(this.gestionVentas);
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
            invocarSeleccionNroFactura();
        }
        if (e.getSource().equals(this.vista.jbModificarDetalle)) {
            invocarModificarDetalle();
        }
        if (e.getSource().equals(this.vista.jbVendedor)) {
            invocarSeleccionVendedor();
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
                SeleccionarCliente sp = new SeleccionarCliente(this.gestionVentas);
                sp.setCallback(this);
                sp.mostrarVista();
                break;
            }
            case KeyEvent.VK_F4: {
                SeleccionarProducto sp = new SeleccionarProducto(this.vista, this);
                sp.mostrarVista();
                break;
            }
            case KeyEvent.VK_F5: {
                invocarSeleccionNroFactura();
                break;
            }
            case KeyEvent.VK_F6: {
                if (vista.jbVendedor.isEnabled()) {
                    invocarSeleccionVendedor();
                }
                break;
            }
        }
    }

    @Override
    public void notificarCambioFacturaDetalle() {
        sumarTotal();
    }

    protected void establecerNroFactura() {
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

    @Override
    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        M_facturaDetalle detalle = new M_facturaDetalle();
        detalle.setCantidad(cantidad);
        detalle.setDescuento(descuento);
        detalle.setPrecio(precio);
        detalle.setObservacion(observacion);
        detalle.setProducto(producto);
        detalle.setIdProducto(producto.getId());
        recibirDetalle(detalle);
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        modificarDetalle(cantidad, precio, descuento, observacion, posicion);
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.vista.jtfVendedor.setText(funcionario.getAlias());
        this.modelo.establecerVendedor(funcionario);
    }
}
