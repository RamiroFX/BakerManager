/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Empleado;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_seleccionar_funcionario extends JDialog {

    JButton jbAceptar, jbCancelar;
    JScrollPane jspFuncionario;
    JTable jtFuncionario;
    JPanel jpBotones, jpNorth;
    JTextField jtfBuscar;
    JCheckBox jckbEntidadNombre, jckbCi;
    JRadioButton jrbExclusivo, jrbInclusivo;

    public V_seleccionar_funcionario(JFrame main) {
        super(main, "Seleccionar funcionario", true);
        setSize(600, 400);
        setLocationRelativeTo(main);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jspFuncionario, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    public V_seleccionar_funcionario(JDialog dialog) {
        super(dialog, "Seleccionar funcionario", true);
        setSize(600, 400);
        setLocationRelativeTo(dialog);
        initComp();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jspFuncionario, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void initComp() {
        jtFuncionario = new JTable();
        jtFuncionario.getTableHeader().setReorderingAllowed(false);
        jspFuncionario = new JScrollPane(jtFuncionario);
        jbAceptar = new JButton("Aceptar");
        jbCancelar = new JButton("Cancelar");
        jpBotones = new JPanel();
        jpBotones.add(jbAceptar);
        jpBotones.add(jbCancelar);
        jtfBuscar = new JTextField();
        jckbEntidadNombre = new JCheckBox("Entidad o nombre");
        jckbEntidadNombre.setSelected(true);
        jckbCi = new JCheckBox("R.U.C.");
        jckbCi.setSelected(true);
        jrbExclusivo = new javax.swing.JRadioButton("Exclusivo", true);
        jrbInclusivo = new javax.swing.JRadioButton("Inclusivo");
        javax.swing.ButtonGroup bg1 = new javax.swing.ButtonGroup();

        bg1.add(jrbExclusivo);
        bg1.add(jrbInclusivo);
        jpNorth = new JPanel(new MigLayout("align center"));
        String width = "width :100:";
        //C F
        jpNorth.add(jtfBuscar, "width :300:, cell 0 0");
        jpNorth.add(jckbEntidadNombre, width + ", cell 1 0");
        jpNorth.add(jckbCi, width + ", cell 1 1");
        jpNorth.add(jrbExclusivo, width + ", cell 0 1, span");
        jpNorth.add(jrbInclusivo, width + ", cell 0 1");
    }
}
