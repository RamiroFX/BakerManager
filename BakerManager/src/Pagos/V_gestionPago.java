/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import Entities.Estado;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_gestionPago extends JInternalFrame {

    //NORTH VARIABLES
    //PAGO
    public JButton jbBuscarPago, jbBorrarPago, jbDetallePago, jbAnular,
            jbProveedor, jbEmpCobro, jbPago, jbPagoPendientes, jbResumen, jbBanco,
            jbCheques;
    public JTextField jtfNroRecibo, jtfCliente, jtfEmpCobro;
    public JComboBox jcbEmpleado, jcbEstadoPedido;
    public JComboBox<Estado> jcbEstado;
    private JPanel jpPagos, jpPagoTop, jpPagosBotonesTop, jpPagosBot;
    public JTable jtPagoCabecera, jtPagoDetalle;
    private JScrollPane jspPagoCabecera, jspPagoDetalle;
    private JSplitPane jspPagoMid;
    public JDateChooser jddInicioPago, jddFinalPago;

    public V_gestionPago() {
        super("Pago a proveedores", true, true, true, true);
        initializeCobroVariables();
        constructWindows();
        constructLayout();
    }

    private void initializeCobroVariables() {
        Insets insets = new Insets(10, 10, 10, 10);
        jpPagosBotonesTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltrosCobro = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jbEmpCobro = new JButton("Funcionario");
        jbProveedor = new JButton("Proveedor");
        jtfEmpCobro = new JTextField();
        jtfEmpCobro.setPreferredSize(new Dimension(250, 10));
        jtfEmpCobro.setEditable(false);
        jtfCliente = new JTextField();
        jtfCliente.setPreferredSize(new Dimension(250, 10));
        jtfCliente.setEditable(false);
        jddInicioPago = new JDateChooser();
        jddInicioPago.setPreferredSize(new Dimension(150, 10));
        jddFinalPago = new JDateChooser();
        jddFinalPago.setPreferredSize(new Dimension(150, 10));
        jcbEstado = new JComboBox<Estado>();
        jtfNroRecibo = new JFormattedTextField();
        jpFiltrosCobro.add(jbProveedor, "growx");
        jpFiltrosCobro.add(jtfCliente, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha inicio:"));
        jpFiltrosCobro.add(jddInicioPago, "growx");
        jpFiltrosCobro.add(new JLabel("Estado:"));
        jpFiltrosCobro.add(jcbEstado, "wrap");
        jpFiltrosCobro.add(jbEmpCobro);
        jpFiltrosCobro.add(jtfEmpCobro, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha final:"));
        jpFiltrosCobro.add(jddFinalPago, "growx");
        jpFiltrosCobro.add(new JLabel("Nro. recibo:"));
        jpFiltrosCobro.add(jtfNroRecibo, "growx");
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        //
        jpPagosBotonesTop = new JPanel(new MigLayout());
        jpPagosBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jbBuscarPago = new JButton("Buscar");
        jbBuscarPago.setName("buscar pago");
        jbPagoPendientes = new JButton("Pago pendiente");
        jbPagoPendientes.setName("buscar pago pendiente");
        jbBorrarPago = new JButton("Borrar");
        jpPagosBotonesTop.add(jbBorrarPago);
        jpPagosBotonesTop.add(jbBuscarPago);
        jpPagosBotonesTop.add(jbBorrarPago, "span, growx, wrap");
        jpPagosBotonesTop.add(jbPagoPendientes, "span, growx");

        //MID VARIABLES
        jtPagoCabecera = new JTable();
        jtPagoCabecera.getTableHeader().setReorderingAllowed(false);
        jspPagoCabecera = new JScrollPane(jtPagoCabecera);
        jtPagoDetalle = new JTable();
        jspPagoDetalle = new JScrollPane(jtPagoDetalle);
        jspPagoMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspPagoCabecera, jspPagoDetalle);
        //jspCobroMid.setDividerLocation(this.getWidth() / 2);
        jspPagoMid.setOneTouchExpandable(true);
        //SOUTH VARIABLES
        jbPago = new JButton("Pagar");
        jbPago.setName("crear pago");
        jbPago.setFont(CommonFormat.fuente);
        jbPago.setMargin(insets);
        jbAnular = new JButton("Anular");
        jbAnular.setName("anular pago");
        jbAnular.setFont(CommonFormat.fuente);
        jbAnular.setMargin(insets);
        jbResumen = new JButton("Resumen");
        jbResumen.setName("resumen pago");
        jbResumen.setFont(CommonFormat.fuente);
        jbResumen.setMargin(insets);
        jbBanco = new JButton("Bancos");
        jbBanco.setName("gestión bancos pago");
        jbBanco.setFont(CommonFormat.fuente);
        jbBanco.setMargin(insets);
        jbCheques = new JButton("Cheques pendientes");
        jbCheques.setName("cheques pendientes pago");
        jbCheques.setFont(CommonFormat.fuente);
        jbCheques.setMargin(insets);

        jbDetallePago = new JButton("Ver detalle");
        jbDetallePago.setName("detalle pago");
        jbDetallePago.setFont(CommonFormat.fuente);
        jbDetallePago.setEnabled(false);
        jbDetallePago.setMargin(insets);
        TitledBorder optionBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones");
        TitledBorder filterBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda");
        jpPagoTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        jpPagoTop.setBorder(filterBorder);
        jpPagoTop.add(jpFiltrosCobro);
        jpPagoTop.add(jpPagosBotonesTop);
        jpPagosBot = new JPanel();
        jpPagosBot.setBorder(optionBorder);
        jpPagosBot.add(jbPago);
        jpPagosBot.add(jbDetallePago);
        jpPagosBot.add(jbAnular);
        jpPagosBot.add(jbResumen);
        jpPagosBot.add(jbBanco);
        jpPagosBot.add(jbCheques);
        jpPagos = new JPanel(new BorderLayout());
        jpPagos.add(jpPagoTop, BorderLayout.NORTH);
        jpPagos.add(jspPagoMid, BorderLayout.CENTER);
        jpPagos.add(jpPagosBot, BorderLayout.SOUTH);
    }

    private void constructWindows() {
        setSize(950, 600);
        setName("jifGestionPago");
    }

    private void constructLayout() {
        getContentPane().add(jpPagos, BorderLayout.CENTER);
    }
}
