/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
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
public class V_crearAjuste extends JDialog {

    public static final String UPDATE_TITLE = "Ver/Modificar ajuste de Stock";
    public static final String JB_ADD_PROD = "Agregar producto",
            JB_UPDATE_PROD = "Modificar producto",
            JB_DELETE_PROD = "Eliminar producto";

    //NORTE
    //norte 1
    JPanel jpNorth1;
    public JTextField jtfFuncionario, jtfObservacion;
    public JButton jbFuncionario;
    public JLabel jlFecha;
    public JDateChooser jdcFecha;
    //CENTRO
    JPanel jpCenter;
    //MATERIA PRIMA
    public JTable jtDetalle;
    public JScrollPane jspDetalle;
    public JButton jbSeleccionarProducto, jbModificarProducto, jbEliminarProducto;
    //SUR
    public JPanel jpSouth;
    public JButton jbAceptar, jbSalir;

    public V_crearAjuste(JDialog dialog) {
        super(dialog, "Crear ajuste de Stock", JDialog.ModalityType.APPLICATION_MODAL);
        setSize(900, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(dialog);
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
        jpNorth1.setBorder(new javax.swing.border.TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Datos de stock", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
        jbFuncionario = new JButton("Responsable");
        jtfFuncionario = new JTextField(30);
        jtfFuncionario.setEditable(false);
        jlFecha = new JLabel("Fecha");
        jtfObservacion = new JTextField(30);

        jdcFecha = new JDateChooser();
        jdcFecha.setPreferredSize(new Dimension(150, 20));
        jpNorth1.add(jbFuncionario);
        jpNorth1.add(jtfFuncionario);
        jpNorth1.add(jlFecha);
        jpNorth1.add(jdcFecha, "wrap");
        jpNorth1.add(new JLabel("Observación"));
        jpNorth1.add(jtfObservacion, "pushx, growx, spanx");
    }

    private void initCenter() {
        int width = 30, height = 10;
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));

        //ROLLOS UTILIZADOS
        jtDetalle = new JTable();
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jtDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspDetalle = new JScrollPane(jtDetalle);
        jbSeleccionarProducto = new JButton("Agregar");
        jbSeleccionarProducto.setName(JB_ADD_PROD);
        jbSeleccionarProducto.setSize(new Dimension(width, height));
        jbModificarProducto = new JButton("Modificar");
        jbModificarProducto.setName(JB_UPDATE_PROD);
        jbModificarProducto.setSize(new Dimension(width, height));
        jbEliminarProducto = new JButton("Eliminar");
        jbEliminarProducto.setName(JB_DELETE_PROD);
        jbEliminarProducto.setSize(new Dimension(width, height));
        JPanel jpSouthAuxRollos = new JPanel(new MigLayout());
        jpSouthAuxRollos.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAuxRollos.add(jbSeleccionarProducto, "wrap");
        jpSouthAuxRollos.add(jbModificarProducto, "growx, wrap");
        jpSouthAuxRollos.add(jbEliminarProducto, "growx");

        JPanel jpCenterAux2 = new JPanel(new BorderLayout());
        jpCenterAux2.add(jspDetalle, BorderLayout.CENTER);
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
