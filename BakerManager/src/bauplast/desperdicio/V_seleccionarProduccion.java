/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_seleccionarProduccion extends JDialog {

    JButton jbAceptar, jbSalir;
    JScrollPane jspProducto;
    JTable jtProducto;
    JPanel jpBotones;

    public V_seleccionarProduccion(JDialog main) {
        super(main, "Seleccionar producción", true);
        setSize(1200, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jspProducto, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void initComp() {
        jtProducto = new JTable();
        jtProducto.getTableHeader().setReorderingAllowed(false);
        jspProducto = new JScrollPane(jtProducto);
        jbAceptar = new JButton("Seleccionar producción");
        jbSalir = new JButton("Cerrar");
        jpBotones = new JPanel();
        jpBotones.add(jbAceptar);
        jpBotones.add(jbSalir);
    }
}
