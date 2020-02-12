/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Cobro;
import DB.DB_Ingreso;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaSinPago;
import Entities.M_campoImpresion;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import ModeloTabla.CtaCteCabeceraTableModel;
import ModeloTabla.CtaCteDetalleAgrupadoTableModel;
import ModeloTabla.FacturaDetalleTableModel;
import ModeloTabla.FacturaSinPagoTableModel;
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
public class ResumenCobroPendiente extends JDialog implements ActionListener, KeyListener {

    JScrollPane jspCobros, jspDetalle;
    JTable jtCobros, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlTotal;
    JFormattedTextField jftTotalCobrado;
    JTabbedPane jtpPanel;

    //DATOS DE MODELO
    FacturaSinPagoTableModel cabeceraTableModel;
    FacturaDetalleTableModel detalleTableModel;

    public ResumenCobroPendiente(JDialog vista, FacturaSinPagoTableModel tm) {
        super(vista, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de cobros pendientes");
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
        detalleTableModel = new FacturaDetalleTableModel();
    }

    private void inicializarComponentes() {
        jtCobros = new JTable();
        jtCobros.getTableHeader().setReorderingAllowed(false);
        jspCobros = new JScrollPane(jtCobros);
        jtDetalle = new JTable();
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jtDetalle.setModel(detalleTableModel);
        jspDetalle = new JScrollPane(jtDetalle);
        JPanel jpTotalCobrado = new JPanel(new GridLayout(1, 2));
        jftTotalCobrado = new JFormattedTextField();
        //jftTotalCobrado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));

        jlTotal = new JLabel("Total");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
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

    private void inicializarVista(FacturaSinPagoTableModel tm) {
        this.cabeceraTableModel = tm;
        jtCobros.setModel(tm);
        Integer total = 0;
        ArrayList<M_facturaCabecera> cadenaCabeceras = new ArrayList<>();
        for (E_facturaSinPago e_facturaSinPago : cabeceraTableModel.getList()) {
            M_facturaCabecera faca = new M_facturaCabecera();
            faca.setIdFacturaCabecera(e_facturaSinPago.getIdCabecera());
            total = total + e_facturaSinPago.getSaldo();
            cadenaCabeceras.add(faca);
        }
        if (cadenaCabeceras.isEmpty()) {
            return;
        }
        detalleTableModel.setFacturaDetalleList(DB_Ingreso.consultarIngresoDetalleAgrupado(cadenaCabeceras));
        Utilities.c_packColumn.packColumns(jtCobros, 1);
        Utilities.c_packColumn.packColumns(jtDetalle, 1);
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
