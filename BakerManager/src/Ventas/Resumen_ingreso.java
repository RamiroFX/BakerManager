/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Ingreso;
import Entities.M_cliente;
import bakermanager.C_inicio;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro
 */
public class Resumen_ingreso extends JDialog implements ActionListener {

    JScrollPane jspEgreso, jspDetalle;
    JTable jtEgreso, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlContado, jlCredito, jlTotal;
    JFormattedTextField jftTotalEgreso, jftTotalEgCont, jftTotalEgCred;
    Date inicio, fin;
    String idEmpleado, tipo_operacion;
    Integer nro_factura;
    M_cliente cliente_entidad;

    public Resumen_ingreso(C_inicio c_inicio, TableModel tm, M_cliente cliente_entidad, Integer nro_factura, String idEmpleado, Date inicio, Date fin, String tipo_operacion) {
        super(c_inicio.vista, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de egresos");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(c_inicio.vista);
        this.inicio = inicio;
        this.fin = fin;
        this.cliente_entidad = cliente_entidad;
        this.idEmpleado = idEmpleado;
        this.tipo_operacion = tipo_operacion;
        this.nro_factura = nro_factura;
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
        jlContado = new JLabel("Egresos al contado");
        jlContado.setHorizontalAlignment(SwingConstants.CENTER);
        jlCredito = new JLabel("Egresos a crédito");
        jlCredito.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotal = new JLabel("Total egresos");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalEgreso.add(jlContado);
        jpTotalEgreso.add(jftTotalEgCont);
        jpTotalEgreso.add(jlCredito);
        jpTotalEgreso.add(jftTotalEgCred);
        jpTotalEgreso.add(jlTotal);
        jpTotalEgreso.add(jftTotalEgreso);
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        JTabbedPane jtpPanel = new JTabbedPane();

        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //jpSouth.add(jbImportarXLS);
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
            total = total + Integer.valueOf(String.valueOf(jtEgreso.getValueAt(i, 4)));
            if (jtEgreso.getValueAt(i, 5).equals("Contado")) {
                totalContado = totalContado + Integer.valueOf(String.valueOf(jtEgreso.getValueAt(i, 4)));
            } else {
                totalCredito = totalCredito + Integer.valueOf(String.valueOf(jtEgreso.getValueAt(i, 4)));
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
        jtDetalle.setModel(DB_Ingreso.consultarIngresoDetalleAgrupado(fInicio, fFin, cliente_entidad));
        jftTotalEgCred.setValue(totalCredito);
        jftTotalEgCont.setValue(totalContado);
        jftTotalEgreso.setValue(total);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    private void importarExcel(String proveedor_entidad, Integer nro_factura, String idEmpleado, String tipo_operacion) {
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(jbSalir)) {
            dispose();
        } else if (ae.getSource().equals(jbImportarXLS)) {
            //importarExcel(cliente_entidad, nro_factura, idEmpleado, tipo_operacion);
        }
    }
}
