/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearRollo;

import Entities.E_produccionFilm;
import Entities.E_productoClasificacion;
import Entities.Estado;
import Entities.M_producto;
import Interface.InterfaceRecibirProduccionFilm;
import com.nitido.utils.toaster.Toaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearFilm implements ActionListener, KeyListener {

    private M_crearFilm modelo;
    public V_crearFilm vista;
    private InterfaceRecibirProduccionFilm interfaceRecibirProduccionFilm;
    boolean modificar;
    int index;

    public C_crearFilm(M_crearFilm modelo, V_crearFilm vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.modificar = false;
        this.index = -1;
        inicializarVista();
        agregarListeners();
    }

    public void setInterface(InterfaceRecibirProduccionFilm interfaceRecibirProduccionFilm) {
        this.interfaceRecibirProduccionFilm = interfaceRecibirProduccionFilm;
    }

    public void mostrarVista() {
        this.vista.setSize(800, 600);
        this.vista.setLocationRelativeTo(this.vista.getParent());
        this.vista.setVisible(true);
    }

    private void inicializarVista() {
        ArrayList<E_productoClasificacion> productoClasificacion = modelo.obtenerTipoMateriaPrima();
        if (productoClasificacion.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No existen las categorias.", "Atención", JOptionPane.ERROR_MESSAGE);
        }
        for (int i = 0; i < productoClasificacion.size(); i++) {
            this.vista.jcbTipoMateriaPrima.addItem(productoClasificacion.get(i));
        }
        ArrayList<Estado> estado = modelo.obtenerEstado();
        for (int i = 0; i < estado.size(); i++) {
            this.vista.jcbEstadoFilm.addItem(estado.get(i));
        }
    }

    public void rellenarVista(M_producto rollo) {
        this.modelo.setProducto(rollo);
        this.vista.jtfProducto.setText(rollo.getDescripcion());
    }

    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jtfNroFilm.addActionListener(this);
        this.vista.jtfMedida.addActionListener(this);
        this.vista.jtfMicron.addActionListener(this);
        this.vista.jtfPeso.addActionListener(this);
        this.vista.jtfCono.addActionListener(this);
        /*
        KEYLISTENER
         */
        this.vista.jcbTipoMateriaPrima.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbCancelar.addKeyListener(this);
    }

    private void creaFilm() {
        if (!validarNroFilm()) {
            return;
        }
        if (!validarMedida()) {
            return;
        }
        if (!validarMicron()) {
            return;
        }
        if (!validarPeso()) {
            return;
        }
        if (!validarCono()) {
            return;
        }
        int nroFilm = Integer.valueOf(this.vista.jtfNroFilm.getText().trim());
        int cono = Integer.valueOf(this.vista.jtfCono.getText().trim());
        int medida = Integer.valueOf(this.vista.jtfMedida.getText().trim());
        int micron = Integer.valueOf(this.vista.jtfMicron.getText().trim());
        Double peso = Double.valueOf(this.vista.jtfPeso.getText().trim());
        int jcbTMPIndex = vista.jcbTipoMateriaPrima.getSelectedIndex();
        E_productoClasificacion productoClasificacion = vista.jcbTipoMateriaPrima.getItemAt(jcbTMPIndex);
        int jcbEstadoIndex = vista.jcbEstadoFilm.getSelectedIndex();
        Estado estado = vista.jcbEstadoFilm.getItemAt(jcbEstadoIndex);
        E_produccionFilm pf = new E_produccionFilm();
        pf.setNroFilm(nroFilm);
        pf.setCono(cono);
        pf.setMicron(micron);
        pf.setMedida(medida);
        pf.setPeso(peso);
        pf.setProductoClasificacion(productoClasificacion);
        pf.setEstado(estado);
        pf.setProducto(modelo.getProducto());
        if (modificar) {
            interfaceRecibirProduccionFilm.modificarFilm(index, pf);
        } else {
            interfaceRecibirProduccionFilm.recibirFilm(pf);
        }
        cerrar();
    }

    private boolean validarNroFilm() {
        int nroFilm = -1;
        try {
            nroFilm = Integer.valueOf(this.vista.jtfNroFilm.getText().trim());
            if (nroFilm < 0) {
                JOptionPane.showMessageDialog(vista, "El número de Film debe ser positivo", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un número de Film válido. Solo números enteros y positivos.", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarMedida() {
        int medida = -1;
        try {
            medida = Integer.valueOf(this.vista.jtfMedida.getText().trim());
            if (medida < 0) {
                JOptionPane.showMessageDialog(vista, "La medida debe ser un número positivo", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un número de medida válido. Solo números enteros y positivos.", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarMicron() {
        int micron = -1;
        try {
            micron = Integer.valueOf(this.vista.jtfMicron.getText().trim());
            if (micron < 0) {
                JOptionPane.showMessageDialog(vista, "El número de micrón debe ser positivo", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un número de micrón válido. Solo números enteros y positivos.", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCono() {
        int cono = -1;
        try {
            cono = Integer.valueOf(this.vista.jtfCono.getText().trim());
            if (cono < 0) {
                JOptionPane.showMessageDialog(vista, "El número de cono debe ser positivo", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un número de cono válido. Solo números enteros y positivos.", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarPeso() {
        Double peso = -1.0;
        try {
            peso = Double.valueOf(this.vista.jtfPeso.getText().trim());
            if (peso < 0) {
                JOptionPane.showMessageDialog(vista, "El peso debe ser positivo", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un peso válido. Solo números enteros y positivos.", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            creaFilm();
        } else if (e.getSource() == this.vista.jbCancelar) {
            cerrar();
        } else if (e.getSource() == this.vista.jtfNroFilm) {
            this.vista.jtfMedida.requestFocusInWindow();
        } else if (e.getSource() == this.vista.jtfMedida) {
            this.vista.jtfMicron.requestFocusInWindow();
        } else if (e.getSource() == this.vista.jtfMedida) {
            this.vista.jtfMicron.requestFocusInWindow();
        } else if (e.getSource() == this.vista.jtfMicron) {
            this.vista.jcbTipoMateriaPrima.requestFocusInWindow();
        } else if (e.getSource() == this.vista.jtfPeso) {
            this.vista.jtfCono.requestFocusInWindow();
        } else if (e.getSource() == this.vista.jtfCono) {
            this.vista.jbAceptar.requestFocusInWindow();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
            case KeyEvent.VK_ENTER: {
                if (this.vista.jcbTipoMateriaPrima.hasFocus()) {
                    this.vista.jtfPeso.requestFocusInWindow();
                }
                if (this.vista.jbAceptar.hasFocus()) {
                    creaFilm();
                }
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void modificarRollo(int index, E_produccionFilm rollo) {
        this.index = index;
        this.modificar = true;
        this.modelo.setProducto(rollo.getProducto());
        this.vista.jtfProducto.setText(rollo.getProducto().getDescripcion());
        this.vista.jtfNroFilm.setText(rollo.getNroFilm() + "");
        this.vista.jtfCono.setText(rollo.getCono() + "");
        this.vista.jtfMedida.setText(rollo.getMedida() + "");
        this.vista.jtfMicron.setText(rollo.getMicron() + "");
        this.vista.jtfPeso.setText(rollo.getPeso() + "");
        vista.jcbTipoMateriaPrima.setSelectedItem(rollo.getProductoClasificacion());
    }
}
