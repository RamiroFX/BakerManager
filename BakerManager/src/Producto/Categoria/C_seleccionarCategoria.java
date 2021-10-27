/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.Categoria;

import DB.DB_Producto;
import DB.DB_Proveedor;
import DB.DB_manager;
import Entities.ProductoCategoria;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Ramiro
 */
public class C_seleccionarCategoria implements ActionListener, MouseListener, KeyListener {

    private static final int MAX_LENGTH = 50, CREAR_SUB_CATEGORIA = 1, MODIFICAR_SUB_CATEGORIA = 2;
    private M_seleccionarCategoria modelo;
    private V_seleccionarCategoria vista;
    boolean cerrar;

    public C_seleccionarCategoria(M_seleccionarCategoria modelo, V_seleccionarCategoria vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.cerrar = true;
        initializarLogica();
        inicializarVista();
        agregarListener();
    }

    private void initializarLogica() {
        this.modelo.actualizarProductoCategoria();
        this.modelo.actualizarProductoSubCategoria();
        actualizarComboBox();
    }

    private void inicializarVista() {
        this.vista.jbEliminar.setEnabled(false);
        this.vista.jbModificar.setEnabled(false);
        this.vista.jtCategorias.setModel(this.modelo.getProductoCategoriaTm());
        this.vista.jtSubCategorias.setModel(this.modelo.getProductoSubCategoriaTm());
        Utilities.c_packColumn.packColumns(this.vista.jtCategorias, 1);
        Utilities.c_packColumn.packColumns(this.vista.jtSubCategorias, 1);
    }

    private void agregarListener() {
        this.vista.jbSeleccionar.addActionListener(this);
        this.vista.jbCrear.addActionListener(this);
        this.vista.jbModificar.addActionListener(this);
        this.vista.jbEliminar.addActionListener(this);
        this.vista.jcbCategorias.addActionListener(this);
        /*
        KEYLISTENERS
         */
        this.vista.jtpCenter.addKeyListener(this);
        this.vista.jtCategorias.addKeyListener(this);
        this.vista.jtSubCategorias.addKeyListener(this);
        this.vista.jbCrear.addKeyListener(this);
        this.vista.jbModificar.addKeyListener(this);
        this.vista.jbEliminar.addKeyListener(this);
        /*
        MOUSE LISTENERS
         */
        this.vista.jtpCenter.addMouseListener(this);
        this.vista.jtCategorias.addMouseListener(this);
        this.vista.jtSubCategorias.addMouseListener(this);
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void agregarCategoria(String categoria) {
        String c = categoria.trim();
        if (c.length() < 1 || c.isEmpty()) {
            JOptionPane.showMessageDialog(this.vista, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (c.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this.vista, "Máximo permitido " + MAX_LENGTH + " caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdProductoCategoria(c);
        if (b == null) {
            DB_Proveedor.insertarCategoria(c);
            this.vista.jbModificar.setEnabled(false);
            this.vista.jbEliminar.setEnabled(false);
            this.modelo.actualizarProductoCategoria();
            actualizarComboBox();
        } else {
            JOptionPane.showMessageDialog(this.vista, "Categoría existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarCategoria(String categoria) {
        String c = categoria.trim();
        if (c.length() < 1 || c.isEmpty()) {
            JOptionPane.showMessageDialog(this.vista, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (c.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this.vista, "Máximo permitido " + MAX_LENGTH + " caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdProductoCategoria(c);
        if (b == null) {
            if (this.vista.jtCategorias.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this.vista, "Seleccione nuevamente la Categoría a modificar.", "Alerta", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idCategoria = this.modelo.getProductoCategoriaTm().getList().get(this.vista.jtCategorias.getSelectedRow()).getId();
            DB_Proveedor.modificarCategoria(idCategoria, categoria);
            this.vista.jbModificar.setEnabled(false);
            this.vista.jbEliminar.setEnabled(false);
            this.modelo.actualizarProductoCategoria();
            actualizarComboBox();
        } else {
            JOptionPane.showMessageDialog(this.vista, "Categoría existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCategoria() {
        System.out.println("Producto.ProductoParametros.eliminarCategoria()");
        if (this.vista.jtCategorias.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this.vista, "Seleccione nuevamente la Categoría a eliminar.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idCategoria = this.modelo.getProductoCategoriaTm().getList().get(this.vista.jtCategorias.getSelectedRow()).getId();
        boolean m = DB_manager.productCategoriaEnUso(idCategoria);
        boolean n = DB_manager.productoSubCategoriaEnUso(idCategoria);
        if (m == true && n == false) {
            int option = JOptionPane.showConfirmDialog(this.vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    DB_Proveedor.eliminarProductoCategoria(idCategoria);
                    this.vista.jbModificar.setEnabled(false);
                    this.vista.jbEliminar.setEnabled(false);
                    this.modelo.actualizarProductoCategoria();
                    actualizarComboBox();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this.vista, "Ocurrio un error al eliminar la categoría.", "Alerta", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this.vista, "Existe productos o sub-categorías que se encuentran utilizando la categoría seleccionada.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarSubCategoria(String subCategoria, ProductoCategoria padre) {
        String c = subCategoria.trim();
        if (c.length() < 1 || c.isEmpty()) {
            JOptionPane.showMessageDialog(this.vista, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (c.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this.vista, "Máximo permitido 30 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean b = DB_manager.existeSubCategoria(c, padre.getId());
        if (!b) {
            DB_manager.insertarSubCategoria(c, padre);
            this.vista.jbModificar.setEnabled(false);
            this.vista.jbEliminar.setEnabled(false);
            this.modelo.actualizarProductoSubCategoria(padre.getId());
        } else {
            JOptionPane.showMessageDialog(this.vista, "Sub Categoría existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarSubCategoria(String categoria, ProductoCategoria padre) {
        String c = categoria.trim();
        if (c.length() < 1 || c.isEmpty()) {
            JOptionPane.showMessageDialog(this.vista, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (c.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this.vista, "Máximo permitido " + MAX_LENGTH + " caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (this.vista.jtSubCategorias.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this.vista, "Seleccione nuevamente la Sub Categoría a modificar.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean b = DB_manager.existeSubCategoria(c, padre.getId());
        int idSubCategoria = this.modelo.getProductoSubCategoriaTm().getList().get(this.vista.jtSubCategorias.getSelectedRow()).getId();
        if (!b) {
            System.out.println("La sub categoria no existe");
            DB_Proveedor.modificarCategoria(idSubCategoria, c);
            this.vista.jbModificar.setEnabled(false);
            this.vista.jbEliminar.setEnabled(false);
            this.modelo.actualizarProductoSubCategoria(padre.getId());
        } else {
            System.out.println("La sub categoria existe");
            String subCategoria = this.modelo.getProductoSubCategoriaTm().getList().get(this.vista.jtSubCategorias.getSelectedRow()).getDescripcion();
            if (c.toLowerCase().equals(subCategoria.toLowerCase())) {
                System.out.println("ES LA MISMA SUB CATEGORIA.");
                DB_Proveedor.modificarCategoria(idSubCategoria, c);
                this.vista.jbModificar.setEnabled(false);
                this.vista.jbEliminar.setEnabled(false);
                this.modelo.actualizarProductoSubCategoria(padre.getId());
            } else {
                JOptionPane.showMessageDialog(this.vista, "Sub Categoría existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarSubCategoria() {
        if (this.vista.jtSubCategorias.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this.vista, "Seleccione nuevamente la Sub Categoría a eliminar.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idCategoria = this.modelo.getProductoSubCategoriaTm().getList().get(this.vista.jtSubCategorias.getSelectedRow()).getId();
        boolean m = DB_manager.productCategoriaEnUso(idCategoria);
        if (m) {
            int option = JOptionPane.showConfirmDialog(this.vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    int index = this.vista.jcbCategorias.getSelectedIndex();
                    ProductoCategoria pc = this.vista.jcbCategorias.getItemAt(index);
                    DB_Proveedor.eliminarProductoCategoria(idCategoria);
                    this.vista.jbModificar.setEnabled(false);
                    this.vista.jbEliminar.setEnabled(false);
                    this.modelo.actualizarProductoSubCategoria(pc.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this.vista, "Existe productos que se encuentran utilizando la categoría seleccionada.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createButtonHandler() {
        if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jspCategorias)) {
            String rubro = JOptionPane.showInputDialog(this.vista, "Inserte el nombre del rubro", "Insertar categoria", JOptionPane.PLAIN_MESSAGE);
            if (rubro != null) {
                if (!rubro.isEmpty()) {
                    agregarCategoria(rubro);
                }
            }
        } else if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jpSubCategorias)) {
            mostrarDialogoCreacionSubCategoria(CREAR_SUB_CATEGORIA, null);
        }
    }

    private void updateButtonHandler() {
        if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jspCategorias)) {
            String rubro = JOptionPane.showInputDialog(this.vista, "Inserte el nombre del rubro", "Modificar categoria", JOptionPane.PLAIN_MESSAGE);
            if (rubro != null) {
                if (!rubro.isEmpty()) {
                    modificarCategoria(rubro);
                }
            }
        } else if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jpSubCategorias)) {
            int fila = this.vista.jtSubCategorias.getSelectedRow();
            if (fila < 0) {
                return;
            }
            ProductoCategoria prodCat = this.modelo.getProductoSubCategoriaTm().getList().get(fila);
            mostrarDialogoCreacionSubCategoria(MODIFICAR_SUB_CATEGORIA, prodCat);
        }
    }

    private void deleteButtonHandler() {
        if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jspCategorias)) {
            eliminarCategoria();
        } else if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jpSubCategorias)) {
            eliminarSubCategoria();
        }
    }

    private void cerrar() {
        System.runFinalization();
        this.vista.dispose();
    }

    private void actualizarComboBox() {
        ProductoCategoria categoria = new ProductoCategoria();
        categoria.setId(-1);
        categoria.setDescripcion("Todos");
        this.vista.jcbCategorias.removeAllItems();
        this.vista.jcbCategorias.addItem(categoria);
        for (ProductoCategoria productoCategoria : this.modelo.getProductoCategoriaTm().getList()) {
            this.vista.jcbCategorias.addItem(productoCategoria);
        }
    }

    private void jcbCategoriasHandler() {
        int index = this.vista.jcbCategorias.getSelectedIndex();
        if (index < 0) {
            return;
        }
        ProductoCategoria pc = this.vista.jcbCategorias.getItemAt(index);
        this.modelo.getProductoSubCategoriaTm().setList(DB_Producto.obtenerProductoSubCategoria(pc.getId()));
    }

    private void seleccionarCategoria() {
        if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jspCategorias)) {
            int row = vista.jtCategorias.getSelectedRow();
            if (row > -1) {
                ProductoCategoria pc = modelo.getProductoCategoriaTm().getList().get(row);
                modelo.getCallback().recibirProductoCategoria(pc);
                //cerrar();
            }
        } else if (this.vista.jtpCenter.getSelectedComponent().equals(this.vista.jpSubCategorias)) {
            int row = vista.jtSubCategorias.getSelectedRow();
            if (row > -1) {
                ProductoCategoria pc = modelo.getProductoSubCategoriaTm().getList().get(row);
                modelo.getCallback().recibirProductoCategoria(pc);
                //cerrar();
            }
        }
        if (cerrar) {
            cerrar();
        }
    }

    public void mostrarDialogoCreacionSubCategoria(int tipo, ProductoCategoria categoria) {
        String titulo = "Crear/Modificar sub categoría";
        JPanel fields = new JPanel(new GridLayout(3, 1));
        JTextField info = new JTextField("Seleccione la categoría e ingrese el nombre de la subcategoría");
        info.setEditable(false);
        JTextField nombreSubCategoria = new JTextField(10);
        JComboBox<ProductoCategoria> comboBox = new JComboBox();
        for (ProductoCategoria productoCategoria : this.modelo.getProductoCategoriaTm().getList()) {
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
        int result = JOptionPane.showConfirmDialog(this.vista, fields, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbCrear)) {
            createButtonHandler();
        }
        if (e.getSource().equals(this.vista.jbModificar)) {
            updateButtonHandler();
        }
        if (e.getSource().equals(this.vista.jbEliminar)) {
            deleteButtonHandler();
        }
        if (e.getSource().equals(this.vista.jcbCategorias)) {
            jcbCategoriasHandler();
        }
        if (e.getSource().equals(this.vista.jbSeleccionar)) {
            seleccionarCategoria();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtpCenter)) {
            this.vista.jbModificar.setEnabled(false);
            this.vista.jbEliminar.setEnabled(false);
        }
        if (e.getSource().equals(this.vista.jtCategorias)) {
            int fila = this.vista.jtCategorias.rowAtPoint(e.getPoint());
            int columna = this.vista.jtCategorias.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbModificar.setEnabled(true);
                this.vista.jbEliminar.setEnabled(true);
                if (e.getClickCount() == 2) {
                    seleccionarCategoria();
                }
            } else {
                this.vista.jbModificar.setEnabled(false);
                this.vista.jbEliminar.setEnabled(false);
            }
        }

        if (e.getSource().equals(this.vista.jtSubCategorias)) {
            int fila = this.vista.jtSubCategorias.rowAtPoint(e.getPoint());
            int columna = this.vista.jtSubCategorias.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbModificar.setEnabled(true);
                this.vista.jbEliminar.setEnabled(true);
                if (e.getClickCount() == 2) {
                    seleccionarCategoria();
                }
            } else {
                this.vista.jbModificar.setEnabled(false);
                this.vista.jbEliminar.setEnabled(false);
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

    public void establecerSiempreVisible() {
        this.cerrar = false;
    }

}
