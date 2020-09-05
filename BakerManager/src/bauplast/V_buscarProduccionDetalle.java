/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
public class V_buscarProduccionDetalle extends JDialog {

    JButton jbSalir, jbBuscar, jbBorrar, jbResumen;
    JScrollPane jspProducto;
    public JCheckBox jcbActivarFecha;
    JDateChooser jdcFechaInicio, jdcFechaFinal;
    JTable jtProducto;
    JPanel jpBotones, jpTop, jpBotonesTop, jpJtextFieldTop;
    public JComboBox jcbBuscarPor, jcbOrdenarPor, jcbClasificarPor, jcbEstado;
    public JTextField jtfBuscar;

    public V_buscarProduccionDetalle(JDialog main) {
        super(main, "Buscar producción por detalle", true);
        setSize(1200, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpTop, BorderLayout.NORTH);
        getContentPane().add(jspProducto, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    public V_buscarProduccionDetalle(JFrame main) {
        super(main, "Buscar producción por detalle", true);
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
        jpFiltros.setBorder(new EtchedBorder());
        JPanel jpFiltroFecha = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpFiltroFecha.setBorder(new EtchedBorder());

        jcbEstado = new JComboBox();
        jcbActivarFecha = new JCheckBox("Buscar por fecha");
        jcbActivarFecha.setSelected(true);
        jdcFechaInicio = new JDateChooser();
        jdcFechaFinal = new JDateChooser();
        jcbClasificarPor = new JComboBox();
        jcbBuscarPor = new JComboBox();
        jcbOrdenarPor = new JComboBox();
        jpFiltros.add(new JLabel("Buscar por:"));
        jpFiltros.add(jcbBuscarPor);
        jpFiltros.add(new JLabel("Ordenar por:"));
        jpFiltros.add(jcbClasificarPor);
        jpFiltros.add(jcbOrdenarPor);
        jpFiltros.add(new JLabel("Estado:"));
        jpFiltros.add(jcbEstado);
        jpFiltroFecha.add(jcbActivarFecha);
        jpFiltroFecha.add(new JLabel("Fecha inicio"));
        jpFiltroFecha.add(jdcFechaInicio);
        jpFiltroFecha.add(new JLabel("Fecha final"));
        jpFiltroFecha.add(jdcFechaFinal);
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
        jpTop.add(jpFiltroFecha, "span, grow");
        jpTop.add(jpFiltros, "span, grow");
        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void initComp() {
        initTop();
        jtProducto = new JTable();
        jtProducto.getTableHeader().setReorderingAllowed(false);
        jspProducto = new JScrollPane(jtProducto);
        jbSalir = new JButton("Cerrar");
        jbResumen = new JButton("Resumen");
        jpBotones = new JPanel();
        jpBotones.add(jbSalir);
        jpBotones.add(jbResumen);
    }
}
