/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros_Pagos;

import Interface.CommonFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    public JTextField jtfEmpCobro;
    public JDateChooser jddInicioCobro, jddFinalCobro;
    public JButton jbEmpCobro, jbBuscarCobro, jbBorrarCobro;
    private JPanel jpBotonesTopCobro;
    //PAGO
    public JTextField jtfEmpPago;
    public JDateChooser jddInicioPago, jddFinalPago;
    public JButton jbEmpPago, jbBuscarPago, jbBorrarPago;
    private JPanel jpBotonesTopPago;

    //CENTER VARIABLES
    public JTabbedPane jtpMid;
    public JScrollPane jspCobro, jspPago;
    public JTable jtCobro, jtPago;
    //SOUTH VARIABLES
    public JButton jbCobro, jbPago, jbDetalleCobro,
            jbDetallePago;

    public V_gestionCobroPago() {
        super("Cobros y pagos", true, true, true, true);
        initializeVariables();
        constructWindows();
        constructLayout();
    }

    private void initializeVariables() {
        jpBotonesTopCobro = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltrosCobro = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jbEmpCobro = new JButton("Funcionario");
        jtfEmpCobro = new JTextField();
        jtfEmpCobro.setPreferredSize(new Dimension(250, 10));
        jtfEmpCobro.setEditable(false);
        jddInicioCobro = new JDateChooser();
        jddInicioCobro.setPreferredSize(new Dimension(150, 10));
        jddFinalCobro = new JDateChooser();
        jddFinalCobro.setPreferredSize(new Dimension(150, 10));
        jpFiltrosCobro.add(jbEmpCobro, "growx");
        jpFiltrosCobro.add(jtfEmpCobro, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha inicio:"));
        jpFiltrosCobro.add(jddInicioCobro, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha final:"));
        jpFiltrosCobro.add(jddFinalCobro, "growx");
        jpBotonesTopCobro = new JPanel(new MigLayout());
        jpBotonesTopCobro.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jbBuscarCobro = new JButton("Buscar");
        jbBuscarCobro.setName("buscar venta");
        jbBorrarCobro = new JButton("Borrar");
        jpBotonesTopCobro.add(jbBuscarCobro);
        jpBotonesTopCobro.add(jbBorrarCobro);
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

        //MID VARIABLES
        jtpMid = new JTabbedPane();
        jtCobro = new JTable();
        jspCobro = new JScrollPane(jtCobro);
        jtPago = new JTable();
        jspPago = new JScrollPane(jtPago);

        //SOUTH VARIABLES        
        Insets insets = new Insets(10, 10, 10, 10);
        jbCobro = new JButton("Cobrar");
        jbCobro.setName("cobrar venta");
        jbCobro.setFont(CommonFormat.fuente);
        jbCobro.setEnabled(false);
        jbCobro.setMargin(insets);
        jbPago = new JButton("Pagar");
        jbPago.setName("cobrar venta");
        jbPago.setFont(CommonFormat.fuente);
        jbPago.setEnabled(false);
        jbPago.setMargin(insets);
        jbDetalleCobro = new JButton("Ver detalle");
        jbDetalleCobro.setName("detalle cobro");
        jbDetalleCobro.setFont(CommonFormat.fuente);
        jbDetalleCobro.setEnabled(false);
        jbDetalleCobro.setMargin(insets);
        jbDetallePago = new JButton("Ver detalle");
        jbDetallePago.setName("detalle pago");
        jbDetallePago.setFont(CommonFormat.fuente);
        jbDetallePago.setEnabled(false);
        jbDetallePago.setMargin(insets);

        TitledBorder optionBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones");
        TitledBorder filterBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda");        
        JPanel jpBotonesNorteCobro = new JPanel();
        jpBotonesNorteCobro.setBorder(filterBorder);
        jpBotonesNorteCobro.add(jpFiltrosCobro);
        jpBotonesNorteCobro.add(jpBotonesTopCobro);
        JPanel jpBotonesSurCobro = new JPanel();
        jpBotonesSurCobro.setBorder(optionBorder);
        jpBotonesSurCobro.add(jbCobro);
        jpBotonesSurCobro.add(jbDetalleCobro);
        JPanel jpCobros = new JPanel(new BorderLayout());
        jpCobros.add(jpBotonesNorteCobro, BorderLayout.NORTH);
        jpCobros.add(jspCobro, BorderLayout.CENTER);
        jpCobros.add(jpBotonesSurCobro, BorderLayout.SOUTH);

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

        jtpMid.addTab("COBROS", jpCobros);
        jtpMid.addTab("PAGOS", jpPagos);
    }

    private void constructWindows() {
        setSize(950, 600);
        setName("jifGestionCobroPago");
    }

    private void constructLayout() {
        getContentPane().add(jtpMid, BorderLayout.CENTER);
    }
}
