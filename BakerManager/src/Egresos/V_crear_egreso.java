/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro
 */
public class V_crear_egreso extends javax.swing.JDialog {

    public javax.swing.JButton jbAceptar, jbSalir, jbAgregarProd, jbAgregarProv, jbEliminarDetalle, jbModificarDetalle;
    private javax.swing.JLabel jlTotal, jlNroFactura, jlIva5, jlIva10, jlExenta;
    private javax.swing.JPanel jpNorth, jpSouth, jpCenter;
    private javax.swing.JScrollPane jspProductos;
    public javax.swing.JTable jtProductos;
    public javax.swing.JTextField jtfProveedor, jtfNroFactura;
    public javax.swing.JRadioButton jrbContado, jrbCredito;
    public javax.swing.JFormattedTextField jftIva5, jftIva10, jftExenta, jftTotal;

    /**
     * Creates new form V_Egresos
     */
    public V_crear_egreso(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Crear egreso");
        setMinimumSize(new java.awt.Dimension(950, 500));
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {

        jpNorth = new javax.swing.JPanel();
        jpSouth = new javax.swing.JPanel();
        jlNroFactura = new javax.swing.JLabel("Nro. factura");
        jtfProveedor = new javax.swing.JTextField();
        jbAgregarProv = new javax.swing.JButton("Proveedor");
        jtfNroFactura = new javax.swing.JTextField(20);
        jbAceptar = new javax.swing.JButton("Aceptar");
        jbSalir = new javax.swing.JButton("Salir");
        jrbContado = new javax.swing.JRadioButton("Contado", true);
        jrbCredito = new javax.swing.JRadioButton("Crédito");
        javax.swing.ButtonGroup bg1 = new javax.swing.ButtonGroup();

        bg1.add(jrbContado);
        bg1.add(jrbCredito);
        jpNorth.add(jbAgregarProv);

        jtfProveedor.setEditable(false);
        jtfProveedor.setColumns(20);
        jpNorth.add(jtfProveedor);


        jpNorth.add(jrbContado);
        jpNorth.add(jrbCredito);
        jpNorth.add(jlNroFactura);
        jpNorth.add(jtfNroFactura);

        jpSouth.add(jbAceptar);
        jpSouth.add(jbSalir);
        initCenter();

        getContentPane().add(jpNorth, java.awt.BorderLayout.NORTH);
        getContentPane().add(jpCenter, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);
    }

    private void initCenter() {
        jpCenter = new javax.swing.JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtProductos = new javax.swing.JTable();
        jtProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspProductos = new javax.swing.JScrollPane(jtProductos);
        jbAgregarProd = new javax.swing.JButton("Agregar producto");
        jbModificarDetalle = new javax.swing.JButton("Modificar detalle");
        jbEliminarDetalle = new javax.swing.JButton("Eliminar detalle");
        JPanel jpEastAux = new javax.swing.JPanel(new GridLayout(3, 1));
        jpEastAux.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpEastAux.add(jbAgregarProd);
        jpEastAux.add(jbModificarDetalle);
        jpEastAux.add(jbEliminarDetalle);

        jlIva5 = new javax.swing.JLabel("I.V.A. 5%");
        jlIva10 = new javax.swing.JLabel("I.V.A. 10%");
        jlExenta = new javax.swing.JLabel("Exentas");
        jlTotal = new javax.swing.JLabel("Total");
        jftExenta = new JFormattedTextField();
        jftExenta.setEditable(false);
        jftIva5 = new JFormattedTextField();
        jftIva5.setEditable(false);
        jftIva10 = new JFormattedTextField();
        jftIva10.setEditable(false);
        jftTotal = new JFormattedTextField();
        jftTotal.setColumns(15);
        jftTotal.setEditable(false);
        javax.swing.JPanel jpDetalleAux = new javax.swing.JPanel(new MigLayout());
        jpDetalleAux.add(jpEastAux, "growx, wrap");
        jpDetalleAux.add(jlExenta, "wrap");
        jpDetalleAux.add(jftExenta, "growx, wrap");
        jpDetalleAux.add(jlIva5, "wrap");
        jpDetalleAux.add(jftIva5, "growx, wrap");
        jpDetalleAux.add(jlIva10, "wrap");
        jpDetalleAux.add(jftIva10, "growx, wrap");
        jpDetalleAux.add(jlTotal, "wrap");
        jpDetalleAux.add(jftTotal, "growx, wrap");
        jpCenter.add(jspProductos, BorderLayout.CENTER);
        jpCenter.add(jpDetalleAux, BorderLayout.EAST);
    }
}
