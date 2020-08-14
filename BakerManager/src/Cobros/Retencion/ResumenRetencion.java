/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import Entities.E_NotaCreditoCabecera;
import Entities.E_retencionVenta;
import Entities.Estado;
import Excel.ExportarRetencion;
import Interface.InterfaceFacturaDetalle;
import ModeloTabla.RetencionVentaTableModel;
import bakermanager.C_inicio;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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
import javax.swing.JTable;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class ResumenRetencion extends JDialog implements ActionListener, KeyListener, InterfaceFacturaDetalle {

    JScrollPane jspCabecera;
    JTable jtCabecera;
    JButton jbSalir, jbImportarXLS;
    JLabel jlTotal;
    JFormattedTextField jftTotalRetencion;
    Date fechaInicio, fechaFin;
    JPanel jtpPanel;
    E_NotaCreditoCabecera cabecera;
    RetencionVentaTableModel tm;
    Estado estado;

    public ResumenRetencion(C_inicio c_inicio) {
        super(c_inicio.vista, DEFAULT_MODALITY_TYPE);
        constructLayout();
        inicializarComponentes();
        agregarListener();
    }

    private void constructLayout() {
        setTitle("Resumen de Retenciones");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void inicializarDatos(RetencionVentaTableModel tm) {
        inicializarVista(tm);
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    private void inicializarComponentes() {
        jtCabecera = new JTable();
        jtCabecera.getTableHeader().setReorderingAllowed(false);
        jspCabecera = new JScrollPane(jtCabecera);
        JPanel jpTotales = new JPanel(new MigLayout());
        jftTotalRetencion = new JFormattedTextField();
        jftTotalRetencion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jlTotal = new JLabel("Total de retenciones");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotales.add(jlTotal);
        jpTotales.add(jftTotalRetencion, "pushx, growx");
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar retencion");
        jtpPanel = new JPanel(new BorderLayout());

        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspCabecera, BorderLayout.CENTER);
        jpCenter.add(jpTotales, BorderLayout.SOUTH);

        jtpPanel.add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jtpPanel, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void inicializarVista(RetencionVentaTableModel tm) {
        jtCabecera.setModel(tm);
        this.tm = tm;
        Utilities.c_packColumn.packColumns(jtCabecera, 1);
        BigInteger total = new BigInteger("0");
        for (E_retencionVenta unaRetencion : tm.getList()) {
            total = total.add(new BigInteger(unaRetencion.getMonto() + ""));
        }
        jftTotalRetencion.setValue(total);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    private void importarExcelCompleto() {

    }

    private void importarExcelResumido() {
        ArrayList<E_retencionVenta> ed = new ArrayList<>(this.tm.getList());
        ExportarRetencion ce = new ExportarRetencion("Resumen de retenciones", ed);
        ce.exportacionResumida();
    }

    private void exportHandler() {
        Object[] options = {"Resumido"};
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
                        importarExcelResumido();
                    }
                });
                break;
            }
//            case 1: {
//                //Minimalista
//                EventQueue.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        importarExcelResumido();
//                    }
//                });
//                break;
//            }
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
