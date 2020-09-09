/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Cliente.SeleccionarCliente;
import DB.DB_Ingreso;
import DB.DB_manager;
import Entities.M_funcionario;
import bakermanager.C_inicio;
import Empleado.SeleccionarFuncionario;
import Entities.M_cliente;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class C_buscar_venta_detalle extends MouseAdapter implements ActionListener,
        KeyListener, RecibirEmpleadoCallback, RecibirClienteCallback {

    public V_buscarVentaDetalle vista;
    M_cliente cliente;
    M_funcionario funcionario;
    public C_inicio c_inicio;

    public C_buscar_venta_detalle(C_inicio c_inicio) {
        this.vista = new V_buscarVentaDetalle(c_inicio);
        this.c_inicio = c_inicio;
        inicializarVista();
        agregarListeners();
    }

    private void inicializarVista() {
        this.vista.jbDetalle.setEnabled(false);
        Vector condCompra = DB_manager.obtenerTipoOperacion();
        this.vista.jcbCondCompra.addItem("Todos");
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondCompra.addItem(condCompra.get(i));
        }
        Vector marca = DB_manager.obtenerMarca();
        this.vista.jcbMarca.addItem("Todos");
        for (int i = 0; i < marca.size(); i++) {
            this.vista.jcbMarca.addItem(marca.get(i));
        }
        Vector rubro = DB_manager.obtenerCategoria();
        this.vista.jcbCategoria.addItem("Todos");
        for (int i = 0; i < rubro.size(); i++) {
            this.vista.jcbCategoria.addItem(rubro.get(i));
        }
        Vector impuesto = DB_manager.obtenerImpuesto();
        this.vista.jcbImpuesto.addItem("Todos");
        for (int i = 0; i < impuesto.size(); i++) {
            this.vista.jcbImpuesto.addItem(impuesto.get(i));
        }
        Vector estado = DB_manager.obtenerEstado();
        this.vista.jcbEstado.addItem("Todos");
        for (int i = 0; i < estado.size(); i++) {
            this.vista.jcbEstado.addItem(estado.get(i));
        }
        Date today = Calendar.getInstance().getTime();
        this.vista.jddFinal.setDate(today);
        this.vista.jddInicio.setDate(today);
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void agregarListeners() {
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbFuncionario.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbDetalle.addActionListener(this);
        this.vista.jbCerrar.addActionListener(this);
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jtDetalle.table.addMouseListener(this);
        this.vista.jtCabecera.addMouseListener(this);
    }

    private String empleado() {
        if (vista.jtfFuncionario.getText().isEmpty()) {
            return "Todos";
        }
        return funcionario.getId_funcionario().toString();
    }

    private String cliente() {
        if (vista.jtfCliente.getText().isEmpty()) {
            return "Todos";
        }
        return cliente.getEntidad();
    }

    private String tipoOperacion() {
        String idEmpleado = "";
        String datosTipoOperacion = vista.jcbCondCompra.getSelectedItem().toString();
        if (vista.jcbCondCompra.getSelectedItem().toString().equals("Todos")) {
            return "Todos";
        } else {
            idEmpleado = DB_manager.obtenerTipoOperacion(datosTipoOperacion).toString();
        }
        return idEmpleado;
    }

    private boolean tipoBusqueda() {
        if (vista.jrbDescripcion.isSelected()) {
            return true;
        }
        return false;
    }

    public void displayQueryResults() {
        /*
         * Para permitir que los mensajes puedan ser desplegados, no se ejecuta
         * el query directamente, sino que se lo coloca en una cola de eventos
         * para que se ejecute luego de los eventos pendientes.
         */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
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
                String producto = vista.jtfBuscar.getText();
                if (producto.length() > 50) {
                    JOptionPane.showMessageDialog(vista, "Su busqueda sobrepasa el máximo de 50 caracteres.", "Atención", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                producto = producto.replace("!", "!!")
                        .replace("%", "!%")
                        .replace("_", "!_")
                        .replace("[", "![");
                String cliente = cliente();
                String marca = vista.jcbMarca.getSelectedItem().toString();
                String impuesto = vista.jcbImpuesto.getSelectedItem().toString();
                String categoria = vista.jcbCategoria.getSelectedItem().toString();
                String estado = vista.jcbEstado.getSelectedItem().toString();
                String empleado = empleado();
                String tiop = tipoOperacion();
                boolean tipoBusquedaDescripcion = tipoBusqueda();
                /*
                 * Se utiliza el objeto factory para obtener un TableModel
                 * para los resultados del query.
                 */
 /*vista.jtDetalle.setModel(DB_Egreso.obtenerEgresoDetalleAvanzado(
                 producto, cliente, marca, impuesto,
                 categoria, estado, fechaInicio, fechaFinal, tiop, empleado, tipoBusquedaDescripcion));*/
                vista.jtDetalle.establecerModelo(DB_Ingreso.obtenerIngresoDetalleAvanzado(
                        producto, cliente, marca, impuesto,
                        categoria, estado, fechaInicio, fechaFinal, tiop, empleado, tipoBusquedaDescripcion));
            }
        });
    }

    private void completarCampos() {
        int fila = vista.jtDetalle.table.getSelectedRow();
        int idEgrsoCabecera = Integer.valueOf(String.valueOf(vista.jtDetalle.table.getValueAt(fila, 0).toString()));
        this.vista.jtCabecera.setModel(DB_Ingreso.obtenerIngresoDetalle(idEgrsoCabecera));
    }

    private void borrarParametros() {
        this.vista.jtfBuscar.setText("");
        this.vista.jtfFuncionario.setText("");
        this.vista.jcbCondCompra.setSelectedItem("Todos");
        this.vista.jtfCliente.setText("");
        this.vista.jcbEstado.setSelectedItem("Todos");
        this.vista.jcbImpuesto.setSelectedItem("Todos");
        this.vista.jcbMarca.setSelectedItem("Todos");
        this.vista.jcbCategoria.setSelectedItem("Todos");
        this.cliente = null;
        this.funcionario = null;
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbBuscar) {
            this.vista.jbDetalle.setEnabled(false);
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBorrar) {
            borrarParametros();
        } else if (e.getSource() == this.vista.jbDetalle) {
            Integer idEgresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtCabecera.getValueAt(this.vista.jtCabecera.getSelectedRow(), 0)));
            VerIngreso ver_ingreso = new VerIngreso(c_inicio, idEgresoCabecera, false);
            ver_ingreso.mostrarVista();
            this.vista.jbDetalle.setEnabled(false);

            this.vista.jbDetalle.setEnabled(false);
        } else if (e.getSource() == this.vista.jbCliente) {
            SeleccionarCliente sp = new SeleccionarCliente(this.c_inicio.vista);
            sp.setCallback(this);
            sp.mostrarVista();
        } else if (e.getSource() == this.vista.jbFuncionario) {
            SeleccionarFuncionario sf = new SeleccionarFuncionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (e.getSource() == this.vista.jbCerrar) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtDetalle.table)) {
            int fila = this.vista.jtDetalle.table.rowAtPoint(e.getPoint());
            int columna = this.vista.jtDetalle.table.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                Integer idEgresoDetalle = Integer.valueOf(String.valueOf(this.vista.jtDetalle.table.getValueAt(fila, 0)));
                this.vista.jtCabecera.setModel(DB_Ingreso.obtenerIngresoCabecera(idEgresoDetalle));
                this.vista.jbDetalle.setEnabled(false);
            }
        }
        if (e.getSource().equals(this.vista.jtCabecera)) {
            int fila = this.vista.jtDetalle.table.rowAtPoint(e.getPoint());
            int columna = this.vista.jtDetalle.table.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                Integer idEgresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtCabecera.getValueAt(fila, 0)));
                this.vista.jbDetalle.setEnabled(true);
                if (e.getClickCount() == 2) {
                    VerIngreso ver_egreso = new VerIngreso(c_inicio, idEgresoCabecera, false);
                    ver_egreso.mostrarVista();
                    this.vista.jbDetalle.setEnabled(false);
                }
            } else {
                this.vista.jbDetalle.setEnabled(false);
            }
        }
    }

    public void keyTyped(KeyEvent e) {

        displayQueryResults();

    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        if (this.vista.jtDetalle.table.hasFocus()) {
            completarCampos();
        }
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        this.cliente = cliente;
        String nombre = cliente.getNombre();
        String entidad = cliente.getEntidad();
        this.vista.jtfCliente.setText(nombre + " - " + entidad);
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
        String nombre = funcionario.getNombre();
        String apellido = funcionario.getApellido();
        String alias = funcionario.getAlias();
        this.vista.jtfFuncionario.setText(nombre + " " + apellido + " (" + alias + ")");
    }
}
