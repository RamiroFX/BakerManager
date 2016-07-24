/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

/**
 *
 * @author Usuario
 */
public class V_crear_proveedor extends javax.swing.JDialog {

    public javax.swing.JPanel jpImagen, jpNorth, jpSouth, jpDatosGenerales,
            jpDatosSucursal;
    public javax.swing.JButton jbAceptar, jbCambiarImagen, jbCancelar,
            jbAgregarTelefono, jbQuitarTelefono, jbModTelefono, jbAgregarSucursal, jbQuitarSucursal, jbModSucursal;
    public javax.swing.JTextField jtfRazonSocial, jtfNombreFantasia, jtfRUC, jtfRUC_ID,
            jtfDescripcion, jtfDireccion, jtfemail, jtfPagWeb, jtfTelefono;
    public javax.swing.JLabel jlRazonSocial, jlNombreFantasia, jlRUC, jlRUC_ID,
            jlDescripcion, jlDireccion, jlemail, jlPagWeb, jlTelefono, jlNota;
    //INICIO VARIABLES DE CONTACTO
    public javax.swing.JPanel jpDatosContacto;
    public javax.swing.JButton jbAgregarContacto, jbQuitarContacto, jbModContacto;
    public javax.swing.JTable jtContacto;
    public javax.swing.JScrollPane jspContacto;
    //FIN VARIABLES DE CONTACTO
    public javax.swing.JTabbedPane jtpCenter;
    public javax.swing.JTextArea jtaAyuda, jtaNota;
    public javax.swing.JScrollPane jspAyuda;
    public javax.swing.JLabel jlImagen;
    public javax.swing.JTable jtTelefono, jtSucursal;
    public javax.swing.JScrollPane jspTelefono, jspNota, jspSucursal;

    public V_crear_proveedor(javax.swing.JFrame v_jfMain, boolean modal, boolean isCreate) {
        super(v_jfMain, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        if (isCreate) {
            setTitle("Crear proveedor");
            setName("jdCrearProveedor");
        } else {
            setTitle("Modificar proveedor");
            setName("jdModificarProveedor");
        }
        setAlwaysOnTop(false);
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        initPaneNorth();
        initDatosGenerales();
        initDatosSucursal();
        initDatosContacto();
        initPanelSouth();
        jtpCenter = new javax.swing.JTabbedPane();
        jtpCenter.add("Datos Generales", jpDatosGenerales);
        jtpCenter.add("Datos de Sucursales", jpDatosSucursal);
        jtpCenter.add("Datos de Contacto", jpDatosContacto);
        getContentPane().add(jpNorth, java.awt.BorderLayout.NORTH);
        getContentPane().add(jtpCenter, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void initPaneNorth() {
        //Panel
        jpImagen = new javax.swing.JPanel(new java.awt.BorderLayout());
        jpImagen.setBorder(javax.swing.BorderFactory.createTitledBorder("Imagen"));
        //Label & Button
        jlImagen = new javax.swing.JLabel();
        jlImagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlImagen.setPreferredSize(new java.awt.Dimension(200, 200));
        jbCambiarImagen = new javax.swing.JButton("Cambiar imagen");
        //Adding components into panel
        jpImagen.add(jlImagen, java.awt.BorderLayout.CENTER);
        jpImagen.add(jbCambiarImagen, java.awt.BorderLayout.SOUTH);
        jpNorth = new javax.swing.JPanel(new java.awt.GridLayout(1, 2));
        javax.swing.JPanel panel2 = new javax.swing.JPanel(new java.awt.GridLayout(1, 2));
        jtaAyuda = new javax.swing.JTextArea();
        jtaAyuda.setEditable(false);
        jtaAyuda.setEnabled(false);
        jtaAyuda.setText("Los campos marcados con un asterisco(*) son obligatorios.");
        jspAyuda = new javax.swing.JScrollPane(jtaAyuda);
        jpNorth.add(jpImagen);
        jpNorth.add(new javax.swing.JLabel("Los campos marcados con un asterisco(*) son obligatorios."));
    }

    private void initDatosGenerales() {
        int swingConstant = javax.swing.SwingConstants.CENTER;
        jpDatosGenerales = new javax.swing.JPanel(new net.miginfocom.swing.MigLayout());
        jtfRazonSocial = new javax.swing.JTextField();
        jtfNombreFantasia = new javax.swing.JTextField();
        jtfRUC = new javax.swing.JTextField();
        jtfRUC_ID = new javax.swing.JTextField();
        jtfDescripcion = new javax.swing.JTextField();
        jtfDireccion = new javax.swing.JTextField();
        jtfemail = new javax.swing.JTextField();
        jtfPagWeb = new javax.swing.JTextField();
        jtfTelefono = new javax.swing.JTextField();
        jtTelefono = new javax.swing.JTable();
        jspTelefono = new javax.swing.JScrollPane(jtTelefono);
        jtaNota = new javax.swing.JTextArea();
        jspNota = new javax.swing.JScrollPane(jtaNota);

        jlRazonSocial = new javax.swing.JLabel("Razón social");
        jlRazonSocial.setHorizontalAlignment(swingConstant);
        jlNombreFantasia = new javax.swing.JLabel("Nombre fantasía");
        jlNombreFantasia.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlRUC = new javax.swing.JLabel("R.U.C.");
        jlRUC.setHorizontalAlignment(swingConstant);
        jlRUC_ID = new javax.swing.JLabel("División");
        jlRUC_ID.setHorizontalAlignment(swingConstant);
        jlDescripcion = new javax.swing.JLabel("Descripción");
        jlDescripcion.setHorizontalAlignment(swingConstant);
        jlDireccion = new javax.swing.JLabel("Dirección");
        jlDireccion.setHorizontalAlignment(swingConstant);
        jlemail = new javax.swing.JLabel("E-mail");
        jlemail.setHorizontalAlignment(swingConstant);
        jlPagWeb = new javax.swing.JLabel("Web:");
        jlPagWeb.setHorizontalAlignment(swingConstant);
        jlTelefono = new javax.swing.JLabel("Telefono");
        jlTelefono.setHorizontalAlignment(swingConstant);
        jlNota = new javax.swing.JLabel("Notas");
        jlNota.setHorizontalAlignment(swingConstant);
        jbAgregarTelefono = new javax.swing.JButton("Agr. telefono");
        jbAgregarTelefono.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbQuitarTelefono = new javax.swing.JButton("Quit. telefono");
        jbQuitarTelefono.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbModTelefono = new javax.swing.JButton("Mod. telefono");
        jbModTelefono.setMargin(new java.awt.Insets(0, 0, 0, 0));
        //COLUMNA FILA

        jpDatosGenerales.add(jlRazonSocial, "width :100:,spanx, cell 0 0");
        jpDatosGenerales.add(jtfRazonSocial, "width :290:,spanx,cell 1 0");

        jpDatosGenerales.add(jlNombreFantasia, "width :100:,spanx,cell 0 1");
        jpDatosGenerales.add(jtfNombreFantasia, "width :290:,spanx,cell 1 1");

        jpDatosGenerales.add(jlRUC, "width :100:,cell 0 2");
        jpDatosGenerales.add(jtfRUC, "width :200:,cell 1 2");

        jpDatosGenerales.add(jlRUC_ID, "width :25:,cell 1 2");
        jpDatosGenerales.add(jtfRUC_ID, "width :50:,cell 3 2");

        jpDatosGenerales.add(jlDireccion, "width :100:,cell 0 3");
        jpDatosGenerales.add(jtfDireccion, "width :300:,cell 1 3");

        jpDatosGenerales.add(jlNota, "width :100:,cell 0 4");
        jpDatosGenerales.add(jspNota, "width :260:,spany,growy,cell 1 4");

        jpDatosGenerales.add(jlPagWeb, "width :100:,cell 4 0");
        jpDatosGenerales.add(jtfPagWeb, "width :300:,cell 5 0");

        jpDatosGenerales.add(jlemail, "width :100:,cell 4 1");
        jpDatosGenerales.add(jtfemail, "width :300:,cell 5 1");

        jpDatosGenerales.add(jlDescripcion, "width :100:,cell 4 2");
        jpDatosGenerales.add(jtfDescripcion, "width :300:,cell 5 2");

        jpDatosGenerales.add(jlTelefono, "width :100:,cell 4 3");
        jpDatosGenerales.add(jspTelefono, "width :300:,spany,cell 5 3");

        jpDatosGenerales.add(jbAgregarTelefono, "width :100:,cell 4 4");
        jpDatosGenerales.add(jbModTelefono, "width :100:,cell 4 5");
        jpDatosGenerales.add(jbQuitarTelefono, "width :100:,cell 4 6");

    }

    private void initDatosSucursal() {
        jpDatosSucursal = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JPanel jpsotuh = new javax.swing.JPanel();
        jbAgregarSucursal = new javax.swing.JButton("Agregar sucursal");
        jpsotuh.add(jbAgregarSucursal);
        jbModSucursal = new javax.swing.JButton("Modificar sucursal");
        jpsotuh.add(jbModSucursal);
        jbQuitarSucursal = new javax.swing.JButton("Quitar sucursal");
        jpsotuh.add(jbQuitarSucursal);
        jtSucursal = new javax.swing.JTable();
        jspSucursal = new javax.swing.JScrollPane(jtSucursal);

        jpDatosSucursal.add(jpsotuh, java.awt.BorderLayout.NORTH);
        jpDatosSucursal.add(jspSucursal, java.awt.BorderLayout.CENTER);
    }

    private void initDatosContacto() {
        jpDatosContacto = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JPanel jpsotuh = new javax.swing.JPanel();
        jbAgregarContacto = new javax.swing.JButton("Agregar contacto");
        jpsotuh.add(jbAgregarContacto);
        jbModContacto = new javax.swing.JButton("Modificar contacto");
        jpsotuh.add(jbModContacto);
        jbQuitarContacto = new javax.swing.JButton("Quitar contacto");
        jpsotuh.add(jbQuitarContacto);
        jtContacto = new javax.swing.JTable();
        jspContacto = new javax.swing.JScrollPane(jtContacto);

        jpDatosContacto.add(jpsotuh, java.awt.BorderLayout.NORTH);
        jpDatosContacto.add(jspContacto, java.awt.BorderLayout.CENTER);
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
