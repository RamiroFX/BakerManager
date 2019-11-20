/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Ingreso;
import Entities.E_facturaCabeceraFX;
import Entities.E_facturaDetalleFX;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Excel.ExportarVentas;
import bakermanager.C_inicio;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class Resumen_ingreso extends JDialog implements ActionListener, KeyListener {

    JScrollPane jspEgreso, jspDetalle;
    JTable jtEgreso, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlContado, jlCredito, jlTotal;
    JFormattedTextField jftTotalEgreso, jftTotalEgCont, jftTotalEgCred;
    Date inicio, fin;
    String idEmpleado, tipo_operacion;
    Integer nro_factura;
    JTabbedPane jtpPanel;
    M_cliente cliente;
    Estado estado;

    public Resumen_ingreso(C_inicio c_inicio, TableModel tm, M_cliente cliente_entidad,
            Integer nro_factura, String idEmpleado, Date inicio, Date fin,
            String tipo_operacion, Estado estado) {
        super(c_inicio.vista, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de ingresos");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(c_inicio.vista);
        this.inicio = inicio;
        this.fin = fin;
        this.cliente = cliente_entidad;
        this.idEmpleado = idEmpleado;
        this.tipo_operacion = tipo_operacion;
        this.nro_factura = nro_factura;
        this.estado = estado;
        inicializarComponentes();
        inicializarVista(tm, inicio, fin);
        agregarListener();
    }

    private void inicializarComponentes() {
        jtEgreso = new JTable();
        jtEgreso.getTableHeader().setReorderingAllowed(false);
        jspEgreso = new JScrollPane(jtEgreso);
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
        jbImportarXLS.setName("exportar venta");
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

    private void inicializarVista(TableModel tm, Date inicio, Date fin) {
        jtEgreso.setModel(tm);
        Utilities.c_packColumn.packColumns(jtEgreso, 1);
        Integer total = 0;
        Integer totalContado = 0;
        Integer totalCredito = 0;
        int cantFilas = jtEgreso.getRowCount();
        for (int i = 0; i < cantFilas; i++) {
            total = total + Integer.valueOf(String.valueOf(jtEgreso.getValueAt(i, 5)));
            if (jtEgreso.getValueAt(i, 6).equals("Contado")) {
                totalContado = totalContado + Integer.valueOf(String.valueOf(jtEgreso.getValueAt(i, 5)));
            } else {
                totalCredito = totalCredito + Integer.valueOf(String.valueOf(jtEgreso.getValueAt(i, 5)));
            }
        }
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(inicio);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 000);
        java.sql.Timestamp fInicio = java.sql.Timestamp.valueOf(sdfs.format(calendario.getTime()));
        calendario.setTime(fin);
        calendario.set(Calendar.HOUR_OF_DAY, 24);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);
        java.sql.Timestamp fFin = java.sql.Timestamp.valueOf(sdfs.format(calendario.getTime()));
        jtDetalle.setModel(DB_Ingreso.consultarIngresoDetalleAgrupado(fInicio, fFin, cliente, estado));
        Utilities.c_packColumn.packColumns(jtDetalle, 1);
        jftTotalEgCred.setValue(totalCredito);
        jftTotalEgCont.setValue(totalContado);
        jftTotalEgreso.setValue(total);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    private void importarExcelCompleto(M_cliente cliente, Integer nro_factura, String idEmpleado, String tipo_operacion, Estado estado) {
        String fechaInicio = "";
        String fechaFinal = "";
        try {
            fechaInicio = new Timestamp(this.inicio.getTime()).toString().substring(0, 11);
            fechaInicio = fechaInicio + "00:00:00.000";
        } catch (Exception e) {
            fechaInicio = "Todos";
        }
        try {
            fechaFinal = new Timestamp(this.fin.getTime()).toString().substring(0, 11);
            fechaFinal = fechaFinal + "23:59:59.000";
        } catch (Exception e) {
            fechaFinal = "Todos";
        }

        String entidad = "Todos";
        if (cliente != null) {
            if (cliente.getEntidad() != null) {
                entidad = cliente.getEntidad();
            }
        }
        ArrayList<E_facturaDetalleFX> ed = DB_Ingreso.obtenerVentaDetalles(entidad, nro_factura, idEmpleado, fechaInicio, fechaFinal, tipo_operacion, estado);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.format(Calendar.getInstance().getTime());
        String nombreHoja = null;
        try {
            nombreHoja = new Timestamp(this.inicio.getTime()).toString().substring(0, 11);
        } catch (Exception e) {
            nombreHoja = sdf.format(Calendar.getInstance().getTime());
        }
        ExportarVentas ce = new ExportarVentas(nombreHoja, ed, this.inicio, this.fin, 1);
        ce.initComp();
    }

    private void importarExcelResumido(M_cliente cliente, Integer nro_factura, String idEmpleado, String tipo_operacion, Estado estado) {
        String fechaInicio = "";
        String fechaFinal = "";
        try {
            fechaInicio = new Timestamp(this.inicio.getTime()).toString().substring(0, 11);
            fechaInicio = fechaInicio + "00:00:00.000";
        } catch (Exception e) {
            fechaInicio = "Todos";
        }
        try {
            fechaFinal = new Timestamp(this.fin.getTime()).toString().substring(0, 11);
            fechaFinal = fechaFinal + "23:59:59.000";
        } catch (Exception e) {
            fechaFinal = "Todos";
        }

        String entidad = "Todos";
        if (cliente != null) {
            if (cliente.getEntidad() != null) {
                entidad = cliente.getEntidad();
            }
        }
        ArrayList<E_facturaDetalleFX> ed = DB_Ingreso.obtenerVentaDetalles(entidad, nro_factura, idEmpleado, fechaInicio, fechaFinal, tipo_operacion, estado);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.format(Calendar.getInstance().getTime());
        String nombreHoja = null;
        try {
            nombreHoja = new Timestamp(this.inicio.getTime()).toString().substring(0, 11);
        } catch (Exception e) {
            nombreHoja = sdf.format(Calendar.getInstance().getTime());
        }
        ExportarVentas ce = new ExportarVentas(nombreHoja, ed, this.inicio, this.fin, 1);
        ce.initCompResumidoFX();
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
                        importarExcelCompleto(cliente, nro_factura, idEmpleado, tipo_operacion, estado);
                    }
                });
                break;
            }
            case 1: {
                //Minimalista
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        importarExcelResumido(cliente, nro_factura, idEmpleado, tipo_operacion, estado);
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
}
