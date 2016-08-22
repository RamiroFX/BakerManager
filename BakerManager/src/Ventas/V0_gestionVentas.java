/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

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
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V0_gestionVentas extends JInternalFrame {

    public JButton jbBuscar, jbBuscarDetalle, jbBorrar, jbAgregar, jbDetalle,
            jbResumen, jbCliente, jbEmpleado;
    public JTextField jtfNroFactura, jtfCliente, jtfEmpleado;
    public JComboBox jcbEmpleado, jcbCondCompra;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JTable jtEgresoDetalle;
    public JTablePagination jtEgresoCabecera;
    private JScrollPane jspEgresoDetalle;
    private JSplitPane jspMid;
    public JDateChooser jddInicio, jddFinal;

    public V0_gestionVentas() {
        super("Ventas", true, true, true, true);
        //setPreferredSize(new Dimension(950, 600));
        setSize(950, 600);
        setName("jifGestionVentas");
        initTop();
        initMid();
        initBot();
        getContentPane().add(jpTop, "North");
        getContentPane().add(jspMid, "Center");
        getContentPane().add(jpBot, "South");
    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jbCliente = new JButton("Cliente");
        jtfCliente = new JTextField();
        jtfCliente.setPreferredSize(new Dimension(250, 10));
        jtfCliente.setEditable(false);
        jbEmpleado = new JButton("Funcionario");
        jtfEmpleado = new JTextField();
        jtfEmpleado.setPreferredSize(new Dimension(250, 10));
        jtfEmpleado.setEditable(false);
        jcbCondCompra = new JComboBox();
        jtfNroFactura = new JTextField();
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jpFiltros.add(jbCliente, "growx");
        jpFiltros.add(jtfCliente, "growx");
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx");
        jpFiltros.add(new JLabel("Cond. compra:"));
        jpFiltros.add(jcbCondCompra, "wrap");
        jpFiltros.add(jbEmpleado);
        jpFiltros.add(jtfEmpleado, "growx");
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx");
        jpFiltros.add(new JLabel("Nro. factura:"));
        jpFiltros.add(jtfNroFactura, "growx");
        jpBotonesTop = new JPanel(new MigLayout());
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        /*jtfBuscar = new JTextField();
         jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
         jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
         jpJtextFieldTop.add(jtfBuscar);*/
        jbBuscar = new JButton("Buscar");
        jbBuscar.setName("buscar venta");
        jbBorrar = new JButton("Borrar");
        jbBuscarDetalle = new JButton("Buscar por detalle");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar, "wrap");
        jpBotonesTop.add(jbBuscarDetalle, "span, growx");
        //jpTop.add(jpJtextFieldTop, "pushx");
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void initMid() {
        //Panel medio izquierda
        jtEgresoCabecera = new JTablePagination(100);

        //panel medio derecha
        jtEgresoDetalle = new JTable();
        jspEgresoDetalle = new JScrollPane(jtEgresoDetalle);
        //creamos nuestro splitpane y agregamos los dos paneles del medio
        jspMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jtEgresoCabecera, jspEgresoDetalle);
        jspMid.setDividerLocation(this.getWidth() / 2);
        jspMid.setOneTouchExpandable(true);
    }

    private void initBot() {
        jpBot = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAgregar = new JButton("Crear venta");
        jbAgregar.setName("crear venta");
        jbAgregar.setMargin(insets);
        jbDetalle = new JButton("Ver detalle");
        jbDetalle.setName("detalle venta");
        jbDetalle.setMargin(insets);
        jbResumen = new JButton("Ver resumen");
        jbResumen.setName("resumen venta");
        jbResumen.setMargin(insets);
        jbResumen.setEnabled(false);
        jpBot.add(jbAgregar);
        jpBot.add(jbDetalle);
        jpBot.add(jbResumen);
    }
}
