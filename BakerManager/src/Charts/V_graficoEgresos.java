/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Charts;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_graficoEgresos extends JDialog {

    private JPanel jpSouth, jpNorth, jpCenter;
    public ChartPanel chartPanel;
    public JFreeChart chart;
    public JDateChooser jddInicio, jddFin;
    public JList jltDiagramas, jlClientes;
    public JButton jbBuscar, jbBorrar, jbSalir;
    public JButton jbAddCliente, jbDelCliente;

    public V_graficoEgresos(JFrame jFrame) {
        super(jFrame, "Gráfico de Egresos", Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(jFrame);
        inicializarComponentes();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void inicializarComponentes() {
        jpSouth = new JPanel();
        jbSalir = new JButton("Salir");
        jpSouth.add(jbSalir);

        jpNorth = new JPanel(new MigLayout());
        jbBuscar = new JButton("Buscar");
        jbBorrar = new JButton("Borrar");
        jddInicio = new JDateChooser();
        jddFin = new JDateChooser();
        jpNorth.add(new JLabel("Fecha inicio"));
        jpNorth.add(jddInicio);
        jpNorth.add(new JLabel("Fecha fin"));
        jpNorth.add(jddFin, "wrap");
        jpNorth.add(jbBuscar);
        jpNorth.add(jbBorrar);
        createChart();
    }

    private void createChart() {
        chart = ChartFactory.createPieChart(
                "Compras a proveedores",
                null, //dataset
                true, // legend?
                true, // tooltips?
                false // URLs?
                );
        Object[] a = {"Gráfico Circular", "Gráfico de Barras"};
        jltDiagramas = new JList(a);
        jltDiagramas.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jltDiagramas.setVisibleRowCount(-1);
        
        jlClientes = new JList();
        jlClientes.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jlClientes.setVisibleRowCount(-1);
        JScrollPane jspDiagramas = new JScrollPane(jltDiagramas);
        JScrollPane jspClientes = new JScrollPane(jlClientes);
        
        jbAddCliente = new JButton("Agr. cliente");
        jbDelCliente = new JButton("Quit. cliente");
        
        JPanel jpListasCliente = new JPanel(new MigLayout());
        jpListasCliente.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jpListasCliente.add(jbAddCliente);
        jpListasCliente.add(jbDelCliente,"wrap");
        jpListasCliente.add(jspClientes,"span, pushy");
        JPanel jpListas = new JPanel(new GridLayout(2, 1));
        jpListas.add(jspDiagramas);
        jpListas.add(jpListasCliente);
        chartPanel = new ChartPanel(chart);
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.add(jpListas, BorderLayout.WEST);
        jpCenter.add(chartPanel, BorderLayout.CENTER);
    }
}
