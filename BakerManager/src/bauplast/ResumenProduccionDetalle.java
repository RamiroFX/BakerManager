/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import DB.DB_Produccion;
import Entities.E_produccionFilm;
import Excel.ExportarProduccion;
import Interface.InterfaceFacturaDetalle;
import ModeloTabla.RolloProducidoTableModel;
import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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
import javax.swing.table.TableColumn;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class ResumenProduccionDetalle extends JDialog implements ActionListener, KeyListener, InterfaceFacturaDetalle {

    JScrollPane jspEgreso;
    JTable jtEgreso;
    JButton jbSalir, jbImportarXLS;
    JLabel jlTotal, jlTotalUtilizado, jlTotalDisponible;
    JFormattedTextField jftTotalProducido, jftTotalUtilizado, jftTotalDisponible;
    Date inicio, fin;
    JTabbedPane jtpPanel;
    RolloProducidoTableModel tm, tmAux;
    String descripcion;

    public ResumenProduccionDetalle(JDialog frame, RolloProducidoTableModel tm, String descripcion) {
        super(frame, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de producción");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        this.tm = tm;
        this.descripcion = descripcion;
        inicializarComponentes();
        inicializarDatos();
        agregarListener();
    }

    private void inicializarDatos() {
        //Kls. Rollo 40x50 Tr. S/color BD S/I
        tmAux = new RolloProducidoTableModel();
        tmAux.setList(DB_Produccion.consultarFilmDisponibleAgrupado(tm, descripcion, false));
        jtEgreso.setModel(tmAux);
        double totalProducido = 0;
        double totalUtilizado = 0;
        double totalDisponible = 0;
        for (int i = 0; i < tm.getList().size(); i++) {
            E_produccionFilm get = tm.getList().get(i);
            totalProducido = totalProducido + get.getPeso();
            totalUtilizado = totalUtilizado + get.getPesoUtilizado();
            totalDisponible = totalDisponible + get.getPesoActual();
        }
        jftTotalProducido.setValue(totalProducido);
        jftTotalUtilizado.setValue(totalUtilizado);
        jftTotalDisponible.setValue(totalDisponible);

        TableColumn tcol0 = jtEgreso.getColumnModel().getColumn(0);
        TableColumn tcol1 = jtEgreso.getColumnModel().getColumn(1);
        TableColumn tcol2 = jtEgreso.getColumnModel().getColumn(2);
        jtEgreso.removeColumn(tcol0);
        jtEgreso.removeColumn(tcol1);
        jtEgreso.removeColumn(tcol2);//nroFilm
        //jtEgreso.removeColumn(tcol8);
        //jtEgreso.removeColumn(tcol9);
        Utilities.c_packColumn.packColumns(jtEgreso, 1);
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    private void inicializarComponentes() {
        jtEgreso = new JTable();
        jtEgreso.getTableHeader().setReorderingAllowed(false);
        jspEgreso = new JScrollPane(jtEgreso);
        JPanel jpTotalProducido = new JPanel(new MigLayout());
        jftTotalProducido = new JFormattedTextField();
        jftTotalUtilizado = new JFormattedTextField();
        jftTotalDisponible = new JFormattedTextField();
        //jftTotalEgreso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jlTotal = new JLabel("Total producido");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotalUtilizado = new JLabel("Total utilizado");
        jlTotalUtilizado.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotalDisponible = new JLabel("Total disponible");
        jlTotalDisponible.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalProducido.add(jlTotal);
        jpTotalProducido.add(jftTotalProducido, "span, grow, pushx, wrap");
        jpTotalProducido.add(jlTotalUtilizado);
        jpTotalProducido.add(jftTotalUtilizado, "span, grow, pushx, wrap");
        jpTotalProducido.add(jlTotalDisponible);
        jpTotalProducido.add(jftTotalDisponible, "span, grow, pushx");
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
                ExportarProduccion ep = new ExportarProduccion("Produccion", new ArrayList<E_produccionFilm>(tmAux.getList()));
                ep.exportacionAgrupadaPorDetalle();
            }
        });
    }

    public void importarExcelCompleto() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ExportarProduccion ep = new ExportarProduccion("Produccion", new ArrayList<E_produccionFilm>(tm.getList()));
                ep.generarInformeCompleto();
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
