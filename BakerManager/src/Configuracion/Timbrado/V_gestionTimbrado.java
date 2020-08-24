/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import Entities.Estado;
import bakermanager.V_inicio;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_gestionTimbrado extends JDialog {

    public JButton jbBuscar, jbBorrar, jbSalir, jbNueva, jbDetalle, jbAnular;
    public JTextField jtfNroTimbrado;
    public JComboBox<Estado> jcbEstado;
    public JComboBox jcbFecha;
    public JCheckBox jcbActivarFecha;
    private JPanel jpTop, jpBotonesTop, jpBot;
    public JTable jtCabecera;
    public JDateChooser jddInicio, jddFinal;
    private JScrollPane jspTimbrados;

    public V_gestionTimbrado(V_inicio inicio) {
        super(inicio, "Gestión de timbrados");
        setSize(1100, 600);
        setName("jdGestionTimbrados");
        initTop();
        initMid();
        initBot();
        getContentPane().add(jpTop, "North");
        getContentPane().add(jspTimbrados, "Center");
        getContentPane().add(jpBot, "South");
        setLocationRelativeTo(null);
    }

    private void initTop() {
        jpTop = new JPanel(new MigLayout("", "[fill][fill]", "[fill][]"));
        JPanel jpFiltros = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[grow][][grow]", // Column constraints
                "[][shrink 0]"));    // Row constraints);
        jcbActivarFecha = new JCheckBox("Buscar por fechas");
        jcbFecha = new JComboBox();
        jcbEstado = new JComboBox();
        jtfNroTimbrado = new JTextField();
        jddInicio = new JDateChooser();
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser();
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jpFiltros.add(jcbActivarFecha, "growx");
        jpFiltros.add(jcbFecha, "growx");
        jpFiltros.add(new JLabel("Fecha inicio:"));
        jpFiltros.add(jddInicio, "growx");
        jpFiltros.add(new JLabel("Fecha final:"));
        jpFiltros.add(jddFinal, "growx, wrap");
        jpFiltros.add(new JLabel("Estado:"));
        jpFiltros.add(jcbEstado, "growx");
        jpFiltros.add(new JLabel("Nro. timbrado:"));
        jpFiltros.add(jtfNroTimbrado, "growx");
        jpBotonesTop = new JPanel(new MigLayout());
        jpBotonesTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jbBuscar = new JButton("Buscar");
        jbBuscar.setName("buscar timbrado venta");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar, "wrap");
        jpBotonesTop.add(jbBorrar);
        jpTop.add(jpFiltros);
        jpTop.add(jpBotonesTop);
        jpTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Filtro de busqueda"));
    }

    private void initMid() {
        jtCabecera = new JTable();
        jtCabecera.getTableHeader().setReorderingAllowed(false);
        jspTimbrados = new JScrollPane(jtCabecera);
    }

    private void initBot() {
        jpBot = new JPanel();
        jbNueva = new JButton("Crear timbrado");
        jbNueva.setName("crear timbrado venta");
        jbAnular = new JButton("Anular");
        jbAnular.setName("anular timbrado venta");
        jbDetalle = new JButton("Detalle");
        jbDetalle.setName("detalle timbrado venta");
        jbSalir = new JButton("Salir");
        jpBot.add(jbNueva);
        jpBot.add(jbAnular);
        jpBot.add(jbDetalle);
        jpBot.add(jbSalir);
        jpBot.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Opciones"));
    }
}
