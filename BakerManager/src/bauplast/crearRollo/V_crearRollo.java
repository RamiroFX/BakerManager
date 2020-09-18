/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearRollo;

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
class V_crearRollo extends JDialog {

    public static final String JB_ADD_FILM = "Agregar bobina",
            JB_ADD_FILM_POST = "Agregar bobina Posterior",
            JB_UPDATE_FILM = "Modificar bobina",
            JB_UPDATE_FILM_POST = "Modificar bobina Posterior",
            JB_DELETE_FILM = "Eliminar bobina",
            JB_DELETE_FILM_POST = "Eliminar bobina Posterior",
            JB_ADD_RAW_MATERIAL = "Agregar Materia Prima",
            JB_ADD_RAW_MATERIAL_POST = "Agregar Materia Prima Posterior",
            JB_UPDATE_RAW_MATERIAL = "Modificar Materia Prima",
            JB_UPDATE_RAW_MATERIAL_POST = "Modificar Materia Prima Posterior",
            JB_DELETE_RAW_MATERIAL = "Eliminar Materia Prima",
            JB_DELETE_RAW_MATERIAL_POST = "Eliminar Materia Prima Posterior";

    //NORTE
    //norte 1
    JPanel jpNorth1;
    public JTextField jtfFuncionario, jtfNroOrdenTrabajo;
    public JButton jbFuncionario;
    public JLabel jlNroOrdenTrabajo, jlFechaProduccion;
    public JDateChooser jdcFechaEntrega;
    //CENTRO
    JSplitPane jSplitTablas;
    JPanel jpCenter;
    public JTable jtProduccionDetalle;
    public JScrollPane jspProduccionDetalle;
    public JButton jbSeleccionarProducto, jbModificarDetalle, jbEliminarDetalle;
    //MATERIA PRIMA
    public JTable jtMateriaPrimaUtilizada;
    public JScrollPane jspMateriaPrimaUtilizada;
    public JButton jbSeleccionarMP, jbModificarMP, jbEliminarMP;
    //SUR
    public JPanel jpSouth;
    public JButton jbAceptar, jbSalir;

    public V_crearRollo(JFrame frame) {
        super(frame, "Crear Rollo", JDialog.ModalityType.APPLICATION_MODAL);
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
        jbFuncionario = new JButton("Responsable[F3]");
        jtfFuncionario = new JTextField(30);
        jtfFuncionario.setEditable(false);
        jlNroOrdenTrabajo = new JLabel("Nro. Orden de trabajo");
        jlFechaProduccion = new JLabel("Fecha");
        jtfNroOrdenTrabajo = new JTextField(30);

        jdcFechaEntrega = new JDateChooser();
        jdcFechaEntrega.setPreferredSize(new Dimension(150, 20));
        jpNorth1.add(jbFuncionario);
        jpNorth1.add(jtfFuncionario);
        jpNorth1.add(jlNroOrdenTrabajo);
        jpNorth1.add(jtfNroOrdenTrabajo, "wrap");
        jpNorth1.add(jlFechaProduccion);
        jpNorth1.add(jdcFechaEntrega);
    }

    private void initCenter() {
        int width = 30, height = 10;
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtProduccionDetalle = new JTable();
        this.jtProduccionDetalle.getTableHeader().setReorderingAllowed(false);
        jtProduccionDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspProduccionDetalle = new JScrollPane(jtProduccionDetalle);
        jbSeleccionarProducto = new JButton("Agregar producto[F4]");
        jbSeleccionarProducto.setName(JB_ADD_FILM);
        jbModificarDetalle = new JButton("Modificar detalle");
        jbModificarDetalle.setName(JB_UPDATE_FILM);
        jbEliminarDetalle = new JButton("Eliminar detalle");
        jbEliminarDetalle.setName(JB_DELETE_FILM);
        JPanel jpSouthAux = new JPanel(new MigLayout());
        jpSouthAux.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAux.add(jbSeleccionarProducto, "wrap");
        jpSouthAux.add(jbModificarDetalle, "growx, wrap");
        jpSouthAux.add(jbEliminarDetalle, "growx");

        //ROLLOS UTILIZADOS
        jtMateriaPrimaUtilizada = new JTable();
        jtMateriaPrimaUtilizada.getTableHeader().setReorderingAllowed(false);
        jtMateriaPrimaUtilizada.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspMateriaPrimaUtilizada = new JScrollPane(jtMateriaPrimaUtilizada);
        jbSeleccionarMP = new JButton(JB_ADD_RAW_MATERIAL);
        jbSeleccionarMP.setName(JB_ADD_RAW_MATERIAL);
        jbSeleccionarMP.setSize(new Dimension(width, height));
        jbModificarMP = new JButton(JB_UPDATE_RAW_MATERIAL);
        jbModificarMP.setName(JB_UPDATE_RAW_MATERIAL);
        jbModificarMP.setSize(new Dimension(width, height));
        jbEliminarMP = new JButton(JB_DELETE_RAW_MATERIAL);
        jbEliminarMP.setName(JB_DELETE_RAW_MATERIAL);
        jbEliminarMP.setSize(new Dimension(width, height));
        JPanel jpSouthAuxRollos = new JPanel(new MigLayout());
        jpSouthAuxRollos.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAuxRollos.add(jbSeleccionarMP, "wrap");
        jpSouthAuxRollos.add(jbModificarMP, "growx, wrap");
        jpSouthAuxRollos.add(jbEliminarMP, "growx");

        JPanel jpCenterAux1 = new JPanel(new BorderLayout());
        jpCenterAux1.add(jspProduccionDetalle, BorderLayout.CENTER);
        jpCenterAux1.add(jpSouthAux, BorderLayout.EAST);

        JPanel jpCenterAux2 = new JPanel(new BorderLayout());
        jpCenterAux2.add(jspMateriaPrimaUtilizada, BorderLayout.CENTER);
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
