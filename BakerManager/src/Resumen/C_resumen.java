/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Resumen;

import Excel.C_create_excel;
import bakermanager.C_inicio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_resumen implements ActionListener {

    M_resumen modelo;
    V_resumen vista;
    C_inicio inicio;

    public C_resumen(M_resumen modelo, V_resumen vista, C_inicio inicio) {
        this.inicio = inicio;
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListener();
    }

    private void inicializarVista() {
        this.vista.jtResumen.setModel(this.modelo.getRstm());
        Utilities.c_packColumn.packColumns(this.vista.jtResumen, 1);
        Integer total = 0;
        Integer totalContado = 0;
        Integer totalCredito = 0;
        switch (this.modelo.tipo) {
            case (M_resumen.RESUMEN_EGRESO): {
                int cantFilas = this.vista.jtResumen.getRowCount();
                for (int i = 0; i < cantFilas; i++) {
                    total = total + Integer.valueOf(String.valueOf(this.vista.jtResumen.getValueAt(i, 5)));
                    if (this.vista.jtResumen.getValueAt(i, 6).equals("Contado")) {
                        totalContado = totalContado + Integer.valueOf(String.valueOf(this.vista.jtResumen.getValueAt(i, 5)));
                    } else {
                        totalCredito = totalCredito + Integer.valueOf(String.valueOf(this.vista.jtResumen.getValueAt(i, 5)));
                    }
                }
                break;
            }
            case (M_resumen.RESUMEN_PEDIDO): {
                int cantFilas = this.vista.jtResumen.getRowCount();
                for (int i = 0; i < cantFilas; i++) {
                    System.err.println("total: "+this.vista.jtResumen.getValueAt(i, 5));
                    System.err.println("fecha: "+this.vista.jtResumen.getValueAt(i, 3));
                    total = total + Integer.valueOf(String.valueOf(this.vista.jtResumen.getValueAt(i, 5)));
                    if (this.vista.jtResumen.getValueAt(i, 7).equals("Contado")) {
                        totalContado = totalContado + Integer.valueOf(String.valueOf(this.vista.jtResumen.getValueAt(i, 5)));
                    } else {
                        totalCredito = totalCredito + Integer.valueOf(String.valueOf(this.vista.jtResumen.getValueAt(i, 5)));
                    }
                }
                this.vista.jtDetalle.setModel(this.modelo.obtenerDetallePedido());
                Utilities.c_packColumn.packColumns(this.vista.jtDetalle, 1);
                break;
            }
        }
        this.vista.jftTotalEgCred.setValue(totalCredito);
        this.vista.jftTotalEgCont.setValue(totalContado);
        this.vista.jftTotalEgreso.setValue(total);
    }

    private void agregarListener() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbImportarXLS.addActionListener(this);
    }

    private void importarExcel() {
        String fechaInicio = "";
        String fechaFinal = "";

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.format(Calendar.getInstance().getTime());
        String nombreHoja = null;
        try {
            nombreHoja = new Timestamp(this.modelo.getInicio().getTime()).toString().substring(0, 11);
        } catch (Exception e) {
            nombreHoja = sdf.format(Calendar.getInstance().getTime());
        }
        C_create_excel ce = new C_create_excel(nombreHoja, this.modelo.getEgresoDetalles(), this.modelo.getInicio(), this.modelo.getFin());
        ce.initComp();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        } else if (ae.getSource().equals(this.vista.jbImportarXLS)) {
            importarExcel();
        }
    }

    public void mostraVista() {
        this.vista.setVisible(true);
    }

    public void cerrar() {
        this.vista.dispose();
    }
}
