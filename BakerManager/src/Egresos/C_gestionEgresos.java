/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import Charts.MenuDiagramas;
import DB.DB_Egreso;
import DB.DB_manager;
import Entities.M_egreso_cabecera;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Entities.M_proveedor;
import MenuPrincipal.DatosUsuario;
import Proveedor.Seleccionar_proveedor;
import bakermanager.C_inicio;
import Empleado.SeleccionarFuncionario;
import Entities.Estado;
import Interface.RecibirEmpleadoCallback;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionEgresos extends MouseAdapter implements ActionListener, KeyListener, RecibirEmpleadoCallback {

    private M_egreso_cabecera m_egreso_cabecera;
    V_gestion_egresos vista;
    public C_inicio c_inicio;

    public C_gestionEgresos(V_gestion_egresos vista, C_inicio c_inicio) {
        this.m_egreso_cabecera = new M_egreso_cabecera();
        this.vista = vista;
        this.c_inicio = c_inicio;
        this.vista.setLocation(c_inicio.centrarPantalla(this.vista));
        inicializarVista();
        concederPermisos();
    }

    /**
     * @return the producto
     */
    public M_egreso_cabecera getEgreso_cabecera() {
        return m_egreso_cabecera;
    }

    /**
     * @param producto the producto to set
     */
    public void setEgreso_cabecera(M_egreso_cabecera m_egreso_cabecera) {
        this.m_egreso_cabecera = m_egreso_cabecera;
    }

    private void inicializarVista() {
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);
        Vector condCompra = DB_Egreso.obtenerTipoOperacion();
        this.vista.jcbCondCompra.addItem("Todos");
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondCompra.addItem(condCompra.get(i));
        }
        ArrayList<Estado> estados = DB_manager.obtenerEstados();
        estados.add(new Estado(-1, "Todos"));
        for (int i = 0; i < estados.size(); i++) {
            this.vista.jcbEstado.addItem(estados.get(i));
        }
        Date today = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(today);
        this.vista.jddFinal.setDate(today);
    }

    public void mostrarVista() {
        this.c_inicio.agregarVentana(vista);
    }

    private void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
            if (this.vista.jbAgregar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbAgregar.setEnabled(true);
                this.vista.jbAgregar.addActionListener(this);
            }
            if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbResumen.addActionListener(this);
            }
            if (this.vista.jbAnular.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbAnular.addActionListener(this);
            }
            if (this.vista.jbDetalle.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbDetalle.addActionListener(this);
            }
            if (this.vista.jbBuscar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbBuscar.setEnabled(true);
                this.vista.jcbCondCompra.setEnabled(true);
                this.vista.jtfNroFactura.setEnabled(true);
                this.vista.jddFinal.setEnabled(true);
                this.vista.jddInicio.setEnabled(true);
                this.vista.jbBuscar.addActionListener(this);
                this.vista.jbProveedor.addActionListener(this);
                this.vista.jbFuncionario.addActionListener(this);
                this.vista.jbBorrar.addActionListener(this);
                this.vista.jbBuscarDetalle.addActionListener(this);
            }
        }
        this.vista.jbMasOpciones.addActionListener(this);
        this.vista.jtEgresoCabecera.addMouseListener(this);
        //this.vista.jbGraficos.addActionListener(this);
        this.vista.jtEgresoCabecera.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbAgregar.addKeyListener(this);
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jbDetalle.addKeyListener(this);
        this.vista.jbAnular.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbProveedor.addKeyListener(this);
        this.vista.jbFuncionario.addKeyListener(this);
        this.vista.jcbCondCompra.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbBuscarDetalle.addKeyListener(this);
    }

    private void verificarPermiso() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
            if (this.vista.jbDetalle.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbDetalle.setEnabled(true);
            }
            if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbResumen.setEnabled(true);
            }
            if (this.vista.jbAnular.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbAnular.setEnabled(true);
            }
        }
    }

    private void verDetalle() {
        int row = this.vista.jtEgresoCabecera.getSelectedRow();
        if (row > -1) {
            Integer idEgresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtEgresoCabecera.getValueAt(row, 0)));
            Ver_Egresos ver_egreso = new Ver_Egresos(c_inicio, idEgresoCabecera);
            ver_egreso.mostrarVista();
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
        }
    }

    private String empleado() {
        if (vista.jtfFuncionario.getText().isEmpty()) {
            return "Todos";
        }
        return m_egreso_cabecera.getId_empleado().toString();
    }

    private String proveedor() {
        if (vista.jtfProveedor.getText().isEmpty()) {
            return "Todos";
        }
        return m_egreso_cabecera.getProveedor().getEntidad();
    }

    private String tipoOperacion() {
        String idEmpleado = "";
        String datosTipoOperacion = vista.jcbCondCompra.getSelectedItem().toString();
        if (vista.jcbCondCompra.getSelectedItem().toString().equals("Todos")) {
            return "Todos";//1kg*26000// 10kg*210000
        } else {
            idEmpleado = DB_Egreso.obtenerTipoOperacion(datosTipoOperacion).toString();
        }
        return idEmpleado;
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
                if (vista.jddInicio.getDate() != null && vista.jddFinal.getDate() != null) {
                    int dateValue = vista.jddInicio.getDate().compareTo(vista.jddFinal.getDate());
                    if (dateValue > 0) {
                        vista.jddFinal.setDate(vista.jddInicio.getDate());
                        vista.jddFinal.updateUI();
                        JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                String fechaInicio = "";
                String fechaFinal = "";
                Integer nro_factura = null;
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
                try {
                    if (!vista.jtfNroFactura.getText().isEmpty()) {
                        nro_factura = Integer.valueOf(String.valueOf(vista.jtfNroFactura.getText()));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(vista, "El numero de factura debe ser solo numérico", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String empleado = empleado();
                String proveedor = proveedor();
                String tiop = tipoOperacion();
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                /*
                 * Se utiliza el objeto factory para obtener un TableModel
                 * para los resultados del query.
                 */
                vista.jtEgresoCabecera.setModel(DB_Egreso.obtenerEgreso(proveedor, nro_factura, empleado, fechaInicio, fechaFinal, tiop, estado.getId()));
                Utilities.c_packColumn.packColumns(vista.jtEgresoCabecera, 1);
                controlarTablaEgreso();
            }
        });
    }

    private void completarCampos() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int fila = vista.jtEgresoCabecera.getSelectedRow();
                if (fila > -1) {
                    int idEgrsoCabecera = Integer.valueOf(String.valueOf(vista.jtEgresoCabecera.getValueAt(fila, 0).toString()));
                    vista.jtEgresoDetalle.setModel(DB_Egreso.obtenerEgresoDetalle(idEgrsoCabecera));
                    Utilities.c_packColumn.packColumns(vista.jtEgresoDetalle, 1);
                }
            }
        });
    }

    public final void cerrar() {
        try {
            this.vista.setClosed(true);
        } catch (PropertyVetoException ex) {
        }
    }

    private void borrarParametros() {
        this.m_egreso_cabecera.setId_empleado(null);
        this.m_egreso_cabecera.setFuncionario(null);
        this.m_egreso_cabecera.setId_proveedor(null);
        this.m_egreso_cabecera.setProveedor(null);
        this.m_egreso_cabecera.setNro_factura(null);
        this.vista.jtfProveedor.setText("");
        this.vista.jtfFuncionario.setText("");
        this.vista.jtfNroFactura.setText("");
        this.vista.jcbCondCompra.setSelectedItem("Todos");
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
        String proveedor = proveedor();
        String tiop = tipoOperacion();
        Resumen_egreso re = new Resumen_egreso(c_inicio, this.vista.jtEgresoCabecera.getModel(), proveedor, nro_factura, empleado, vista.jddInicio.getDate(), vista.jddFinal.getDate(), tiop);
        re.setVisible(true);
    }

    public void recibirProveedor(M_proveedor proveedor) {
        this.m_egreso_cabecera.setProveedor(proveedor);
        this.m_egreso_cabecera.setId_proveedor(proveedor.getId());
        String entidad = this.m_egreso_cabecera.getProveedor().getEntidad();
        String nombre = this.m_egreso_cabecera.getProveedor().getNombre();
        this.vista.jtfProveedor.setText(nombre + " - " + entidad);
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.m_egreso_cabecera.setFuncionario(funcionario);
        this.m_egreso_cabecera.setId_empleado(funcionario.getId_funcionario());
        String alias = this.m_egreso_cabecera.getFuncionario().getAlias();
        String apellido = this.m_egreso_cabecera.getFuncionario().getApellido();
        String nombre = this.m_egreso_cabecera.getFuncionario().getNombre();
        this.vista.jtfFuncionario.setText(nombre + " " + apellido + " (" + alias + ")");
    }

    private void controlarTablaEgreso() {
        if (this.vista.jtEgresoCabecera.getRowCount() > 0) {
            this.vista.jbResumen.setEnabled(true);
        } else {
            this.vista.jbResumen.setEnabled(false);
        }
    }

    private void verGraficos() {
        MenuDiagramas ge = new MenuDiagramas(this);
        ge.setVisible(true);
    }

    private void anularCompra() {
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Está seguro que desea continuar? Accion irreversible.", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            int fila = this.vista.jtEgresoCabecera.getSelectedRow();
            if (fila > -1) {
                Integer idIngresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtEgresoCabecera.getValueAt(fila, 0)));
                Object nroFactura = this.vista.jtEgresoCabecera.getValueAt(fila, 1);
                if (nroFactura != null) {
                    int opcion2 = JOptionPane.showConfirmDialog(vista, "¿Desea recuperar el número de factura?.", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                    if (opcion2 == JOptionPane.YES_OPTION) {
                        DB_Egreso.anularCompra(idIngresoCabecera, 2, true);
                    } else {
                        DB_Egreso.anularCompra(idIngresoCabecera, 2, false);
                    }
                } else {
                    DB_Egreso.anularCompra(idIngresoCabecera, 2, false);
                }
            }
        }
        displayQueryResults();
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);
    }

    private void mostrarOpciones() {
        Object[] options = {"Crear egreso con fecha"};
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
//                        ArrayList<M_menu_item> accesos = modelo.getAccesos();
//                        for (int i = 0; i < accesos.size(); i++) {
//                            if (vista.jbHistorialFacturacion.getName().equals(accesos.get(i).getItemDescripcion())) {
//                                historialFacturacion();
//                                return;
//                            }
//                        }
                        crearEgresoConFecha();
                    }
                });
                break;
            }
        }
    }

    private void crearEgresoConFecha() {
        CrearEgresoPorFecha crearEgreso = new CrearEgresoPorFecha(c_inicio.vista);
        crearEgreso.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbBuscar) {
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBuscarDetalle) {
            C_buscar_detalle buscar_detalle = new C_buscar_detalle(c_inicio);
            buscar_detalle.mostrarVista();
        } else if (e.getSource() == this.vista.jbProveedor) {
            Seleccionar_proveedor sp = new Seleccionar_proveedor(this);
            sp.mostrarVista();
        } else if (e.getSource() == this.vista.jbFuncionario) {
            SeleccionarFuncionario sf = new SeleccionarFuncionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (e.getSource() == this.vista.jbBorrar) {
            borrarParametros();
        } else if (e.getSource() == this.vista.jbAgregar) {
            crearEgresoConFecha();
        } else if (e.getSource().equals(this.vista.jbResumen)) {
            crearResumen();
        } else if (e.getSource().equals(this.vista.jbGraficos)) {
            verGraficos();
        } else if (e.getSource() == this.vista.jbDetalle) {
            verDetalle();
        } else if (e.getSource() == this.vista.jbAnular) {
            anularCompra();
        } /*else if (e.getSource().equals(this.vista.jbMasOpciones)) {
            mostrarOpciones();
        }*/
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtEgresoCabecera)) {
            int fila = this.vista.jtEgresoCabecera.rowAtPoint(e.getPoint());
            int columna = this.vista.jtEgresoCabecera.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                Integer idEgresoCabecera = Integer.valueOf(String.valueOf(this.vista.jtEgresoCabecera.getValueAt(fila, 0)));
                setEgreso_cabecera(DB_Egreso.obtenerEgresoCabeceraID(idEgresoCabecera));
                if (e.getClickCount() == 2) {
                    if (this.vista.jbDetalle.isEnabled()) {
                        verDetalle();
                    }
                }
                verificarPermiso();
                this.vista.jtEgresoDetalle.setModel(DB_Egreso.obtenerEgresoDetalle(idEgresoCabecera));
                Utilities.c_packColumn.packColumns(vista.jtEgresoDetalle, 1);
            } else {
                this.vista.jbDetalle.setEnabled(false);
                this.vista.jbAnular.setEnabled(false);
            }
        }
    }

    public void keyTyped(KeyEvent e) {
        displayQueryResults();
    }

    public void keyPressed(KeyEvent e) {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1: {
                for (M_menu_item acceso : accesos) {
                    if (this.vista.jbAgregar.getName().equals(acceso.getItemDescripcion())) {
                        crearEgresoConFecha();
                        break;
                    }
                }
                break;
            }
            case KeyEvent.VK_F2: {
                for (M_menu_item acceso : accesos) {
                    if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                        crearResumen();
                        break;
                    }
                }
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (this.vista.jtEgresoCabecera.hasFocus()) {
            completarCampos();
        }
    }
}
