/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import DB_manager.DB_Funcionario;
import DB_manager.DB_rol;
import DB_manager.DB_rol_usuario;
import MenuPrincipal.MenuPrincipal;
import bakermanager.C_inicio;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Administrador
 */
public class C_seleccionarRol implements ActionListener, KeyListener {

    public V_jifSeleccionarRol vista;
    C_inicio c_inicio; //referencia a la ventana principal

    public C_seleccionarRol(V_jifSeleccionarRol jifSelRol, C_inicio c_main) {
        this.c_inicio = c_main;
        this.vista = jifSelRol;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setSize((int) (this.c_inicio.vista.getWidth() * 0.3), (int) (this.c_inicio.vista.getHeight() * 0.2));
        this.vista.setLocation(new Point((this.c_inicio.vista.getWidth() - this.vista.getWidth()) / 2, (this.c_inicio.vista.getHeight() - this.vista.getHeight() - 45) / 2));
        this.c_inicio.agregarVentana(vista);
    }

    private void inicializarVista() {
        Vector vRolUsuario = DB_Funcionario.obtenerRolFuncionario(this.c_inicio.modelo.getRol_usuario().getFuncionario());
        for (int i = 0; i < vRolUsuario.size(); i++) {
            this.vista.jcbRol.addItem(vRolUsuario.get(i));
        }
    }

    private void agregarListeners() {
        vista.jbAceptar.addActionListener(this);
        vista.jbAceptar.addKeyListener(this);
        vista.jbSalir.addActionListener(this);
        vista.jcbRol.addActionListener(this);
        vista.jcbRol.addKeyListener(this);
    }

    private void controlarAccesoBarraMenu(Vector v) {
        Iterator it = v.iterator();
        while (it.hasNext()) {
            String rolMenuItem = it.next().toString();
            System.out.println(rolMenuItem);
//            if (rolMenuItem.equals("jifGestionUsuario")) {
//                c_inicio.v_mainFrame.getJMbarraMenu().jmGestionUsuario.setEnabled(true);
//            } else if (rolMenuItem.equals("jifGestionProducto")) {
//                c_inicio.v_mainFrame.getJMbarraMenu().jmGestionProducto.setEnabled(true);
//            } else if (rolMenuItem.equals("jifGestionCompra")) {
//                c_inicio.v_mainFrame.getJMbarraMenu().jmGestionCompra.setEnabled(true);
//            } else if (rolMenuItem.equals("jifGestionVenta")) {
//                c_inicio.v_mainFrame.getJMbarraMenu().jmGestionVenta.setEnabled(true);
//            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.jbSalir) {
            c_inicio.vista.setJtfUsuario("");
            //c_main.vista.getJMbarraMenu().jmiLogOut.setEnabled(false);
            //c_main.vista.getJMbarraMenu().jmiLogIn.setEnabled(true);
            vista.cerrar();
        } else if (e.getSource() == vista.jbAceptar) {
            vista.dispose();
            c_inicio.modelo.getRol_usuario().getFuncionario().setOcupacion((String) vista.jcbRol.getSelectedItem());
            c_inicio.setRol_usuario(DB_rol_usuario.obtenerRolUsuario(c_inicio.modelo.getRol_usuario().getFuncionario().getId_funcionario()));
            c_inicio.getRol_usuario().setRolActual(DB_rol.obtenerRol((String) vista.jcbRol.getSelectedItem()));
            c_inicio.getRol_usuario().setAccesos(DB_rol.obtenerAccesos(c_inicio.getRol_usuario().getRolActual().getId()));            
            MenuPrincipal c_menuPrincipal = new MenuPrincipal(c_inicio);
            c_menuPrincipal.mostrarVista();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (this.vista.jcbRol.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                this.vista.jbAceptar.requestFocusInWindow();
            }
        }
        if (this.vista.jbAceptar.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                vista.dispose();
                c_inicio.modelo.getRol_usuario().getFuncionario().setOcupacion((String) vista.jcbRol.getSelectedItem());
                c_inicio.setRol_usuario(DB_rol_usuario.obtenerRolUsuario(c_inicio.modelo.getRol_usuario().getFuncionario().getId_funcionario()));
                c_inicio.getRol_usuario().setRolActual(DB_rol.obtenerRol((String) vista.jcbRol.getSelectedItem()));
                c_inicio.getRol_usuario().setAccesos(DB_rol.obtenerAccesos(c_inicio.getRol_usuario().getRolActual().getId()));            
                MenuPrincipal c_menuPrincipal = new MenuPrincipal(c_inicio);
                c_menuPrincipal.mostrarVista();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
