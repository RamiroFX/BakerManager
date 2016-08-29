/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Empleado;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.BorderLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Administrador
 */
public class V_gestion_usuario extends JInternalFrame {

    private javax.swing.JPanel jpDatosPersonalesVarios2;
    private javax.swing.JPanel jpDatosPersonalesVarios1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jspObservacion;
    private javax.swing.JTabbedPane jTabbedPane;
    public javax.swing.JButton jbModificarUsuario;
    public javax.swing.JButton jbEliminarUsuario;
    public javax.swing.JButton jbCambiarPassword;
    public javax.swing.JButton jbUsuarioParametros;
    public javax.swing.JButton jbCrearUsuario;
    public javax.swing.JButton jbGestionRol;
    private javax.swing.JLabel jlAlias;
    private javax.swing.JLabel jlApellido;
    private javax.swing.JLabel jlCedulaIdentidad;
    private javax.swing.JLabel jlCiudad;
    private javax.swing.JLabel jlCorreoElectronico;
    private javax.swing.JLabel jlDireccion;
    private javax.swing.JLabel jlEstadoCivil;
    private javax.swing.JLabel jlFechaIngreso;
    private javax.swing.JLabel jlFechaNacimiento;
    private javax.swing.JLabel jlGenero;
    private javax.swing.JLabel jlNacionalidad;
    private javax.swing.JLabel jlNombre;
    private javax.swing.JLabel jlNroCelular;
    private javax.swing.JLabel jlRol;
    private javax.swing.JLabel jlTelefono;
    private javax.swing.JPanel jpCenter;
    private javax.swing.JPanel jpDatos;
    private javax.swing.JPanel jpDatosEmpresariales;
    private javax.swing.JPanel jpDatosPersonales1;
    private javax.swing.JPanel jpDatosPersonales2;
    private javax.swing.JPanel jpSouth;
    public javax.swing.JTable jtUsuario;
    public javax.swing.JTextArea jtaObservacion;
    public javax.swing.JTextField jtfAlias;
    public javax.swing.JTextField jtfApellido;
    public javax.swing.JFormattedTextField jftCedulaIdentidad;
    public javax.swing.JTextField jtfCiudad;
    public javax.swing.JTextField jtfCorreoElectronico;
    public javax.swing.JTextField jtfDireccion;
    public javax.swing.JTextField jtfEstadoCivil;
    public javax.swing.JTextField jtfFechaIngreso;
    public javax.swing.JTextField jtfFechaNacimiento;
    public javax.swing.JTextField jtfGenero;
    public javax.swing.JTextField jtfNacionalidad;
    public javax.swing.JTextField jtfNombre;
    public javax.swing.JTextField jtfNroCelular;
    public javax.swing.JTextField jtfNroTelefono;
    public javax.swing.JComboBox jcbRol;
    JPanel jpNorth;
    public JTextField jtfBuscar;
    public JCheckBox jckbNombreApellido, jckbCedula;
    public JRadioButton jrbExclusivo, jrbInclusivo;

    public V_gestion_usuario() {
        setClosable(true);
        setForeground(java.awt.Color.white);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestión de Usuarios");
        setName("Gestión usuario'");
        setPreferredSize(new java.awt.Dimension(800, 600));
        initComponents();
    }

    private void initComponents() {
        initFilter();
        jpCenter = new javax.swing.JPanel();
        jpCenter.setLayout(new java.awt.GridLayout(1, 0));
        jtUsuario = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setViewportView(jtUsuario);
        jTabbedPane = new javax.swing.JTabbedPane();
        jlNombre = new javax.swing.JLabel();
        jtfNombre = new javax.swing.JTextField();
        jtfNombre.setEditable(false);
        jlApellido = new javax.swing.JLabel();
        jtfApellido = new javax.swing.JTextField();
        jtfApellido.setEditable(false);
        jlFechaNacimiento = new javax.swing.JLabel();
        jtfFechaNacimiento = new javax.swing.JTextField();
        jtfFechaNacimiento.setEditable(false);
        jlCedulaIdentidad = new javax.swing.JLabel();
        jftCedulaIdentidad = new javax.swing.JFormattedTextField();
        jlNacionalidad = new javax.swing.JLabel();
        jtfNacionalidad = new javax.swing.JTextField();
        jtfNacionalidad.setEditable(false);
        jlCiudad = new javax.swing.JLabel();
        jtfCiudad = new javax.swing.JTextField();
        jtfCiudad.setEditable(false);
        jlEstadoCivil = new javax.swing.JLabel();
        jtfEstadoCivil = new javax.swing.JTextField();
        jtfEstadoCivil.setEditable(false);
        jlGenero = new javax.swing.JLabel();
        jtfGenero = new javax.swing.JTextField();
        jtfGenero.setEditable(false);
        jpDatosPersonales2 = new javax.swing.JPanel();
        jlDireccion = new javax.swing.JLabel();
        jtfDireccion = new javax.swing.JTextField();
        jtfDireccion.setEditable(false);
        jlTelefono = new javax.swing.JLabel();
        jtfNroTelefono = new javax.swing.JTextField();
        jtfNroTelefono.setEditable(false);
        jlNroCelular = new javax.swing.JLabel();
        jtfNroCelular = new javax.swing.JTextField();
        jtfNroCelular.setEditable(false);
        jpDatosPersonalesVarios2 = new javax.swing.JPanel();
        jspObservacion = new javax.swing.JScrollPane();
        jtaObservacion = new javax.swing.JTextArea();
        jlAlias = new javax.swing.JLabel();
        jtfAlias = new javax.swing.JTextField();
        jtfAlias.setEditable(false);
        jlFechaIngreso = new javax.swing.JLabel();
        jtfFechaIngreso = new javax.swing.JTextField();
        jtfFechaIngreso.setEditable(false);
        jlCorreoElectronico = new javax.swing.JLabel();
        jtfCorreoElectronico = new javax.swing.JTextField();
        jtfCorreoElectronico.setEditable(false);
        jlRol = new javax.swing.JLabel();
        jcbRol = new javax.swing.JComboBox();
        jbCrearUsuario = new javax.swing.JButton();
        jbModificarUsuario = new javax.swing.JButton();
        jbEliminarUsuario = new javax.swing.JButton("Eliminar usuario");
        jbCambiarPassword = new javax.swing.JButton("Cambiar contraseña");
        jbCambiarPassword.setName("modificar contrasenha");
        jbUsuarioParametros = new javax.swing.JButton("Parametros");
        jbUsuarioParametros.setName("parametros empleado");
        jbCrearUsuario.setName("crear empleado");

        Insets insets = new Insets(10, 10, 10, 10);
        jbCrearUsuario.setMargin(insets);
        jbModificarUsuario.setMargin(insets);
        jbEliminarUsuario.setMargin(insets);
        jbCambiarPassword.setMargin(insets);
        jbUsuarioParametros.setMargin(insets);

        jbModificarUsuario.setName("modificar empleado");
        jbEliminarUsuario.setName("borrar empleado");

        jpDatosPersonales1 = new javax.swing.JPanel();
        jpDatosPersonales1.setLayout(new java.awt.BorderLayout());


        jpDatos = new javax.swing.JPanel();
        jlNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlNombre.setText("Nombre");
        jlApellido.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlApellido.setText("Apellido");
        jlFechaNacimiento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFechaNacimiento.setText("Fecha de nacimiento");
        jlCedulaIdentidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlCedulaIdentidad.setText("Cedula de identidad");
        jlNacionalidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlNacionalidad.setText("Nacionalidad");
        jlCiudad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlCiudad.setText("Ciudad");
        jlEstadoCivil.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlEstadoCivil.setText("Estado civil");
        jlGenero.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlGenero.setText("Género");
        jpDatos.setLayout(new java.awt.GridLayout(8, 2));
        jpDatos.add(jlNombre);
        jpDatos.add(jtfNombre);
        jpDatos.add(jlApellido);
        jpDatos.add(jtfApellido);
        jpDatos.add(jlFechaNacimiento);
        jpDatos.add(jtfFechaNacimiento);
        jpDatos.add(jlCedulaIdentidad);
        jpDatos.add(jftCedulaIdentidad);
        jpDatos.add(jlNacionalidad);
        jpDatos.add(jtfNacionalidad);
        jpDatos.add(jlCiudad);
        jpDatos.add(jtfCiudad);
        jpDatos.add(jlEstadoCivil);
        jpDatos.add(jtfEstadoCivil);
        jpDatos.add(jlGenero);
        jpDatos.add(jtfGenero);

        jpDatosPersonales1.add(jpDatos, java.awt.BorderLayout.CENTER);


        jpDatosPersonales2.setLayout(new java.awt.BorderLayout());

        jpDatosPersonalesVarios1 = new javax.swing.JPanel(new java.awt.GridLayout(3, 2));
        jlDireccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDireccion.setText("Direccion");
        jlNroCelular.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlNroCelular.setText("Celular");
        jlTelefono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTelefono.setText("Telefono");
        jpDatosPersonalesVarios1.add(jlDireccion);
        jpDatosPersonalesVarios1.add(jtfDireccion);
        jpDatosPersonalesVarios1.add(jlTelefono);
        jpDatosPersonalesVarios1.add(jtfNroTelefono);
        jpDatosPersonalesVarios1.add(jlNroCelular);
        jpDatosPersonalesVarios1.add(jtfNroCelular);

        jpDatosPersonales2.add(jpDatosPersonalesVarios1, java.awt.BorderLayout.PAGE_START);

        jpDatosPersonalesVarios2.setBorder(javax.swing.BorderFactory.createTitledBorder("Observacion"));
        jpDatosPersonalesVarios2.setLayout(new java.awt.BorderLayout());

        jtaObservacion.setColumns(20);
        jtaObservacion.setRows(5);
        jspObservacion.setViewportView(jtaObservacion);

        jpDatosPersonalesVarios2.add(jspObservacion, java.awt.BorderLayout.CENTER);

        jpDatosPersonales2.add(jpDatosPersonalesVarios2, java.awt.BorderLayout.CENTER);


        jpDatosEmpresariales = new javax.swing.JPanel();
        jpDatosEmpresariales.setLayout(new java.awt.GridLayout(12, 2));

        jlAlias.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAlias.setText("Alias");
        jlFechaIngreso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFechaIngreso.setText("Fecha de Ingreso");
        jlCorreoElectronico.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlCorreoElectronico.setText("Correo electronico");
        jlRol.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlRol.setText("Roles");
        jpDatosEmpresariales.add(jlAlias);
        jpDatosEmpresariales.add(jtfAlias);
        jpDatosEmpresariales.add(jlFechaIngreso);
        jpDatosEmpresariales.add(jtfFechaIngreso);
        jpDatosEmpresariales.add(jlCorreoElectronico);
        jpDatosEmpresariales.add(jtfCorreoElectronico);
        jpDatosEmpresariales.add(jlRol);
        jpDatosEmpresariales.add(jcbRol);
        jpDatosEmpresariales.add(new JLabel());
        jpDatosEmpresariales.add(new JLabel());
        jpDatosEmpresariales.add(new JLabel());
        jpDatosEmpresariales.add(new JLabel());
        jpDatosEmpresariales.add(new JLabel());
        jpDatosEmpresariales.add(new JLabel());
        jTabbedPane.addTab("Datos personales 1", jpDatosPersonales1);
        jTabbedPane.addTab("Datos personales 2", jpDatosPersonales2);
        jTabbedPane.addTab("Datos corporativos", jpDatosEmpresariales);

        JPanel jpaux = new JPanel(new BorderLayout());
        jpaux.add(jpNorth, BorderLayout.NORTH);
        jpaux.add(jScrollPane1, BorderLayout.CENTER);

        jpCenter.add(jpaux);
        jpCenter.add(jTabbedPane);

        //JP SOUTH
        jpSouth = new javax.swing.JPanel();
        jpSouth.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbCrearUsuario.setText("Crear usuario");
        jbModificarUsuario.setText("Actualizar usuario");
        jbGestionRol = new JButton("Gestión de roles");
        jbGestionRol.setName("roles");
        jbGestionRol.setMargin(insets);
        jpSouth.add(jbCrearUsuario);
        jpSouth.add(jbModificarUsuario);
        jpSouth.add(jbEliminarUsuario);
        jpSouth.add(jbCambiarPassword);
        jpSouth.add(jbUsuarioParametros);
        jpSouth.add(jbGestionRol);

        //ADDING INTO CONTAINER
        getContentPane().add(jpCenter, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);

    }

    private void initFilter() {
        jtfBuscar = new JTextField();
        jtfBuscar.setName("buscar empleado");
        jckbNombreApellido = new JCheckBox("Nombre/Apellido");
        jckbNombreApellido.setSelected(true);
        jckbCedula = new JCheckBox("Cedula");
        jckbCedula.setSelected(true);
        jrbExclusivo = new javax.swing.JRadioButton("Exclusivo", true);
        jrbInclusivo = new javax.swing.JRadioButton("Inclusivo");
        javax.swing.ButtonGroup bg1 = new javax.swing.ButtonGroup();
        bg1.add(jrbExclusivo);
        bg1.add(jrbInclusivo);
        jpNorth = new JPanel(new MigLayout("align center"));
        String width = "width :100:";
        //C F
        jpNorth.add(jtfBuscar, "width :300:, cell 0 0");
        jpNorth.add(jckbNombreApellido, width + ", cell 1 0");
        jpNorth.add(jckbCedula, width + ", cell 1 1");
        jpNorth.add(jrbExclusivo, width + ", cell 0 1, span");
        jpNorth.add(jrbInclusivo, width + ", cell 0 1");
    }
}
