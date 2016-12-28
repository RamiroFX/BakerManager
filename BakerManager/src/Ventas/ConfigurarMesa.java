/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Cliente.Seleccionar_cliente;
import DB.DB_Cliente;
import DB.DB_Ingreso;
import Entities.M_cliente;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Parametros.TipoOperacion;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class ConfigurarMesa extends JDialog implements ActionListener, KeyListener {

    private JLabel jlNroMesa;
    private JButton jbCliente, jbAceptar, jbCancelar;
    private JTextField jtfCliente, jtfNroMesa;
    private JPanel jpCenter, jpSouth;
    public C_crearVentas crearVentas;
    public M_cliente cliente;
    int numeroMesa;
    long idMesa;

    public ConfigurarMesa(C_crearVentas crearVentas) {
        super(crearVentas.gestionVentas.c_inicio.vista, "Crear mesa", true);
        this.crearVentas = crearVentas;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new java.awt.Dimension(300, 300));
        inicializarVista();
        agregarListeners();
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbAceptar)) {
            crearMesa();
        }
        if (e.getSource().equals(jbCancelar)) {
            cerrar();
        }
        if (e.getSource().equals(jbCliente)) {
            crearCliente();
        }
    }

    private void inicializarVista() {
        this.cliente = DB_Cliente.obtenerDatosClienteID(1);
        jpSouth = new JPanel();
        jbCliente = new JButton("Cliente");
        jbAceptar = new JButton("Aceptar");
        jbCancelar = new JButton("Cancelar");
        jpSouth.add(jbAceptar);
        jpSouth.add(jbCancelar);

        jtfCliente = new JTextField();
        jtfCliente.setPreferredSize(new Dimension(250, 20));
        jtfCliente.setEditable(false);
        String nombre = this.cliente.getNombre();
        String Entidad = this.cliente.getEntidad();
        this.jtfCliente.setText(nombre + " (" + Entidad + ")");
        jlNroMesa = new JLabel("Nro. mesa");
        jtfNroMesa = new JTextField();
        jtfNroMesa.setPreferredSize(new Dimension(250, 20));
        jpCenter = new JPanel(new MigLayout());
        jpCenter.add(jbCliente);
        jpCenter.add(jtfCliente, "wrap");
        jpCenter.add(jlNroMesa);
        jpCenter.add(jtfNroMesa);
        this.setLocationRelativeTo(crearVentas.vista);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                jtfNroMesa.requestFocusInWindow();
            }
        });
    }

    private void agregarListeners() {
        this.jbCliente.addActionListener(this);
        this.jbAceptar.addActionListener(this);
        this.jbCancelar.addActionListener(this);
        this.jtfNroMesa.addKeyListener(this);
    }

    private void cerrar() {
        this.dispose();
    }

    private void crearMesa() {
        if (controlarMesaOcupada()) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    numeroMesa = Integer.valueOf(jtfNroMesa.getText());
                    M_mesa m = new M_mesa();
                    m.setCliente(cliente);
                    m.setFuncionario(crearVentas.gestionVentas.c_inicio.modelo.getRol_usuario().getFuncionario());
                    m.setIdCondVenta(TipoOperacion.CONTADO);
                    m.setNumeroMesa(numeroMesa);
                    ArrayList<M_mesa_detalle> d = new ArrayList<>();
                    idMesa = DB_Ingreso.insertarMesa(m, d);
                }
            });
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    crearVentas.actualizarTablaMesa();
                }
            });
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Integer id = (int) idMesa;
                    Ver_mesa vm = new Ver_mesa(crearVentas, id);
                    vm.mostrarVista();
                }
            });
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    cerrar();
                }
            });
//            Ver_mesa vm = new Ver_mesa(crearVentas, numeroMesa);
//            vm.mostrarVista();
//            cerrar();
        } else {
            JOptionPane.showMessageDialog(this, "La mesa se encuentra ocupada. Seleccione otro número de mesa", "Mesa Ocupada", JOptionPane.INFORMATION_MESSAGE);
            jtfNroMesa.setText("");
            jtfNroMesa.requestFocusInWindow();
        }
    }

    private void crearCliente() {
        Seleccionar_cliente sc = new Seleccionar_cliente(this);
        sc.mostrarVista();
    }

    public void recibirCliente(M_cliente cliente) {
        this.cliente = cliente;
        String nombre = this.cliente.getNombre();
        String Entidad = this.cliente.getEntidad();
        this.jtfCliente.setText(nombre + " (" + Entidad + ")");
    }

    void mostrarVista() {
        this.setVisible(true);
    }

    private boolean controlarMesaOcupada() {
        int numeroMesa;
        try {
            numeroMesa = Integer.valueOf(jtfNroMesa.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Inserte un número de mesa válido (solo número enteros)", "Error", JOptionPane.ERROR_MESSAGE);
            jtfNroMesa.setText("");
            jtfNroMesa.requestFocusInWindow();
            return false;
        }
        return DB_Ingreso.estaLibreMesa(numeroMesa);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.jtfNroMesa.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                crearMesa();
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                cerrar();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
