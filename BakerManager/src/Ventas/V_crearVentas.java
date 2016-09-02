/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import bakermanager.V_inicio;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearVentas extends JDialog {

    public JTextField jtfFuncionario;
    public JFormattedTextField jftTiempo;
    public JScrollPane jspFactutraCabecera, jspFacturaDetalle;
    public JTable jtMesa, jtMesaDetalle;
    public JButton jbVerMesa, jbAñadirMesa, jbEliminarMesa;
    public JButton jbVentaRapida, jbSalir;
    private JPanel jpNorth, jpCenter, jpSouth;

    public V_crearVentas(V_inicio vista) {
        super(vista, "Ventas", true);
        setSize(1200, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(vista);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void initComponents() {
        jpNorth = new JPanel();
        jtfFuncionario = new JTextField();
        jtfFuncionario.setPreferredSize(new Dimension(250, 30));
        jtfFuncionario.setEditable(false);
        jtfFuncionario.setFocusable(false);
        jftTiempo = new JFormattedTextField(
                new DefaultFormatterFactory(
                new DateFormatter(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"))));
        jftTiempo.setFont(new java.awt.Font("Monospaced", 1, 14));
        jftTiempo.setFocusable(false);
        // setFecha(new Date());
        //jftFecha.setText("00/00/00");
        //jftFecha.setText(DateFormat.getDateInstance().format(getFecha()));
        jftTiempo.setEditable(false);
        jftTiempo.setPreferredSize(new Dimension(250, 30));
        jpNorth.add(new JLabel("Funcionario: "));
        jpNorth.add(jtfFuncionario);
        jpNorth.add(new JLabel("Tiempo: "));
        jpNorth.add(jftTiempo);

        jpCenter = new JPanel(new BorderLayout());
        jtMesa = new JTable();
        jtMesa.getTableHeader().setReorderingAllowed(false);
        jspFactutraCabecera = new JScrollPane(jtMesa);
        jtMesaDetalle = new JTable();
        jtMesaDetalle.getTableHeader().setReorderingAllowed(false);
        jspFacturaDetalle = new JScrollPane(jtMesaDetalle);
        JPanel jpFacturaCabecera = new JPanel(new MigLayout());
        jbVerMesa = new JButton("Ver mesa");
        jbAñadirMesa = new JButton("Añadir mesa");
        jbEliminarMesa = new JButton("Eliminar mesa");
        JPanel jpFacturaCabeceraBotones = new JPanel();
        jpFacturaCabeceraBotones.add(jbVerMesa);
        jpFacturaCabeceraBotones.add(jbAñadirMesa);
        jpFacturaCabeceraBotones.add(jbEliminarMesa);
        jpFacturaCabecera.add(jspFactutraCabecera, "dock center");
        jpFacturaCabecera.add(jpFacturaCabeceraBotones, "dock south");
        JSplitPane jspp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpFacturaCabecera, jspFacturaDetalle);
        jspp.setOneTouchExpandable(true);
        jpCenter.add(jspp, BorderLayout.CENTER);

        jpSouth = new JPanel();
        jpSouth.setBorder(new EtchedBorder());
        jbVentaRapida = new JButton("Venta rápida");
        jbSalir = new JButton("Salir");
        jpSouth.add(jbVentaRapida);
        jpSouth.add(jbSalir);
    }
}
