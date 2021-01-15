/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import java.awt.BorderLayout;
import javax.swing.JButton;
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
public class V_seleccionarPagoAnticipado extends JDialog {

    JButton jbAceptar, jbSalir, jbBuscar, jbBorrar;
    JScrollPane jspPagosAnticipados;
    JTable jtPagosAnticipados;
    JPanel jpBotones, jpTop, jpBotonesTop, jpJtextFieldTop;
    public JTextField jtfBuscar;

    public V_seleccionarPagoAnticipado(JDialog main) {
        super(main, "Seleccionar pago anticipado", true);
        setSize(1200, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpTop, BorderLayout.NORTH);
        getContentPane().add(jspPagosAnticipados, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    public V_seleccionarPagoAnticipado(JFrame main) {
        super(main, "Seleccionar pago anticipado", true);
        setSize(1200, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpTop, BorderLayout.NORTH);
        getContentPane().add(jspPagosAnticipados, BorderLayout.CENTER);
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
        jtPagosAnticipados = new JTable();
        jtPagosAnticipados.getTableHeader().setReorderingAllowed(false);
        jspPagosAnticipados = new JScrollPane(jtPagosAnticipados);
        jbAceptar = new JButton("Seleccionar pago anticipado");
        jbSalir = new JButton("Cerrar");
        jpBotones = new JPanel();
        jpBotones.add(jbAceptar);
        jpBotones.add(jbSalir);
    }
}
