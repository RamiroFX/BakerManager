/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_generarInforme extends JDialog {

    private static final String JCBFECHA_STRING = "INCLUIR FECHA",
            JCBPRODUCTOSTERMINADOS_STRING = "Productos terminados",
            JCBROLLOSPRODUCIDOS_STRING = "Rollos producidos",
            JCBROLLOSUTILIZADOS_STRING = "Rollos utilizados",
            JCBROLLOSDISPONIBLES_STRING = "Rollos disponibles",
            JBFUNCIONARIO_STRING = "Funcionario",
            JCBRESPONSABLE_STRING = "Responsable",
            JCBGENERAR_STRING = "Generar",
            JCBSALIR_STRING = "Salir";
    public static final String CREATE_TITLE = "Informe de producción";
//            JTFFUNCIONARIO_STRING = "ROLLOS PRODUCIDOS",
//            JTFRESPONSABLE_STRING = "ROLLOS PRODUCIDOS",
//            JDCFECHAINICIO_STRING = "ROLLOS PRODUCIDOS",
//            JDCFECHAFINAL_STRING = "ROLLOS PRODUCIDOS";

    JCheckBox jcbFecha, jcbProductosTerminados, jcbRollosProducidos,
            jcbRollosUtilizados, jcbRollosDisponibles;
    JButton jbFuncionario, jbResponsable, jbGenerar, jbSalir;
    JTextField jtfFuncionario, jtfResponsable;
    JDateChooser jdcFechaInicio, jdcFechaFinal;
    JPanel jpSur, jpNorte, jpCenter;

    public V_generarInforme(JFrame vista) {
        super(vista);
        initializeComponents();
        constructLayout();
        constructAppWindow();
    }

    private void initializeComponents() {
        jcbFecha = new JCheckBox(JCBFECHA_STRING);
        jcbFecha.setSelected(true);
        jcbProductosTerminados = new JCheckBox(JCBPRODUCTOSTERMINADOS_STRING);
        jcbRollosProducidos = new JCheckBox(JCBROLLOSPRODUCIDOS_STRING);
        jcbRollosUtilizados = new JCheckBox(JCBROLLOSUTILIZADOS_STRING);
        jcbRollosDisponibles = new JCheckBox(JCBROLLOSDISPONIBLES_STRING);
        jbFuncionario = new JButton(JBFUNCIONARIO_STRING);
        jbResponsable = new JButton(JCBRESPONSABLE_STRING);
        jbGenerar = new JButton(JCBGENERAR_STRING);
        jbSalir = new JButton(JCBSALIR_STRING);
        jtfFuncionario = new JTextField();
        jtfResponsable = new JTextField();
        jdcFechaInicio = new JDateChooser();
        jdcFechaFinal = new JDateChooser();
        jpSur = new JPanel();
        jpSur.add(jbGenerar);
        jpSur.add(jbSalir);
        JPanel jpFecha = new JPanel(new MigLayout());
        jpFecha.add(jcbFecha);
        jpFecha.add(jdcFechaInicio);
        jpFecha.add(jdcFechaFinal);
        jpCenter = new JPanel(new MigLayout());
        jpCenter.add(jbFuncionario);
        jpCenter.add(jtfFuncionario, "wrap");
        jpCenter.add(jbResponsable);
        jpCenter.add(jtfResponsable, "wrap");
        jpCenter.add(jcbProductosTerminados, "wrap");
        jpCenter.add(jcbRollosProducidos, "wrap");
        jpCenter.add(jcbRollosUtilizados, "wrap");
        jpCenter.add(jcbRollosDisponibles, "wrap");
    }

    private void constructLayout() {
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSur, BorderLayout.SOUTH);

    }

    private void constructAppWindow() {
        setTitle(CREATE_TITLE);
        setName("jdInformeProduccion");
        setSize(new Dimension(430, 430));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setModal(true);
    }
}
