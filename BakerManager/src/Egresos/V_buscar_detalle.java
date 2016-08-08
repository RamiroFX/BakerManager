/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import Utilities.JTablePagination;
import bakermanager.C_inicio;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
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
 * @author Ramiro
 */
public class V_buscar_detalle extends JDialog {

    public JButton jbBuscar, jbBorrar, jbCerrar, jbDetalle, jbProveedor,
            jbFuncionario;
    public JTextField jtfBuscar, jtfProveedor, jtfFuncionario;
    public JComboBox jcbImpuesto, jcbMarca, jcbEstado, jcbCategoria, jcbCondCompra;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JTable jtCabecera;
    private JScrollPane jspEgresoCabecera;
    public JTablePagination jtDetalle;
    private JSplitPane jspMid;
    public JDateChooser jddInicio, jddFinal;
    public JRadioButton jrbDescripcion, jrbObservacion;

    public V_buscar_detalle(C_inicio c_inicio) {
        super(c_inicio.vista, "Buscar egreso por detalle", JDialog.ModalityType.APPLICATION_MODAL);
        setSize(900, 600);
        setName("jdBuscarEgresoDetalle");
        setLocationRelativeTo(c_inicio.vista);
        initTop();
        initMid();
        initBot();
        getContentPane().add(jpTop, "North");
        getContentPane().add(jspMid, "Center");
        getContentPane().add(jpBot, "South");
    }

    private void initTop() {
        //jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        jpTop = new JPanel(new MigLayout());
        //JPanel jpFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jbProveedor = new JButton("Proveedor");
        jtfProveedor = new JTextField();
        jtfProveedor.setEditable(false);
        jtfProveedor.setPreferredSize(new Dimension(250, 10));
        jcbImpuesto = new JComboBox();
        jcbMarca = new JComboBox();
        jcbEstado = new JComboBox();
        jcbCategoria = new JComboBox();
        jbFuncionario = new JButton("Funcionario");
        jtfFuncionario = new JTextField();
        jtfFuncionario.setEditable(false);
        jtfFuncionario.setPreferredSize(new Dimension(250, 10));
        jcbCondCompra = new JComboBox();
        jddInicio = new JDateChooser();
        jddFinal = new JDateChooser();
        jpBotonesTop = new JPanel();
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtfBuscar = new JTextField();
        jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
        jtfBuscar.setPreferredSize(new Dimension(300, 30));
        jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
        jbBuscar = new JButton("Buscar");
        jbBorrar = new JButton("Borrar");
        jrbDescripcion = new JRadioButton("Descripción", true);
        jrbObservacion = new JRadioButton("Observación");
        javax.swing.ButtonGroup bg1 = new javax.swing.ButtonGroup();
        bg1.add(jrbDescripcion);
        bg1.add(jrbObservacion);
        jpBotonesTop.add(jrbDescripcion);
        jpBotonesTop.add(jrbObservacion);
        jpBotonesTop.add(jtfBuscar, "width :200:");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar);

        jpTop.add(jbProveedor);
        jpTop.add(jtfProveedor);
        jpTop.add(jbFuncionario);
        jpTop.add(jtfFuncionario, "wrap");
        jpTop.add(new JLabel("Fecha inicio:"));
        jpTop.add(jddInicio, "growx");
        jpTop.add(new JLabel("Marca:"));
        jpTop.add(jcbMarca);
        jpTop.add(new JLabel("Categoría:"));
        jpTop.add(jcbCategoria, "growx, wrap");
        jpTop.add(new JLabel("Fecha final:"));
        jpTop.add(jddFinal, "growx");
        jpTop.add(new JLabel("Impuesto:"));
        jpTop.add(jcbImpuesto, "growx");
        jpTop.add(new JLabel("Estado:"));
        jpTop.add(jcbEstado, "growx");
        jpTop.add(new JLabel("Cond. compra:"));
        jpTop.add(jcbCondCompra);
        jpTop.add(jpBotonesTop, "dock north");
        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void initMid() {
        //Panel medio izquierda
        /*jtDetalle = new JTable();
         jspEgresoDetalle = new JScrollPane(jtDetalle);*/
        jtDetalle = new JTablePagination(100);

        //panel medio derecha
        jtCabecera = new JTable();
        jspEgresoCabecera = new JScrollPane(jtCabecera);
        //creamos nuestro splitpane y agregamos los dos paneles del medio
        jspMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jtDetalle, jspEgresoCabecera);
        jspMid.setDividerLocation(this.getWidth() / 2);
        jspMid.setOneTouchExpandable(true);
    }

    private void initBot() {
        jpBot = new JPanel();
        jbDetalle = new JButton("Ver detalle");
        jbCerrar = new JButton("Cerrar");
        jpBot.add(jbDetalle);
        jpBot.add(jbCerrar);
    }
}
