/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

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
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_seleccionarTimbrado extends JDialog {

    JButton jbAceptar, jbSalir, jbBuscar, jbBorrar, jbCrearTimbrado, jbEstablecerPredeterminado;
    JScrollPane jspTimbrado;
    JTable jtTimbrado;
    JPanel jpBotones, jpTop, jpBotonesTop;
    public JComboBox jcbTipoFecha;
    public JCheckBox jcbPorFecha;
    public JDateChooser jdcFechaInicio, jdcFechaFinal;

    public V_seleccionarTimbrado(JFrame main) {
        super(main, "Seleccionar timbrado", true);
        setSize(800, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpTop, BorderLayout.NORTH);
        getContentPane().add(jspTimbrado, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    public V_seleccionarTimbrado(JDialog main) {
        super(main, "Seleccionar timbrado", true);
        setSize(800, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpTop, BorderLayout.NORTH);
        getContentPane().add(jspTimbrado, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jcbTipoFecha = new JComboBox();
        jcbPorFecha = new JCheckBox("Buscar por fechas");
        jdcFechaInicio = new JDateChooser();
        jdcFechaFinal = new JDateChooser();
        jpFiltros.add(jcbPorFecha);
        jpFiltros.add(new JLabel("Tipo fecha:"));
        jpFiltros.add(jcbTipoFecha);
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jdcFechaInicio);
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jdcFechaFinal);
        jpBotonesTop = new JPanel();
        jbBuscar = new JButton("Buscar");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar);
        jpTop.add(jpFiltros, "push x");
        jpTop.add(jpBotonesTop, "wrap");
        jpTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void initComp() {
        initTop();
        jtTimbrado = new JTable();
        jtTimbrado.getTableHeader().setReorderingAllowed(false);
        jspTimbrado = new JScrollPane(jtTimbrado);
        jbCrearTimbrado = new JButton("Crear timbrado");
        jbCrearTimbrado.setName("crear timbrado");
        jbEstablecerPredeterminado = new JButton("Establecer predeterminado");
        jbEstablecerPredeterminado.setName("Establecer predeterminado");
        jbAceptar = new JButton("Seleccionar timbrado");
        jbSalir = new JButton("Cerrar");
        jpBotones = new JPanel();
        jpBotones.add(jbCrearTimbrado);
        jpBotones.add(jbAceptar);
        jpBotones.add(jbEstablecerPredeterminado);
        jpBotones.add(jbSalir);
    }
}
