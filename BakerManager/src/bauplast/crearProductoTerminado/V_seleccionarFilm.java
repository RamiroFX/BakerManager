/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearProductoTerminado;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_seleccionarFilm extends JDialog {

    JButton jbAceptar, jbSalir, jbBuscar, jbBorrar;
    JScrollPane jspProducto;
    JTable jtProducto;
    JPanel jpBotones, jpTop, jpBotonesTop, jpJtextFieldTop;
    public JComboBox jcbBuscarPor, jcbOrdenarPor, jcbClasificarPor, jcbEstado;
    public JTextField jtfBuscar;
    public JCheckBox jcbPorFecha;
    public JDateChooser jdcInicio, jdcFin;

    public V_seleccionarFilm(JDialog main) {
        super(main, "Seleccionar rollo", true);
        setSize(1200, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpTop, BorderLayout.NORTH);
        getContentPane().add(jspProducto, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpFiltroFecha = new JPanel(new FlowLayout(FlowLayout.CENTER));

        jdcInicio = new JDateChooser();
        jdcFin = new JDateChooser();
        jcbPorFecha = new JCheckBox("Por fecha");
        jcbEstado = new JComboBox();
        jcbClasificarPor = new JComboBox();
        jcbBuscarPor = new JComboBox();
        jcbOrdenarPor = new JComboBox();
        jpFiltroFecha.add(jcbPorFecha);
        jpFiltroFecha.add(new JLabel("Fecha inicio"));
        jpFiltroFecha.add(jdcInicio);
        jpFiltroFecha.add(new JLabel("Fecha fin"));
        jpFiltroFecha.add(jdcFin);
        jpFiltros.add(new JLabel("Buscar por:"));
        jpFiltros.add(jcbBuscarPor);
        jpFiltros.add(new JLabel("Ordenar por:"));
        jpFiltros.add(jcbClasificarPor);
        jpFiltros.add(jcbOrdenarPor);
        jpFiltros.add(new JLabel("Estado:"));
        jpFiltros.add(jcbEstado);
        jpBotonesTop = new JPanel();
        jpJtextFieldTop = new JPanel(new BorderLayout());
        jtfBuscar = new JTextField();
        jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
        jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
        jpJtextFieldTop.add(jtfBuscar);
        jbBuscar = new JButton("Buscar");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar);
        jpTop.add(jpJtextFieldTop, "push x");
        jpTop.add(jpBotonesTop, "wrap");
        jpTop.add(jpFiltroFecha, "span, grow, wrap");
        jpTop.add(jpFiltros, "span, grow");
        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void initComp() {
        initTop();
        jtProducto = new JTable();
        jtProducto.getTableHeader().setReorderingAllowed(false);
        jspProducto = new JScrollPane(jtProducto);
        jbAceptar = new JButton("Seleccionar producto");
        jbSalir = new JButton("Cerrar");
        jpBotones = new JPanel();
        jpBotones.add(jbAceptar);
        jpBotones.add(jbSalir);
    }
}
