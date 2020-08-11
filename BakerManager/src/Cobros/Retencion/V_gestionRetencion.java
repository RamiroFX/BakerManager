/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import Entities.E_tipoOperacion;
import Entities.Estado;
import bakermanager.V_inicio;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
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
 * @author Ramiro Ferreira
 */
public class V_gestionRetencion extends JDialog {

    public JButton jbBuscar, jbBorrar, jbSalir, jbResumen,
            jbCliente, jbEmpleado, jbNueva, jbDetalle, jbAnular;
    public JTextField jtfNroNotaCredito, jtfCliente, jtfEmpleado;
    public JComboBox<Estado> jcbEstado;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JTable jtCabecera, jtDetalle;
    ;
    private JSplitPane jspMid;
    public JDateChooser jddInicio, jddFinal;

    public V_gestionRetencion(V_inicio inicio) {
        super(inicio, "Gestión de retenciones de I.V.A.");
        setSize(1100, 600);
        setName("jdGestionRetencionIVA");
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
        jcbEstado = new JComboBox();
        jtfNroNotaCredito = new JTextField();
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jpFiltros.add(jbCliente, "growx");
        jpFiltros.add(jtfCliente, "growx");
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx");
        jpFiltros.add(new JLabel("Estado:"));
        jpFiltros.add(jcbEstado, "wrap");
        jpFiltros.add(jbEmpleado);
        jpFiltros.add(jtfEmpleado, "growx");
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx");
        jpFiltros.add(new JLabel("Nro. retención:"));
        jpFiltros.add(jtfNroNotaCredito, "growx, wrap");
        jpBotonesTop = new JPanel(new MigLayout());
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        /*jtfBuscar = new JTextField();
         jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
         jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
         jpJtextFieldTop.add(jtfBuscar);*/
        jbBuscar = new JButton("Buscar");
        jbBuscar.setName("buscar retencion iva venta");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar, "wrap");
        jpBotonesTop.add(jbBorrar);
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda"));
    }

    private void initMid() {
        //Panel medio izquierda
        jtCabecera = new JTable();
        jtCabecera.getTableHeader().setReorderingAllowed(false);
        JScrollPane jspFacturacion = new JScrollPane(jtCabecera);

        //panel medio derecha
        jtDetalle = new JTable();
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        JScrollPane jspVentas = new JScrollPane(jtDetalle);
        //creamos nuestro splitpane y agregamos los dos paneles del medio
        jspMid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspFacturacion, jspVentas);
        jspMid.setDividerLocation(this.getWidth() / 2);
        jspMid.setOneTouchExpandable(true);
    }

    private void initBot() {
        jpBot = new JPanel();
        jbNueva = new JButton("Crear retención");
        jbAnular = new JButton("Anular");
        jbDetalle = new JButton("Detalle");
        jbResumen = new JButton("Resumen");
        jbSalir = new JButton("Salir");
        jpBot.add(jbNueva);
        jpBot.add(jbAnular);
        jpBot.add(jbDetalle);
        jpBot.add(jbResumen);
        jpBot.add(jbSalir);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
