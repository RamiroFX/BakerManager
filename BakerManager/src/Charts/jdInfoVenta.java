/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Administrador
 */
public class jdInfoVenta extends JDialog  {
    private JLabel jlVentaEfectivo,jlVentaCredito,jlTotalIngreso,jlEgreso,jlDiferencia,jlFondoFijo,jlTotalVenta,jlTotalCobro,jlTotalEgreso,jlTotalEfectivo,jlDepositar;
    public JFormattedTextField jtfVentaEfectivo,jtfVentaCredito,jtfTotalIngreso,jtfEgreso,jtfDiferencia,jtfFondoFijo,jtfTotalVenta,jtfTotalCobro,jtfTotalEgreso,jtfTotalEfectivo,jtfDepositar;
    public JButton jbImprimir,jbSalir;
    private JPanel jpIngreso,jpEgreso,jpDiferencia,jpResumen,jpBotones,
            jpContenedor2,jpContenedor1;
    private Font fuente;
    private JTabbedPane jtpPaneles;
    public jdInfoVenta(JFrame frame) {
        super(frame, "Informe de Ventas", ModalityType.APPLICATION_MODAL);
        initComp();
        getContentPane().add(jtpPaneles);
        setName("jdInfoVenta");
    }
    private void initComp(){
        jtpPaneles = new JTabbedPane();
        fuente = new Font("Times New Roman", Font.BOLD, 15);
        jlVentaEfectivo=new JLabel("Venta efectivo");
        jlVentaEfectivo.setHorizontalAlignment(JLabel.CENTER);
        jlVentaEfectivo.setFont(fuente);
        jlVentaCredito=new JLabel("Venta credito");
        jlVentaCredito.setHorizontalAlignment(JLabel.CENTER);
        jlVentaCredito.setFont(fuente);
        jlTotalIngreso=new JLabel("Total Ingreso");
        jlTotalIngreso.setHorizontalAlignment(JLabel.CENTER);
        jlTotalIngreso.setFont(fuente);
        jlEgreso=new JLabel("Egreso");
        jlEgreso.setHorizontalAlignment(JLabel.CENTER);
        jlEgreso.setFont(fuente);
        jlTotalEgreso=new JLabel("Total egreso");
        jlTotalEgreso.setHorizontalAlignment(JLabel.CENTER);
        jlTotalEgreso.setFont(fuente);
        jlDiferencia=new JLabel("Diferencia");
        jlDiferencia.setHorizontalAlignment(JLabel.CENTER);
        jlDiferencia.setFont(fuente);
        jlFondoFijo=new JLabel("Fondo fijo");
        jlFondoFijo.setHorizontalAlignment(JLabel.CENTER);
        jlFondoFijo.setFont(fuente);
        jlTotalVenta=new JLabel("Total venta");
        jlTotalVenta.setHorizontalAlignment(JLabel.CENTER);
        jlTotalVenta.setFont(fuente);
        jlTotalCobro=new JLabel("Total cobro");
        jlTotalCobro.setHorizontalAlignment(JLabel.CENTER);
        jlTotalCobro.setFont(fuente);
        jlTotalEfectivo=new JLabel("Total efectivo");
        jlTotalEfectivo.setHorizontalAlignment(JLabel.CENTER);
        jlTotalEfectivo.setFont(fuente);
        jlDepositar=new JLabel("Total a rendir");
        jlDepositar.setHorizontalAlignment(JLabel.CENTER);
        jlDepositar.setFont(fuente);

        java.text.NumberFormat df = java.text.NumberFormat.getIntegerInstance();
        NumberFormatter nf = new NumberFormatter(df);
        DefaultFormatterFactory dff= new DefaultFormatterFactory(nf);
        jtfVentaEfectivo=new JFormattedTextField(dff);
        jtfVentaEfectivo.setFont(fuente);
        jtfVentaCredito=new JFormattedTextField(dff);
        jtfVentaCredito.setFont(fuente);
        jtfTotalIngreso=new JFormattedTextField(dff);
        jtfTotalIngreso.setFont(fuente);
        jtfEgreso=new JFormattedTextField(dff);
        jtfEgreso.setFont(fuente);
        jtfDiferencia=new JFormattedTextField(dff);
        jtfDiferencia.setColumns(20);
        jtfDiferencia.setFont(fuente);
        jtfFondoFijo=new JFormattedTextField(dff);
        jtfFondoFijo.setFont(fuente);
        jtfTotalVenta=new JFormattedTextField(dff);
        jtfTotalVenta.setFont(fuente);
        jtfTotalCobro=new JFormattedTextField(dff);
        jtfTotalCobro.setFont(fuente);
        jtfTotalEgreso=new JFormattedTextField(dff);
        jtfTotalEgreso.setFont(fuente);
        jtfTotalEfectivo=new JFormattedTextField(dff);
        jtfTotalEfectivo.setFont(fuente);
        jtfDepositar=new JFormattedTextField(dff);
        jtfDepositar.setFont(fuente);

        jbImprimir=new JButton("Imprimir");
        jbSalir=new JButton("Salir");
        jpIngreso=new JPanel(new GridLayout(4, 2));
        jpIngreso.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jpIngreso.add(jlVentaEfectivo);
        jpIngreso.add(jtfVentaEfectivo);
        jpIngreso.add(jlVentaCredito);
        jpIngreso.add(jtfVentaCredito);
        jpIngreso.add(jlTotalIngreso);
        jpIngreso.add(jtfTotalIngreso);
        jpEgreso=new JPanel(new GridLayout(4, 2));
        jpEgreso.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jpEgreso.add(jlEgreso);
        jpEgreso.add(jtfEgreso);
        jpEgreso.add(jlTotalEgreso);
        jpEgreso.add(jtfTotalEgreso);
        jpEgreso.add(new Component() {});
        jpEgreso.add(new Component() {});
        jpDiferencia=new JPanel();
        jpDiferencia.add(jlDiferencia);
        jpDiferencia.add(jtfDiferencia);
        jpResumen=new JPanel(new GridLayout(5, 2));
        jpResumen.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jpResumen.add(jlFondoFijo);
        jpResumen.add(jtfFondoFijo);
        jpResumen.add(jlTotalVenta);
        jpResumen.add(jtfTotalVenta);
        jpResumen.add(jlTotalCobro);
        jpResumen.add(jtfTotalCobro);
        jpResumen.add(jlTotalEfectivo);
        jpResumen.add(jtfTotalEfectivo);
        jpResumen.add(jlDepositar);
        jpResumen.add(jtfDepositar);


        jpBotones=new JPanel();
        jpBotones.add(jbImprimir);
        jpBotones.add(jbSalir);

        jpContenedor1= new JPanel(new GridLayout(1, 2));
        jpContenedor1.add(jpIngreso);
        jpContenedor1.add(jpEgreso);
        jpContenedor2= new JPanel(new BorderLayout());
        jpContenedor2.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jpContenedor2.add(jpDiferencia,"North");
        jpContenedor2.add(jpResumen,"Center");
        jpContenedor2.add(jpBotones,"South");

        JPanel jp= new JPanel(new GridLayout(2, 1));
        jp.add(jpContenedor1);
        jp.add(jpContenedor2);
        JPanel jp2= new JPanel();
        jp2.add(new JLabel(freeChart(new Dimension(400, 400))));
        jtpPaneles.add("Resumen", jp);
        jtpPaneles.add("Grafico de ventas", jp2);
        
    }

    private ImageIcon freeChart(Dimension d){
        ImageIcon chart = new ImageIcon();
         //se declara el grafico XY Lineal
        XYDataset xydataset = xyDataset();
        JFreeChart jfreechart = ChartFactory.createXYLineChart(
        "Ingresos del día" , "Tiempo", "Ingresos",
        xydataset, PlotOrientation.VERTICAL,  true, true, false);

        //personalización del grafico
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setBackgroundPaint( Color.white );
        xyplot.setDomainGridlinePaint( Color.BLACK );
        xyplot.setRangeGridlinePaint( Color.BLACK );
        // -> Pinta Shapes en los puntos dados por el XYDataset
        XYLineAndShapeRenderer xylineandshaperenderer =  (XYLineAndShapeRenderer) xyplot.getRenderer();
        xylineandshaperenderer.setBaseShapesVisible(true);
        //--> muestra los valores de cada punto XY
        XYItemLabelGenerator xy = new StandardXYItemLabelGenerator();
        xylineandshaperenderer.setBaseItemLabelGenerator( xy );
        xylineandshaperenderer.setBaseItemLabelsVisible(true);
	xylineandshaperenderer.setBaseLinesVisible(true);
	xylineandshaperenderer.setBaseItemLabelsVisible(true);
        //fin de personalización

        //se crea la imagen y se asigna a la clase ImageIcon
        BufferedImage bufferedImage  = jfreechart.createBufferedImage( d.width, d.height);
        chart.setImage(bufferedImage);
        return chart;
    }

    /**
     * Datos
     */
    private XYDataset xyDataset()
    {
        //se declaran las series y se llenan los datos
        XYSeries sIngresos = new XYSeries("Ingresos");
        XYSeries sTiempo = new XYSeries("Tiempo");
        //serie #1
        sIngresos.add( 1, 340);
        sIngresos.add( 2, 210);
        sIngresos.add( 3, 410);
        sIngresos.add( 4, 200);
        sIngresos.add( 5, 525);
        sIngresos.add( 6, 492);
        sIngresos.add( 7, 390);
        //serie #2
        sTiempo.add( 1, 90);
        sTiempo.add( 2, 434);
        sTiempo.add( 3, 741);
        sTiempo.add( 4, 91);
        sTiempo.add( 5, 412);
        sTiempo.add( 6, 361);
        sTiempo.add( 7, 271);

        XYSeriesCollection xyseriescollection =  new XYSeriesCollection();
        xyseriescollection.addSeries( sIngresos );
        xyseriescollection.addSeries( sTiempo );

        return xyseriescollection;
    }
}
