/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import DB.DB_NotaCredito;
import Entities.E_NotaCreditoCabecera;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Excel.ExportarNotaCredito;
import Interface.InterfaceFacturaDetalle;
import ModeloTabla.NotaCreditoCabeceraTableModel;
import ModeloTabla.NotaCreditoDetalleTableModel;
import bakermanager.C_inicio;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
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
public class ResumenNotaCredito extends JDialog implements ActionListener, KeyListener, InterfaceFacturaDetalle {

    JScrollPane jspCabecera, jspDetalle;
    JTable jtCabecera, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlContado, jlCredito, jlTotal;
    JFormattedTextField jftTotalNotaCredito, jftTotalCont, jftTotalCred;
    Date fechaInicio, fechaFin;
    JTabbedPane jtpPanel;
    E_NotaCreditoCabecera cabecera;
    NotaCreditoCabeceraTableModel tm;
    NotaCreditoDetalleTableModel tmDetalle;
    Estado estado;

    public ResumenNotaCredito(C_inicio c_inicio) {
        super(c_inicio.vista, DEFAULT_MODALITY_TYPE);
        constructLayout();
        inicializarComponentes();
        agregarListener();
    }

    private void constructLayout() {
        setTitle("Resumen de Notas de Crédito");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void inicializarDatos(NotaCreditoCabeceraTableModel tm) {
        inicializarVista(tm);
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
        jftTotalNotaCredito = new JFormattedTextField();
        jftTotalNotaCredito.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalCont = new JFormattedTextField();
        jftTotalCont.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalCred = new JFormattedTextField();
        jftTotalCred.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jlContado = new JLabel("Ingresos al contado");
        jlContado.setHorizontalAlignment(SwingConstants.CENTER);
        jlCredito = new JLabel("Ingresos a crédito");
        jlCredito.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotal = new JLabel("Total ingresos");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalEgreso.add(jlContado);
        jpTotalEgreso.add(jftTotalCont);
        jpTotalEgreso.add(jlCredito);
        jpTotalEgreso.add(jftTotalCred);
        jpTotalEgreso.add(jlTotal);
        jpTotalEgreso.add(jftTotalNotaCredito);
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar venta");
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

    private void inicializarVista(NotaCreditoCabeceraTableModel tm) {
        jtCabecera.setModel(tm);
        this.tm = tm;
        this.tmDetalle = new NotaCreditoDetalleTableModel();
        this.jtDetalle.setModel(tmDetalle);
        Utilities.c_packColumn.packColumns(jtCabecera, 1);
        BigInteger total = new BigInteger("0");
        BigInteger totalContado = new BigInteger("0");
        BigInteger totalCredito = new BigInteger("0");
        for (E_NotaCreditoCabecera faca : tm.getList()) {
            total = total.add(new BigInteger(faca.getTotal() + ""));
            switch (faca.getFacturaCabecera().getTipoOperacion().getId()) {
                case E_tipoOperacion.CONTADO: {//contado
                    totalContado = totalContado.add(new BigInteger(faca.getTotal() + ""));
                    break;
                }
                case E_tipoOperacion.CREDITO_30: {//credito
                    totalCredito = totalCredito.add(new BigInteger(faca.getTotal() + ""));
                    break;
                }
                default: {//credito
                    totalCredito = totalCredito.add(new BigInteger(faca.getTotal() + ""));
                    break;
                }
            }
        }
        tmDetalle.setList((DB_NotaCredito.consultarDetalleAgrupado(tm.getList())));
        Utilities.c_packColumn.packColumns(jtDetalle, 1);
        jftTotalCred.setValue(totalCredito);
        jftTotalCont.setValue(totalContado);
        jftTotalNotaCredito.setValue(total);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    private void importarExcelCompleto() {
        ArrayList<E_NotaCreditoCabecera> ed = new ArrayList<>(this.tm.getList());
        ExportarNotaCredito ce = new ExportarNotaCredito("Resumen de nota de crédito", ed);
        ce.exportacionCompleta();
    }

    private void importarExcelResumido() {
        ArrayList<E_NotaCreditoCabecera> ed = new ArrayList<>(this.tm.getList());
        ExportarNotaCredito ce = new ExportarNotaCredito("Resumen de nota de crédito", ed);
        ce.exportacionResumida();;
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

    private void cerrar() {
        this.dispose();
    }

    @Override
    public void notificarCambioFacturaDetalle() {
    }
}
