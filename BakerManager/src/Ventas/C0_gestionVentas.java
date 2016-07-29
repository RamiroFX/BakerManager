/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Cliente.Seleccionar_cliente;
import DB_manager.DB_Egreso;
import DB_manager.DB_Ingreso;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Interface.Gestion;
import bakermanager.C_inicio;
import empleado.Seleccionar_funcionario;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C0_gestionVentas implements Gestion {

    public M0_gestionVentas modelo;
    public V0_gestionVentas vista;
    public C_inicio c_inicio;
    private ArrayList<M_menu_item> accesos;

    public C0_gestionVentas(M0_gestionVentas modelo, V0_gestionVentas vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        agregarListeners();
    }

    @Override
    public final void inicializarVista() {
        this.vista.jbDetalle.setEnabled(false);
        Vector condCompra = DB_Egreso.obtenerTipoOperacion();
        this.vista.jcbCondCompra.addItem("Todos");
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondCompra.addItem(condCompra.get(i));
        }
        Date today = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(today);
        this.vista.jddFinal.setDate(today);
        accesos = c_inicio.modelo.getRol_usuario().getAccesos();
        this.vista.jbAgregar.setEnabled(false);
        this.vista.jbBuscar.setEnabled(false);
        this.vista.jbBuscarDetalle.setEnabled(false);
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbResumen.setEnabled(false);
        this.vista.jbCliente.setEnabled(false);
        this.vista.jbEmpleado.setEnabled(false);
        this.vista.jcbCondCompra.setEnabled(false);
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbAgregar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbAgregar.setEnabled(true);
            }
            if (this.vista.jbBuscar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbBuscar.setEnabled(true);
                this.vista.jbCliente.setEnabled(true);
                this.vista.jbEmpleado.setEnabled(true);
                this.vista.jcbCondCompra.setEnabled(true);
            }
            if (this.vista.jbDetalle.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbDetalle.setEnabled(true);
            }
            if (this.vista.jbResumen.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbResumen.setEnabled(true);
            }
        }
    }

    @Override
    public final void agregarListeners() {
        this.vista.jbAgregar.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbDetalle.addActionListener(this);
        this.vista.jtEgresoCabecera.addMouseListener(this);
    }

    @Override
    public final void mostrarVista() {
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        this.c_inicio.agregarVentana(vista);
    }

    @Override
    public final void cerrar() {
        this.vista.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbAgregar)) {
            CrearVentas cv = new CrearVentas(this);
            cv.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbBuscar)) {
            displayQueryResults();
        }
        if (e.getSource().equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this);
            sc.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this);
            sf.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbBorrar)) {
            borrarDatos();
        }
        if (e.getSource().equals(this.vista.jbDetalle)) {
            int fila = this.vista.jtEgresoCabecera.getSelectedRow();
            Integer idIngresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtEgresoCabecera.getValueAt(fila, 0)));
            Ver_ingreso ver_egreso = new Ver_ingreso(c_inicio, idIngresoCabecera);
            ver_egreso.mostrarVista();
            this.vista.jbDetalle.setEnabled(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtEgresoCabecera)) {
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void displayQueryResults() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (modelo.validarFechas(vista.jddInicio.getDate(), vista.jddFinal.getDate())) {
                    vista.jtEgresoCabecera.setModel(modelo.obtenerVentas(vista.jddInicio.getDate(), vista.jddFinal.getDate(), vista.jtfNroFactura.getText(), vista.jcbCondCompra.getSelectedItem().toString()));
                    Utilities.c_packColumn.packColumns(vista.jtEgresoCabecera, 1);
                } else {
                    vista.jddFinal.setDate(vista.jddInicio.getDate());
                    vista.jddFinal.updateUI();
                    JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public void recibirCliente(M_cliente cliente) {
        this.modelo.cabecera.setCliente(cliente);
        String nombre = this.modelo.cabecera.getCliente().getNombre();
        String entidad = this.modelo.cabecera.getCliente().getEntidad();
        this.vista.jtfCliente.setText(nombre + "-(" + entidad + ")");
    }

    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.cabecera.setFuncionario(funcionario);
        String alias = this.modelo.cabecera.getFuncionario().getAlias();
        String nombre = this.modelo.cabecera.getFuncionario().getNombre();
        String apellido = this.modelo.cabecera.getFuncionario().getApellido();
        this.vista.jtfEmpleado.setText(alias + "-(" + nombre + " " + apellido + ")");
    }

    private void borrarDatos() {
        this.modelo.borrarDatos();
        this.vista.jtfCliente.setText("");
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfNroFactura.setText("");
        this.vista.jcbCondCompra.setSelectedItem("Todos");
    }

    private void obtenerEgresoDetalle(MouseEvent e) {
        int fila = this.vista.jtEgresoCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtEgresoCabecera.columnAtPoint(e.getPoint());
        Integer idIngresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtEgresoCabecera.getValueAt(fila, 0)));
        //setProducto(DBmanagerProducto.mostrarProducto(idProducto));
        this.modelo.setCabecera(DB_Ingreso.obtenerIngresoCabeceraID(idIngresoCabecera));
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbDetalle.setEnabled(true);
            this.vista.jtEgresoDetalle.setModel(DB_Ingreso.obtenerIngresoDetalle(idIngresoCabecera));
        } else {
            this.vista.jbDetalle.setEnabled(false);
        }
        if (e.getClickCount() == 2) {
            Ver_ingreso ver_egreso = new Ver_ingreso(c_inicio, idIngresoCabecera);
            ver_egreso.mostrarVista();
            this.vista.jbDetalle.setEnabled(false);
        }
    }
}
