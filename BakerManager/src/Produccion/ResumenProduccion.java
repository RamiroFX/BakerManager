/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Entities.E_facturacionCabecera;
import Entities.E_produccionTipo;
import Entities.Estado;
import Entities.M_funcionario;
import Interface.InterfaceFacturaDetalle;
import ModeloTabla.ProduccionCabeceraTableModel;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ResumenProduccion extends JDialog implements ActionListener, KeyListener, InterfaceFacturaDetalle {

    JScrollPane jspEgreso, jspDetalle;
    JTable jtEgreso, jtDetalle;
    JButton jbSalir, jbImportarXLS, jbImprimirFacturacion;
    JLabel jlContado, jlCredito, jlTotal;
    JFormattedTextField jftTotalEgreso, jftTotalEgCont, jftTotalEgCred;
    Date inicio, fin;
    M_funcionario funcionario;
    E_produccionTipo tipoProduccion;
    Integer nroOrdenProd;
    JTabbedPane jtpPanel;
    E_facturacionCabecera facturacionCabecera;
    Estado estado;

    public ResumenProduccion(JFrame frame, ProduccionCabeceraTableModel tm, Integer nroOrdenProd, 
            M_funcionario funcionario, Date inicio, Date fin,
            E_produccionTipo tipoProduccion, Estado estado) {
        super(frame, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de producción");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        this.inicio = inicio;
        this.fin = fin;
        this.funcionario = funcionario;
        this.tipoProduccion = tipoProduccion;
        this.nroOrdenProd = nroOrdenProd;
        this.estado = estado;
        inicializarComponentes();
        inicializarVista(tm, inicio, fin);
        agregarListener();
    }

    public void inicializarDatos(E_facturacionCabecera facturacionCabecera) {
    }

    private void inicializarComponentes() {
    }

    private void inicializarVista(TableModel tm, Date inicio, Date fin) {
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent ae) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void cerrar() {
        this.dispose();
    }

    @Override
    public void notificarCambioFacturaDetalle() {
    }
}
