/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import DB.DB_Producto;
import Entities.M_egreso_detalle;
import Entities.M_producto;
import Entities.M_proveedor;
import MenuPrincipal.C_MenuPrincipal;
import Producto.SeleccionarCantidadProduducto;
import Producto.SeleccionarProducto;
import Proveedor.Seleccionar_proveedor;
import Utilities.MyDefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class C_crear_egreso extends MouseAdapter implements ActionListener {

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
        this.vista.jbAgregarProd.addActionListener(this);
        this.vista.jbAgregarProv.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jtProductos.addMouseListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
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
                    this.modelo.egreso_cabecera.setNro_factura(nro_factura);
                } else {
                    this.modelo.egreso_cabecera.setNro_factura(null);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, "El numero de factura debe ser solo numérico", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (this.modelo.existeProveedorNroFactura(this.modelo.proveedor.getId(), nro_factura)) {
                JOptionPane.showMessageDialog(vista, "La compra con el proveedor y el número de factura seleccionado ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int option = JOptionPane.showConfirmDialog(vista, "¿Desea confirmar la compra?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                this.modelo.egreso_cabecera.setId_cabecera(null);
                this.modelo.egreso_cabecera.setId_empleado(1);
                this.modelo.egreso_cabecera.setId_proveedor(this.modelo.proveedor.getId());
                this.modelo.egreso_cabecera.setTiempo(new java.sql.Timestamp(System.currentTimeMillis()));

                int cantFilas = this.dtm.getRowCount();
                if (cantFilas <= 0) {
                    JOptionPane.showMessageDialog(vista, "Seleccione un producto", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                this.modelo.egreso_detalle = new M_egreso_detalle[cantFilas];
                //Col0=ID Col1=Cant. Col2=Desc. Col3=P.U. Col4=Ex Col5=5% Col6=10%
                for (int i = 0; i < cantFilas; i++) {
                    Integer idProducto = Integer.valueOf(String.valueOf(dtm.getValueAt(i, 0)));
                    Double Cantidad = Double.valueOf(String.valueOf(dtm.getValueAt(i, 1)));
                    String Observacion = String.valueOf(dtm.getValueAt(i, 2));

                    //Precio = Precio - Math.round(Math.round(((Precio * descuento) / 100)));
                    Integer Precio = Integer.valueOf(String.valueOf(dtm.getValueAt(i, 3)));
                    Double Descuento = Double.valueOf(String.valueOf(dtm.getValueAt(i, 4)));
                    Integer ivaExenta = Integer.valueOf(String.valueOf(dtm.getValueAt(i, 5)));
                    Integer iva5 = Integer.valueOf(String.valueOf(dtm.getValueAt(i, 6)));
                    Integer iva10 = Integer.valueOf(String.valueOf(dtm.getValueAt(i, 7)));
                    //Precio = Precio - Math.round(Math.round(((Precio * Descuento) / 100)));
                    Integer total = Math.round(Math.round(Cantidad * Precio));
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
                this.vista.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarStock() {
        int option = JOptionPane.showConfirmDialog(vista, "¿Desea agregar estos productos al stock?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
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
                e.printStackTrace();
                return;
            }
        }
    }

    public void recibirProveedor(M_proveedor proveedor) {
        this.modelo.proveedor = proveedor;
        this.vista.jtfProveedor.setText(this.modelo.proveedor.getEntidad() + " | " + proveedor.getRuc() + "-" + proveedor.getRuc_id());
    }

    private void sumarTotal() {
        Integer exenta = 0;
        Integer iva5 = 0;
        Integer iva10 = 0;
        Integer total = 0;
        int cantFilas = this.dtm.getRowCount();
        for (int i = 0; i < cantFilas; i++) {
            exenta = exenta + Integer.valueOf(String.valueOf(dtm.getValueAt(i, 5)));
            iva5 = iva5 + Integer.valueOf(String.valueOf(dtm.getValueAt(i, 6)));
            iva10 = iva10 + Integer.valueOf(String.valueOf(dtm.getValueAt(i, 7)));
        }
        total = exenta + iva5 + iva10;
        this.vista.jftExenta.setValue(exenta);
        this.vista.jftIva5.setValue(iva5);
        this.vista.jftIva10.setValue(iva10);
        this.vista.jftTotal.setValue(total);
    }

    public void recibirProducto(Double cantidad, Integer precio, Double descuento, String observacion, M_producto producto) {
        Integer impExenta = null;
        Integer imp5 = null;
        Integer imp10 = null;
        Integer Precio = precio;
        Precio = Precio - Math.round(Math.round(((Precio * descuento) / 100)));
        Integer total = Math.round(Math.round((cantidad * Precio)));

        if (producto.getImpuesto().equals(0)) {
            impExenta = total;
            imp5 = 0;
            imp10 = 0;
        } else if (producto.getImpuesto().equals(5)) {
            impExenta = 0;
            imp5 = total;
            imp10 = 0;
        } else {
            impExenta = 0;
            imp5 = 0;
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

    public void modificarCelda(Double cantidad, Integer precio, Double descuento, String observacion, int row) {
        Double Cantidad = cantidad;
        Integer Precio = precio;
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
        String producto = this.dtm.getValueAt(row, 2).toString();
        if (null != observacion) {
            if (!observacion.isEmpty()) {
                producto = producto + "-(" + observacion + ")";
            }
        }
        this.dtm.setValueAt(producto, row, 2);
        this.dtm.setValueAt(precio, row, 3);
        this.dtm.setValueAt(descuento, row, 4);
        sumarTotal();
    }

    private void eliminarCompra(int row) {
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        dtm.removeRow(row);
        sumarTotal();
    }

    private void cerrar() {
        this.vista.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.vista.jbModificarDetalle.setEnabled(true);
        this.vista.jbEliminarDetalle.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbAgregarProv)) {
            Seleccionar_proveedor sp = new Seleccionar_proveedor(vista, this);
            sp.mostrarVista();
        } else if (e.getSource().equals(this.vista.jbAgregarProd)) {
            SeleccionarProducto sp = new SeleccionarProducto(vista, this);
            sp.mostrarVista();
        } else if (e.getSource().equals(this.vista.jbAceptar)) {
            insertarEgreso();
            System.runFinalization();
        } else if (e.getSource().equals(this.vista.jbModificarDetalle)) {
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, this.vista.jtProductos.getSelectedRow());
            scp.setVisible(true);
        } else if (e.getSource().equals(this.vista.jbEliminarDetalle)) {
            eliminarCompra(this.vista.jtProductos.getSelectedRow());
        } else if (e.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        }
    }
}
