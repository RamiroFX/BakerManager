package Caja;

import Entities.M_menu_item;
import Interface.GestionInterface;
import MenuPrincipal.DatosUsuario;
import bakermanager.C_inicio;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionCaja implements GestionInterface {

    V_gestionCaja vista;
    M_gestionCaja modelo;
    C_inicio c_inicio;

    public C_gestionCaja(V_gestionCaja vista, M_gestionCaja modelo, C_inicio c_inicio) {
        this.vista = vista;
        this.modelo = modelo;
        this.c_inicio = c_inicio;
        this.vista.setLocation(c_inicio.centrarPantalla(this.vista));
        callMethods();
    }

    private void callMethods() {
        inicializarVista();
        concederPermisos();
    }

    @Override
    public void inicializarVista() {
        Date date = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(date);
        this.vista.jddFinal.setDate(date);
    }

    @Override
    public void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
            if (this.vista.jbAgregar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbAgregar.setEnabled(true);
                this.vista.jbAgregar.addActionListener(this);
            }
            if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbResumen.addActionListener(this);
            }
            if (this.vista.jbDetalle.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbDetalle.addActionListener(this);
            }
            if (this.vista.jbBuscar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbBuscar.setEnabled(true);
                this.vista.jcbCondCompra.setEnabled(true);
                this.vista.jtfNroFactura.setEnabled(true);
                this.vista.jddFinal.setEnabled(true);
                this.vista.jddInicio.setEnabled(true);
                this.vista.jbBuscar.addActionListener(this);
                this.vista.jbCliente.addActionListener(this);
                this.vista.jbEmpleado.addActionListener(this);
                this.vista.jbBorrar.addActionListener(this);
                this.vista.jbBuscarDetalle.addActionListener(this);
            }
        }
        this.vista.jtIngresoCabecera.table.addMouseListener(this);
        //this.vista.jbGraficos.addActionListener(this);
        this.vista.jtIngresoCabecera.table.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbAgregar.addKeyListener(this);
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jbDetalle.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jcbCondCompra.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbBuscarDetalle.addKeyListener(this);
    }

    @Override
    public void mostrarVista() {
        this.c_inicio.agregarVentana(vista);
    }

    @Override
    public void cerrar() {
        this.vista.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src.equals(this.vista.jbAgregar)){
            SaldarCaja sc = new SaldarCaja(c_inicio);
            sc.setVisible(true);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
