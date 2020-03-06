/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import java.awt.BorderLayout;
import javax.swing.JButton;
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
public class V_seleccionarPagoPendiente extends JDialog {

    JButton jbAceptar, jbSalir, jbBuscar, jbBorrar;
    JScrollPane jspFacturaPendiente;
    JTable jtFacturaPendiente;
    JPanel jpBotones, jpTop, jpBotonesTop, jpJtextFieldTop;
    public JTextField jtfBuscar;

    public V_seleccionarPagoPendiente(JDialog main) {
        super(main, "Seleccionar pago pendiente", true);
        setSize(1200, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpTop, BorderLayout.NORTH);
        getContentPane().add(jspFacturaPendiente, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        /*jcbCondVenta = new JComboBox();
        jbFuncionario = new JButton("Funcionario");
        jtfFuncionario = new JTextField(20);
        jtfFuncionario.setEditable(false);*/
        jpBotonesTop = new JPanel();
        jpJtextFieldTop = new JPanel(new BorderLayout());
        jtfBuscar = new JTextField();
        jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
        jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
        jpJtextFieldTop.add(new JLabel("Nro Factura"), BorderLayout.WEST);
        jpJtextFieldTop.add(jtfBuscar, BorderLayout.CENTER);
        jbBuscar = new JButton("Buscar");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar);
        /*jpTop.add(jbFuncionario);
        jpTop.add(jtfFuncionario);
        jpTop.add(new JLabel("Cond. venta:"));
        jpTop.add(jcbCondVenta);*/
        jpTop.add(jpJtextFieldTop, "push x");
        jpTop.add(jpBotonesTop, "wrap");
        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
//        jpTop.add(jpJtextFieldTop, "push x");
//        jpTop.add(jpBotonesTop, "wrap");
//        jpTop.add(jpFiltros, "span, grow");
//        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void initComp() {
        initTop();
        jtFacturaPendiente = new JTable();
        jtFacturaPendiente.getTableHeader().setReorderingAllowed(false);
        jspFacturaPendiente = new JScrollPane(jtFacturaPendiente);
        jbAceptar = new JButton("Seleccionar pago pendiente");
        jbSalir = new JButton("Cerrar");
        jpBotones = new JPanel();
        jpBotones.add(jbAceptar);
        jpBotones.add(jbSalir);
    }
}
