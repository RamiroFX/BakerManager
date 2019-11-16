/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros_Pagos;

import Entities.E_tipoOperacion;
import Interface.CommonFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_gestionCobroPago extends JInternalFrame {

    //NORTH VARIABLES
    //COBRO
    public JButton jbBuscarCobro, jbBorrarCobro, jbDetalleCobro,
            jbCliente, jbEmpCobro, jbCobro, jbCobroPendientes;
    public JTextField jtfNroFactura, jtfCliente, jtfEmpCobro;
    public JComboBox jcbEmpleado, jcbEstadoPedido;
    public JComboBox<E_tipoOperacion> jcbCondVenta;
    private JPanel jpCobros, jpCobroTop, jpCobroBotonesTop, jpCobroBot;
    public JTable jtCobroCabecera, jtCobroDetalle;
    private JScrollPane jspCobroCabecera, jspCobroDetalle;
    private JSplitPane jspCobroMid;
    public JDateChooser jddInicioCobro, jddFinalCobro;
    //PAGO
    public JTextField jtfEmpPago;
    public JDateChooser jddInicioPago, jddFinalPago;
    public JButton jbEmpPago, jbBuscarPago, jbBorrarPago;
    private JPanel jpBotonesTopPago;

    //CENTER VARIABLES
    public JTabbedPane jtpMid;
    public JScrollPane jspPago;
    public JTable jtPago;
    //SOUTH VARIABLES
    public JButton jbPago, jbDetallePago;

    public V_gestionCobroPago() {
        super("Cobros y pagos", true, true, true, true);
        initializeCobroVariables();
        initializeVariables();
        initializeGlobalVariables();

        constructWindows();
        constructLayout();
    }

    private void initializeGlobalVariables() {

        jtpMid = new JTabbedPane();

        jtpMid.addTab("COBROS", jpCobros);
        //jtpMid.addTab("PAGOS", jpPagos);
    }

    private void initializeCobroVariables() {
        Insets insets = new Insets(10, 10, 10, 10);
        jpCobroBotonesTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltrosCobro = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jbEmpCobro = new JButton("Funcionario");
        jbCliente = new JButton("Cliente");
        jtfEmpCobro = new JTextField();
        jtfEmpCobro.setPreferredSize(new Dimension(250, 10));
        jtfEmpCobro.setEditable(false);
        jtfCliente = new JTextField();
        jtfCliente.setPreferredSize(new Dimension(250, 10));
        jtfCliente.setEditable(false);
        jddInicioCobro = new JDateChooser();
        jddInicioCobro.setPreferredSize(new Dimension(150, 10));
        jddFinalCobro = new JDateChooser();
        jddFinalCobro.setPreferredSize(new Dimension(150, 10));
        jcbCondVenta = new JComboBox<E_tipoOperacion>();
        jtfNroFactura = new JFormattedTextField();
        jpFiltrosCobro.add(jbCliente, "growx");
        jpFiltrosCobro.add(jtfCliente, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha inicio:"));
        jpFiltrosCobro.add(jddInicioCobro, "growx");
        jpFiltrosCobro.add(new JLabel("Cond. compra:"));
        jpFiltrosCobro.add(jcbCondVenta, "wrap");
        jpFiltrosCobro.add(jbEmpCobro);
        jpFiltrosCobro.add(jtfEmpCobro, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha final:"));
        jpFiltrosCobro.add(jddFinalCobro, "growx");
        jpFiltrosCobro.add(new JLabel("Nro. factura:"));
        jpFiltrosCobro.add(jtfNroFactura, "growx");
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        //
        jpCobroBotonesTop = new JPanel(new MigLayout());
        jpCobroBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jbBuscarCobro = new JButton("Buscar");
        jbBuscarCobro.setName("buscar cobro");
        jbCobroPendientes = new JButton("Cobro pendiente");
        jbCobroPendientes.setName("cobro pendiente");
        jbBorrarCobro = new JButton("Borrar");
        jpCobroBotonesTop.add(jbBorrarCobro);
        jpCobroBotonesTop.add(jbBuscarCobro);
        jpCobroBotonesTop.add(jbBorrarCobro, "span, growx, wrap");
        jpCobroBotonesTop.add(jbCobroPendientes, "span, growx");

        //MID VARIABLES
        jtCobroCabecera = new JTable();
        jtCobroCabecera.getTableHeader().setReorderingAllowed(false);
        jspCobroCabecera = new JScrollPane(jtCobroCabecera);
        jtCobroDetalle = new JTable();
        jspCobroDetalle = new JScrollPane(jtCobroDetalle);
        jspCobroMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspCobroCabecera, jspCobroDetalle);
        //jspCobroMid.setDividerLocation(this.getWidth() / 2);
        jspCobroMid.setOneTouchExpandable(true);
        //SOUTH VARIABLES
        jbCobro = new JButton("Cobrar");
        jbCobro.setName("cobrar venta");
        jbCobro.setFont(CommonFormat.fuente);
        jbCobro.setEnabled(false);
        jbCobro.setMargin(insets);

        jbDetalleCobro = new JButton("Ver detalle");
        jbDetalleCobro.setName("detalle cobro");
        jbDetalleCobro.setFont(CommonFormat.fuente);
        jbDetalleCobro.setEnabled(false);
        jbDetalleCobro.setMargin(insets);
        TitledBorder optionBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones");
        TitledBorder filterBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda");
        jpCobroTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        jpCobroTop.setBorder(filterBorder);
        jpCobroTop.add(jpFiltrosCobro);
        jpCobroTop.add(jpCobroBotonesTop);
        jpCobroBot = new JPanel();
        jpCobroBot.setBorder(optionBorder);
        jpCobroBot.add(jbCobro);
        jpCobroBot.add(jbDetalleCobro);
        jpCobros = new JPanel(new BorderLayout());
        jpCobros.add(jpCobroTop, BorderLayout.NORTH);
        jpCobros.add(jspCobroMid, BorderLayout.CENTER);
        jpCobros.add(jpCobroBot, BorderLayout.SOUTH);
    }

    private void initializeVariables() {
        Insets insets = new Insets(10, 10, 10, 10);
        //PAGO NORTH VARIABLES
        jpBotonesTopPago = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltrosPago = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jbEmpPago = new JButton("Funcionario");
        jtfEmpPago = new JTextField();
        jtfEmpPago.setPreferredSize(new Dimension(250, 10));
        jtfEmpPago.setEditable(false);
        jddInicioPago = new JDateChooser();
        jddInicioPago.setPreferredSize(new Dimension(150, 10));
        jddFinalPago = new JDateChooser();
        jddFinalPago.setPreferredSize(new Dimension(150, 10));
        jpFiltrosPago.add(jbEmpPago, "growx");
        jpFiltrosPago.add(jtfEmpPago, "growx");
        jpFiltrosPago.add(new JLabel("Fecha inicio:"));
        jpFiltrosPago.add(jddInicioPago, "growx");
        jpFiltrosPago.add(new JLabel("Fecha final:"));
        jpFiltrosPago.add(jddFinalPago, "growx");
        jpBotonesTopPago = new JPanel(new MigLayout());
        jpBotonesTopPago.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jbBuscarPago = new JButton("Buscar");
        jbBuscarPago.setName("buscar venta");
        jbBorrarPago = new JButton("Borrar");
        jpBotonesTopPago.add(jbBuscarPago);
        jpBotonesTopPago.add(jbBorrarPago);

        jtPago = new JTable();
        jspPago = new JScrollPane(jtPago);

        jbPago = new JButton("Pagar");
        jbPago.setName("cobrar venta");
        jbPago.setFont(CommonFormat.fuente);
        jbPago.setEnabled(false);
        jbPago.setMargin(insets);
        jbDetallePago = new JButton("Ver detalle");
        jbDetallePago.setName("detalle pago");
        jbDetallePago.setFont(CommonFormat.fuente);
        jbDetallePago.setEnabled(false);
        jbDetallePago.setMargin(insets);

        TitledBorder optionBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones");
        TitledBorder filterBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda");

        JPanel jpBotonesNortePago = new JPanel();
        jpBotonesNortePago.setBorder(filterBorder);
        jpBotonesNortePago.add(jpFiltrosPago);
        jpBotonesNortePago.add(jpBotonesTopPago);
        JPanel jpBotonesSurPago = new JPanel();
        jpBotonesSurPago.setBorder(optionBorder);
        jpBotonesSurPago.add(jbPago);
        jpBotonesSurPago.add(jbDetallePago);
        JPanel jpPagos = new JPanel(new BorderLayout());
        jpPagos.add(jpBotonesNortePago, BorderLayout.NORTH);
        jpPagos.add(jspPago, BorderLayout.CENTER);
        jpPagos.add(jpBotonesSurPago, BorderLayout.SOUTH);
    }

    private void constructWindows() {
        setSize(950, 600);
        setName("jifGestionCobroPago");
    }

    private void constructLayout() {
        getContentPane().add(jtpMid, BorderLayout.CENTER);
    }
}
