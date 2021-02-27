/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Empresa;

import Entities.E_Empresa;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class C_empresa implements ActionListener {

    M_empresa modelo;
    V_empresa vista;

    public C_empresa(V_empresa vista, M_empresa modelo) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarVista();
        agregarListeners();
    }

    /**
     * Establece el tamaño, posicion y visibilidad de la vista.
     */
    public void mostrarVista() {
        this.vista.setSize(800, 350);
        this.vista.setLocationRelativeTo(this.vista.getOwner());
        this.vista.setVisible(true);
    }

    /**
     * Elimina la vista.
     */
    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    /**
     * Agrega ActionListeners los controles.
     */
    private void agregarListeners() {
        this.vista.jbGuardar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
    }

    /**
     * Agrega valores a los componentes.
     */
    private void inicializarVista() {
        this.vista.jtfDireccion.setText(modelo.getDireccionEmpresa());
        this.vista.jtfEmail.setText(modelo.getEmpresa().getEmail());
        this.vista.jtfEntidad.setText(modelo.getEmpresa().getEntidad());
        this.vista.jtfNombre.setText(modelo.getEmpresa().getNombre());
        this.vista.jtfObservacion.setText(modelo.getEmpresa().getDescripcion());
        this.vista.jtfPaginaWeb.setText(modelo.getEmpresa().getPagWeb());
        this.vista.jtfRUC.setText(modelo.getEmpresa().getRuc());
        this.vista.jtfTelefono.setText(modelo.getEmpresa().getTelefono());
    }

    /**
     * Guarda los datos de la empresa.
     */
    private void guardarDatosEmpresariales() {
        /*
         * VALIDAR RAZON SOCIAL
         60 caracteres maximo
         */
        String entidad;
        if (this.vista.jtfEntidad.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo Razón social esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else if (this.vista.jtfEntidad.getText().length() > 100) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 100 para Razón social.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else {
            entidad = this.vista.jtfEntidad.getText().trim();
        }
        /*
         * VALIDAR NOMBRE FANTASIA
         */

        String nombreFantasia;
        if (this.vista.jtfNombre.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El Nombre de fanasía esta vacío.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else if (this.vista.jtfNombre.getText().length() > 100) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 100 para Nombre de fanasía.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else {
            nombreFantasia = this.vista.jtfNombre.getText().trim();
        }
        /*
         * VALIDAR R.U.C.
         */
        String ruc = this.vista.jtfRUC.getText().trim();
        if (ruc.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo R.U.C. esta vacio.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else if (this.vista.jtfRUC.getText().trim().length() > 30) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 30 para R.U.C.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        /*
        Validar direccion
         */
        String direccion = this.vista.jtfDireccion.getText().trim();
        if (direccion.length() > 250) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 250 para dirección.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else if (direccion.trim().isEmpty()) {
            direccion = null;
        }
        /*
        Validar email
         */
        String email = this.vista.jtfEmail.getText().trim();
        if (email.length() > 30) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 30 para e/mail.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else if (email.trim().isEmpty()) {
            email = null;
        }
        /*
        Validar página web
         */
        String pagWeb = this.vista.jtfPaginaWeb.getText().trim();
        if (pagWeb.length() > 30) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 30 para Web.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else if (pagWeb.trim().isEmpty()) {
            pagWeb = null;
        }
        /*
        Validar observación
         */
        String telefono = this.vista.jtfTelefono.getText().trim();
        if (telefono.length() > 250) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 15 para telefono.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else if (telefono.trim().isEmpty()) {
            telefono = null;
        }
        /*
        Validar observación
         */
        String observacion = this.vista.jtfObservacion.getText().trim();
        if (observacion.length() > 250) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El máximo permitido de caracteres es 250 para Observacion.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else if (observacion.trim().isEmpty()) {
            observacion = null;
        }
        E_Empresa empresa = new E_Empresa();
        empresa.setDireccion(direccion);
        empresa.setEmail(email);
        empresa.setEntidad(entidad);
        empresa.setNombre(nombreFantasia);
        empresa.setDescripcion(observacion);
        empresa.setPagWeb(pagWeb);
        empresa.setRuc(ruc);
        empresa.setTelefono(telefono);
        if (modelo.modificarDatosEmpresa(empresa) > -1) {
            JOptionPane.showMessageDialog(vista, "Los cambios se guardaron correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(vista.jbSalir)) {
            cerrar();
        } else if (source.equals(vista.jbGuardar)) {
            guardarDatosEmpresariales();
        }
    }
}
