/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Resumen;

import Entities.E_facturaCabecera;
import Entities.E_tipoOperacion;
import Excel.ExportarPedidos;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

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
        this.vista.jtResumen.setModel(this.modelo.getTm());
        this.vista.jtDetalle.setModel(this.modelo.getTmDetalle());
        Utilities.c_packColumn.packColumns(this.vista.jtResumen, 1);
        Integer total = 0;
        Integer totalContado = 0;
        Integer totalCredito = 0;
        for (E_facturaCabecera fade : modelo.getTm().getList()) {
            switch (fade.getTipoOperacion().getId()) {
                case E_tipoOperacion.CONTADO: {
                    totalContado = totalContado + fade.getTotal();
                    break;
                }
                case E_tipoOperacion.CREDITO_30: {
                    totalCredito = totalCredito + fade.getTotal();
                    break;
                }
                default: {
                    totalCredito = totalCredito + fade.getTotal();
                    break;
                }
            }
        }
        total = totalContado + totalCredito;
        this.vista.jftTotalEgCred.setValue(totalCredito);
        this.vista.jftTotalEgCont.setValue(totalContado);
        this.vista.jftTotalEgreso.setValue(total);
    }

    private void agregarListener() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbImportarXLS.addActionListener(this);
    }

    private void importarExcelCompleto() {
        ExportarPedidos ce = new ExportarPedidos("Pedidos", modelo.getFechaInicio(), modelo.getFechaInicio(), modelo.getTm().getList());
        ce.exportacionCompleta();
    }

    private void importarExcelResumido() {
        ExportarPedidos ce = new ExportarPedidos("Pedidos", modelo.getFechaInicio(), modelo.getFechaInicio(), modelo.getTm().getList());
        ce.exportacionResumida();
    }

    private void exportHandler() {
        Object[] options = {"Completo",
            "Resumido"};
        int n = JOptionPane.showOptionDialog(this.vista,
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        } else if (ae.getSource().equals(this.vista.jbImportarXLS)) {
            exportHandler();
        }
    }

    public void mostraVista() {
        this.vista.setVisible(true);
    }

    public void cerrar() {
        this.vista.dispose();
    }
}
