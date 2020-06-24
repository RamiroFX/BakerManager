/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import Interface.CommonFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_cajaDetalle extends JDialog {

    public JButton jbBuscar, jbBorrar, jbAceptar, jbCancelar,
            jbEmpleado;
    public JTextField jtfEmpleado;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JScrollPane jspVentas, jspCompras, jspPagos, jspCobros;
    public JTable jtVentas, jtCompras, jtPagos, jtCobros;
    public JDateChooser jddInicio, jddFinal;
    private JTabbedPane jtpMovimientos;
    /*
    VARIALBLES DE MOVIMIENTO DE VENTAS
     */
    public JButton jbAgregarVentas, jbQuitarVentas;
    private JLabel jlTotalVenta, jlTotalVentaContado, jlTotalVentaCredito;
    public JFormattedTextField jftTotalVenta, jftTotalVentaContado, jftTotalVentaCredito;
    /*
    VARIALBLES DE MOVIMIENTO DE COMPRAS
     */
    public JButton jbAgregarCompras, jbQuitarCompras;
    private JLabel jlTotalCompra, jlTotalCompraContado, jlTotalCompraCredito;
    public JFormattedTextField jftTotalCompra, jftTotalCompraContado, jftTotalCompraCredito;
    /*
    VARIALBLES DE MOVIMIENTO DE COBRO
     */
    public JButton jbAgregarCobro, jbQuitarCobro;
    private JLabel jlTotalCobro, jlTotalCobroEfectivo, jlTotalCobroCheque;
    public JFormattedTextField jftTotalCobro, jftTotalCobroEfectivo, jftTotalCobroCheque;
    /*
    VARIALBLES DE MOVIMIENTO DE PAGO
     */
    public JButton jbAgregarPago, jbQuitarPago;
    private JLabel jlTotalPago, jlTotalPagoEfectivo, jlTotalPagoCheque;
    public JFormattedTextField jftTotalPago, jftTotalPagoEfectivo, jftTotalPagoCheque;

    public V_cajaDetalle(JDialog owner) {
        super(owner, "Detalle de caja", true);
        setSize(950, 600);
        setName("jdDetalleCaja");
        initializeVariable();
        constructLayout();
    }

    private void initializeVariable() {
        initTop();
        initMid();
        initBot();
    }

    private void constructLayout() {
        getContentPane().add(jpTop, "North");
        getContentPane().add(jtpMovimientos, "Center");
        getContentPane().add(jpBot, "South");

    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jbEmpleado = new JButton("Funcionario");
        jtfEmpleado = new JTextField();
        jtfEmpleado.setPreferredSize(new Dimension(250, 10));
        jtfEmpleado.setEditable(false);
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jpFiltros.add(jbEmpleado, "growx");
        jpFiltros.add(jtfEmpleado, "growx");
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx");
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx");
        jpBotonesTop = new JPanel(new MigLayout());
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        /*jtfBuscar = new JTextField();
         jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
         jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
         jpJtextFieldTop.add(jtfBuscar);*/
        jbBuscar = new JButton("Buscar");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar);
        //jpTop.add(jpJtextFieldTop, "pushx");
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda"));
    }

    private void initMid() {
        //MOVIMIENTO DE VENTAS
        jtVentas = new JTable();
        jspVentas = new JScrollPane(jtVentas);
        jbAgregarVentas = new JButton("Agregar todo");
        jbQuitarVentas = new JButton("Quitar todo");
        jlTotalVenta = new JLabel("Total venta");
        jlTotalVentaContado = new JLabel("Total contado");
        jlTotalVentaCredito = new JLabel("Total crédito");
        jftTotalVenta = new JFormattedTextField();
        jftTotalVenta.setEditable(false);
        jftTotalVentaContado = new JFormattedTextField();
        jftTotalVentaContado.setEditable(false);
        jftTotalVentaCredito = new JFormattedTextField();
        jftTotalVentaCredito.setEditable(false);
        JPanel jpMovimientoVentasControles = new JPanel(new MigLayout());
        jpMovimientoVentasControles.add(jlTotalVenta);
        jpMovimientoVentasControles.add(jftTotalVenta, "growx, spanx, wrap");
        jpMovimientoVentasControles.add(jlTotalVentaContado);
        jpMovimientoVentasControles.add(jftTotalVentaContado, "growx, spanx, wrap");
        jpMovimientoVentasControles.add(jlTotalVentaCredito);
        jpMovimientoVentasControles.add(jftTotalVentaCredito, "growx, spanx, wrap");
        jpMovimientoVentasControles.add(jbAgregarVentas);
        jpMovimientoVentasControles.add(jbQuitarVentas);
        JPanel jpMovimientoVentas = new JPanel(new BorderLayout());
        jpMovimientoVentas.add(jspVentas, BorderLayout.CENTER);
        jpMovimientoVentas.add(jpMovimientoVentasControles, BorderLayout.EAST);

        //MOVIMIENTO DE COMPRAS
        jtCompras = new JTable();
        jspCompras = new JScrollPane(jtCompras);
        jbAgregarCompras = new JButton("Agregar todo");
        jbQuitarCompras = new JButton("Quitar todo");
        jlTotalCompra = new JLabel("Total compra");
        jlTotalCompraContado = new JLabel("Total contado");
        jlTotalCompraCredito = new JLabel("Total crédito");
        jftTotalCompra = new JFormattedTextField();
        jftTotalCompra.setEditable(false);
        jftTotalCompraContado = new JFormattedTextField();
        jftTotalCompraContado.setEditable(false);
        jftTotalCompraCredito = new JFormattedTextField();
        jftTotalCompraCredito.setEditable(false);
        JPanel jpMovimientoCompraControles = new JPanel(new MigLayout());
        jpMovimientoCompraControles.add(jlTotalCompra);
        jpMovimientoCompraControles.add(jftTotalCompra, "growx, spanx, wrap");
        jpMovimientoCompraControles.add(jlTotalCompraContado);
        jpMovimientoCompraControles.add(jftTotalCompraContado, "growx, spanx, wrap");
        jpMovimientoCompraControles.add(jlTotalCompraCredito);
        jpMovimientoCompraControles.add(jftTotalCompraCredito, "growx, spanx, wrap");
        jpMovimientoCompraControles.add(jbAgregarCompras);
        jpMovimientoCompraControles.add(jbQuitarCompras);
        JPanel jpMovimientoCompras = new JPanel(new BorderLayout());
        jpMovimientoCompras.add(jspCompras, BorderLayout.CENTER);
        jpMovimientoCompras.add(jpMovimientoCompraControles, BorderLayout.EAST);

        //MOVIMIENTO DE COBROS
        jtCobros = new JTable();
        jspCobros = new JScrollPane(jtCobros);
        jbAgregarCobro = new JButton("Agregar todo");
        jbQuitarCobro = new JButton("Quitar todo");
        jlTotalCobro = new JLabel("Total cobros");
        jlTotalCobroEfectivo = new JLabel("Total efectivo");
        jlTotalCobroCheque = new JLabel("Total cheque");
        jftTotalCobro = new JFormattedTextField();
        jftTotalCobro.setEditable(false);
        jftTotalCobroEfectivo = new JFormattedTextField();
        jftTotalCobroEfectivo.setEditable(false);
        jftTotalCobroCheque = new JFormattedTextField();
        jftTotalCobroCheque.setEditable(false);
        JPanel jpMovimientoCobroControles = new JPanel(new MigLayout());
        jpMovimientoCobroControles.add(jlTotalCobro);
        jpMovimientoCobroControles.add(jftTotalCobro, "growx, spanx, wrap");
        jpMovimientoCobroControles.add(jlTotalCobroEfectivo);
        jpMovimientoCobroControles.add(jftTotalCobroEfectivo, "growx, spanx, wrap");
        jpMovimientoCobroControles.add(jlTotalCobroCheque);
        jpMovimientoCobroControles.add(jftTotalCobroCheque, "growx, spanx, wrap");
        jpMovimientoCobroControles.add(jbAgregarCobro);
        jpMovimientoCobroControles.add(jbQuitarCobro);
        JPanel jpMovimientoCobro = new JPanel(new BorderLayout());
        jpMovimientoCobro.add(jspCobros, BorderLayout.CENTER);
        jpMovimientoCobro.add(jpMovimientoCobroControles, BorderLayout.EAST);

        //MOVIMIENTO DE COBROS
        jtPagos = new JTable();
        jspPagos = new JScrollPane(jtPagos);
        jbAgregarPago = new JButton("Agregar todo");
        jbQuitarPago = new JButton("Quitar todo");
        jlTotalPago = new JLabel("Total pagos");
        jlTotalPagoEfectivo = new JLabel("Total efectivo");
        jlTotalPagoCheque = new JLabel("Total cheque");
        jftTotalPago = new JFormattedTextField();
        jftTotalPago.setEditable(false);
        jftTotalPagoEfectivo = new JFormattedTextField();
        jftTotalPagoEfectivo.setEditable(false);
        jftTotalPagoCheque = new JFormattedTextField();
        jftTotalPagoCheque.setEditable(false);
        JPanel jpMovimientoPagoControles = new JPanel(new MigLayout());
        jpMovimientoPagoControles.add(jlTotalPago);
        jpMovimientoPagoControles.add(jftTotalPago, "growx, spanx, wrap");
        jpMovimientoPagoControles.add(jlTotalPagoEfectivo);
        jpMovimientoPagoControles.add(jftTotalPagoEfectivo, "growx, spanx, wrap");
        jpMovimientoPagoControles.add(jlTotalPagoCheque);
        jpMovimientoPagoControles.add(jftTotalPagoCheque, "growx, spanx, wrap");
        jpMovimientoPagoControles.add(jbAgregarPago);
        jpMovimientoPagoControles.add(jbQuitarPago);
        JPanel jpMovimientoPago = new JPanel(new BorderLayout());
        jpMovimientoPago.add(jspPagos, BorderLayout.CENTER);
        jpMovimientoPago.add(jpMovimientoPagoControles, BorderLayout.EAST);

        jtpMovimientos = new JTabbedPane();
        jtpMovimientos.addTab("Ventas", jpMovimientoVentas);
        jtpMovimientos.addTab("Compras", jpMovimientoCompras);
        jtpMovimientos.addTab("Cobros", jpMovimientoCobro);
        jtpMovimientos.addTab("Pagos", jpMovimientoPago);
    }

    private void initBot() {
        jpBot = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAceptar = new JButton("Aceptar");
        jbAceptar.setFont(CommonFormat.fuente);
        jbAceptar.setMargin(insets);
        jbCancelar = new JButton("Cancelar");
        jbCancelar.setFont(CommonFormat.fuente);
        jbCancelar.setMargin(insets);
        jpBot.add(jbAceptar);
        jpBot.add(jbCancelar);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
