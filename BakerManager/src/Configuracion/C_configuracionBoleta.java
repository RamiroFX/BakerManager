/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import DB.DB_Preferencia;
import Entities.E_Divisa;
import Entities.E_impresionOrientacion;
import Entities.M_campoImpresion;
import Entities.M_preferenciasImpresion;
import Interface.crearModificarParametroCallback;
import Impresora.Impresora;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Ramiro
 */
public class C_configuracionBoleta extends MouseAdapter implements ActionListener, KeyListener, MouseListener, crearModificarParametroCallback {

    private static final int CREAR_PARAMETRO = 1, MODIFICAR_PARAMETRO = 2;
    private V_configuracionFactura vista;
    private M_configuracionBoleta modelo;

    public C_configuracionBoleta(V_configuracionFactura vista, M_configuracionBoleta modelo) {
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
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jbAgregarCampo.addActionListener(this);
        this.vista.jbModificarCampo.addActionListener(this);
        this.vista.jbHabilitarDeshabilitarCampo.addActionListener(this);
        this.vista.jbImprimirPaginaPrueba.addActionListener(this);
        this.vista.jbOcultarMostrarCampo.addActionListener(this);
        this.vista.jbGuardarPreferencias.addActionListener(this);
        this.vista.jbGuardarImpresora.addActionListener(this);
        this.vista.jtFactura.addMouseListener(this);
    }

    /**
     * Agrega valores a los componentes.
     */
    private void inicializarVista() {
        this.vista.setTitle("Configuración de boleta");
        this.vista.setName("configuracion_boleta");
        this.vista.jbModificarCampo.setEnabled(false);
        this.vista.jbHabilitarDeshabilitarCampo.setEnabled(false);
        this.vista.jtFactura.setModel(modelo.getImpresionBoletaTM());
        //panel de preferencia
        for (int i = 0; i < modelo.getFormatoFechas().length; i++) {
            this.vista.jcbFormatoFecha.addItem(modelo.getFormatoFechas()[i]);
        }
        for (int i = 1; i < modelo.getMaxProducts() + 1; i++) {
            this.vista.jcbCantProd.addItem(i);
        }
        for (int i = 1; i < modelo.getMaxLetterSize(); i++) {
            this.vista.jcbTamañoLetra.addItem(i);
        }
        this.vista.jcbMoneda.addItem(new E_Divisa(1, "Guaraní/es"));
        this.vista.jtfDistanciaEntreCopias.setText(modelo.getPreferenciasImpresion().getDistanceBetweenCopies() + "");
        this.vista.jtfTipoLetra.setText(modelo.getPreferenciasImpresion().getLetterFont());
        this.vista.jcbCantProd.setSelectedItem(modelo.getPreferenciasImpresion().getMaxProducts());
        this.vista.jcbTamañoLetra.setSelectedItem(modelo.getPreferenciasImpresion().getLetterSize());
        this.vista.jcbFormatoFecha.setSelectedItem(modelo.getPreferenciasImpresion().getFormatoFecha());
        if (modelo.getPreferenciasImpresion().getIdDuplicado() == 1) {
            this.vista.jchkDuplicado.setSelected(true);
        } else {
            this.vista.jchkDuplicado.setSelected(false);
        }
        if (modelo.getPreferenciasImpresion().getIdTriplicado() == 1) {
            this.vista.jchkTriplicado.setSelected(true);
        } else {
            this.vista.jchkTriplicado.setSelected(false);
        }
        if (modelo.getPreferenciasImpresion().getImprimirMoneda() == 1) {
            this.vista.jchkMoneda.setSelected(true);
        } else {
            this.vista.jchkMoneda.setSelected(false);
        }
        this.vista.jcbOrientacion.setVisible(false);
        this.vista.jlOrientacion.setVisible(false);
        this.vista.jtfNombreImpresora.setText(modelo.getPreferenciasImpresion().getNombreImpresora());
        this.vista.jtfAnchoPapel.setText(modelo.getPreferenciasImpresion().getAnchoPagina() + "");
        this.vista.jtfLargoPapel.setText(modelo.getPreferenciasImpresion().getLargoPagina() + "");
        this.vista.jtfMargenX.setText(modelo.getPreferenciasImpresion().getMargenX() + "");
        this.vista.jtfMargenY.setText(modelo.getPreferenciasImpresion().getMargenY() + "");
    }

    private void agregarCampo() {
        V_crearModificarCampoImpresion cmci = new V_crearModificarCampoImpresion(CREAR_PARAMETRO, this.vista);
        cmci.setCallback(this);
        cmci.setVisible(true);
        this.modelo.updateTable();
    }

    private void modificarCampo() {
        int row = vista.jtFactura.getSelectedRow();
        if (row > -1) {
            M_campoImpresion ci = modelo.getImpresionBoletaTM().getValueFromList(row);
            V_crearModificarCampoImpresion cmci = new V_crearModificarCampoImpresion(MODIFICAR_PARAMETRO, this.vista, ci);
            cmci.setCallback(this);
            cmci.setVisible(true);
            this.modelo.updateTable();
            this.vista.jbModificarCampo.setEnabled(false);
            this.vista.jbHabilitarDeshabilitarCampo.setEnabled(false);
        }
    }

    private void quitarCampo() {
        int row = vista.jtFactura.getSelectedRow();
        if (row > -1) {
            modelo.habilitarDeshabilitarCampo(row);
            this.modelo.updateTable();
            this.vista.jbModificarCampo.setEnabled(false);
            this.vista.jbHabilitarDeshabilitarCampo.setEnabled(false);
        }
    }

    private void ocultarMostrarCampo() {
        modelo.ocultarMostrarCampo();
        this.vista.jbModificarCampo.setEnabled(false);
        this.vista.jbHabilitarDeshabilitarCampo.setEnabled(false);
        if (modelo.isIsVisible()) {
            modelo.setIsVisible(false);
        } else {
            modelo.setIsVisible(true);
        }
    }

    private void guardarPreferencia() {
        M_preferenciasImpresion pi = new M_preferenciasImpresion();
        Integer distancia;
        if (this.vista.jtfDistanciaEntreCopias.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Distancia entre copias.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfDistanciaEntreCopias.setText("0");
            this.vista.jtfDistanciaEntreCopias.requestFocusInWindow();
            return;
        }
        try {
            String cantidad = this.vista.jtfDistanciaEntreCopias.getText().trim();
            distancia = Integer.valueOf(cantidad);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Distancia entre copias.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfDistanciaEntreCopias.setText("0");
            this.vista.jtfDistanciaEntreCopias.requestFocusInWindow();
            return;
        }
        if (distancia > 10000) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido menos a 10.000\n"
                    + "en el campo Distancia entre copias.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfDistanciaEntreCopias.setText("0");
            this.vista.jtfDistanciaEntreCopias.requestFocusInWindow();
            return;
        }
        Integer anchoPagina;
        if (this.vista.jtfAnchoPapel.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Ancho de papel.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfAnchoPapel.setText("0");
            this.vista.jtfAnchoPapel.requestFocusInWindow();
            return;
        }
        try {
            String anchoPaginaTxt = this.vista.jtfAnchoPapel.getText().trim();
            anchoPagina = Integer.valueOf(anchoPaginaTxt);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Ancho de papel.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfAnchoPapel.setText("0");
            this.vista.jtfAnchoPapel.requestFocusInWindow();
            return;
        }
        if (anchoPagina > 10000) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido menos a 10.000\n"
                    + "en el campo Ancho de papel.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfAnchoPapel.setText("0");
            this.vista.jtfAnchoPapel.requestFocusInWindow();
            return;
        }

        Integer largoPagina;
        if (this.vista.jtfLargoPapel.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Largo de papel.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfLargoPapel.setText("0");
            this.vista.jtfLargoPapel.requestFocusInWindow();
            return;
        }
        try {
            String largoPaginaTxt = this.vista.jtfLargoPapel.getText().trim();
            largoPagina = Integer.valueOf(largoPaginaTxt);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Largo de papel.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfLargoPapel.setText("0");
            this.vista.jtfLargoPapel.requestFocusInWindow();
            return;
        }
        if (largoPagina > 10000) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido menos a 10.000\n"
                    + "en el campo Largo de papel.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfLargoPapel.setText("0");
            this.vista.jtfLargoPapel.requestFocusInWindow();
            return;
        }
        Double margenX;
        if (this.vista.jtfMargenX.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Margen X.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfMargenX.setText("0");
            this.vista.jtfMargenX.requestFocusInWindow();
            return;
        }
        try {
            String margenXTxt = this.vista.jtfMargenX.getText().trim();
            margenX = Double.valueOf(margenXTxt);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Margen X.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfMargenX.setText("0");
            this.vista.jtfMargenX.requestFocusInWindow();
            return;
        }
        if (margenX > 10000) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido menos a 10.000\n"
                    + "en el campo Margen X.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfMargenX.setText("0");
            this.vista.jtfMargenX.requestFocusInWindow();
            return;
        }
        Double margenY;
        if (this.vista.jtfMargenY.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Margen Y.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfMargenY.setText("0");
            this.vista.jtfMargenY.requestFocusInWindow();
            return;
        }
        try {
            String margenYTxt = this.vista.jtfMargenY.getText().trim();
            margenY = Double.valueOf(margenYTxt);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Margen Y.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfMargenY.setText("0");
            this.vista.jtfMargenY.requestFocusInWindow();
            return;
        }
        if (margenY > 10000) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido menos a 10.000\n"
                    + "en el campo Margen Y.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.vista.jtfMargenY.setText("0");
            this.vista.jtfMargenY.requestFocusInWindow();
            return;
        }
        String tipoLetra;
        if (this.vista.jtfTipoLetra.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo nombre esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else {
            if (this.vista.jtfTipoLetra.getText().trim().length() > 30) {
                javax.swing.JOptionPane.showMessageDialog(this.vista,
                        "El campo tipo letra sobrepasa el limite permitido(30) de caracteres",
                        "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                return;
            } else {
                tipoLetra = this.vista.jtfTipoLetra.getText().trim();
            }
        }
        String nombreImpresora;
        if (this.vista.jtfNombreImpresora.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "El campo nombre de impresora esta vacio",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        } else {
            if (this.vista.jtfNombreImpresora.getText().trim().length() > 30) {
                javax.swing.JOptionPane.showMessageDialog(this.vista,
                        "El campo nombre de impresora sobrepasa el limite permitido(30) de caracteres",
                        "Parametros incorrectos",
                        javax.swing.JOptionPane.OK_OPTION);
                return;
            } else {
                nombreImpresora = this.vista.jtfNombreImpresora.getText().trim();
            }
        }
        pi.setDistanceBetweenCopies(distancia);
        pi.setNombreImpresora(nombreImpresora);
        pi.setAnchoPagina(anchoPagina);
        pi.setLargoPagina(largoPagina);
        pi.setMargenX(margenX);
        pi.setMargenY(margenY);
        pi.setDivisa(new E_Divisa(1, "Guaraní/es"));
        if (this.vista.jchkDuplicado.isSelected()) {
            pi.setIdDuplicado(1);
        } else {
            pi.setIdDuplicado(2);
        }
        if (this.vista.jchkTriplicado.isSelected()) {
            pi.setIdTriplicado(1);
        } else {
            pi.setIdTriplicado(2);
        }
        if (this.vista.jchkMoneda.isSelected()) {
            pi.setImprimirMoneda(1);
        } else {
            pi.setImprimirMoneda(2);
        }
        pi.setLetterFont(tipoLetra);
        pi.setMaxProducts(this.vista.jcbCantProd.getSelectedIndex() + 1);
        pi.setLetterSize(this.vista.jcbTamañoLetra.getSelectedIndex() + 1);
        pi.setFormatoFecha(this.vista.jcbFormatoFecha.getSelectedItem() + "");
        pi.setOrientacion(new E_impresionOrientacion(1, "vertical"));
        modelo.guardarPreferencias(pi);
        Impresora.PREF_PRINT_BOLETA = DB_Preferencia.obtenerPreferenciaImpresionBoleta();
        javax.swing.JOptionPane.showMessageDialog(this.vista, "Cambios guardados",
                "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void imprimirBoletaPrueba() {
        Impresora.imprimirBoletaPrueba();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbCancelar) {
            cerrar();
        } else if (e.getSource() == this.vista.jbAgregarCampo) {
            agregarCampo();
        } else if (e.getSource() == this.vista.jbModificarCampo) {
            modificarCampo();
        } else if (e.getSource() == this.vista.jbHabilitarDeshabilitarCampo) {
            quitarCampo();
        } else if (e.getSource() == this.vista.jbImprimirPaginaPrueba) {
            imprimirBoletaPrueba();
        } else if (e.getSource() == this.vista.jbOcultarMostrarCampo) {
            ocultarMostrarCampo();
        } else if (e.getSource() == this.vista.jbGuardarPreferencias) {
            guardarPreferencia();
        } else if (e.getSource() == this.vista.jbGuardarImpresora) {
            guardarPreferencia();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtFactura)) {
            int fila = this.vista.jtFactura.rowAtPoint(e.getPoint());
            int columna = this.vista.jtFactura.columnAtPoint(e.getPoint());
            if ((fila > -1) && (columna > -1)) {
                this.vista.jbModificarCampo.setEnabled(true);
                this.vista.jbHabilitarDeshabilitarCampo.setEnabled(true);
                if (e.getClickCount() == 2) {
                    int row = vista.jtFactura.getSelectedRow();
                    M_campoImpresion ci = modelo.getImpresionBoletaTM().getValueFromList(row);
                    modificarParametroImpresion(ci);
                }
            } else {
                this.vista.jbModificarCampo.setEnabled(false);
                this.vista.jbHabilitarDeshabilitarCampo.setEnabled(false);
            }
        }
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

    @Override
    public void recibirParametroImpresion(M_campoImpresion ci) {
        //modelo.crearParametro(ci);
    }

    @Override
    public void modificarParametroImpresion(M_campoImpresion ci) {
        modelo.modificarParametro(ci);
    }
}
