/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

import Configuracion.ConfiguracionBoleta;
import Configuracion.ConfiguracionFactura;
import Configuracion.ConfiguracionTicket;
import Configuracion.Timbrado.GestionTimbrado;
import Empresa.Empresa;
import Entities.M_rol_usuario;
import Login.Login;
import Produccion.GestionProduccion;
import UsoMateriaPrima.GestionMateriaPrima;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;

/**
 *
 * @author Ramiro
 */
/*
 * Esta clase gerencia la comunicacion entre la barra de menu, el logIn y la
 * pantalla principal.
 */
public class C_inicio implements ActionListener {

    public V_inicio vista = null;//referencia a la ventana principal.
    public M_inicio modelo;
    private escuchadorVentana wa;

    public C_inicio(V_inicio mainFrame, M_inicio modelo) {
        this.modelo = modelo;
        this.vista = mainFrame;
        this.vista.setSize(obtenerDimensionDePantalla());
        wa = new escuchadorVentana();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void agregarListeners() {
        //vista.getJMenuBar().jmiLogIn.addActionListener(this);
        //vista.getJMenuBar().jmiLogOut.addActionListener(this);
        vista.getJMenuBar().jmiCerrar.addActionListener(this);
        vista.getJMenuBar().jmiConfigImpresion.addActionListener(this);
        vista.getJMenuBar().jmiConfigImpresionTicket.addActionListener(this);
        vista.getJMenuBar().jmiConfigImpresionBoleta.addActionListener(this);
        vista.getJMenuBar().jmiGestionTimbrado.addActionListener(this);
        vista.getJMenuBar().jmiEmpresa.addActionListener(this);
        //TODO remove
        vista.getJMenuBar().jmiProduccion.addActionListener(this);
        vista.getJMenuBar().jmiMateriaPrima.addActionListener(this);
        vista.getJMenuBar().jmiCobroPendiente.addActionListener(this);
        vista.getJMenuBar().jmiCobroPendienteVencidos.addActionListener(this);
        vista.getJMenuBar().jmiCobroPendientePorCliente.addActionListener(this);
        vista.getJMenuBar().jmiCobroPendientePorClienteVencidos.addActionListener(this);
        vista.getJMenuBar().jmiEstadoCuentaClientes.addActionListener(this);
        vista.getJMenuBar().jmiEstadoCuentaProveedores.addActionListener(this);
        vista.getJMenuBar().jmiCompras.addActionListener(this);
        vista.addWindowListener(wa);
        
    }

    public void agregarVentana(JInternalFrame mdi) {
        vista.getDesktop().add(mdi);
        vista.setCurrentJIF(mdi);
        mdi.setVisible(true);
        mdi.moveToFront();
        try {
            mdi.setSelected(true);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }
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
        pantalla.height = pantalla.height - 25;
        return pantalla;
    }

    /**
     * Para poder agregar una ventana cualquiera que sea JInternalFrame o
     * heredados de ella sirve este metodo, solo tienes que crear el
     * JIternalFrame como si fuese un JFrame o Frame normal y lo agregas aquí.
     *
     * @param mdi JInternalFrame
     */
    public Point centrarPantalla(JInternalFrame i) {
        /*con este codigo centramos el panel en el centro del contenedor
         la anchura del contenedor menos la anchura de nuestro componente divido a 2
         lo mismo con la altura.*/
        return new Point((vista.getWidth() - i.getWidth()) / 2, (vista.getHeight() - i.getHeight() - 45) / 2);
    }

    public Dimension establecerTamañoPanel() {
        return new Dimension((int) (vista.getWidth() * 0.8), (int) (vista.getHeight() * 0.8));
    }

    /**
     * @return the rol_usuario
     */
    public M_rol_usuario getRol_usuario() {
        return this.modelo.getRol_usuario();
    }

    /**
     * @param rol_usuario the rol_usuario to set
     */
    public void setRol_usuario(M_rol_usuario rol_usuario) {
        this.modelo.setRol_usuario(rol_usuario);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();

        //Object enter = ActionEvent.KEY_EVENT_MASK;
        /*if (fuente == vista.getJMenuBar().jmiLogIn) {
         //se verifica que el panel login no se encuentre creado.
         mostrarIngreso();
         vista.getJMenuBar().jmiLogIn.setEnabled(false);
         vista.getJMenuBar().jmiLogOut.setEnabled(true);
         } else if (fuente == vista.getJMenuBar().jmiLogOut) {
         //frame.getDesktop().getDesktopManager().closeFrame(frame.getCurrentJIF());
         vista.getDesktop().removeAll();
         vista.getDesktop().updateUI();
         vista.setJtfUsuario("");
         vista.getJMenuBar().jmiLogIn.setEnabled(true);
         vista.getJMenuBar().jmiLogOut.setEnabled(false);
         } else*/
        if (fuente == vista.getJMenuBar().jmiCerrar) {
            System.exit(0);
        } else if (fuente == vista.getJMenuBar().jmiConfigImpresion) {
            ConfiguracionFactura config = new ConfiguracionFactura(vista);
            config.mostrarVista();
        } else if (fuente == vista.getJMenuBar().jmiConfigImpresionTicket) {
            ConfiguracionTicket config = new ConfiguracionTicket(vista);
            config.mostrarVista();
        } else if (fuente == vista.getJMenuBar().jmiConfigImpresionBoleta) {
            ConfiguracionBoleta config = new ConfiguracionBoleta(vista);
            config.mostrarVista();
        } else if (fuente == vista.getJMenuBar().jmiGestionTimbrado) {
            GestionTimbrado gestionTimbrado = new GestionTimbrado(this);
            gestionTimbrado.mostrarVista();
        } else if (fuente == vista.getJMenuBar().jmiEmpresa) {
            Empresa empresa = new Empresa(vista);
            empresa.mostrarVista();
        } else if (fuente == vista.getJMenuBar().jmiProduccion) {
            GestionProduccion gp = new GestionProduccion(this);
            gp.mostrarVista();
        } else if (fuente == vista.getJMenuBar().jmiMateriaPrima) {
            GestionMateriaPrima gp = new GestionMateriaPrima(this);
            gp.mostrarVista();
        } else if (fuente == vista.getJMenuBar().jmiCobroPendiente) {
            Reportes.ReportesCobros.cobrosPendientes(vista);
        } else if (fuente == vista.getJMenuBar().jmiCobroPendienteVencidos) {
            Reportes.ReportesCobros.cobrosPendientesVencidos(vista);
        } else if (fuente == vista.getJMenuBar().jmiCobroPendientePorCliente) {
            Reportes.ReportesCobros.cobrosPendientesPorCliente(vista);
        } else if (fuente == vista.getJMenuBar().jmiCobroPendientePorClienteVencidos) {
            Reportes.ReportesCobros.cobrosPendientesPorClienteVencidos(vista);
        }else if (fuente == vista.getJMenuBar().jmiEstadoCuentaClientes) {
            Reportes.ReportesCobros.estadoCuentaClientes(vista);
        }else if (fuente == vista.getJMenuBar().jmiEstadoCuentaProveedores) {
            Reportes.ReportesPagos.estadoCuentaProveedores(vista);
        }else if (fuente == vista.getJMenuBar().jmiCompras) {
            Reportes.ReportesCompras.compras(vista);
        }
    }

    public void mostrarIngreso() {
        Login login = new Login(this);
    }

    private class escuchadorVentana extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
