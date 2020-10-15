/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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

    public static final String UPDATE_TITLE = "Ver/Modificar Desperdicio";
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
    public JLabel jlFechaProduccion;
    public JDateChooser jdcFechaEntrega;
    //CENTRO
    JSplitPane jSplitTablas;
    JPanel jpCenter;
    public JTable jtProduccionDesperdicio;
    public JScrollPane jspProduccionDesperdicio;
    public JButton jbSeleccionarDesperdicio, jbModificarDesperdicio, jbEliminarDesperdicio;
    //MATERIA PRIMA
    public JTable jtDesperdicioRecuperado;
    public JScrollPane jspDesperdicioRecuperado;
    public JButton jbSeleccionarRecuperado, jbModificarRecuperado, jbEliminarRecuperado;
    //SUR
    public JPanel jpSouth;
    public JButton jbAceptar, jbSalir;

    public V_crearDesperdicioRapido(JFrame frame) {
        super(frame, "Crear desperdicio", JDialog.ModalityType.APPLICATION_MODAL);
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
        jlFechaProduccion = new JLabel("Fecha");
        jtfObservacion = new JTextField(30);

        jdcFechaEntrega = new JDateChooser();
        jdcFechaEntrega.setPreferredSize(new Dimension(150, 20));
        jpNorth1.add(jbFuncionario);
        jpNorth1.add(jtfFuncionario);
        jpNorth1.add(jlFechaProduccion);
        jpNorth1.add(jdcFechaEntrega, "wrap");
        jpNorth1.add(new JLabel("Observación"));
        jpNorth1.add(jtfObservacion);
    }

    private void initCenter() {
        int width = 30, height = 10;
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtProduccionDesperdicio = new JTable();
        this.jtProduccionDesperdicio.getTableHeader().setReorderingAllowed(false);
        jtProduccionDesperdicio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspProduccionDesperdicio = new JScrollPane(jtProduccionDesperdicio);
        jbSeleccionarDesperdicio = new JButton("Agregar desperdicio[F4]");
        jbSeleccionarDesperdicio.setName(JB_ADD_WASTE);
        jbModificarDesperdicio = new JButton("Modificar desperdicio");
        jbModificarDesperdicio.setName(JB_UPDATE_WASTE);
        jbEliminarDesperdicio = new JButton("Eliminar desperdicio");
        jbEliminarDesperdicio.setName(JB_DELETE_WASTE);
        JPanel jpSouthAux = new JPanel(new MigLayout());
        jpSouthAux.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAux.add(jbSeleccionarDesperdicio, "wrap");
        jpSouthAux.add(jbModificarDesperdicio, "growx, wrap");
        jpSouthAux.add(jbEliminarDesperdicio, "growx");

        //ROLLOS UTILIZADOS
        jtDesperdicioRecuperado = new JTable();
        jtDesperdicioRecuperado.getTableHeader().setReorderingAllowed(false);
        jtDesperdicioRecuperado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspDesperdicioRecuperado = new JScrollPane(jtDesperdicioRecuperado);
        jbSeleccionarRecuperado = new JButton("Agregar recuperado");
        jbSeleccionarRecuperado.setName(JB_ADD_RECOVERED_MATERIAL);
        jbSeleccionarRecuperado.setSize(new Dimension(width, height));
        jbModificarRecuperado = new JButton("Modificar recuperado");
        jbModificarRecuperado.setName(JB_UPDATE_RECOVERED_MATERIAL);
        jbModificarRecuperado.setSize(new Dimension(width, height));
        jbEliminarRecuperado = new JButton("Eliminar recuperado");
        jbEliminarRecuperado.setName(JB_DELETE_RECOVERED_MATERIAL);
        jbEliminarRecuperado.setSize(new Dimension(width, height));
        JPanel jpSouthAuxRollos = new JPanel(new MigLayout());
        jpSouthAuxRollos.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAuxRollos.add(jbSeleccionarRecuperado, "wrap");
        jpSouthAuxRollos.add(jbModificarRecuperado, "growx, wrap");
        jpSouthAuxRollos.add(jbEliminarRecuperado, "growx");

        JPanel jpCenterAux1 = new JPanel(new BorderLayout());
        jpCenterAux1.add(jspProduccionDesperdicio, BorderLayout.CENTER);
        jpCenterAux1.add(jpSouthAux, BorderLayout.EAST);

        JPanel jpCenterAux2 = new JPanel(new BorderLayout());
        jpCenterAux2.add(jspDesperdicioRecuperado, BorderLayout.CENTER);
        jpCenterAux2.add(jpSouthAuxRollos, BorderLayout.EAST);

        jSplitTablas = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jpCenterAux1, jpCenterAux2);
        jSplitTablas.setDividerLocation(300);
        jpCenter.add(jSplitTablas, BorderLayout.CENTER);
    }

    private void initSouth() {
        jpSouth = new JPanel();
        jbAceptar = new JButton("Guardar cambios[F1]");
        jbSalir = new JButton("Salir[ESC]");
        jpSouth.add(jbAceptar);
        jpSouth.add(jbSalir);
    }
}
