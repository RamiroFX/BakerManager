/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.Categoria;

import Entities.ProductoCategoria;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_seleccionarCategoria extends javax.swing.JDialog {

    public javax.swing.JButton jbCrear, jbModificar, jbEliminar, jbSeleccionar;
    public JTabbedPane jtpCenter;
    public JPanel jpSouth, jpSubCategorias;
    public JScrollPane jspCategorias, jspSubCategorias;
    public JTable jtCategorias, jtSubCategorias;
    public JComboBox<ProductoCategoria> jcbCategorias;

    public V_seleccionarCategoria(JFrame frame) {
        super(frame, true);
        construirLayout();
        setLocationRelativeTo(frame);
        initComponents();
    }

    public V_seleccionarCategoria(JDialog dialog) {
        super(dialog, true);
        construirLayout();
        setLocationRelativeTo(dialog);
        initComponents();
    }

    private void construirLayout() {
        setTitle("Categorías de productos");
        setSize(new java.awt.Dimension(500, 300));
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
        initCategorias();
        jpSubCategorias = new JPanel(new BorderLayout());
        JPanel jpSubCategorias2 = new JPanel();
        jpSubCategorias2.add(new JLabel("Categoria"));
        jpSubCategorias2.add(jcbCategorias);
        jpSubCategorias.add(jpSubCategorias2, BorderLayout.NORTH);
        jpSubCategorias.add(jspSubCategorias, BorderLayout.CENTER);
        jtpCenter = new JTabbedPane();
        jtpCenter.add("Categorias", jspCategorias);
        jtpCenter.add("Sub Categorias", jpSubCategorias);
        jpSouth = new JPanel();
        jbCrear = new javax.swing.JButton("Agregar");
        jbModificar = new javax.swing.JButton("Modificar");
        jbEliminar = new javax.swing.JButton("Eliminar");
        jbSeleccionar = new javax.swing.JButton("Seleccionar");
        jpSouth.add(jbCrear);
        jpSouth.add(jbModificar);
        jpSouth.add(jbEliminar);
        jpSouth.add(jbSeleccionar);
        getContentPane().setLayout(new MigLayout());
        getContentPane().add(jtpCenter, "dock center");
        getContentPane().add(jpSouth, "dock south");
    }
}
