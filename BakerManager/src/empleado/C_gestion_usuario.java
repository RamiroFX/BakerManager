/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package empleado;

import Entities.M_menu_item;
import Utilities.c_packColumn;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Vector;
import GestionRol.Gestion_rol;
import Interface.Gestion;
import bakermanager.C_inicio;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestion_usuario implements Gestion {

    public C_inicio c_inicio;
    M_gestion_usuario modelo;
    V_gestion_usuario vista;

    public C_gestion_usuario(M_gestion_usuario modelo, V_gestion_usuario vista, C_inicio c_main) {
        this.c_inicio = c_main;
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    @Override
    public final void inicializarVista() {
        c_packColumn.packColumns(this.vista.jtUsuario, 2);
        this.vista.jbModificarUsuario.setEnabled(false);
        this.vista.jbEliminarUsuario.setEnabled(false);
        this.vista.jftCedulaIdentidad.setFormatterFactory(
                new javax.swing.text.DefaultFormatterFactory(
                        new javax.swing.text.NumberFormatter(
                                new java.text.DecimalFormat("#,##0"))));
        this.vista.jtfBuscar.setEnabled(false);
        this.vista.jckbCedula.setEnabled(false);
        this.vista.jckbNombreApellido.setEnabled(false);
        this.vista.jrbExclusivo.setEnabled(false);
        this.vista.jrbInclusivo.setEnabled(false);
        this.vista.jbCrearUsuario.setEnabled(false);
        this.vista.jbModificarUsuario.setEnabled(false);
        this.vista.jbEliminarUsuario.setEnabled(false);
        this.vista.jbGestionRol.setEnabled(false);
        this.vista.jbCambiarPassword.setEnabled(false);
        ArrayList<M_menu_item> accesos = c_inicio.modelo.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbCrearUsuario.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbCrearUsuario.setEnabled(true);
            }
            if (this.vista.jbModificarUsuario.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbModificarUsuario.setEnabled(true);
            }
            if (this.vista.jbEliminarUsuario.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbEliminarUsuario.setEnabled(true);
            }
            if (this.vista.jbGestionRol.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbGestionRol.setEnabled(true);
            }
            if (this.vista.jbCambiarPassword.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbCambiarPassword.setEnabled(true);
            }
            if (this.vista.jtfBuscar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jtfBuscar.setEnabled(true);
                this.vista.jckbCedula.setEnabled(true);
                this.vista.jckbNombreApellido.setEnabled(true);
                this.vista.jrbExclusivo.setEnabled(true);
                this.vista.jrbInclusivo.setEnabled(true);
            }
        }
    }

    /**
     * Establece el tamaño, posicion y visibilidad de la vista.
     */
    public void mostrarVista() {
        this.vista.setSize(this.c_inicio.establecerTamañoPanel());
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        this.c_inicio.agregarVentana(this.vista);
    }

    /**
     * Elimina la vista.
     */
    @Override
    public final void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    /**
     * Agrega ActionListeners los controles.
     */
    @Override
    public final void agregarListeners() {
        this.vista.jbCrearUsuario.addActionListener(this);
        this.vista.jbModificarUsuario.addActionListener(this);
        this.vista.jbEliminarUsuario.addActionListener(this);
        this.vista.jtUsuario.addMouseListener(this);
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jbGestionRol.addActionListener(this);
        this.vista.jckbCedula.addActionListener(this);
        this.vista.jckbNombreApellido.addActionListener(this);
        this.vista.jrbExclusivo.addActionListener(this);
        this.vista.jrbInclusivo.addActionListener(this);
        this.vista.jbCambiarPassword.addActionListener(this);
    }

    public void displayQueryResults() {
        /*
         * Para permitir que los mensajes puedan ser desplegados, no se ejecuta
         * el query directamente, sino que se lo coloca en una cola de eventos
         * para que se ejecute luego de los eventos pendientes.
         */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                /*
                 * Se utiliza el objeto factory para obtener un TableModel
                 * para los resultados del query.
                 */
                String busqueda = vista.jtfBuscar.getText().toLowerCase();
                boolean isExclusivo = false;
                boolean entidad = false;
                boolean ruc = false;
                if (vista.jrbExclusivo.isSelected()) {
                    isExclusivo = true;
                }
                if (vista.jckbNombreApellido.isSelected()) {
                    entidad = true;
                }
                if (vista.jckbCedula.isSelected()) {
                    ruc = true;
                }
                vista.jtUsuario.setModel(modelo.consultarFuncionario(busqueda, isExclusivo, entidad, ruc));
                c_packColumn.packColumns(vista.jtUsuario, 2);
                vista.jbModificarUsuario.setEnabled(false);
                vista.jbEliminarUsuario.setEnabled(false);
            }
        });
    }

    private void cambiarContraseña() {
        CambiarPassword cp = new CambiarPassword(this.c_inicio.vista, c_inicio);
        cp.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbCrearUsuario) {
            Crear_empleado crearEmpleado = new Crear_empleado(c_inicio);
            crearEmpleado.mostrarVista();
        } else if (e.getSource() == this.vista.jbModificarUsuario) {
            int row = this.vista.jtUsuario.getSelectedRow();
            if (row > 0) {
                int idEmpleado = Integer.valueOf(String.valueOf(this.vista.jtUsuario.getValueAt(row, 0)));
                if (idEmpleado == 1) {
                    JOptionPane.showMessageDialog(null, "El usuario admin no puede ser modificado.", "Atención", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Modificar_empleado modEmpleado = new Modificar_empleado(c_inicio, idEmpleado);
                    modEmpleado.mostrarVista();
                }

            }else{
                JOptionPane.showMessageDialog(null, "Seleccione un usuario para modificar", "Atención", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (e.getSource() == this.vista.jbEliminarUsuario) {
            int row = this.vista.jtUsuario.getSelectedRow();
            int idEmpleado = Integer.valueOf(String.valueOf(this.vista.jtUsuario.getValueAt(row, 0)));
            modelo.eliminarUsuario(idEmpleado);
            this.vista.jtUsuario.setModel(modelo.consultarFuncionario("", false, false, false));
        } else if (e.getSource() == this.vista.jbCambiarPassword) {
            cambiarContraseña();
        } else if (e.getSource() == this.vista.jbGestionRol) {
            Gestion_rol gestion_rol = new Gestion_rol(c_inicio);
            gestion_rol.mostrarVista();
        } else if (e.getSource() == this.vista.jrbExclusivo) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jrbInclusivo) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jckbCedula) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jckbNombreApellido) {
            displayQueryResults();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtUsuario.rowAtPoint(e.getPoint());
        int columna = this.vista.jtUsuario.columnAtPoint(e.getPoint());
        int idFuncionario = (Integer.valueOf((String) this.vista.jtUsuario.getValueAt(fila, 0)));
        //modelo.setFuncionario(DB_Funcionario.obtenerDatosFuncionarioID(idFuncionario));
        modelo.setFuncionario(modelo.obtenerDatosFuncionarioID(idFuncionario));
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbModificarUsuario.setEnabled(true);
            this.vista.jbEliminarUsuario.setEnabled(true);
            this.vista.jtfAlias.setText(modelo.getFuncionario().getAlias());
            this.vista.jtfDireccion.setText(modelo.getFuncionario().getDireccion());
            this.vista.jtfCorreoElectronico.setText(modelo.getFuncionario().getEmail());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                this.vista.jtfFechaIngreso.setText(sdf.format(modelo.getFuncionario().getFecha_ingreso()));
            } catch (java.lang.NullPointerException ex) {
                this.vista.jtfFechaIngreso.setText("");
            }
            try {
                this.vista.jtfFechaNacimiento.setText(sdf.format(modelo.getFuncionario().getFecha_nacimiento()));
            } catch (java.lang.NullPointerException ex) {
                this.vista.jtfFechaNacimiento.setText("");
            }
            this.vista.jtaObservacion.setText(modelo.getFuncionario().getObservacion());
            this.vista.jtfNroCelular.setText(modelo.getFuncionario().getNro_celular());
            this.vista.jtfNroTelefono.setText(modelo.getFuncionario().getNro_telefono());
            //Datos personales
            this.vista.jtfNombre.setText(modelo.getFuncionario().getNombre());
            this.vista.jtfApellido.setText(modelo.getFuncionario().getApellido());
            this.vista.jftCedulaIdentidad.setValue(modelo.getFuncionario().getCedula());
            this.vista.jtfGenero.setText(modelo.getFuncionario().getSexo());
            this.vista.jtfEstadoCivil.setText(modelo.getFuncionario().getEstado_civil());
            this.vista.jtfNacionalidad.setText(modelo.getFuncionario().getPais());
            this.vista.jtfCiudad.setText(modelo.getFuncionario().getCiudad());
            this.vista.jcbRol.removeAllItems();
            Vector vRolUsuario = modelo.obtenerRolFuncionario();
            for (int i = 0; i < vRolUsuario.size(); i++) {
                this.vista.jcbRol.addItem(vRolUsuario.get(i));
            }
        } else {
            this.vista.jbModificarUsuario.setEnabled(false);
            this.vista.jbEliminarUsuario.setEnabled(false);
        }
        if (e.getClickCount() == 2) {
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
        displayQueryResults();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
