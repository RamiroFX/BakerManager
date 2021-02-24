/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import DB.DB_Producto;
import DB.DB_Proveedor;
import Entities.M_egreso_detalle;
import Entities.M_producto;
import Entities.M_proveedor;
import Entities.M_telefono;
import Interface.RecibirProductoCallback;
import MenuPrincipal.C_MenuPrincipal;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Proveedor.Seleccionar_proveedor;
import Utilities.MyDefaultTableModel;
import com.nitido.utils.toaster.Toaster;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crear_egreso extends MouseAdapter implements ActionListener, KeyListener,
        RecibirProductoCallback {

    public V_crear_egreso vista;
    M_Egresos modelo;
    DefaultTableModel dtm;
    C_MenuPrincipal menuPrincipal;
    TableModelEvent e;

    public C_crear_egreso(V_crear_egreso vista, M_Egresos modelo) {
        this.vista = vista;
        this.modelo = modelo;
        initComp();
        agregarListeners();
    }

    public C_crear_egreso(C_MenuPrincipal menuPrincipal, V_crear_egreso vista, M_Egresos modelo) {
        this.menuPrincipal = menuPrincipal;
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
        this.vista.jrbContado.addKeyListener(this);
        this.vista.jrbCredito.addKeyListener(this);
    }

    private void initComp() {
        dtm = new MyDefaultTableModel();
        dtm.addColumn("ID art.");
        dtm.addColumn("Cantidad");
        dtm.addColumn("Descripción del producto");
        dtm.addColumn("Precio unit.");
        dtm.addColumn("% Desc.");
        dtm.addColumn("Exentas");
        dtm.addColumn("5 %");
        dtm.addColumn("10 %");
        this.vista.jtProductos.setModel(dtm);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jrbContado.setSelected(true);
    }

    private void insertarEgreso() {
        try {

            if (this.modelo.proveedor.getId() == null) {
                JOptionPane.showMessageDialog(vista, "Seleccione un proveedor", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (this.vista.jrbContado.isSelected()) {
                this.modelo.egreso_cabecera.setId_condVenta(1);
            } else {
                this.modelo.egreso_cabecera.setId_condVenta(2);
            }
            Integer nro_factura = null;
            try {
                if (!vista.jtfNroFactura.getText().isEmpty()) {
                    nro_factura = Integer.valueOf(String.valueOf(vista.jtfNroFactura.getText()));
                    this.modelo.egreso_cabecera.setNroFactura(nro_factura);
                } else {
                    this.modelo.egreso_cabecera.setNroFactura(null);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, "El numero de factura debe ser solo numérico", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
//            if (this.modelo.existeProveedorNroFactura(this.modelo.proveedor.getId(), nro_factura)) {
//                JOptionPane.showMessageDialog(vista, "La compra con el proveedor y el número de factura seleccionado ya existe", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
            int cantFilas = this.dtm.getRowCount();
            if (cantFilas <= 0) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar la compra?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                this.modelo.egreso_cabecera.setId_cabecera(null);
                this.modelo.egreso_cabecera.setId_empleado(this.modelo.empleado.getIdFuncionario());
                this.modelo.egreso_cabecera.setId_proveedor(this.modelo.proveedor.getId());
                this.modelo.egreso_cabecera.setTiempo(new java.sql.Timestamp(System.currentTimeMillis()));
                this.modelo.egreso_detalle = new M_egreso_detalle[cantFilas];
                //Col0=ID Col1=Cant. Col2=Desc. Col3=P.U. Col4=Ex Col5=5% Col6=10%
                for (int i = 0; i < cantFilas; i++) {
                    Integer idProducto = Integer.valueOf(String.valueOf(dtm.getValueAt(i, 0)));
                    Double Cantidad = Double.valueOf(String.valueOf(dtm.getValueAt(i, 1)));
                    String Observacion = String.valueOf(dtm.getValueAt(i, 2));

                    //Precio = Precio - Math.round(Math.round(((Precio * descuento) / 100)));
                    Double Precio = Double.valueOf(String.valueOf(dtm.getValueAt(i, 3)));
                    Double Descuento = Double.valueOf(String.valueOf(dtm.getValueAt(i, 4)));
                    Double ivaExenta = Double.valueOf(String.valueOf(dtm.getValueAt(i, 5)));
                    Double iva5 = Double.valueOf(String.valueOf(dtm.getValueAt(i, 6)));
                    Double iva10 = Double.valueOf(String.valueOf(dtm.getValueAt(i, 7)));
                    //Precio = Precio - Math.round(Math.round(((Precio * Descuento) / 100)));
                    Double total = (Cantidad * Precio);
                    this.modelo.egreso_detalle[i] = new M_egreso_detalle();
                    this.modelo.egreso_detalle[i].setId_cabecera(this.modelo.egreso_cabecera.getId_cabecera());
                    this.modelo.egreso_detalle[i].setId_detalle(null);//en db_egreso
                    this.modelo.egreso_detalle[i].setCantidad(Cantidad);
                    this.modelo.egreso_detalle[i].setId_producto(idProducto);
                    this.modelo.egreso_detalle[i].setIva_cinco(iva5);
                    this.modelo.egreso_detalle[i].setIva_diez(iva10);
                    this.modelo.egreso_detalle[i].setIva_exenta(ivaExenta);
                    this.modelo.egreso_detalle[i].setPrecio(Precio);
                    this.modelo.egreso_detalle[i].setDescuento(Descuento);
                    this.modelo.egreso_detalle[i].setTotal(total);
                    if (Observacion.contains("-(")) {
                        int inicio = Observacion.indexOf("-(");
                        Observacion = Observacion.substring(inicio + 2, Observacion.length() - 1);
                        this.modelo.egreso_detalle[i].setObservacion(Observacion);
                    } else {
                        this.modelo.egreso_detalle[i].setObservacion(null);
                    }
                }
                DB_Egreso.insertarEgresoTEMPORAL(this.modelo.egreso_cabecera, this.modelo.egreso_detalle);
                actualizarStock();
                mostrarMensaje("La compra se registró con éxito.");
                this.vista.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarStock() {
        try {
            ArrayList<Integer> id = new ArrayList();
            ArrayList<Double> cantidad = new ArrayList();
            int cantFilas = this.dtm.getRowCount();
            for (int i = 0; i < cantFilas; i++) {
                id.add(this.modelo.egreso_detalle[i].getId_producto());
                cantidad.add(this.modelo.egreso_detalle[i].getCantidad());
            }
            DB_Producto.sumarStock(id, cantidad);
        } catch (Exception e) {
            JOptionPane.showInternalMessageDialog(vista, "Hubo un problema al agregar los productos al stock", "Atención", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void recibirProveedor(M_proveedor proveedor) {
        this.modelo.proveedor = proveedor;
        this.vista.jtfProveedor.setText(this.modelo.proveedor.getEntidad() + " ( " + this.modelo.proveedor.getNombre() + ")");
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
        int cantFilas = this.dtm.getRowCount();
        for (int i = 0; i < cantFilas; i++) {
            exenta = exenta + Double.valueOf(String.valueOf(dtm.getValueAt(i, 5)));
            iva5 = iva5 + Double.valueOf(String.valueOf(dtm.getValueAt(i, 6)));
            iva10 = iva10 + Double.valueOf(String.valueOf(dtm.getValueAt(i, 7)));
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
        dtm.removeRow(row);
        sumarTotal();
    }

    private void cerrar() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int cantFilas = dtm.getRowCount();
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

    private void modificarDetalle() {
        int row = this.vista.jtProductos.getSelectedRow();
        if (row < 0) {
            return;
        }
        M_producto prod = new M_producto();
        prod.setId(Integer.valueOf(String.valueOf(this.vista.jtProductos.getValueAt(row, 0))));
        prod.setPrecioCosto(Double.valueOf(String.valueOf(this.vista.jtProductos.getValueAt(row, 3))));
        SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this.vista, prod, this, row);
        scp.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbAgregarProv)) {
            Seleccionar_proveedor sp = new Seleccionar_proveedor(vista, this);
            sp.mostrarVista();
        } else if (e.getSource().equals(this.vista.jbAgregarProducto)) {
            SeleccionarProducto sp = new SeleccionarProducto(vista, this);
            sp.mostrarVista();
        } else if (e.getSource().equals(this.vista.jbAceptar)) {
            insertarEgreso();
            System.runFinalization();
        } else if (e.getSource().equals(this.vista.jbModificarDetalle)) {
            modificarDetalle();
        } else if (e.getSource().equals(this.vista.jbEliminarDetalle)) {
            eliminarCompra(this.vista.jtProductos.getSelectedRow());
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
                Seleccionar_proveedor sp = new Seleccionar_proveedor(vista, this);
                sp.mostrarVista();
                break;
            }
            case KeyEvent.VK_F4: {
                SeleccionarProducto sp = new SeleccionarProducto(vista, this);
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

    @Override
    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        Double impExenta = null;
        Double imp5 = null;
        Double imp10 = null;
        Double Precio = precio;
        Precio = Precio - Math.round(Math.round(((Precio * descuento) / 100)));
        Double total = cantidad * Precio;

        if (producto.getImpuesto().equals(0)) {
            impExenta = total;
            imp5 = 0.0;
            imp10 = 0.0;
        } else if (producto.getImpuesto().equals(5)) {
            impExenta = 0.0;
            imp5 = total;
            imp10 = 0.0;
        } else {
            impExenta = 0.0;
            imp5 = 0.0;
            imp10 = total;
        }
        if (null != observacion) {
            if (!observacion.isEmpty()) {
                String aux = producto.getDescripcion();
                producto.setDescripcion(aux + "-(" + observacion + ")");
            }
        }
        Object[] rowData = {producto.getId(), cantidad, producto.getDescripcion(), precio, descuento, impExenta, imp5, imp10};
        this.dtm.addRow(rowData);
        this.vista.jtProductos.updateUI();
        sumarTotal();
    }

    @Override
    public void modificarProducto(int row, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        Double Cantidad = cantidad;
        Double Precio = precio;
        Double Descuento = descuento;
        Precio = Precio - Math.round(Math.round(((Precio * Descuento) / 100)));
        Integer total = Math.round(Math.round((Cantidad * Precio)));

        Integer impExenta = null;
        Integer imp5 = null;
        impExenta = Integer.valueOf(String.valueOf(dtm.getValueAt(row, 5)));
        imp5 = Integer.valueOf(String.valueOf(dtm.getValueAt(row, 6)));
        if (impExenta > 0) {
            this.dtm.setValueAt(total, row, 5);
        } else if (imp5 > 0) {
            this.dtm.setValueAt(total, row, 6);
        } else {
            this.dtm.setValueAt(total, row, 7);
        }
        this.dtm.setValueAt(cantidad, row, 1);
        String productoString = this.dtm.getValueAt(row, 2).toString();
        if (null != observacion) {
            if (!observacion.isEmpty()) {
                productoString = productoString + "-(" + observacion + ")";
            }
        }
        this.dtm.setValueAt(productoString, row, 2);
        this.dtm.setValueAt(precio, row, 3);
        this.dtm.setValueAt(descuento, row, 4);
        sumarTotal();
    }
}
