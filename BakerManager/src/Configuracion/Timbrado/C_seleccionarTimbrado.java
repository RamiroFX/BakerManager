/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import Entities.E_Timbrado;
import Entities.Estado;
import Entities.M_menu_item;
import Interface.RecibirTimbradoVentaCallback;
import MenuPrincipal.DatosUsuario;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarTimbrado extends MouseAdapter implements ActionListener, KeyListener {

    private static final String ENTER_KEY = "Entrar";
    public M_seleccionarTimbrado modelo;
    public V_seleccionarTimbrado vista;

    private RecibirTimbradoVentaCallback callback;
    private boolean esModoCreacion;

    public C_seleccionarTimbrado(M_seleccionarTimbrado modelo, V_seleccionarTimbrado vista, RecibirTimbradoVentaCallback callback) {
        this.callback = callback;
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    void mostrarVista() {
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    public void activarModoCreacion() {
        this.esModoCreacion = true;
    }

    private void inicializarVista() {
        this.esModoCreacion = false;
        this.vista.jbCrearTimbrado.setEnabled(false);
        this.vista.jbAceptar.setEnabled(false);
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbCrearTimbrado.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbCrearTimbrado.setEnabled(true);
            }
        }
        this.vista.jtTimbrado.setModel(modelo.getTm());
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtTimbrado.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtTimbrado.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarTimbrado();
            }
        });
        ArrayList<Estado> estados = modelo.obtenerEstados();
        for (int i = 0; i < estados.size(); i++) {
///            this.vista.jcbEstado.addItem(estados.get(i));
        }
        ArrayList tipoFechas = modelo.getTipoFechas();
        for (int i = 0; i < tipoFechas.size(); i++) {
            this.vista.jcbTipoFecha.addItem(tipoFechas.get(i));
        }
        handleDateParams();
        Date today = Calendar.getInstance().getTime();
        this.vista.jdcFechaFinal.setDate(today);
        this.vista.jdcFechaInicio.setDate(today);
        this.vista.jtTimbrado.setModel(modelo.getTm());
        displayQueryResults();
        Utilities.c_packColumn.packColumns(vista.jtTimbrado, 1);
    }

    private void handleDateParams() {
        if (this.vista.jcbPorFecha.isSelected()) {
            this.vista.jcbTipoFecha.setEnabled(true);
            this.vista.jdcFechaFinal.setEnabled(true);
            this.vista.jdcFechaInicio.setEnabled(true);
        } else {
            this.vista.jcbTipoFecha.setEnabled(false);
            this.vista.jdcFechaFinal.setEnabled(false);
            this.vista.jdcFechaInicio.setEnabled(false);
        }
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbCrearTimbrado.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jcbTipoFecha.addActionListener(this);
        this.vista.jcbPorFecha.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtTimbrado.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jbCrearTimbrado.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jcbTipoFecha.addKeyListener(this);
        this.vista.jcbPorFecha.addKeyListener(this);
        this.vista.jtTimbrado.addKeyListener(this);
    }

    private boolean validarFechas() {
        Date inicio = vista.jdcFechaInicio.getDate();
        Date fin = vista.jdcFechaFinal.getDate();
        if (inicio != null && fin != null) {
            int dateValue = inicio.compareTo(fin);
            if (dateValue <= 0) {
                return true;
            }
        }
        vista.jdcFechaFinal.setDate(vista.jdcFechaInicio.getDate());
        vista.jdcFechaFinal.updateUI();
        JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    public void displayQueryResults() {
        /*
         * Para permitir que los mensajes puedan ser desplegados, no se ejecuta
         * el query directamente, sino que se lo coloca en una cola de eventos
         * para que se ejecute luego de los eventos pendientes.
         */

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!validarFechas()) {
                    return;
                }
                int nroTimbrado = -1;
                Estado estado = new Estado(1, "Activo");
                Date fechaInicio = vista.jdcFechaInicio.getDate();
                Date fechaFinal = vista.jdcFechaFinal.getDate();
                String tipoFecha = vista.jcbTipoFecha.getSelectedItem() + "";
                boolean esConFecha = vista.jcbPorFecha.isSelected();
                long startTime = System.nanoTime();
                modelo.getTm().setList(modelo.obtenerTimbradoVentas(fechaInicio, fechaFinal, nroTimbrado, estado, esConFecha, tipoFecha));
                long elapsedTime = System.nanoTime() - startTime;
                System.out.println("timbrados: Tiempo total de busqueda  in millis: " + elapsedTime / 1000000);
                Utilities.c_packColumn.packColumns(vista.jtTimbrado, 1);
            }
        });
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void borrarParametros() {

    }

    private void seleccionarTimbrado() {
        int fila = vista.jtTimbrado.getSelectedRow();
        int columna = vista.jtTimbrado.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            E_Timbrado timbrado = modelo.getTm().getList().get(fila);
            callback.recibirTimbrado(timbrado);
            cerrar();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            seleccionarTimbrado();
        }
        if (e.getSource() == this.vista.jbCrearTimbrado) {
            CrearTimbrado sp = new CrearTimbrado(vista);
            sp.mostrarVista();
        }
        if (e.getSource() == this.vista.jcbPorFecha) {
            handleDateParams();
        } else if (e.getSource() == this.vista.jbBuscar) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBorrar) {
            borrarParametros();
        } else if (e.getSource() == this.vista.jbSalir) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtTimbrado.rowAtPoint(e.getPoint());
        int columna = this.vista.jtTimbrado.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                seleccionarTimbrado();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
            case KeyEvent.VK_DOWN: {
                if (!e.getSource().equals(vista.jtTimbrado)) {
                    vista.jtTimbrado.requestFocusInWindow();
                }
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
