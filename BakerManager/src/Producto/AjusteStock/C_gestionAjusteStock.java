/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import Empleado.SeleccionarFuncionario;
import Entities.M_cliente;
import Entities.M_funcionario;
import Interface.GestionInterface;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionAjusteStock implements GestionInterface, RecibirEmpleadoCallback, RecibirClienteCallback {

    public M_gestionAjusteStock modelo;
    public V_gestionAjusteStock vista;

    public C_gestionAjusteStock(M_gestionAjusteStock modelo, V_gestionAjusteStock vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        concederPermisos();
    }

    @Override
    public final void inicializarVista() {
        Date today = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(today);
        this.vista.jddFinal.setDate(today);
        /*this.vista.jbBuscar.setEnabled(false);
        this.vista.jbEmpleado.setEnabled(false);
        this.vista.jbSalir.setEnabled(false);*/
    }

    @Override
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
        this.vista.jbCrear.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);

        this.vista.jtCabecera.addMouseListener(this);
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

    @Override
    public final void mostrarVista() {
        this.vista.setVisible(true);
    }

    @Override
    public final void cerrar() {
        this.vista.dispose();
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
//        this.modelo.cabecera.setCliente(cliente);
//        String nombre = this.modelo.cabecera.getCliente().getNombre();
//        String entidad = this.modelo.cabecera.getCliente().getEntidad();
//        this.vista.jtfCliente.setText(nombre + "-(" + entidad + ")");
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
//        this.modelo.cabecera.setFuncionario(funcionario);
//        this.vista.jtfEmpleado.setText(this.modelo.obtenerNombreFuncionario());
    }

    private void borrarDatos() {
        this.modelo.borrarDatos();
        this.vista.jtfCliente.setText("");
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfIDAjusteStock.setText("");
    }

    private void consultarFacturaciones() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    private void consultarVentas() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int fila = vista.jtCabecera.getSelectedRow();
                if (fila > -1) {

                }
            }
        });
    }

    private void facturacionMouseHandler(MouseEvent e) {
//        int fila = this.vista.jtFacturacion.rowAtPoint(e.getPoint());
//        int columna = this.vista.jtFacturacion.columnAtPoint(e.getPoint());
//        Integer idFacturacion = Integer.valueOf(String.valueOf(this.vista.jtFacturacion.getValueAt(fila, 0)));
//        if ((fila > -1) && (columna > -1)) {
//            //TODO add verificarPermiso();
//            this.vista.jtVentas.setModel(modelo.obtenerVentasPorFacturacion(idFacturacion));
//            this.vista.jbFacturacionDetalle.setEnabled(true);//TODO si tiene permiso
//        } else {
//            this.vista.jbFacturacionDetalle.setEnabled(false);
//        }
//        if (e.getClickCount() == 2) {
//            if (vista.jbFacturacionDetalle.isEnabled()) {
//                facturacionDetalle();
//                this.vista.jbFacturacionDetalle.setEnabled(false);
//            }
//        }
    }

    private void facturacionDetalle() {
        int fila = vista.jtCabecera.getSelectedRow();
        if (fila > -1) {
//            Integer idFacturacion = Integer.valueOf(String.valueOf(this.vista.jtFacturacion.getValueAt(fila, 0)));
//            FacturacionCabeceraTableModel tm = (FacturacionCabeceraTableModel) this.vista.jtFacturacion.getModel();
//            E_facturacionCabecera facturacionCabecera = tm.getFacturacionCabeceraList().get(fila);
//            ResumenIngreso re = new ResumenIngreso(c_inicio);
//            re.inicializarDatos(facturacionCabecera);
//            re.setVisible(true);
        }
    }

    private void ventaDetalle() {
        int fila = this.vista.jtDetalle.getSelectedRow();
        if (fila > -1) {
            //verificarPermiso();
//            Integer idVentaCabecera = Integer.valueOf(String.valueOf(this.vista.jtVentas.getValueAt(fila, 0)));
//            VerIngreso ver_egreso = new VerIngreso(c_inicio, idVentaCabecera, false);
//            ver_egreso.mostrarVista();
//            this.vista.jbVentaDetalle.setEnabled(false);
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
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbCrear)) {
            invocarPrevisionStock();
        }
        if (source.equals(this.vista.jbBuscar)) {
            consultarFacturaciones();
        }
        if (source.equals(this.vista.jbEmpleado)) {
            invocarSeleccionFuncionario();
        }
        if (source.equals(this.vista.jbBorrar)) {
            borrarDatos();
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
            facturacionMouseHandler(e);
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
            consultarVentas();
        }
    }
}
