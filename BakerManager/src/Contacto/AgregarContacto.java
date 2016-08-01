/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Contacto;

import Cliente.C_crear_cliente;
import Cliente.C_modificar_cliente;
import DB_manager.DB_manager;
import Entities.M_cliente_contacto;
import Entities.M_contacto;
import Proveedor.C_crear_proveedor;
import Proveedor.C_modificar_proveedor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class AgregarContacto extends JDialog implements ActionListener {

    public javax.swing.JLabel jlNombre, jlApellido, jlCedulaIdentidad, jlCiudad, jlEstadoCivil,
            jlFechaNacimiento, jlGenero, jlNacionalidad, jlObservacion;
    public javax.swing.JTextField jtfNombre, jtfNroTelefono, jtfApellido;
    public javax.swing.JComboBox jcbCiudad, jcbEstadoCivil, jcbGenero, jcbNacionalidad, jcbTipoCliente, jcbCategoriaCliente;
    public com.toedter.calendar.JDateChooser dccFechaNacimiento;
    public javax.swing.JLabel jlTelefonoContacto, jlCorreoElectronico, jlDireccionContacto;
    public javax.swing.JTextField jtfTelefonoContacto, jtfCorreoElecContacto, jtfDireccionContacto, jtfObservacion;
    public javax.swing.JFormattedTextField jftCedulaIdentidad;
    private JPanel jpCenter, jpSouth;
    private JButton jbAceptar, jbCancelar;
    private C_crear_cliente crearCliente;
    private M_cliente_contacto clie_contacto;
    private M_contacto prov_contacto;
    private C_modificar_cliente modificarCliente;
    private C_crear_proveedor crearProveedor;
    private C_modificar_proveedor modificarProveedor;
    private static final int CREAR_CLIENTE_CONTACTO = 1;
    private static final int MODIFICAR_CLIENTE_CONTACTO = 2;
    private static final int CREAR_PROVEEDOR_CONTACTO = 3;
    private static final int MODIFICAR_PROVEEDOR_CONTACTO = 4;
    private int accion;

    public AgregarContacto(C_crear_cliente crearCliente) {
        super(crearCliente.vista, "Agregar contacto", true);
        this.crearCliente = crearCliente;
        accion = CREAR_CLIENTE_CONTACTO;
        setSize(800, 330);
        setLocationRelativeTo(crearCliente.vista);
        inicializarVista();
        agregarListeners();
    }

    public AgregarContacto(C_modificar_cliente modificarCliente) {
        super(modificarCliente.vista, "Agregar contacto", true);
        this.modificarCliente = modificarCliente;
        accion = MODIFICAR_CLIENTE_CONTACTO;
        setSize(800, 330);
        setLocationRelativeTo(modificarCliente.vista);
        inicializarVista();
        agregarListeners();
    }

    public AgregarContacto(C_crear_proveedor crearProveedor) {
        super(crearProveedor.vista, "Agregar contacto", true);
        this.crearProveedor = crearProveedor;
        accion = CREAR_PROVEEDOR_CONTACTO;
        setSize(800, 330);
        setLocationRelativeTo(crearProveedor.vista);
        inicializarVista();
        agregarListeners();
    }

    public AgregarContacto(C_modificar_proveedor modificarProveedor) {
        super(modificarProveedor.vista, "Agregar contacto", true);
        this.modificarProveedor = modificarProveedor;
        accion = MODIFICAR_PROVEEDOR_CONTACTO;
        setSize(800, 330);
        setLocationRelativeTo(modificarProveedor.vista);
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    private void inicializarVista() {
        int swingConstant = javax.swing.SwingConstants.CENTER;
        //Labels, textfields, combobox
        jlNombre = new javax.swing.JLabel("Nombre (*)");
        jlNombre.setHorizontalAlignment(swingConstant);
        jtfNombre = new javax.swing.JTextField();
        jlApellido = new javax.swing.JLabel("Apellido (*)");
        jtfApellido = new javax.swing.JTextField();
        jlApellido.setHorizontalAlignment(swingConstant);
        jlFechaNacimiento = new javax.swing.JLabel("Fecha de nacimiento");
        jlFechaNacimiento.setHorizontalAlignment(swingConstant);
        dccFechaNacimiento = new com.toedter.calendar.JDateChooser();
        dccFechaNacimiento.setDateFormatString("dd/MM/yyyy");
        jlCedulaIdentidad = new javax.swing.JLabel("Cedula de identidad (**)");
        jlCedulaIdentidad.setHorizontalAlignment(swingConstant);
        jftCedulaIdentidad = new javax.swing.JFormattedTextField();
        jlNacionalidad = new javax.swing.JLabel("Nacionalidad");
        jlNacionalidad.setHorizontalAlignment(swingConstant);
        jcbNacionalidad = new javax.swing.JComboBox();
        jlCiudad = new javax.swing.JLabel("Ciudad");
        jlCiudad.setHorizontalAlignment(swingConstant);
        jcbCiudad = new javax.swing.JComboBox();
        jlGenero = new javax.swing.JLabel("Género");
        jlGenero.setHorizontalAlignment(swingConstant);
        jcbGenero = new javax.swing.JComboBox();
        jlEstadoCivil = new javax.swing.JLabel("Estado civil");
        jlEstadoCivil.setHorizontalAlignment(swingConstant);
        jcbEstadoCivil = new javax.swing.JComboBox();

        jlTelefonoContacto = new javax.swing.JLabel("Telefono");
        jlTelefonoContacto.setHorizontalAlignment(swingConstant);
        jlCorreoElectronico = new javax.swing.JLabel("E-mail");
        jlCorreoElectronico.setHorizontalAlignment(swingConstant);
        jlDireccionContacto = new javax.swing.JLabel("Dirección");
        jlDireccionContacto.setHorizontalAlignment(swingConstant);
        jlObservacion = new javax.swing.JLabel("Observación");
        jlObservacion.setHorizontalAlignment(swingConstant);
        jtfTelefonoContacto = new javax.swing.JTextField();
        jtfCorreoElecContacto = new javax.swing.JTextField();
        jtfDireccionContacto = new javax.swing.JTextField();
        jtfObservacion = new javax.swing.JTextField();

        jbAceptar = new javax.swing.JButton("Aceptar");
        jbCancelar = new javax.swing.JButton("Cancelar");
        getContentPane().setLayout(new MigLayout());
        String width1 = "width :100:";
        String width2 = "width :270:";
        jpCenter = new JPanel(new MigLayout());
        jpSouth = new JPanel(new MigLayout("align center"));
        jpCenter.add(jlNombre, width1 + ", cell 0 0");
        jpCenter.add(jtfNombre, width2 + ", cell 1 0");

        jpCenter.add(jlApellido, width1 + ", cell 0 1");
        jpCenter.add(jtfApellido, width2 + ", cell 1 1");

        jpCenter.add(jlFechaNacimiento, width1 + ", cell 0 2");
        jpCenter.add(dccFechaNacimiento, width2 + ", cell 1 2");

        jpCenter.add(jlCedulaIdentidad, width1 + ", cell 0 3");
        jpCenter.add(jftCedulaIdentidad, width2 + ", cell 1 3");

        jpCenter.add(jlNacionalidad, width1 + ", cell 0 4");
        jpCenter.add(jcbNacionalidad, width2 + ", cell 1 4");

        jpCenter.add(jlCiudad, width1 + ", cell 0 5");
        jpCenter.add(jcbCiudad, width2 + ", cell 1 5");

        jpCenter.add(jlGenero, width1 + ", cell 0 6");
        jpCenter.add(jcbGenero, width2 + ", cell 1 6");

        jpCenter.add(jlEstadoCivil, width1 + ", cell 0 7");
        jpCenter.add(jcbEstadoCivil, width2 + ", cell 1 7");

        jpCenter.add(jlTelefonoContacto, width1 + ", cell 2 0");
        jpCenter.add(jtfTelefonoContacto, width2 + ",cell 3 0");

        jpCenter.add(jlDireccionContacto, width1 + ",cell 2 1");
        jpCenter.add(jtfDireccionContacto, width2 + ",cell 3 1");

        jpCenter.add(jlCorreoElectronico, width1 + ",cell 2 2");
        jpCenter.add(jtfCorreoElecContacto, width2 + ",cell 3 2");

        jpCenter.add(jlObservacion, width1 + ",cell 2 3");
        jpCenter.add(jtfObservacion, width2 + ",cell 3 3");

        jpSouth.add(jbAceptar);
        jpSouth.add(jbCancelar);
        getContentPane().add(jpCenter, "dock center");
        getContentPane().add(jpSouth, "dock south");

        Vector genero = DB_manager.obtenerGenero();
        for (int i = 0; i < genero.size(); i++) {
            this.jcbGenero.addItem(genero.get(i));
        }
        Vector pais = DB_manager.obtenerPais();
        for (int i = 0; i < pais.size(); i++) {
            this.jcbNacionalidad.addItem(pais.get(i));
        }
        Vector ciudad = DB_manager.obtenerCiudad();
        for (int i = 0; i < ciudad.size(); i++) {
            this.jcbCiudad.addItem(ciudad.get(i));
        }
        Vector estado_civil = DB_manager.obtenerEstadoCivil();
        for (int i = 0; i < estado_civil.size(); i++) {
            this.jcbEstadoCivil.addItem(estado_civil.get(i));
        }
    }

    private void agregarListeners() {
        jbAceptar.addActionListener(this);
        jbCancelar.addActionListener(this);
    }

    private void cerrar() {
        this.dispose();
        System.runFinalization();
    }

    private boolean validarDatos() {
        String nombre = this.jtfNombre.getText().trim();
        /*
         * VALIDAR Nombre
         */
        if (nombre.isEmpty()) {
            this.jtfNombre.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El campo Nombre esta vacío",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        } else if (nombre.length() > 30) {
            this.jtfNombre.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El campo Nombre supera 30 caracteres",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        String apellido = jtfApellido.getText().trim();
        /*
         * VALIDAR Apellido
         */
        if (apellido.isEmpty()) {
            this.jtfApellido.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El campo Apellido esta vacío",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        } else if (apellido.length() > 30) {
            this.jtfApellido.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El campo Nombre supera 30 caracteres",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }

        Date fechaNacimiento = null;
        if (dccFechaNacimiento.getDate() != null) {
            try {
                fechaNacimiento = dccFechaNacimiento.getDate();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error en el campo Fecha nacimiento, inserte una fecha válida",
                        "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                return false;
            }
        }
        Integer cedula = null;
        if (!jftCedulaIdentidad.getText().isEmpty()) {
            try {
                cedula = Integer.valueOf(jftCedulaIdentidad.getText());
                if (DB_manager.existCi(cedula)) {
                    this.jftCedulaIdentidad.setBackground(Color.red);
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Cédula en uso",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                    return false;
                }
            } catch (Exception e) {
                this.jftCedulaIdentidad.setBackground(Color.red);
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error en el campo Cédula, inserte solo números",
                        "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                return false;
            }
        }
        String telefono = jtfTelefonoContacto.getText().trim();
        if (telefono.length() > 30) {
            this.jtfTelefonoContacto.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El campo telefono supera 30 caracteres",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        } else if (telefono.isEmpty()) {
            telefono = null;
        }
        String direccion = jtfDireccionContacto.getText().trim();
        if (direccion.length() > 120) {
            this.jtfDireccionContacto.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El campo dirección supera 120 caracteres",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        } else if (direccion.isEmpty()) {
            direccion = null;
        }
        String email = jtfCorreoElecContacto.getText().trim();
        if (email.length() > 30) {
            this.jtfCorreoElecContacto.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El campo email supera 30 caracteres",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        } else if (email.isEmpty()) {
            email = null;
        }
        String observacion = jtfObservacion.getText().trim();
        if (observacion.length() > 120) {
            this.jtfObservacion.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El campo Observación supera 120 caracteres",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        } else if (observacion.isEmpty()) {
            observacion = null;
        }
        if (accion == CREAR_CLIENTE_CONTACTO || accion == MODIFICAR_CLIENTE_CONTACTO) {
            clie_contacto = new M_cliente_contacto();
            String nacionalidad = jcbNacionalidad.getSelectedItem().toString();
            String ciudad = jcbCiudad.getSelectedItem().toString();
            String genero = jcbGenero.getSelectedItem().toString();
            String estadoCivil = jcbEstadoCivil.getSelectedItem().toString();

            clie_contacto.setApellido(apellido);
            clie_contacto.setCedula(cedula);
            clie_contacto.setCiudad(ciudad);
            clie_contacto.setDireccion(direccion);
            clie_contacto.setEmail(email);
            clie_contacto.setEstado_civil(estadoCivil);
            clie_contacto.setFecha_nacimiento(fechaNacimiento);
            clie_contacto.setNombre(nombre);
            clie_contacto.setObservacion(observacion);
            clie_contacto.setPais(nacionalidad);
            clie_contacto.setSexo(genero);
            clie_contacto.setTelefono(telefono);
            clie_contacto.setId_ciudad(DB_manager.obtenerIdCiudad(ciudad));
            clie_contacto.setId_estado_civil(DB_manager.obtenerIdEstadoCivil(estadoCivil));
            clie_contacto.setId_pais(DB_manager.obtenerIdPais(nacionalidad));
            clie_contacto.setId_sexo(DB_manager.obtenerIdGenero(genero));
        } else if (accion == CREAR_PROVEEDOR_CONTACTO || accion == MODIFICAR_PROVEEDOR_CONTACTO) {
            prov_contacto = new M_contacto();
            String nacionalidad = jcbNacionalidad.getSelectedItem().toString();
            String ciudad = jcbCiudad.getSelectedItem().toString();
            String genero = jcbGenero.getSelectedItem().toString();
            String estadoCivil = jcbEstadoCivil.getSelectedItem().toString();
            prov_contacto.setApellido(apellido);
            prov_contacto.setCedula(cedula);
            prov_contacto.setCiudad(ciudad);
            prov_contacto.setDireccion(direccion);
            prov_contacto.setEmail(email);
            prov_contacto.setEstado_civil(estadoCivil);
            prov_contacto.setFecha_nacimiento(fechaNacimiento);
            prov_contacto.setNombre(nombre);
            prov_contacto.setObservacion(observacion);
            prov_contacto.setPais(nacionalidad);
            prov_contacto.setSexo(genero);
            prov_contacto.setTelefono(telefono);
            prov_contacto.setId_ciudad(DB_manager.obtenerIdCiudad(ciudad));
            prov_contacto.setId_estado_civil(DB_manager.obtenerIdEstadoCivil(estadoCivil));
            prov_contacto.setId_pais(DB_manager.obtenerIdPais(nacionalidad));
            prov_contacto.setId_sexo(DB_manager.obtenerIdGenero(genero));
        }
        return true;
    }

    private void crearContacto() {
        if (validarDatos()) {
            switch (accion) {
                case CREAR_CLIENTE_CONTACTO: {
                    this.crearCliente.recibirContacto(clie_contacto);
                    break;
                }
                case MODIFICAR_CLIENTE_CONTACTO: {
                    this.modificarCliente.recibirContacto(clie_contacto);
                    break;
                }
                case CREAR_PROVEEDOR_CONTACTO: {
                    this.crearProveedor.recibirContacto(prov_contacto);
                    break;
                }
                case MODIFICAR_PROVEEDOR_CONTACTO: {
                    this.modificarProveedor.recibirContacto(prov_contacto);
                    break;
                }
                default: {
                    cerrar();
                    break;
                }
            }
            cerrar();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbAceptar)) {
            crearContacto();
        } else if (e.getSource().equals(jbCancelar)) {
            cerrar();
        }
    }
}
