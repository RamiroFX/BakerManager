/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.M_menu_item;
import Entities.M_producto;
import Entities.M_proveedor;
import Entities.ProductoCategoria;
import Excel.ExportarProducto;
import Interface.InterfaceNotificarCambio;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.ProductoSimpleTableModel;
import Producto.AjusteStock.GestionAjusteStock;
import Producto.AjusteStock.V_gestionAjusteStock;
import Proveedor.Seleccionar_proveedor;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestion_producto implements ActionListener, KeyListener, MouseListener, InterfaceNotificarCambio {

    private M_producto m_producto;
    private M_proveedor proveedor;
    private ProductoSimpleTableModel productoTM;
    private DecimalFormat decimalFormat;
    public V_gestion_producto vista;
    public C_inicio c_inicio;
    private M_gestionProducto modelo;

    public C_gestion_producto(M_gestionProducto modelo, V_gestion_producto vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.m_producto = new M_producto();
        this.proveedor = new M_proveedor();
        this.productoTM = new ProductoSimpleTableModel();
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.vista = vista;
        this.c_inicio = c_inicio;
        this.vista.setLocation(c_inicio.centrarPantalla(this.vista));
        inicializarVista();
        concederPermisos();
    }

    /**
     * @return the producto
     */
    public M_producto getProducto() {
        return m_producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(M_producto producto) {
        this.m_producto = producto;
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
        this.vista.jcbExistence.addItem("Cero");
        this.vista.jtProducto.setModel(productoTM);
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
        this.vista.jbMasOpciones.addActionListener(this);
        this.vista.jcbCategoria.addActionListener(this);
        this.vista.jbMasOpciones.addKeyListener(this);
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

                //vista.jtProducto.setModel(DB_Producto.consultaSimpleProducto(desc.toLowerCase(), proveedor, marca, categoria.getId(), subCategoria.getId(), impuesto, estado, orderBy, existence));
                productoTM.setList(DB_Producto.consultaSimpleProducto(desc.toLowerCase(), proveedor, marca, categoria.getId(), subCategoria.getId(), impuesto, estado, orderBy, existence));
                Utilities.c_packColumn.packColumns(vista.jtProducto, 1);
            }
        });
    }

    private void completarCampos(KeyEvent e) {
        if (e.getSource().equals(this.vista.jtProducto)) {
            int row = this.vista.jtProducto.getSelectedRow();
            int columna = this.vista.jtProducto.getSelectedRow();
            int idProducto = productoTM.getList().get(row).getId();
            if ((row > -1) && (columna > -1)) {
                MathContext c = new MathContext(9, RoundingMode.CEILING);
                setProducto(DB_Producto.obtenerDatosProductoID(idProducto));
                this.vista.jbModificar.setEnabled(true);
                this.vista.jbEliminar.setEnabled(true);
                this.vista.jtfProducto.setText(getProducto().getDescripcion());
                this.vista.jtfCodigo.setText(String.valueOf(getProducto().getCodBarra()));
                this.vista.jtfPrecioCosto.setText(String.valueOf(getProducto().getPrecioCosto()));
                this.vista.jtfPrecioMayorista.setText(String.valueOf(getProducto().getPrecioMayorista()));
                this.vista.jtfPrecioVta.setText(String.valueOf(getProducto().getPrecioVenta()));
                this.vista.jtfRubro.setText(getProducto().getCategoria());
                this.vista.jtfImpuesto.setText(String.valueOf(getProducto().getImpuesto()));
                this.vista.jtfMarca.setText(getProducto().getMarca());
                this.vista.jtfSuspendido.setText(getProducto().getEstado());
                this.vista.jtfCantActual.setText(decimalFormat.format(new BigDecimal(getProducto().getCantActual(), c)));
                String observacion = "";
                if (getProducto().getObservacion() != null) {
                    observacion = String.valueOf(getProducto().getObservacion());
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
        return this.proveedor.getEntidad();
    }

    public void recibirProveedor(M_proveedor proveedor) {
        this.proveedor = proveedor;
        String nombre = this.proveedor.getNombre();
        String entidad = this.proveedor.getEntidad();
        this.vista.jtfProveedor.setText(nombre + " (" + entidad + ")");
    }

    private void borrarParametros() {
        this.proveedor = new M_proveedor();
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
                ProductoCategoria categoria = vista.jcbCategoria.getItemAt(vista.jcbCategoria.getSelectedIndex());
                ProductoCategoria subCategoria = vista.jcbSubCategoria.getItemAt(vista.jcbSubCategoria.getSelectedIndex());
                String impuesto = vista.jcbImpuesto.getSelectedItem().toString();
                String estado = vista.jcbEstado.getSelectedItem().toString();
                String orderBy = vista.jcbOrderBy.getSelectedItem().toString();
                String existence = vista.jcbExistence.getSelectedItem().toString();
                String proveedor = proveedor();
                ArrayList<M_producto> productos = DB_Producto.consultaSimpleProductos(desc.toLowerCase(), proveedor, marca, categoria.getId(), subCategoria.getId(), impuesto, estado, orderBy, existence);
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

    private void invocarGestionAjusteStock() {
        GestionAjusteStock gaj = new GestionAjusteStock(this.c_inicio.vista);
        gaj.mostrarVista();
    }

    private void masOpciones() {
        Object[] options = {"Ajuste de stock"};
        int n = JOptionPane.showOptionDialog(this.vista,
                "Eliga su opción",
                "Atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        switch (n) {
            case 0: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<M_menu_item> accesos = modelo.getAccesos();
                        for (int i = 0; i < accesos.size(); i++) {
                            if (V_gestionAjusteStock.NOMBRE_MODULO.equals(accesos.get(i).getItemDescripcion())) {
                                invocarGestionAjusteStock();
                                return;
                            }
                        }
                    }
                });
                break;
            }
        }
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
            Modificar_producto c_modProd = new Modificar_producto(this, getProducto().getId());
            c_modProd.mostrarVista();
        } else if (e.getSource() == this.vista.jbAgregar) {
            Crear_producto crearProd = new Crear_producto(this);
            crearProd.mostrarVista();
        } else if (e.getSource().equals(this.vista.jbParametros)) {
            ProductoParametros param = new ProductoParametros(c_inicio);
            param.setCallback(this);
            param.setVisible(true);
        } else if (e.getSource() == this.vista.jbExportar) {
            exportarProductos();
        } else if (e.getSource() == this.vista.jbEliminar) {
            //DBmanagerProducto.eliminarProducto(producto);
            JOptionPane.showMessageDialog(vista, "Funcion no implementada", "Atencion", JOptionPane.INFORMATION_MESSAGE, null);
        } else if (e.getSource() == this.vista.jbAsigProdProv) {
            Crear_producto_proveedor cpp = new Crear_producto_proveedor(this);
            cpp.setVisible(true);
        } else if (e.getSource() == this.vista.jbProveedor) {
            Seleccionar_proveedor sp = new Seleccionar_proveedor(this);
            sp.mostrarVista();
        } else if (e.getSource() == this.vista.jcbCategoria) {
            actualizarJCBsubCategoria();
        } else if (e.getSource() == this.vista.jbMasOpciones) {
            masOpciones();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtProducto.rowAtPoint(e.getPoint());
        int columna = this.vista.jtProducto.columnAtPoint(e.getPoint());
        Integer idProducto = productoTM.getList().get(fila).getId();
        //setProducto(DBmanagerProducto.mostrarProducto(idProducto));
        setProducto(DB_Producto.obtenerDatosProductoID(idProducto));
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbModificar.setEnabled(true);
            this.vista.jbEliminar.setEnabled(true);
            this.vista.jtfProducto.setText(getProducto().getDescripcion());
            this.vista.jtfCodigo.setText(getProducto().getCodigo());
            this.vista.jtfPrecioCosto.setText(String.valueOf(getProducto().getPrecioCosto()));
            this.vista.jtfPrecioMayorista.setText(String.valueOf(getProducto().getPrecioMayorista()));
            this.vista.jtfPrecioVta.setText(String.valueOf(getProducto().getPrecioVenta()));
            this.vista.jtfRubro.setText(getProducto().getCategoria());
            this.vista.jtfImpuesto.setText(String.valueOf(getProducto().getImpuesto()));
            this.vista.jtfMarca.setText(getProducto().getMarca());
            this.vista.jtfSuspendido.setText(getProducto().getEstado());
            MathContext c = new MathContext(9, RoundingMode.CEILING);
            this.vista.jtfCantActual.setText(decimalFormat.format(new BigDecimal(getProducto().getCantActual(), c)));
            String observacion = "";
            if (getProducto().getObservacion() != null) {
                observacion = String.valueOf(getProducto().getObservacion());
            }
            this.vista.jtfObservacion.setText(observacion);
            if (e.getClickCount() == 2) {
                Modificar_producto c_modProd = new Modificar_producto(this, getProducto().getId());
                c_modProd.mostrarVista();
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
                    Crear_producto crearProd = new Crear_producto(this);
                    crearProd.mostrarVista();
                }
                break;
            }
            case KeyEvent.VK_F2: {
                if (vista.jbParametros.isEnabled()) {
                    ProductoParametros param = new ProductoParametros(c_inicio);
                    param.setCallback(this);
                    param.setVisible(true);
                }
                break;
            }
            case KeyEvent.VK_F3: {
                if (vista.jbAsigProdProv.isEnabled()) {
                    Crear_producto_proveedor cpp = new Crear_producto_proveedor(this);
                    cpp.setVisible(true);
                }
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
