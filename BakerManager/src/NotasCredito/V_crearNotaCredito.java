/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearNotaCredito extends JDialog {
    
    private static final String INGRESAR_COD_PROD = "Ingresar número de factura";
    private static final String COD_PROD = "Número de factura";
    private static final String TITULO_CREAR = "Crear Nota de Crédito";
    private static final String TITULO_VER = "Ver Nota de Crédito";

    //NORTE
    JPanel jpNorth;
    public JTextField jtfCliente, jtfNroNotaCredito, jtfNroFactura;
    public JButton jbCliente;
    public JLabel jlNroNotaCredito, jlFechaNotaCredito;
    public JDateChooser jdcFechaotaCredito;
    //CENTRO
    JPanel jpCenter;
    public JTable jtNotaCreditoDetalle;
    public JScrollPane jspNotaCreditoDetalle;
    public JButton jbAgregarFactura, jbModificarDetalle, jbEliminarDetalle;
    public JLabel jlTotal;
    public JFormattedTextField jftTotal;
    //SUR
    JPanel jpSouth;
    public JButton jbAceptar, jbImprimir, jbSalir;
    
    public V_crearNotaCredito(JFrame frame) {
        super(frame, TITULO_CREAR, JDialog.ModalityType.APPLICATION_MODAL);
        initComponents();
        constructLayout(frame);
    }
    
    private void constructLayout(JFrame frame) {
        setSize(950, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                jtfNroFactura.requestFocus();
            }
        });        
    }
    
    private void initComponents() {
        initNorth();
        initCenter();
        initSouth();
    }
    
    private void initNorth() {
        jpNorth = new JPanel(new MigLayout());
        jbCliente = new JButton("Agregar cliente [F3] ");
        jtfCliente = new JTextField(30);
        jtfCliente.setEditable(false);
        jlNroNotaCredito = new JLabel("Nro. Nota de crédito");
        jtfNroNotaCredito = new JTextField(20);
        jlFechaNotaCredito = new JLabel("Fecha");
        jdcFechaotaCredito = new JDateChooser();
        jdcFechaotaCredito.setPreferredSize(new Dimension(150, 20));
        jpNorth.add(jbCliente, "growx");
        jpNorth.add(jtfCliente);
        jpNorth.add(jlNroNotaCredito);
        jpNorth.add(jtfNroNotaCredito);
        jpNorth.add(jlFechaNotaCredito);
        jpNorth.add(jdcFechaotaCredito);
    }
    
    private void initCenter() {
        int columns = 8;
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtNotaCreditoDetalle = new JTable();
        jtNotaCreditoDetalle.getTableHeader().setReorderingAllowed(false);
        jtNotaCreditoDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspNotaCreditoDetalle = new JScrollPane(jtNotaCreditoDetalle);
        jtfNroFactura = new JTextField(13);
        jtfNroFactura.setToolTipText(INGRESAR_COD_PROD);
        JPanel jpCodProd = new JPanel(new MigLayout());
        jpCodProd.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), COD_PROD));
        jpCodProd.add(jtfNroFactura);
        jbAgregarFactura = new JButton("Agregar factura [F4]");
        jbModificarDetalle = new JButton("Modificar detalle");
        jbEliminarDetalle = new JButton("Eliminar detalle");
        JPanel jpSouthAux = new JPanel(new MigLayout());
        jpSouthAux.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAux.add(jpCodProd, "growx, wrap");
        jpSouthAux.add(jbAgregarFactura, "growx, wrap");
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
        jpCenter.add(jspNotaCreditoDetalle, BorderLayout.CENTER);
        jpCenter.add(jpDetalleAux, BorderLayout.EAST);
    }
    
    private void initSouth() {
        jpSouth = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAceptar = new JButton("Confirmar Nota de crédito [F1]");
        jbAceptar.setMargin(insets);
        jbImprimir = new JButton("Imprimir [F2]");
        jbImprimir.setMargin(insets);
        jbImprimir.setVisible(false);
        jbSalir = new JButton("Salir [ESC]");
        jbSalir.setMargin(insets);
        jpSouth.add(jbAceptar);
        jpSouth.add(jbImprimir);
        jpSouth.add(jbSalir);
    }
}
