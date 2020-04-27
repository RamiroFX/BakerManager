/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
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

    JButton jbSalir, jbBuscar, jbBorrar;
    JScrollPane jspProducto;
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

        jcbEstado = new JComboBox();
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
        jpTop.add(jpFiltros, "span, grow");
        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void initComp() {
        initTop();
        jtProducto = new JTable();
        jtProducto.getTableHeader().setReorderingAllowed(false);
        jspProducto = new JScrollPane(jtProducto);
        jbSalir = new JButton("Cerrar");
        jpBotones = new JPanel();
        jpBotones.add(jbSalir);
    }
}
