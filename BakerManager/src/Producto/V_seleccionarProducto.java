/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_seleccionarProducto extends JDialog {

    JButton jbAceptar, jbSalir, jbBuscar, jbBorrar, jbProveedor, jbCrearProducto;
    JScrollPane jspProducto;
    JTable jtProducto;
    JPanel jpBotones, jpTop, jpBotonesTop, jpJtextFieldTop;
    public JComboBox jcbImpuesto, jcbMarca, jcbEstado, jcbRubro, jcbBusqueda;
    public JTextField jtfBuscar, jtfProveedor;

    public V_seleccionarProducto(JDialog main) {
        super(main, "Seleccionar producto", true);
        setSize(1200, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpTop, BorderLayout.NORTH);
        getContentPane().add(jspProducto, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jcbImpuesto = new JComboBox();
        jcbMarca = new JComboBox();
        jcbEstado = new JComboBox();
        jcbRubro = new JComboBox();
        jbProveedor = new JButton("Proveedor");
        jcbBusqueda = new JComboBox();
        jtfProveedor = new JTextField();
        jtfProveedor.setPreferredSize(new Dimension(250, 25));
        jtfProveedor.setEditable(false);
        jpFiltros.add(jbProveedor);
        jpFiltros.add(jtfProveedor);
        jpFiltros.add(new JLabel("Marca:"));
        jpFiltros.add(jcbMarca);
        jpFiltros.add(new JLabel("Rubro:"));
        jpFiltros.add(jcbRubro);
        jpFiltros.add(new JLabel("Impuesto:"));
        jpFiltros.add(jcbImpuesto);
        jpFiltros.add(new JLabel("Estado:"));
        jpFiltros.add(jcbEstado);
        jpFiltros.add(new JLabel("Busqueda:"));
        jpFiltros.add(jcbBusqueda);
        jpBotonesTop = new JPanel();
        jpJtextFieldTop = new JPanel(new BorderLayout());
        jtfBuscar = new JTextField();
        jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
        jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
        jpJtextFieldTop.add(jtfBuscar);
        jbBuscar = new JButton("Buscar");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar);
        jpTop.add(jpJtextFieldTop, "push x");
        jpTop.add(jpBotonesTop, "wrap");
        jpTop.add(jpFiltros, "span, grow");
        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void initComp() {
        initTop();
        jtProducto = new JTable();
        jspProducto = new JScrollPane(jtProducto);
        jbCrearProducto = new JButton("Crear producto");
        jbCrearProducto.setName("crear producto");
        jbAceptar = new JButton("Seleccionar producto");
        jbSalir = new JButton("Cerrar");
        jpBotones = new JPanel();
        jpBotones.add(jbCrearProducto);
        jpBotones.add(jbAceptar);
        jpBotones.add(jbSalir);
    }
}
