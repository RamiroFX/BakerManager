/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Entities.E_tipoOperacion;
import Entities.Estado;
import Interface.CommonFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
public class V_gestionVentas extends JInternalFrame {

    public JButton jbBuscar, jbBuscarDetalle, jbBorrar, jbAgregar, jbDetalle,
            jbResumen, jbCliente, jbVendedor, jbAnular, jbFacturar,
            jbHistorialFacturacion, jbNotasCredito, jbMasOpciones;
    public JTextField jtfNroFactura, jtfCliente, jtfEmpleado;
    public JComboBox<E_tipoOperacion> jcbCondVenta;
    public JComboBox<Estado> jcbEstado;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JTable jtIngresoDetalle, jtIngresoCabecera;
    private JScrollPane jspEgresoDetalle;
    private JSplitPane jspMid;
    public JDateChooser jddInicio, jddFinal;

    public V_gestionVentas() {
        super("Ventas", true, true, true, true);
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
        jbVendedor = new JButton("Vendedor");
        jtfEmpleado = new JTextField();
        jtfEmpleado.setPreferredSize(new Dimension(250, 10));
        jtfEmpleado.setEditable(false);
        jcbCondVenta = new JComboBox();
        jtfNroFactura = new JTextField();
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jcbEstado = new JComboBox<>();
        jpFiltros.add(jbCliente, "growx");
        jpFiltros.add(jtfCliente, "growx");
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx");
        jpFiltros.add(new JLabel("Cond. compra:"));
        jpFiltros.add(jcbCondVenta, "wrap");
        jpFiltros.add(jbVendedor);
        jpFiltros.add(jtfEmpleado, "growx");
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx");
        jpFiltros.add(new JLabel("Nro. factura:"));
        jpFiltros.add(jtfNroFactura, "growx, wrap");
        jpFiltros.add(new JComponent() {
        });
        jpFiltros.add(new JComponent() {
        });
        jpFiltros.add(new JComponent() {
        });
        jpFiltros.add(new JComponent() {
        });
        jpFiltros.add(new JLabel("Estado"));
        jpFiltros.add(jcbEstado, "growx");
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
        jpTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda"));
    }

    private void initMid() {
        //Panel medio izquierda
        jtIngresoCabecera = new JTable();
        jtIngresoCabecera.getTableHeader().setReorderingAllowed(false);
        JScrollPane jspIngresoCabecera = new JScrollPane(jtIngresoCabecera);

        //panel medio derecha
        jtIngresoDetalle = new JTable();
        jtIngresoDetalle.getTableHeader().setReorderingAllowed(false);
        jspEgresoDetalle = new JScrollPane(jtIngresoDetalle);
        //creamos nuestro splitpane y agregamos los dos paneles del medio
        jspMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspIngresoCabecera, jspEgresoDetalle);
        jspMid.setDividerLocation(this.getWidth() / 2);
        jspMid.setOneTouchExpandable(true);
    }

    private void initBot() {
        jpBot = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAgregar = new JButton("Crear venta [F1]");
        jbAgregar.setName("crear venta");
        jbAgregar.setFont(CommonFormat.fuente);
        jbAgregar.setMargin(insets);
        jbDetalle = new JButton("Ver detalle");
        jbDetalle.setName("detalle venta");
        jbDetalle.setFont(CommonFormat.fuente);
        jbDetalle.setMargin(insets);
        jbAnular = new JButton("Anular");
        jbAnular.setName("anular venta");
        jbAnular.setFont(CommonFormat.fuente);
        jbAnular.setMargin(insets);
        jbResumen = new JButton("Ver resumen [F2]");
        jbResumen.setName("resumen venta");
        jbResumen.setMargin(insets);
        jbResumen.setFont(CommonFormat.fuente);
        jbResumen.setEnabled(false);
        jbFacturar = new JButton("Facturar");
        jbFacturar.setName("facturar venta");
        jbFacturar.setMargin(insets);
        jbFacturar.setFont(CommonFormat.fuente);
        jbHistorialFacturacion = new JButton("Historial de facturación");
        jbHistorialFacturacion.setName("historial facturacion");
        jbHistorialFacturacion.setMargin(insets);
        jbHistorialFacturacion.setFont(CommonFormat.fuente);
        jbNotasCredito = new JButton("Notas de crédito");
        jbNotasCredito.setName("Gestión notas crédito");
        jbNotasCredito.setMargin(insets);
        jbNotasCredito.setFont(CommonFormat.fuente);
        jbMasOpciones = new JButton("Más opciones");
        jbMasOpciones.setFont(CommonFormat.fuente);
        jbMasOpciones.setMargin(insets);
        //jbFacturacion.setEnabled(false);
        jpBot.add(jbAgregar);
        jpBot.add(jbDetalle);
        jpBot.add(jbAnular);
        jpBot.add(jbResumen);
        jpBot.add(jbFacturar);
        jpBot.add(jbMasOpciones);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
