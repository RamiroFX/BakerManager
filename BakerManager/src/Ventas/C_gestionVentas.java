/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Cliente.Seleccionar_cliente;
import DB.DB_Egreso;
import DB.DB_Ingreso;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Interface.GestionInterface;
import MenuPrincipal.DatosUsuario;
import bakermanager.C_inicio;
import Empleado.Seleccionar_funcionario;
import Entities.Estado;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionVentas implements GestionInterface, RecibirEmpleadoCallback,RecibirClienteCallback {

    public M_gestionVentas modelo;
    public V_gestionVentas vista;
    public C_inicio c_inicio;
    private final C_gestionVentas gestionVentas;

    public C_gestionVentas(M_gestionVentas modelo, V_gestionVentas vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        this.gestionVentas = this;
        inicializarVista();
        concederPermisos();
    }

    @Override
    public final void inicializarVista() {
        this.vista.jbDetalle.setEnabled(false);
        Vector condCompra = DB_Egreso.obtenerTipoOperacion();
        this.vista.jcbCondCompra.addItem("Todos");
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondCompra.addItem(condCompra.get(i));
        }
        ArrayList<Estado> estados = modelo.getEstados();
        for (int i = 0; i < estados.size(); i++) {
            this.vista.jcbEstado.addItem(estados.get(i));
        }
        Date today = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(today);
        this.vista.jddFinal.setDate(today);
        this.vista.jbAgregar.setEnabled(false);
        this.vista.jbBuscar.setEnabled(false);
        this.vista.jbBuscarDetalle.setEnabled(false);
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbResumen.setEnabled(false);
        this.vista.jbCliente.setEnabled(false);
        this.vista.jbEmpleado.setEnabled(false);
        this.vista.jcbCondCompra.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);
    }

    @Override
    public final void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbAgregar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbAgregar.setEnabled(true);
                this.vista.jbAgregar.addActionListener(this);
            }
            if (this.vista.jbBuscar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbBuscar.setEnabled(true);
                this.vista.jbBuscar.addActionListener(this);
                this.vista.jbCliente.setEnabled(true);
                this.vista.jbCliente.addActionListener(this);
                this.vista.jbEmpleado.setEnabled(true);
                this.vista.jbEmpleado.addActionListener(this);
                this.vista.jcbCondCompra.setEnabled(true);
                this.vista.jbBorrar.addActionListener(this);
                this.vista.jbBuscarDetalle.setEnabled(true);
                this.vista.jbBuscarDetalle.addActionListener(this);
            }
            if (this.vista.jbDetalle.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbDetalle.addActionListener(this);
            }
            if (this.vista.jbResumen.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbResumen.setEnabled(true);
                this.vista.jbResumen.addActionListener(this);
            }
            if (this.vista.jbAnular.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbAnular.addActionListener(this);
            }
        }
        this.vista.jtIngresoCabecera.addMouseListener(this);
        this.vista.jtIngresoCabecera.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbAgregar.addKeyListener(this);
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jbAnular.addKeyListener(this);
        this.vista.jbDetalle.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jcbCondCompra.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbBuscarDetalle.addKeyListener(this);
    }

    private void verificarPermiso() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbDetalle.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbDetalle.setEnabled(true);
            }
            if (this.vista.jbAnular.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbAnular.setEnabled(true);
            }
        }
    }

    @Override
    public final void mostrarVista() {
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        this.c_inicio.agregarVentana(vista);
    }

    @Override
    public final void cerrar() {
        try {
            this.vista.setClosed(true);
        } catch (PropertyVetoException ex) {
        }
    }

    private void displayQueryResults() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!vista.jtfNroFactura.getText().trim().isEmpty()) {
                        int nroFac = Integer.valueOf(vista.jtfNroFactura.getText());
                        modelo.getCabecera().setNroFactura(nroFac);
                    } else {
                        modelo.getCabecera().setIdFacturaCabecera(null);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(vista, "Ingrese un número entero válido para Nro. factura", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (modelo.validarFechas(vista.jddInicio.getDate(), vista.jddFinal.getDate())) {
                    Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                    Date fechaInicio = vista.jddInicio.getDate();
                    Date fechaFinal = vista.jddFinal.getDate();
                    String condCompra = vista.jcbCondCompra.getSelectedItem().toString();
                    vista.jtIngresoCabecera.setModel(modelo.obtenerVentas(fechaInicio, fechaFinal, condCompra, estado.getId()));
                    Utilities.c_packColumn.packColumns(vista.jtIngresoCabecera, 1);
                } else {
                    vista.jddFinal.setDate(vista.jddInicio.getDate());
                    vista.jddFinal.updateUI();
                    JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        this.modelo.cabecera.setCliente(cliente);
        String nombre = this.modelo.cabecera.getCliente().getNombre();
        String entidad = this.modelo.cabecera.getCliente().getEntidad();
        this.vista.jtfCliente.setText(nombre + "-(" + entidad + ")");
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.cabecera.setFuncionario(funcionario);
        this.vista.jtfEmpleado.setText(this.modelo.obtenerNombreFuncionario());
    }

    private void borrarDatos() {
        this.modelo.borrarDatos();
        this.vista.jtfCliente.setText("");
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfNroFactura.setText("");
        this.vista.jcbCondCompra.setSelectedItem("Todos");
        this.vista.jcbEstado.setSelectedItem(new Estado(1, "Activo"));
    }

    private void obtenerEgresoDetalle(MouseEvent e) {
        int fila = this.vista.jtIngresoCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtIngresoCabecera.columnAtPoint(e.getPoint());
        Integer idIngresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtIngresoCabecera.getValueAt(fila, 0)));
        if ((fila > -1) && (columna > -1)) {
            verificarPermiso();
            this.vista.jtIngresoDetalle.setModel(DB_Ingreso.obtenerIngresoDetalle(idIngresoCabecera));
        } else {
            this.vista.jbDetalle.setEnabled(false);
        }
        if (e.getClickCount() == 2) {
            if (vista.jbDetalle.isEnabled()) {
                Ver_ingreso ver_egreso = new Ver_ingreso(c_inicio, idIngresoCabecera);
                ver_egreso.mostrarVista();
                this.vista.jbDetalle.setEnabled(false);
            }
        }
    }

    private String empleado() {
        if (vista.jtfEmpleado.getText().isEmpty()) {
            return "Todos";
        }
        return modelo.cabecera.getFuncionario().getId_funcionario().toString();
    }

    private M_cliente cliente() {
        if (vista.jtfCliente.getText().isEmpty()) {
            return null;
        }
        return modelo.cabecera.getCliente();
    }

    private String tipoOperacion() {
        String idEmpleado = "";
        String datosTipoOperacion = vista.jcbCondCompra.getSelectedItem().toString();
        if (vista.jcbCondCompra.getSelectedItem().toString().equals("Todos")) {
            return "Todos";
        } else {
            idEmpleado = DB_Egreso.obtenerTipoOperacion(datosTipoOperacion).toString();
        }
        return idEmpleado;
    }

    private void crearResumen() {
        if (vista.jddInicio.getDate() != null && vista.jddFinal.getDate() != null) {
            int dateValue = vista.jddInicio.getDate().compareTo(vista.jddFinal.getDate());
            if (dateValue > 0) {
                vista.jddFinal.setDate(vista.jddInicio.getDate());
                vista.jddFinal.updateUI();
                JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        Integer nro_factura = null;
        try {
            if (!vista.jtfNroFactura.getText().isEmpty()) {
                nro_factura = Integer.valueOf(String.valueOf(vista.jtfNroFactura.getText()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "El numero de factura debe ser solo numérico", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String empleado = empleado();
        M_cliente cliente = cliente();
        String tiop = tipoOperacion();
        Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
        Resumen_ingreso re = new Resumen_ingreso(c_inicio, this.vista.jtIngresoCabecera.getModel(), cliente, nro_factura, empleado, vista.jddInicio.getDate(), vista.jddFinal.getDate(), tiop, estado);
        re.setVisible(true);
    }

    private void completarCampos() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int fila = vista.jtIngresoCabecera.getSelectedRow();
                if (fila > -1) {
                    int idIngresoCabecera = Integer.valueOf(String.valueOf(vista.jtIngresoCabecera.getValueAt(fila, 0).toString()));
                    vista.jtIngresoDetalle.setModel(DB_Ingreso.obtenerIngresoDetalle(idIngresoCabecera));
                    Utilities.c_packColumn.packColumns(vista.jtIngresoDetalle, 1);
                }
            }
        });
    }

    private void crearVentaRapida() {
        /*CrearVentaRapida crv = new CrearVentaRapida(gestionVentas);
        crv.mostrarVista();/*
        SwingUtilities.invokeLater(new Runnable() {//if we remove this block it wont work also (no matter when we call requestFocusInWindow)
            @Override
            public void run() {
                CrearVentaRapida crv = new CrearVentaRapida(gestionVentas);
                crv.mostrarVista();
            }
        });*/
        CrearVentas cv = new CrearVentas(gestionVentas);
        cv.mostrarVista();
    }

    private void anularVenta() {
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Está seguro que desea continuar? Accion irreversible.", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            int fila = this.vista.jtIngresoCabecera.getSelectedRow();
            if (fila > -1) {
                Integer idIngresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtIngresoCabecera.getValueAt(fila, 0)));
                modelo.anularVenta(idIngresoCabecera);
            }
        }
        displayQueryResults();
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbAgregar)) {
            crearVentaRapida();
        }
        if (e.getSource().equals(this.vista.jbBuscar)) {
            displayQueryResults();
        }
        if (e.getSource().equals(this.vista.jbBuscarDetalle)) {
            buscarVentaDetalle();
        }
        if (e.getSource().equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this.c_inicio.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbBorrar)) {
            borrarDatos();
        }
        if (e.getSource().equals(this.vista.jbResumen)) {
            crearResumen();
        }
        if (e.getSource().equals(this.vista.jbAnular)) {
            anularVenta();
        }
        if (e.getSource().equals(this.vista.jbDetalle)) {
            int fila = this.vista.jtIngresoCabecera.getSelectedRow();
            if (fila > -1) {
                Integer idIngresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtIngresoCabecera.getValueAt(fila, 0)));
                Ver_ingreso ver_egreso = new Ver_ingreso(c_inicio, idIngresoCabecera);
                ver_egreso.mostrarVista();
                this.vista.jbDetalle.setEnabled(false);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtIngresoCabecera)) {
            obtenerEgresoDetalle(e);
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
                crearVentaRapida();
                break;
            }
            case KeyEvent.VK_F2: {
                crearResumen();
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
        if (this.vista.jtIngresoCabecera.hasFocus()) {
            completarCampos();
        }
    }

    private void buscarVentaDetalle() {
        C_buscar_venta_detalle bvd = new C_buscar_venta_detalle(c_inicio);
        bvd.mostrarVista();
    }
}
