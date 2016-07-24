/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB_manager.DB_Egreso;
import DB_manager.DB_manager;
import Entities.M_funcionario;
import Entities.M_proveedor;
import Proveedor.Seleccionar_proveedor;
import bakermanager.C_inicio;
import empleado.Seleccionar_funcionario;
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

/**
 *
 * @author Ramiro
 */
public class C_buscar_detalle extends MouseAdapter implements ActionListener, KeyListener {

    V_buscar_detalle vista;
    M_proveedor proveedor;
    M_funcionario funcionario;
    public C_inicio c_inicio;

    public C_buscar_detalle(C_inicio c_inicio) {
        this.vista = new V_buscar_detalle(c_inicio);
        this.c_inicio = c_inicio;
        inicializarVista();
        agregarListeners();
    }

    private void inicializarVista() {
        this.vista.jbDetalle.setEnabled(false);
        Vector condCompra = DB_Egreso.obtenerTipoOperacion();
        this.vista.jcbCondCompra.addItem("Todos");
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondCompra.addItem(condCompra.get(i));
        }
        Vector marca = DB_manager.obtenerMarca();
        this.vista.jcbMarca.addItem("Todos");
        for (int i = 0; i < marca.size(); i++) {
            this.vista.jcbMarca.addItem(marca.get(i));
        }
        Vector rubro = DB_manager.obtenerRubro();
        this.vista.jcbRubro.addItem("Todos");
        for (int i = 0; i < rubro.size(); i++) {
            this.vista.jcbRubro.addItem(rubro.get(i));
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
        this.vista.jbProveedor.addActionListener(this);
        this.vista.jbFuncionario.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbDetalle.addActionListener(this);
        this.vista.jbCerrar.addActionListener(this);
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jtIzq.addMouseListener(this);
        this.vista.jtDer.addMouseListener(this);
    }

    private String empleado() {
        if (vista.jtfFuncionario.getText().isEmpty()) {
            return "Todos";
        }
        return funcionario.getId_funcionario().toString();
    }

    private String proveedor() {
        if (vista.jtfProveedor.getText().isEmpty()) {
            return "Todos";
        }
        return proveedor.getEntidad();
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
                String proveedor = proveedor();
                String marca = vista.jcbMarca.getSelectedItem().toString();
                String impuesto = vista.jcbImpuesto.getSelectedItem().toString();
                String rubro = vista.jcbRubro.getSelectedItem().toString();
                String estado = vista.jcbEstado.getSelectedItem().toString();
                String empleado = empleado();
                String tiop = tipoOperacion();
                boolean tipoBusquedaDescripcion = tipoBusqueda();
                /*
                 * Se utiliza el objeto factory para obtener un TableModel
                 * para los resultados del query.
                 */
                vista.jtIzq.setModel(DB_Egreso.obtenerEgresoDetalleAvanzado(
                        producto, proveedor, marca, impuesto,
                        rubro, estado, fechaInicio, fechaFinal, tiop, empleado, tipoBusquedaDescripcion));
            }
        });
    }

    private void completarCampos() {
        int fila = vista.jtIzq.getSelectedRow();
        int idEgrsoCabecera = Integer.valueOf(String.valueOf(vista.jtIzq.getValueAt(fila, 0).toString()));
        this.vista.jtDer.setModel(DB_Egreso.obtenerEgresoDetalle(idEgrsoCabecera));
    }

    private void borrarParametros() {
        this.vista.jtfBuscar.setText("");
        this.vista.jtfFuncionario.setText("");
        this.vista.jcbCondCompra.setSelectedItem("Todos");
        this.vista.jtfProveedor.setText("");
        this.vista.jcbEstado.setSelectedItem("Todos");
        this.vista.jcbImpuesto.setSelectedItem("Todos");
        this.vista.jcbMarca.setSelectedItem("Todos");
        this.vista.jcbRubro.setSelectedItem("Todos");
        this.proveedor = null;
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
            Integer idEgresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtDer.getValueAt(this.vista.jtDer.getSelectedRow(), 0)));
            Ver_Egresos ver_egreso = new Ver_Egresos(c_inicio, idEgresoCabecera);
            ver_egreso.mostrarVista();
            this.vista.jbDetalle.setEnabled(false);

            this.vista.jbDetalle.setEnabled(false);
        } else if (e.getSource() == this.vista.jbProveedor) {
            Seleccionar_proveedor sp = new Seleccionar_proveedor(this);
            sp.mostrarVista();
        } else if (e.getSource() == this.vista.jbFuncionario) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this);
            sf.mostrarVista();
        } else if (e.getSource() == this.vista.jbCerrar) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtIzq)) {
            int fila = this.vista.jtIzq.rowAtPoint(e.getPoint());
            int columna = this.vista.jtIzq.columnAtPoint(e.getPoint());
            Integer idEgresoDetalle = Integer.valueOf(String.valueOf(this.vista.jtIzq.getValueAt(fila, 0)));
            if ((fila > -1) && (columna > -1)) {
                this.vista.jtDer.setModel(DB_Egreso.obtenerEgresoCabecera(idEgresoDetalle));
                this.vista.jbDetalle.setEnabled(false);
            }
        }
        if (e.getSource().equals(this.vista.jtDer)) {
            int fila = this.vista.jtIzq.rowAtPoint(e.getPoint());
            int columna = this.vista.jtIzq.columnAtPoint(e.getPoint());
            Integer idEgresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtIzq.getValueAt(fila, 0)));
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbDetalle.setEnabled(true);
            } else {
                this.vista.jbDetalle.setEnabled(false);
            }
            if (e.getClickCount() == 2) {
                Ver_Egresos ver_egreso = new Ver_Egresos(c_inicio, idEgresoCabecera);
                ver_egreso.mostrarVista();
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
        if (this.vista.jtIzq.hasFocus()) {
            completarCampos();
        }
    }

    public void recibirProveedor(M_proveedor proveedor) {
        this.proveedor = proveedor;
        String nombre = proveedor.getNombre();
        String entidad = proveedor.getEntidad();
        this.vista.jtfProveedor.setText(nombre + " - " + entidad);
    }

    public void recibirFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
        String nombre = funcionario.getNombre();
        String apellido = funcionario.getApellido();
        String alias = funcionario.getAlias();
        this.vista.jtfFuncionario.setText(nombre + " " + apellido + " (" + alias + ")");
    }
}
