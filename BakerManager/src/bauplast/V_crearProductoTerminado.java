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
class V_crearProductoTerminado extends JDialog {

    //NORTE
    //norte 1
    JPanel jpNorth1;
    public JTextField jtfFuncionario, jtfNroOrdenTrabajo;
    public JButton jbFuncionario;
    public JLabel jlNroOrdenTrabajo, jlFechaProduccion;
    public JDateChooser jdcFechaEntrega;
    //CENTRO
    JPanel jpCenter;
    JSplitPane jSplitTablas;
    //PRODUCTOS TERMINADOS
    public JTable jtProduccionDetalle;
    public JScrollPane jspProduccionDetalle;
    public JButton jbSeleccionarProducto, jbModificarProducto, jbEliminarProducto;
    //ROLLOS UTILIZADOS
    public JTable jtRolloUtilizado;
    public JScrollPane jspRolloUtilizado;
    public JButton jbSeleccionarRollo, jbModificarRollo, jbEliminarRollo;
    //SUR
    public JPanel jpSouth;
    public JButton jbAceptar, jbSalir;

    public V_crearProductoTerminado(JFrame frame) {
        super(frame, "Crear Producto terminado", JDialog.ModalityType.APPLICATION_MODAL);
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
        //PRODUCTOS TERMINADOS
        jtProduccionDetalle = new JTable();
        jtProduccionDetalle.getTableHeader().setReorderingAllowed(false);
        jtProduccionDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspProduccionDetalle = new JScrollPane(jtProduccionDetalle);
        jbSeleccionarProducto = new JButton("Agregar producto[F4]");
        jbSeleccionarProducto.setSize(new Dimension(width, height));
        jbModificarProducto = new JButton("Modificar detalle");
        jbModificarProducto.setSize(new Dimension(width, height));
        jbEliminarProducto = new JButton("Eliminar detalle");
        jbEliminarProducto.setSize(new Dimension(width, height));
        JPanel jpSouthAuxProdTerminados = new JPanel(new MigLayout());
        jpSouthAuxProdTerminados.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAuxProdTerminados.add(jbSeleccionarProducto, "wrap");
        jpSouthAuxProdTerminados.add(jbModificarProducto, "growx, wrap");
        jpSouthAuxProdTerminados.add(jbEliminarProducto, "growx");

        //ROLLOS UTILIZADOS        
        jtRolloUtilizado = new JTable();
        jtRolloUtilizado.getTableHeader().setReorderingAllowed(false);
        jtRolloUtilizado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspRolloUtilizado = new JScrollPane(jtRolloUtilizado);
        jbSeleccionarRollo = new JButton("Agregar rollo             ");
        jbSeleccionarRollo.setSize(new Dimension(width, height));
        jbModificarRollo = new JButton("Modificar rollo           ");
        jbModificarRollo.setSize(new Dimension(width, height));
        jbEliminarRollo = new JButton("Eliminar rollo             ");
        jbEliminarRollo.setSize(new Dimension(width, height));
        JPanel jpSouthAuxRollos = new JPanel(new MigLayout());
        jpSouthAuxRollos.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAuxRollos.add(jbSeleccionarRollo, "wrap");
        jpSouthAuxRollos.add(jbModificarRollo, "growx, wrap");
        jpSouthAuxRollos.add(jbEliminarRollo, "growx");

        JPanel jpCenterAux1 = new JPanel(new BorderLayout());
        jpCenterAux1.add(jspProduccionDetalle, BorderLayout.CENTER);
        jpCenterAux1.add(jpSouthAuxProdTerminados, BorderLayout.EAST);

        JPanel jpCenterAux2 = new JPanel(new BorderLayout());
        jpCenterAux2.add(jspRolloUtilizado, BorderLayout.CENTER);
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
