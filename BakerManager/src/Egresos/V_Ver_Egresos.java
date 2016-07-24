/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author Ramiro Ferreira
 */
class V_Ver_Egresos extends JDialog{

    public javax.swing.JButton jbSalir;
    private javax.swing.JLabel jlProveedor;
    private javax.swing.JPanel jpNorth;
    public javax.swing.JPanel jpSouth;
    private javax.swing.JScrollPane jspProductos;
    public javax.swing.JTable jtProductos;
    public javax.swing.JTextField jtfProveedor;
    public javax.swing.JFormattedTextField jftTotal;

    public V_Ver_Egresos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Egresos");
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        setSize(new java.awt.Dimension(800, 600));
        
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        jpNorth = new javax.swing.JPanel();
        jlProveedor = new javax.swing.JLabel();
        jtfProveedor = new javax.swing.JTextField();
        jspProductos = new javax.swing.JScrollPane();
        jtProductos = new javax.swing.JTable();
        jpSouth = new javax.swing.JPanel();
        jbSalir = new javax.swing.JButton();
        jftTotal = new javax.swing.JFormattedTextField();
        jftTotal.setColumns(20);
        jlProveedor.setText("Proveedor");
        jpNorth.add(jlProveedor);

        jtfProveedor.setEditable(false);
        jtfProveedor.setColumns(20);
        jpNorth.add(jtfProveedor);

        jspProductos.setViewportView(jtProductos);

        jpSouth.add(new JLabel("Total"));
        jpSouth.add(jftTotal);

        jbSalir.setText("Salir");
        jpSouth.add(jbSalir);

        getContentPane().add(jpNorth, java.awt.BorderLayout.NORTH);
        getContentPane().add(jspProductos, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);
    }
}
