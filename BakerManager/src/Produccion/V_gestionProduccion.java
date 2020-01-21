/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Entities.E_produccionTipo;
import Entities.Estado;
import Interface.CommonFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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
public class V_gestionProduccion extends JInternalFrame {

    public JButton jbBuscar, jbBuscarDetalle, jbBorrar, jbCrearProduccion,
            jbRegistroMateriaPrima, jbDetalle,
            jbResumen, jbEmpleado, jbAnular;
    public JTextField jtfNroOrdenTrabajo, jtfEmpleado;
    public JComboBox jcbEmpleado;
    public JComboBox<E_produccionTipo> jcbTipoProduccion;
    public JComboBox<Estado> jcbEstado;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JTable jtProduccion, jtPedidoDetalle;
    private JScrollPane jspEgresoCabecera, jspEgresoDetalle;
    private JSplitPane jspMid;
    public JDateChooser jddInicio, jddFinal;

    public V_gestionProduccion(JFrame frame) {
        super("Gesión de producción", true, true, true, true);
        //super(frame, "Gestión de pedidos", true);
        //setPreferredSize(new Dimension(950, 600));
        setSize(1200, 600);
        setName("jifGestionProduccion");
        //setLocationRelativeTo(frame);
        initTop();
        initMid();
        initBot();
        getContentPane().add(jpTop, "North");
        getContentPane().add(jspMid, "Center");
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
        jcbTipoProduccion = new JComboBox();
        jcbEstado = new JComboBox();
        jtfNroOrdenTrabajo = new JTextField();
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jpFiltros.add(jbEmpleado, "growx");
        jpFiltros.add(jtfEmpleado, "growx");
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx");
        jpFiltros.add(new JLabel("Tipo producción:"));
        jpFiltros.add(jcbTipoProduccion, "wrap");
        jpFiltros.add(new JLabel("Estado:"));
        jpFiltros.add(jcbEstado);
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx");
        jpFiltros.add(new JLabel("Nro. Orden trabajo:"));
        jpFiltros.add(jtfNroOrdenTrabajo, "growx");
        jpFiltros.add(new JLabel(), "growx");
        jpFiltros.add(new JLabel(), "growx");
        jpBotonesTop = new JPanel(new MigLayout());
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jbBuscar = new JButton("Buscar");
        jbBuscar.setName("buscar produccion");
        jbBorrar = new JButton("Borrar");
        jbBuscarDetalle = new JButton("Buscar por detalle");
        jpBotonesTop.add(jbBuscar, "span, growx, wrap");
        jpBotonesTop.add(jbBorrar, "growx");
        jpBotonesTop.add(jbBuscarDetalle, "span, growx");
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda"));
    }

    private void initMid() {
        //Panel medio izquierda
        jtProduccion = new JTable();
        this.jtProduccion.getTableHeader().setReorderingAllowed(false);
        jspEgresoCabecera = new JScrollPane(jtProduccion);

        //panel medio derecha
        jtPedidoDetalle = new JTable();
        this.jtPedidoDetalle.getTableHeader().setReorderingAllowed(false);
        jspEgresoDetalle = new JScrollPane(jtPedidoDetalle);
        //creamos nuestro splitpane y agregamos los dos paneles del medio
        jspMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspEgresoCabecera, jspEgresoDetalle);
        jspMid.setDividerLocation(this.getWidth() / 2);
        jspMid.setOneTouchExpandable(true);
    }

    private void initBot() {
        jpBot = new JPanel();
        Insets inset = new Insets(10, 10, 10, 10);
        jbCrearProduccion = new JButton("Crear produccion [F1]");
        jbCrearProduccion.setName("crear produccion");
        jbCrearProduccion.setMargin(inset);
        jbCrearProduccion.setFont(CommonFormat.fuente);
        jbRegistroMateriaPrima = new JButton("Utilizar Materia Prima [F2]");
        jbRegistroMateriaPrima.setName("utilizar materia prima");
        jbRegistroMateriaPrima.setMargin(inset);
        jbRegistroMateriaPrima.setFont(CommonFormat.fuente);
        jbAnular = new JButton("Cancelar produccion");
        jbAnular.setName("cancelar produccion");
        jbAnular.setMargin(inset);
        jbAnular.setFont(CommonFormat.fuente);
        jbDetalle = new JButton("Ver detalle");
        jbDetalle.setName("modificar produccion");
        jbDetalle.setMargin(inset);
        jbDetalle.setFont(CommonFormat.fuente);
        jbResumen = new JButton("Ver resumen [F3]");
        jbResumen.setName("resumen produccion");
        jbResumen.setMargin(inset);
        jbResumen.setFont(CommonFormat.fuente);
        jpBot.add(jbCrearProduccion);
        jpBot.add(jbRegistroMateriaPrima);
        jpBot.add(jbAnular);
        jpBot.add(jbDetalle);
        jpBot.add(jbResumen);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
