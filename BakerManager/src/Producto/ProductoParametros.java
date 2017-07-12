/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB.DB_Proveedor;
import DB.DB_manager;
import bakermanager.C_inicio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProductoParametros extends javax.swing.JDialog implements ActionListener, MouseListener, KeyListener {

    private javax.swing.JButton jbCrear, jbModificar, jbEliminar;
    private JTabbedPane jtpCenter;
    private JPanel jpSouth;
    JScrollPane jspMarcas, jspCategorias;
    JTable jtMarcas, jtCategorias;
    C_gestion_producto gestion_producto;

    public ProductoParametros(C_inicio c_inicio) {
        super(c_inicio.vista, true);
        setTitle("Parametros");
        setSize(new java.awt.Dimension(400, 300));
        setLocationRelativeTo(c_inicio.vista);
        initComponents();
        inicializarVista();
        agregarListener();
    }

    ProductoParametros(C_inicio c_inicio, C_gestion_producto aThis) {
        super(c_inicio.vista, true);
        gestion_producto = aThis;
        setTitle("Parametros");
        setSize(new java.awt.Dimension(400, 300));
        setLocationRelativeTo(c_inicio.vista);
        initComponents();
        inicializarVista();
        agregarListener();
    }

    private void inicializarVista() {
        jbEliminar.setEnabled(false);
        jbModificar.setEnabled(false);
        jtMarcas.setModel(DB_manager.consultarMarca());
        jtCategorias.setModel(DB_manager.consultarCategoria());
    }

    private void initMarcas() {
        jtMarcas = new JTable();
        jtMarcas.getTableHeader().setReorderingAllowed(false);
        jspMarcas = new JScrollPane(jtMarcas);

    }

    private void initCategorias() {
        jtCategorias = new JTable();
        jtCategorias.getTableHeader().setReorderingAllowed(false);
        jspCategorias = new JScrollPane(jtCategorias);
    }

    private void initComponents() {
        initMarcas();
        initCategorias();
        jtpCenter = new JTabbedPane();
        jtpCenter.add("Marcas", jspMarcas);
        jtpCenter.add("Categorias", jspCategorias);
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
        jtMarcas.addMouseListener(this);
        jbCrear.addActionListener(this);
        jbModificar.addActionListener(this);
        jbEliminar.addActionListener(this);
        /*
        KEYLISTENERS
        */
        jtpCenter.addKeyListener(this);
        jtCategorias.addKeyListener(this);
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
        if (m.length() > 30) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 30 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
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
        if (m.length() > 30) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 30 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
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
        if (c.length() > 30) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 30 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdProductoCategoria(c);
        if (b == null) {
            DB_Proveedor.insertarCategoria(c);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtCategorias.setModel(DB_manager.consultarCategoria());
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
        if (c.length() > 30) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 30 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdProductoCategoria(c);
        if (b == null) {
            int idCategoria = Integer.valueOf(String.valueOf(this.jtCategorias.getValueAt(jtCategorias.getSelectedRow(), 0)));
            DB_Proveedor.modificarCategoria(idCategoria, categoria);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtCategorias.setModel(DB_manager.consultarCategoria());
        } else {
            JOptionPane.showMessageDialog(this, "Categoría existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCategoria() {
        int idCategoria = Integer.valueOf(String.valueOf(this.jtCategorias.getValueAt(jtCategorias.getSelectedRow(), 0)));
        boolean m = DB_manager.productCategoriaEnUso(idCategoria);
        if (m) {
            int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    DB_Proveedor.eliminarProductoCategoria(idCategoria);
                    this.jbModificar.setEnabled(false);
                    this.jbEliminar.setEnabled(false);
                    this.jtCategorias.setModel(DB_manager.consultarCategoria());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Existe productos que se encuentran utilizando la categoría seleccionada.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     private void agregarImpuesto(String impuesto) {
     DB_Proveedor.insertarImpuesto(impuesto);
     this.jbModificar.setEnabled(false);
     this.jbEliminar.setEnabled(false);
     this.jtImpuesto.setModel(DB_manager.consultarImpuesto());
     }
     private void modificarImpuesto(String impuesto) {
     int idImpuesto = Integer.valueOf(String.valueOf(this.jtImpuesto.getValueAt(jtImpuesto.getSelectedRow(), 0)));
     DB_Proveedor.modificarCategoria(idImpuesto, impuesto);
     this.jbModificar.setEnabled(false);
     this.jbEliminar.setEnabled(false);
     this.jtImpuesto.setModel(DB_manager.consultarImpuesto());
     }
     private void eliminarImpuesto() {
     int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     if (option == JOptionPane.YES_OPTION) {
     try {
     int idImpuesto = Integer.valueOf(String.valueOf(this.jtImpuesto.getValueAt(jtImpuesto.getSelectedRow(), 0)));
     DB_Proveedor.eliminarProductoCategoria(idImpuesto);
     this.jbModificar.setEnabled(false);
     this.jbEliminar.setEnabled(false);
     this.jtImpuesto.setModel(DB_manager.consultarImpuesto());
     } catch (Exception e) {
     e.printStackTrace();
     return;
     }
     }
     }
     */
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
        }
        gestion_producto.actualizarVista();
    }

    private void updateButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspMarcas)) {
            String marca = JOptionPane.showInputDialog(this, "Inserte el nombre de la marca", "Insertar marca", JOptionPane.PLAIN_MESSAGE);
            if (marca != null) {
                if (!marca.isEmpty()) {
                    modificarMarca(marca);
                }
            }
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspCategorias)) {
            String rubro = JOptionPane.showInputDialog(this, "Inserte el nombre del rubro", "Insertar categoria", JOptionPane.PLAIN_MESSAGE);
            if (rubro != null) {
                if (!rubro.isEmpty()) {
                    modificarCategoria(rubro);
                }
            }
        }
        gestion_producto.actualizarVista();
    }

    private void deleteButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspMarcas)) {
            eliminarMarca();
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspCategorias)) {
            eliminarCategoria();
        }
        gestion_producto.actualizarVista();
    }

    private void cerrar() {
        System.runFinalization();
        this.dispose();
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
}
