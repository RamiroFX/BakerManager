/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB_manager.DB_Producto;
import DB_manager.DB_Proveedor;
import DB_manager.DB_manager;
import Entities.M_proveedor;
import Proveedor.Seleccionar_proveedor;
import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro
 */
public class Crear_producto_proveedor extends JDialog implements ActionListener, KeyListener, MouseListener {

    JButton jbSalir, jbBuscar, jbBorrar, jbAgregarProv;
    public JTextField jtfBuscar, jtfProveedor;
    JScrollPane jspProductos, jspProveedor;
    JTable jtProducto, jtProveedor;
    JLabel jlContado, jlCredito, jlTotal;
    JPanel jpBotones, jpTopProducto, jpBotonesTop, jpJtextFieldTop;
    public JComboBox jcbImpuesto, jcbMarca, jcbEstado, jcbRubro;
    private Integer idProducto;
    //private m_producto producto;
    private M_proveedor proveedor;
    private JButton jbAgregar, jbQuitar;
    public DefaultTableModel dtmProdAgreg;

    public Crear_producto_proveedor(C_gestion_producto c_gestion_producto) {
        super(c_gestion_producto.c_inicio.vista, DEFAULT_MODALITY_TYPE);
        setTitle("Asignar productos a proveedores");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(c_gestion_producto.c_inicio.vista);
        inicializarComponentes();
        inicializarVista();
        agregarListener();
    }

    private void initTop() {
        jpTopProducto = new JPanel(new MigLayout("", "[][]", "[][]"));
        JPanel jpFiltros = new JPanel(new MigLayout("", "[][]", "[][]"));
        jcbImpuesto = new JComboBox();
        jcbMarca = new JComboBox();
        jcbEstado = new JComboBox();
        jcbRubro = new JComboBox();
        jpFiltros.add(new JLabel("Marca:"));
        jpFiltros.add(jcbMarca);
        jpFiltros.add(new JLabel("Impuesto:"));
        jpFiltros.add(jcbImpuesto, "wrap");
        jpFiltros.add(new JLabel("Rubro:"));
        jpFiltros.add(jcbRubro);
        jpFiltros.add(new JLabel("Estado:"));
        jpFiltros.add(jcbEstado);
        jpBotonesTop = new JPanel();
        jpJtextFieldTop = new JPanel(new BorderLayout());
        jtfBuscar = new JTextField();
        jtfBuscar.setHorizontalAlignment(JTextField.CENTER);
        jtfBuscar.setFont(new java.awt.Font("Times New Roman", 0, 16));
        jpJtextFieldTop.add(jtfBuscar);
        jbBuscar = new JButton("Buscar");
        jbBorrar = new JButton("Borrar");
        jpBotonesTop.add(jbBuscar);
        jpBotonesTop.add(jbBorrar);
        jpTopProducto.add(jpJtextFieldTop, "width :200:,cell 0 0");
        jpTopProducto.add(jpBotonesTop, "cell 1 0");
        jpTopProducto.add(jpFiltros, "cell 0 1, span");
        jpTopProducto.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }

    private void inicializarComponentes() {
        initTop();
        jbSalir = new JButton("Cerrar");
        jbAgregar = new JButton("Agregar ->");
        jbAgregar.setEnabled(false);
        jbQuitar = new JButton("<- Quitar");
        jbQuitar.setEnabled(false);
        jpBotones = new JPanel();
        jpBotones.add(jbSalir);
        jtProducto = new JTable();
        dtmProdAgreg = new DefaultTableModel();
        dtmProdAgreg.addColumn("ID");
        dtmProdAgreg.addColumn("Descripción");
        jtProveedor = new JTable(dtmProdAgreg);
        jspProductos = new JScrollPane(jtProducto);
        jspProveedor = new JScrollPane(jtProveedor);
        JPanel jpProductoDisponible = new JPanel(new BorderLayout());
        jpProductoDisponible.add(jspProductos, BorderLayout.CENTER);
        jpProductoDisponible.add(jbAgregar, BorderLayout.SOUTH);

        JPanel jpProductoAgregado = new JPanel(new BorderLayout());
        jpProductoAgregado.add(jspProveedor, BorderLayout.CENTER);
        jpProductoAgregado.add(jbQuitar, BorderLayout.SOUTH);
        jlContado = new JLabel("Egresos al contado");
        jlContado.setHorizontalAlignment(SwingConstants.CENTER);
        jlCredito = new JLabel("Egresos a crédito");
        jlCredito.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotal = new JLabel("Total egresos");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel jpCenter = new JPanel(new GridLayout(1, 2));

        jpCenter.add(jpProductoDisponible);
        jpCenter.add(jpProductoAgregado);

        JPanel jpTopCenter = new JPanel(new GridLayout(1, 2));
        jpTopCenter.add(jpTopProducto);

        JPanel jpNorth = new javax.swing.JPanel();
        JLabel jlProveedor = new javax.swing.JLabel("Proveedor");
        jtfProveedor = new javax.swing.JTextField(20);
        jtfProveedor.setEditable(false);
        jbAgregarProv = new javax.swing.JButton("Proveedor");
        jpNorth.add(jlProveedor);
        jpNorth.add(jtfProveedor);
        jpNorth.add(jbAgregarProv);
        jpTopCenter.add(jpNorth);
        getContentPane().add(jpTopCenter, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void inicializarVista() {
        jtProducto.setModel(DB_Producto.consultaSimpleProducto(""));
        Utilities.c_packColumn.packColumns(jtProducto, 1);

        Vector marca = DB_manager.obtenerMarca();
        jcbMarca.addItem("Todos");
        for (int i = 0; i < marca.size(); i++) {
            jcbMarca.addItem(marca.get(i));
        }
        Vector rubro = DB_manager.obtenerRubro();
        jcbRubro.addItem("Todos");
        for (int i = 0; i < rubro.size(); i++) {
            jcbRubro.addItem(rubro.get(i));
        }
        Vector impuesto = DB_manager.obtenerImpuesto();
        jcbImpuesto.addItem("Todos");
        for (int i = 0; i < impuesto.size(); i++) {
            jcbImpuesto.addItem(impuesto.get(i));
        }
        Vector estado = DB_manager.obtenerEstado();
        jcbEstado.addItem("Todos");
        for (int i = 0; i < estado.size(); i++) {
            jcbEstado.addItem(estado.get(i));
        }
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
                String desc = jtfBuscar.getText();
                String marca = jcbMarca.getSelectedItem().toString();
                String rubro = jcbRubro.getSelectedItem().toString();
                String impuesto = jcbImpuesto.getSelectedItem().toString();
                String estado = jcbEstado.getSelectedItem().toString();
                String proveedor = "Todos";
                /*
                 * Se utiliza el objeto factory para obtener un TableModel
                 * para los resultados del query.
                 */

                jtProducto.setModel(DB_Producto.consultaSimpleProducto(desc, proveedor, marca, rubro, impuesto, estado));
                Utilities.c_packColumn.packColumns(jtProducto, 1);
            }
        });
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jtProducto.addMouseListener(this);
        jtProveedor.addMouseListener(this);
        jtfBuscar.addActionListener(this);
        jtfBuscar.addKeyListener(this);
        jbBuscar.addActionListener(this);
        jbBorrar.addActionListener(this);
        jbAgregarProv.addActionListener(this);
        jbAgregar.addActionListener(this);
        jbQuitar.addActionListener(this);
    }

    private void agregarProducto(int idProducto) {
        if (jtfProveedor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                int cantFilas = jtProveedor.getRowCount();
                int idProd = idProducto;
                for (int i = 0; i < cantFilas; i++) {
                    int idAux = Integer.valueOf(String.valueOf(jtProveedor.getValueAt(i, 0)));
                    if (idAux == idProd) {
                        return;
                    }
                }
                Object[] Row = {idProducto, jtProducto.getValueAt(jtProducto.getSelectedRow(), 1)};
                dtmProdAgreg.addRow(Row);
                jtProveedor.updateUI();
                jbAgregar.setEnabled(false);
                DB_Proveedor.insertarProveedorProducto(proveedor.getId(), idProducto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void quitarProducto() {
        int option = JOptionPane.showConfirmDialog(this, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            jbQuitar.setEnabled(false);
            dtmProdAgreg.removeRow(jtProveedor.getSelectedRow());
            DB_Proveedor.eliminarProveedorProducto(proveedor.getId(), idProducto);
            jtProveedor.updateUI();
        }
    }

    private void cerrar() {
        dispose();
        System.runFinalization();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jtfBuscar) {
            displayQueryResults();
        } else if (e.getSource().equals(this.jbAgregarProv)) {
            Seleccionar_proveedor sp = new Seleccionar_proveedor(this, this);
            sp.mostrarVista();
        } else if (e.getSource() == jbBorrar) {
            jtfBuscar.setText("");
            jtfBuscar.requestFocusInWindow();
        } else if (e.getSource() == jbAgregar) {
            agregarProducto(idProducto);
        } else if (e.getSource() == jbQuitar) {
            quitarProducto();
        } else if (e.getSource() == jbSalir) {
            cerrar();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        displayQueryResults();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(jtProducto)) {
            jbAgregar.setEnabled(true);
            int fila = jtProducto.rowAtPoint(e.getPoint());
            int columna = jtProducto.columnAtPoint(e.getPoint());
            idProducto = Integer.valueOf(String.valueOf(jtProducto.getValueAt(fila, 0)));
            //producto = DB_Producto.obtenerDatosProductoID(idProducto);
            if ((fila > -1) && (columna > -1)) {
                if (e.getClickCount() == 2) {
                    agregarProducto(idProducto);
                }
            }

        }
        if (e.getSource().equals(jtProveedor)) {
            jbQuitar.setEnabled(true);
            int fila = jtProveedor.rowAtPoint(e.getPoint());
            int columna = jtProveedor.columnAtPoint(e.getPoint());
            idProducto = Integer.valueOf(String.valueOf(jtProveedor.getValueAt(fila, 0)));
            //producto = DB_Producto.obtenerDatosProductoID(idProducto);
            if ((fila > -1) && (columna > -1)) {
                if (e.getClickCount() == 2) {
                    quitarProducto();
                }
            }

        }

    }

    public void recibirProveedor(M_proveedor proveedor) {
        this.proveedor = proveedor;
        this.jtfProveedor.setText(this.proveedor.getNombre());
        this.jtProveedor.setModel(DB_Producto.consultaSimpleProducto("", this.proveedor.getEntidad(), "Todos", "Todos", "Todos", "Todos"));
        int cantRow = this.jtProveedor.getRowCount();
        boolean b = true;
        for (int i = 0; i < cantRow; i++) {
            Object[] Row = {jtProveedor.getValueAt(i, 0), jtProveedor.getValueAt(i, 1)};
            int idNuevo = Integer.valueOf(String.valueOf(jtProveedor.getValueAt(i, 0)));
            for (int j = 0; j < dtmProdAgreg.getRowCount(); j++) {
                int idViejo = Integer.valueOf(String.valueOf(dtmProdAgreg.getValueAt(j, 0)));
                if (idNuevo == idViejo) {
                    b = false;
                }
            }
            if (b) {
                dtmProdAgreg.addRow(Row);
                b = true;
            }
        }
        this.jtProveedor.setModel(dtmProdAgreg);
    }
}
