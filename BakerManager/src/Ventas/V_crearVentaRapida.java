/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Entities.E_impresionTipo;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearVentaRapida extends JDialog {

    public static final String TITLE_CREATE = "Crear venta", TITLE_READ = "Ver venta";

    private static final String INGRESAR_COD_PROD = "Ingresar código de producto";
    private static final String COD_PROD = "Código de producto";

    //NORTE
    public JPanel jpNorth;
    public JTextField jtfCliente, jtfNroFactura, jtfClieDireccion, jtfClieTelefono,
            jtfClieRuc, jtfCodProd;
    public JButton jbCliente, jbNroFactura;
    //public JLabel jlNroFactura;
    //public JRadioButton jrbContado, jrbCredito;
    public JComboBox jcbCondVenta;
    public JComboBox<E_impresionTipo> jcbTipoVenta;
    //CENTRO
    JPanel jpCenter;
    public JTable jtFacturaDetalle;
    public JScrollPane jspFacturaDetalle;
    public JButton jbAgregarProducto, jbModificarDetalle, jbEliminarDetalle;
    public JLabel jlIva5, jlImpIva5, jlIva10, jlImpIva10, jlExenta, jlTotal, jlIvaTotal;
    public JFormattedTextField jftIva5, jftImpIva5, jftIva10, jftImpIva10, jftExenta, jftTotal, jftIvaTotal;
    //SUR
    JPanel jpSouth;
    public JButton jbAceptar, jbImprimir, jbSalir;
    public boolean esModoCreacion;

    public V_crearVentaRapida(JFrame frame, boolean esModoCreacion_) {
        super(frame, TITLE_CREATE, JDialog.ModalityType.APPLICATION_MODAL);
        setSize(950, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(frame);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
        this.esModoCreacion = esModoCreacion_;
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                if (esModoCreacion) {
                    jtfCodProd.requestFocusInWindow();
                } else {
                    jbSalir.requestFocusInWindow();
                }
            }
        });
    }

    public V_crearVentaRapida(JDialog dialog, boolean esModoCreacion_) {
        super(dialog, TITLE_CREATE, JDialog.ModalityType.APPLICATION_MODAL);
        setSize(950, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(dialog);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
        this.esModoCreacion = esModoCreacion_;
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                if (esModoCreacion) {
                    jtfCodProd.requestFocusInWindow();
                } else {
                    jbSalir.requestFocusInWindow();
                }
            }
        });
    }

    private void initComponents() {
        initNorth();
        initCenter();
        initSouth();
    }

    public void initNorth() {
        jpNorth = new JPanel(new MigLayout());
        jbCliente = new JButton("Selecionar cliente [F3]");
        jbNroFactura = new JButton("Nro. Factura [F5]");
        jtfCliente = new JTextField(30);
        jtfCliente.setEditable(false);
        jtfNroFactura = new JTextField(30);
        jtfNroFactura.setEditable(false);
        jcbCondVenta = new JComboBox();
        jcbTipoVenta = new JComboBox();
        jtfClieRuc = new JTextField(30);
        jtfClieRuc.setEditable(false);
        jtfClieDireccion = new JTextField(30);
        jtfClieDireccion.setEditable(false);
        jtfClieTelefono = new JTextField(30);
        jtfClieTelefono.setEditable(false);
        jpNorth.add(jbCliente);
        jpNorth.add(jtfCliente);
        jpNorth.add(jbNroFactura);
        jpNorth.add(jtfNroFactura);
        jpNorth.add(jcbCondVenta);
        jpNorth.add(jcbTipoVenta, "wrap");
        jpNorth.add(new JLabel("R.U.C.:"));
        jpNorth.add(jtfClieRuc);
        jpNorth.add(new JLabel("Direccion:"));
        jpNorth.add(jtfClieDireccion);
        jpNorth.add(new JLabel("Telefono:"));
        jpNorth.add(jtfClieTelefono);
    }

    private void initCenter() {
        int columns = 8;
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtFacturaDetalle = new JTable();
        jtFacturaDetalle.getTableHeader().setReorderingAllowed(false);
        jtFacturaDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspFacturaDetalle = new JScrollPane(jtFacturaDetalle);
        jtfCodProd = new JTextField(13);
        jtfCodProd.setToolTipText(INGRESAR_COD_PROD);
        JPanel jpCodProd = new JPanel(new MigLayout());
        jpCodProd.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), COD_PROD));
        jpCodProd.add(jtfCodProd);
        jbAgregarProducto = new JButton("Agregar producto [F4]");
        jbModificarDetalle = new JButton("Modificar detalle");
        jbEliminarDetalle = new JButton("Eliminar detalle");
        JPanel jpSouthAux = new JPanel(new MigLayout());
        jpSouthAux.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAux.add(jpCodProd, "growx, wrap");
        jpSouthAux.add(jbAgregarProducto, "growx, wrap");
        jpSouthAux.add(jbModificarDetalle, "growx, wrap");
        jpSouthAux.add(jbEliminarDetalle, "growx");

        jlExenta = new JLabel("Exentas   ");
        jlExenta.setHorizontalAlignment(SwingConstants.CENTER);
        jftExenta = new JFormattedTextField();
        jftExenta.setColumns(columns);
        jftExenta.setEditable(false);
        JPanel jpExenta = new JPanel(new MigLayout());
        jpExenta.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jpExenta.add(jlExenta);
        jpExenta.add(jftExenta);
        jlIva5 = new JLabel("Total 5%  ");
        jlImpIva5 = new JLabel("I.V.A. 5% ");
        jftIva5 = new JFormattedTextField();
        jftImpIva5 = new JFormattedTextField();
        jlIva5.setHorizontalAlignment(SwingConstants.CENTER);
        jlImpIva5.setHorizontalAlignment(SwingConstants.CENTER);
        jftIva5.setEditable(false);
        jftIva5.setColumns(columns);
        jftImpIva5.setEditable(false);
        jftImpIva5.setColumns(columns);
        JPanel jpIva5 = new JPanel(new MigLayout());
        jpIva5.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jpIva5.add(jlIva5);
        jpIva5.add(jftIva5, "growx, wrap");
        jpIva5.add(jlImpIva5);
        jpIva5.add(jftImpIva5);
        jlIva10 = new JLabel("Total 10%");
        jlImpIva10 = new JLabel("I.V.A. 10%");
        jftIva10 = new JFormattedTextField();
        jftIva10.setEditable(false);
        jftIva10.setColumns(columns);
        jftImpIva10 = new JFormattedTextField();
        jftImpIva10.setEditable(false);
        jftImpIva10.setColumns(columns);
        jlIva10.setHorizontalAlignment(SwingConstants.CENTER);
        jlImpIva10.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel jpIva10 = new JPanel(new MigLayout());
        jpIva10.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jpIva10.add(jlIva10);
        jpIva10.add(jftIva10, "growx, wrap");
        jpIva10.add(jlImpIva10);
        jpIva10.add(jftImpIva10);

        Font fTotal = new Font(Font.SERIF, Font.BOLD, 15);
        jlTotal = new JLabel("Total");
        jlTotal.setFont(fTotal);
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jftTotal = new JFormattedTextField();
        jftTotal.setEditable(false);
        jftTotal.setColumns(columns);
        jlIvaTotal = new JLabel("Total iva");
        jlIvaTotal.setFont(fTotal);
        jlIvaTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jftIvaTotal = new JFormattedTextField();
        jftIvaTotal.setEditable(false);
        jftIvaTotal.setColumns(columns);
        JPanel jpTotal = new JPanel(new MigLayout());
        jpTotal.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jpTotal.add(jlTotal);
        jpTotal.add(jftTotal, "growx, wrap");
        jpTotal.add(jlIvaTotal);
        jpTotal.add(jftIvaTotal);

        JPanel jpDetalleAux = new JPanel(new MigLayout());
        jpDetalleAux.add(jpSouthAux, "growx,wrap");
        jpDetalleAux.add(jpExenta, "growx, wrap");
        jpDetalleAux.add(jpIva5, "growx, wrap");
        jpDetalleAux.add(jpIva10, "growx, wrap");
        jpDetalleAux.add(jpTotal, "growx, wrap");
        jpCenter.add(jspFacturaDetalle, BorderLayout.CENTER);
        jpCenter.add(jpDetalleAux, BorderLayout.EAST);
    }

    private void initSouth() {
        jpSouth = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAceptar = new JButton("Confirmar venta [F1]");
        jbAceptar.setMargin(insets);
        jbImprimir = new JButton("Imprimir [F2]");
        jbImprimir.setMargin(insets);
        jbSalir = new JButton("Salir [ESC]");
        jbSalir.setMargin(insets);
        jpSouth.add(jbAceptar);
        jpSouth.add(jbImprimir);
        jpSouth.add(jbSalir);
    }
}
