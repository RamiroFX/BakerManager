/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock.Parametros;

import DB.DB_Inventario;
import Entities.E_ajusteStockMotivo;
import ModeloTabla.AjusteStockMotivoTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class AjusteStockParametros extends javax.swing.JDialog implements ActionListener, MouseListener, KeyListener {

    private static final int MAX_LENGTH = 50;

    private javax.swing.JButton jbCrear, jbModificar, jbEliminar;
    private JPanel jpSouth;
    JScrollPane jspMotivoAjuste;
    JTable jtMotivos;
    private AjusteStockMotivoTableModel ajusteStockMotivoTM;

    public AjusteStockParametros(JDialog frame) {
        super(frame, true);
        construirLayout(frame);
        initComponents();
        inicializarVista();
        initializarLogica();
        agregarListener();
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    private void construirLayout(JDialog frame) {
        setTitle("Parametros");
        setSize(new java.awt.Dimension(500, 300));
        setLocationRelativeTo(frame);
    }

    private void initializarLogica() {
        this.ajusteStockMotivoTM = new AjusteStockMotivoTableModel();
        jtMotivos.setModel(ajusteStockMotivoTM);
        ajusteStockMotivoTM.setList(DB_Inventario.consultarAjusteStockMotivo());
    }

    private void inicializarVista() {
        jbEliminar.setEnabled(false);
        jbModificar.setEnabled(false);
        Utilities.c_packColumn.packColumns(jtMotivos, 1);
    }

    private void initMarcas() {
        jtMotivos = new JTable();
        jtMotivos.getTableHeader().setReorderingAllowed(false);
        jspMotivoAjuste = new JScrollPane(jtMotivos);
    }

    private void initComponents() {
        initMarcas();
        jpSouth = new JPanel();
        jbCrear = new javax.swing.JButton("Agregar");
        jbModificar = new javax.swing.JButton("Modificar");
        jbEliminar = new javax.swing.JButton("Eliminar");
        jpSouth.add(jbCrear);
        jpSouth.add(jbModificar);
        jpSouth.add(jbEliminar);
        getContentPane().setLayout(new MigLayout());
        getContentPane().add(jspMotivoAjuste, "dock center");
        getContentPane().add(jpSouth, "dock south");
    }

    private void agregarListener() {
        jtMotivos.addMouseListener(this);
        jbCrear.addActionListener(this);
        jbModificar.addActionListener(this);
        jbEliminar.addActionListener(this);
        /*
        KEYLISTENERS
         */
        jtMotivos.addKeyListener(this);
        jbCrear.addKeyListener(this);
        jbModificar.addKeyListener(this);
        jbEliminar.addKeyListener(this);
    }

    private void actualizarTabla() {
        ajusteStockMotivoTM.setList(DB_Inventario.consultarAjusteStockMotivo());
    }

    private void agregarMotivo(String motivo, String obs) {
        String m = motivo.trim();
        if (m.length() < 1 || m.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (m.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this, "Máximo permitido " + MAX_LENGTH + " caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean b = DB_Inventario.existeAjusteMotivoEnUso(motivo);
        if (b) {
            JOptionPane.showMessageDialog(this, "Motivo existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        } else {
            E_ajusteStockMotivo unMotivo = new E_ajusteStockMotivo();
            unMotivo.setDescripcion(motivo);
            unMotivo.setObservacion(obs);
            DB_Inventario.insertarAjusteStockMotivo(unMotivo);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            actualizarTabla();
        }
    }

    private void modificarMotivo(String motivo, String obs) {
        String m = motivo.trim();
        if (m.length() < 1 || m.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (m.length() > MAX_LENGTH) {
            JOptionPane.showMessageDialog(this, "Máximo permitido " + MAX_LENGTH + " caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean b = DB_Inventario.existeAjusteMotivoEnUso(motivo);
        if (b) {
            JOptionPane.showMessageDialog(this, "Motivo existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        } else {
            int index = jtMotivos.getSelectedRow();
            int idMotivo = ajusteStockMotivoTM.getList().get(index).getId();
            E_ajusteStockMotivo unMotivo = new E_ajusteStockMotivo();
            unMotivo.setId(idMotivo);
            unMotivo.setDescripcion(motivo);
            unMotivo.setObservacion(obs);
            DB_Inventario.actualizarAjusteStockMotivo(unMotivo);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            actualizarTabla();
        }
    }

    private void eliminarMotivo() {
        int index = jtMotivos.getSelectedRow();
        int idMotivo = ajusteStockMotivoTM.getList().get(index).getId();
        boolean m = DB_Inventario.existeAjusteMotivoEnUso(idMotivo);
        if (m) {
            JOptionPane.showMessageDialog(this, "Existe detalles de ajuste de stock que se encuentran utilizando el motivo seleccionado.", "Alerta", JOptionPane.ERROR_MESSAGE);
        } else {
            int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    DB_Inventario.eliminarAjusteStockMotivo(idMotivo);
                    this.jbModificar.setEnabled(false);
                    this.jbEliminar.setEnabled(false);
                    actualizarTabla();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createButtonHandler() {
        String marca = JOptionPane.showInputDialog(this, "Inserte el nombre del motivo", "Insertar marca", JOptionPane.PLAIN_MESSAGE);
        String observacion = JOptionPane.showInputDialog(this, "Inserte una descripción motivo", "Insertar marca", JOptionPane.PLAIN_MESSAGE);
        if (marca != null) {
            if (!marca.isEmpty()) {
                agregarMotivo(marca, observacion);
            }
        }

    }

    private void updateButtonHandler() {
        String motivo = JOptionPane.showInputDialog(this, "Inserte el nombre del motivo", "Modificar marca", JOptionPane.PLAIN_MESSAGE);
        String observacion = JOptionPane.showInputDialog(this, "Inserte una descripción motivo", "Insertar marca", JOptionPane.PLAIN_MESSAGE);
        if (motivo != null) {
            if (!motivo.isEmpty()) {
                modificarMotivo(motivo, observacion);
            }
        }
    }

    private void deleteButtonHandler() {
        eliminarMotivo();
    }

    private void cerrar() {
        System.runFinalization();
        this.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbCrear)) {
            createButtonHandler();
        }
        if (e.getSource().equals(jbModificar)) {
            updateButtonHandler();
        }
        if (e.getSource().equals(jbEliminar)) {
            deleteButtonHandler();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.jtMotivos)) {
            int fila = this.jtMotivos.rowAtPoint(e.getPoint());
            int columna = this.jtMotivos.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.jbModificar.setEnabled(true);
                this.jbEliminar.setEnabled(true);
            } else {
                this.jbModificar.setEnabled(false);
                this.jbEliminar.setEnabled(false);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
