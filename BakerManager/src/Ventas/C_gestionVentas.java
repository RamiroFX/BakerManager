/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Facturacion.Facturacion;
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
import Entities.E_tipoOperacion;
import Entities.Estado;
import Facturacion.HistorialFacturacion;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import NotasCredito.GestionNotasCredito;
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
public class C_gestionVentas implements GestionInterface, RecibirEmpleadoCallback, RecibirClienteCallback {

    private static final String VALIDAR_NRO_FACTURA_1 = "Ingrese solo números enteros en número de factura",
            VALIDAR_NRO_FACTURA_2 = "Ingrese solo números enteros y positivos en número de factura";
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
        ArrayList<E_tipoOperacion> condVenta = modelo.obtenerTipoOperaciones();
        for (int i = 0; i < condVenta.size(); i++) {
            this.vista.jcbCondVenta.addItem(condVenta.get(i));
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
        this.vista.jcbCondVenta.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);
        this.vista.jbFacturar.setEnabled(false);
        this.vista.jbHistorialFacturacion.setEnabled(false);
        this.vista.jtIngresoCabecera.setModel(modelo.getTm());
    }

    @Override
    public final void concederPermisos() {
        ArrayList<M_menu_item> accesos = this.modelo.getAccesos();
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
                this.vista.jcbCondVenta.setEnabled(true);
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
            if (this.vista.jbFacturar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbFacturar.setEnabled(true);
                this.vista.jbFacturar.addActionListener(this);
            }
            if (this.vista.jbHistorialFacturacion.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbHistorialFacturacion.setEnabled(true);
                this.vista.jbHistorialFacturacion.addActionListener(this);
            }
        }
        this.vista.jbMasOpciones.addActionListener(this);
        this.vista.jtIngresoCabecera.addMouseListener(this);
        this.vista.jtIngresoCabecera.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbMasOpciones.addKeyListener(this);
        this.vista.jbAgregar.addKeyListener(this);
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jbAnular.addKeyListener(this);
        this.vista.jbDetalle.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jcbCondVenta.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbBuscarDetalle.addKeyListener(this);
        this.vista.jbFacturar.addKeyListener(this);
        this.vista.jbHistorialFacturacion.addKeyListener(this);
    }

    private void verificarPermiso() {
        ArrayList<M_menu_item> accesos = this.modelo.getAccesos();
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
                if (!validarFechas()) {
                    return;
                }
                if (!validarNroFactura()) {
                    return;
                }
                int nroFactura = -1;
                if (!vista.jtfNroFactura.getText().trim().isEmpty()) {
                    nroFactura = Integer.valueOf(vista.jtfNroFactura.getText().trim());
                }
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                Date fechaInicio = vista.jddInicio.getDate();
                Date fechaFinal = vista.jddFinal.getDate();
                E_tipoOperacion condVenta = vista.jcbCondVenta.getItemAt(vista.jcbCondVenta.getSelectedIndex());

                M_cliente cliente = modelo.getCabecera().getCliente();
                M_funcionario funcionario = modelo.getCabecera().getFuncionario();
                long startTime = System.nanoTime();
                modelo.getTm().setFacturaCabeceraList(modelo.obtenerVentas(cliente, funcionario, fechaInicio, fechaFinal, condVenta, nroFactura, estado));
                long elapsedTime = System.nanoTime() - startTime;
                System.out.println("ventas: Tiempo total de busqueda  in millis: " + elapsedTime / 1000000);
                Utilities.c_packColumn.packColumns(vista.jtIngresoCabecera, 1);
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

    private boolean validarFechas() {
        Date inicio = vista.jddInicio.getDate();
        Date fin = vista.jddFinal.getDate();
        if (inicio != null && fin != null) {
            int dateValue = inicio.compareTo(fin);
            if (dateValue <= 0) {
                return true;
            }
        }
        vista.jddFinal.setDate(vista.jddInicio.getDate());
        vista.jddFinal.updateUI();
        JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    private boolean validarNroFactura() {
        int nroFactura = -1;
        if (this.vista.jtfNroFactura.getText().trim().isEmpty()) {
            return true;
        } else {
            try {
                nroFactura = Integer.valueOf(this.vista.jtfNroFactura.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroFactura < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void borrarDatos() {
        this.modelo.borrarDatos();
        this.vista.jtfCliente.setText("");
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfNroFactura.setText("");
        this.vista.jcbCondVenta.setSelectedItem("Todos");
        this.vista.jcbEstado.setSelectedItem(new Estado(1, "Activo"));
    }

    private void obtenerEgresoDetalle(MouseEvent e) {
        int fila = this.vista.jtIngresoCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtIngresoCabecera.columnAtPoint(e.getPoint());;
        if ((fila > -1) && (columna > -1)) {
            Integer idIngresoCabecera = modelo.getTm().getFacturaCabeceraList().get(fila).getIdFacturaCabecera();
            verificarPermiso();
            this.vista.jtIngresoDetalle.setModel(DB_Ingreso.obtenerIngresoDetalle(idIngresoCabecera));
        } else {
            this.vista.jbDetalle.setEnabled(false);
        }
        if (e.getClickCount() == 2) {
            if (vista.jbDetalle.isEnabled()) {
                Integer idIngresoCabecera = modelo.getTm().getFacturaCabeceraList().get(fila).getIdFacturaCabecera();
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
        String datosTipoOperacion = vista.jcbCondVenta.getSelectedItem().toString();
        if (vista.jcbCondVenta.getSelectedItem().toString().equals("Todos")) {
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
        Resumen_ingreso re = new Resumen_ingreso(c_inicio, this.modelo.getTm(), cliente, nro_factura, empleado, vista.jddInicio.getDate(), vista.jddFinal.getDate(), tiop, estado);
        re.setVisible(true);
    }

    private void completarCampos() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int fila = vista.jtIngresoCabecera.getSelectedRow();
                if (fila > -1) {
                    int idIngresoCabecera = modelo.getTm().getFacturaCabeceraList().get(fila).getIdFacturaCabecera();
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
                Integer idIngresoCabecera = modelo.getTm().getFacturaCabeceraList().get(fila).getIdFacturaCabecera();
                Object nroFactura = modelo.getTm().getFacturaCabeceraList().get(fila).getNroFactura();
                if (nroFactura != null) {
                    int opcion2 = JOptionPane.showConfirmDialog(vista, "¿Desea recuperar el número de factura?.", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                    if (opcion2 == JOptionPane.YES_OPTION) {
                        modelo.anularVenta(idIngresoCabecera, true);
                    } else {
                        modelo.anularVenta(idIngresoCabecera, false);
                    }
                } else {
                    modelo.anularVenta(idIngresoCabecera, false);
                }
            }
        }
        displayQueryResults();
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);
    }

    private void facturarVentas() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //VALIDAR CLIENTE
                int idCliente = -1;
                if (cliente() == null) {
                    JOptionPane.showMessageDialog(vista, "Seleccione un cliente para facturar", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    idCliente = cliente().getIdCliente();
                }
                //VALIDAR NRO FACTURA
                if (!vista.jtfNroFactura.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Solo se permiten ventas sin Nro de Factura", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //VALIDAR ESTADO
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                if (!estado.getDescripcion().equals("Activo")) {
                    JOptionPane.showMessageDialog(vista, "Solo se permiten ventas Activas", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //VALIDAR FECHAS
                if (!modelo.validarFechas(vista.jddInicio.getDate(), vista.jddFinal.getDate())) {
                    vista.jddFinal.setDate(vista.jddInicio.getDate());
                    vista.jddFinal.updateUI();
                    JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String fechaInicio = "";
                String fechaFinal = "";
                try {
                    java.util.Date dateInicio = vista.jddInicio.getDate();
                    fechaInicio = new Timestamp(dateInicio.getTime()).toString().substring(0, 11);
                    fechaInicio = fechaInicio + "00:00:00.000";
                } catch (Exception e) {
                    fechaInicio = "Todos";
                }
                try {
                    java.util.Date dateFinal = vista.jddFinal.getDate();
                    fechaFinal = new Timestamp(dateFinal.getTime()).toString().substring(0, 11);
                    fechaFinal = fechaFinal + "23:59:59.000";
                } catch (Exception e) {
                    fechaFinal = "Todos";
                }
                //VALIDAR CONDICION DE VENTA
                String condVenta = vista.jcbCondVenta.getSelectedItem().toString();
                if (condVenta.equals("Todos")) {
                    JOptionPane.showMessageDialog(vista, "Seleccione una condición de venta", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Facturacion facturacion = new Facturacion(c_inicio, idCliente, fechaInicio, fechaFinal, condVenta);
                facturacion.mostrarVista();
            }
        });
    }

    private void historialFacturacion() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                HistorialFacturacion hf = new HistorialFacturacion(c_inicio);
                hf.mostrarVista();
            }
        });
    }

    private void gestionNotasCredito() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GestionNotasCredito hf = new GestionNotasCredito(c_inicio);
                hf.mostrarVista();
            }
        });
    }

    private void mostrarOpciones() {
        Object[] options = {"Historial de Facturación", "Notas de crédito"};
        int n = JOptionPane.showOptionDialog(this.vista,
                "Eliga su opción",
                "Atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        switch (n) {
            case 0: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<M_menu_item> accesos = modelo.getAccesos();
                        for (int i = 0; i < accesos.size(); i++) {
                            if (vista.jbHistorialFacturacion.getName().equals(accesos.get(i).getItemDescripcion())) {
                                historialFacturacion();
                                return;
                            }
                        }
                    }
                });
                break;
            }
            case 1: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<M_menu_item> accesos = modelo.getAccesos();
                        //Gestión notas crédito
                        gestionNotasCredito();
                        for (int i = 0; i < accesos.size(); i++) {
                            if (vista.jbNotasCredito.getName().equals(accesos.get(i).getItemDescripcion())) {
                                gestionNotasCredito();
                                return;
                            }
                        }
                    }
                });
                break;
            }
        }
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
                Integer idIngresoCabecera = modelo.getTm().getFacturaCabeceraList().get(fila).getIdFacturaCabecera();
                Ver_ingreso ver_egreso = new Ver_ingreso(c_inicio, idIngresoCabecera);
                ver_egreso.mostrarVista();
                this.vista.jbDetalle.setEnabled(false);
            }
        }
        if (e.getSource().equals(this.vista.jbFacturar)) {
            facturarVentas();
        }
        if (e.getSource().equals(this.vista.jbHistorialFacturacion)) {
            historialFacturacion();
        } else if (e.getSource().equals(this.vista.jbMasOpciones)) {
            mostrarOpciones();
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
