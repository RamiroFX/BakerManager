/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import Empleado.Seleccionar_funcionario;
import Entities.E_tipoOperacion;
import Entities.M_funcionario;
import Interface.GestionInterface;
import Interface.InterfaceSeleccionVentaCabecera;
import Interface.RecibirEmpleadoCallback;
import ModeloTabla.SeleccionVentaCabecera;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_cajaDetalle implements GestionInterface, RecibirEmpleadoCallback, InterfaceSeleccionVentaCabecera {

    V_cajaDetalle vista;
    M_cajaDetalle modelo;

    public C_cajaDetalle(V_cajaDetalle vista, M_cajaDetalle modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.modelo.getMovVentasTM().setInterface(this);
        this.vista.setLocationRelativeTo(null);
        callMethods();
    }

    private void callMethods() {
        inicializarVista();
        concederPermisos();
    }

    @Override
    public void inicializarVista() {
        this.vista.jtVentas.setModel(modelo.getMovVentasTM());
        Date date = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(date);
        this.vista.jddFinal.setDate(date);
    }

    @Override
    public void concederPermisos() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jbBuscar.setEnabled(true);
        this.vista.jddFinal.setEnabled(true);
        this.vista.jddInicio.setEnabled(true);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        //this.vista.jtCobros.addMouseListener(this);
        //this.vista.jtCobros.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbCancelar.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
    }

    @Override
    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    @Override
    public void cerrar() {
        this.vista.dispose();
    }

    private void consultarCajas() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Date inicio = vista.jddInicio.getDate();
                Date fin = vista.jddFinal.getDate();
                if (validarFechas(inicio, fin)) {
                    Calendar calendarioInicio = Calendar.getInstance();
                    calendarioInicio.setTime(inicio);
                    calendarioInicio.set(Calendar.HOUR_OF_DAY, 0);
                    calendarioInicio.set(Calendar.MINUTE, 0);
                    Calendar calendarioFin = Calendar.getInstance();
                    calendarioFin.setTime(fin);
                    calendarioFin.set(Calendar.HOUR_OF_DAY, 23);
                    calendarioFin.set(Calendar.MINUTE, 59);
                    Timestamp ini = new Timestamp(calendarioInicio.getTimeInMillis());
                    Timestamp fi = new Timestamp(calendarioFin.getTimeInMillis());
                    int idFuncionario = -1;
                    if (modelo.getFuncionario() != null && modelo.getFuncionario().getId_funcionario() != null) {
                        idFuncionario = modelo.getFuncionario().getId_funcionario();
                    }
                    //vista.jtCaja.setModel(modelo.consultarCajas(idFuncionario, ini, fi));
                    //Utilities.c_packColumn.packColumns(vista.jtCaja, 1);
                    //vista.jbDetalle.setEnabled(false);
                } else {
                    vista.jddFinal.setDate(vista.jddInicio.getDate());
                    vista.jddFinal.updateUI();
                    JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.setFuncionario(funcionario);
        this.vista.jtfEmpleado.setText(this.modelo.obtenerNombreFuncionario());
    }

    private void borrarDatos() {
        Date date = Calendar.getInstance().getTime();
        this.modelo.borrarDatos();
        this.vista.jtfEmpleado.setText("");
        this.vista.jddInicio.setDate(date);
        this.vista.jddFinal.setDate(date);
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

    private void actualizarSumaVentas() {
        int totalContado = 0, totalCredito = 0;
        for (SeleccionVentaCabecera seleccionVentaCabecera : modelo.getMovVentasTM().getList()) {
            switch (seleccionVentaCabecera.getFacturaCabecera().getTipoOperacion().getId()) {
                case E_tipoOperacion.CONTADO: {
                    totalContado = totalContado + seleccionVentaCabecera.getFacturaCabecera().getTotal();
                    break;
                }
                case E_tipoOperacion.CREDITO_30: {
                    totalCredito = totalCredito + seleccionVentaCabecera.getFacturaCabecera().getTotal();
                    break;
                }
            }
        }
        this.vista.jftTotalVentaContado.setValue(totalContado);
        this.vista.jftTotalVentaCredito.setValue(totalCredito);
        this.vista.jftTotalVenta.setValue(totalContado + totalCredito);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbAceptar)) {
        } else if (src.equals(this.vista.jbBuscar)) {
            consultarCajas();
        } else if (src.equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (src.equals(this.vista.jbBorrar)) {
            borrarDatos();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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

    @Override
    public void notificarCambioSeleccion() {
        actualizarSumaVentas();
    }

}
