/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearProductoTerminado;

import DB.DB_Produccion;
import Entities.E_produccionDetalle;
import Excel.ExportarProduccionTerminados;
import Interface.InterfaceFacturaDetalle;
import ModeloTabla.ProduccionDetalleAgrupadaTableModel;
import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class ResumenProductosTerminados extends JDialog implements ActionListener, KeyListener, InterfaceFacturaDetalle {

    JScrollPane jspDesperdicios;
    JTable jtDesperdicios;
    JButton jbSalir, jbImportarXLS;
    JLabel jlTotalDesperdicio;
    JFormattedTextField jftTotalUtilizado;
    Date fechaInicio, fechaFinal;
    boolean conFecha;
    JTabbedPane jtpPanel;
    ProduccionDetalleAgrupadaTableModel productosTerminadosAgrupadosTM;

    //VARIABLES PARA EL RANGO
    ButtonGroup buttonGroup;
    JRadioButton jrbMes, jrbDias, jrbHistorico;
    JTextField jtfCantMes, jtfCantDias;
    JButton jbActualizar;
    String producto, buscarPor, ordenarPor, categoria;

    public ResumenProductosTerminados(JDialog frame, String descripcion, String buscarPor, String ordenarPor,
            String categoria, boolean porFecha, Date fechaInicio, Date fechaFinal) {
        super(frame, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de productos terminados");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        inicializarComponentes();
        inicializarDatos(descripcion, buscarPor, ordenarPor,
                categoria, porFecha, fechaInicio, fechaFinal);
        agregarListener();
    }

    private void inicializarDatos(String descripcion, String buscarPor, String ordenarPor,
            String categoria, boolean porFecha, Date fechaInicio, Date fechaFinal) {
        this.producto = descripcion;
        this.buscarPor = buscarPor;
        this.ordenarPor = ordenarPor;
        this.categoria = categoria;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.conFecha = porFecha;
        productosTerminadosAgrupadosTM = new ProduccionDetalleAgrupadaTableModel(ProduccionDetalleAgrupadaTableModel.SUPER);
        productosTerminadosAgrupadosTM.setFechaLimite(fechaInicio);
        productosTerminadosAgrupadosTM.setRangoSaldoAnterior(ProduccionDetalleAgrupadaTableModel.TIPO_FECHA_MES, 2, false);
        productosTerminadosAgrupadosTM.setList(DB_Produccion.consultarProductosTerminadosAgrupado(descripcion, buscarPor, ordenarPor,
                categoria, porFecha, fechaInicio, fechaFinal));
        jtDesperdicios.setModel(productosTerminadosAgrupadosTM);
        double totalUtilizado = 0;
        for (E_produccionDetalle unaMP : productosTerminadosAgrupadosTM.getList()) {
            totalUtilizado = totalUtilizado + unaMP.getCantidad();
        }
        jftTotalUtilizado.setValue(totalUtilizado);
        Utilities.c_packColumn.packColumns(jtDesperdicios, 1);
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    private void inicializarComponentes() {
        jtDesperdicios = new JTable();
        jtDesperdicios.getTableHeader().setReorderingAllowed(false);
        jspDesperdicios = new JScrollPane(jtDesperdicios);
        JPanel jpTotalProducido = new JPanel(new MigLayout());
        jftTotalUtilizado = new JFormattedTextField();
        jlTotalDesperdicio = new JLabel("Total producido");
        jlTotalDesperdicio.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalProducido.add(jlTotalDesperdicio);
        jpTotalProducido.add(jftTotalUtilizado, "span, grow, pushx, wrap");
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar produccion");
        jtpPanel = new JTabbedPane();
        jtpPanel.addKeyListener(this);

        jrbMes = new JRadioButton("Mes");
        jrbMes.setSelected(true);
        jrbDias = new JRadioButton("Días");
        jrbHistorico = new JRadioButton("Histórico");
        buttonGroup = new ButtonGroup();
        buttonGroup.add(jrbMes);
        buttonGroup.add(jrbDias);
        buttonGroup.add(jrbHistorico);
        jtfCantMes = new JTextField("2");
        jtfCantMes.setColumns(15);
        jtfCantDias = new JTextField("50");
        jtfCantDias.setColumns(15);
        jbActualizar = new JButton("Actualizar");
        JPanel jpControlTiempo = new JPanel(new MigLayout());
        jpControlTiempo.setBorder(new TitledBorder("Rango de saldo anterior"));
        jpControlTiempo.add(jrbMes);
        jpControlTiempo.add(jtfCantMes);
        jpControlTiempo.add(jrbDias);
        jpControlTiempo.add(jtfCantDias);
        jpControlTiempo.add(jrbHistorico, "wrap");
        jpControlTiempo.add(jbActualizar);
        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspDesperdicios, BorderLayout.CENTER);
        jpCenter.add(jpTotalProducido, BorderLayout.SOUTH);

        jtpPanel.addTab("Resumen", jpCenter);
        getContentPane().add(jpControlTiempo, BorderLayout.NORTH);
        getContentPane().add(jtpPanel, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
        jbActualizar.addActionListener(this);
    }

    public void importarExcelAgrupado() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ExportarProduccionTerminados ept = new ExportarProduccionTerminados("Prod terminados", conFecha, fechaInicio, fechaFinal, productosTerminadosAgrupadosTM.getList());
                ept.exportacionAgrupada();
            }
        });
    }

    public void importarExcelCompleto() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

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

    private void actualizarResumen() {
        int tipo = -1;
        int cantidad = 0;
        boolean esHistorico = false;
        if (jrbMes.isSelected()) {
            if (!validarCantidadMes()) {
                return;
            }
            String cantidadTiempoString = this.jtfCantMes.getText().trim();
            cantidad = Integer.valueOf(cantidadTiempoString);
            tipo = ProduccionDetalleAgrupadaTableModel.TIPO_FECHA_MES;
        } else if (jrbDias.isSelected()) {
            if (!validarCantidadDias()) {
                return;
            }
            String cantidadTiempoString = this.jtfCantDias.getText().trim();
            cantidad = Integer.valueOf(cantidadTiempoString);
            tipo = ProduccionDetalleAgrupadaTableModel.TIPO_FECHA_DIA;
        } else if (jrbHistorico.isSelected()) {
            esHistorico = true;
        }
        productosTerminadosAgrupadosTM.setRangoSaldoAnterior(tipo, cantidad, esHistorico);
        productosTerminadosAgrupadosTM.setList(DB_Produccion.consultarProductosTerminadosAgrupado(producto, buscarPor, ordenarPor,
                categoria, conFecha, fechaInicio, fechaFinal));
    }

    private boolean validarCantidadDias() {
        int cantidadTiempo = -1;
        if (this.jtfCantDias.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese una cantidad de días", "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        try {
            String cantidadTiempoString = this.jtfCantDias.getText().trim();
            cantidadTiempo = Integer.valueOf(cantidadTiempoString);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese un número válido", "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (cantidadTiempo < 1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese un número válido", "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private boolean validarCantidadMes() {
        int cantidadTiempo = -1;
        if (this.jtfCantMes.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese una cantidad de meses", "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        try {
            String cantidadTiempoString = this.jtfCantMes.getText().trim();
            cantidadTiempo = Integer.valueOf(cantidadTiempoString);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese un número válido", "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (cantidadTiempo < 1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese un número válido", "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(jbSalir)) {
            dispose();
        } else if (ae.getSource().equals(jbActualizar)) {
            actualizarResumen();
        } else if (ae.getSource().equals(jbImportarXLS)) {
            importarExcelAgrupado();
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
