/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

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
public class V_gestionCobro extends JInternalFrame {

    //NORTH VARIABLES
    //COBRO
    public JButton jbBuscarCobro, jbBorrarCobro, jbDetalleCobro, jbAnular,
            jbCliente, jbEmpCobro, jbCobro, jbCobroPendientes, jbResumen, jbBanco,
            jbCheques, jbMasOpciones;
    public JTextField jtfNroRecibo, jtfCliente, jtfEmpCobro;
    public JComboBox jcbEmpleado, jcbEstadoPedido;
    public JComboBox<Estado> jcbEstado;
    private JPanel jpCobros, jpCobroTop, jpCobroBotonesTop, jpCobroBot;
    public JTable jtCobroCabecera, jtCobroDetalle;
    private JScrollPane jspCobroCabecera, jspCobroDetalle;
    private JSplitPane jspCobroMid;
    public JDateChooser jddInicioCobro, jddFinalCobro;

    public V_gestionCobro() {
        super("Cobros a clientes", true, true, true, true);
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
        jcbEstado = new JComboBox<Estado>();
        jtfNroRecibo = new JFormattedTextField();
        jpFiltrosCobro.add(jbCliente, "growx");
        jpFiltrosCobro.add(jtfCliente, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha inicio:"));
        jpFiltrosCobro.add(jddInicioCobro, "growx");
        jpFiltrosCobro.add(new JLabel("Estado:"));
        jpFiltrosCobro.add(jcbEstado, "wrap");
        jpFiltrosCobro.add(jbEmpCobro);
        jpFiltrosCobro.add(jtfEmpCobro, "growx");
        jpFiltrosCobro.add(new JLabel("Fecha final:"));
        jpFiltrosCobro.add(jddFinalCobro, "growx");
        jpFiltrosCobro.add(new JLabel("Nro. recibo:"));
        jpFiltrosCobro.add(jtfNroRecibo, "growx");
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
        jbCobroPendientes.setName("buscar cobro pendiente");
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
        jbCobro.setName("crear cobro");
        jbCobro.setFont(CommonFormat.fuente);
        jbCobro.setMargin(insets);
        jbAnular = new JButton("Anular");
        jbAnular.setName("anular cobro");
        jbAnular.setFont(CommonFormat.fuente);
        jbAnular.setMargin(insets);
        jbResumen = new JButton("Resumen");
        jbResumen.setName("resumen cobro");
        jbResumen.setFont(CommonFormat.fuente);
        jbResumen.setMargin(insets);
        jbBanco = new JButton("Bancos");
        jbBanco.setName("gestión bancos cobro");
        jbBanco.setFont(CommonFormat.fuente);
        jbBanco.setMargin(insets);
        jbCheques = new JButton("Cheques pendientes");
        jbCheques.setName("cheques pendientes cobro");
        jbCheques.setFont(CommonFormat.fuente);
        jbCheques.setMargin(insets);
        jbMasOpciones = new JButton("Más opciones");
        jbMasOpciones.setFont(CommonFormat.fuente);
        jbMasOpciones.setMargin(insets);

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
        jpCobroBot.add(jbAnular);
        jpCobroBot.add(jbResumen);
        jpCobroBot.add(jbBanco);
        jpCobroBot.add(jbCheques);
        jpCobroBot.add(jbMasOpciones);
        jpCobros = new JPanel(new BorderLayout());
        jpCobros.add(jpCobroTop, BorderLayout.NORTH);
        jpCobros.add(jspCobroMid, BorderLayout.CENTER);
        jpCobros.add(jpCobroBot, BorderLayout.SOUTH);
    }

    private void constructWindows() {
        setSize(950, 600);
        setName("jifGestionCobro");
    }

    private void constructLayout() {
        getContentPane().add(jpCobros, BorderLayout.CENTER);
    }
}
