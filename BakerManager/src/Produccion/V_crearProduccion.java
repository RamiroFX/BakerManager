/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Entities.E_impresionTipo;
import Entities.E_produccionTipo;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
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
 * @author Ramiro
 */
class V_crearProduccion extends JDialog {

    //NORTE
    //norte 1
    JPanel jpNorth1;
    public JTextField jtfFuncionario, jtfNroOrdenTrabajo;
    public JButton jbFuncionario;
    public JLabel jlNroOrdenTrabajo, jlFechaProduccion;
    public JComboBox<E_produccionTipo> jcbTipoProduccion;
    public JDateChooser jdcFechaEntrega;
    //CENTRO
    JPanel jpCenter;
    public JTable jtProduccionDetalle;
    public JScrollPane jspProduccionDetalle;
    public JButton jbSeleccionarProducto, jbModificarDetalle, jbEliminarDetalle;
    //SUR
    public JPanel jpSouth;
    public JButton jbAceptar, jbSalir;

    public V_crearProduccion(JFrame frame) {
        super(frame, "Crear producción", JDialog.ModalityType.APPLICATION_MODAL);
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
        jcbTipoProduccion = new JComboBox();

        jdcFechaEntrega = new JDateChooser();
        jdcFechaEntrega.setPreferredSize(new Dimension(150, 20));
        jpNorth1.add(jbFuncionario);
        jpNorth1.add(jtfFuncionario);
        jpNorth1.add(jlNroOrdenTrabajo);
        jpNorth1.add(jtfNroOrdenTrabajo, "wrap");
        jpNorth1.add(new JLabel("Tipo producción"));
        jpNorth1.add(jcbTipoProduccion);
        jpNorth1.add(jlFechaProduccion);
        jpNorth1.add(jdcFechaEntrega);
    }

    private void initCenter() {
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        jtProduccionDetalle = new JTable();
        this.jtProduccionDetalle.getTableHeader().setReorderingAllowed(false);
        jtProduccionDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspProduccionDetalle = new JScrollPane(jtProduccionDetalle);
        jbSeleccionarProducto = new JButton("Agregar producto[F4]");
        jbModificarDetalle = new JButton("Modificar detalle");
        jbEliminarDetalle = new JButton("Eliminar detalle");
        JPanel jpSouthAux = new JPanel(new MigLayout());
        jpSouthAux.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        jpSouthAux.add(jbSeleccionarProducto, "wrap");
        jpSouthAux.add(jbModificarDetalle, "growx, wrap");
        jpSouthAux.add(jbEliminarDetalle, "growx");

        jpCenter.add(jspProduccionDetalle, BorderLayout.CENTER);
        jpCenter.add(jpSouthAux, BorderLayout.EAST);
    }

    private void initSouth() {
        jpSouth = new JPanel();
        jbAceptar = new JButton("Guardar cambios[F1]");
        jbSalir = new JButton("Salir[ESC]");
        jpSouth.add(jbAceptar);
        jpSouth.add(jbSalir);
    }
}
