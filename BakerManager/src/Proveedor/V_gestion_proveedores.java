/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import java.awt.BorderLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
class V_gestion_proveedores extends JInternalFrame {

    private javax.swing.JScrollPane jspProveedor;
    private javax.swing.JTabbedPane jTabbedPane;
    public javax.swing.JButton jbModificarProveedor;
    public javax.swing.JButton jbCrearProveedor;
    //PESTAÑA 1    
    private javax.swing.JLabel jlEntidad;
    public javax.swing.JTextField jtfEntidad;
    private javax.swing.JLabel jlNombre;
    public javax.swing.JTextField jtfNombre;
    private javax.swing.JLabel jlRUC;
    public javax.swing.JTextField jtfRUC;
    private javax.swing.JLabel jlDescripcion;
    public javax.swing.JTextField jftDescripcion;
    private javax.swing.JLabel jlDireccion;
    public javax.swing.JTextField jtfDireccion;
    private javax.swing.JLabel jlEmail;
    public javax.swing.JTextField jtfEmail;
    private javax.swing.JLabel jlPagWeb;
    public javax.swing.JTextField jtfPagWeb;
    private javax.swing.JLabel jlObservacion;
    public javax.swing.JTextField jtfObservacion;
    private javax.swing.JScrollPane jspTelefono;
    public javax.swing.JTable jtTelefono;
    private javax.swing.JLabel jlTelefono;
    //PESTAÑA 2
    private javax.swing.JScrollPane jspSucursal;
    public javax.swing.JTable jtSucursal;
    //PESTAÑA 3
    private javax.swing.JScrollPane jspContacto;
    public javax.swing.JTable jtContacto;
    JPanel jpNorth;
    public JTextField jtfBuscar;
    public JCheckBox jckbEntidad, jckbRUC;
    public JRadioButton jrbExclusivo, jrbInclusivo;
    public javax.swing.JLabel jlNombreContacto, jlApellidoContacto, jlCedulaIdentidad, jlCiudad, jlEstadoCivil,
            jlFechaNacimiento, jlGenero, jlNacionalidad, jlObservacionContacto;
    public javax.swing.JTextField jtfNombreContacto, jtfApellidoContacto;
    public javax.swing.JTextField jtfCiudad, jtfEstadoCivil, jtfGenero, jtfNacionalidad;
    public javax.swing.JFormattedTextField jftFechaNacimiento;
    public javax.swing.JLabel jlTelefonoContacto, jlCorreoElectronicoContacto, jlDireccionContacto;
    public javax.swing.JTextField jtfTelefonoContacto, jtfCorreoElecContacto, jtfDireccionContacto, jtfObservacionContacto;
    public javax.swing.JFormattedTextField jftCedulaIdentidad;
    private javax.swing.JPanel jpDatosContactoSouth;
    private javax.swing.JPanel jpCenter;
    private javax.swing.JPanel jpDatosEmp;
    private javax.swing.JPanel jpDatosContacto;
    private javax.swing.JPanel jpDatosEmpresariales;
    private javax.swing.JPanel jpDatosSucursal;
    private javax.swing.JPanel jpSouth;
    public javax.swing.JTable jtProveedor;

    public V_gestion_proveedores() {
        setClosable(true);
        setForeground(java.awt.Color.white);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestión de proveedores ");
        setName("jifGestionProveedor");
        setPreferredSize(new java.awt.Dimension(800, 600));
        initComponents();
    }

    private void initComponents() {
        initFilter();
        inicializarVariablesContacto();
        jpCenter = new javax.swing.JPanel();
        jpCenter.setLayout(new java.awt.GridLayout(1, 0));
        jtProveedor = new javax.swing.JTable();
        jspProveedor = new javax.swing.JScrollPane();
        jspProveedor.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jspProveedor.setViewportView(jtProveedor);
        jTabbedPane = new javax.swing.JTabbedPane();
        jlNombre = new javax.swing.JLabel();
        jtfNombre = new javax.swing.JTextField();
        jtfNombre.setEditable(false);
        jlEntidad = new javax.swing.JLabel();
        jtfEntidad = new javax.swing.JTextField();
        jtfEntidad.setEditable(false);
        jlRUC = new javax.swing.JLabel();
        jtfRUC = new javax.swing.JTextField();
        jtfRUC.setEditable(false);
        jlDescripcion = new javax.swing.JLabel();
        jftDescripcion = new javax.swing.JTextField();
        jftDescripcion.setEditable(false);
        jlDireccion = new javax.swing.JLabel();
        jlDireccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDireccion.setText("Direccion");
        jtfDireccion = new javax.swing.JTextField();
        jtfDireccion.setEditable(false);
        jlEmail = new javax.swing.JLabel();
        jlEmail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlEmail.setText("E-mail");
        jtfEmail = new javax.swing.JTextField();
        jtfEmail.setEditable(false);
        jlPagWeb = new javax.swing.JLabel();
        jlPagWeb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPagWeb.setText("Pág. web");
        jtfPagWeb = new javax.swing.JTextField();
        jtfPagWeb.setEditable(false);
        jlObservacion = new javax.swing.JLabel();
        jlObservacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlObservacion.setText("Notas");
        jtfObservacion = new javax.swing.JTextField();
        jtfObservacion.setEditable(false);

        jlTelefono = new javax.swing.JLabel("Telefono");
        jtTelefono = new JTable();
        jspTelefono = new JScrollPane(jtTelefono);

        Insets inset = new Insets(10, 10, 10, 10);
        jbCrearProveedor = new javax.swing.JButton("Crear proveedor");
        jbCrearProveedor.setName("crear proveedor");
        jbCrearProveedor.setMargin(inset);
        jbModificarProveedor = new javax.swing.JButton("Modificar proveedor");
        jbModificarProveedor.setName("Modificar proveedor");
        jbModificarProveedor.setMargin(inset);

        jpDatosEmp = new javax.swing.JPanel(new MigLayout());
        jlNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlNombre.setText("Nombre");
        jlEntidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlEntidad.setText("Entidad");
        jlRUC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlRUC.setText("R.U.C.");
        jlDescripcion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDescripcion.setText("Descripción");
        jpDatosEmp.add(jlEntidad);
        jpDatosEmp.add(jtfEntidad, "growx, wrap");
        jpDatosEmp.add(jlNombre);
        jpDatosEmp.add(jtfNombre, "growx, wrap");
        jpDatosEmp.add(jlRUC);
        jpDatosEmp.add(jtfRUC, "growx, wrap");
        jpDatosEmp.add(jlDescripcion);
        jpDatosEmp.add(jftDescripcion, "growx, wrap");
        jpDatosEmp.add(jlDireccion);
        jpDatosEmp.add(jtfDireccion, "growx, wrap");
        jpDatosEmp.add(jlEmail);
        jpDatosEmp.add(jtfEmail, "growx, wrap");
        jpDatosEmp.add(jlPagWeb);
        jpDatosEmp.add(jtfPagWeb, "growx, wrap");
        jpDatosEmp.add(jlObservacion);
        jpDatosEmp.add(jtfObservacion, "growx, wrap");
        jpDatosEmp.add(jlTelefono);
        jpDatosEmp.add(jspTelefono);

        jpDatosEmpresariales = new javax.swing.JPanel();
        jpDatosEmpresariales.setLayout(new java.awt.BorderLayout());
        jpDatosEmpresariales.add(jpDatosEmp, java.awt.BorderLayout.CENTER);


        jpDatosSucursal = new javax.swing.JPanel(new java.awt.BorderLayout());
        jtSucursal = new JTable();
        jspSucursal = new JScrollPane(jtSucursal);
        jpDatosSucursal.add(jspSucursal, java.awt.BorderLayout.CENTER);

        jpDatosContacto = new javax.swing.JPanel(new java.awt.BorderLayout());
        jtContacto = new JTable();
        jspContacto = new JScrollPane(jtContacto);
        jpDatosContacto.add(jspContacto, java.awt.BorderLayout.CENTER);
        jpDatosContacto.add(jpDatosContactoSouth, java.awt.BorderLayout.SOUTH);

        jTabbedPane.addTab("Datos empresariales", jpDatosEmpresariales);
        jTabbedPane.addTab("Datos de sucursales", jpDatosSucursal);
        jTabbedPane.addTab("Datos de contactos", jpDatosContacto);

        JPanel jpaux = new JPanel(new BorderLayout());
        jpaux.add(jpNorth, BorderLayout.NORTH);
        jpaux.add(jspProveedor, BorderLayout.CENTER);
        jpCenter.add(jpaux);
        jpCenter.add(jTabbedPane);

        //JP SOUTH
        jpSouth = new javax.swing.JPanel();
        jpSouth.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpSouth.add(jbCrearProveedor);
        jpSouth.add(jbModificarProveedor);

        //ADDING INTO CONTAINER
        getContentPane().add(jpCenter, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);
    }

    private void initFilter() {
        jtfBuscar = new JTextField();
        jtfBuscar.setName("buscar proveedor");
        jckbEntidad = new JCheckBox("Entidad/Nombre");
        jckbEntidad.setSelected(true);
        jckbRUC = new JCheckBox("R.U.C");
        jckbRUC.setSelected(true);
        jrbExclusivo = new javax.swing.JRadioButton("Exclusivo", true);
        jrbInclusivo = new javax.swing.JRadioButton("Inclusivo");
        javax.swing.ButtonGroup bg1 = new javax.swing.ButtonGroup();
        bg1.add(jrbExclusivo);
        bg1.add(jrbInclusivo);
        jpNorth = new JPanel(new MigLayout("align center"));
        String width = "width :100:";
        //C F
        jpNorth.add(jtfBuscar, "width :300:, cell 0 0");
        jpNorth.add(jckbEntidad, width + ", cell 1 0");
        jpNorth.add(jckbRUC, width + ", cell 1 1");
        jpNorth.add(jrbExclusivo, width + ", cell 0 1, span");
        jpNorth.add(jrbInclusivo, width + ", cell 0 1");
    }

    private void inicializarVariablesContacto() {
        int swingConstant = javax.swing.SwingConstants.CENTER;
        //Labels, textfields, combobox
        jlNombreContacto = new javax.swing.JLabel("Nombre (*)");
        jlNombreContacto.setHorizontalAlignment(swingConstant);
        jtfNombreContacto = new javax.swing.JTextField();
        jlApellidoContacto = new javax.swing.JLabel("Apellido (*)");
        jtfApellidoContacto = new javax.swing.JTextField();
        jlApellidoContacto.setHorizontalAlignment(swingConstant);
        jlFechaNacimiento = new javax.swing.JLabel("Fecha de nacimiento");
        jlFechaNacimiento.setHorizontalAlignment(swingConstant);
        jftFechaNacimiento = new javax.swing.JFormattedTextField();
        //dccFechaNacimiento.setDateFormatString("dd/MM/yyyy");
        jlCedulaIdentidad = new javax.swing.JLabel("Cedula de identidad (*)");
        jlCedulaIdentidad.setHorizontalAlignment(swingConstant);
        jftCedulaIdentidad = new javax.swing.JFormattedTextField();
        jlNacionalidad = new javax.swing.JLabel("Nacionalidad");
        jlNacionalidad.setHorizontalAlignment(swingConstant);
        jtfNacionalidad = new javax.swing.JTextField();
        jlCiudad = new javax.swing.JLabel("Ciudad");
        jlCiudad.setHorizontalAlignment(swingConstant);
        jtfCiudad = new javax.swing.JTextField();
        jlGenero = new javax.swing.JLabel("Género");
        jlGenero.setHorizontalAlignment(swingConstant);
        jtfGenero = new javax.swing.JTextField();
        jlEstadoCivil = new javax.swing.JLabel("Estado civil");
        jlEstadoCivil.setHorizontalAlignment(swingConstant);
        jtfEstadoCivil = new javax.swing.JTextField();

        jlTelefonoContacto = new javax.swing.JLabel("Telefono");
        jlTelefonoContacto.setHorizontalAlignment(swingConstant);
        jlCorreoElectronicoContacto = new javax.swing.JLabel("E-mail");
        jlCorreoElectronicoContacto.setHorizontalAlignment(swingConstant);
        jlDireccionContacto = new javax.swing.JLabel("Dirección");
        jlDireccionContacto.setHorizontalAlignment(swingConstant);
        jlObservacionContacto = new javax.swing.JLabel("Observación");
        jlObservacionContacto.setHorizontalAlignment(swingConstant);
        jtfTelefonoContacto = new javax.swing.JTextField();
        jtfCorreoElecContacto = new javax.swing.JTextField();
        jtfDireccionContacto = new javax.swing.JTextField();
        jtfObservacionContacto = new javax.swing.JTextField();

        //getContentPane().setLayout(new MigLayout());
        String width1 = "width :100:";
        String width2 = "width :270:";
        jpDatosContactoSouth = new JPanel(new MigLayout());
        jpDatosContactoSouth.add(jlNombreContacto, width1 + ", cell 0 0");
        jpDatosContactoSouth.add(jtfNombreContacto, width2 + ", cell 1 0");

        jpDatosContactoSouth.add(jlApellidoContacto, width1 + ", cell 0 1");
        jpDatosContactoSouth.add(jtfApellidoContacto, width2 + ", cell 1 1");

        jpDatosContactoSouth.add(jlFechaNacimiento, width1 + ", cell 0 2");
        jpDatosContactoSouth.add(jftFechaNacimiento, width2 + ", cell 1 2");

        jpDatosContactoSouth.add(jlCedulaIdentidad, width1 + ", cell 0 3");
        jpDatosContactoSouth.add(jftCedulaIdentidad, width2 + ", cell 1 3");

        jpDatosContactoSouth.add(jlNacionalidad, width1 + ", cell 0 4");
        jpDatosContactoSouth.add(jtfNacionalidad, width2 + ", cell 1 4");

        jpDatosContactoSouth.add(jlCiudad, width1 + ", cell 0 5");
        jpDatosContactoSouth.add(jtfCiudad, width2 + ", cell 1 5");

        jpDatosContactoSouth.add(jlGenero, width1 + ", cell 0 6");
        jpDatosContactoSouth.add(jtfGenero, width2 + ", cell 1 6");

        jpDatosContactoSouth.add(jlEstadoCivil, width1 + ", cell 0 7");
        jpDatosContactoSouth.add(jtfEstadoCivil, width2 + ", cell 1 7");

        jpDatosContactoSouth.add(jlTelefonoContacto, width1 + ", cell 2 0");
        jpDatosContactoSouth.add(jtfTelefonoContacto, width2 + ",cell 3 0");

        jpDatosContactoSouth.add(jlDireccionContacto, width1 + ",cell 2 1");
        jpDatosContactoSouth.add(jtfDireccionContacto, width2 + ",cell 3 1");

        jpDatosContactoSouth.add(jlCorreoElectronicoContacto, width1 + ",cell 2 2");
        jpDatosContactoSouth.add(jtfCorreoElecContacto, width2 + ",cell 3 2");

        jpDatosContactoSouth.add(jlObservacionContacto, width1 + ",cell 2 3");
        jpDatosContactoSouth.add(jtfObservacionContacto, width2 + ",cell 3 3");
    }
}
