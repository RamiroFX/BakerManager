/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import DB.DB_Produccion;
import Empleado.Seleccionar_funcionario;
import Entities.E_produccionCabecera;
import Entities.E_produccionTipo;
import Entities.Estado;
import Entities.M_funcionario;
import Excel.ExportarProduccion;
import Interface.RecibirEmpleadoCallback;
import com.nitido.utils.toaster.Toaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_generarInforme implements ActionListener, KeyListener, RecibirEmpleadoCallback {

    private static final String VALIDAR_FECHA = "La fecha de final es menor a la fecha de inicio",
            VALIDAR_SELECCION = "Seleccione por lo menos una opción";

    private M_generarInforme modelo;
    private V_generarInforme vista;
    boolean isRegistrationEmploy;

    public C_generarInforme(M_generarInforme modelo, V_generarInforme vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.isRegistrationEmploy = false;
        inicializarVista();
        agregarListeners();
    }

    private void inicializarVista() {
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaInicio.setDate(calendar.getTime());
        this.vista.jdcFechaFinal.setDate(calendar.getTime());
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    public void cerrar() {
        this.vista.dispose();
    }

    private void agregarListeners() {
        this.vista.jcbFecha.addActionListener(this);
        this.vista.jbFuncionario.addActionListener(this);
        this.vista.jbResponsable.addActionListener(this);
        this.vista.jbGenerar.addActionListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private boolean validarFecha() {
        Date jdcFechaInicio = this.vista.jdcFechaInicio.getDate();
        Date jdcFechaFinal = this.vista.jdcFechaFinal.getDate();
        return jdcFechaFinal.after(jdcFechaInicio);
    }

    private boolean validarSeleccion() {
        if (vista.jcbProductosTerminados.isSelected()) {
            return true;
        }
        if (vista.jcbRollosProducidos.isSelected()) {
            return true;
        }
        if (vista.jcbRollosUtilizados.isSelected()) {
            return true;
        }
        if (vista.jcbRollosDisponibles.isSelected()) {
            return true;
        }
        return false;
    }

    private void handleJCBFecha() {
        if (vista.jcbFecha.isSelected()) {
            this.vista.jdcFechaInicio.setEnabled(true);
            this.vista.jdcFechaFinal.setEnabled(true);
        } else {
            this.vista.jdcFechaInicio.setEnabled(false);
            this.vista.jdcFechaFinal.setEnabled(false);
        }
    }

    private void handleEmployBtn(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(vista.jbFuncionario)) {
            isRegistrationEmploy = true;
        } else if (source.equals(vista.jbResponsable)) {
            isRegistrationEmploy = false;
        }
        Seleccionar_funcionario sf = new Seleccionar_funcionario(vista);
        sf.setCallback(this);
        sf.mostrarVista();
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    private void generarInforme() {
        if (!validarFecha()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA, "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validarSeleccion()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_SELECCION, "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean esConFechas = vista.jcbFecha.isSelected();
        Date inicio = vista.jdcFechaInicio.getDate();
        Date fin = vista.jdcFechaFinal.getDate();
        ArrayList<E_produccionCabecera> productosTerminadosList = new ArrayList<>();
        ArrayList<E_produccionCabecera> rollosProducidosList = new ArrayList<>();
        ArrayList<E_produccionCabecera> rollosUtilizadosList = new ArrayList<>();
        ArrayList<E_produccionCabecera> rollosDisponiblesList = new ArrayList<>();
        if (vista.jcbProductosTerminados.isSelected()) {
            productosTerminadosList.addAll(DB_Produccion.consultarProduccion(inicio, fin, E_produccionTipo.PRODUCTO_TERMINADO, -1, Estado.ACTIVO, -1, esConFechas));
        }
        if (vista.jcbRollosProducidos.isSelected()) {
            rollosProducidosList.addAll(DB_Produccion.consultarProduccion(inicio, fin, E_produccionTipo.ROLLO, -1, Estado.ACTIVO, -1, esConFechas));
        }
        if (vista.jcbRollosUtilizados.isSelected()) {
            rollosUtilizadosList.addAll(DB_Produccion.consultarProduccion(inicio, fin, -1, -1, Estado.ACTIVO, -1, esConFechas));
        }
        if (vista.jcbRollosDisponibles.isSelected()) {
            rollosDisponiblesList.addAll(DB_Produccion.consultarProduccion(inicio, fin, -1, -1, Estado.ACTIVO, -1, esConFechas));
        }
        Excel.ExportarProduccion ep = new ExportarProduccion();
        ep.cargarDatos(productosTerminadosList, rollosProducidosList, rollosUtilizadosList, rollosDisponiblesList);
        ep.generarInformeCompleto();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(vista.jbGenerar)) {
            generarInforme();
        } else if (source.equals(vista.jcbFecha)) {
            handleJCBFecha();
        } else if (source.equals(vista.jbFuncionario)) {
            handleEmployBtn(e);
        } else if (source.equals(vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        if (isRegistrationEmploy) {
            this.vista.jtfFuncionario.setText(funcionario.getNombre());
        } else {
            this.vista.jtfResponsable.setText(funcionario.getNombre());
        }
    }

}
