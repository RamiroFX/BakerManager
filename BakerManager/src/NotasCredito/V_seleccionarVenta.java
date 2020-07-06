/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import Entities.E_tipoOperacion;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
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
public class V_seleccionarVenta extends JDialog {

    JButton jbAceptar, jbSalir, jbBuscar, jbBorrar;
    JScrollPane jspVentaCabecera;
    JTable jtVentaCabecera;
    JPanel jpBotones, jpTop, jpBotonesTop, jpJtextFieldTop;
    public JDateChooser jddInicio, jddFinal;
    public JComboBox<E_tipoOperacion> jcbTipoOperacion;
    public JTextField jtfBuscar;

    public V_seleccionarVenta(JDialog main) {
        super(main, "Seleccionar venta", true);
        setSize(1200, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpTop, BorderLayout.NORTH);
        getContentPane().add(jspVentaCabecera, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER));

        jddInicio = new JDateChooser();
        jddFinal = new JDateChooser();
        jcbTipoOperacion = new JComboBox();
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio);
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal);
        jpFiltros.add(new JLabel("Condición de venta:"));
        jpFiltros.add(jcbTipoOperacion);
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
        jtVentaCabecera = new JTable();
        jtVentaCabecera.getTableHeader().setReorderingAllowed(false);
        jspVentaCabecera = new JScrollPane(jtVentaCabecera);
        jbAceptar = new JButton("Seleccionar venta");
        jbSalir = new JButton("Cerrar");
        jpBotones = new JPanel();
        jpBotones.add(jbAceptar);
        jpBotones.add(jbSalir);
    }
}
