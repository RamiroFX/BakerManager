/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
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
public class V_crearPago extends JDialog {

    private static final String INGRESAR_COD_PROD = "Ingresar número de factura";
    private static final String COD_PROD = "Número de factura";

    //NORTE
    JPanel jpNorth;
    public JTextField jtfProveedor, jtfNroRecibo, jtfProveedorRuc, jtfNroFactura;//, jtfFuncionario
    public JButton jbProveedor;//, jbFuncionario;
    public JLabel jlNroRecibo, jlFechaCobro;
    public JDateChooser jdcFechaCobro;
    //CENTRO
    JPanel jpCenter;
    public JTable jtReciboDetalle;
    public JScrollPane jspReciboDetalle;
    public JButton jbAgregarFactura, jbModificarDetalle, jbEliminarDetalle;
    public JLabel jlTotal;
    public JFormattedTextField jftTotal;
    //SUR
    JPanel jpSouth;
    public JButton jbAceptar, jbSalir;

    public V_crearPago(JFrame frame) {
        super(frame, "Crear pago", JDialog.ModalityType.APPLICATION_MODAL);
        setSize(950, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
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
        jbProveedor = new JButton("Agregar proveedor [F3] ");
        //jbFuncionario = new JButton("Agregar cobrador [F5]");
        jtfProveedor = new JTextField(30);
        jtfProveedor.setEditable(false);
        //jtfFuncionario = new JTextField(30);
        //jtfFuncionario.setEditable(false);
        jlNroRecibo = new JLabel("Nro. Recibo");
        jtfNroRecibo = new JTextField(20);
        jlFechaCobro = new JLabel("Fecha");
        jdcFechaCobro = new JDateChooser();
        jdcFechaCobro.setPreferredSize(new Dimension(150, 20));
        jpNorth.add(jbProveedor);
        jpNorth.add(jtfProveedor);
        jpNorth.add(jlNroRecibo);
        jpNorth.add(jtfNroRecibo);
        jpNorth.add(jlFechaCobro);
        jpNorth.add(jdcFechaCobro, "wrap");
        /*//jpNorth.add(jbFuncionario);
        //jpNorth.add(jtfFuncionario);
        jpNorth.add(new JComponent() {
        });
        jpNorth.add(new JComponent() {
        });
        jpNorth.add(new JComponent() {
        });
        jpNorth.add(new JComponent() {
        });*/
    }

    private void initCenter() {
        int columns = 8;
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtReciboDetalle = new JTable();
        jtReciboDetalle.getTableHeader().setReorderingAllowed(false);
        jtReciboDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspReciboDetalle = new JScrollPane(jtReciboDetalle);
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
        jpCenter.add(jspReciboDetalle, BorderLayout.CENTER);
        jpCenter.add(jpDetalleAux, BorderLayout.EAST);
    }

    private void initSouth() {
        jpSouth = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAceptar = new JButton("Confirmar pago [F1]");
        jbAceptar.setMargin(insets);
        jbSalir = new JButton("Salir [ESC]");
        jbSalir.setMargin(insets);
        jpSouth.add(jbAceptar);
        jpSouth.add(jbSalir);
    }
}
