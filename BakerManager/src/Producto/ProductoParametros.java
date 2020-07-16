/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB.DB_Producto;
import DB.DB_Proveedor;
import DB.DB_manager;
import Entities.ProductoCategoria;
import ModeloTabla.ProductoCategoriaTableModel;
import ModeloTabla.ProductoSubCategoriaTableModel;
import bakermanager.C_inicio;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProductoParametros extends javax.swing.JDialog implements ActionListener, MouseListener, KeyListener {

    private static final int MAX_LENGTH = 50, CREAR_SUB_CATEGORIA = 1, MODIFICAR_SUB_CATEGORIA = 2;

    private javax.swing.JButton jbCrear, jbModificar, jbEliminar;
    private JTabbedPane jtpCenter;
    private JPanel jpSouth, jpSubCategorias;
    JScrollPane jspMarcas, jspCategorias, jspSubCategorias;
    JTable jtMarcas, jtCategorias, jtSubCategorias;
    private JComboBox<ProductoCategoria> jcbCategorias;
    C_gestion_producto gestion_producto;
    ProductoCategoriaTableModel productoCategoriaTm;
    ProductoSubCategoriaTableModel productoSubCategoriaTm;

    public ProductoParametros(C_inicio c_inicio) {
        super(c_inicio.vista, true);
        construirLayout(c_inicio);
        initializarLogica();
        initComponents();
        inicializarVista();
        agregarListener();
    }

    ProductoParametros(C_inicio c_inicio, C_gestion_producto aThis) {
        super(c_inicio.vista, true);
        gestion_producto = aThis;
        construirLayout(c_inicio);
        initComponents();
        initializarLogica();
        inicializarVista();
        agregarListener();
    }

    private void construirLayout(C_inicio c_inicio) {
        setTitle("Parametros");
        setSize(new java.awt.Dimension(500, 300));
        setLocationRelativeTo(c_inicio.vista);
    }

    private void initializarLogica() {
        this.productoCategoriaTm = new ProductoCategoriaTableModel();
        this.productoSubCategoriaTm = new ProductoSubCategoriaTableModel();
        this.productoCategoriaTm.setList(DB_Producto.obtenerProductoCategoria());
        this.productoSubCategoriaTm.setList(DB_Producto.obtenerProductoSubCategoria());
        actualizarComboBox();
    }

    private void inicializarVista() {
        jbEliminar.setEnabled(false);
        jbModificar.setEnabled(false);
        jtMarcas.setModel(DB_manager.consultarMarca());
        jtCategorias.setModel(productoCategoriaTm);
        jtSubCategorias.setModel(productoSubCategoriaTm);
        Utilities.c_packColumn.packColumns(jtMarcas, 1);
        Utilities.c_packColumn.packColumns(jtCategorias, 1);
        Utilities.c_packColumn.packColumns(jtSubCategorias, 1);
    }

    private void initMarcas() {
        jtMarcas = new JTable();
        jtMarcas.getTableHeader().setReorderingAllowed(false);
        jspMarcas = new JScrollPane(jtMarcas);
    }

    private void initCategorias() {
        jtCategorias = new JTable();
        jtCategorias.getTableHeader().setReorderingAllowed(false);
        jtSubCategorias = new JTable();
        jtSubCategorias.getTableHeader().setReorderingAllowed(false);
        jspCategorias = new JScrollPane(jtCategorias);
        jspSubCategorias = new JScrollPane(jtSubCategorias);
        jcbCategorias = new JComboBox();
    }

    private void initComponents() {
        initMarcas();
        initCategorias();
        jpSubCategorias = new JPanel(new BorderLayout());
        JPanel jpSubCategorias2 = new JPanel();
        jpSubCategorias2.add(new JLabel("Categoria"));
        jpSubCategorias2.add(jcbCategorias);
        jpSubCategorias.add(jpSubCategorias2, BorderLayout.NORTH);
        jpSubCategorias.add(jspSubCategorias, BorderLayout.CENTER);
        jtpCenter = new JTabbedPane();
        jtpCenter.add("Marcas", jspMarcas);
        jtpCenter.add("Categorias", jspCategorias);
        jtpCenter.add("Sub Categorias", jpSubCategorias);
        jpSouth = new JPanel();
        jbCrear = new javax.swing.JButton("Agregar");
        jbModificar = new javax.swing.JButton("Modificar");
        jbEliminar = new javax.swing.JButton("Eliminar");
        jpSouth.add(jbCrear);
        jpSouth.add(jbModificar);
        jpSouth.add(jbEliminar);
        getContentPane().setLayout(new MigLayout());
        getContentPane().add(jtpCenter, "dock center");
        getContentPane().add(jpSouth, "dock south");
    }

    private void agregarListener() {
        jtpCenter.addMouseListener(this);
        jtCategorias.addMouseListener(this);
        jcbCategorias.addActionListener(this);
        jtSubCategorias.addMouseListener(this);
        jtMarcas.addMouseListener(this);
        jbCrear.addActionListener(this);
        jbModificar.addActionListener(this);
        jbEliminar.addActionListener(this);
        /*
        KEYLISTENERS
         */
        jtpCenter.addKeyListener(this);
        jtCategorias.addKeyListener(this);
        jtSubCategorias.addKeyListener(this);
        jtMarcas.addKeyListener(this);
        jbCrear.addKeyListener(this);
        jbModificar.addKeyListener(this);
        jbEliminar.addKeyListener(this);
    }

    private void agregarMarca(String marca) {
        String m = marca.trim();
        if (m.length() < 1 || m.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (m.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this, "Máximo permitido " + MAX_LENGTH + " caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdMarca(m);
        if (b == null) {
            DB_Proveedor.insertarMarca(m);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtMarcas.setModel(DB_manager.consultarMarca());
        } else {
            JOptionPane.showMessageDialog(this, "Marca existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarMarca(String marca) {
        String m = marca.trim();
        if (m.length() < 1 || m.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (m.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this, "Máximo permitido " + MAX_LENGTH + " caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdMarca(m);
        if (b == null) {
            int idMarca = Integer.valueOf(String.valueOf(this.jtMarcas.getValueAt(jtMarcas.getSelectedRow(), 0)));
            DB_Proveedor.modificarMarca(idMarca, marca);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtMarcas.setModel(DB_manager.consultarMarca());
        } else {
            JOptionPane.showMessageDialog(this, "Marca existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarMarca() {
        int idMarca = Integer.valueOf(String.valueOf(this.jtMarcas.getValueAt(jtMarcas.getSelectedRow(), 0)));
        boolean m = DB_manager.marcaEnUso(idMarca);
        if (m) {
            int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    DB_Proveedor.eliminarMarca(idMarca);
                    this.jbModificar.setEnabled(false);
                    this.jbEliminar.setEnabled(false);
                    this.jtMarcas.setModel(DB_manager.consultarMarca());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Existe productos que se encuentran utilizando la marca seleccionada.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarCategoria(String categoria) {
        String c = categoria.trim();
        if (c.length() < 1 || c.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (c.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this, "Máximo permitido " + MAX_LENGTH + " caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdProductoCategoria(c);
        if (b == null) {
            DB_Proveedor.insertarCategoria(c);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.productoCategoriaTm.setList(DB_Producto.obtenerProductoCategoria());
            actualizarComboBox();
        } else {
            JOptionPane.showMessageDialog(this, "Categoría existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarCategoria(String categoria) {
        String c = categoria.trim();
        if (c.length() < 1 || c.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (c.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this, "Máximo permitido " + MAX_LENGTH + " caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdProductoCategoria(c);
        if (b == null) {
            if (jtCategorias.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione nuevamente la Categoría a modificar.", "Alerta", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idCategoria = productoCategoriaTm.getList().get(jtCategorias.getSelectedRow()).getId();
            DB_Proveedor.modificarCategoria(idCategoria, categoria);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.productoCategoriaTm.setList(DB_Producto.obtenerProductoCategoria());
            actualizarComboBox();
        } else {
            JOptionPane.showMessageDialog(this, "Categoría existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCategoria() {
        System.out.println("Producto.ProductoParametros.eliminarCategoria()");
        if (jtCategorias.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione nuevamente la Categoría a eliminar.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idCategoria = productoCategoriaTm.getList().get(jtCategorias.getSelectedRow()).getId();
        boolean m = DB_manager.productCategoriaEnUso(idCategoria);
        boolean n = DB_manager.productoSubCategoriaEnUso(idCategoria);
        if (m == true && n == false) {
            int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    DB_Proveedor.eliminarProductoCategoria(idCategoria);
                    this.jbModificar.setEnabled(false);
                    this.jbEliminar.setEnabled(false);
                    this.productoCategoriaTm.setList(DB_Producto.obtenerProductoCategoria());
                    actualizarComboBox();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Ocurrio un error al eliminar la categoría.", "Alerta", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Existe productos o sub-categorías que se encuentran utilizando la categoría seleccionada.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarSubCategoria(String subCategoria, ProductoCategoria padre) {
        String c = subCategoria.trim();
        if (c.length() < 1 || c.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (c.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 30 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean b = DB_manager.existeSubCategoria(c, padre.getId());
        if (!b) {
            DB_manager.insertarSubCategoria(c, padre);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.productoSubCategoriaTm.setList(DB_Producto.obtenerProductoSubCategoria(padre.getId()));
        } else {
            JOptionPane.showMessageDialog(this, "Sub Categoría existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarSubCategoria(String categoria, ProductoCategoria padre) {
        String c = categoria.trim();
        if (c.length() < 1 || c.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (c.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this, "Máximo permitido " + MAX_LENGTH + " caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (jtSubCategorias.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione nuevamente la Sub Categoría a modificar.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean b = DB_manager.existeSubCategoria(c, padre.getId());
        int idSubCategoria = productoSubCategoriaTm.getList().get(jtSubCategorias.getSelectedRow()).getId();
        if (!b) {
            System.out.println("La sub categoria no existe");
            DB_Proveedor.modificarCategoria(idSubCategoria, c);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.productoSubCategoriaTm.setList(DB_Producto.obtenerProductoSubCategoria(padre.getId()));
        } else {
            System.out.println("La sub categoria existe");
            String subCategoria = productoSubCategoriaTm.getList().get(jtSubCategorias.getSelectedRow()).getDescripcion();
            if (c.toLowerCase().equals(subCategoria.toLowerCase())) {
                System.out.println("ES LA MISMA SUB CATEGORIA.");
                DB_Proveedor.modificarCategoria(idSubCategoria, c);
                this.jbModificar.setEnabled(false);
                this.jbEliminar.setEnabled(false);
                this.productoSubCategoriaTm.setList(DB_Producto.obtenerProductoSubCategoria(padre.getId()));
            } else {
                JOptionPane.showMessageDialog(this, "Sub Categoría existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarSubCategoria() {
        if (jtSubCategorias.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione nuevamente la Sub Categoría a eliminar.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idCategoria = productoSubCategoriaTm.getList().get(jtSubCategorias.getSelectedRow()).getId();
        boolean m = DB_manager.productCategoriaEnUso(idCategoria);
        if (m) {
            int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    int index = jcbCategorias.getSelectedIndex();
                    ProductoCategoria pc = jcbCategorias.getItemAt(index);
                    DB_Proveedor.eliminarProductoCategoria(idCategoria);
                    this.jbModificar.setEnabled(false);
                    this.jbEliminar.setEnabled(false);
                    this.productoSubCategoriaTm.setList(DB_Producto.obtenerProductoSubCategoria(pc.getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Existe productos que se encuentran utilizando la categoría seleccionada.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspMarcas)) {
            String marca = JOptionPane.showInputDialog(this, "Inserte el nombre de la marca", "Insertar marca", JOptionPane.PLAIN_MESSAGE);
            if (marca != null) {
                if (!marca.isEmpty()) {
                    agregarMarca(marca);
                }
            }
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspCategorias)) {
            String rubro = JOptionPane.showInputDialog(this, "Inserte el nombre del rubro", "Insertar categoria", JOptionPane.PLAIN_MESSAGE);
            if (rubro != null) {
                if (!rubro.isEmpty()) {
                    agregarCategoria(rubro);
                }
            }
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jpSubCategorias)) {
            mostrarDialogoCreacionSubCategoria(CREAR_SUB_CATEGORIA, null);
        }
        gestion_producto.actualizarVista();
    }

    private void updateButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspMarcas)) {
            String marca = JOptionPane.showInputDialog(this, "Inserte el nombre de la marca", "Modificar marca", JOptionPane.PLAIN_MESSAGE);
            if (marca != null) {
                if (!marca.isEmpty()) {
                    modificarMarca(marca);
                }
            }
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspCategorias)) {
            String rubro = JOptionPane.showInputDialog(this, "Inserte el nombre del rubro", "Modificar categoria", JOptionPane.PLAIN_MESSAGE);
            if (rubro != null) {
                if (!rubro.isEmpty()) {
                    modificarCategoria(rubro);
                }
            }
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jpSubCategorias)) {
            int fila = jtSubCategorias.getSelectedRow();
            if (fila < 0) {
                return;
            }
            ProductoCategoria prodCat = productoSubCategoriaTm.getList().get(fila);
            mostrarDialogoCreacionSubCategoria(MODIFICAR_SUB_CATEGORIA, prodCat);
        }
        gestion_producto.actualizarVista();
    }

    private void deleteButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspMarcas)) {
            eliminarMarca();
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspCategorias)) {
            eliminarCategoria();
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jpSubCategorias)) {
            eliminarSubCategoria();
        }
        gestion_producto.actualizarVista();
    }

    private void cerrar() {
        System.runFinalization();
        this.dispose();
    }

    private void actualizarComboBox() {
        ProductoCategoria categoria = new ProductoCategoria();
        categoria.setId(-1);
        categoria.setDescripcion("Todos");
        jcbCategorias.removeAllItems();
        jcbCategorias.addItem(categoria);
        for (ProductoCategoria productoCategoria : productoCategoriaTm.getList()) {
            jcbCategorias.addItem(productoCategoria);
        }
    }

    private void jcbCategoriasHandler() {
        int index = jcbCategorias.getSelectedIndex();
        if (index < 0) {
            return;
        }
        ProductoCategoria pc = jcbCategorias.getItemAt(index);
        this.productoSubCategoriaTm.setList(DB_Producto.obtenerProductoSubCategoria(pc.getId()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbCrear)) {
            createButtonHandler();
        }
        if (e.getSource().equals(jbModificar)) {
            updateButtonHandler();
        }
        if (e.getSource().equals(jbEliminar)) {
            deleteButtonHandler();
        }
        if (e.getSource().equals(jcbCategorias)) {
            jcbCategoriasHandler();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.jtpCenter)) {
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
        }
        if (e.getSource().equals(this.jtMarcas)) {
            int fila = this.jtMarcas.rowAtPoint(e.getPoint());
            int columna = this.jtMarcas.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.jbModificar.setEnabled(true);
                this.jbEliminar.setEnabled(true);
            } else {
                this.jbModificar.setEnabled(false);
                this.jbEliminar.setEnabled(false);
            }
        }
        if (e.getSource().equals(this.jtCategorias)) {
            int fila = this.jtCategorias.rowAtPoint(e.getPoint());
            int columna = this.jtCategorias.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.jbModificar.setEnabled(true);
                this.jbEliminar.setEnabled(true);
            } else {
                this.jbModificar.setEnabled(false);
                this.jbEliminar.setEnabled(false);
            }
        }

        if (e.getSource().equals(this.jtSubCategorias)) {
            int fila = this.jtSubCategorias.rowAtPoint(e.getPoint());
            int columna = this.jtSubCategorias.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.jbModificar.setEnabled(true);
                this.jbEliminar.setEnabled(true);
            } else {
                this.jbModificar.setEnabled(false);
                this.jbEliminar.setEnabled(false);
            }
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
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void mostrarDialogoCreacionSubCategoria(int tipo, ProductoCategoria categoria) {
        String titulo = "Crear/Modificar sub categoría";
        JPanel fields = new JPanel(new GridLayout(3, 1));
        JTextField info = new JTextField("Seleccione la categoría e ingrese el nombre de la subcategoría");
        info.setEditable(false);
        JTextField nombreSubCategoria = new JTextField(10);
        JComboBox<ProductoCategoria> comboBox = new JComboBox();
        for (ProductoCategoria productoCategoria : productoCategoriaTm.getList()) {
            comboBox.addItem(productoCategoria);
        }
        switch (tipo) {
            case CREAR_SUB_CATEGORIA:
                titulo = "Crear sub categoría";
                break;
            case MODIFICAR_SUB_CATEGORIA:
                titulo = "Modificar sub categoría";
                nombreSubCategoria.setText(categoria.getDescripcion());
                comboBox.setSelectedItem(categoria.getPadre());
                comboBox.setEnabled(false);
                break;
        }
        fields.add(info);
        fields.add(comboBox);
        fields.add(nombreSubCategoria);
        int result = JOptionPane.showConfirmDialog(this, fields, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (result) {
            case JOptionPane.OK_OPTION:
                switch (tipo) {
                    case CREAR_SUB_CATEGORIA:
                        agregarSubCategoria(nombreSubCategoria.getText(), comboBox.getItemAt(comboBox.getSelectedIndex()));
                        break;
                    case MODIFICAR_SUB_CATEGORIA:
                        modificarSubCategoria(nombreSubCategoria.getText(), comboBox.getItemAt(comboBox.getSelectedIndex()));
                        break;
                }
                break;
        }
    }
}
