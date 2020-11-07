/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos.BuscarCheques;

import DB.DB_Pago;
import Entities.E_cuentaCorrienteDetalle;
import Excel.ExportarReciboPago;
import Interface.InterfaceFacturaDetalle;
import ModeloTabla.ChequesPendienteTableModel;
import ModeloTabla.CtaCteDetalleAgrupadoTableModel;
import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.List;
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
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class ResumenChequesPagos extends JDialog implements ActionListener, KeyListener, InterfaceFacturaDetalle {

    JScrollPane jspChequesEmitidos;
    JTable jtChequesEmitidos;
    JButton jbSalir, jbImportarXLS;
    JLabel jlTotal;
    JFormattedTextField jftTotal;
    Date inicio, fin;
    JTabbedPane jtpPanel;
    private CtaCteDetalleAgrupadoTableModel chequesEmitidosAgrupadoTM;
    private List<E_cuentaCorrienteDetalle> chequesEmitidosDetalle;
    private Date fechaInicio, fechaFinal;

    public ResumenChequesPagos(JDialog frame, ChequesPendienteTableModel tm, Date fechaInicio, Date fechaFinal) {
        super(frame, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de cheques emitidos");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        this.chequesEmitidosDetalle = tm.getList();
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        inicializarComponentes();
        inicializarDatos();
        agregarListener();
    }

    private void inicializarDatos() {
        chequesEmitidosAgrupadoTM = new CtaCteDetalleAgrupadoTableModel();
        chequesEmitidosAgrupadoTM.setList(DB_Pago.consultarChequesEmitidosAgrupados(chequesEmitidosDetalle));
        jtChequesEmitidos.setModel(chequesEmitidosAgrupadoTM);
        double totalUtilizado = 0;
        for (E_cuentaCorrienteDetalle unCheque : chequesEmitidosAgrupadoTM.getList()) {
            totalUtilizado = totalUtilizado + unCheque.getMonto();
        }
        jftTotal.setValue(totalUtilizado);
        Utilities.c_packColumn.packColumns(jtChequesEmitidos, 1);
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    private void inicializarComponentes() {
        jtChequesEmitidos = new JTable();
        jtChequesEmitidos.getTableHeader().setReorderingAllowed(false);
        jspChequesEmitidos = new JScrollPane(jtChequesEmitidos);
        JPanel jpTotalProducido = new JPanel(new MigLayout());
        jftTotal = new JFormattedTextField();
        jlTotal = new JLabel("Total");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalProducido.add(jlTotal);
        jpTotalProducido.add(jftTotal, "span, grow, pushx, wrap");
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar cheques emitidos");
        jtpPanel = new JTabbedPane();
        jtpPanel.addKeyListener(this);

        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspChequesEmitidos, BorderLayout.CENTER);
        jpCenter.add(jpTotalProducido, BorderLayout.SOUTH);

        jtpPanel.addTab("Resumen", jpCenter);
        getContentPane().add(jtpPanel, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    public void importarExcelAgrupado() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ExportarReciboPago erp = new ExportarReciboPago("Cheques emitidos");
                erp.exportarChequesEmitidosAgrupado(chequesEmitidosAgrupadoTM.getList(), fechaInicio, fechaFinal);
            }
        });
    }

    public void importarExcelCompleto() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ExportarReciboPago erp = new ExportarReciboPago("Cheques emitidos");
                erp.exportarChequesEmitidosDetallado(chequesEmitidosDetalle);
            }
        });
    }

    private void exportHandler() {
        Object[] options = {"Completo",
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
                        importarExcelAgrupado();
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
