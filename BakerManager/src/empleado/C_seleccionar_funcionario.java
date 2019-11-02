package Empleado;

import DB.DB_Cliente;
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

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionar_funcionario extends MouseAdapter implements ActionListener, KeyListener {

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
        this.vista.jtCliente.setModel(DB_Funcionario.consultarFuncionario("", false, true, true));
        Utilities.c_packColumn.packColumns(this.vista.jtCliente, 1);
    }

    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jckbEntidadNombre.addActionListener(this);
        this.vista.jckbCi.addActionListener(this);
        this.vista.jrbExclusivo.addActionListener(this);
        this.vista.jrbInclusivo.addActionListener(this);
        this.vista.jtCliente.addMouseListener(this);
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

    private void displayQueryResults() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String cliente = vista.jtfBuscar.getText();
                boolean entidad = vista.jckbEntidadNombre.isSelected();
                boolean ruc = vista.jckbCi.isSelected();
                boolean exclusivo = vista.jrbExclusivo.isSelected();
                vista.jtCliente.setModel(DB_Cliente.consultarCliente(cliente.toLowerCase(), entidad, ruc, exclusivo));
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
        int fila = this.vista.jtCliente.rowAtPoint(e.getPoint());
        int columna = this.vista.jtCliente.columnAtPoint(e.getPoint());
        idFuncionario = Integer.valueOf(String.valueOf(this.vista.jtCliente.getValueAt(fila, 0)));
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
    }
}
