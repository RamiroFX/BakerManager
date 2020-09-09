package Empleado;

import DB.DB_Funcionario;
import Entities.M_funcionario;
import Interface.RecibirEmpleadoCallback;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionar_funcionario extends MouseAdapter implements ActionListener, KeyListener {

    private static final String ENTER_KEY = "Entrar";
    int idFuncionario;
    M_funcionario funcionario;
    V_seleccionar_funcionario vista;
    private RecibirEmpleadoCallback callback;

    public C_seleccionar_funcionario(V_seleccionar_funcionario vista) {
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    void mostrarVista() {
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        this.vista.jbAceptar.setEnabled(false);
        this.vista.jtFuncionario.setModel(DB_Funcionario.consultarFuncionario("", false, true, true));
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtFuncionario.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtFuncionario.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarFuncionario();
            }
        });
        Utilities.c_packColumn.packColumns(this.vista.jtFuncionario, 1);
    }

    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jckbEntidadNombre.addActionListener(this);
        this.vista.jckbCi.addActionListener(this);
        this.vista.jrbExclusivo.addActionListener(this);
        this.vista.jrbInclusivo.addActionListener(this);
        this.vista.jtFuncionario.addMouseListener(this);
        this.vista.jtfBuscar.addKeyListener(this);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    public void setCallback(RecibirEmpleadoCallback recb) {
        this.callback = recb;
    }

    private void seleccionarFuncionario(M_funcionario funcionario) {
        this.callback.recibirFuncionario(funcionario);
        cerrar();
    }

    private void seleccionarFuncionario() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int fila = vista.jtFuncionario.getSelectedRow();
                int columna = vista.jtFuncionario.getSelectedColumn();
                if ((fila > -1) && (columna > -1)) {
                    idFuncionario = Integer.valueOf(String.valueOf(vista.jtFuncionario.getValueAt(fila, 0)));
                    funcionario = DB_Funcionario.obtenerDatosFuncionarioID(idFuncionario);
                    seleccionarFuncionario(funcionario);
                }
            }
        });
    }

    private void displayQueryResults() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String cliente = vista.jtfBuscar.getText();
                boolean entidad = vista.jckbEntidadNombre.isSelected();
                boolean ruc = vista.jckbCi.isSelected();
                boolean exclusivo = vista.jrbExclusivo.isSelected();
                vista.jtFuncionario.setModel(DB_Funcionario.consultarFuncionario(cliente.toLowerCase(), entidad, ruc, exclusivo));
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == this.vista.jbAceptar) {
            seleccionarFuncionario(funcionario);
        } else if (ae.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jckbEntidadNombre) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jckbCi) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jrbExclusivo) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jrbInclusivo) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jbCancelar) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtFuncionario.rowAtPoint(e.getPoint());
        int columna = this.vista.jtFuncionario.columnAtPoint(e.getPoint());
        idFuncionario = Integer.valueOf(String.valueOf(this.vista.jtFuncionario.getValueAt(fila, 0)));
        funcionario = DB_Funcionario.obtenerDatosFuncionarioID(idFuncionario);
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                seleccionarFuncionario(funcionario);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        displayQueryResults();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrar();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (this.vista.jtfBuscar.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (vista.jtFuncionario.getModel().getRowCount() > 0) {
                    vista.jtFuncionario.requestFocusInWindow();
                }
            }
        }
    }
}
