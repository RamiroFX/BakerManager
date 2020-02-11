/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Cobro;
import DB.DB_UtilizacionMateriaPrima;
import Entities.E_utilizacionMateriaPrimaCabecera;
import Excel.ExportarUtilizacionMP;
import Interface.InterfaceFacturaDetalle;
import ModeloTabla.CtaCteCabeceraTableModel;
import ModeloTabla.CtaCteDetalleTableModel;
import ModeloTabla.UtilizacionMPCabeceraTableModel;
import ModeloTabla.UtilizacionMPDetalleTableModel;
import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 *
 * @author Ramiro Ferreira
 */
public class ResumenCobros extends JDialog implements ActionListener, KeyListener, InterfaceFacturaDetalle {

    JScrollPane jspCabecera, jspDetalle;
    JTable jtCabecera, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlEfectivo, jlCheque, jlTotal;
    JFormattedTextField jftTotalCobrado, jftTotalEfectivo, jftTotalCheque;
    Date inicio, fin;
    JTabbedPane jtpPanel;
    CtaCteCabeceraTableModel tm;

    public ResumenCobros(JFrame frame, CtaCteCabeceraTableModel tm) {
        super(frame, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de cobros");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        this.tm = tm;
        inicializarComponentes();
        inicializarDatos(tm);
        agregarListener();
    }

    public ResumenCobros(JFrame frame, CtaCteCabeceraTableModel tm, Date inicio, Date fin) {
        super(frame, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de cobros");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        this.tm = tm;
        this.inicio = inicio;
        this.fin = fin;
        inicializarComponentes();
        inicializarDatos(tm);
        agregarListener();
    }

    private void inicializarDatos(CtaCteCabeceraTableModel tm) {
        jtCabecera.setModel(tm);
        CtaCteDetalleTableModel tmDetalle = new CtaCteDetalleTableModel();
        tmDetalle.setList(DB_Cobro.consultarCobroDetalleAgrupado(tm.getList()));
        jtDetalle.setModel(tmDetalle);
        Utilities.c_packColumn.packColumns(jtCabecera, 1);
        Utilities.c_packColumn.packColumns(jtDetalle, 1);
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    private void inicializarComponentes() {
        jtCabecera = new JTable();
        jtCabecera.getTableHeader().setReorderingAllowed(false);
        jspCabecera = new JScrollPane(jtCabecera);
        jtDetalle = new JTable();
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jspDetalle = new JScrollPane(jtDetalle);
        JPanel jpTotalEgreso = new JPanel(new GridLayout(3, 2));
        jftTotalCobrado = new JFormattedTextField();
        jftTotalCobrado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalEfectivo = new JFormattedTextField();
        jftTotalEfectivo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalCheque = new JFormattedTextField();
        jftTotalCheque.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jlEfectivo = new JLabel("Cobro en efectivo");
        jlEfectivo.setHorizontalAlignment(SwingConstants.CENTER);
        jlCheque = new JLabel("Cobro en cheque");
        jlCheque.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotal = new JLabel("Total cobrado");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalEgreso.add(jlEfectivo);
        jpTotalEgreso.add(jftTotalEfectivo);
        jpTotalEgreso.add(jlCheque);
        jpTotalEgreso.add(jftTotalCheque);
        jpTotalEgreso.add(jlTotal);
        jpTotalEgreso.add(jftTotalCobrado);
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar cobros");
        jtpPanel = new JTabbedPane();
        jtpPanel.addKeyListener(this);

        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspCabecera, BorderLayout.CENTER);
        jpCenter.add(jpTotalEgreso, BorderLayout.SOUTH);

        jtpPanel.addTab("Resumen", jpCenter);
        jtpPanel.addTab("Detalle", jspDetalle);
        getContentPane().add(jtpPanel, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    private void exportHandler() {
        Object[] options = {"Individual",
            "Agrupado"};
        int n = JOptionPane.showOptionDialog(this,
                "Eliga tipo de reporte",
                "Atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        switch (n) {
            case 0: {
                //Individual
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        
                    }
                });
                break;
            }
            case 1: {
                //Agrupado
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        
                    }
                });
                break;
            }
        }
    }

    private void keyPressedHandler(final KeyEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE: {
                        cerrar();
                    }
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(jbSalir)) {
            dispose();
        } else if (ae.getSource().equals(jbImportarXLS)) {
            exportHandler();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressedHandler(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void cerrar() {
        this.dispose();
    }

    @Override
    public void notificarCambioFacturaDetalle() {
    }
}
