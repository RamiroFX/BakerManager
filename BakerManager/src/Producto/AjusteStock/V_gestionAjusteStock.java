/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
public class V_gestionAjusteStock extends JDialog {

    public static final String NOMBRE_MODULO = "Gestión de ajuste de stock";

    public JButton jbBuscar, jbBorrar, jbSalir,
            jbEmpleado, jbCrear, jbVer, jbAnular, jbParametros;
    public JTextField jtfIDAjusteStock, jtfCliente, jtfEmpleado;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JTable jtCabecera, jtDetalle;
    ;
    private JSplitPane jspMid;
    public JDateChooser jddInicio, jddFinal;

    public V_gestionAjusteStock(JFrame frame) {
        super(frame, "Gestión de ajuste de stock");
        setSize(950, 600);
        setName("jdGestionAjusteStock");
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
        jtfCliente = new JTextField();
        jtfCliente.setPreferredSize(new Dimension(250, 10));
        jtfCliente.setEditable(false);
        jbEmpleado = new JButton("Funcionario");
        jtfEmpleado = new JTextField();
        jtfEmpleado.setPreferredSize(new Dimension(250, 10));
        jtfEmpleado.setEditable(false);
        jtfIDAjusteStock = new JTextField(10);
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jpFiltros.add(jbEmpleado, "growx");
        jpFiltros.add(jtfEmpleado, "growx");
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx, wrap");
        jpFiltros.add(new JLabel("ID ajuste:"));
        jpFiltros.add(jtfIDAjusteStock);
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx");
        jpBotonesTop = new JPanel(new MigLayout());
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        /*jtfBuscar = new JTextField();
         jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
         jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
         jpJtextFieldTop.add(jtfBuscar);*/
        jbBuscar = new JButton("Buscar");
        jbBuscar.setName("buscar facturacion");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar);
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda"));
    }

    private void initMid() {
        //Panel medio izquierda
        Insets insets = new Insets(10, 10, 10, 10);
        jtCabecera = new JTable();
        JScrollPane jspCabecera = new JScrollPane(jtCabecera);
        jtDetalle = new JTable();
        JScrollPane jspDetalle = new JScrollPane(jtCabecera);
        //creamos nuestro splitpane y agregamos los dos paneles del medio
        jspMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspCabecera, jspDetalle);
        jspMid.setDividerLocation(this.getWidth() / 2);
        jspMid.setOneTouchExpandable(true);
    }

    private void initBot() {
        jpBot = new JPanel();
        jbSalir = new JButton("Salir");
        jbCrear = new JButton("Crear ajuste");
        jbVer = new JButton("Ver detalle");
        jbAnular = new JButton("Anular ajuste");
        jbParametros = new JButton("Parametros");
        jpBot.add(jbCrear);
        jpBot.add(jbVer);
        jpBot.add(jbAnular);
        jpBot.add(jbParametros);
        jpBot.add(jbSalir);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
