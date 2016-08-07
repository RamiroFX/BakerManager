/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Administrador
 */
public class V_gestion_producto extends JInternalFrame {

    public JButton jbParametros, jbBuscar, jbBorrar, jbProveedor,
            jbModificar, jbAgregar, jbEliminar, jbAsigProdProv;
    private JLabel jlProducto, jlCodigo, jlImpuesto, jlRubro, jlPrecioCosto, jlMarca,
            jlPrecioVta, jlPrecioMayorista, jlEstado, jlCantActual;
    public JTextField jtfBuscar, jtfProducto, jtfCodigo, jtfPrecioCosto,
            jtfPrecioVta, jtfPrecioMayorista, jtfImpuesto, jtfMarca,
            jtfSuspendido, jtfRubro, jtfCantActual,jtfProveedor;
    public JComboBox jcbImpuesto, jcbMarca, jcbEstado, jcbRubro;
    private JPanel jpTop, jpBotonesTop, jpJtextFieldTop, jpMid, jpBot, jpMid1;
    public JTable jtProducto;
    private JScrollPane jspProducto;
    private JSplitPane jspMid;

    public V_gestion_producto() {
        super("Gestión de productos", true, true, true, true);
        setSize(900, 600);
        setName("jifGestionProducto");
        initTop();
        initMid();
        initBot();
        getContentPane().add(jpTop, "North");
        getContentPane().add(jspMid, "Center");
        getContentPane().add(jpBot, "South");
    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new MigLayout());
        jbProveedor = new JButton("Proveedor");
        jtfProveedor = new JTextField();
        jtfProveedor.setPreferredSize(new Dimension(250, 10));
        jtfProveedor.setEditable(false);
        jcbImpuesto = new JComboBox();
        jcbMarca = new JComboBox();
        jcbEstado = new JComboBox();
        jcbRubro = new JComboBox();
        jpFiltros.add(jbProveedor);
        jpFiltros.add(jtfProveedor);
        jpFiltros.add(new JLabel("Marca:"));
        jpFiltros.add(jcbMarca);
        jpFiltros.add(new JLabel("Rubro:"));
        jpFiltros.add(jcbRubro, "wrap");
        jpFiltros.add(new JLabel("Impuesto:"));
        jpFiltros.add(jcbImpuesto);
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
        jpTop.add(jpJtextFieldTop, "pushx");
        jpTop.add(jpBotonesTop, "wrap");
        jpTop.add(jpFiltros, "span");
        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void initMid() {
        //Panel medio izquierda
        jtProducto = new JTable();
        jspProducto = new JScrollPane(jtProducto);

        //panel medio derecha
        jpMid = new JPanel(new BorderLayout());
        jpMid1 = new JPanel(new GridLayout(10, 2));
        jlProducto = new JLabel("Producto");
        jlProducto.setHorizontalAlignment(JLabel.CENTER);
        jlCodigo = new JLabel("Codigo");
        jlCodigo.setHorizontalAlignment(JLabel.CENTER);
        jlImpuesto = new JLabel("Impuesto");
        jlImpuesto.setHorizontalAlignment(JLabel.CENTER);
        jlRubro = new JLabel("Rubro");
        jlRubro.setHorizontalAlignment(JLabel.CENTER);
        jlPrecioCosto = new JLabel("Precio Costo");
        jlPrecioCosto.setHorizontalAlignment(JLabel.CENTER);
        jlMarca = new JLabel("Marca");
        jlMarca.setHorizontalAlignment(JLabel.CENTER);
        jlPrecioVta = new JLabel("Prec. Vta");
        jlPrecioVta.setHorizontalAlignment(JLabel.CENTER);
        jlPrecioMayorista = new JLabel("Prec. May");
        jlPrecioMayorista.setHorizontalAlignment(JLabel.CENTER);
        jlEstado = new JLabel("Estado");
        jlEstado.setHorizontalAlignment(JLabel.CENTER);
        jlCantActual = new JLabel("Cantidad actual");
        jlCantActual.setHorizontalAlignment(JLabel.CENTER);
        jtfProducto = new JTextField();
        jtfProducto.setEditable(false);
        jtfCodigo = new JTextField();
        jtfCodigo.setEditable(false);
        jtfImpuesto = new JTextField();
        jtfImpuesto.setEditable(false);
        jtfRubro = new JTextField();
        jtfRubro.setEditable(false);
        jtfPrecioCosto = new JTextField();
        jtfPrecioCosto.setEditable(false);
        jtfMarca = new JTextField();
        jtfMarca.setEditable(false);
        jtfPrecioVta = new JTextField();
        jtfPrecioVta.setEditable(false);
        jtfPrecioMayorista = new JTextField();
        jtfPrecioMayorista.setEditable(false);
        jtfSuspendido = new JTextField();
        jtfSuspendido.setEditable(false);
        jtfCantActual = new JTextField();
        jtfCantActual.setEditable(false);

        jpMid1.add(jlProducto);
        jpMid1.add(jtfProducto);
        jpMid1.add(jlCodigo);
        jpMid1.add(jtfCodigo);
        jpMid1.add(jlImpuesto);
        jpMid1.add(jtfImpuesto);
        jpMid1.add(jlRubro);
        jpMid1.add(jtfRubro);
        jpMid1.add(jlPrecioCosto);
        jpMid1.add(jtfPrecioCosto);
        jpMid1.add(jlMarca);
        jpMid1.add(jtfMarca);
        jpMid1.add(jlPrecioVta);
        jpMid1.add(jtfPrecioVta);
        jpMid1.add(jlPrecioMayorista);
        jpMid1.add(jtfPrecioMayorista);
        jpMid1.add(jlEstado);
        jpMid1.add(jtfSuspendido);
        jpMid1.add(jlCantActual);
        jpMid1.add(jtfCantActual);

        jpMid.add(jpMid1, "Center");
        //creamos nuestro splitpane y agregamos los dos paneles del medio
        jspMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspProducto, jpMid);
        jspMid.setDividerLocation(this.getWidth() / 2);
        jspMid.setOneTouchExpandable(true);
    }

    private void initBot() {
        jpBot = new JPanel();
        jbParametros = new JButton("Parametros");
        jbModificar = new JButton("Modificar");
        jbModificar.setEnabled(false);
        jbAgregar = new JButton("Agregar");
        jbEliminar = new JButton("Eliminar");
        jbEliminar.setEnabled(false);
        jbAsigProdProv = new JButton("Prod-Proveedor");
        //jpBot.add(jbParametros);
        jpBot.add(jbModificar);
        jpBot.add(jbAgregar);
        jpBot.add(jbParametros);
        jpBot.add(jbAsigProdProv);
        //jpBot.add(jbEliminar);
        //jpBot.add(jbCostear);
    }
}
