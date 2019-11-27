/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import Entities.E_tipoOperacion;
import Interface.CommonFormat;
import bakermanager.V_inicio;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro
 */
public class V_historialFacturacion extends JDialog {

    public JButton jbBuscar, jbBorrar, jbSalir,
            jbCliente, jbEmpleado, jbFacturacionDetalle, jbVentaDetalle;
    public JTextField jtfNroFactura, jtfCliente, jtfEmpleado;
    public JComboBox<E_tipoOperacion> jcbCondVenta;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JTable jtFacturacion, jtVentas;
    ;
    private JSplitPane jspMid;
    public JDateChooser jddInicio, jddFinal;

    public V_historialFacturacion(V_inicio inicio) {
        super(inicio, "Historial de facturacion");
        setSize(950, 600);
        setName("jdHistorialFacturacion");
        initTop();
        initMid();
        initBot();
        getContentPane().add(jpTop, "North");
        getContentPane().add(jspMid, "Center");
        getContentPane().add(jpBot, "South");
        setLocationRelativeTo(null);
    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jbCliente = new JButton("Cliente");
        jtfCliente = new JTextField();
        jtfCliente.setPreferredSize(new Dimension(250, 10));
        jtfCliente.setEditable(false);
        jbEmpleado = new JButton("Funcionario");
        jtfEmpleado = new JTextField();
        jtfEmpleado.setPreferredSize(new Dimension(250, 10));
        jtfEmpleado.setEditable(false);
        jcbCondVenta = new JComboBox();
        jtfNroFactura = new JTextField();
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jpFiltros.add(jbCliente, "growx");
        jpFiltros.add(jtfCliente, "growx");
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx");
        jpFiltros.add(new JLabel("Cond. compra:"));
        jpFiltros.add(jcbCondVenta, "wrap");
        jpFiltros.add(jbEmpleado);
        jpFiltros.add(jtfEmpleado, "growx");
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx");
        jpFiltros.add(new JLabel("Nro. factura:"));
        jpFiltros.add(jtfNroFactura, "growx, wrap");
        jpBotonesTop = new JPanel(new MigLayout());
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        /*jtfBuscar = new JTextField();
         jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
         jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
         jpJtextFieldTop.add(jtfBuscar);*/
        jbBuscar = new JButton("Buscar");
        jbBuscar.setName("buscar facturacion");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar, "wrap");
        jpBotonesTop.add(jbBorrar);
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda"));
    }

    private void initMid() {
        //Panel medio izquierda
        Insets insets = new Insets(10, 10, 10, 10);
        jbFacturacionDetalle = new JButton("Facturación detalle");
        jbFacturacionDetalle.setFont(CommonFormat.fuente);
        jbFacturacionDetalle.setMargin(insets);
        jbFacturacionDetalle.setName("detalle facturacion");
        jtFacturacion = new JTable();
        jtFacturacion.getTableHeader().setReorderingAllowed(false);
        JScrollPane jspFacturacion = new JScrollPane(jtFacturacion);
        JPanel jpFacturacion = new JPanel(new BorderLayout());
        jpFacturacion.add(jspFacturacion, BorderLayout.CENTER);
        jpFacturacion.add(jbFacturacionDetalle, BorderLayout.SOUTH);

        //panel medio derecha
        jbVentaDetalle = new JButton("Venta detalle");
        jbVentaDetalle.setFont(CommonFormat.fuente);
        jbVentaDetalle.setName("detalle venta");
        jbVentaDetalle.setMargin(insets);
        jtVentas = new JTable();
        jtVentas.getTableHeader().setReorderingAllowed(false);
        JScrollPane jspVentas = new JScrollPane(jtVentas);
        JPanel jpVentas = new JPanel(new BorderLayout());
        jpVentas.add(jspVentas, BorderLayout.CENTER);
        jpVentas.add(jbVentaDetalle, BorderLayout.SOUTH);
        //creamos nuestro splitpane y agregamos los dos paneles del medio
        jspMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpFacturacion, jpVentas);
        jspMid.setDividerLocation(this.getWidth() / 2);
        jspMid.setOneTouchExpandable(true);
    }

    private void initBot() {
        jpBot = new JPanel();
        jbSalir = new JButton("Salir");
        jpBot.add(jbSalir);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
