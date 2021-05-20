/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.EstadoCuenta;

import Cliente.SeleccionarCliente;
import Entities.E_movimientoContable;
import Entities.M_cliente;
import Interface.RecibirClienteCallback;
import Utilities.c_packColumn;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class C_estadoCuenta implements ActionListener, KeyListener, RecibirClienteCallback {

    private M_estadoCuenta modelo;
    private V_estadoCuenta vista;

    public C_estadoCuenta(M_estadoCuenta modelo, V_estadoCuenta vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListener();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void cerrar() {
        this.vista.dispose();
    }

    private void inicializarVista() {
        this.vista.jtCobros.setModel(modelo.obtenerTableModel());
        Utilities.c_packColumn.packColumns(this.vista.jtCobros, 1);
        BigInteger total = new BigInteger("0");
        BigInteger totalCheque = new BigInteger("0");
        BigInteger totalEfectivo = new BigInteger("0");
        total = total.add(totalCheque).add(totalEfectivo);
        this.vista.jftTotalEfectivo.setValue(totalEfectivo);
        this.vista.jftTotalCheque.setValue(totalCheque);
        this.vista.jftTotalCobrado.setValue(total);
    }

    private void agregarListener() {
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbImportarXLS.addActionListener(this);
    }

    private void importarExcelIndividual() {
    }

    private void importarExcelResumido() {
    }

    private void exportHandler() {
        Object[] options = {"Individual",
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
                //Individual
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        importarExcelIndividual();
                    }
                });
                break;
            }
            case 1: {
                //Resumido
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

    private void invocarVistaSeleccionCliente() {
        SeleccionarCliente sc = new SeleccionarCliente(vista);
        sc.setCallback(this);
        sc.mostrarVista();
    }

    private void sumarTotales() {
        this.vista.jtCobros.setModel(modelo.obtenerTableModel());
        BigDecimal balance = new BigDecimal("0");
        BigDecimal totalDebe = new BigDecimal("0");
        BigDecimal totalHaber = new BigDecimal("0");
        for (E_movimientoContable unMov : modelo.cabeceraTableModel.getList()) {
            switch (unMov.getTipo()) {
                case E_movimientoContable.TIPO_SALDO_INICIAL: {
                    totalHaber = totalHaber.add(new BigDecimal(unMov.getClienteSaldoInicial().getSaldoInicial() + ""));
                    break;
                }
                case E_movimientoContable.TIPO_VENTA: {
                    totalHaber = totalHaber.add(new BigDecimal(unMov.getVenta().getMonto() + ""));
                    break;
                }
                case E_movimientoContable.TIPO_COBRO: {
                    totalDebe = totalDebe.add(new BigDecimal(unMov.getCobro().getMonto() + ""));
                    break;
                }
                case E_movimientoContable.TIPO_NOTA_CREDITO: {
                    totalDebe = totalDebe.add(new BigDecimal(unMov.getNotaCredito().getTotal() + ""));
                    break;
                }
                case E_movimientoContable.TIPO_RETENCION_VENTA: {
                    totalDebe = totalDebe.add(new BigDecimal(unMov.getRetencionVenta().getMonto() + ""));
                    break;
                }
            }
        }
        balance = balance.add(totalHaber).subtract(totalDebe);
        this.vista.jftTotalEfectivo.setValue(totalHaber);
        this.vista.jftTotalCheque.setValue(totalDebe);
        this.vista.jftTotalCobrado.setValue(balance);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        } else if (ae.getSource().equals(this.vista.jbCliente)) {
            invocarVistaSeleccionCliente();
        } else if (ae.getSource().equals(this.vista.jbImportarXLS)) {
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

    @Override
    public void recibirCliente(final M_cliente cliente) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                vista.jtfCliente.setText(cliente.getClienteDescripcion());
                modelo.obtenerEstadoCuenta(cliente.getIdCliente());
                sumarTotales();
                c_packColumn.packColumns(vista.jtCobros, 1);
            }
        });
    }

}
