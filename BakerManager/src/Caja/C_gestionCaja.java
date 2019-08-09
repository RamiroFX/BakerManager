package Caja;

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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionCaja implements GestionInterface {

    V_gestionCaja vista;
    M_gestionCaja modelo;
    public C_inicio c_inicio;

    public C_gestionCaja(V_gestionCaja vista, M_gestionCaja modelo, C_inicio c_inicio) {
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
        this.vista.jddInicio.setDate(date);
        this.vista.jddFinal.setDate(date);
    }

    @Override
    public void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
            if (this.vista.jbAgregar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbAgregar.setEnabled(true);
                this.vista.jbAgregar.addActionListener(this);
            }
            /*if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbResumen.addActionListener(this);
            }*/
            if (this.vista.jbDetalle.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbDetalle.addActionListener(this);
            }
            if (this.vista.jbBuscar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbBuscar.setEnabled(true);
                this.vista.jddFinal.setEnabled(true);
                this.vista.jddInicio.setEnabled(true);
                this.vista.jbBuscar.addActionListener(this);
                this.vista.jbEmpleado.addActionListener(this);
                this.vista.jbBorrar.addActionListener(this);
            }
        }
        this.vista.jtCaja.addMouseListener(this);
        this.vista.jtCaja.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbAgregar.addKeyListener(this);
        //this.vista.jbResumen.addKeyListener(this);
        this.vista.jbDetalle.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        //exportar
        this.vista.jbExportar.addActionListener(this);
        this.vista.jbExportar.addKeyListener(this);
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
        SaldarCaja sc = new SaldarCaja(c_inicio);
        sc.setVisible(true);
    }

    private void invocarVistaVerCaja() {
        int fila = this.vista.jtCaja.getSelectedRow();
        int columna = this.vista.jtCaja.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            int idCaja = Integer.valueOf(String.valueOf(this.vista.jtCaja.getValueAt(fila, 0)));
            VerDetalleCaja detalleCaja = new VerDetalleCaja(c_inicio, idCaja);
            detalleCaja.setVisible(true);
        }
        this.vista.jbDetalle.setEnabled(false);
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
                    vista.jtCaja.setModel(modelo.consultarCajas(idFuncionario, ini, fi));
                    Utilities.c_packColumn.packColumns(vista.jtCaja, 1);
                    vista.jbDetalle.setEnabled(false);
                } else {
                    vista.jddFinal.setDate(vista.jddInicio.getDate());
                    vista.jddFinal.updateUI();
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
        this.vista.jtfEmpleado.setText(alias + "-(" + nombre + " " + apellido + ")");
    }

    public void exportarExcel() {
        if (vista.jtCaja.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(vista, "No hay cajas para exportar", "Atención", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Object[] options = {"Completo",
            "Minimalista"};
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
                //Completo
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Date inicio = vista.jddInicio.getDate();
                        Date fin = vista.jddFinal.getDate();
                        if (validarFechas(inicio, fin)) {
                            Integer idFuncionario = null;
                            if (modelo.getFuncionario() != null && modelo.getFuncionario().getId_funcionario() != null) {
                                idFuncionario = modelo.getFuncionario().getId_funcionario();
                            }
                            modelo.exportarExcel(idFuncionario, inicio, fin);
                            Utilities.c_packColumn.packColumns(vista.jtCaja, 1);
                        } else {
                            vista.jddFinal.setDate(vista.jddInicio.getDate());
                            vista.jddFinal.updateUI();
                            JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
                break;
            }
            case 1: {
                //Minimalista
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Date inicio = vista.jddInicio.getDate();
                        Date fin = vista.jddFinal.getDate();
                        if (validarFechas(inicio, fin)) {
                            Integer idFuncionario = null;
                            if (modelo.getFuncionario() != null && modelo.getFuncionario().getId_funcionario() != null) {
                                idFuncionario = modelo.getFuncionario().getId_funcionario();
                            }
                            modelo.exportarExcelMinimalista(idFuncionario, inicio, fin);
                            Utilities.c_packColumn.packColumns(vista.jtCaja, 1);
                        } else {
                            vista.jddFinal.setDate(vista.jddInicio.getDate());
                            vista.jddFinal.updateUI();
                            JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
                break;
            }
        }
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbAgregar)) {
            invocarVistaSaldarCaja();
        } else if (src.equals(this.vista.jbBuscar)) {
            consultarCajas();
        } else if (src.equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this);
            sf.mostrarVista();
        } else if (src.equals(this.vista.jbBorrar)) {
            borrarDatos();
        } else if (src.equals(this.vista.jbDetalle)) {
            invocarVistaVerCaja();
        } else if (src.equals(this.vista.jbExportar)) {
            exportarExcel();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtCaja.rowAtPoint(e.getPoint());
        int columna = this.vista.jtCaja.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbDetalle.setEnabled(true);
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
                if (vista.jbAgregar.isEnabled()) {
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
