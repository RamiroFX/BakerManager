/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import DB.DB_Ingreso;
import Entities.E_tipoOperacion;
import Entities.M_egresoCabecera;
import Excel.C_create_excel;
import ModeloTabla.EgresoCabeceraTableModel;
import ModeloTabla.EgresoDetalleTableModel;
import bakermanager.C_inicio;
import java.awt.BorderLayout;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class Resumen_egreso extends JDialog implements ActionListener, KeyListener {

    JScrollPane jspEgreso, jspDetalle;
    JTable jtEgreso, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlContado, jlCredito, jlTotal;
    JFormattedTextField jftTotalEgreso, jftTotalEgCont, jftTotalEgCred;
    EgresoCabeceraTableModel cabeceraTm;
    EgresoDetalleTableModel detalleTm;
    JTabbedPane jtpPanel;
    Date dateInicio, dateFin;

    public Resumen_egreso(C_inicio c_inicio, EgresoCabeceraTableModel tm, Date dateInicio, Date dateFin) {
        super(c_inicio.vista, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de compras");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(c_inicio.vista);
        this.cabeceraTm = tm;
        this.detalleTm = new EgresoDetalleTableModel();
        this.dateInicio = dateInicio;
        this.dateFin = dateFin;
        inicializarComponentes();
        inicializarVista(tm);
        agregarListener();
    }

    private void inicializarComponentes() {
        jtEgreso = new JTable();
        jspEgreso = new JScrollPane(jtEgreso);
        jtDetalle = new JTable();
        jspDetalle = new JScrollPane(jtDetalle);
        JPanel jpTotalEgreso = new JPanel(new GridLayout(3, 2));
        jftTotalEgreso = new JFormattedTextField();
        jftTotalEgreso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalEgCont = new JFormattedTextField();
        jftTotalEgCont.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalEgCred = new JFormattedTextField();
        jftTotalEgCred.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jlContado = new JLabel("Egresos al contado");
        jlContado.setHorizontalAlignment(SwingConstants.CENTER);
        jlCredito = new JLabel("Egresos a crédito");
        jlCredito.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotal = new JLabel("Total compras");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalEgreso.add(jlContado);
        jpTotalEgreso.add(jftTotalEgCont);
        jpTotalEgreso.add(jlCredito);
        jpTotalEgreso.add(jftTotalEgCred);
        jpTotalEgreso.add(jlTotal);
        jpTotalEgreso.add(jftTotalEgreso);
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jtpPanel = new JTabbedPane();
        jtpPanel.addKeyListener(this);
        
        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspEgreso, BorderLayout.CENTER);
        jpCenter.add(jpTotalEgreso, BorderLayout.SOUTH);
 
        jtpPanel.addTab("Resumen", jpCenter);
        jtpPanel.addTab("Detalle", jspDetalle);
        getContentPane().add(jtpPanel, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void inicializarVista(TableModel tm) {
        jtEgreso.setModel(tm);
        Utilities.c_packColumn.packColumns(jtEgreso, 1);
        int total = 0;
        int totalContado = 0;
        int totalCredito = 0;
        for (M_egresoCabecera unaCompra : this.cabeceraTm.getList()) {
            total = total + unaCompra.getTotal();
            switch (unaCompra.getCondCompra().getId()) {
                case E_tipoOperacion.CONTADO: {
                    totalContado = totalContado + unaCompra.getTotal();
                    break;
                }
                case E_tipoOperacion.CREDITO_30: {
                    totalCredito = totalCredito + unaCompra.getTotal();
                    break;
                }
                default: {
                    totalCredito = totalCredito + unaCompra.getTotal();
                    break;
                }
            }
        }
        jftTotalEgCred.setValue(totalCredito);
        jftTotalEgCont.setValue(totalContado);
        jftTotalEgreso.setValue(total);
        jtDetalle.setModel(detalleTm);
        detalleTm.setList(DB_Egreso.consultarEgresoDetalleAgrupado(this.cabeceraTm.getList()));
        Utilities.c_packColumn.packColumns(jtDetalle, 1);
    }

    private void cerrar() {
        this.dispose();
    }
    
    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    private void importarExcelCompleto() {
        C_create_excel ce = new C_create_excel("Compras", dateInicio, dateFin, (ArrayList<M_egresoCabecera>) cabeceraTm.getList());
        ce.exportacionCompleta();
    }
    private void importarExcelResumido() {
        C_create_excel ce = new C_create_excel("Compras", dateInicio, dateFin, (ArrayList<M_egresoCabecera>) cabeceraTm.getList());
        ce.exportacionResumida();
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
    public void keyReleased(KeyEvent e) {
    }
}
