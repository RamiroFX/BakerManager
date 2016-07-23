/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MenuPrincipal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

/**
 *
 * @author Ramiro Ferreira
 */
class V_MenuPrincipal extends JInternalFrame {

    JPanel jpSouth, jpCenter, jpNorth;
    JButton jbSalir, jbProveedores, jbProducto, jbMovimientos, jbEmpleados;
    JFormattedTextField jftFecha;

    public V_MenuPrincipal() {
        super("Menú principal", true, true, true, true);
        setSize(600, 400);
        setName("menuPrincipal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicializarVista();
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    /**
     * Este metodo se encargará de obtener la dimension de la pantalla en
     * pixeles, para ello utilizamos la clase java.awt.Toolkit.
     *
     * Una vez obtenida la dimension de la pantalla, reducimos el alto de
     * nuestra aplicación puesto que la barra de tareas ocupa parte de la
     * pantalla, comúnmente el alto promedio es de 25 pixeles, por lo tanto a la
     * altura la reducimos 25
     *
     * @return Dimension
     */
    private Dimension obtenerDimensionDePantalla() {
        Dimension pantalla = null;
        pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        pantalla.height = pantalla.height - 80;
        return pantalla;
    }

    private void inicializarVista() {
        jpNorth = new JPanel();
        jpSouth = new JPanel();
        jpSouth.setBorder(new EtchedBorder());
        jpCenter = new JPanel();
        jbSalir = new JButton("Salir");
        jbProveedores = new JButton("Proveedores");
        jbProveedores.setName("Gestión proveedor");
        jbProveedores.setPreferredSize(new Dimension(200, 100));
        jbProducto = new JButton("Productos");
        jbProducto.setName("Gestión producto");
        jbProducto.setPreferredSize(new Dimension(200, 100));
        jbMovimientos = new JButton("Movimientos");
        jbMovimientos.setName("Gestión movimiento");
        jbMovimientos.setPreferredSize(new Dimension(200, 100));
        jbEmpleados = new JButton("Empleados");
        jbEmpleados.setName("Gestión empleado");
        jbEmpleados.setPreferredSize(new Dimension(200, 100));
        jftFecha = new JFormattedTextField(
                new DefaultFormatterFactory(
                new DateFormatter(new SimpleDateFormat("dd/MM/yyyy"))));
        jftFecha.setFont(new java.awt.Font("Monospaced", 1, 14));
        jftFecha.setEditable(false);

        jpNorth.add(jftFecha);
        jpSouth.add(jbSalir);
        jpCenter.add(jbProveedores);
        jpCenter.add(jbProducto);
        jpCenter.add(jbMovimientos);
        jpCenter.add(jbEmpleados);
    }
}
