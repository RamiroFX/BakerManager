/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import Cobros.*;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Interface.CommonFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
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
public class V_pagoPendiente extends JDialog {

    //NORTH VARIABLES
    //COBRO
    public JButton jbBuscarPago, jbBorrarPago, jbBuscarPendiente, jbDetallePago,
            jbProveedor, jbResumen;
    public JTextField jtfNroFactura, jtfProveedor;
    public JComboBox jcbEmpleado, jcbEstadoPedido;
    private JPanel jpPagos, jpPagoTop, jpPagoBotonesTop, jpPagoBot;
    public JTable jtPagoCabecera, jtPagoDetalle;
    private JScrollPane jspPagoCabecera, jspPagoDetalle;
    private JSplitPane jspPagoMid;
    public JDateChooser jddInicioPago, jddFinalPago;

    public V_pagoPendiente(JFrame frame) {
        super(frame, "Pagos pendientes", true);
        initializeCobroVariables();
        constructWindows();
        constructLayout();
    }

    private void initializeCobroVariables() {
        Insets insets = new Insets(10, 10, 10, 10);
        jpPagoBotonesTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltrosCobro = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jbProveedor = new JButton("Proveedor");
        jtfProveedor = new JTextField();
        jtfProveedor.setPreferredSize(new Dimension(250, 10));
        jtfProveedor.setEditable(false);
        jddInicioPago = new JDateChooser();
        jddInicioPago.setPreferredSize(new Dimension(150, 10));
        jddFinalPago = new JDateChooser();
        jddFinalPago.setPreferredSize(new Dimension(150, 10));
        jtfNroFactura = new JTextField(15);
        jpFiltrosCobro.add(jbProveedor, "growx");
        jpFiltrosCobro.add(jtfProveedor, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha inicio:"));
        jpFiltrosCobro.add(jddInicioPago, "growx");
        jpFiltrosCobro.add(new JLabel("Nro. factura:"));
        jpFiltrosCobro.add(jtfNroFactura, "growx, wrap");
        jpFiltrosCobro.add(new JComponent() {
        });
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha final:"));
        jpFiltrosCobro.add(jddFinalPago, "growx");
        jpFiltrosCobro.add(new JComponent() {
        });
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        //
        jpPagoBotonesTop = new JPanel(new MigLayout());
        jpPagoBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jbBuscarPago = new JButton("Buscar");
        jbBuscarPago.setName("buscar cobro");
        jbBuscarPendiente = new JButton("Pendiente");
        jbBorrarPago = new JButton("Borrar");
        jpPagoBotonesTop.add(jbBuscarPago);
        jpPagoBotonesTop.add(jbBorrarPago,"wrap");
        jpPagoBotonesTop.add(jbBuscarPendiente, "span, growx");

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
        jbResumen = new JButton("Resumen");
        jbResumen.setName("resumen cobro pendiente");
        jbResumen.setFont(CommonFormat.fuente);
        jbResumen.setMargin(insets);

        jbDetallePago = new JButton("Ver detalle");
        jbDetallePago.setName("detalle cobro pendiente");
        jbDetallePago.setFont(CommonFormat.fuente);
        jbDetallePago.setEnabled(false);
        jbDetallePago.setMargin(insets);
        TitledBorder optionBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones");
        TitledBorder filterBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda");
        jpPagoTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        jpPagoTop.setBorder(filterBorder);
        jpPagoTop.add(jpFiltrosCobro);
        jpPagoTop.add(jpPagoBotonesTop);
        jpPagoBot = new JPanel();
        jpPagoBot.setBorder(optionBorder);
        jpPagoBot.add(jbDetallePago);
        jpPagoBot.add(jbResumen);
        jpPagos = new JPanel(new BorderLayout());
        jpPagos.add(jpPagoTop, BorderLayout.NORTH);
        jpPagos.add(jspPagoMid, BorderLayout.CENTER);
        jpPagos.add(jpPagoBot, BorderLayout.SOUTH);
    }

    private void constructWindows() {
        setSize(950, 600);
        setName("jdCobroPendiente");
    }

    private void constructLayout() {
        getContentPane().add(jpPagos, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }
}
