/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Cobro;
import DB.DB_Pago;
import Entities.E_cuentaCorrienteConcepto;
import Entities.E_cuentaCorrienteDetalle;
import ModeloTabla.ChequesPendienteTableModel;
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
public class ChequesPendientes extends javax.swing.JDialog implements ActionListener, MouseListener, KeyListener {

    private javax.swing.JButton jbCobrar, jbCerrar;
    private JPanel jpSouth;
    JScrollPane jspCheques;
    JTable jtCheques;
    ChequesPendienteTableModel tm;
    private int concepto;

    public ChequesPendientes(JFrame vista, int tipoConcepto) {
        super(vista, true);
        setTitle("Cheques diferidos pendientes");
        setSize(new java.awt.Dimension(900, 300));
        setLocationRelativeTo(vista);
        this.concepto = tipoConcepto;
        initComponents();
        inicializarVista();
        agregarListener();
    }

    public ChequesPendientes(JDialog vista, int tipoConcepto) {
        super(vista, true);
        setTitle("Cheques diferidos pendientes");
        setSize(new java.awt.Dimension(700, 300));
        setLocationRelativeTo(vista);
        this.concepto = tipoConcepto;
        initComponents();
        inicializarVista();
        agregarListener();
    }

    private void inicializarVista() {
        jbCobrar.setEnabled(false);
        switch (this.concepto) {
            case E_cuentaCorrienteConcepto.COMPRAS: {
                tm.setList(DB_Pago.obtenerChequesPendientes());
                break;
            }
            case E_cuentaCorrienteConcepto.VENTAS: {
                tm.setList(DB_Cobro.obtenerChequesPendientes());
                break;
            }
        }
        jtCheques.setModel(tm);
        c_packColumn.packColumns(jtCheques, 1);
    }

    private void initMarcas() {
        jtCheques = new JTable();
        jtCheques.getTableHeader().setReorderingAllowed(false);
        jspCheques = new JScrollPane(jtCheques);

    }

    private void initComponents() {
        tm = new ChequesPendienteTableModel(concepto);
        initMarcas();
        jpSouth = new JPanel();
        jbCobrar = new javax.swing.JButton("Cobrar");
        jbCerrar = new javax.swing.JButton("Cerrar");
        jpSouth.add(jbCobrar);
        jpSouth.add(jbCerrar);
        getContentPane().setLayout(new MigLayout());
        getContentPane().add(jspCheques, "dock center");
        getContentPane().add(jpSouth, "dock south");
    }

    private void agregarListener() {
        jtCheques.addMouseListener(this);
        jbCobrar.addActionListener(this);
        jbCerrar.addActionListener(this);
        /*
        KEYLISTENERS
         */
        jtCheques.addKeyListener(this);
        jbCobrar.addKeyListener(this);
        jbCerrar.addKeyListener(this);
    }

    private void cobrarCheque() {
        String mensaje = "";
        switch (this.concepto) {
            case E_cuentaCorrienteConcepto.COMPRAS: {
                mensaje = "¿Está seguro que desea pagar el cheque seleccionado?";
                break;
            }
            case E_cuentaCorrienteConcepto.VENTAS: {
                mensaje = "¿Está seguro que desea cobrar el cheque seleccionado?";
                break;
            }
        }
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        int selectedRow = jtCheques.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        E_cuentaCorrienteDetalle ccd = tm.getList().get(selectedRow);
        DB_Cobro.cobrarCheque(ccd.getId());
        switch (this.concepto) {
            case E_cuentaCorrienteConcepto.COMPRAS: {
                DB_Pago.cobrarCheque(ccd.getId());
                tm.setList(DB_Pago.obtenerChequesPendientes());
                break;
            }
            case E_cuentaCorrienteConcepto.VENTAS: {
                DB_Cobro.cobrarCheque(ccd.getId());
                tm.setList(DB_Cobro.obtenerChequesPendientes());
                break;
            }
        }
        c_packColumn.packColumns(jtCheques, 1);
    }

    private void cerrar() {
        System.runFinalization();
        this.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbCobrar)) {
            cobrarCheque();
        }
        if (e.getSource().equals(jbCerrar)) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.jtCheques)) {
            int fila = this.jtCheques.rowAtPoint(e.getPoint());
            int columna = this.jtCheques.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.jbCobrar.setEnabled(true);
            } else {
                this.jbCobrar.setEnabled(false);
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
