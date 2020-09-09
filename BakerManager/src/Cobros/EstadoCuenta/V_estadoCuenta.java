/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.EstadoCuenta;

import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_estadoCuenta extends JDialog {

    JTextField jtfCliente;
    JScrollPane jspCobros;
    JTable jtCobros;
    JButton jbCliente, jbSalir, jbImportarXLS;
    JLabel jlEfectivo, jlCheque, jlTotal;
    JFormattedTextField jftTotalCobrado, jftTotalCheque, jftTotalEfectivo;

    public V_estadoCuenta(JFrame vista) {
        super(vista, DEFAULT_MODALITY_TYPE);
        setTitle("Estado de cuenta");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(vista);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        jbCliente = new JButton("Cliente");
        jtfCliente = new JTextField();
        jtfCliente.setEditable(false);
        jtCobros = new JTable();
        jtCobros.getTableHeader().setReorderingAllowed(false);
        jspCobros = new JScrollPane(jtCobros);
        JPanel jpTotalCobrado = new JPanel(new GridLayout(3, 2));
        jftTotalCobrado = new JFormattedTextField();
        jftTotalCobrado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalCheque = new JFormattedTextField();
        jftTotalCheque.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalEfectivo = new JFormattedTextField();
        jftTotalEfectivo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jlEfectivo = new JLabel("Cobros en efectivo");
        jlEfectivo.setHorizontalAlignment(SwingConstants.CENTER);
        jlCheque = new JLabel("Cobros en cheque");
        jlCheque.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotal = new JLabel("Total");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalCobrado.add(jlEfectivo);
        jpTotalCobrado.add(jftTotalEfectivo);
        jpTotalCobrado.add(jlCheque);
        jpTotalCobrado.add(jftTotalCheque);
        jpTotalCobrado.add(jlTotal);
        jpTotalCobrado.add(jftTotalCobrado);
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar cobro");

        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspCobros, BorderLayout.CENTER);
        jpCenter.add(jpTotalCobrado, BorderLayout.SOUTH);
        JPanel jpNorth = new JPanel(new BorderLayout());
        jpNorth.add(jbCliente, BorderLayout.WEST);
        jpNorth.add(jtfCliente, BorderLayout.CENTER);
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

}
