/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package empleado;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author RAMIRO-VM
 */
public class V_modificar_usuario extends javax.swing.JDialog {

    private javax.swing.JPanel jpNorth;
    private javax.swing.JScrollPane jspObservacion;
    public javax.swing.JButton jbAceptar, jbAgregarRol, jbCancelar, jbQuitarRol;
    public javax.swing.JComboBox jcbCiudad, jcbEstadoCivil, jcbGenero, jcbNacionalidad;
    public javax.swing.JLabel jlAlias, jlApellido, jlCedulaIdentidad, jlCiudad, jlEstadoCivil,
            jlFechaNacimiento, jlGenero, jlNacionalidad, jlNombre,
            jlNroCelular, jlNroTelefono, jlPassword, jlRepetirPassword, jlFechaIngreso, jlCorreoElectronico, jlDireccion;
    public javax.swing.JPanel jpDatosEmpresariales, jpSouth, jpDatosPersonales1, jpRol, jpDatosPersonales2;
    public javax.swing.JPasswordField jpassword1, jpassword2;
    public javax.swing.JTextArea jtaObservacion;
    public javax.swing.JFormattedTextField jftCedulaIdentidad;
    public javax.swing.JTextField jtfAlias, jtfApellido, jtfCorreoElectronico,
            jtfDireccion, jtfNombre, jtfNroCelular, jtfNroTelefono;
    public JDateChooser dccFechaNacimiento, dccFechaIngreso;
    public javax.swing.JTabbedPane jtpCenter;
    public JTable jtRolesSeleccionados, jtRolesDisponibles;

    public V_modificar_usuario(JFrame frame) {
        super(frame, true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modificar usuario");
        setAlwaysOnTop(false);
        setName("Modificar usuario");
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        initPanelDatosPersonales1();
        initPanelDatosPersonales2();
        initPanelDatosEmpresariales();
        initPaneRol();
        initPaneNorth();
        initPanelSouth();
        jtpCenter = new JTabbedPane();
        jtpCenter.add("Datos Generales", jpDatosPersonales1);
        jtpCenter.add("Datos Personales", jpDatosPersonales2);
        jtpCenter.add("Datos Empresariales", jpDatosEmpresariales);
        jtpCenter.add("Roles", jpRol);
        getContentPane().add(jpNorth, java.awt.BorderLayout.NORTH);
        getContentPane().add(jtpCenter, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void initPanelDatosPersonales1() {
        //Panel
        jpDatosPersonales1 = new javax.swing.JPanel();
        //jpDatosPersonales1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos personales 1"));
        jpDatosPersonales1.setLayout(new java.awt.GridLayout(7, 4));
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
        dccFechaNacimiento = new JDateChooser();
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

        jpDatosPersonales1.add(jlNombre);
        jpDatosPersonales1.add(jtfNombre);
        jpDatosPersonales1.add(jlApellido);
        jpDatosPersonales1.add(jtfApellido);
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(jlFechaNacimiento);
        jpDatosPersonales1.add(dccFechaNacimiento);
        jpDatosPersonales1.add(jlCedulaIdentidad);
        jpDatosPersonales1.add(jftCedulaIdentidad);
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(jlNacionalidad);
        jpDatosPersonales1.add(jcbNacionalidad);
        jpDatosPersonales1.add(jlCiudad);
        jpDatosPersonales1.add(jcbCiudad);
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(new Component() {
        });
        jpDatosPersonales1.add(jlGenero);
        jpDatosPersonales1.add(jcbGenero);
        jpDatosPersonales1.add(jlEstadoCivil);
        jpDatosPersonales1.add(jcbEstadoCivil);
    }

    private void initPanelDatosPersonales2() {
        //Panel
        //jpDatosPersonales2 = new javax.swing.JPanel(new java.awt.GridLayout(9, 2));
        jpDatosPersonales2 = new javax.swing.JPanel(new MigLayout());
        //int swingConstant = javax.swing.SwingConstants.CENTER;
        //Labels, Textfields & textarea
        jlNroTelefono = new javax.swing.JLabel("Número de telefono");
        //jlNroTelefono.setHorizontalAlignment(swingConstant);
        jtfNroTelefono = new javax.swing.JTextField();
        jlNroCelular = new javax.swing.JLabel("Número de celular");
        //jlNroCelular.setHorizontalAlignment(swingConstant);
        jtfNroCelular = new javax.swing.JTextField();
        jtaObservacion = new javax.swing.JTextArea();
        jspObservacion = new javax.swing.JScrollPane(jtaObservacion);
        jspObservacion.setBorder(javax.swing.BorderFactory.createTitledBorder("Observaciones"));
        jlDireccion = new javax.swing.JLabel("Dirección");
        //jlDireccion.setHorizontalAlignment(swingConstant);
        jtfDireccion = new javax.swing.JTextField();
        //Adding components into panel
        jpDatosPersonales2.add(jlDireccion);
        jpDatosPersonales2.add(jtfDireccion, "pushx, grow, wrap");
        /*jpDatosPersonales2.add(new Component() {
         });
         jpDatosPersonales2.add(new Component() {
         });*/
        jpDatosPersonales2.add(jlNroTelefono);
        jpDatosPersonales2.add(jtfNroTelefono, "pushx, grow, wrap");
        /*jpDatosPersonales2.add(new Component() {
         });
         jpDatosPersonales2.add(new Component() {
         });*/
        jpDatosPersonales2.add(jlNroCelular);
        jpDatosPersonales2.add(jtfNroCelular, "pushx, grow, wrap");
        /*jpDatosPersonales2.add(new Component() {
         });
         jpDatosPersonales2.add(new Component() {
         });*/
    }

    private void initPanelDatosEmpresariales() {
        //Panel
        jpDatosEmpresariales = new javax.swing.JPanel(new java.awt.GridLayout(8, 4));
        int swingConstant = javax.swing.SwingConstants.CENTER;
        //jpDatosEmpresariales.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos empresariales"));
        //Labels, textfields, buttons, combobox, list & passwordfields
        jlAlias = new javax.swing.JLabel();
        jlAlias.setHorizontalAlignment(swingConstant);
        jlAlias.setText("Alias (**)");
        jtfAlias = new javax.swing.JTextField();
        jlFechaIngreso = new javax.swing.JLabel();
        jlFechaIngreso.setHorizontalAlignment(swingConstant);
        jlFechaIngreso.setText("Fecha de ingreso");
        dccFechaIngreso = new JDateChooser();
        dccFechaIngreso.setDateFormatString("dd/MM/yyyy");
        jlPassword = new javax.swing.JLabel();
        jlPassword.setHorizontalAlignment(swingConstant);
        jlPassword.setText("Contraseña (*)");
        jpassword1 = new javax.swing.JPasswordField();
        jlRepetirPassword = new javax.swing.JLabel();
        jlRepetirPassword.setHorizontalAlignment(swingConstant);
        jlRepetirPassword.setText("Repetir contraseña (*)");
        jpassword2 = new javax.swing.JPasswordField();
        jlCorreoElectronico = new javax.swing.JLabel();
        jlCorreoElectronico.setHorizontalAlignment(swingConstant);
        jlCorreoElectronico.setText("Correo electrónico");
        jtfCorreoElectronico = new javax.swing.JTextField();
        jpDatosEmpresariales.add(jlAlias);
        jpDatosEmpresariales.add(jtfAlias);
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });

        jpDatosEmpresariales.add(jlFechaIngreso);
        jpDatosEmpresariales.add(dccFechaIngreso);
        jpDatosEmpresariales.add(jlPassword);
        jpDatosEmpresariales.add(jpassword1);
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });

        jpDatosEmpresariales.add(jlCorreoElectronico);
        jpDatosEmpresariales.add(jtfCorreoElectronico);
        jpDatosEmpresariales.add(jlRepetirPassword);
        jpDatosEmpresariales.add(jpassword2);
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });

        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });
        jpDatosEmpresariales.add(new Component() {
        });

    }

    private void initPaneNorth() {
        jpNorth = new javax.swing.JPanel(new MigLayout("center"));
        jpNorth.add(new JLabel("Los campos marcados con un asterisco(*) son obligatorios."),"wrap");
        jpNorth.add(new JLabel("Los campos marcados con dos asteriscos(**) son únicos."));
    }

    private void initPaneRol() {
        //Panel
        jpRol = new javax.swing.JPanel(new java.awt.GridLayout(1, 2));
        jbAgregarRol = new javax.swing.JButton("Agregar");
        jbQuitarRol = new javax.swing.JButton("Quitar");

        jtRolesSeleccionados = new JTable();
        jtRolesDisponibles = new JTable();
        JScrollPane jspRolesSeleccionados = new JScrollPane(jtRolesSeleccionados);
        jspRolesSeleccionados.setBorder(javax.swing.BorderFactory.createTitledBorder("Roles seleccionados(*)"));
        JScrollPane jspRolesDisponibles = new JScrollPane(jtRolesDisponibles);
        jspRolesDisponibles.setBorder(javax.swing.BorderFactory.createTitledBorder("Roles disponibles"));

        javax.swing.JPanel left = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JPanel right = new javax.swing.JPanel(new java.awt.BorderLayout());
        left.add(jspRolesSeleccionados,BorderLayout.CENTER);
        left.add(jbQuitarRol,BorderLayout.SOUTH);
        right.add(jspRolesDisponibles,BorderLayout.CENTER);
        right.add(jbAgregarRol,BorderLayout.SOUTH);

        jpRol.add(left);
        jpRol.add(right);
    }

    private void initPanelSouth() {
        jpSouth = new javax.swing.JPanel();
        jpSouth.setBorder(new javax.swing.border.EtchedBorder());
        jbAceptar = new javax.swing.JButton("Aceptar");
        jbCancelar = new javax.swing.JButton("Cancelar");
        jpSouth.add(jbAceptar);
        jpSouth.add(jbCancelar);
    }
}
