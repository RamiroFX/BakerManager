/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

/**
 *
 * @author Ramiro
 */
public class V_crear_egreso extends javax.swing.JDialog {

    public javax.swing.JButton jbAceptar, jbAgregarProd, jbAgregarProv, jbEliminarCelda, jbModificarCelda;
    private javax.swing.JLabel jlProveedor, jlTotal, jlNroFactura;
    private javax.swing.JPanel jpNorth, jpSouth;
    private javax.swing.JScrollPane jspProductos;
    public javax.swing.JTable jtProductos;
    public javax.swing.JTextField jtfProveedor, jtfNroFactura;
    public javax.swing.JRadioButton jrbContado, jrbCredito;
    public javax.swing.JFormattedTextField jftTotal;

    /**
     * Creates new form V_Egresos
     */
    public V_crear_egreso(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Crear egreso");
        setMinimumSize(new java.awt.Dimension(950, 400));
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {

        jpNorth = new javax.swing.JPanel();
        jlProveedor = new javax.swing.JLabel("Proveedor");
        jlNroFactura = new javax.swing.JLabel("Nro. factura");
        jtfProveedor = new javax.swing.JTextField();
        jbAgregarProv = new javax.swing.JButton("Proveedor");
        jbAgregarProd = new javax.swing.JButton("Producto");
        jspProductos = new javax.swing.JScrollPane();
        jtProductos = new javax.swing.JTable();
        jpSouth = new javax.swing.JPanel();
        jlTotal = new javax.swing.JLabel();
        jftTotal = new javax.swing.JFormattedTextField();
        jtfNroFactura = new javax.swing.JTextField(20);
        jbModificarCelda = new javax.swing.JButton();
        jbEliminarCelda = new javax.swing.JButton();
        jbAceptar = new javax.swing.JButton();
        jrbContado = new javax.swing.JRadioButton("Contado",true);
        jrbCredito = new javax.swing.JRadioButton("Crédito");
        javax.swing.ButtonGroup bg1 = new javax.swing.ButtonGroup();

        bg1.add(jrbContado);
        bg1.add(jrbCredito);
        jpNorth.add(jlProveedor);

        jtfProveedor.setEditable(false);
        jtfProveedor.setColumns(20);
        jpNorth.add(jtfProveedor);

        jpNorth.add(jbAgregarProv);

        jpNorth.add(jbAgregarProd);
        jpNorth.add(jrbContado);
        jpNorth.add(jrbCredito);
        jpNorth.add(jlNroFactura);
        jpNorth.add(jtfNroFactura);

        getContentPane().add(jpNorth, java.awt.BorderLayout.NORTH);
        jspProductos.setViewportView(jtProductos);

        getContentPane().add(jspProductos, java.awt.BorderLayout.CENTER);

        jlTotal.setText("Total");
        jpSouth.add(jlTotal);

        jftTotal.setColumns(15);
        jpSouth.add(jftTotal);

        jbModificarCelda.setText("Mod. compra");
        jpSouth.add(jbModificarCelda);

        jbEliminarCelda.setText("Eliminar compra");
        jpSouth.add(jbEliminarCelda);

        jbAceptar.setText("Aceptar");
        jpSouth.add(jbAceptar);

        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);
    }
}
