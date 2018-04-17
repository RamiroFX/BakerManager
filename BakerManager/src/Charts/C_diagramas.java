/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Charts;

import Egresos.C_gestionEgresos;
import bakermanager.C_inicio;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_diagramas implements ActionListener, KeyListener {

    public M_diagramas modelo;
    public V_diagramas vista;
    public C_inicio c_inicio;
    private C_gestionEgresos gestionEgreso;

    public C_diagramas(C_inicio c_inicio, M_diagramas modelo, V_diagramas vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        agregarListeners();
    }

    public C_diagramas(M_diagramas modelo, V_diagramas vista, C_inicio c_inicio, C_gestionEgresos gestionEgreso) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        this.gestionEgreso = gestionEgreso;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    public void cerrar() {
        this.vista.dispose();
    }

    private void inicializarVista() {
        PiePlot plot = (PiePlot) this.vista.chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        plot.setLabelGap(0.02);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0} = {2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
        this.vista.chartPanel.setChart(this.vista.chart);
        Date today = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(today);
        this.vista.jddFin.setDate(today);
    }

    private void agregarListeners() {
        //ACTIONLISTENERS
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        //KEYLISTENERS
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jddFin.addKeyListener(this);
        this.vista.jddInicio.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (this.vista.jbBorrar.equals(source)) {
            borrar();
        } else if (this.vista.jbBuscar.equals(source)) {
            buscar();
        } else if (this.vista.jbSalir.equals(source)) {
            cerrar();
        }
    }

    private void borrar() {
        Date today = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(today);
        this.vista.jddFin.setDate(today);
    }

    private void buscar() {
        Date inicio = this.vista.jddInicio.getDate();
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(inicio);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 00);
        calendarInicio.set(Calendar.MINUTE, 00);
        calendarInicio.set(Calendar.SECOND, 00);
        calendarInicio.set(Calendar.MILLISECOND, 00);
        Date fin = this.vista.jddFin.getDate();
        Calendar calendarFin = Calendar.getInstance();
        calendarFin.setTime(fin);
        calendarFin.set(Calendar.HOUR_OF_DAY, 23);
        calendarFin.set(Calendar.MINUTE, 59);
        calendarFin.set(Calendar.SECOND, 59);
        calendarFin.set(Calendar.MILLISECOND, 250);
        PiePlot plot = (PiePlot) this.vista.chart.getPlot();
        plot.setDataset(this.modelo.establecerPieDataset(new Timestamp(calendarInicio.getTime().getTime()), new Timestamp(calendarFin.getTime().getTime())));
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
