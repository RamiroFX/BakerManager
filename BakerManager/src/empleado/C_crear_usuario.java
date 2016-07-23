/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package empleado;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Vector;
import Entities.M_funcionario;
import java.util.Date;

/**
 *
 * @author Ramiro
 */
public class C_crear_usuario extends MouseAdapter implements ActionListener, KeyListener {

    private V_crear_usuario vista;
    private M_crear_usuario modelo;

    public C_crear_usuario(M_crear_usuario modelo, V_crear_usuario vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    /**
     * Establece el tamaño, posicion y visibilidad de la vista.
     */
    public void mostrarVista() {
        this.vista.setSize(800, 350);
        this.vista.setLocationRelativeTo(this.vista.getOwner());
        this.vista.setVisible(true);
    }

    /**
     * Elimina la vista.
     */
    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    /**
     * Agrega ActionListeners los controles.
     */
    private void agregarListeners() {
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbQuitarRol.addActionListener(this);
        this.vista.jbAgregarRol.addActionListener(this);
        this.vista.jftCedulaIdentidad.addKeyListener(this);
        this.vista.jtfNombre.addMouseListener(this);
        this.vista.jtfApellido.addMouseListener(this);
        this.vista.jftCedulaIdentidad.addMouseListener(this);
        this.vista.dccFechaNacimiento.addMouseListener(this);
        this.vista.dccFechaIngreso.addMouseListener(this);
        this.vista.jpassword1.addMouseListener(this);
        this.vista.jpassword2.addMouseListener(this);
        this.vista.jtfAlias.addMouseListener(this);
        this.vista.jtRolesDisponibles.addMouseListener(this);
        this.vista.jtRolesSeleccionados.addMouseListener(this);
    }

    /**
     * Agrega valores a los componentes.
     */
    private void inicializarVista() {
        this.vista.jbAgregarRol.setEnabled(false);
        this.vista.jbQuitarRol.setEnabled(false);
        Vector genero = modelo.obtenerGenero();
        for (int i = 0; i < genero.size(); i++) {
            this.vista.jcbGenero.addItem(genero.get(i));
        }
        Vector pais = modelo.obtenerPais();
        for (int i = 0; i < pais.size(); i++) {
            this.vista.jcbNacionalidad.addItem(pais.get(i));
        }
        Vector ciudad = modelo.obtenerCiudad();
        for (int i = 0; i < ciudad.size(); i++) {
            this.vista.jcbCiudad.addItem(ciudad.get(i));
        }
        Vector estadoCivil = modelo.obtenerEstadoCivil();
        for (int i = 0; i < estadoCivil.size(); i++) {
            this.vista.jcbEstadoCivil.addItem(estadoCivil.get(i));
        }
        this.vista.dccFechaIngreso.setDate(Calendar.getInstance().getTime());
        this.vista.jftCedulaIdentidad.setFormatterFactory(
                new javax.swing.text.DefaultFormatterFactory(
                        new javax.swing.text.NumberFormatter(
                                new java.text.DecimalFormat("#,##0"))));
        this.vista.jtRolesDisponibles.setModel(modelo.obtenerRolesDisp());
    }

    private boolean isValidDataEntry() {
        /*
         * VALIDAR NOMBRE
         */
        System.out.println("isValidDataEntry");
        System.out.println("nombre");
        String nombre;
        if (this.vista.jtfNombre.getText().isEmpty()) {
            this.vista.jtfNombre.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo nombre esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales1);
            return false;
        } else {
            if (this.vista.jtfNombre.getText().length() > 30) {
                this.vista.jtfNombre.setBackground(Color.red);
                javax.swing.JOptionPane.showMessageDialog(this.vista,
                        "El campo nombre sobrepasa el limite permitido(30) de caracteres",
                        "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales1);
                return false;
            } else {
                nombre = this.vista.jtfNombre.getText();
            }
        }
        /*
         * VALIDAR APELLIDO
         */
        System.out.println("apellido");
        String apellido;
        if (this.vista.jtfApellido.getText().isEmpty()) {
            this.vista.jtfApellido.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo apellido esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales1);
            return false;
        } else {
            if (this.vista.jtfApellido.getText().length() > 30) {
                this.vista.jtfApellido.setBackground(Color.red);
                javax.swing.JOptionPane.showMessageDialog(this.vista,
                        "El campo apellido sobrepasa el limite permitido(30) de caracteres",
                        "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales1);
                return false;
            } else {
                apellido = this.vista.jtfApellido.getText();
            }
        }
        /*
         *VALIDAR CI
         */
        System.out.println("cedula");
        Integer cedula;
        try {
            String LongToString = String.valueOf(this.vista.jftCedulaIdentidad.getValue());
            cedula = Integer.valueOf(LongToString.replace(".", ""));
        } catch (Exception e) {
            this.vista.jftCedulaIdentidad.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Coloque un Numero de CI valido",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales1);
            return false;
        }
        /*
         * VALIDAR FECHA NACIMIENTO
         */
        System.out.println("fechaNacimiento");
        Date fechaNacimiento;
        try {
            fechaNacimiento = this.vista.dccFechaNacimiento.getDate();
        } catch (Exception e) {
            this.vista.dccFechaNacimiento.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "Ingrese una fecha valida en el campo Fecha nacimiento",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales1);
            return false;
        }
        /*
         * VALIDAR ALIAS
         */
        System.out.println("alias");
        String alias;
        if (this.vista.jtfAlias.getText().isEmpty()) {
            this.vista.jtfAlias.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo alias esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosEmpresariales);
            return false;
        } else {
            if (this.vista.jtfAlias.getText().length() > 30) {
                this.vista.jtfAlias.setBackground(Color.red);
                javax.swing.JOptionPane.showMessageDialog(this.vista,
                        "El campo alias sobrepasa el limite permitido(30) de caracteres",
                        "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales1);
                return false;
            } else {
                alias = this.vista.jtfAlias.getText().toLowerCase();
            }
        }
        /*
         * VALIDAR TELEFONO
         */
        System.out.println("telefono");
        String telefono;
        try {
            String valor = this.vista.jtfNroTelefono.getText();
            if (valor.isEmpty()) {
                telefono = null;
            } else {
                if (this.vista.jtfNroTelefono.getText().length() > 30) {
                    this.vista.jtfNroTelefono.setBackground(Color.red);
                    javax.swing.JOptionPane.showMessageDialog(this.vista,
                            "El campo teléfono sobrepasa el limite permitido(30) de caracteres",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                    this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales1);
                    return false;
                } else {
                    telefono = this.vista.jtfNroTelefono.getText();
                }
            }
        } catch (NumberFormatException e) {
            this.vista.jtfNroTelefono.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "Coloque un numero de telefono valido",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales2);
            return false;
        }
        /*
         * VALIDAR CELULAR
         */
        System.out.println("CELULAR");
        String celular;
        try {
            String valor = this.vista.jtfNroCelular.getText();
            if (valor.isEmpty()) {
                celular = null;
            } else {
                if (this.vista.jtfNroTelefono.getText().length() > 30) {
                    this.vista.jtfNroTelefono.setBackground(Color.red);
                    javax.swing.JOptionPane.showMessageDialog(this.vista,
                            "El campo celular sobrepasa el limite permitido(30) de caracteres",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                    this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales1);
                    return false;
                } else {
                    celular = this.vista.jtfNroCelular.getText();
                }
            }
        } catch (NumberFormatException e) {
            this.vista.jtfNroCelular.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "Coloque un numero de celular válido",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosPersonales2);
            return false;
        }
        /*
         * VALIDAR FECHA INGRESOO
         */
        System.out.println("fechaIngreso");
        Date fechaIngreso;
        try {
            //Inicio fechas
            if (this.vista.dccFechaIngreso.getDate() == null) {
                throw new Exception(new NullPointerException());
            } else {
                fechaIngreso = this.vista.dccFechaIngreso.getDate();
            }
        } catch (Exception e) {
            this.vista.dccFechaIngreso.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "Ingrese una fecha válida en el campo Fecha ingreso",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpDatosEmpresariales);
            return false;
        }
        System.out.println("Roles");
        if (this.vista.jtRolesSeleccionados.getRowCount() < 1) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "Seleccione por lo menos un rol",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtpCenter.setSelectedComponent(vista.jpRol);
            return false;
        }
        return true;
    }

    private void checkJFTcedula() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String valorIngresado = vista.jftCedulaIdentidad.getText().replace(".", "");
                valorIngresado = valorIngresado.replace(",", "");
                Long StringToLong = null;
                try {
                    StringToLong = Long.valueOf(valorIngresado);
                } catch (NumberFormatException numberFormatException) {
                    javax.swing.JOptionPane.showMessageDialog(vista, "Ingrese solo numeros",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                }
                vista.jftCedulaIdentidad.setValue(StringToLong);
                String valorJFT = vista.jftCedulaIdentidad.getText();
                vista.jftCedulaIdentidad.select(valorJFT.length(), valorJFT.length());
            }
        });
    }

    private void crearUsuario() {
        if (isValidDataEntry()) {
            char[] password1 = vista.jpassword1.getPassword();
            char[] password2 = vista.jpassword2.getPassword();

            String email = this.vista.jtfCorreoElectronico.getText();
            if (email.isEmpty()) {
                email = null;
            }
            String direccion = this.vista.jtfDireccion.getText();
            if (direccion.isEmpty()) {
                direccion = null;
            }
            String observacion = this.vista.jtaObservacion.getText();
            if (observacion.isEmpty()) {
                observacion = null;
            }
            String telefono = this.vista.jtfNroTelefono.getText();
            if (telefono.isEmpty()) {
                telefono = null;
            }
            String celular = this.vista.jtfNroCelular.getText();
            if (celular.isEmpty()) {
                celular = null;
            }
            Date fechaIngreso = this.vista.dccFechaIngreso.getDate();
            String nombre = this.vista.jtfNombre.getText();
            String apellido = this.vista.jtfApellido.getText();
            String LongToString = String.valueOf(this.vista.jftCedulaIdentidad.getValue());
            Integer cedula = Integer.valueOf(LongToString.replace(".", ""));
            Date fechaNacimiento = this.vista.dccFechaNacimiento.getDate();
            String alias = this.vista.jtfAlias.getText().toLowerCase();

            M_funcionario funcionario = new M_funcionario();
            funcionario.setNombre(nombre);
            funcionario.setApellido(apellido);
            funcionario.setCedula(cedula);
            funcionario.setFecha_nacimiento(fechaNacimiento);
            funcionario.setAlias(alias);
            funcionario.setPassword(String.copyValueOf(password1));
            funcionario.setNro_telefono(telefono);
            funcionario.setNro_celular(celular);
            funcionario.setFecha_ingreso(fechaIngreso);
            funcionario.setEstado_civil((String) this.vista.jcbEstadoCivil.getSelectedItem());
            funcionario.setObservacion(observacion);
            funcionario.setDireccion(direccion);
            funcionario.setEmail(email);
            funcionario.setCiudad((String) this.vista.jcbCiudad.getSelectedItem());
            funcionario.setRol(null);//se establece en el modelo
            funcionario.setSexo((String) this.vista.jcbGenero.getSelectedItem());
            funcionario.setPais((String) this.vista.jcbNacionalidad.getSelectedItem());

            if (modelo.crearUsuario(funcionario, password1, password2)) {
                cerrar();
            }
        }
    }

    private void agregarRol() {
        int fila = this.vista.jtRolesDisponibles.getSelectedRow();
        int idRol = (Integer.valueOf((String) this.vista.jtRolesDisponibles.getValueAt(fila, 0)));
        String descripcionRol = (String) this.vista.jtRolesDisponibles.getValueAt(fila, 1);
        this.modelo.agregarRol(idRol, descripcionRol);
        this.vista.jtRolesSeleccionados.setModel(this.modelo.obtenerRolesSelecc());
        Utils.c_packColumn.packColumns(this.vista.jtRolesSeleccionados, 1);
        this.vista.jbAgregarRol.setEnabled(false);
        this.vista.jbQuitarRol.setEnabled(false);
    }

    private void quitarRol() {
        int fila = this.vista.jtRolesSeleccionados.getSelectedRow();
        int idRol = (int) this.vista.jtRolesSeleccionados.getValueAt(fila, 0);
        this.modelo.quitarRol(idRol);
        this.vista.jtRolesSeleccionados.setModel(this.modelo.obtenerRolesSelecc());
        Utils.c_packColumn.packColumns(this.vista.jtRolesSeleccionados, 1);
        this.vista.jbAgregarRol.setEnabled(false);
        this.vista.jbQuitarRol.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbCancelar) {
            cerrar();
        } else if (e.getSource() == this.vista.jbAceptar) {
            crearUsuario();
        } else if (e.getSource() == this.vista.jbAgregarRol) {
            agregarRol();
        } else if (e.getSource() == this.vista.jbQuitarRol) {
            quitarRol();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.vista.jftCedulaIdentidad.hasFocus()) {
            this.vista.jftCedulaIdentidad.setBackground(Color.white);
        } else if (this.vista.jtfNombre.hasFocus()) {
            this.vista.jtfNombre.setBackground(Color.white);
        } else if (this.vista.jtfApellido.hasFocus()) {
            this.vista.jtfApellido.setBackground(Color.white);
        } else if (this.vista.jtfAlias.hasFocus()) {
            this.vista.jtfAlias.setBackground(Color.white);
        } else if (this.vista.dccFechaIngreso.hasFocus()) {
            this.vista.dccFechaIngreso.setBackground(Color.white);
        } else if (this.vista.dccFechaNacimiento.hasFocus()) {
            this.vista.dccFechaNacimiento.setBackground(Color.white);
        } else if (this.vista.jpassword1.hasFocus()) {
            this.vista.jpassword1.setBackground(Color.white);
        } else if (this.vista.jpassword2.hasFocus()) {
            this.vista.jpassword2.setBackground(Color.white);
        }
        if (e.getSource().equals(this.vista.jtRolesDisponibles)) {
            int fila = this.vista.jtRolesDisponibles.rowAtPoint(e.getPoint());
            int columna = this.vista.jtRolesDisponibles.columnAtPoint(e.getPoint());
            int idRol = (Integer.valueOf((String) this.vista.jtRolesDisponibles.getValueAt(fila, 0)));
            String descripcionRol = (String) this.vista.jtRolesDisponibles.getValueAt(fila, 1);
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbAgregarRol.setEnabled(true);
            } else {
                this.vista.jbAgregarRol.setEnabled(false);
            }
            if (e.getClickCount() == 2) {
                this.modelo.agregarRol(idRol, descripcionRol);
                this.vista.jtRolesSeleccionados.setModel(this.modelo.obtenerRolesSelecc());
                Utils.c_packColumn.packColumns(this.vista.jtRolesSeleccionados, 1);
                this.vista.jbAgregarRol.setEnabled(false);
                this.vista.jbQuitarRol.setEnabled(false);
            }
        }
        if (e.getSource().equals(this.vista.jtRolesSeleccionados)) {
            int fila = this.vista.jtRolesSeleccionados.rowAtPoint(e.getPoint());
            int columna = this.vista.jtRolesSeleccionados.columnAtPoint(e.getPoint());
            int idRol = (int) this.vista.jtRolesSeleccionados.getValueAt(fila, 0);
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbQuitarRol.setEnabled(true);
            } else {
                this.vista.jbQuitarRol.setEnabled(false);
            }
            if (e.getClickCount() == 2) {
                this.modelo.quitarRol(idRol);
                this.vista.jtRolesSeleccionados.setModel(this.modelo.obtenerRolesSelecc());
                Utils.c_packColumn.packColumns(this.vista.jtRolesSeleccionados, 1);
                this.vista.jbAgregarRol.setEnabled(false);
                this.vista.jbQuitarRol.setEnabled(false);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == this.vista.jftCedulaIdentidad) {
            checkJFTcedula();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
