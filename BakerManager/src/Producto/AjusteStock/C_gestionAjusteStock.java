/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import Empleado.SeleccionarFuncionario;
import Entities.Estado;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Interface.GestionInterface;
import Interface.RecibirEmpleadoCallback;
import MenuPrincipal.DatosUsuario;
import Producto.AjusteStock.Parametros.AjusteStockParametros;
import Utilities.CellRenderers.InventarioStatusCellRenderer;
import com.nitido.utils.toaster.Toaster;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionAjusteStock implements GestionInterface, RecibirEmpleadoCallback {

    public M_gestionAjusteStock modelo;
    public V_gestionAjusteStock vista;
    private InventarioStatusCellRenderer scr;

    public C_gestionAjusteStock(M_gestionAjusteStock modelo, V_gestionAjusteStock vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        concederPermisos();
    }

    @Override
    public final void inicializarVista() {
        this.scr = new InventarioStatusCellRenderer();
        Date today = Calendar.getInstance().getTime();
        this.vista.jddFinal.setDate(today);
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.set(Calendar.DAY_OF_MONTH, 1);
        this.vista.jddInicio.setDate(c.getTime());
        this.vista.jtCabecera.setModel(modelo.getTmCabecera());
        this.vista.jtCabecera.setDefaultRenderer(Object.class, scr);
        this.vista.jtDetalle.setModel(modelo.getTmDetalle());

        ArrayList<Estado> estados = modelo.obtenerEstados();
        for (int i = 0; i < estados.size(); i++) {
            this.vista.jcbEstado.addItem(estados.get(i));
        }
    }

    @Override
    public final void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbBuscar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jtfIDAjusteStock.setEnabled(true);
                this.vista.jtfEmpleado.setEnabled(true);
                this.vista.jddFinal.setEnabled(true);
                this.vista.jddInicio.setEnabled(true);
                this.vista.jcbEstado.setEnabled(true);
                this.vista.jbEmpleado.setEnabled(true);
                this.vista.jbBorrar.setEnabled(true);
                this.vista.jbBuscar.setEnabled(true);
                this.vista.jbEmpleado.setEnabled(true);
                this.vista.jbBuscar.addActionListener(this);
                this.vista.jbEmpleado.addActionListener(this);
                this.vista.jbBorrar.addActionListener(this);
            }
            if (this.vista.jbCrear.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbCrear.addActionListener(this);
                this.vista.jbCrear.setEnabled(true);
            }
            if (this.vista.jbVer.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbVer.addActionListener(this);
            }
            if (this.vista.jbAnular.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbAnular.addActionListener(this);
            }
            if (this.vista.jbParametros.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbParametros.addActionListener(this);
                this.vista.jbParametros.setEnabled(true);
            }
        }
        this.vista.jbSalir.addActionListener(this);

        this.vista.jtCabecera.addMouseListener(this);
        this.vista.jbParametros.addKeyListener(this);
        this.vista.jbCrear.addKeyListener(this);
        this.vista.jtCabecera.addKeyListener(this);
        this.vista.jtDetalle.addMouseListener(this);
        this.vista.jtDetalle.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private void verificarPermiso() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbVer.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbVer.setEnabled(true);
            }
            if (this.vista.jbAnular.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbAnular.setEnabled(true);
            }
        }
    }

    @Override
    public final void mostrarVista() {
        this.vista.setVisible(true);
    }

    @Override
    public final void cerrar() {
        this.vista.dispose();
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.getCabecera().setResponsable(funcionario);
        this.vista.jtfEmpleado.setText(this.modelo.obtenerNombreFuncionario());
    }

    private void borrarDatos() {
        this.modelo.borrarDatos();
        this.vista.jtfCliente.setText("");
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfIDAjusteStock.setText("");
    }

    private boolean validarFechas() {
        Date inicio = vista.jddInicio.getDate();
        Date fin = vista.jddFinal.getDate();
        if (inicio != null && fin != null) {
            int dateValue = inicio.compareTo(fin);
            if (dateValue <= 0) {
                return true;
            }
        }
        vista.jddInicio.setDate(vista.jddInicio.getDate());
        vista.jddFinal.updateUI();
        JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    private int obtenerIDCabecera() {
        String idCabecera = vista.jtfIDAjusteStock.getText().trim();
        int value = -1;
        if (idCabecera.isEmpty()) {
            return value;
        }
        try {
            value = Integer.valueOf(idCabecera);
        } catch (Exception e) {
            vista.jtfIDAjusteStock.setText("");
            return -1;
        }
        if (value > 0) {
            return value;
        } else {
            vista.jtfIDAjusteStock.setText("");
            return -1;
        }
    }

    private void consultarInventarios() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!validarFechas()) {
                    return;
                }
                Date fechaInicio = vista.jddInicio.getDate();
                Date fechaFinal = vista.jddFinal.getDate();
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                int idEstado = estado.getId();
                int idCabecera = obtenerIDCabecera();
                modelo.consultarInventarios(fechaInicio, fechaFinal, idEstado, idCabecera);
                scr.setList(modelo.getTmCabecera().getList());
                Utilities.c_packColumn.packColumns(vista.jtCabecera, 1);
            }
        });
    }

    private void consultarInventarioDetalle() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int fila = vista.jtCabecera.getSelectedRow();
                if (fila < 0) {
                    vista.jbVer.setEnabled(false);
                    vista.jbAnular.setEnabled(false);
                    return;
                }
                //TODO add verificarPermiso();
                vista.jbVer.setEnabled(true);
                vista.jbAnular.setEnabled(true);
                int idCabecera = modelo.getTmCabecera().getList().get(fila).getId();
                modelo.consultarInventarioDetalle(idCabecera);
                Utilities.c_packColumn.packColumns(vista.jtDetalle, 1);
            }
        });
    }

    private void inventarioMouseHandler(MouseEvent e) {
        int fila = this.vista.jtCabecera.getSelectedRow();
        if (fila < 0) {
            this.vista.jbVer.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
            return;
        }
        verificarPermiso();
        int idCabecera = this.modelo.getTmCabecera().getList().get(fila).getId();
        this.modelo.consultarInventarioDetalle(idCabecera);
        Utilities.c_packColumn.packColumns(vista.jtDetalle, 1);
        if (e.getClickCount() == 2) {
            if (vista.jbVer.isEnabled()) {
                verDetalle();
                this.vista.jbVer.setEnabled(false);
                this.vista.jbAnular.setEnabled(false);
            }
        }
    }

    private void invocarSeleccionFuncionario() {
        SeleccionarFuncionario sf = new SeleccionarFuncionario(this.vista);
        sf.setCallback(this);
        sf.mostrarVista();
    }

    private void invocarPrevisionStock() {
        PrevisionStock ps = new PrevisionStock(this.vista);
        ps.mostrarVista();
    }

    private void invocarGestionParametros() {
        AjusteStockParametros ap = new AjusteStockParametros(this.vista);
        ap.mostrarVista();
    }

    private void verDetalle() {
        int fila = vista.jtCabecera.getSelectedRow();
        if (fila < 0) {
            this.vista.jbVer.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
            return;
        }
        int idCabecera = this.modelo.getTmCabecera().getList().get(fila).getId();
        CrearAjuste ca = new CrearAjuste(vista, idCabecera, false);
        ca.mostrarVista();
    }

    private void anularInventario() {
        int fila = vista.jtCabecera.getSelectedRow();
        if (fila < 0) {
            this.vista.jbVer.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Está seguro que desea continuar? Accion irreversible.", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        int idCabecera = modelo.getTmCabecera().getList().get(fila).getId();
        int genKey = modelo.anularInventario(idCabecera);
        if (genKey > 0) {
            mostrarMensaje("Inventario anulado");
        }
        consultarInventarios();
        this.vista.jbVer.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbCrear)) {
            invocarPrevisionStock();
        }
        if (source.equals(this.vista.jbBuscar)) {
            consultarInventarios();
        }
        if (source.equals(this.vista.jbEmpleado)) {
            invocarSeleccionFuncionario();
        }
        if (source.equals(this.vista.jbBorrar)) {
            borrarDatos();
        }
        if (source.equals(this.vista.jbParametros)) {
            invocarGestionParametros();
        }
        if (source.equals(this.vista.jbVer)) {
            verDetalle();
        }
        if (source.equals(this.vista.jbAnular)) {
            anularInventario();
        }
        if (source.equals(this.vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jtCabecera)) {
            //verificarPermiso();
            inventarioMouseHandler(e);
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
            case KeyEvent.VK_F1: {
                break;
            }
            case KeyEvent.VK_F2: {
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (this.vista.jtCabecera.hasFocus()) {
            consultarInventarioDetalle();
        }
    }
}
