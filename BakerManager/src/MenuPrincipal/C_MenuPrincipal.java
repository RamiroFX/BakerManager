/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MenuPrincipal;

import Entities.M_menu_item;
import bakermanager.C_inicio;
import empleado.Gestion_empleado;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JInternalFrame;

/**
 *
 * @author Usuario
 */
public class C_MenuPrincipal implements ActionListener {

    public V_MenuPrincipal vista;
    public C_inicio c_inicio;

    public C_MenuPrincipal(V_MenuPrincipal vista, C_inicio inicio) {
        this.vista = vista;
        this.c_inicio = inicio;
        inicializarVista();
        agregarListeners();
    }

    private void inicializarVista() {
        this.vista.jftFecha.setValue(Calendar.getInstance().getTime());
        this.vista.jbEmpleados.setEnabled(false);
        this.vista.jbMovimientos.setEnabled(false);
        this.vista.jbProveedores.setEnabled(false);
        this.vista.jbProducto.setEnabled(false);
        ArrayList<M_menu_item> accesos = c_inicio.modelo.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbEmpleados.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbEmpleados.setEnabled(true);
            }
            if (this.vista.jbMovimientos.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbMovimientos.setEnabled(true);
            }
            if (this.vista.jbProducto.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbProducto.setEnabled(true);
            }
            if (this.vista.jbProveedores.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbProveedores.setEnabled(true);
            }
        }
    }

    private void agregarListeners() {
        this.vista.jbProveedores.addActionListener(this);
        this.vista.jbProducto.addActionListener(this);
        this.vista.jbMovimientos.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbEmpleados.addActionListener(this);
    }

    void mostrarVista() {
        this.vista.setVisible(true);
        this.c_inicio.agregarVentana(vista);
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        //this.c_inicio.centrarPantalla(vista);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbEmpleados)) {
            Gestion_empleado ges_usuario = new Gestion_empleado(c_inicio);
            ges_usuario.mostrarVista();
        } else if (src.equals(this.vista.jbSalir)) {
            System.exit(0);
        }
    }

    public Point centrarPantalla(JInternalFrame i) {
        /*con este codigo centramos el panel en el centro del contenedor
         la anchura del contenedor menos la anchura de nuestro componente divido a 2
         lo mismo con la altura.*/
        return new Point((this.vista.getWidth() - i.getWidth()) / 2, (this.vista.getHeight() - i.getHeight() - 45) / 2);
    }

    public Dimension establecerTamañoPanel() {
        return new Dimension((int) (this.vista.getWidth() * 0.8), (int) (this.vista.getHeight() * 0.8));
    }
}
