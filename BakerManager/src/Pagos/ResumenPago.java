/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import DB.DB_Pago;
import Entities.E_reciboPagoCabecera;
import Entities.E_reciboPagoDetalle;
import Excel.ExportarReciboPago;
import ModeloTabla.ReciboPagoCabeceraTableModel;
import ModeloTabla.ReciboPagoDetalleAgrupadoTableModel;
import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class ResumenPago extends JDialog implements ActionListener, KeyListener {

    JScrollPane jspPagos, jspDetalle;
    JTable jtPagos, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlEfectivo, jlCheque, jlTotal;
    JFormattedTextField jftTotalCobrado, jftTotalCheque, jftTotalEfectivo;
    JTabbedPane jtpPanel;

    //DATOS DE MODELO
    ReciboPagoCabeceraTableModel cabeceraTableModel;
    ReciboPagoDetalleAgrupadoTableModel detalleTableModel;

    public ResumenPago(JFrame vista, ReciboPagoCabeceraTableModel tm) {
        super(vista, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de pagos");
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
        detalleTableModel = new ReciboPagoDetalleAgrupadoTableModel();
    }

    private void inicializarComponentes() {
        jtPagos = new JTable();
        jtPagos.getTableHeader().setReorderingAllowed(false);
        jspPagos = new JScrollPane(jtPagos);
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
        jpTotalCobrado.add(jftTotalEfectivo);
        jpTotalCobrado.add(jlCheque);
        jpTotalCobrado.add(jftTotalCheque);
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
        jpCenter.add(jspPagos, BorderLayout.CENTER);
        jpCenter.add(jpTotalCobrado, BorderLayout.SOUTH);

        jtpPanel.addTab("Resumen", jpCenter);
        jtpPanel.addTab("Detalle", jspDetalle);
        getContentPane().add(jtpPanel, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void inicializarVista(ReciboPagoCabeceraTableModel tm) {
        this.cabeceraTableModel = tm;
        jtPagos.setModel(tm);
        detalleTableModel.setList(DB_Pago.consultarPagoDetalleAgrupado(cabeceraTableModel.getList()));
        Utilities.c_packColumn.packColumns(jtPagos, 1);
        Utilities.c_packColumn.packColumns(jtDetalle, 1);
        BigInteger total = new BigInteger("0");
        BigInteger totalCheque = new BigInteger("0");
        BigInteger totalEfectivo = new BigInteger("0");
        for (E_reciboPagoDetalle ctaCteDetalle : detalleTableModel.getList()) {
            if (ctaCteDetalle.getBanco() != null) {
                if (ctaCteDetalle.getBanco().getDescripcion() == null) {
                    totalEfectivo = totalEfectivo.add(new BigInteger(Double.valueOf(ctaCteDetalle.getMonto()).intValue() + ""));
                } else {
                    totalCheque = totalCheque.add(new BigInteger(Double.valueOf(ctaCteDetalle.getMonto()).intValue() + ""));
                }
            }
        }
        total = total.add(totalCheque).add(totalEfectivo);
        jftTotalEfectivo.setValue(totalEfectivo);
        jftTotalCheque.setValue(totalCheque);
        jftTotalCobrado.setValue(total);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    private void importarExcelIndividual() {
        ArrayList<E_reciboPagoCabecera> cabeceraList = new ArrayList<>(cabeceraTableModel.getList());
        ExportarReciboPago erp = new ExportarReciboPago("Resumen de pago", cabeceraList);
        erp.exportacionIndividual();
    }

    private void importarExcelResumido() {
        ArrayList<E_reciboPagoCabecera> cabeceraList = new ArrayList<>(cabeceraTableModel.getList());
        ExportarReciboPago erp = new ExportarReciboPago("Resumen de pago", cabeceraList);
        erp.exportacionResumida();
    }

    private void exportHandler() {
        Object[] options = {"Individual",
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
                //Individual
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        importarExcelIndividual();
                    }
                });
                break;
            }
            case 1: {
                //Resumido
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
