package Caja;

import Interface.CommonFormat;
import Utilities.JTablePagination;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
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
 * @author Ramiro Ferreira
 */
public class V_gestionCaja extends JInternalFrame {

    public JButton jbBuscar, jbBorrar, jbAgregar, jbDetalle,
            jbResumen, jbEmpleado;
    public JTextField jtfEmpleado;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JScrollPane jspCaja;
    public JTable jtCaja;
    public JDateChooser jddInicio, jddFinal;

    public V_gestionCaja() {
        super("Caja", true, true, true, true);
        //setPreferredSize(new Dimension(950, 600));
        setSize(950, 600);
        setName("jifGestionVentas");
        initTop();
        initMid();
        initBot();
        getContentPane().add(jpTop, "North");
        getContentPane().add(jspCaja, "Center");
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
        jbBuscar.setName("buscar venta");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar);
        //jpTop.add(jpJtextFieldTop, "pushx");
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda"));
    }

    private void initMid() {
        jtCaja = new JTable();
        jspCaja = new JScrollPane(jtCaja);
    }

    private void initBot() {
        jpBot = new JPanel();
        Insets insets = new Insets(10, 10, 10, 10);
        jbAgregar = new JButton("Saldar caja");
        jbAgregar.setName("crear venta");
        jbAgregar.setFont(CommonFormat.fuente);
        jbAgregar.setMargin(insets);
        jbDetalle = new JButton("Ver detalle");
        jbDetalle.setName("detalle venta");
        jbDetalle.setFont(CommonFormat.fuente);
        jbDetalle.setMargin(insets);
        jbResumen = new JButton("Ver resumen");
        jbResumen.setName("resumen venta");
        jbResumen.setMargin(insets);
        jbResumen.setFont(CommonFormat.fuente);
        jbResumen.setEnabled(false);
        jpBot.add(jbAgregar);
        jpBot.add(jbDetalle);
        jpBot.add(jbResumen);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
