/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearProductoTerminado;

import Entities.E_produccionFilm;
import Interface.InterfaceRecibirProduccionFilm;
import Produccion.SeleccionCantidadProductoSimple;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarFilm extends MouseAdapter implements ActionListener, KeyListener {

    private static final String ENTER_KEY = "Entrar";

    private M_seleccionarFilm modelo;
    private V_seleccionarFilm vista;
    private InterfaceRecibirProduccionFilm callback;
    private boolean esModoCreacion;

    public C_seleccionarFilm(M_seleccionarFilm modelo, V_seleccionarFilm vista) {
        this.vista = vista;
        this.modelo = modelo;
        this.esModoCreacion = true;
        inicializarVista();
        agregarListeners();
    }

    public void desactivarModoCreacion() {
        esModoCreacion = false;
    }

    public void setCallback(InterfaceRecibirProduccionFilm callback) {
        this.callback = callback;
    }

    public void mostrarVista() {
        displayQueryResults();
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFin.setDate(calendar.getTime());
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR, currentYear - 1);
        this.vista.jdcInicio.setDate(calendar.getTime());
        this.vista.jcbPorFecha.setSelected(true);
        this.vista.jbAceptar.setEnabled(false);
        this.vista.jtProducto.setModel(modelo.getTm());
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtProducto.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtProducto.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarRollo();
            }
        });
        Utilities.c_packColumn.packColumns(this.vista.jtProducto, 1);

        this.vista.jcbBuscarPor.addItem("Todos");
        this.vista.jcbBuscarPor.addItem("Nro. Film");
        this.vista.jcbBuscarPor.addItem("OT");
        this.vista.jcbBuscarPor.addItem("Producto");
        this.vista.jcbClasificarPor.addItem("Fecha");
        this.vista.jcbClasificarPor.addItem("OT");
        this.vista.jcbClasificarPor.addItem("Producto");
        this.vista.jcbClasificarPor.addItem("Nro. Film");
        this.vista.jcbOrdenarPor.addItem("Descendente");
        this.vista.jcbOrdenarPor.addItem("Ascendente");
        this.vista.jcbEstado.addItem("Disponible");
        this.vista.jcbEstado.addItem("Agotado");
        this.vista.jcbEstado.addItem("Todos");
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        //this.vista.jbCrearProducto.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jtfBuscar.addActionListener(this);
        this.vista.jcbOrdenarPor.addActionListener(this);
        this.vista.jcbBuscarPor.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtProducto.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jtProducto.addKeyListener(this);
    }

    public void displayQueryResults() {
        /*
         * Para permitir que los mensajes puedan ser desplegados, no se ejecuta
         * el query directamente, sino que se lo coloca en una cola de eventos
         * para que se ejecute luego de los eventos pendientes.
         */

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String desc = vista.jtfBuscar.getText();
                if (desc.length() > 50) {
                    JOptionPane.showMessageDialog(vista, "El texto ingresado supera el máximo permitido de 50 caracteres.", "Atención", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean buscarPorFecha = vista.jcbPorFecha.isSelected();
                if (buscarPorFecha) {
                    if (!validarFechas()) {
                        return;
                    }
                }
                String buscarPor = vista.jcbBuscarPor.getSelectedItem().toString();
                String ordenarPor = vista.jcbOrdenarPor.getSelectedItem().toString();
                String clasificarPor = vista.jcbClasificarPor.getSelectedItem().toString();
                String estado = vista.jcbEstado.getSelectedItem().toString();
                Date fechaInicio = vista.jdcInicio.getDate();
                Date fechaFin = vista.jdcFin.getDate();
                modelo.consultarRollos(desc.toLowerCase(), buscarPor, ordenarPor, clasificarPor,
                        estado, buscarPorFecha, fechaInicio, fechaFin);
                Utilities.c_packColumn.packColumns(vista.jtProducto, 1);
            }
        });
    }

    private boolean validarFechas() {
        Date inicio = vista.jdcInicio.getDate();
        Date fin = vista.jdcFin.getDate();
        if (inicio != null && fin != null) {
            int dateValue = inicio.compareTo(fin);
            if (dateValue <= 0) {
                return true;
            }
        }
        vista.jdcFin.setDate(vista.jdcInicio.getDate());
        vista.jdcFin.updateUI();
        JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void borrarParametros() {
        this.vista.jtfBuscar.setText("");
        this.vista.jtfBuscar.requestFocusInWindow();
    }

    private void seleccionarRollo() {
        int fila = vista.jtProducto.getSelectedRow();
        if (fila > -1) {
            E_produccionFilm pf = modelo.getTm().getList().get(fila);
            System.out.println("bauplast.C_seleccionarFilm.seleccionarRollo()");
            System.out.println("pf: " + pf.getProductoClasificacion().getDescripcion());
            System.out.println("pf.prod.getCategoria: " + pf.getProducto().getCategoria());
            SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(vista, -1);
            if (esModoCreacion) {
                System.out.println("PROD_TERMINADO_AGREGAR_ROLLO");
                scp.setTipo(SeleccionCantidadProductoSimple.PROD_TERMINADO_AGREGAR_ROLLO);
            } else {
                System.out.println("PROD_TERMINADO_ACTUALIZAR_ROLLO");
                scp.setTipo(SeleccionCantidadProductoSimple.PROD_TERMINADO_ACTUALIZAR_ROLLO);
            }
            scp.setFilm(pf);
            scp.setFilmCallback(callback);
            scp.inicializarVista();
            scp.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            seleccionarRollo();
            this.vista.jtfBuscar.requestFocusInWindow();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    vista.jtfBuscar.selectAll();
                }
            });
        }
        if (e.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        }
        if (e.getSource() == this.vista.jcbBuscarPor) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jcbOrdenarPor) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBuscar) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBorrar) {
            borrarParametros();
        } else if (e.getSource() == this.vista.jbSalir) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtProducto.rowAtPoint(e.getPoint());
        int columna = this.vista.jtProducto.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                seleccionarRollo();
                this.vista.jtfBuscar.requestFocusInWindow();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (this.vista.jtfBuscar.hasFocus()) {
            displayQueryResults();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.vista.jtfBuscar.hasFocus()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN: {
                    vista.jtProducto.requestFocusInWindow();
                    break;
                }
                case KeyEvent.VK_ESCAPE: {
                    cerrar();
                    break;
                }
            }
        }
        if (this.vista.jtProducto.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                cerrar();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
