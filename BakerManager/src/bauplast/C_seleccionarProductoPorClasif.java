/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import Entities.E_produccionDetalle;
import bauplast.crearRollo.CrearFilm;
import Entities.Estado;
import Entities.M_menu_item;
import Entities.M_producto;
import Entities.ProductoCategoria;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.InterfaceRecibirProduccionTerminados;
import Interface.RecibirProductoCallback;
import MenuPrincipal.DatosUsuario;
import Produccion.SeleccionCantidadProductoSimple;
import Producto.C_crear_producto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarProductoPorClasif extends MouseAdapter implements ActionListener, KeyListener {

    private static final String ENTER_KEY = "Entrar";
    public static final int PRODUCTO = 1, ROLLO = 2, PRODUCTO_TERMINADO = 3;

    private M_seleccionarProductoPorClasif modelo;
    private V_seleccionarProductoPorClasif vista;
    private InterfaceRecibirProduccionFilm filmCallback;
    private InterfaceRecibirProduccionTerminados terminadosCallback;
    private RecibirProductoCallback productoCallback;
    private int tipo;

    public C_seleccionarProductoPorClasif(M_seleccionarProductoPorClasif modelo, V_seleccionarProductoPorClasif vista) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarVista();
        agregarListeners();
    }

    public void setProductoCallback(RecibirProductoCallback productoCallback) {
        this.tipo = PRODUCTO;
        this.productoCallback = productoCallback;
    }

    public void setRolloCallback(InterfaceRecibirProduccionFilm callback) {
        this.tipo = ROLLO;
        this.filmCallback = callback;
    }

    public void setProductoTerminadoCallback(InterfaceRecibirProduccionTerminados terminadosCallback) {
        this.tipo = PRODUCTO_TERMINADO;
        this.terminadosCallback = terminadosCallback;
    }

    public void mostrarVista() {
        displayQueryResults();
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        this.vista.jbCrearProducto.setEnabled(false);
        this.vista.jbAceptar.setEnabled(false);
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbCrearProducto.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbCrearProducto.setEnabled(true);
            }
        }
        this.vista.jtProducto.setModel(modelo.getTm());
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtProducto.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtProducto.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarProducto();
            }
        });
        Utilities.c_packColumn.packColumns(this.vista.jtProducto, 1);

        ArrayList<Estado> estado = modelo.obtenerEstado();
        for (int i = 0; i < estado.size(); i++) {
            this.vista.jcbEstado.addItem(estado.get(i));
        }
        ArrayList<ProductoCategoria> categorias = modelo.obtenerCategorias();
        this.vista.jcbCategoria.addItem(new ProductoCategoria(-1, "Todas"));
        for (int i = 0; i < categorias.size(); i++) {
            this.vista.jcbCategoria.addItem(categorias.get(i));
        }
        this.vista.jcbBuscarPor.addItem("Descripción");
        this.vista.jcbBuscarPor.addItem("ID");
        this.vista.jcbBuscarPor.addItem("Código");
    }

    private void agregarListeners() {        
        vista.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                vista.jcbCategoria.setSelectedItem(modelo.getPc());
            }
        });
        //ACTION LISTENERS
        this.vista.jbCrearProducto.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jtfBuscar.addActionListener(this);
        this.vista.jcbCategoria.addActionListener(this);
        this.vista.jcbBuscarPor.addActionListener(this);
        this.vista.jcbEstado.addActionListener(this);
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
                int jcbEstadoIndex = vista.jcbEstado.getSelectedIndex();
                int jcbCategoriaIndex = vista.jcbCategoria.getSelectedIndex();
                Estado estado = vista.jcbEstado.getItemAt(jcbEstadoIndex);
                ProductoCategoria categoria = vista.jcbCategoria.getItemAt(jcbCategoriaIndex);
                String buscarPor = vista.jcbBuscarPor.getSelectedItem().toString();
                modelo.consultarRollos(desc, estado, buscarPor, categoria);
                Utilities.c_packColumn.packColumns(vista.jtProducto, 1);
            }
        });
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void borrarParametros() {
        this.vista.jtfBuscar.setText("");
        this.vista.jtfBuscar.requestFocusInWindow();
        this.vista.jcbEstado.setSelectedIndex(1);
    }

    private void seleccionarProducto() {
        int fila = vista.jtProducto.getSelectedRow();
        if (fila > -1) {
            M_producto producto = modelo.getTm().getProductoList().get(fila);
            switch (tipo) {
                case PRODUCTO: {
                    SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(vista, true);
                    scp.setProducto(producto);
                    scp.setProductoCallback(productoCallback);
                    scp.setTipo(SeleccionCantidadProductoSimple.PRODUCTO);
                    scp.inicializarVista();
                    scp.setVisible(true);
                    break;
                }
                case ROLLO: {
                    vista.jbAceptar.setEnabled(true);
                    CrearFilm crearFilm = new CrearFilm(this.vista);
                    crearFilm.setCallback(filmCallback);
                    crearFilm.rellenarVista(producto);
                    crearFilm.mostrarVista();
                    vista.jtfBuscar.requestFocusInWindow();
                    break;
                }
                case PRODUCTO_TERMINADO: {
                    SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(vista, true);
                    E_produccionDetalle pd = new E_produccionDetalle();
                    pd.setProducto(producto);
                    scp.setProduccionTerminados(pd);
                    scp.setProduccionTerminadosCallback(terminadosCallback);
                    scp.setTipo(SeleccionCantidadProductoSimple.PRODUCCION_TERMINADOS);
                    scp.inicializarVista();
                    scp.setVisible(true);
                    break;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            seleccionarProducto();
            this.vista.jtfBuscar.requestFocusInWindow();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    vista.jtfBuscar.selectAll();
                }
            });
        }

        if (e.getSource() == this.vista.jbCrearProducto) {
            C_crear_producto sp = new C_crear_producto(vista);
            sp.mostrarVista();
        }
        if (e.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        }
        if (e.getSource() == this.vista.jcbCategoria) {
            displayQueryResults();
        }
        if (e.getSource() == this.vista.jcbEstado) {
            displayQueryResults();
        }
        if (e.getSource() == this.vista.jcbBuscarPor) {
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
                seleccionarProducto();
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
