/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_manager;
import Utilities.c_packColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class BancosParametros extends javax.swing.JDialog implements ActionListener, MouseListener, KeyListener {

    private javax.swing.JButton jbCrear, jbModificar, jbEliminar;
    private JPanel jpSouth;
    JScrollPane jspBancos;
    JTable jtBancos;

    public BancosParametros(JFrame vista) {
        super(vista, true);
        setTitle("Bancos");
        setSize(new java.awt.Dimension(400, 300));
        setLocationRelativeTo(vista);
        initComponents();
        inicializarVista();
        agregarListener();
    }

    public BancosParametros(JDialog vista) {
        super(vista, true);
        setTitle("Bancos");
        setSize(new java.awt.Dimension(400, 300));
        setLocationRelativeTo(vista);
        initComponents();
        inicializarVista();
        agregarListener();
    }

    private void inicializarVista() {
        jbEliminar.setEnabled(false);
        jbModificar.setEnabled(false);
        jtBancos.setModel(DB_manager.consultarBanco());
        c_packColumn.packColumns(jtBancos, 1);
    }

    private void initMarcas() {
        jtBancos = new JTable();
        jtBancos.getTableHeader().setReorderingAllowed(false);
        jspBancos = new JScrollPane(jtBancos);

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
        getContentPane().add(jspBancos, "dock center");
        getContentPane().add(jpSouth, "dock south");
    }

    private void agregarListener() {
        jtBancos.addMouseListener(this);
        jbCrear.addActionListener(this);
        jbModificar.addActionListener(this);
        jbEliminar.addActionListener(this);
        /*
        KEYLISTENERS
         */
        jtBancos.addKeyListener(this);
        jbCrear.addKeyListener(this);
        jbModificar.addKeyListener(this);
        jbEliminar.addKeyListener(this);
    }

    private void agregarBanco(String banco) {
        String m = banco.trim();
        if (m.length() < 1 || m.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (m.length() > 50) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 50 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdBanco(m);
        if (b == null) {
            DB_manager.insertarBanco(m);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtBancos.setModel(DB_manager.consultarBanco());
            c_packColumn.packColumns(jtBancos, 1);
        } else {
            JOptionPane.showMessageDialog(this, "Banco existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarBanco(String bancoDescripcion) {
        String m = bancoDescripcion.trim();
        if (m.length() < 1 || m.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (m.length() > 50) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 50 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdBanco(m);
        if (b == null) {
            int idBanco = Integer.valueOf(String.valueOf(this.jtBancos.getValueAt(jtBancos.getSelectedRow(), 0)));
            DB_manager.modificarBanco(idBanco, bancoDescripcion);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtBancos.setModel(DB_manager.consultarBanco());
            c_packColumn.packColumns(jtBancos, 1);
        } else {
            JOptionPane.showMessageDialog(this, "Banco existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarBanco() {
        int idBanco = Integer.valueOf(String.valueOf(this.jtBancos.getValueAt(jtBancos.getSelectedRow(), 0)));
        boolean m = DB_manager.bancoEnUso(idBanco);
        if (m) {
            int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    DB_manager.eliminarBanco(idBanco);
                    this.jbModificar.setEnabled(false);
                    this.jbEliminar.setEnabled(false);
                    this.jtBancos.setModel(DB_manager.consultarBanco());
                    c_packColumn.packColumns(jtBancos, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Existe cheques que se encuentran utilizando el banco seleccionado.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createButtonHandler() {
        String marca = JOptionPane.showInputDialog(this, "Inserte el nombre del banco", "Insertar marca", JOptionPane.PLAIN_MESSAGE);
        if (marca != null) {
            if (!marca.isEmpty()) {
                agregarBanco(marca);
            }
        }
    }

    private void updateButtonHandler() {
        String marca = JOptionPane.showInputDialog(this, "Inserte el nombre del banco", "Modificar marca", JOptionPane.PLAIN_MESSAGE);
        if (marca != null) {
            if (!marca.isEmpty()) {
                modificarBanco(marca);
            }
        }
    }

    private void deleteButtonHandler() {
        eliminarBanco();
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
        if (e.getSource().equals(this.jtBancos)) {
            int fila = this.jtBancos.rowAtPoint(e.getPoint());
            int columna = this.jtBancos.columnAtPoint(e.getPoint());
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
