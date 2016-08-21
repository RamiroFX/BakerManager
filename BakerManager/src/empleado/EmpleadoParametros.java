/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package empleado;

import DB.DB_manager;
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
public class EmpleadoParametros extends javax.swing.JDialog implements ActionListener, MouseListener {

    private javax.swing.JButton jbCrear, jbModificar, jbEliminar;
    private JTabbedPane jtpCenter;
    private JPanel jpSouth;
    JScrollPane jspPais, jspCiudad;
    JTable jtPais, jtCiudad;
    C_gestion_usuario gestion_usuario;

    public EmpleadoParametros(C_inicio c_inicio) {
        super(c_inicio.vista, true);
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
        jtPais.setModel(DB_manager.consultarPais());
        jtCiudad.setModel(DB_manager.consultarCiudad());
    }

    private void initMarcas() {
        jtPais = new JTable();
        jspPais = new JScrollPane(jtPais);

    }

    private void initCategorias() {
        jtCiudad = new JTable();
        jspCiudad = new JScrollPane(jtCiudad);
    }

    private void initComponents() {
        initMarcas();
        initCategorias();
        jtpCenter = new JTabbedPane();
        jtpCenter.add("País", jspPais);
        jtpCenter.add("Ciudad", jspCiudad);
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
        jtCiudad.addMouseListener(this);
        jtPais.addMouseListener(this);
        jbCrear.addActionListener(this);
        jbModificar.addActionListener(this);
        jbEliminar.addActionListener(this);
    }

    private void agregarPais(String pais) {
        String p = pais.trim();
        if (p.length() < 1 || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (p.length() > 30) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 30 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdPais(p.toLowerCase());
        System.out.println("b: " + b);
        if (b == null) {
            DB_manager.insertarPais(p);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtPais.setModel(DB_manager.consultarPais());
        } else {
            JOptionPane.showMessageDialog(this, "País existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarPais(String pais) {
        String m = pais.trim();
        if (m.length() < 1 || m.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (m.length() > 30) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 30 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdPais(m.toLowerCase());
        int idMarca = Integer.valueOf(String.valueOf(this.jtPais.getValueAt(jtPais.getSelectedRow(), 0)));
        if (b == null) {
            DB_manager.modificarPais(idMarca, pais);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtPais.setModel(DB_manager.consultarPais());
        } else if (b == idMarca) {
            DB_manager.modificarPais(idMarca, pais);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtPais.setModel(DB_manager.consultarPais());
        } else {
            JOptionPane.showMessageDialog(this, "País existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPais() {
        int idPais = Integer.valueOf(String.valueOf(this.jtPais.getValueAt(jtPais.getSelectedRow(), 0)));
        boolean m = DB_manager.paisEnUso(idPais);
        if (m) {
            int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    DB_manager.eliminarPais(idPais);
                    this.jbModificar.setEnabled(false);
                    this.jbEliminar.setEnabled(false);
                    this.jtPais.setModel(DB_manager.consultarPais());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Existe personas que se encuentran utilizando el país seleccionado.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarCiudad(String ciudad) {
        String ciud = ciudad.trim();
        if (ciud.length() < 1 || ciud.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ciud.length() > 30) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 30 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Integer b = DB_manager.obtenerIdCiudad(ciud.toLowerCase());
        if (b == null) {
            DB_manager.insertarCiudad(ciud);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtCiudad.setModel(DB_manager.consultarCiudad());
        } else {
            JOptionPane.showMessageDialog(this, "Ciudad existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarCiudad(String ciudad) {
        String c = ciudad.trim();
        if (c.length() < 1 || c.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserte 1 caracter por lo menos.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (c.length() > 30) {
            JOptionPane.showMessageDialog(this, "Máximo permitido 30 caracteres.", "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idCiudad = Integer.valueOf(String.valueOf(this.jtCiudad.getValueAt(jtCiudad.getSelectedRow(), 0)));
        Integer b = DB_manager.obtenerIdCiudad(c.toLowerCase());
        if (b == null) {
            DB_manager.modificarCiudad(idCiudad, ciudad);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtCiudad.setModel(DB_manager.consultarCiudad());
        } else if (b == idCiudad) {
            DB_manager.modificarCiudad(idCiudad, ciudad);
            this.jbModificar.setEnabled(false);
            this.jbEliminar.setEnabled(false);
            this.jtCiudad.setModel(DB_manager.consultarCiudad());
        } else {
            JOptionPane.showMessageDialog(this, "Ciudad existente.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCiudad() {
        int idCiudad = Integer.valueOf(String.valueOf(this.jtCiudad.getValueAt(jtCiudad.getSelectedRow(), 0)));
        boolean m = DB_manager.ciudadEnUso(idCiudad);
        if (m) {
            int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    DB_manager.eliminarCiudad(idCiudad);
                    this.jbModificar.setEnabled(false);
                    this.jbEliminar.setEnabled(false);
                    this.jtCiudad.setModel(DB_manager.consultarCiudad());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Existen personas que se encuentran utilizando la ciudad seleccionada.", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspPais)) {
            String marca = JOptionPane.showInputDialog(this, "Inserte el nombre del país", "Insertar país", JOptionPane.PLAIN_MESSAGE);
            if (marca != null) {
                if (!marca.isEmpty()) {
                    agregarPais(marca);
                }
            }
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspCiudad)) {
            String rubro = JOptionPane.showInputDialog(this, "Inserte el nombre de la ciudad", "Insertar ciudad", JOptionPane.PLAIN_MESSAGE);
            if (rubro != null) {
                if (!rubro.isEmpty()) {
                    agregarCiudad(rubro);
                }
            }
        }
    }

    private void updateButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspPais)) {
            String pais = JOptionPane.showInputDialog(this, "Inserte el nombre del país", "Insertar pais", JOptionPane.PLAIN_MESSAGE);
            if (pais != null) {
                if (!pais.isEmpty()) {
                    modificarPais(pais);
                }
            }
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspCiudad)) {
            String ciudad = JOptionPane.showInputDialog(this, "Inserte el nombre de la ciudad", "Insertar ciudad", JOptionPane.PLAIN_MESSAGE);
            if (ciudad != null) {
                if (!ciudad.isEmpty()) {
                    modificarCiudad(ciudad);
                }
            }
        }
    }

    private void deleteButtonHandler() {
        if (this.jtpCenter.getSelectedComponent().equals(this.jspPais)) {
            eliminarPais();
        } else if (this.jtpCenter.getSelectedComponent().equals(this.jspCiudad)) {
            eliminarCiudad();
        }
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
        if (e.getSource().equals(this.jtPais)) {
            int fila = this.jtPais.rowAtPoint(e.getPoint());
            int columna = this.jtPais.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.jbModificar.setEnabled(true);
                this.jbEliminar.setEnabled(true);
            } else {
                this.jbModificar.setEnabled(false);
                this.jbEliminar.setEnabled(false);
            }
        }
        if (e.getSource().equals(this.jtCiudad)) {
            int fila = this.jtCiudad.rowAtPoint(e.getPoint());
            int columna = this.jtCiudad.columnAtPoint(e.getPoint());
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
