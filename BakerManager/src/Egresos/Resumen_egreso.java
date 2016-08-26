/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import Entities.M_egreso_detalleFX;
import Excel.C_create_excel;
import bakermanager.C_inicio;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class Resumen_egreso extends JDialog implements ActionListener {

    JScrollPane jspEgreso;
    JTable jtEgreso;
    JButton jbSalir, jbImportarXLS;
    JLabel jlContado, jlCredito, jlTotal;
    JFormattedTextField jftTotalEgreso, jftTotalEgCont, jftTotalEgCred;
    Date inicio, fin;
    String proveedor_entidad, idEmpleado, tipo_operacion;
    Integer nro_factura;

    public Resumen_egreso(C_inicio c_inicio, TableModel tm, String proveedor_entidad, Integer nro_factura, String idEmpleado, Date inicio, Date fin, String tipo_operacion) {
        super(c_inicio.vista, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de egresos");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(c_inicio.vista);
        this.inicio = inicio;
        this.fin = fin;
        this.proveedor_entidad = proveedor_entidad;
        this.idEmpleado = idEmpleado;
        this.tipo_operacion = tipo_operacion;
        this.nro_factura = nro_factura;
        inicializarComponentes();
        inicializarVista(tm);
        agregarListener();
    }

    private void inicializarComponentes() {
        jtEgreso = new JTable();
        this.jtEgreso.getTableHeader().setReorderingAllowed(false);
        jspEgreso = new JScrollPane(jtEgreso);
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
        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspEgreso, BorderLayout.CENTER);
        jpCenter.add(jpTotalEgreso, BorderLayout.SOUTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void inicializarVista(TableModel tm) {
        //jtEgreso.setModel(DB_Egreso.consultarResumenEgreso(tsInicio, tsFinal));
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
        jftTotalEgCred.setValue(totalCredito);
        jftTotalEgCont.setValue(totalContado);
        jftTotalEgreso.setValue(total);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    private void importarExcel(String proveedor_entidad, Integer nro_factura, String idEmpleado, String tipo_operacion) {

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

        ArrayList<M_egreso_detalleFX> ed = DB_Egreso.obtenerEgresosDetalle(proveedor_entidad, nro_factura, idEmpleado, fechaInicio, fechaFinal, tipo_operacion);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.format(Calendar.getInstance().getTime());
        System.out.println("Calendar.getInstance().getTime().toString(): " + sdf.format(Calendar.getInstance().getTime()));
        String nombreHoja = null;
        try {
            nombreHoja = new Timestamp(this.inicio.getTime()).toString().substring(0, 11);
        } catch (Exception e) {
            nombreHoja = sdf.format(Calendar.getInstance().getTime());
        }
        C_create_excel ce = new C_create_excel(nombreHoja, ed, this.inicio, this.fin);
        ce.initComp();

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(jbSalir)) {
            dispose();
        } else if (ae.getSource().equals(jbImportarXLS)) {
            importarExcel(proveedor_entidad, nro_factura, idEmpleado, tipo_operacion);
        }
    }
}
