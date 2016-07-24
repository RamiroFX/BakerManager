/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_gestion_cliente extends JInternalFrame {

    private javax.swing.JScrollPane jspClientes;
    private javax.swing.JTabbedPane jTabbedPane;
    public javax.swing.JButton jbModificarCliente;
    public javax.swing.JButton jbCrearCliente;
    //PESTAÑA 1    
    private javax.swing.JPanel jpFoto;
    public javax.swing.JLabel jlFoto;
    private javax.swing.JLabel jlEntidad;
    public javax.swing.JTextField jtfEntidad;
    private javax.swing.JLabel jlNombre;
    public javax.swing.JTextField jtfNombre;
    private javax.swing.JLabel jlRUC;
    public javax.swing.JTextField jtfRUC;
    private javax.swing.JLabel jlObservacion;
    public javax.swing.JTextField jtfObservacion;
    private javax.swing.JLabel jlDireccion;
    public javax.swing.JTextField jtfDireccion, jtfTipoCliente, jtfCategoriaCliente,
            jtfPaginaWebCliente, jtfEmailCliente;
    private javax.swing.JScrollPane jspTelefono;
    public javax.swing.JTable jtTelefono;
    private javax.swing.JLabel jlTelefono, jlTipoCliente, jlCategoriaCliente, jlPaginaWebCliente, jlEmailCliente;
    //PESTAÑA 2
    private javax.swing.JScrollPane jspSucursal;
    public javax.swing.JTable jtSucursal;
    //PESTAÑA 3
    private javax.swing.JScrollPane jspContacto;
    public javax.swing.JTable jtContacto;
    //INCICIO VARIABLES CONTACTO
    public javax.swing.JLabel jlNombreContacto, jlApellidoContacto, jlCedulaIdentidad, jlCiudad, jlEstadoCivil,
            jlFechaNacimiento, jlGenero, jlNacionalidad, jlObservacionContacto;
    public javax.swing.JTextField jtfNombreContacto, jtfApellidoContacto;
    public javax.swing.JTextField jtfCiudad, jtfEstadoCivil, jtfGenero, jtfNacionalidad;
    public javax.swing.JFormattedTextField jftFechaNacimiento;
    public javax.swing.JLabel jlTelefonoContacto, jlCorreoElectronicoContacto, jlDireccionContacto;
    public javax.swing.JTextField jtfTelefonoContacto, jtfCorreoElecContacto, jtfDireccionContacto, jtfObservacionContacto;
    public javax.swing.JFormattedTextField jftCedulaIdentidad;
    private javax.swing.JPanel jpDatosContactoSouth;
    //FIN VARIABLES CONTACTO
    private javax.swing.JSplitPane jpCenter;
    private javax.swing.JPanel jpDatosEmp;
    private javax.swing.JPanel jpDatosContacto;
    private javax.swing.JPanel jpDatosEmpresariales;
    private javax.swing.JPanel jpDatosSucursal;
    private javax.swing.JPanel jpSouth;
    public javax.swing.JTable jtCliente;
    //INICIO VARIABLES FILTRO
    JPanel jpNorth;
    public JTextField jtfBuscar;
    public JCheckBox jckbEntidadNombre, jckbRuc;
    public JRadioButton jrbExclusivo, jrbInclusivo;
    
    public V_gestion_cliente() {
        setClosable(true);
        setForeground(java.awt.Color.white);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestión de clientes ");
        setName("jifGestionClientes");
        setSize(900, 700);
        initComponents();
    }

    private void initComponents() {
        initFilter();
        inicializarVariablesContacto();
        jtCliente = new javax.swing.JTable();
        jspClientes = new javax.swing.JScrollPane();
        jspClientes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jspClientes.setViewportView(jtCliente);
        jTabbedPane = new javax.swing.JTabbedPane();
        jpFoto = new javax.swing.JPanel();
        jlFoto = new javax.swing.JLabel();
        jlNombre = new javax.swing.JLabel();
        jlNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlNombre.setText("Nombre");
        jtfNombre = new javax.swing.JTextField();
        jlEntidad = new javax.swing.JLabel();
        jlEntidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlEntidad.setText("Entidad");
        jtfEntidad = new javax.swing.JTextField();
        jlRUC = new javax.swing.JLabel();
        jlRUC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlRUC.setText("R.U.C.");
        jtfRUC = new javax.swing.JTextField();
        jtfObservacion = new javax.swing.JTextField();
        jlObservacion = new javax.swing.JLabel();
        jlObservacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlObservacion.setText("Notas");
        jlDireccion = new javax.swing.JLabel();
        jlDireccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDireccion.setText("Direccion");
        jlTipoCliente = new javax.swing.JLabel();
        jlTipoCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTipoCliente.setText("Tipo");
        jlCategoriaCliente = new javax.swing.JLabel();
        jlCategoriaCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlCategoriaCliente.setText("Categoría");
        jlPaginaWebCliente = new javax.swing.JLabel();
        jlPaginaWebCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPaginaWebCliente.setText("Página web");
        jlEmailCliente = new javax.swing.JLabel();
        jlEmailCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlEmailCliente.setText("E-mail");
        jtfDireccion = new javax.swing.JTextField();
        jtfTipoCliente = new javax.swing.JTextField();
        jtfCategoriaCliente = new javax.swing.JTextField();
        jtfPaginaWebCliente = new javax.swing.JTextField();
        jtfEmailCliente = new javax.swing.JTextField();
        jlTelefono = new javax.swing.JLabel();
        jtTelefono = new JTable();
        jspTelefono = new JScrollPane(jtTelefono);
        jlTelefono.setText("Telefono");

        jbCrearCliente = new javax.swing.JButton();
        jbModificarCliente = new javax.swing.JButton();

        jpFoto.setBorder(javax.swing.BorderFactory.createTitledBorder("Imagen identificativa"));
        jlFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFoto.setPreferredSize(new java.awt.Dimension(200, 200));
        jpFoto.add(jlFoto);



        jpDatosEmp = new javax.swing.JPanel(new MigLayout());
        jpDatosEmp.add(jlEntidad);
        jpDatosEmp.add(jtfEntidad, "growx, wrap");
        jpDatosEmp.add(jlNombre);
        jpDatosEmp.add(jtfNombre, "growx, wrap");
        jpDatosEmp.add(jlRUC);
        jpDatosEmp.add(jtfRUC, "growx, wrap");
        jpDatosEmp.add(jlDireccion);
        jpDatosEmp.add(jtfDireccion, "growx, wrap");
        jpDatosEmp.add(jlTipoCliente);
        jpDatosEmp.add(jtfTipoCliente, "growx, wrap");
        jpDatosEmp.add(jlCategoriaCliente);
        jpDatosEmp.add(jtfCategoriaCliente, "growx, wrap");
        jpDatosEmp.add(jlPaginaWebCliente);
        jpDatosEmp.add(jtfPaginaWebCliente, "growx, wrap");
        jpDatosEmp.add(jlEmailCliente);
        jpDatosEmp.add(jtfEmailCliente, "growx, wrap");
        jpDatosEmp.add(jlObservacion);
        jpDatosEmp.add(jtfObservacion, "growx, wrap");
        jpDatosEmp.add(jlTelefono);
        jpDatosEmp.add(jspTelefono);

        jpDatosEmpresariales = new javax.swing.JPanel();
        jpDatosEmpresariales.setLayout(new java.awt.BorderLayout());
        jpDatosEmpresariales.add(jpFoto, java.awt.BorderLayout.NORTH);
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
        jpaux.add(jspClientes, BorderLayout.CENTER);
        
        jpCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpaux, jTabbedPane);
        jpCenter.setDividerLocation(this.getWidth() / 2);
        jpCenter.setOneTouchExpandable(true);
        //JP SOUTH
        jpSouth = new javax.swing.JPanel();
        jpSouth.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbCrearCliente.setText("Crear cliente");
        jbModificarCliente.setText("Modificar cliente");
        jpSouth.add(jbCrearCliente);
        jpSouth.add(jbModificarCliente);
        //ADDING INTO CONTAINER
        getContentPane().add(jpCenter, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);

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

    private void initFilter() {
        jtfBuscar = new JTextField();
        jckbEntidadNombre = new JCheckBox("Entidad/Nombre");
        jckbEntidadNombre.setSelected(true);
        jckbRuc = new JCheckBox("R.U.C.");
        jckbRuc.setSelected(true);
        jrbExclusivo = new javax.swing.JRadioButton("Exclusivo", true);
        jrbInclusivo = new javax.swing.JRadioButton("Inclusivo");
        javax.swing.ButtonGroup bg1 = new javax.swing.ButtonGroup();
        bg1.add(jrbExclusivo);
        bg1.add(jrbInclusivo);
        jpNorth = new JPanel(new MigLayout("align center"));
        jpNorth.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        String width = "width :100:";
        //C F
        jpNorth.add(jtfBuscar, "width :300:, cell 0 0");
        jpNorth.add(jckbEntidadNombre, width + ", cell 1 0");
        jpNorth.add(jckbRuc, width + ", cell 1 1");
        jpNorth.add(jrbExclusivo, width + ", cell 0 1, span");
        jpNorth.add(jrbInclusivo, width + ", cell 0 1");
    }
}
