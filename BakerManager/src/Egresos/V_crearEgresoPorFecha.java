/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import Entities.E_tipoOperacion;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearEgresoPorFecha extends JDialog {

    public static final String TITLE_READ = "Ver compra";
    public static final String TITLE_CREATE = "Crear compra";
    public static final String SELECCIONAR_FECHA_COMPRA = "Selecionar fecha compra";
    //NORTE
    JPanel jpNorth;
    public JTextField jtfProveedor, jtfNroFactura, jtfProvDireccion, jtfProvTelefono, jtfProvRuc;
    public JButton jbAgregarProv, jbNroFactura;
    public JComboBox<E_tipoOperacion> jcbTipoCompra;
    public JDateChooser jdcFecha;
    //CENTRO
    JPanel jpCenter;
    public JTable jtProductos;
    public JScrollPane jspFacturaDetalle;
    public JButton jbAgregarProducto, jbModificarDetalle, jbEliminarDetalle;
    public JLabel jlIva5, jlIva10, jlExenta, jlTotal;
    public JFormattedTextField jftIva5, jftIva10, jftExenta, jftTotal;
    //SUR
    JPanel jpSouth;
    public JButton jbAceptar, jbSalir, jbImprimir;

    public V_crearEgresoPorFecha(JFrame frame) {
        super(frame, TITLE_CREATE, JDialog.ModalityType.APPLICATION_MODAL);
        setSize(900, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(frame);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    public V_crearEgresoPorFecha(JDialog dialog) {
        super(dialog, TITLE_CREATE, JDialog.ModalityType.APPLICATION_MODAL);
        setSize(900, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(dialog);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void initComponents() {
        initNorth();
        initCenter();
        initSouth();
    }

    private void initNorth() {
        jpNorth = new JPanel(new MigLayout());
        jbAgregarProv = new JButton("Proveedor [F3]");
        jtfProveedor = new JTextField(30);
        jtfProveedor.setEditable(false);
        jbNroFactura = new JButton("Nro. Factura [F5]");
        jtfNroFactura = new JTextField(30);
        jcbTipoCompra = new JComboBox();
        jtfProvRuc = new JTextField(30);
        jtfProvRuc.setEditable(false);
        jtfProvDireccion = new JTextField(30);
        jtfProvDireccion.setEditable(false);
        jtfProvTelefono = new JTextField(30);
        jtfProvTelefono.setEditable(false);
        jdcFecha = new JDateChooser();
        jdcFecha.setName(SELECCIONAR_FECHA_COMPRA);
        jdcFecha.setEnabled(false);
        jpNorth.add(jbAgregarProv);
        jpNorth.add(jtfProveedor);
        jpNorth.add(jbNroFactura);
        jpNorth.add(jtfNroFactura);
        jpNorth.add(new JLabel("Tipo operación:"));
        jpNorth.add(jcbTipoCompra, "wrap");
        jpNorth.add(new JLabel("R.U.C.:"));
        jpNorth.add(jtfProvRuc);
        jpNorth.add(new JLabel("Direccion:"));
        jpNorth.add(jtfProvDireccion);
        jpNorth.add(new JLabel("Telefono:"));
        jpNorth.add(jtfProvTelefono, "wrap");
        jpNorth.add(new JLabel("Fecha:"));
        jpNorth.add(jdcFecha, "spanx 2, growx 2");
    }

    private void initCenter() {
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtProductos = new JTable();
        jtProductos.getTableHeader().setReorderingAllowed(false);
        jtProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspFacturaDetalle = new JScrollPane(jtProductos);
        jbAgregarProducto = new JButton("Agregar producto [F4]");
        jbModificarDetalle = new JButton("Modificar detalle");
        jbEliminarDetalle = new JButton("Eliminar detalle");
        JPanel jpSouthAux = new JPanel(new MigLayout());
        jpSouthAux.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAux.add(jbAgregarProducto, "wrap");
        jpSouthAux.add(jbModificarDetalle, "growx, wrap");
        jpSouthAux.add(jbEliminarDetalle, "growx");

        jlIva5 = new JLabel("I.V.A. 5%");
        jlIva10 = new JLabel("I.V.A. 10%");
        jlExenta = new JLabel("Exentas");
        jlTotal = new JLabel("Total");
        jftExenta = new JFormattedTextField();
        jftExenta.setEditable(false);
        jftIva5 = new JFormattedTextField();
        jftIva5.setEditable(false);
        jftIva10 = new JFormattedTextField();
        jftIva10.setEditable(false);
        jftTotal = new JFormattedTextField();
        jftTotal.setEditable(false);
        JPanel jpDetalleAux = new JPanel(new MigLayout());
        jpDetalleAux.add(jpSouthAux, "wrap");
        jpDetalleAux.add(jlExenta, "wrap");
        jpDetalleAux.add(jftExenta, "growx, wrap");
        jpDetalleAux.add(jlIva5, "wrap");
        jpDetalleAux.add(jftIva5, "growx, wrap");
        jpDetalleAux.add(jlIva10, "wrap");
        jpDetalleAux.add(jftIva10, "growx, wrap");
        jpDetalleAux.add(jlTotal, "wrap");
        jpDetalleAux.add(jftTotal, "growx, wrap");
        jpCenter.add(jspFacturaDetalle, BorderLayout.CENTER);
        jpCenter.add(jpDetalleAux, BorderLayout.EAST);
    }

    private void initSouth() {
        jpSouth = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAceptar = new JButton("Confirmar compra [F1]");
        jbAceptar.setMargin(insets);
        jbSalir = new JButton("Salir [ESC]");
        jbSalir.setMargin(insets);
        jbImprimir = new JButton("Salir [ESC]");
        jbImprimir.setMargin(insets);
        jbImprimir.setVisible(false);
        jpSouth.add(jbAceptar);
        jpSouth.add(jbImprimir);
        jpSouth.add(jbSalir);
    }
}
