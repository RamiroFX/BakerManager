/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.Categoria;

import Entities.ProductoCategoria;
import ModeloTabla.ProductoCategoriaTableModel;
import ModeloTabla.ProductoSubCategoriaTableModel;
import bakermanager.C_inicio;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
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

    private static final int MAX_LENGTH = 50, CREAR_SUB_CATEGORIA = 1, MODIFICAR_SUB_CATEGORIA = 2;

    private javax.swing.JButton jbCrear, jbModificar, jbEliminar;
    private JTabbedPane jtpCenter;
    private JPanel jpSouth, jpSubCategorias;
    JScrollPane jspCategorias, jspSubCategorias;
    JTable jtCategorias, jtSubCategorias;
    private JComboBox<ProductoCategoria> jcbCategorias;
    ProductoSubCategoriaTableModel productoSubCategoriaTm;
    ProductoCategoriaTableModel productoCategoriaTm;

    public V_seleccionarCategoria(C_inicio c_inicio) {
        super(c_inicio.vista, true);
        construirLayout(c_inicio);
        initComponents();
    }

    private void construirLayout(C_inicio c_inicio) {
        setTitle("Parametros");
        setSize(new java.awt.Dimension(500, 300));
        setLocationRelativeTo(c_inicio.vista);
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
        jpSouth.add(jbCrear);
        jpSouth.add(jbModificar);
        jpSouth.add(jbEliminar);
        getContentPane().setLayout(new MigLayout());
        getContentPane().add(jtpCenter, "dock center");
        getContentPane().add(jpSouth, "dock south");
    }
}
