/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import Interface.CommonFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JDialog;
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
public class V_cajaDetalle extends JDialog {

    public JButton jbBuscar, jbBorrar, jbAceptar, jbCancelar,
            jbEmpleado;
    public JTextField jtfEmpleado;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JScrollPane jspVentas, jspCompras, jspPagos, jspCobros;
    public JTable jtVentas, jtCompras, jtPagos, jtCobros;
    public JDateChooser jddInicio, jddFinal;
    private JTabbedPane jtpMovimientos;

    public V_cajaDetalle(JDialog owner) {
        super(owner, "Detalle de caja", true);
        setSize(950, 600);
        setName("jdDetalleCaja");
        initializeVariable();
        constructLayout();
    }

    private void initializeVariable() {
        initTop();
        initMid();
        initBot();
    }

    private void constructLayout() {
        getContentPane().add(jpTop, "North");
        getContentPane().add(jtpMovimientos, "Center");
        getContentPane().add(jpBot, "South");

    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jbEmpleado = new JButton("Funcionario");
        jtfEmpleado = new JTextField();
        jtfEmpleado.setPreferredSize(new Dimension(250, 10));
        jtfEmpleado.setEditable(false);
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jpFiltros.add(jbEmpleado, "growx");
        jpFiltros.add(jtfEmpleado, "growx");
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx");
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx");
        jpBotonesTop = new JPanel(new MigLayout());
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        /*jtfBuscar = new JTextField();
         jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
         jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
         jpJtextFieldTop.add(jtfBuscar);*/
        jbBuscar = new JButton("Buscar");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar);
        //jpTop.add(jpJtextFieldTop, "pushx");
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda"));
    }

    private void initMid() {
        jtVentas = new JTable();
        jspVentas = new JScrollPane(jtVentas);
        jtCompras = new JTable();
        jspCompras = new JScrollPane(jtCompras);
        jtCobros = new JTable();
        jspCobros = new JScrollPane(jtCobros);
        jtPagos = new JTable();
        jspPagos = new JScrollPane(jtPagos);

        jtpMovimientos = new JTabbedPane();
        jtpMovimientos.addTab("Ventas", jspVentas);
        jtpMovimientos.addTab("Compras", jspCompras);
        jtpMovimientos.addTab("Cobros", jspCobros);
        jtpMovimientos.addTab("Pagos", jspPagos);
    }

    private void initBot() {
        jpBot = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAceptar = new JButton("Aceptar");
        jbAceptar.setFont(CommonFormat.fuente);
        jbAceptar.setEnabled(false);
        jbAceptar.setMargin(insets);
        jbCancelar = new JButton("Cancelar");
        jbCancelar.setFont(CommonFormat.fuente);
        jbCancelar.setEnabled(false);
        jbCancelar.setMargin(insets);
        jpBot.add(jbAceptar);
        jpBot.add(jbCancelar);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
