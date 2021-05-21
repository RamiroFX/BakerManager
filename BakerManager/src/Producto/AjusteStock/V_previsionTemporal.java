/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_previsionTemporal extends JDialog {

    public JScrollPane jspCabecera, jspDetalle;
    public JTable jtCabecera, jtDetalle;
    public JButton jbVerDetalle, jbCrearAjuste, jbEliminarDetalle, jbSalir;
    private JPanel jpNorth, jpCenter, jpSouth;

    public V_previsionTemporal(JDialog dialog) {
        super(dialog, "Crear ajuste de stock", true);
        setSize(1200, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(dialog);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void initComponents() {
        jpNorth = new JPanel();

        jpCenter = new JPanel(new BorderLayout());
        jtCabecera = new JTable();
        jtCabecera.getTableHeader().setReorderingAllowed(false);
        jspCabecera = new JScrollPane(jtCabecera);
        jtDetalle = new JTable();
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jspDetalle = new JScrollPane(jtDetalle);
        JSplitPane jspp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspCabecera, jspDetalle);
        jspp.setOneTouchExpandable(true);
        jpCenter.add(jspp, BorderLayout.CENTER);

        jpSouth = new JPanel();
        jpSouth.setBorder(new EtchedBorder());
        jbVerDetalle = new JButton("Ver detalle");
        jbVerDetalle.setName("ver detalle provision");
        jbVerDetalle.setEnabled(false);
        jbCrearAjuste = new JButton("Crear ajuste");
        jbCrearAjuste.setName("crear inventario");
        jbCrearAjuste.setEnabled(false);
        jbEliminarDetalle = new JButton("Eliminar");
        jbEliminarDetalle.setName("eliminar provision");
        jbEliminarDetalle.setEnabled(false);
        jbSalir = new JButton("Salir");
        jpSouth.add(jbCrearAjuste);
        jpSouth.add(jbVerDetalle);
        jpSouth.add(jbEliminarDetalle);
        jpSouth.add(jbSalir);
    }
}
