/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsoMateriaPrima;

import DB.DB_UtilizacionMateriaPrima;
import Entities.E_utilizacionMateriaPrimaCabecera;
import Excel.ExportarUtilizacionMP;
import Interface.InterfaceFacturaDetalle;
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
public class ResumenUtilizacionMP extends JDialog implements ActionListener, KeyListener, InterfaceFacturaDetalle {

    JScrollPane jspCabecera, jspDetalle;
    JTable jtCabecera, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlContado, jlCredito, jlTotal;
    JFormattedTextField jftTotalEgreso, jftTotalEgCont, jftTotalEgCred;
    Date inicio, fin;
    JTabbedPane jtpPanel;
    UtilizacionMPCabeceraTableModel tm;

    public ResumenUtilizacionMP(JFrame frame, UtilizacionMPCabeceraTableModel tm) {
        super(frame, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de utilización de Materia Prima");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        this.tm = tm;
        inicializarComponentes();
        inicializarDatos(tm);
        agregarListener();
    }

    public ResumenUtilizacionMP(JFrame frame, UtilizacionMPCabeceraTableModel tm, Date inicio, Date fin) {
        super(frame, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de utilización de Materia Prima");
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

    private void inicializarDatos(UtilizacionMPCabeceraTableModel tm) {
        jtCabecera.setModel(tm);
        UtilizacionMPDetalleTableModel tmDetalle = new UtilizacionMPDetalleTableModel();
        tmDetalle.setList(DB_UtilizacionMateriaPrima.consultarUtilizacionMateriaPrimaDetalleAgrupado(tm.getList()));
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
        jftTotalEgreso = new JFormattedTextField();
        jftTotalEgreso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalEgCont = new JFormattedTextField();
        jftTotalEgCont.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalEgCred = new JFormattedTextField();
        jftTotalEgCred.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jlContado = new JLabel("Ingresos al contado");
        jlContado.setHorizontalAlignment(SwingConstants.CENTER);
        jlCredito = new JLabel("Ingresos a crédito");
        jlCredito.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotal = new JLabel("Total ingresos");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalEgreso.add(jlContado);
        jpTotalEgreso.add(jftTotalEgCont);
        jpTotalEgreso.add(jlCredito);
        jpTotalEgreso.add(jftTotalEgCred);
        jpTotalEgreso.add(jlTotal);
        jpTotalEgreso.add(jftTotalEgreso);
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar produccion");
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
                        ExportarUtilizacionMP ep = new ExportarUtilizacionMP("Utilización de materia prima", inicio, fin, new ArrayList<E_utilizacionMateriaPrimaCabecera>(tm.getList()));
                        ep.exportacionIndividual();
                    }
                });
                break;
            }
            case 1: {
                //Agrupado
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ExportarUtilizacionMP ep = new ExportarUtilizacionMP("Utilización de materia prima", inicio, fin, new ArrayList<E_utilizacionMateriaPrimaCabecera>(tm.getList()));
                        ep.exportacionAgrupada();
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
