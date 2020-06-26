package Caja;

import Interface.CommonFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
            jbEmpleado, jbExportar, jbAnular;
    public JTextField jtfEmpleado;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JScrollPane jspCaja;
    public JTable jtCaja;
    public JDateChooser jddInicio, jddFinal;

    public V_gestionCaja() {
        super("Caja", true, true, true, true);
        //setPreferredSize(new Dimension(950, 600));
        setSize(950, 600);
        setName("jifGestionCaja");
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
        jbAgregar = new JButton("Saldar caja [F1]");
        jbAgregar.setName("crear caja");
        jbAgregar.setFont(CommonFormat.fuente);
        jbAgregar.setEnabled(false);
        jbAgregar.setMargin(insets);
        jbAnular = new JButton("Anular caja");
        jbAnular.setName("anular caja");
        jbAnular.setFont(CommonFormat.fuente);
        jbAnular.setEnabled(false);
        jbAnular.setMargin(insets);
        jbDetalle = new JButton("Ver detalle");
        jbDetalle.setName("detalle caja");
        jbDetalle.setFont(CommonFormat.fuente);
        jbDetalle.setEnabled(false);
        jbDetalle.setMargin(insets);
        jbExportar = new JButton("Exportar");
        jbExportar.setName("exportar");
        jbExportar.setFont(CommonFormat.fuente);
        jbExportar.setMargin(insets);
        jpBot.add(jbAgregar);
        jpBot.add(jbAnular);
        jpBot.add(jbDetalle);
        jpBot.add(jbExportar);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
