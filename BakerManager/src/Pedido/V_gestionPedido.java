/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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
public class V_gestionPedido extends JInternalFrame {

    public JButton jbBuscar, jbBuscarDetalle, jbBorrar, jbAgregar, jbDetalle,
            jbResumen, jbCliente, jbEmpleado, jbPagoPedido, jbCancelarPedido
            ,jbPedidosPendientes,jbCharts;
    public JTextField jtfNroPedido, jtfCliente, jtfEmpleado;
    public JComboBox jcbEmpleado, jcbCondVenta, jcbEstadoPedido;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JTable jtPedido, jtPedidoDetalle;
    private JScrollPane jspEgresoCabecera, jspEgresoDetalle;
    private JSplitPane jspMid;
    public JDateChooser jddInicio, jddFinal;

    public V_gestionPedido(JFrame frame) {
        super("Gesión de pedidos", true, true, true, true);
        //super(frame, "Gestión de pedidos", true);
        //setPreferredSize(new Dimension(950, 600));
        setSize(1200, 600);
        setName("jifGestionPedidos");
        //setLocationRelativeTo(frame);
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
        jcbCondVenta = new JComboBox();
        jcbEstadoPedido = new JComboBox();
        jtfNroPedido = new JTextField();
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jpFiltros.add(jbCliente, "growx");
        jpFiltros.add(jtfCliente, "growx");
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx");
        jpFiltros.add(new JLabel("Cond. compra:"));
        jpFiltros.add(jcbCondVenta);
        jpFiltros.add(new JLabel("Estado:"));
        jpFiltros.add(jcbEstadoPedido, "wrap");
        jpFiltros.add(jbEmpleado);
        jpFiltros.add(jtfEmpleado, "growx");
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx");
        jpFiltros.add(new JLabel("Nro. factura:"));
        jpFiltros.add(jtfNroPedido, "growx");
        jpFiltros.add(new JLabel(), "growx");
        jpFiltros.add(new JLabel(), "growx");
        jpBotonesTop = new JPanel(new MigLayout());
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        /*jtfBuscar = new JTextField();
         jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
         jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
         jpJtextFieldTop.add(jtfBuscar);*/
        jbBuscar = new JButton("Buscar");
        jbBorrar = new JButton("Borrar");
        jbBuscarDetalle = new JButton("Buscar por detalle");
        jbPedidosPendientes = new JButton("Buscar pedidos pendientes");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbPedidosPendientes, "span, growx, wrap");
        jpBotonesTop.add(jbBorrar,"growx");
        jpBotonesTop.add(jbBuscarDetalle, "span, growx");
        //jpTop.add(jpJtextFieldTop, "pushx");
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void initMid() {
        //Panel medio izquierda
        jtPedido = new JTable();
        jspEgresoCabecera = new JScrollPane(jtPedido);

        //panel medio derecha
        jtPedidoDetalle = new JTable();
        jspEgresoDetalle = new JScrollPane(jtPedidoDetalle);
        //creamos nuestro splitpane y agregamos los dos paneles del medio
        jspMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspEgresoCabecera, jspEgresoDetalle);
        jspMid.setDividerLocation(this.getWidth() / 2);
        jspMid.setOneTouchExpandable(true);
    }

    private void initBot() {
        jpBot = new JPanel();
        Insets inset = new Insets(10, 10, 10, 10);
        jbAgregar = new JButton("Crear pedido");
        jbAgregar.setMargin(inset);
        jbPagoPedido = new JButton("Pago de pedido");
        jbPagoPedido.setMargin(inset);
        jbCancelarPedido = new JButton("Cancelar pedido");
        jbCancelarPedido.setMargin(inset);
        jbDetalle = new JButton("Ver detalle");
        jbDetalle.setMargin(inset);
        jbResumen = new JButton("Ver resumen");
        jbResumen.setMargin(inset);
        jbCharts = new JButton("Diagramas");
        jbCharts.setMargin(inset);
        jpBot.add(jbAgregar);
        jpBot.add(jbPagoPedido);
        jpBot.add(jbCancelarPedido);
        jpBot.add(jbDetalle);
        jpBot.add(jbResumen);
        jpBot.add(jbCharts);
    }
}
