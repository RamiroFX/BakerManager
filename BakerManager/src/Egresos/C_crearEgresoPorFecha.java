/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Producto;
import DB.DB_Proveedor;
import Egresos.SeleccionarTimbrado.SeleccionarNroFacturaCompra;
import Entities.E_Timbrado;
import Entities.E_impuesto;
import Entities.E_tipoOperacion;
import Entities.M_egreso_detalle;
import Entities.M_menu_item;
import Entities.M_producto;
import Entities.M_proveedor;
import Entities.M_telefono;
import Interface.RecibirProductoCallback;
import Interface.RecibirProveedorCallback;
import Interface.RecibirTimbradoVentaCallback;
import MenuPrincipal.DatosUsuario;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Proveedor.Seleccionar_proveedor;
import com.nitido.utils.toaster.Toaster;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearEgresoPorFecha extends MouseAdapter implements ActionListener, KeyListener,
        RecibirProductoCallback, RecibirProveedorCallback, RecibirTimbradoVentaCallback {

    private V_crearEgresoPorFecha vista;
    private M_crearEgresoPorFecha modelo;

    public C_crearEgresoPorFecha(M_crearEgresoPorFecha modelo, V_crearEgresoPorFecha vista) {
        this.vista = vista;
        this.modelo = modelo;
        initComp();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void agregarListeners() {
        this.vista.jbAgregarProducto.addActionListener(this);
        this.vista.jbAgregarProv.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jtProductos.addMouseListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jcbTipoCompra.addActionListener(this);
        this.vista.jbNroFactura.addActionListener(this);
        /*
        KEYLISTENERS
         */
        this.vista.jbAgregarProducto.addKeyListener(this);
        this.vista.jbAgregarProv.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jtProductos.addKeyListener(this);
        this.vista.jbModificarDetalle.addKeyListener(this);
        this.vista.jbEliminarDetalle.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        this.vista.jbNroFactura.addKeyListener(this);
        //AGREGAR ACCESOS
        for (M_menu_item acceso : DatosUsuario.getRol_usuario().getAccesos()) {
            if (acceso.getItemDescripcion().equals(vista.jdcFecha.getName())) {
                this.vista.jdcFecha.setEnabled(true);
            }
        }
    }

    private void initComp() {
        this.vista.jdcFecha.setDate(Calendar.getInstance().getTime());
        this.vista.jtfNroFactura.setEnabled(false);
        for (E_tipoOperacion item : modelo.obtenerTipoOperaciones()) {
            this.vista.jcbTipoCompra.addItem(item);
        }
        this.vista.jtProductos.setModel(modelo.getTM());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
    }

    private void JCBTipoOperacionHandler() {
        E_tipoOperacion tipoOperacion = vista.jcbTipoCompra.getItemAt(vista.jcbTipoCompra.getSelectedIndex());
        switch (tipoOperacion.getId()) {
            //CONTADO
            case E_tipoOperacion.CONTADO: {
                this.modelo.getEgresoCabecera().setId_condVenta(1);
                break;
            }
            //CREDITO
            case E_tipoOperacion.CREDITO_30: {
                this.modelo.getEgresoCabecera().setId_condVenta(2);
                break;
            }
        }
    }

    private boolean validarFecha() {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(vista.jdcFecha.getDate());
            this.modelo.getEgresoCabecera().setTiempo(new Timestamp(cal.getTimeInMillis()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Seleccione una fecha válida", "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void insertarEgreso() {
        try {

            if (this.modelo.getEgresoCabecera().getProveedor().getId() == null) {
                JOptionPane.showMessageDialog(vista, "Seleccione un proveedor", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (modelo.validarNroFacturaEnUso()) {
                javax.swing.JOptionPane.showMessageDialog(this.vista, "El número de factura ingresado se encuentra en uso", "Atención",
                        javax.swing.JOptionPane.OK_OPTION);
                return;
            }
            int cantFilas = this.modelo.getTM().getRowCount();
            if (cantFilas <= 0) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!validarFecha()) {
                return;
            }
            JCBTipoOperacionHandler();
            int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar la compra?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option != JOptionPane.YES_OPTION) {
                return;
            }
            modelo.insertarEgreso();
            actualizarStock();
            mostrarMensaje("La compra se registró con éxito.");
            this.vista.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarStock() {
        try {
            ArrayList<Integer> id = new ArrayList();
            ArrayList<Double> cantidad = new ArrayList();
            int cantFilas = this.modelo.getTM().getRowCount();
            for (int i = 0; i < cantFilas; i++) {
                id.add(this.modelo.getTM().getList().get(i).getId_producto());
                cantidad.add(this.modelo.getTM().getList().get(i).getCantidad());
            }
            DB_Producto.sumarStock(id, cantidad);
        } catch (Exception e) {
            JOptionPane.showInternalMessageDialog(vista, "Hubo un problema al agregar los productos al stock", "Atención", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void recibirProveedor(M_proveedor proveedor) {
        this.modelo.getEgresoCabecera().setProveedor(proveedor);
        this.vista.jtfProveedor.setText(this.modelo.getEgresoCabecera().getProveedor().getEntidad() + " ( " + this.modelo.getEgresoCabecera().getProveedor().getNombre() + ")");
        String ruc = "";
        if (proveedor.getRuc() != null) {
            ruc = proveedor.getRuc();
            if (proveedor.getRuc_id() != null) {
                ruc = ruc + "-" + proveedor.getRuc_id();
            }
        }
        String direccion = proveedor.getDireccion();
        ArrayList<M_telefono> telefono = DB_Proveedor.obtenerTelefonos(proveedor.getId());
        this.vista.jtfProvRuc.setText(ruc);
        this.vista.jtfProvDireccion.setText(direccion);
        if (!telefono.isEmpty()) {
            this.vista.jtfProvTelefono.setText(telefono.get(0).getNumero());
        } else {
            this.vista.jtfProvTelefono.setText("");
        }
    }

    private void sumarTotal() {
        double exenta = 0;
        double iva5 = 0;
        double iva10 = 0;
        double total = 0;
        for (M_egreso_detalle unDetalle : modelo.getTM().getList()) {
            switch (unDetalle.getProducto().getIdImpuesto()) {
                case E_impuesto.EXENTA: {
                    exenta = exenta + unDetalle.calcularSubTotal();
                    break;
                }
                case E_impuesto.IVA5: {
                    iva5 = iva5 + unDetalle.calcularSubTotal();
                    break;
                }
                case E_impuesto.IVA10: {
                    iva10 = iva10 + unDetalle.calcularSubTotal();
                    break;
                }
            }
        }
        total = exenta + iva5 + iva10;
        this.vista.jftExenta.setValue(exenta);
        this.vista.jftIva5.setValue(iva5);
        this.vista.jftIva10.setValue(iva10);
        this.vista.jftTotal.setValue(total);
    }

    private void eliminarCompra(int row) {
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.modelo.getTM().quitarDetalle(row);
        sumarTotal();
    }

    private void cerrar() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int cantFilas = modelo.getTM().getRowCount();
                if (cantFilas <= 0) {
                    vista.dispose();
                } else {
                    int opcion = JOptionPane.showConfirmDialog(vista, "¿Cancelar compra?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        vista.dispose();
                    }
                }
            }
        });
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.vista.jbModificarDetalle.setEnabled(true);
        this.vista.jbEliminarDetalle.setEnabled(true);
    }

    private void invocarSeleccionProveedor() {
        Seleccionar_proveedor sp = new Seleccionar_proveedor(vista, this);
        sp.mostrarVista();
    }

    private void invocarSeleccionProducto() {
        SeleccionarProducto sp = new SeleccionarProducto(vista, this);
        sp.mostrarVista();
    }

    private void invocarModificarDetalle() {
        int row = this.vista.jtProductos.getSelectedRow();
        M_egreso_detalle unDetalle = modelo.getTM().getList().get(row);
        M_producto prod = unDetalle.getProducto();
        prod.setPrecioCosto(unDetalle.getPrecio());
        SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(vista, prod, this, SeleccionarCantidadProduducto.PRECIO_COSTO, row);
        scp.cargarDatosCompra(unDetalle);
        scp.setVisible(true);
    }

    private void invocarSeleccionarNroFactura() {
        int idProveedor = modelo.getEgresoCabecera().getProveedor().getId();
        if (idProveedor < 1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un proveedor primero", "Atención", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        SeleccionarNroFacturaCompra snfc = new SeleccionarNroFacturaCompra(vista, idProveedor, this);
        snfc.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbAgregarProv)) {
            invocarSeleccionProveedor();
        } else if (e.getSource().equals(this.vista.jbAgregarProducto)) {
            invocarSeleccionProducto();
        } else if (e.getSource().equals(this.vista.jbAceptar)) {
            insertarEgreso();
            System.runFinalization();
        } else if (e.getSource().equals(this.vista.jbModificarDetalle)) {
            invocarModificarDetalle();
        } else if (e.getSource().equals(this.vista.jbEliminarDetalle)) {
            eliminarCompra(this.vista.jtProductos.getSelectedRow());
        } else if (e.getSource().equals(this.vista.jcbTipoCompra)) {
            JCBTipoOperacionHandler();
        } else if (e.getSource().equals(this.vista.jbNroFactura)) {
            invocarSeleccionarNroFactura();
        } else if (e.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1: {
                insertarEgreso();
                System.runFinalization();
                break;
            }
            case KeyEvent.VK_F3: {
                invocarSeleccionProveedor();
                break;
            }
            case KeyEvent.VK_F4: {
                SeleccionarProducto sp = new SeleccionarProducto(vista, this);
                sp.mostrarVista();
                break;
            }
            case KeyEvent.VK_F5: {
                invocarSeleccionarNroFactura();
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
        M_egreso_detalle fd = new M_egreso_detalle();
        fd.setId_producto(producto.getId());
        fd.setCantidad(cantidad);
        fd.setPrecio(precio);
        fd.setProducto(producto);
        fd.setDescuento(descuento);
        fd.setObservacion(observacion);
        this.modelo.getTM().agregarDetalle(fd);
        sumarTotal();
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        this.modelo.getTM().modificarDetalle(posicion, cantidad, descuento, precio, observacion);
        sumarTotal();
    }

    @Override
    public void recibirTimbrado(E_Timbrado timbrado) {
    }

    @Override
    public void recibirTimbradoNroFactura(E_Timbrado timbrado, int nroFactura) {
        this.modelo.establecerNroFactura(timbrado, nroFactura);
        String nroTimbrado = modelo.getNfLarge().format(timbrado.getNroTimbrado());
        String nroSucursal = modelo.getNfSmall().format(timbrado.getNroSucursal());
        String nroPuntoVenta = modelo.getNfSmall().format(timbrado.getNroPuntoVenta());
        String nroFacturaAux = modelo.getNfLarge().format(nroFactura);
        String nroFacturaCompleto = nroTimbrado + "-" + nroSucursal + "-" + nroPuntoVenta + "-" + nroFacturaAux;
        this.vista.jtfNroFactura.setText(nroFacturaCompleto);
        this.vista.jtfNroFactura.setEnabled(true);
    }

}
