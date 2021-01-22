/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import Entities.Estado;
import Entities.ProductoCategoria;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
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
public class V_seleccionarProductoPorClasif extends JDialog {

    JButton jbAceptar, jbSalir, jbBuscar, jbBorrar, jbCrearProducto;
    JScrollPane jspProducto;
    JTable jtProducto;
    JPanel jpBotones, jpTop, jpBotonesTop, jpJtextFieldTop;
    public JComboBox<Estado> jcbEstado;
    public JComboBox<ProductoCategoria> jcbCategoria;
    public JComboBox jcbBuscarPor;
    public JTextField jtfBuscar;

    public V_seleccionarProductoPorClasif(JDialog main) {
        super(main, "Seleccionar producto", true);
        setSize(1200, 400);
        setLocation(establecerPosicion());
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpTop, BorderLayout.NORTH);
        getContentPane().add(jspProducto, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private Point establecerPosicion() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        int width = this.getWidth();
        //int height = this.getHeight();

        int x = ((screenWidth - width) / 2);
        int y = (int) Math.round((screenHeight * 0.8) / 2);

        return new Point(x, y);
    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER));

        jcbEstado = new JComboBox();
        jcbBuscarPor = new JComboBox();
        jcbCategoria = new JComboBox();
        jpFiltros.add(new JLabel("Buscar por:"));
        jpFiltros.add(jcbBuscarPor);
        jpFiltros.add(new JLabel("Categoría:"));
        jpFiltros.add(jcbCategoria);
        jpFiltros.add(new JLabel("Estado:"));
        jpFiltros.add(jcbEstado);
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
        jtProducto.getTableHeader().setReorderingAllowed(false);
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
