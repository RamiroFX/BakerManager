/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Cobro;
import Entities.E_cuentaCorrienteDetalle;
import ModeloTabla.CtaCteCabeceraTableModel;
import ModeloTabla.CtaCteDetalleTableModel;
import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
public class ResumenCobro extends JDialog implements ActionListener, KeyListener {

    JScrollPane jspCobros, jspDetalle;
    JTable jtCobros, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlEfectivo, jlCheque, jlTotal;
    JFormattedTextField jftTotalCobrado, jftTotalCheque, jftTotalEfectivo;
    JTabbedPane jtpPanel;

    //DATOS DE MODELO
    CtaCteCabeceraTableModel cabeceraTableModel;
    CtaCteDetalleTableModel detalleTableModel;

    public ResumenCobro(JFrame vista, CtaCteCabeceraTableModel tm) {
        super(vista, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de cobros");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(vista);
        inicializarDatos();
        inicializarComponentes();
        inicializarVista(tm);
        agregarListener();
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    private void cerrar() {
        this.dispose();
    }

    private void inicializarDatos() {
        detalleTableModel = new CtaCteDetalleTableModel();
    }

    private void inicializarComponentes() {
        jtCobros = new JTable();
        jtCobros.getTableHeader().setReorderingAllowed(false);
        jspCobros = new JScrollPane(jtCobros);
        jtDetalle = new JTable();
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jtDetalle.setModel(detalleTableModel);
        jspDetalle = new JScrollPane(jtDetalle);
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
        jpTotalCobrado.add(jftTotalCheque);
        jpTotalCobrado.add(jlCheque);
        jpTotalCobrado.add(jftTotalEfectivo);
        jpTotalCobrado.add(jlTotal);
        jpTotalCobrado.add(jftTotalCobrado);
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar cobro");
        jtpPanel = new JTabbedPane();
        jtpPanel.addKeyListener(this);

        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspCobros, BorderLayout.CENTER);
        jpCenter.add(jpTotalCobrado, BorderLayout.SOUTH);

        jtpPanel.addTab("Resumen", jpCenter);
        jtpPanel.addTab("Detalle", jspDetalle);
        getContentPane().add(jtpPanel, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void inicializarVista(CtaCteCabeceraTableModel tm) {
        this.cabeceraTableModel = tm;
        jtCobros.setModel(tm);
        detalleTableModel.setList(DB_Cobro.consultarCobroDetalleAgrupado(cabeceraTableModel.getList()));
        Utilities.c_packColumn.packColumns(jtCobros, 1);
        Utilities.c_packColumn.packColumns(jtDetalle, 1);
        Integer total = 0;
        Integer totalCheque = 0;
        Integer totalEfectivo = 0;
        for (E_cuentaCorrienteDetalle ctaCteDetalle : detalleTableModel.getList()) {
            if (ctaCteDetalle.getNroCheque() > 0) {
                totalCheque = totalCheque + (int) ctaCteDetalle.getMonto();
            } else {
                totalEfectivo = totalEfectivo + (int) ctaCteDetalle.getMonto();
            }
        }
        total = totalEfectivo + totalCheque;
        jftTotalEfectivo.setValue(totalEfectivo);
        jftTotalCheque.setValue(totalCheque);
        jftTotalCobrado.setValue(total);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    private void importarExcelCompleto() {
        //TODO
    }

    private void importarExcelResumido() {
        //TODO
    }

    private void exportHandler() {
        Object[] options = {"Completo",
            "Resumido"};
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
                //Completo
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        importarExcelCompleto();
                    }
                });
                break;
            }
            case 1: {
                //Minimalista
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        importarExcelResumido();
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

}

