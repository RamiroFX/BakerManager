/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.pamela;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.M_menu_item;
import Entities.M_producto;
import Entities.M_proveedor;
import Entities.ProductoCategoria;
import Excel.ExportarProducto;
import Interface.InterfaceNotificarCambio;
import MenuPrincipal.DatosUsuario;
import Producto.ProductoParametros;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionProducto implements ActionListener, KeyListener, MouseListener, InterfaceNotificarCambio {

    public M_gestionProducto modelo;
    public V_gestionProducto vista;
    public C_inicio c_inicio;

    public C_gestionProducto(M_gestionProducto modelo, V_gestionProducto vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        this.vista.setLocation(c_inicio.centrarPantalla(this.vista));
        inicializarVista();
        concederPermisos();
    }

    private void inicializarVista() {
        Vector marca = DB_manager.obtenerMarca();
        this.vista.jcbMarca.addItem("Todos");
        for (int i = 0; i < marca.size(); i++) {
            this.vista.jcbMarca.addItem(marca.get(i));
        }
        ArrayList<ProductoCategoria> categorias = new ArrayList<>(DB_Producto.obtenerProductoCategoria());
        ProductoCategoria unaCategoria = new ProductoCategoria(-1, "Todas");
        this.vista.jcbCategoria.addItem(unaCategoria);
        for (int i = 0; i < categorias.size(); i++) {
            this.vista.jcbCategoria.addItem(categorias.get(i));
        }
        ArrayList<ProductoCategoria> subCategorias = new ArrayList<>(DB_Producto.obtenerProductoSubCategoria(-1));
        ProductoCategoria unaSubCategoria = new ProductoCategoria(-1, "Todas");
        this.vista.jcbSubCategoria.addItem(unaSubCategoria);
        for (int i = 0; i < subCategorias.size(); i++) {
            this.vista.jcbSubCategoria.addItem(subCategorias.get(i));
        }
        Vector impuesto = DB_manager.obtenerImpuesto();
        this.vista.jcbImpuesto.addItem("Todos");
        for (int i = 0; i < impuesto.size(); i++) {
            this.vista.jcbImpuesto.addItem(impuesto.get(i));
        }
        Vector estado = DB_manager.obtenerEstado();
        this.vista.jcbEstado.addItem("Todos");
        for (int i = 0; i < estado.size(); i++) {
            this.vista.jcbEstado.addItem(estado.get(i));
        }
        this.vista.jcbOrderBy.addItem("Descripción");
        this.vista.jcbOrderBy.addItem("ID");
        this.vista.jcbExistence.addItem("Todas");
        this.vista.jcbExistence.addItem("Positiva");
        this.vista.jcbExistence.addItem("Negativa");
    }

    public void mostrarVista() {
        this.c_inicio.agregarVentana(vista);
    }

    private void cerrar() {
        try {
            this.vista.setClosed(true);
            System.runFinalization();
        } catch (PropertyVetoException ex) {
        }
    }

    private void concederPermisos() {
        this.vista.jtProducto.addMouseListener(this);
        this.vista.jtProducto.addKeyListener(this);
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
            if (this.vista.jbAgregar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbAgregar.setEnabled(true);
                this.vista.jbAgregar.addActionListener(this);
            }
            if (this.vista.jbModificar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbModificar.addActionListener(this);
                this.vista.jbAsigProdProv.addActionListener(this);
                //this.vista.jbModificar.setEnabled(true);
            }
            if (this.vista.jtfBuscar.getName().equals(acceso.getItemDescripcion())) {
                //this.vista.jtfBuscar.addKeyListener(this);
                this.vista.jbBuscar.addActionListener(this);
                //this.vista.jbProveedor.addActionListener(this);
                this.vista.jbBorrar.addActionListener(this);
                this.vista.jtfBuscar.setEditable(true);
                this.vista.jtfBuscar.setEnabled(true);
                this.vista.jbBuscar.setEnabled(true);
            }
            if (this.vista.jbParametros.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbParametros.addActionListener(this);
                this.vista.jbParametros.setEnabled(true);
            }
            if (this.vista.jbExportar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbExportar.addActionListener(this);
                this.vista.jbExportar.setEnabled(true);
            }
            if (this.vista.jbAsigProdProv.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbAsigProdProv.addActionListener(this);
                this.vista.jbAsigProdProv.setEnabled(true);
            }

//            if (this.vista.jbEliminar.getName().equals(acceso.getItemDescripcion())) {
//                this.vista.jbEliminar.addActionListener(this);
//            }
        }
        this.vista.jcbCategoria.addActionListener(this);
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        //this.vista.jbProveedor.addKeyListener(this);
        this.vista.jbAgregar.addKeyListener(this);
        this.vista.jbModificar.addKeyListener(this);
        this.vista.jbParametros.addKeyListener(this);
        this.vista.jbAsigProdProv.addKeyListener(this);
        this.vista.jbExportar.addKeyListener(this);
    }

    public void displayQueryResults() {
        /*
         * Para permitir que los mensajes puedan ser desplegados, no se ejecuta
         * el query directamente, sino que se lo coloca en una cola de eventos
         * para que se ejecute luego de los eventos pendientes.
         */

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String desc = vista.jtfBuscar.getText();
                String marca = vista.jcbMarca.getSelectedItem().toString();
                String rubro = vista.jcbCategoria.getSelectedItem().toString();
                ProductoCategoria categoria = vista.jcbCategoria.getItemAt(vista.jcbCategoria.getSelectedIndex());
                ProductoCategoria subCategoria = vista.jcbSubCategoria.getItemAt(vista.jcbSubCategoria.getSelectedIndex());
                String impuesto = vista.jcbImpuesto.getSelectedItem().toString();
                String estado = vista.jcbEstado.getSelectedItem().toString();
                String orderBy = vista.jcbOrderBy.getSelectedItem().toString();
                String existence = vista.jcbExistence.getSelectedItem().toString();
                String proveedor = proveedor();
                /*
                 * Se utiliza el objeto factory para obtener un TableModel
                 * para los resultados del query.
                 */

                vista.jtProducto.setModel(DB_Producto.consultaSimpleProducto(desc.toLowerCase(), proveedor, marca, categoria.getId(), subCategoria.getId(), impuesto, estado, orderBy, existence));
                Utilities.c_packColumn.packColumns(vista.jtProducto, 1);
            }
        });
    }

    private void completarCampos(KeyEvent e) {
        if (e.getSource().equals(this.vista.jtProducto)) {
            int row = this.vista.jtProducto.getSelectedRow();
            int columna = this.vista.jtProducto.getSelectedRow();
            int idProducto = Integer.valueOf(String.valueOf(this.vista.jtProducto.getValueAt(row, 0)));
            if ((row > -1) && (columna > -1)) {
                modelo.establecerProducto(idProducto);
                this.vista.jbModificar.setEnabled(true);
                this.vista.jbEliminar.setEnabled(true);
                this.vista.jtfProducto.setText(modelo.getProducto().getDescripcion());
                this.vista.jtfCodigo.setText(String.valueOf(modelo.getProducto().getCodBarra()));
                this.vista.jtfPrecioCosto.setText(String.valueOf(modelo.getProducto().getPrecioCosto()));
                this.vista.jtfPrecioMayorista.setText(String.valueOf(modelo.getProducto().getPrecioMayorista()));
                this.vista.jtfPrecioVta.setText(String.valueOf(modelo.getProducto().getPrecioVenta()));
                this.vista.jtfRubro.setText(modelo.getProducto().getCategoria());
                this.vista.jtfImpuesto.setText(String.valueOf(modelo.getProducto().getImpuesto()));
                this.vista.jtfMarca.setText(modelo.getProducto().getMarca());
                this.vista.jtfSuspendido.setText(modelo.getProducto().getEstado());
                this.vista.jtfCantActual.setText(String.valueOf(modelo.getProducto().getCantActual()));
                String observacion = "";
                if (modelo.getProducto().getObservacion() != null) {
                    observacion = String.valueOf(modelo.getProducto().getObservacion());
                }
                this.vista.jtfObservacion.setText(observacion);
            }
        }
    }

    public void actualizarVista() {
        Vector marca = DB_manager.obtenerMarca();
        this.vista.jcbMarca.removeAllItems();
        this.vista.jcbMarca.addItem("Todos");
        for (int i = 0; i < marca.size(); i++) {
            this.vista.jcbMarca.addItem(marca.get(i));
        }

        ArrayList<ProductoCategoria> categorias = new ArrayList<>(DB_Producto.obtenerProductoCategoria());
        this.vista.jcbCategoria.removeAllItems();
        ProductoCategoria unaCategoria = new ProductoCategoria(-1, "Todas");
        this.vista.jcbCategoria.addItem(unaCategoria);
        for (int i = 0; i < categorias.size(); i++) {
            this.vista.jcbCategoria.addItem(categorias.get(i));
        }
        ArrayList<ProductoCategoria> subCategorias = new ArrayList<>(DB_Producto.obtenerProductoSubCategoria(-1));
        this.vista.jcbSubCategoria.removeAllItems();
        ProductoCategoria unaSubCategoria = new ProductoCategoria(-1, "Todas");
        this.vista.jcbSubCategoria.addItem(unaSubCategoria);
        for (int i = 0; i < subCategorias.size(); i++) {
            this.vista.jcbSubCategoria.addItem(subCategorias.get(i));
        }
        Vector impuesto = DB_manager.obtenerImpuesto();
        this.vista.jcbImpuesto.removeAllItems();
        this.vista.jcbImpuesto.addItem("Todos");
        for (int i = 0; i < impuesto.size(); i++) {
            this.vista.jcbImpuesto.addItem(impuesto.get(i));
        }
        Vector estado = DB_manager.obtenerEstado();
        this.vista.jcbEstado.removeAllItems();
        this.vista.jcbEstado.addItem("Todos");
        for (int i = 0; i < estado.size(); i++) {
            this.vista.jcbEstado.addItem(estado.get(i));
        }
    }

    private String proveedor() {
        if (this.vista.jtfProveedor.getText().isEmpty()) {
            return "Todos";
        }
        return this.modelo.getProveedor().getEntidad();
    }

    public void recibirProveedor(M_proveedor proveedor) {
        this.modelo.setProveedor(proveedor);
        String nombre = this.modelo.getProveedor().getNombre();
        String entidad = this.modelo.getProveedor().getEntidad();
        this.vista.jtfProveedor.setText(nombre + " (" + entidad + ")");
    }

    private void borrarParametros() {
        this.modelo.setProveedor(new M_proveedor());
        this.vista.jtfBuscar.setText("");
        this.vista.jtfBuscar.requestFocusInWindow();
        this.vista.jtfProveedor.setText("");
    }

    private void exportarProductos() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String desc = vista.jtfBuscar.getText();
                String marca = vista.jcbMarca.getSelectedItem().toString();
                String rubro = vista.jcbCategoria.getSelectedItem().toString();
                String impuesto = vista.jcbImpuesto.getSelectedItem().toString();
                String estado = vista.jcbEstado.getSelectedItem().toString();
                String orderBy = vista.jcbOrderBy.getSelectedItem().toString();
                String existence = vista.jcbExistence.getSelectedItem().toString();
                String proveedor = proveedor();
                ArrayList<M_producto> productos = DB_Producto.consultaSimpleProductos(desc.toLowerCase(), proveedor, marca, rubro, impuesto, estado, orderBy, existence);
                ExportarProducto ep = new ExportarProducto(productos);
                ep.exportar();
            }
        });
    }

    private void actualizarJCBsubCategoria() {
        int index = this.vista.jcbCategoria.getSelectedIndex();
        if (index < 0) {
            //para prevenir nullpointer exception mientras se actualiza la lista
            return;
        }
        ProductoCategoria pc = this.vista.jcbCategoria.getItemAt(index);
        this.vista.jcbSubCategoria.removeAllItems();
        ProductoCategoria unaSubCategoria = new ProductoCategoria(-1, "Todas");
        this.vista.jcbSubCategoria.addItem(unaSubCategoria);
        for (ProductoCategoria productoCategoria : DB_Producto.obtenerProductoSubCategoria(pc.getId())) {
            this.vista.jcbSubCategoria.addItem(productoCategoria);
        }
    }

    private void crearProducto() {
        CrearProducto crearProd = new CrearProducto(c_inicio.vista);
        crearProd.mostrarVista();
    }

    private void gestionarParametros() {
        ProductoParametros param = new ProductoParametros(c_inicio);
        param.setCallback(this);
        param.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jtfBuscar) {
            //producto= DBmanager.mostrarProducto(jtfBuscar.getText());
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBuscar) {
            //producto= DBmanager.mostrarProducto(jtfBuscar.getText());
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBorrar) {
            borrarParametros();;
        } else if (e.getSource() == this.vista.jbModificar) {
//            Modificar_producto c_modProd = new Modificar_producto(this, getProducto().getId());
//            c_modProd.mostrarVista();
        } else if (e.getSource() == this.vista.jbAgregar) {
            crearProducto();
        } else if (e.getSource().equals(this.vista.jbParametros)) {
            gestionarParametros();
        } else if (e.getSource() == this.vista.jbExportar) {
            exportarProductos();
        } else if (e.getSource() == this.vista.jbEliminar) {
            //DBmanagerProducto.eliminarProducto(producto);
            JOptionPane.showMessageDialog(vista, "Funcion no implementada", "Atencion", JOptionPane.INFORMATION_MESSAGE, null);
        } else if (e.getSource() == this.vista.jcbCategoria) {
            actualizarJCBsubCategoria();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtProducto.rowAtPoint(e.getPoint());
        int columna = this.vista.jtProducto.columnAtPoint(e.getPoint());
        Integer idProducto = Integer.valueOf(String.valueOf(this.vista.jtProducto.getValueAt(fila, 0)));
        //setProducto(DBmanagerProducto.mostrarProducto(idProducto));
        modelo.establecerProducto(idProducto);
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbModificar.setEnabled(true);
            this.vista.jbEliminar.setEnabled(true);
            this.vista.jtfProducto.setText(modelo.getProducto().getDescripcion());
            this.vista.jtfCodigo.setText(modelo.getProducto().getCodigo());
            this.vista.jtfPrecioCosto.setText(String.valueOf(modelo.getProducto().getPrecioCosto()));
            this.vista.jtfPrecioMayorista.setText(String.valueOf(modelo.getProducto().getPrecioMayorista()));
            this.vista.jtfPrecioVta.setText(String.valueOf(modelo.getProducto().getPrecioVenta()));
            this.vista.jtfRubro.setText(modelo.getProducto().getCategoria());
            this.vista.jtfImpuesto.setText(String.valueOf(modelo.getProducto().getImpuesto()));
            this.vista.jtfMarca.setText(modelo.getProducto().getMarca());
            this.vista.jtfSuspendido.setText(modelo.getProducto().getEstado());
            MathContext c = new MathContext(9, RoundingMode.CEILING);
            this.vista.jtfCantActual.setText(new BigDecimal(modelo.getProducto().getCantActual(), c) + "");
            String observacion = "";
            if (modelo.getProducto().getObservacion() != null) {
                observacion = String.valueOf(modelo.getProducto().getObservacion());
            }
            this.vista.jtfObservacion.setText(observacion);
            if (e.getClickCount() == 2) {
//                Modificar_producto c_modProd = new Modificar_producto(this, getProducto().getId());
//                c_modProd.mostrarVista();
            }
        } else {
            this.vista.jbModificar.setEnabled(false);
            this.vista.jbEliminar.setEnabled(false);
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

    public void keyTyped(KeyEvent e) {
        displayQueryResults();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1: {
                if (vista.jbAgregar.isEnabled()) {
                    crearProducto();
                }
                break;
            }
            case KeyEvent.VK_F2: {
                if (vista.jbParametros.isEnabled()) {
                    gestionarParametros();
                }
                break;
            }
            case KeyEvent.VK_F3: {
//                if (vista.jbAsigProdProv.isEnabled()) {
//                    Crear_producto_proveedor cpp = new Crear_producto_proveedor(this);
//                    cpp.setVisible(true);
//                }
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        completarCampos(e);
    }

    @Override
    public void notificarCambio() {
        actualizarVista();
    }

}
