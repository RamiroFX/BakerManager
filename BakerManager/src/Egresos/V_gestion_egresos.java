/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import Interface.CommonFormat;
import Utilities.JTablePagination;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.Insets;
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
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_gestion_egresos extends JInternalFrame {

    public JButton jbBuscar, jbBuscarDetalle, jbBorrar, jbAgregar, jbDetalle,
            jbResumen, jbProveedor, jbFuncionario, jbGraficos;
    public JTextField jtfNroFactura, jtfProveedor, jtfFuncionario;
    public JComboBox jcbCondCompra;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JTable jtEgresoDetalle;
    public JTablePagination jtEgresoCabecera;
    private JScrollPane jspEgresoDetalle;
    private JSplitPane jspMid;
    public JDateChooser jddInicio, jddFinal;

    public V_gestion_egresos() {
        super("Egresos", true, true, true, true);
        setSize(950, 600);
        setName("jifGestionEgresos");
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
        jbFuncionario = new JButton("Funcionario");
        jcbCondCompra = new JComboBox();
        jtfNroFactura = new JTextField();
        jtfProveedor = new JTextField();
        jtfProveedor.setEditable(false);
        jtfProveedor.setPreferredSize(new Dimension(250, 10));
        jtfFuncionario = new JTextField();
        jtfFuncionario.setEditable(false);
        jtfFuncionario.setPreferredSize(new Dimension(250, 10));
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jpFiltros.add(jbProveedor, "growx");
        jpFiltros.add(jtfProveedor);
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx");
        jpFiltros.add(new JLabel("Cond. compra:"));
        jpFiltros.add(jcbCondCompra, "wrap");
        jpFiltros.add(jbFuncionario);
        jpFiltros.add(jtfFuncionario);
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx");
        jpFiltros.add(new JLabel("Nro. factura:"));
        jpFiltros.add(jtfNroFactura, "growx");
        jpBotonesTop = new JPanel(new MigLayout());
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jbBuscar = new JButton("Buscar");
        jbBuscar.setName("buscar compra");
        jbBorrar = new JButton("Borrar");
        jbBuscarDetalle = new JButton("Buscar por detalle");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar, "wrap");
        jpBotonesTop.add(jbBuscarDetalle, "span, growx");
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda"));
    }

    private void initMid() {
        //Panel medio izquierda
        jtEgresoCabecera = new JTablePagination(100);

        //panel medio derecha
        jtEgresoDetalle = new JTable();
        this.jtEgresoDetalle.getTableHeader().setReorderingAllowed(false);
        jspEgresoDetalle = new JScrollPane(jtEgresoDetalle);
        //creamos nuestro splitpane y agregamos los dos paneles del medio
        jspMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jtEgresoCabecera, jspEgresoDetalle);
        jspMid.setDividerLocation(this.getWidth() / 2);
        jspMid.setOneTouchExpandable(true);
    }

    private void initBot() {
        jpBot = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAgregar = new JButton("Crear egreso [F1]");
        jbAgregar.setName("crear compra");
        jbAgregar.setMargin(insets);
        jbAgregar.setFont(CommonFormat.fuente);
        jbDetalle = new JButton("Ver detalle");
        jbDetalle.setName("detalle compra");
        jbDetalle.setMargin(insets);
        jbDetalle.setFont(CommonFormat.fuente);
        jbResumen = new JButton("Ver resumen [F2]");
        jbResumen.setName("resumen compra");
        jbResumen.setMargin(insets);
        jbResumen.setFont(CommonFormat.fuente);
        jbGraficos = new JButton("Ver gráficos");
        jbGraficos.setMargin(insets);
        jbGraficos.setFont(CommonFormat.fuente);
        jpBot.add(jbAgregar);
        jpBot.add(jbDetalle);
        jpBot.add(jbResumen);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
