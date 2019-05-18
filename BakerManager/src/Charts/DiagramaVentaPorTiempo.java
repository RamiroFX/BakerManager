/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Charts;

import DB.DB_charts;
import com.toedter.calendar.JDateChooser;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 *
 * @author Ramiro Ferreira
 */
public class DiagramaVentaPorTiempo extends JDialog implements ActionListener, KeyListener {

    private JPanel jpSouth, jpNorth;
    public ChartPanel chartPanel;
    public JFreeChart chart;
    public JDateChooser jddInicio, jddFin;
    public JButton jbBuscar, jbBorrar, jbSalir;

    public DiagramaVentaPorTiempo(JFrame jFrame) {
        super(jFrame, "Serie de tiempo", Dialog.ModalityType.APPLICATION_MODAL);
        inicializarComponentes(jFrame);
        inicializarVista();
        agregarListeners();
    }

    private void inicializarComponentes(JFrame jFrame) {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
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
        setLocationRelativeTo(jFrame);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(chartPanel, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void createChart() {
        chart = ChartFactory.createTimeSeriesChart("Pedido de clientes", "Fecha", "Monto", null, true, true, true);
        chartPanel = new ChartPanel(chart);
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    public void cerrar() {
        this.dispose();
    }

    private void inicializarVista() {
        XYPlot plot = (XYPlot) this.chart.getPlot();
        plot.setNoDataMessage("No data available");
        XYItemRenderer renderer = plot.getRenderer();
        //renderer.setSeriesPaint( 0 , Color.RED );
        /*renderer.setSeriesPaint( 1 , Color.GREEN );
        renderer.setSeriesPaint( 2 , Color.YELLOW );*/
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        /*renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
        renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );*/
        //plot.setRenderer(renderer);
        /*plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        plot.setLabelGap(0.02);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0} = {2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));*/
        this.chartPanel.setChart(this.chart);
        Date today = Calendar.getInstance().getTime();
        this.jddInicio.setDate(today);
        this.jddFin.setDate(today);
    }

    private void agregarListeners() {
        //ACTIONLISTENERS
        this.jbBorrar.addActionListener(this);
        this.jbBuscar.addActionListener(this);
        this.jbSalir.addActionListener(this);
        //KEYLISTENERS
        this.jbBorrar.addKeyListener(this);
        this.jbBuscar.addKeyListener(this);
        this.jbSalir.addKeyListener(this);
        this.jddFin.addKeyListener(this);
        this.jddInicio.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (this.jbBorrar.equals(source)) {
            borrar();
        } else if (this.jbBuscar.equals(source)) {
            buscar();
        } else if (this.jbSalir.equals(source)) {
            cerrar();
        }
    }

    private void borrar() {
        Date today = Calendar.getInstance().getTime();
        this.jddInicio.setDate(today);
        this.jddFin.setDate(today);
    }

    private void buscar() {
        Date inicio = this.jddInicio.getDate();
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(inicio);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 00);
        calendarInicio.set(Calendar.MINUTE, 00);
        calendarInicio.set(Calendar.SECOND, 00);
        calendarInicio.set(Calendar.MILLISECOND, 00);
        Date fin = this.jddFin.getDate();
        Calendar calendarFin = Calendar.getInstance();
        calendarFin.setTime(fin);
        calendarFin.set(Calendar.HOUR_OF_DAY, 23);
        calendarFin.set(Calendar.MINUTE, 59);
        calendarFin.set(Calendar.SECOND, 59);
        calendarFin.set(Calendar.MILLISECOND, 250);
        XYPlot plot = (XYPlot) this.chart.getPlot();
//        inicio= new Timestamp(calendarInicio.getTime().getTime());
        //      fin= new Timestamp(calendarFin.getTime().getTime());
        ArrayList idClientes = new ArrayList();
        idClientes.add(1);
        plot.setDataset(DB_charts.obtenerPedidoClientes(new Timestamp(calendarInicio.getTime().getTime()), new Timestamp(calendarFin.getTime().getTime()), idClientes));
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
