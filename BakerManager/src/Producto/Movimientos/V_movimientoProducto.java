/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.Movimientos;

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
public class V_movimientoProducto extends JDialog {

    JTextField jtfProducto;
    JScrollPane jspMovimientos;
    JTable jtMovimientos;
    JButton jbProducto, jbSalir, jbImportarXLS;
    JLabel jlEntrada, jlSalida, jlTotal;
    JFormattedTextField jftEntrada, jftSalida, jftTotal;

    public V_movimientoProducto(JFrame vista) {
        super(vista, DEFAULT_MODALITY_TYPE);
        setTitle("Movimientos");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(vista);
        inicializarComponentes();
    }

    public V_movimientoProducto(JDialog vista) {
        super(vista, DEFAULT_MODALITY_TYPE);
        setTitle("Movimientos");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(vista);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        jbProducto = new JButton("Producto");
        jtfProducto = new JTextField();
        jtfProducto.setEditable(false);
        jtMovimientos = new JTable();
        jtMovimientos.getTableHeader().setReorderingAllowed(false);
        jspMovimientos = new JScrollPane(jtMovimientos);
        JPanel jpTotalCobrado = new JPanel(new GridLayout(3, 2));
        jftEntrada = new JFormattedTextField();
        jftEntrada.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jftSalida = new JFormattedTextField();
        jftSalida.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jftTotal = new JFormattedTextField();
        jftTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jlEntrada = new JLabel("Total entradas");
        jlEntrada.setHorizontalAlignment(SwingConstants.CENTER);
        jlSalida = new JLabel("Total salidas");
        jlSalida.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotal = new JLabel("Balance");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalCobrado.add(jlEntrada);
        jpTotalCobrado.add(jftEntrada);
        jpTotalCobrado.add(jlSalida);
        jpTotalCobrado.add(jftSalida);
        jpTotalCobrado.add(jlTotal);
        jpTotalCobrado.add(jftTotal);
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar cobro");

        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspMovimientos, BorderLayout.CENTER);
        jpCenter.add(jpTotalCobrado, BorderLayout.SOUTH);
        JPanel jpNorth = new JPanel(new BorderLayout());
        jpNorth.add(jbProducto, BorderLayout.WEST);
        jpNorth.add(jtfProducto, BorderLayout.CENTER);
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

}
