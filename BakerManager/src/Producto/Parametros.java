/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB_manager.DB_Proveedor;
import DB_manager.DB_manager;
import bakermanager.C_inicio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Usuario
 */
public class Parametros extends javax.swing.JDialog implements ActionListener, MouseListener {

    private javax.swing.JButton jbCrear, jbModificar, jbEliminar;
    private javax.swing.JLabel jl;
    private JTabbedPane jtpCenter;
    private JPanel jpSouth;
    JScrollPane jspMarcas, jspRubros;
    JTable jtMarcas, jtRubros;
    C_gestion_producto gestion_producto;

    public Parametros(C_inicio c_inicio) {
        super(c_inicio.vista, true);
        setTitle("Parametros");
        setSize(new java.awt.Dimension(400, 300));
        setLocationRelativeTo(c_inicio.vista);
        initComponents();
        inicializarVista();
        agregarListener();
    }

    Parametros(C_inicio c_inicio, C_gestion_producto aThis) {
        super(c_inicio.vista, true);
        gestion_producto = aThis;
        setTitle("Parametros");
        setSize(new java.awt.Dimension(400, 300));
        setLocationRelativeTo(c_inicio.vista);
        initComponents();
        inicializarVista();
        agregarListener();
    }

    private void inicializarVista() {
        jbEliminar.setEnabled(false);
        jbModificar.setEnabled(false);
        jtMarcas.setModel(DB_manager.consultarMarca());
        jtRubros.setModel(DB_manager.consultarRubro());
    }

    private void initMarcas() {
        jtMarcas = new JTable();
        jspMarcas = new JScrollPane(jtMarcas);

    }

    private void initRubros() {
        jtRubros = new JTable();
        jspRubros = new JScrollPane(jtRubros);
    }

    private void initComponents() {
        initMarcas();
        initRubros();
        jtpCenter = new JTabbedPane();
        jtpCenter.add("Marcas", jspMarcas);
        jtpCenter.add("Rubros", jspRubros);
        jpSouth = new JPanel();
        jbCrear = new javax.swing.JButton("Agregar");
        jbModificar = new javax.swing.JButton("Modificar");
        jbEliminar = new javax.swing.JButton("Eliminar");
        jpSouth.add(jbCrear);
        jpSouth.add(jbModificar);
        jpSouth.add(jbEliminar);
        getContentPane().setLayout(new MigLayout());
        getContentPane().add(jtpCenter, "dock center");
        getContentPane().add(jpSouth, "dock south");
    }

    private void agregarListener() {
        jtpCenter.addMouseListener(this);
        jtRubros.addMouseListener(this);
        jtMarcas.addMouseListener(this);
        jbCrear.addActionListener(this);
        jbModificar.addActionListener(this);
        jbEliminar.addActionListener(this);
    }

    private void agregarMarca(String marca) {
        System.out.println("marca: " + marca);
        DB_Proveedor.insertarMarca(marca);
        this.jbModificar.setEnabled(false);
        this.jbEliminar.setEnabled(false);
        this.jtMarcas.setModel(DB_manager.consultarMarca());
    }

    private void modificarMarca(String marca) {
        int idMarca = Integer.valueOf(String.valueOf(this.jtMarcas.getValueAt(jtMarcas.getSelectedRow(), 0)));
        DB_Proveedor.modificarMarca(idMarca, marca);
        this.jbModificar.setEnabled(false);
        this.jbEliminar.setEnabled(false);
        this.jtMarcas.setModel(DB_manager.consultarMarca());
    }

    private void eliminarMarca() {
        int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int idMarca = Integer.valueOf(String.valueOf(this.jtMarcas.getValueAt(jtMarcas.getSelectedRow(), 0)));
                DB_Proveedor.eliminarMarca(idMarca);
                this.jbModificar.setEnabled(false);
                this.jbEliminar.setEnabled(false);
                this.jtMarcas.setModel(DB_manager.consultarMarca());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void agregarRubro(String rubro) {
        DB_Proveedor.insertarRubro(rubro);
        this.jbModificar.setEnabled(false);
        this.jbEliminar.setEnabled(false);
        this.jtRubros.setModel(DB_manager.consultarRubro());
    }

    private void modificarRubro(String rubro) {
        int idRubro = Integer.valueOf(String.valueOf(this.jtRubros.getValueAt(jtRubros.getSelectedRow(), 0)));
        DB_Proveedor.modificarRubro(idRubro, rubro);
        this.jbModificar.setEnabled(false);
        this.jbEliminar.setEnabled(false);
        this.jtRubros.setModel(DB_manager.consultarRubro());
    }

    private void eliminarRubro() {
        int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int idRubro = Integer.valueOf(String.valueOf(this.jtRubros.getValueAt(jtRubros.getSelectedRow(), 0)));
                DB_Proveedor.eliminarRubro(idRubro);
                this.jbModificar.setEnabled(false);
                this.jbEliminar.setEnabled(false);
                this.jtRubros.setModel(DB_manager.consultarRubro());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
    /*
     private void agregarImpuesto(String impuesto) {
     DB_Proveedor.insertarImpuesto(impuesto);
     this.jbModificar.setEnabled(false);
     this.jbEliminar.setEnabled(false);
     this.jtImpuesto.setModel(DB_manager.consultarImpuesto());
     }
     private void modificarImpuesto(String impuesto) {
     int idImpuesto = Integer.valueOf(String.valueOf(this.jtImpuesto.getValueAt(jtImpuesto.getSelectedRow(), 0)));
     DB_Proveedor.modificarRubro(idImpuesto, impuesto);
     this.jbModificar.setEnabled(false);
     this.jbEliminar.setEnabled(false);
     this.jtImpuesto.setModel(DB_manager.consultarImpuesto());
     }
     private void eliminarImpuesto() {
     int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     if (option == JOptionPane.YES_OPTION) {
     try {
     int idImpuesto = Integer.valueOf(String.valueOf(this.jtImpuesto.getValueAt(jtImpuesto.getSelectedRow(), 0)));
     DB_Proveedor.eliminarRubro(idImpuesto);
     this.jbModificar.setEnabled(false);
     this.jbEliminar.setEnabled(false);
     this.jtImpuesto.setModel(DB_manager.consultarImpuesto());
     } catch (Exception e) {
     e.printStackTrace();
     return;
     }
     }
     }
     */

    private void createButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspMarcas)) {
            String marca = JOptionPane.showInputDialog(this, "Inserte el nombre de la marca", "Insertar marca", JOptionPane.PLAIN_MESSAGE);
            if (marca != null) {
                if (!marca.isEmpty()) {
                    agregarMarca(marca);
                }
            }
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspRubros)) {
            String rubro = JOptionPane.showInputDialog(this, "Inserte el nombre del rubro", "Insertar categoria", JOptionPane.PLAIN_MESSAGE);
            if (rubro != null) {
                if (!rubro.isEmpty()) {
                    agregarRubro(rubro);
                }
            }
        }
        gestion_producto.actualizarVista();
    }

    private void updateButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspMarcas)) {
            String marca = JOptionPane.showInputDialog(this, "Inserte el nombre de la marca", "Insertar marca", JOptionPane.PLAIN_MESSAGE);
            if (marca != null) {
                if (!marca.isEmpty()) {
                    modificarMarca(marca);
                }
            }
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspRubros)) {
            String rubro = JOptionPane.showInputDialog(this, "Inserte el nombre del rubro", "Insertar categoria", JOptionPane.PLAIN_MESSAGE);
            if (rubro != null) {
                if (!rubro.isEmpty()) {
                    modificarRubro(rubro);
                }
            }
        }
        gestion_producto.actualizarVista();
    }

    private void deleteButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspMarcas)) {
            eliminarMarca();
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspRubros)) {
            eliminarRubro();
        }
        gestion_producto.actualizarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbCrear)) {
            createButtonHandler();
        }
        if (e.getSource().equals(jbModificar)) {
            updateButtonHandler();
        }
        if (e.getSource().equals(jbEliminar)) {
            deleteButtonHandler();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.jtpCenter)) {
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
        }
        if (e.getSource().equals(this.jtMarcas)) {
            int fila = this.jtMarcas.rowAtPoint(e.getPoint());
            int columna = this.jtMarcas.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.jbModificar.setEnabled(true);
                this.jbEliminar.setEnabled(true);
            } else {
                this.jbModificar.setEnabled(false);
                this.jbEliminar.setEnabled(false);
            }
        }
        if (e.getSource().equals(this.jtRubros)) {
            int fila = this.jtRubros.rowAtPoint(e.getPoint());
            int columna = this.jtRubros.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.jbModificar.setEnabled(true);
                this.jbEliminar.setEnabled(true);
            } else {
                this.jbModificar.setEnabled(false);
                this.jbEliminar.setEnabled(false);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
