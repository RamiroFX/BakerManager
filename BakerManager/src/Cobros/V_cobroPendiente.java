/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Interface.CommonFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
public class V_cobroPendiente extends JDialog {

    //NORTH VARIABLES
    //COBRO
    public JButton jbBuscarCobro, jbBorrarCobro, jbBuscarPendiente, jbDetalleCobro,
            jbCliente, jbResumen;
    public JTextField jtfNroFactura, jtfCliente;
    public JComboBox jcbEmpleado, jcbEstadoPedido;
    private JPanel jpCobros, jpCobroTop, jpCobroBotonesTop, jpCobroBot;
    public JTable jtCobroCabecera, jtCobroDetalle;
    private JScrollPane jspCobroCabecera, jspCobroDetalle;
    private JSplitPane jspCobroMid;
    public JDateChooser jddInicioCobro, jddFinalCobro;

    public V_cobroPendiente(JFrame frame) {
        super(frame, "Cobros pendientes", true);
        initializeCobroVariables();
        constructWindows();
        constructLayout();
    }

    private void initializeCobroVariables() {
        Insets insets = new Insets(10, 10, 10, 10);
        jpCobroBotonesTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltrosCobro = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jbCliente = new JButton("Cliente");
        jtfCliente = new JTextField();
        jtfCliente.setPreferredSize(new Dimension(250, 10));
        jtfCliente.setEditable(false);
        jddInicioCobro = new JDateChooser();
        jddInicioCobro.setPreferredSize(new Dimension(150, 10));
        jddFinalCobro = new JDateChooser();
        jddFinalCobro.setPreferredSize(new Dimension(150, 10));
        jtfNroFactura = new JTextField(15);
        jpFiltrosCobro.add(jbCliente, "growx");
        jpFiltrosCobro.add(jtfCliente, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha inicio:"));
        jpFiltrosCobro.add(jddInicioCobro, "growx");
        jpFiltrosCobro.add(new JLabel("Nro. factura:"));
        jpFiltrosCobro.add(jtfNroFactura, "growx, wrap");
        jpFiltrosCobro.add(new JComponent() {
        });
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha final:"));
        jpFiltrosCobro.add(jddFinalCobro, "growx");
        jpFiltrosCobro.add(new JComponent() {
        });
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        jpFiltrosCobro.add(new JComponent() {
        }, "growx");
        //
        jpCobroBotonesTop = new JPanel(new MigLayout());
        jpCobroBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jbBuscarCobro = new JButton("Buscar");
        jbBuscarCobro.setName("buscar cobro");
        jbBuscarPendiente = new JButton("Pendiente");
        jbBorrarCobro = new JButton("Borrar");
        jpCobroBotonesTop.add(jbBuscarCobro);
        jpCobroBotonesTop.add(jbBorrarCobro,"wrap");
        jpCobroBotonesTop.add(jbBuscarPendiente, "span, growx");

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
        jbResumen = new JButton("Resumen");
        jbResumen.setName("resumen cobro pendiente");
        jbResumen.setFont(CommonFormat.fuente);
        jbResumen.setMargin(insets);

        jbDetalleCobro = new JButton("Ver detalle");
        jbDetalleCobro.setName("detalle cobro pendiente");
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
        jpCobroBot.add(jbDetalleCobro);
        jpCobroBot.add(jbResumen);
        jpCobros = new JPanel(new BorderLayout());
        jpCobros.add(jpCobroTop, BorderLayout.NORTH);
        jpCobros.add(jspCobroMid, BorderLayout.CENTER);
        jpCobros.add(jpCobroBot, BorderLayout.SOUTH);
    }

    private void constructWindows() {
        setSize(950, 600);
        setName("jdCobroPendiente");
    }

    private void constructLayout() {
        getContentPane().add(jpCobros, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }
}
