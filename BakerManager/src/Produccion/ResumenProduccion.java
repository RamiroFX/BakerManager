/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import DB.DB_Produccion;
import Entities.E_produccionCabecera;
import Entities.E_produccionDetalle;
import Excel.ExportarProduccion;
import Interface.InterfaceFacturaDetalle;
import ModeloTabla.ProduccionCabeceraTableModel;
import ModeloTabla.ProduccionDetalleTableModel;
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
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class ResumenProduccion extends JDialog implements ActionListener, KeyListener, InterfaceFacturaDetalle {

    JScrollPane jspEgreso, jspDetalle;
    JTable jtEgreso, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlTotal;
    JFormattedTextField jftTotalProducido;
    Date inicio, fin;
    JTabbedPane jtpPanel;
    ProduccionCabeceraTableModel tm;

    public ResumenProduccion(JFrame frame, ProduccionCabeceraTableModel tm) {
        super(frame, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de producción");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        this.tm = tm;
        inicializarComponentes();
        inicializarDatos(tm);
        agregarListener();
    }

    public ResumenProduccion(JFrame frame, ProduccionCabeceraTableModel tm, Date inicio, Date fin) {
        super(frame, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de producción");
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

    private void inicializarDatos(ProduccionCabeceraTableModel tm) {
        jtEgreso.setModel(tm);
        ProduccionDetalleTableModel tmDetalle = new ProduccionDetalleTableModel();
        tmDetalle.setList(DB_Produccion.consultarProduccionDetalleAgrupado(tm.getList()));
        double totalProducido = 0;
        for (E_produccionDetalle e_produccionDetalle : tmDetalle.getList()) {
            totalProducido = totalProducido + e_produccionDetalle.getCantidad();
        }
        jtDetalle.setModel(tmDetalle);
        jftTotalProducido.setValue(totalProducido);
        Utilities.c_packColumn.packColumns(jtEgreso, 1);
        Utilities.c_packColumn.packColumns(jtDetalle, 1);
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    private void inicializarComponentes() {
        jtEgreso = new JTable();
        jtEgreso.getTableHeader().setReorderingAllowed(false);
        jspEgreso = new JScrollPane(jtEgreso);
        jtDetalle = new JTable();
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jspDetalle = new JScrollPane(jtDetalle);
        JPanel jpTotalProducido = new JPanel(new MigLayout());
        jftTotalProducido = new JFormattedTextField();
        //jftTotalEgreso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jlTotal = new JLabel("Total producido");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalProducido.add(jlTotal);
        jpTotalProducido.add(jftTotalProducido, "span, grow, pushx");
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar produccion");
        jtpPanel = new JTabbedPane();
        jtpPanel.addKeyListener(this);

        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspEgreso, BorderLayout.CENTER);
        jpCenter.add(jpTotalProducido, BorderLayout.SOUTH);

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
                        ExportarProduccion ep = new ExportarProduccion("Produccion", inicio, fin, new ArrayList<E_produccionCabecera>(tm.getList()));
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
                        ExportarProduccion ep = new ExportarProduccion("Produccion", inicio, fin, new ArrayList<E_produccionCabecera>(tm.getList()));
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
