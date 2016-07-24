/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Charts;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_diagramas extends JDialog {

    private JPanel jpSouth, jpNorth;
    public ChartPanel chartPanel;
    public JFreeChart chart;
    public JDateChooser jddInicio, jddFin;
    public JButton jbBuscar, jbBorrar, jbSalir;

    public V_diagramas(JFrame jFrame) {
        super(jFrame, "Diagramas", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(jFrame);
        inicializarComponentes();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(chartPanel, BorderLayout.CENTER);
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
                "Pedidos de clientes",
                null, //dataset
                true, // legend?
                true, // tooltips?
                false // URLs?
                );
        chartPanel = new ChartPanel(chart);
    }
}
