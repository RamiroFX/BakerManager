/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import java.awt.BorderLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_ver_ingreso extends JDialog {
    //NORTE

    JPanel jpNorth;
    public JTextField jtfCliente, jtfFuncionario, jtfClieDireccion, jtfClieTelefono, jtfClieRuc;
    public JRadioButton jrbContado, jrbCredito;
    //CENTRO
    JPanel jpCenter;
    public JTable jtFacturaDetalle;
    public JScrollPane jspFacturaDetalle;
    public JLabel jlIva5, jlIva10, jlExenta, jlTotal;
    public JFormattedTextField jftIva5, jftIva10, jftExenta, jftTotal;
    //SUR
    JPanel jpSouth;
    public JButton jbImprimir, jbSalir;

    public V_ver_ingreso(java.awt.Frame parent) {
        super(parent, true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ingresos");
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        setSize(new java.awt.Dimension(800, 600));
        initComponents();
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void initNorth() {
        jpNorth = new JPanel(new MigLayout());
        jtfCliente = new JTextField(30);
        jtfCliente.setEditable(false);
        jtfFuncionario = new JTextField(30);
        jtfFuncionario.setEditable(false);
        jrbContado = new JRadioButton("Contado");
        jrbCredito = new JRadioButton("Crédito");
        jrbContado.setEnabled(false);
        jrbCredito.setEnabled(false);
        jtfClieRuc = new JTextField(30);
        jtfClieRuc.setEditable(false);
        jtfClieDireccion = new JTextField(30);
        jtfClieDireccion.setEditable(false);
        jtfClieTelefono = new JTextField(30);
        jtfClieTelefono.setEditable(false);
        javax.swing.ButtonGroup bg1 = new javax.swing.ButtonGroup();
        bg1.add(jrbContado);
        bg1.add(jrbCredito);
        jpNorth.add(new JLabel("Cliente:"));
        jpNorth.add(jtfCliente);
        jpNorth.add(new JLabel("Funcionario:"));
        jpNorth.add(jtfFuncionario);
        jpNorth.add(jrbContado);
        jpNorth.add(jrbCredito, "wrap");
        jpNorth.add(new JLabel("R.U.C.:"));
        jpNorth.add(jtfClieRuc);
        jpNorth.add(new JLabel("Direccion:"));
        jpNorth.add(jtfClieDireccion);
        jpNorth.add(new JLabel("Telefono:"));
        jpNorth.add(jtfClieTelefono);
    }

    private void initCenter() {
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtFacturaDetalle = new JTable();
        jtFacturaDetalle.getTableHeader().setReorderingAllowed(false);
        jtFacturaDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspFacturaDetalle = new JScrollPane(jtFacturaDetalle);

        jlIva5 = new JLabel("I.V.A. 5%");
        jlIva10 = new JLabel("I.V.A. 10%");
        jlExenta = new JLabel("Exentas");
        jlTotal = new JLabel("Total");
        jftExenta = new JFormattedTextField();
        jftExenta.setEditable(false);
        jftIva5 = new JFormattedTextField();
        jftIva5.setEditable(false);
        jftIva10 = new JFormattedTextField();
        jftIva10.setEditable(false);
        jftTotal = new JFormattedTextField();
        jftTotal.setEditable(false);
        JPanel jpDetalleAux = new JPanel(new MigLayout());
        jpDetalleAux.add(jlExenta, "wrap");
        jpDetalleAux.add(jftExenta, "growx, wrap");
        jpDetalleAux.add(jlIva5, "wrap");
        jpDetalleAux.add(jftIva5, "growx, wrap");
        jpDetalleAux.add(jlIva10, "wrap");
        jpDetalleAux.add(jftIva10, "growx, wrap");
        jpDetalleAux.add(jlTotal, "wrap");
        jpDetalleAux.add(jftTotal, "growx, wrap");
        jpCenter.add(jspFacturaDetalle, BorderLayout.CENTER);
        jpCenter.add(jpDetalleAux, BorderLayout.EAST);
    }

    private void initSouth() {
        jpSouth = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbImprimir = new JButton("Imprimir [F2]");
        jbImprimir.setMargin(insets);
        jbSalir = new JButton("Salir [F3]");
        jbSalir.setMargin(insets);
        jpSouth.add(jbImprimir);
        jpSouth.add(jbSalir);
    }

    private void initComponents() {
        initNorth();
        initCenter();
        initSouth();
    }
}
