/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import Entities.E_impresionTipo;
import Entities.E_tipoOperacion;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearPedido extends JDialog {

    //NORTE
    //norte 1
    JPanel jpNorth, jpNorth1, jpNorth2;
    public JTextField jtfCliente, jtfNroFactura, jtfClieDireccion, jtfClieTelefono, jtfClieRuc;
    public JButton jbCliente, jbNroFactura;
    public JComboBox<E_tipoOperacion> jcbCondVenta;
    public JComboBox<E_impresionTipo> jcbTipoVenta;
    //norte 2
    public JComboBox jcbHora, jcbMinuto;
    public JDateChooser jdcFechaEntrega;
    public JTextField jtfDireccionPedido, jtfReferencia;
    //CENTRO
    JPanel jpCenter;
    public JTable jtPedidoDetalle;
    public JScrollPane jspFacturaDetalle;
    public JButton jbSeleccionarProducto, jbModificarDetalle, jbEliminarDetalle;
    public JLabel jlIva5, jlIva10, jlExenta, jlTotal;
    public JFormattedTextField jftIva5, jftIva10, jftExenta, jftTotal;
    //SUR
    public JPanel jpSouth;
    public JButton jbAceptar, jbSalir, jbImprimir, jbPagarPedido;

    public V_crearPedido(JFrame frame) {
        super(frame, "Crear pedido", JDialog.ModalityType.APPLICATION_MODAL);
        setSize(900, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(frame);
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
        jpNorth1 = new JPanel(new MigLayout());
        jpNorth1.setBorder(new javax.swing.border.TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Datos de cliente", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
        jbCliente = new JButton("Agregar cliente[F3]");
        jtfCliente = new JTextField(30);
        jtfCliente.setEditable(false);
        jbNroFactura = new JButton("Nro. factura");
        jtfNroFactura = new JTextField(30);
        jcbCondVenta = new JComboBox();
        jcbTipoVenta = new JComboBox();
        jtfClieRuc = new JTextField(30);
        jtfClieRuc.setEditable(false);
        jtfClieDireccion = new JTextField(30);
        jtfClieDireccion.setEditable(false);
        jtfClieTelefono = new JTextField(30);
        jtfClieTelefono.setEditable(false);
        JPanel jpTiop = new JPanel();
        jpTiop.add(new JLabel("Tipo operación"));
        jpTiop.add(jcbTipoVenta);
        JPanel jpCondVenta = new JPanel();
        jpCondVenta.add(new JLabel("Cond. venta"));
        jpCondVenta.add(jcbCondVenta);
        jpNorth1.add(jbCliente);
        jpNorth1.add(jtfCliente);
        jpNorth1.add(jbNroFactura);
        jpNorth1.add(jtfNroFactura);
        jpNorth1.add(jpCondVenta);
        jpNorth1.add(jpTiop, "wrap");
        jpNorth1.add(new JLabel("R.U.C.:"));
        jpNorth1.add(jtfClieRuc);
        jpNorth1.add(new JLabel("Direccion:"));
        jpNorth1.add(jtfClieDireccion);
        jpNorth1.add(new JLabel("Telefono:"));
        jpNorth1.add(jtfClieTelefono);

        jpNorth2 = new JPanel(new MigLayout());
        jpNorth2.setBorder(new javax.swing.border.TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Datos de pedido", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
        jcbHora = new JComboBox();
        for (int i = 1; i < 10; i++) {
            jcbHora.addItem("0" + i);
        }
        for (int i = 10; i < 24; i++) {
            jcbHora.addItem("" + i);
        }
        jcbMinuto = new JComboBox();
        for (int i = 0; i < 10; i++) {
            jcbMinuto.addItem("0" + i);
        }
        for (int i = 10; i < 60; i++) {
            jcbMinuto.addItem("" + i);
        }
        JPanel jpHora = new JPanel(new GridLayout(1, 2));
        jpHora.add(jcbHora);
        jpHora.add(jcbMinuto);
        jdcFechaEntrega = new JDateChooser();
        jdcFechaEntrega.setPreferredSize(new Dimension(150, 20));
        jtfDireccionPedido = new JTextField();
        jtfReferencia = new JTextField();
        jpNorth2.add(new JLabel("Fecha de entrega"));
        jpNorth2.add(jdcFechaEntrega);
        jpNorth2.add(new JLabel("Dirección de entrega"));
        jpNorth2.add(jtfDireccionPedido, "growx,pushx, wrap");
        jpNorth2.add(new JLabel("Hora de entrega"));
        jpNorth2.add(jpHora, "growx");
        jpNorth2.add(new JLabel("Referencias"));
        jpNorth2.add(jtfReferencia, "pushx, growx");
        jpNorth = new JPanel(new GridLayout(2, 1));
        jpNorth.add(jpNorth1);
        jpNorth.add(jpNorth2);
    }

    private void initCenter() {
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtPedidoDetalle = new JTable();
        this.jtPedidoDetalle.getTableHeader().setReorderingAllowed(false);
        jtPedidoDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspFacturaDetalle = new JScrollPane(jtPedidoDetalle);
        jbSeleccionarProducto = new JButton("Agregar producto[F4]");
        jbModificarDetalle = new JButton("Modificar detalle");
        jbEliminarDetalle = new JButton("Eliminar detalle");
        JPanel jpSouthAux = new JPanel(new MigLayout());
        jpSouthAux.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAux.add(jbSeleccionarProducto, "wrap");
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
        jbAceptar = new JButton("Guardar cambios[F1]");
        jbImprimir = new JButton("Imprimir[F2]");
        jbPagarPedido = new JButton("Pagar pedido[F5]");
        jbSalir = new JButton("Salir[ESC]");
        jpSouth.add(jbAceptar);
        jpSouth.add(jbImprimir);
        jpSouth.add(jbPagarPedido);
        jpSouth.add(jbSalir);
    }
}
