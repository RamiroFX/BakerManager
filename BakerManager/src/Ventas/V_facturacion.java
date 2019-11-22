/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import bakermanager.C_inicio;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_facturacion extends JDialog {

    private static final String TITULO = "Facturación";
    public static final String TOTAL_ITEMS = "Items seleccionados = ";

    JTextField jtfTotalItems;
    JPanel jpCenter, jpSouth;
    JTable jtVentasCabecera, jtVentasDetalle;
    JSplitPane jspCenter;
    JButton jbQuitar, jbAgregar, jbFacturar, jbCancelar;

    public V_facturacion(C_inicio c_inicio) {
        super(c_inicio.vista, TITULO, false);
        setSize(1024, 600);
        setLocationRelativeTo(c_inicio.vista);
        inicializarVista();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void inicializarVista() {
        /*
         * VENTAS CABECERA
         */
        this.jtVentasCabecera = new JTable();
        JScrollPane jspVentasCabecera = new JScrollPane(this.jtVentasCabecera);
        this.jbAgregar = new JButton("Agregar");
        this.jbQuitar = new JButton("Quitar");
        JPanel jpBotones = new JPanel();
        jpBotones.add(jbAgregar);
        jpBotones.add(jbQuitar);
        JPanel jpVentasCabecera = new JPanel(new BorderLayout());
        jpVentasCabecera.add(jspVentasCabecera, BorderLayout.CENTER);
        jpVentasCabecera.add(jpBotones, BorderLayout.SOUTH);
        /*
         * VENTAS DETALLE
         */
        this.jtfTotalItems = new JTextField();
        this.jtfTotalItems.setHorizontalAlignment(JTextField.CENTER);
        this.jtVentasDetalle = new JTable();
        JScrollPane jspVentasDetalle = new JScrollPane(this.jtVentasDetalle);
        JPanel jpVentasDetalle = new JPanel(new BorderLayout());
        jpVentasDetalle.add(jspVentasDetalle, BorderLayout.CENTER);
        jpVentasDetalle.add(jtfTotalItems, BorderLayout.SOUTH);
        /*
         * SPLIT PANE
         */
        this.jspCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpVentasCabecera, jpVentasDetalle);
        jspCenter.setDividerLocation(this.getWidth() / 2);
        jspCenter.setOneTouchExpandable(true);

        this.jpCenter = new JPanel();
        this.jpCenter.setLayout(new BorderLayout());
        this.jpCenter.setBorder(javax.swing.BorderFactory.createTitledBorder("Ventas"));
        this.jpCenter.add(jspCenter, BorderLayout.CENTER);
        /*
         * BOTONES
         */
        this.jbFacturar = new JButton("Facturar");
        this.jbCancelar = new JButton("Cancelar");
        this.jpSouth = new JPanel();
        this.jpSouth.add(jbFacturar);
        this.jpSouth.add(jbCancelar);
    }

}
