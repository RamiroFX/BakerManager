/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_cobroAnticipado extends JDialog {

    private static final String INGRESAR_COD_PROD = "Ingresar número de factura";
    private static final String COD_PROD = "Número de factura";

    //NORTE
    JPanel jpNorth;
    public JTextField jtfCliente, jtfFuncionario, jtfNroRecibo, jtfClieRuc;
    public JButton jbCliente, jbFuncionario;
    public JLabel jlNroRecibo, jlFechaCobro;
    public JDateChooser jdcFechaCobro;
    //CENTRO
    JPanel jpCenter;
    public JTable jtReciboDetalle;
    public JScrollPane jspReciboDetalle;
    public JButton jbAgregarMonto, jbModificarDetalle, jbEliminarDetalle;
    public JLabel jlTotal;
    public JFormattedTextField jftTotal;
    //SUR
    JPanel jpSouth;
    public JButton jbAceptar, jbImprimir, jbSalir;

    public V_cobroAnticipado(JFrame frame) {
        super(frame, "Crear cobro anticipado", JDialog.ModalityType.APPLICATION_MODAL);
        setSize(950, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(frame);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void initComponents() {
        initNorth();
        initCenter();
        initSouth();
        establecerNombres();
    }

    private void establecerNombres() {
        jbAceptar.setName("jbAceptarV_crearCobro");
        jbCliente.setName("jbClienteV_crearCobro");
        jtfCliente.setName("jtfClienteV_crearCobro");
        jtfNroRecibo.setName("jtfNroReciboV_crearCobro");
        jbFuncionario.setName("jbFuncionarioV_crearCobro");
        jtfFuncionario.setName("jtfFuncionarioV_crearCobro");
        jdcFechaCobro.setName("jdcFechaCobroV_crearCobro");
        jbAgregarMonto.setName("jbAgregarFacturaV_crearCobro");
        jbEliminarDetalle.setName("jbEliminarDetalleV_crearCobro");
        jbModificarDetalle.setName("jbModificarDetalleV_crearCobro");
        jbSalir.setName("jbSalirV_crearCobro");
        jbImprimir.setName("jbImprimirV_crearCobro");
    }

    private void initNorth() {
        jpNorth = new JPanel(new MigLayout());
        jbCliente = new JButton("Agregar cliente [F3] ");
        jbFuncionario = new JButton("Agregar cobrador [F5]");
        jtfCliente = new JTextField(30);
        jtfCliente.setEditable(false);
        jtfFuncionario = new JTextField(30);
        jtfFuncionario.setEditable(false);
        jlNroRecibo = new JLabel("Nro. Recibo");
        jtfNroRecibo = new JTextField(20);
        jlFechaCobro = new JLabel("Fecha");
        jdcFechaCobro = new JDateChooser();
        jdcFechaCobro.setPreferredSize(new Dimension(150, 20));
        jpNorth.add(jbCliente, "growx");
        jpNorth.add(jtfCliente);
        jpNorth.add(jlNroRecibo);
        jpNorth.add(jtfNroRecibo);
        jpNorth.add(jlFechaCobro);
        jpNorth.add(jdcFechaCobro, "wrap");
        jpNorth.add(jbFuncionario);
        jpNorth.add(jtfFuncionario);
        jpNorth.add(new JComponent() {
        });
        jpNorth.add(new JComponent() {
        });
        jpNorth.add(new JComponent() {
        });
        jpNorth.add(new JComponent() {
        });
    }

    private void initCenter() {
        int columns = 8;
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtReciboDetalle = new JTable();
        jtReciboDetalle.getTableHeader().setReorderingAllowed(false);
        jtReciboDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspReciboDetalle = new JScrollPane(jtReciboDetalle);
        jbAgregarMonto = new JButton("Agregar monto [F4]");
        jbModificarDetalle = new JButton("Modificar detalle");
        jbEliminarDetalle = new JButton("Eliminar detalle");
        JPanel jpSouthAux = new JPanel(new MigLayout());
        jpSouthAux.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAux.add(jbAgregarMonto, "growx, wrap");
        jpSouthAux.add(jbModificarDetalle, "growx, wrap");
        jpSouthAux.add(jbEliminarDetalle, "growx");

        Font fTotal = new Font(Font.SERIF, Font.BOLD, 15);
        jlTotal = new JLabel("Total");
        jlTotal.setFont(fTotal);
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jftTotal = new JFormattedTextField();
        jftTotal.setEditable(false);
        jftTotal.setColumns(columns);
        JPanel jpTotal = new JPanel(new MigLayout());
        jpTotal.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jpTotal.add(jlTotal);
        jpTotal.add(jftTotal);

        JPanel jpDetalleAux = new JPanel(new MigLayout());
        jpDetalleAux.add(jpSouthAux, "growx,wrap");
        jpDetalleAux.add(jpTotal, "growx, wrap");
        jpCenter.add(jspReciboDetalle, BorderLayout.CENTER);
        jpCenter.add(jpDetalleAux, BorderLayout.EAST);
    }

    private void initSouth() {
        jpSouth = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAceptar = new JButton("Confirmar cobro [F1]");
        jbAceptar.setMargin(insets);
        jbImprimir = new JButton("Imprimir [F2]");
        jbImprimir.setMargin(insets);
        jbSalir = new JButton("Salir [ESC]");
        jbSalir.setMargin(insets);
        jpSouth.add(jbAceptar);
        jpSouth.add(jbImprimir);
        jpSouth.add(jbSalir);
    }
}
