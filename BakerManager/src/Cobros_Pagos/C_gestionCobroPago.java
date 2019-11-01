/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros_Pagos;

import Empleado.Seleccionar_funcionario;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Interface.GestionInterface;
import MenuPrincipal.DatosUsuario;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionCobroPago implements GestionInterface {

    V_gestionCobroPago vista;
    M_gestionCobroPago modelo;
    public C_inicio c_inicio;

    public C_gestionCobroPago(V_gestionCobroPago vista, M_gestionCobroPago modelo, C_inicio c_inicio) {
        this.vista = vista;
        this.modelo = modelo;
        this.c_inicio = c_inicio;
        this.vista.setLocation(c_inicio.centrarPantalla(this.vista));
        callMethods();
    }

    private void callMethods() {
        inicializarVista();
        concederPermisos();
    }

    @Override
    public void inicializarVista() {
        Date date = Calendar.getInstance().getTime();
        this.vista.jddInicioCobro.setDate(date);
        this.vista.jddFinalCobro.setDate(date);

        this.vista.jddInicioPago.setDate(date);
        this.vista.jddFinalPago.setDate(date);
    }

    @Override
    public void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
            if (this.vista.jbCobro.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbCobro.setEnabled(true);
                this.vista.jbCobro.addActionListener(this);
            }
            /*if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbResumen.addActionListener(this);
            }*/
            if (this.vista.jbDetalleCobro.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbDetalleCobro.addActionListener(this);
            }
            if (this.vista.jbBuscarCobro.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbBuscarCobro.setEnabled(true);
                this.vista.jddFinalCobro.setEnabled(true);
                this.vista.jddInicioCobro.setEnabled(true);
                this.vista.jbBuscarCobro.addActionListener(this);
                this.vista.jbEmpCobro.addActionListener(this);
                this.vista.jbBorrarCobro.addActionListener(this);
            }
            //PAGO
            if (this.vista.jbPago.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbPago.setEnabled(true);
                this.vista.jbPago.addActionListener(this);
            }
            /*if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbResumen.addActionListener(this);
            }*/
            if (this.vista.jbDetallePago.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbDetallePago.addActionListener(this);
            }
            if (this.vista.jbBuscarPago.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbBuscarPago.setEnabled(true);
                this.vista.jddFinalPago.setEnabled(true);
                this.vista.jddInicioPago.setEnabled(true);
                this.vista.jbBuscarPago.addActionListener(this);
                this.vista.jbEmpPago.addActionListener(this);
                this.vista.jbBorrarPago.addActionListener(this);
            }
        }
        this.vista.jtCobroCabecera.addMouseListener(this);
        this.vista.jtCobroCabecera.addKeyListener(this);
        this.vista.jtPago.addMouseListener(this);
        this.vista.jtPago.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        //cobro
        this.vista.jbCobro.addKeyListener(this);
        this.vista.jbDetalleCobro.addKeyListener(this);
        this.vista.jbBuscarCobro.addKeyListener(this);
        this.vista.jbEmpCobro.addKeyListener(this);
        this.vista.jbBorrarCobro.addKeyListener(this);
        //pago
        this.vista.jbPago.addKeyListener(this);
        this.vista.jbDetallePago.addKeyListener(this);
        this.vista.jbBuscarPago.addKeyListener(this);
        this.vista.jbEmpPago.addKeyListener(this);
        this.vista.jbBorrarPago.addKeyListener(this);
    }

    @Override
    public void mostrarVista() {
        this.c_inicio.agregarVentana(vista);
    }

    @Override
    public void cerrar() {
        try {
            this.vista.setClosed(true);
        } catch (PropertyVetoException ex) {
        }
    }

    private void invocarVistaSaldarCaja() {
        //TO DO
    }

    private void invocarVistaVerCaja() {
        int fila = this.vista.jtCobroCabecera.getSelectedRow();
        int columna = this.vista.jtCobroCabecera.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            int idCaja = Integer.valueOf(String.valueOf(this.vista.jtCobroCabecera.getValueAt(fila, 0)));
            ////TO DO
        }
        this.vista.jbDetalleCobro.setEnabled(false);
    }

    private void consultarCobros() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Date inicio = vista.jddInicioCobro.getDate();
                Date fin = vista.jddFinalCobro.getDate();
                if (validarFechas(inicio, fin)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String fecha_inicio = sdf.format(vista.jddInicioCobro.getDate());
                    String fecha_fin = sdf.format(vista.jddFinalCobro.getDate());
                    int idFuncionario = -1;
                    if (modelo.getFuncionario() != null && modelo.getFuncionario().getId_funcionario() != null) {
                        idFuncionario = modelo.getFuncionario().getId_funcionario();
                    }
                    vista.jtCobroCabecera.setModel(modelo.consultarCajas(idFuncionario, fecha_inicio, fecha_fin));
                    Utilities.c_packColumn.packColumns(vista.jtCobroCabecera, 1);
                    vista.jbDetalleCobro.setEnabled(false);
                } else {
                    vista.jddFinalCobro.setDate(vista.jddInicioCobro.getDate());
                    vista.jddFinalCobro.updateUI();
                    JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.setFuncionario(funcionario);
        String alias = this.modelo.getFuncionario().getAlias();
        String nombre = this.modelo.getFuncionario().getNombre();
        String apellido = this.modelo.getFuncionario().getApellido();
        this.vista.jtfEmpCobro.setText(alias + "-(" + nombre + " " + apellido + ")");
    }

    private void borrarDatos() {
        Date date = Calendar.getInstance().getTime();
        this.modelo.borrarDatos();
        this.vista.jtfEmpCobro.setText("");
        this.vista.jddInicioCobro.setDate(date);
        this.vista.jddFinalCobro.setDate(date);
    }

    private boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbCobro)) {
            invocarVistaSaldarCaja();
        } else if (src.equals(this.vista.jbBuscarCobro)) {
            consultarCobros();
        } else if (src.equals(this.vista.jbEmpCobro)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this);
            sf.mostrarVista();
        } else if (src.equals(this.vista.jbBorrarCobro)) {
            borrarDatos();
        } else if (src.equals(this.vista.jbDetalleCobro)) {
            invocarVistaVerCaja();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtCobroCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtCobroCabecera.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbDetalleCobro.setEnabled(true);
            if (e.getClickCount() == 2) {
                invocarVistaVerCaja();
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
            case KeyEvent.VK_F1: {
                if (vista.jbCobro.isEnabled()) {
                    invocarVistaSaldarCaja();
                }
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
