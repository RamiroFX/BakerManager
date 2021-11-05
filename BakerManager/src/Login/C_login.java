/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import Entities.M_proveedor;
import Entities.ProductoCategoria;
import Excel.C_create_excel;
import Utilities.Config;
import bakermanager.C_inicio;
import com.nitido.utils.toaster.Toaster;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_login implements ActionListener, KeyListener {

    protected static final String USER = "postgres", PASS = "postgres";

    M_login modelo;
    V_loginExtended vista;
    C_inicio c_inicio;

    public C_login(M_login modelo, V_loginExtended vista, C_inicio c_inicio) {
        this.c_inicio = c_inicio;
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    private void inicializarVista() {
        this.vista.txtNombre.setText(Config.getUser());
        //this.vista.txtPassword.setText("admin");
        this.vista.jtfHost.setText(Config.getHost());
        this.vista.jtfPort.setText(Config.getPort());
        this.vista.jtfDB.setText(Config.getDB());
        this.vista.txtPassword.requestFocusInWindow();
    }

    public void mostrarVista() {
        this.vista.setSize(400, 220);
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        c_inicio.agregarVentana(vista);
        this.vista.btnAceptar.requestFocusInWindow();
    }

    public void eliminarVista() {
        vista.dispose();
        vista = null;
    }

    private void agregarListeners() {
        vista.btnAceptar.addActionListener(this);
        vista.btnAceptar.addKeyListener(this);
        vista.btnSalir.addActionListener(this);
        vista.btnSalir.addKeyListener(this);
        vista.txtNombre.addActionListener(this);
        vista.txtPassword.addActionListener(this);
        vista.jbSaveConfig.addActionListener(this);
    }

    private void crearSeleccionRol() {
        seleccionarRol seleccionarRol = new seleccionarRol(c_inicio);
        seleccionarRol.mostrarVista();
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    public void logIn() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (modelo.contador > 5) {
                    JOptionPane.showMessageDialog(vista, "Ha superado el limite de intentos para entrar al sistema.", "Alerta", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                modelo.contador++;
                //alias de usuario
                String user = vista.txtNombre.getText();
                if (user.isEmpty() || user.length() > 15) {
                    JOptionPane.showMessageDialog(vista, "Ingrese un nombre de usuario", "Atención", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                //contraseña
                String password = "";
                char[] pss = vista.txtPassword.getPassword();
                for (char c : pss) {
                    password = password + c;
                }
                if (password.isEmpty() || password.length() > 15) {
                    JOptionPane.showMessageDialog(vista, "Ingrese una contraseña válida", "Atención", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                //se conecta contra la base de datos
                if (modelo.conectar(USER, PASS)) {
                    if (modelo.verificarUsuario(user, password)) {
                        Config.setUser(user);
                        c_inicio.modelo.getRol_usuario().setFuncionario(modelo.funcionario);
                        mostrarMensaje("La conexion se ha establecido con exito \n"
                                + "Bienvenido " + c_inicio.modelo.getRol_usuario().getFuncionario().getNombre() + " " + c_inicio.modelo.getRol_usuario().getFuncionario().getApellido() + "!");
                        //una vez correcto el nombre/contraseña se elimina la pantalla de logeo
                        //y se procede a mostrar la pantalla de seleccion de rol
                        vista.dispose();
                        vista = null;
                        crearSeleccionRol();
                        //se habilita la opcion de deslogeo
                        //c_main.vista.getJMbarraMenu().jmiLogOut.setEnabled(true);
                        //se coloca en la barra de menu el nombre de usuario
                        c_inicio.vista.setJtfUsuario(c_inicio.modelo.getRol_usuario().getFuncionario().getAlias());
                    } else {
                        mostrarMensaje("Atencion\n"
                                + "Usuario y/o contraseña incorrectos");
                    }
                } else {
                    mostrarMensaje("Atencion\n"
                            + "Usuario y/o contraseña incorrectos");
                }
            }
        });
    }

    private void saveConfig() {
        try {
            String port = vista.jtfPort.getText().trim();
            Config.setPort(port);
            String host = vista.jtfHost.getText().trim();
            Config.setHost(host);
            String database = vista.jtfDB.getText().trim();
            Config.setDB(database);
            JOptionPane.showMessageDialog(vista, "Cambios guardados", "Atención", JOptionPane.INFORMATION_MESSAGE);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(vista, "Ocurrio un error al guardar cambios. Intente nuevamente", "Atención", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (vista != null) {
            if (vista.btnAceptar == e.getSource()) {
                logIn();
            } else if (vista.btnSalir == e.getSource()) {
                System.exit(0);
            }
            if (e.getSource() == vista.txtNombre) {
                vista.txtPassword.requestFocus();
            }
            if (e.getSource() == vista.jbSaveConfig) {
                saveConfig();
            }
            if (e.getSource() == vista.txtPassword) {
                logIn();
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (this.vista.btnAceptar.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                logIn();
            }
        }
        if (this.vista.btnSalir.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                System.exit(0);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    protected void testReport() {
        M_proveedor unProveedor = new M_proveedor();
        unProveedor.setId(16);
        unProveedor.setEntidad("Invipint S.A.C.I");
        unProveedor.setNombre("Bambi");
        unProveedor.setRuc("80022935");
        unProveedor.setRuc_id("5");
        //ProductoCategoria pc = new ProductoCategoria(4, "Materia Prima");
        ProductoCategoria pc2 = new ProductoCategoria(2, "Diluyente");
        List<ProductoCategoria> listpc = new ArrayList<>();
        //listpc.add(pc);
        listpc.add(pc2);
        List<M_proveedor> listp = new ArrayList<>();
        listp.add(unProveedor);
        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        firstDay.set(Calendar.MONTH, 0);
        Calendar secDay = Calendar.getInstance();
        secDay.set(Calendar.DAY_OF_MONTH, 30);
        secDay.set(Calendar.MONTH, 2);
        C_create_excel c = new C_create_excel();
        c.exportacionProveedoresPorCategorias(listp, listpc, firstDay.getTime(), Calendar.getInstance().getTime());
    }
}
