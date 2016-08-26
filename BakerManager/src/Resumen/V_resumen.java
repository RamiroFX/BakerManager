/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Resumen;

import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_resumen extends JDialog {

    public static final int PEDIDO = 1;
    public static final int EGRESO = 2;
    JScrollPane jspEgreso, jspDetalle;
    JTable jtResumen, jtDetalle;
    JButton jbSalir, jbImportarXLS;
    JLabel jlContado, jlCredito, jlTotal;
    JFormattedTextField jftTotalEgreso, jftTotalEgCont, jftTotalEgCred;
    Date inicio, fin;
    String proveedor_entidad, idEmpleado, tipo_operacion;
    Integer nro_factura;

    public V_resumen(JFrame jFrame) {
        super(jFrame, ModalityType.APPLICATION_MODAL);
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(jFrame);
        inicializarComponentes(1);
    }

    public V_resumen(JFrame jFrame, int tipo) {
        super(jFrame, ModalityType.APPLICATION_MODAL);
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(jFrame);
        inicializarComponentes(PEDIDO);
    }

    private void inicializarComponentes(int tipo) {
        jtResumen = new JTable();
        jtResumen.getTableHeader().setReorderingAllowed(false);
        jspEgreso = new JScrollPane(jtResumen);
        JPanel jpTotalEgreso = new JPanel(new GridLayout(3, 2));
        jftTotalEgreso = new JFormattedTextField();
        jftTotalEgreso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalEgCont = new JFormattedTextField();
        jftTotalEgCont.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalEgCred = new JFormattedTextField();
        jftTotalEgCred.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jlContado = new JLabel("Egresos al contado");
        jlContado.setHorizontalAlignment(SwingConstants.CENTER);
        jlCredito = new JLabel("Egresos a crédito");
        jlCredito.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotal = new JLabel("Total egresos");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalEgreso.add(jlContado);
        jpTotalEgreso.add(jftTotalEgCont);
        jpTotalEgreso.add(jlCredito);
        jpTotalEgreso.add(jftTotalEgCred);
        jpTotalEgreso.add(jlTotal);
        jpTotalEgreso.add(jftTotalEgreso);
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbSalir);
        jpCenter.add(jspEgreso, BorderLayout.CENTER);
        jpCenter.add(jpTotalEgreso, BorderLayout.SOUTH);
        switch (tipo) {
            case PEDIDO: {
                jtDetalle = new JTable();
                jtDetalle.getTableHeader().setReorderingAllowed(false);
                jspDetalle = new JScrollPane(jtDetalle);
                JTabbedPane jtp = new JTabbedPane();
                jtp.add("Cabecera", jpCenter);
                jtp.add("Detalle", jspDetalle);
                getContentPane().add(jtp, BorderLayout.CENTER);
                getContentPane().add(jpSouth, BorderLayout.SOUTH);
                break;
            }
            default: {
                getContentPane().add(jpCenter, BorderLayout.CENTER);
                getContentPane().add(jpSouth, BorderLayout.SOUTH);
                break;
            }
        }
    }

    private void inicializarBase() {
    }
}
