/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearVentas extends MouseAdapter implements ActionListener {

    V_crearVentas vista;
    M_crearVentas modelo;
    C0_gestionVentas gestionVentas;

    public C_crearVentas(V_crearVentas vista, M_crearVentas modelo, C0_gestionVentas gestionVentas) {
        this.vista = vista;
        this.modelo = modelo;
        this.gestionVentas = gestionVentas;
        inicializarComponentes();
        agregarListeners();
    }

    void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void inicializarComponentes() {
        this.vista.jtfFuncionario.setText(this.gestionVentas.c_inicio.modelo.getRol_usuario().getFuncionario().getAlias());
        this.vista.jftTiempo.setValue(Calendar.getInstance().getTime());
        this.vista.jbVerMesa.setEnabled(false);
        this.vista.jbEliminarMesa.setEnabled(false);
        this.vista.jtMesa.setModel(this.modelo.getRstmMesa());
        this.vista.jbVentaRapida.requestFocusInWindow();
        Utilities.c_packColumn.packColumns(this.vista.jtMesa, 1);
    }

    private void agregarListeners() {
        this.vista.jbAñadirMesa.addActionListener(this);
        this.vista.jbEliminarMesa.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbVentaRapida.addActionListener(this);
        this.vista.jbVerMesa.addActionListener(this);
        this.vista.jtMesa.addMouseListener(this);
    }

    public void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtMesa)) {
            facturaCabeceraHandler(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbAñadirMesa)) {
            añadirMesa();
        }
        if (e.getSource().equals(this.vista.jbEliminarMesa)) {
            eliminarMesa();
        }
        if (e.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        }
        if (e.getSource().equals(this.vista.jbVentaRapida)) {
            crearVentaRapida();
        }
        if (e.getSource().equals(this.vista.jbVerMesa)) {
            verMesa();
        }
    }

    private void facturaCabeceraHandler(MouseEvent e) {
        int fila = this.vista.jtMesa.rowAtPoint(e.getPoint());
        int columna = this.vista.jtMesa.columnAtPoint(e.getPoint());
        Integer idMesa = Integer.valueOf(String.valueOf(this.vista.jtMesa.getValueAt(fila, 0)));
        if ((fila > -1) && (columna > -1)) {
            this.modelo.setMesa(idMesa);
            this.modelo.setRstmMesaDetalle(idMesa);
            this.vista.jbVerMesa.setEnabled(true);
            this.vista.jbEliminarMesa.setEnabled(true);
            this.vista.jtMesaDetalle.setModel(this.modelo.getRstmMesaDetalle());
        } else {
            this.vista.jbVerMesa.setEnabled(false);
            this.vista.jbEliminarMesa.setEnabled(false);
            this.vista.jtMesaDetalle.setModel(this.modelo.getDtm());
        }
        if (e.getClickCount() == 2) {
            verMesa();
            this.vista.jbVerMesa.setEnabled(false);
            this.vista.jbEliminarMesa.setEnabled(false);
            this.vista.jtMesaDetalle.setModel(this.modelo.getDtm());
        }
    }

    private void verMesa() {
        int idMesa = Integer.valueOf(String.valueOf(this.vista.jtMesa.getValueAt(this.vista.jtMesa.getSelectedRow(), 0)));
        Ver_mesa vi = new Ver_mesa(this, idMesa);
        vi.mostrarVista();
        this.vista.jbVerMesa.setEnabled(false);
        this.vista.jbEliminarMesa.setEnabled(false);
        this.vista.jtMesaDetalle.setModel(this.modelo.getDtm());
    }

    private void añadirMesa() {
        ConfigurarMesa confMesa = new ConfigurarMesa(this);
        confMesa.mostrarVista();
        this.vista.jbVerMesa.setEnabled(false);
        this.vista.jbEliminarMesa.setEnabled(false);
        this.vista.jtMesaDetalle.setModel(this.modelo.getDtm());
    }

    private void eliminarMesa() {
        int option = JOptionPane.showConfirmDialog(this.vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int row = this.vista.jtMesa.getSelectedRow();
                int idMesa = Integer.valueOf(String.valueOf(this.vista.jtMesa.getValueAt(row, 0)));
                this.modelo.eliminarMesa(idMesa);
                this.modelo.actualizarTablaMesa();
                this.vista.jtMesa.setModel(this.modelo.getRstmMesa());
                Utilities.c_packColumn.packColumns(this.vista.jtMesa, 1);
                this.vista.jbEliminarMesa.setEnabled(false);
                this.vista.jbVerMesa.setEnabled(false);
                this.vista.jtMesaDetalle.setModel(this.modelo.getDtm());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.vista.jbVerMesa.setEnabled(false);
        this.vista.jbEliminarMesa.setEnabled(false);
        this.vista.jtMesaDetalle.setModel(this.modelo.getDtm());
    }

    private void crearVentaRapida() {
        CrearVentaRapida crv = new CrearVentaRapida(this.gestionVentas);
        crv.mostrarVista();
    }

    public void actualizarTablaMesa() {
        this.modelo.actualizarTablaMesa();
        this.vista.jtMesa.setModel(this.modelo.getRstmMesa());
        Utilities.c_packColumn.packColumns(this.vista.jtMesa, 1);
    }
}
