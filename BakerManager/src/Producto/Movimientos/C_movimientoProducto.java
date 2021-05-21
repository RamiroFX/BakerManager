/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.Movimientos;

import Entities.E_movimientoProduccion;
import Entities.M_producto;
import Interface.RecibirProductoCallback;
import Producto.SeleccionarProducto;
import Utilities.c_packColumn;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class C_movimientoProducto implements ActionListener, KeyListener, RecibirProductoCallback {

    private M_movimientoProducto modelo;
    private V_movimientoProducto vista;

    public C_movimientoProducto(M_movimientoProducto modelo, V_movimientoProducto vista) {
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
        this.vista.jtMovimientos.setModel(modelo.obtenerTableModel());
        Utilities.c_packColumn.packColumns(this.vista.jtMovimientos, 1);
        this.vista.jftEntrada.setValue(0);
        this.vista.jftSalida.setValue(0);
        this.vista.jftTotal.setValue(0);
        this.vista.jftTotal.setValue(0);
    }

    private void agregarListener() {
        this.vista.jbProducto.addActionListener(this);
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

    private void invocarVistaSeleccionProducto() {
        SeleccionarProducto sc = new SeleccionarProducto(this.vista, this);
        sc.activarModoCreacion();
        sc.mostrarVista();
    }

    private void sumarTotales(int idProducto) {
        BigDecimal total = new BigDecimal("0");
        BigDecimal totalSalidas = new BigDecimal("0");
        BigDecimal totalEntradas = new BigDecimal("0");
        double cantActualAux = modelo.obtenerProducto(idProducto).getCantActual();
        BigDecimal cantActual = new BigDecimal(cantActualAux + "");
        for (E_movimientoProduccion unMov : modelo.cabeceraTableModel.getList()) {
            switch (unMov.getTipo()) {
                case E_movimientoProduccion.TIPO_PRODUCCION: {
                    totalEntradas = totalEntradas.add(new BigDecimal(unMov.getProduccionDetalle().getCantidad() + ""));
                    break;
                }
                case E_movimientoProduccion.TIPO_COMPRA: {
                    totalEntradas = totalEntradas.add(new BigDecimal(unMov.getCompraDetalle().getCantidad() + ""));
                    break;
                }
                case E_movimientoProduccion.TIPO_VENTA: {
                    totalSalidas = totalSalidas.add(new BigDecimal(unMov.getVentaDetalle().getCantidad() + ""));
                    break;
                }
                case E_movimientoProduccion.TIPO_DESPERDICIO: {
                    totalSalidas = totalSalidas.add(new BigDecimal(unMov.getDesperdicioDetalle().getCantidad() + ""));
                    break;
                }
                case E_movimientoProduccion.TIPO_INVENTARIO: {
                    double cantInventario = unMov.getInventarioDetalle().getCantidadNueva();
                    if (cantInventario > 0) {
                        totalEntradas = totalEntradas.add(new BigDecimal(cantInventario + ""));
                    } else {
                        totalSalidas = totalSalidas.add(new BigDecimal(cantInventario + ""));
                    }
                    break;
                }
            }
        }
        total = total.add(totalEntradas).subtract(totalSalidas);
        this.vista.jftEntrada.setValue(totalEntradas);
        this.vista.jftSalida.setValue(totalSalidas);
        this.vista.jftTotal.setValue(total);
        this.vista.jftCantActual.setValue(cantActual);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        } else if (ae.getSource().equals(this.vista.jbProducto)) {
            invocarVistaSeleccionProducto();
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
    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                vista.jtfProducto.setText(producto.getDescripcion());
                modelo.obtenerMovimientos(producto.getId());
                sumarTotales(producto.getId());
                c_packColumn.packColumns(vista.jtMovimientos, 1);
            }
        });
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
