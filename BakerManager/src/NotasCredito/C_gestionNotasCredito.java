/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import Cliente.Seleccionar_cliente;
import Empleado.Seleccionar_funcionario;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_funcionario;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro
 */
public class C_gestionNotasCredito implements ActionListener, MouseListener, KeyListener, RecibirEmpleadoCallback, RecibirClienteCallback {

    public M_gestionNotasCredito modelo;
    public V_gestionNotasCredito vista;
    public C_inicio c_inicio;

    public C_gestionNotasCredito(M_gestionNotasCredito modelo, V_gestionNotasCredito vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        concederPermisos();
    }

    public final void inicializarVista() {
        this.vista.jtCabecera.setModel(modelo.getTm());
        this.vista.jtDetalle.setModel(modelo.getTmDetalle());
        ArrayList<E_tipoOperacion> condVenta = modelo.obtenerTipoOperaciones();
        for (int i = 0; i < condVenta.size(); i++) {
            this.vista.jcbCondVenta.addItem(condVenta.get(i));
        }
        ArrayList<Estado> estados = modelo.obtenerEstados();
        for (int i = 0; i < estados.size(); i++) {
            this.vista.jcbEstado.addItem(estados.get(i));
        }
        Date today = Calendar.getInstance().getTime();
        Calendar longAgo = Calendar.getInstance();
        longAgo.set(Calendar.YEAR, 2015);
        this.vista.jddInicio.setDate(longAgo.getTime());
        this.vista.jddFinal.setDate(today);
    }

    public final void concederPermisos() {
        /*ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbBuscar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbBuscar.setEnabled(true);
                this.vista.jbBuscar.addActionListener(this);
                this.vista.jbCliente.setEnabled(true);
                this.vista.jbCliente.addActionListener(this);
                this.vista.jbEmpleado.setEnabled(true);
                this.vista.jbEmpleado.addActionListener(this);
                this.vista.jcbCondVenta.setEnabled(true);
                this.vista.jbBorrar.addActionListener(this);
            }
            if (this.vista.jbFacturacionDetalle.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbFacturacionDetalle.addActionListener(this);
            }
            if (this.vista.jbVentaDetalle.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbVentaDetalle.addActionListener(this);
            }
        }*/
        //TODO remove
        
        this.vista.jbNueva.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);

        this.vista.jtCabecera.addMouseListener(this);
        this.vista.jtCabecera.addKeyListener(this);
        this.vista.jtDetalle.addMouseListener(this);
        this.vista.jtDetalle.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jcbCondVenta.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jbNueva.addKeyListener(this);
    }

    public final void mostrarVista() {
        this.vista.setVisible(true);
    }

    public final void cerrar() {
        this.vista.dispose();
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        this.modelo.getCabecera().setCliente(cliente);
        String nombre = this.modelo.getCabecera().getCliente().getNombre();
        String entidad = this.modelo.getCabecera().getCliente().getEntidad();
        this.vista.jtfCliente.setText(nombre + "-(" + entidad + ")");
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.getCabecera().setFuncionario(funcionario);
        this.vista.jtfEmpleado.setText(this.modelo.obtenerNombreFuncionario());
    }

    private void borrarDatos() {
        this.modelo.borrarDatos();
        this.vista.jtfCliente.setText("");
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfNroNotaCredito.setText("");
        this.vista.jcbCondVenta.setSelectedIndex(0);
    }

    private void consultarNotasCredito() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int idFuncionario = modelo.getCabecera().getFuncionario().getId_funcionario();
                int idCliente = modelo.getCabecera().getCliente().getIdCliente();
                int nroNotaCredito = obtenerNroNotaCredito();
                E_tipoOperacion tiop = vista.jcbCondVenta.getItemAt(vista.jcbCondVenta.getSelectedIndex());
                int idCondVenta = tiop.getId();
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                int idEstado = estado.getId();
                Date fechaInicio = vista.jddInicio.getDate();
                Date fechaFinal = vista.jddFinal.getDate();
                modelo.getTm().setList(modelo.obtenerNotasCreditoCabecera(idCliente, idFuncionario, nroNotaCredito, fechaInicio, fechaFinal, idCondVenta, idEstado));
                Utilities.c_packColumn.packColumns(vista.jtDetalle, 1);
            }
        });
    }

    private int obtenerNroNotaCredito() {
        String nroNotaCredito = vista.jtfNroNotaCredito.getText().trim();
        int value = -1;
        if (nroNotaCredito.isEmpty()) {
            return value;
        }
        try {
            value = Integer.valueOf(nroNotaCredito);
        } catch (Exception e) {
            return -1;
        }
        if (value > 0) {
            return value;
        } else {
            return -1;
        }
    }

    private void consultarNotasCreditoDetalle() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int fila = vista.jtCabecera.getSelectedRow();
                if (fila > -1) {
                    int idNotaCreditoCabecera = modelo.getTm().getList().get(fila).getId();
                    modelo.getTmDetalle().setList(modelo.obtenerNotasCreditoDetalle(idNotaCreditoCabecera));
                    Utilities.c_packColumn.packColumns(vista.jtDetalle, 1);
                }
            }
        });
    }

    private void crearNotaCredito() {
        CrearNotaCredito cnc = new CrearNotaCredito(c_inicio);
        cnc.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbBuscar)) {
            consultarNotasCredito();
        }
        if (source.equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this.c_inicio.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        }
        if (source.equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        }
        if (source.equals(this.vista.jbBorrar)) {
            borrarDatos();
        }
        if (source.equals(this.vista.jbNueva)) {
            crearNotaCredito();
        }
        if (source.equals(this.vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jtCabecera)) {
            consultarNotasCreditoDetalle();
        }
        if (source.equals(this.vista.jtDetalle)) {
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
            consultarNotasCreditoDetalle();
        }
    }
}
