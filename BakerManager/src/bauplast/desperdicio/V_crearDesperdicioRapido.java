/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import Entities.E_produccionTipoBaja;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
class V_crearDesperdicioRapido extends JDialog {

    public static final String UPDATE_TITLE = "Ver/Modificar Baja de producción";
    public static final String JB_ADD_WASTE = "Agregar desperdicio",
            JB_ADD_WASTE_POST = "Agregar desperdicio Posterior",
            JB_UPDATE_WASTE = "Modificar desperdicio",
            JB_UPDATE_WASTE_POST = "Modificar desperdicio Posterior",
            JB_DELETE_WASTE = "Eliminar desperdicio",
            JB_DELETE_WASTE_POST = "Eliminar desperdicio Posterior",
            JB_ADD_RECOVERED_MATERIAL = "Agregar recuperado",
            JB_ADD_RECOVERED_MATERIAL_POST = "Agregar recuperado Posterior",
            JB_UPDATE_RECOVERED_MATERIAL = "Modificar recuperado",
            JB_UPDATE_RECOVERED_MATERIAL_POST = "Modificar recuperado Posterior",
            JB_DELETE_RECOVERED_MATERIAL = "Eliminar recuperado",
            JB_DELETE_RECOVERED_MATERIAL_POST = "Eliminar recuperado Posterior";

    //NORTE
    //norte 1
    JPanel jpNorth1;
    public JTextField jtfFuncionario, jtfObservacion;
    public JButton jbFuncionario;
    public JLabel jlFechaDesperdicio;
    public JDateChooser jdcFechaDesperdicio;
    public JComboBox<E_produccionTipoBaja> jcbTipoBaja;
    //CENTRO
    JPanel jpCenter;
    //MATERIA PRIMA
    public JTable jtBajaFilm;
    public JScrollPane jspDesperdicio;
    public JButton jbSeleccionarDesperdicio, jbModificarDesperdicio, jbEliminarDesperdicio;
    //SUR
    public JPanel jpSouth;
    public JButton jbAceptar, jbSalir;

    public V_crearDesperdicioRapido(JFrame frame) {
        super(frame, "Baja de producción", JDialog.ModalityType.APPLICATION_MODAL);
        setSize(900, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(frame);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth1, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void initComponents() {
        initNorth();
        initCenter();
        initSouth();
    }

    private void initNorth() {
        jpNorth1 = new JPanel(new MigLayout());
        jpNorth1.setBorder(new javax.swing.border.TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Datos de produccion", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
        jbFuncionario = new JButton("Responsable");
        jtfFuncionario = new JTextField(30);
        jtfFuncionario.setEditable(false);
        jlFechaDesperdicio = new JLabel("Fecha");
        jtfObservacion = new JTextField(30);
        jcbTipoBaja = new JComboBox<>();

        jdcFechaDesperdicio = new JDateChooser();
        jdcFechaDesperdicio.setPreferredSize(new Dimension(150, 20));
        jpNorth1.add(jbFuncionario);
        jpNorth1.add(jtfFuncionario);
        jpNorth1.add(jlFechaDesperdicio);
        jpNorth1.add(jdcFechaDesperdicio, "wrap");
        jpNorth1.add(new JLabel("Tipo de baja"));
        jpNorth1.add(jcbTipoBaja, "wrap");
        jpNorth1.add(new JLabel("Observación"));
        jpNorth1.add(jtfObservacion, "pushx, growx, spanx");
    }

    private void initCenter() {
        int width = 30, height = 10;
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));

        //ROLLOS UTILIZADOS
        jtBajaFilm = new JTable();
        jtBajaFilm.getTableHeader().setReorderingAllowed(false);
        jtBajaFilm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspDesperdicio = new JScrollPane(jtBajaFilm);
        jbSeleccionarDesperdicio = new JButton("Agregar");
        jbSeleccionarDesperdicio.setName(JB_ADD_RECOVERED_MATERIAL);
        jbSeleccionarDesperdicio.setSize(new Dimension(width, height));
        jbModificarDesperdicio = new JButton("Modificar");
        jbModificarDesperdicio.setName(JB_UPDATE_RECOVERED_MATERIAL);
        jbModificarDesperdicio.setSize(new Dimension(width, height));
        jbEliminarDesperdicio = new JButton("Eliminar");
        jbEliminarDesperdicio.setName(JB_DELETE_RECOVERED_MATERIAL);
        jbEliminarDesperdicio.setSize(new Dimension(width, height));
        JPanel jpSouthAuxRollos = new JPanel(new MigLayout());
        jpSouthAuxRollos.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAuxRollos.add(jbSeleccionarDesperdicio, "wrap");
        jpSouthAuxRollos.add(jbModificarDesperdicio, "growx, wrap");
        jpSouthAuxRollos.add(jbEliminarDesperdicio, "growx");

        JPanel jpCenterAux2 = new JPanel(new BorderLayout());
        jpCenterAux2.add(jspDesperdicio, BorderLayout.CENTER);
        jpCenterAux2.add(jpSouthAuxRollos, BorderLayout.EAST);

        jpCenter.add(jpCenterAux2, BorderLayout.CENTER);
    }

    private void initSouth() {
        jpSouth = new JPanel();
        jbAceptar = new JButton("Guardar cambios[F1]");
        jbSalir = new JButton("Salir[ESC]");
        jpSouth.add(jbAceptar);
        jpSouth.add(jbSalir);
    }
}
