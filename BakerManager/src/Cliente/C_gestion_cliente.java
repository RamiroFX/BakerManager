/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import DB.DB_Cliente;
import Entities.M_cliente;
import Entities.M_cliente_contacto;
import Entities.M_menu_item;
import Interface.GestionInterface;
import MenuPrincipal.DatosUsuario;
import bakermanager.C_inicio;
import java.awt.AlphaComposite;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestion_cliente implements GestionInterface {

    public C_inicio c_inicio;
    private M_cliente m_cliente;
    private M_cliente_contacto contacto;
    int idCliente, idContacto;
    V_gestion_cliente vista;
    String imagePath;
    ImageIcon image;
    File fileImage;

    public C_gestion_cliente(C_inicio c_inicio) {
        this.c_inicio = c_inicio;
        this.m_cliente = new M_cliente();
        this.vista = new V_gestion_cliente();
        this.vista.setLocation(c_inicio.centrarPantalla(this.vista));
        inicializarVista();
        concederPermisos();
    }

    /**
     * @return the m_funcionario
     */
    public M_cliente getCliente() {
        return m_cliente;
    }

    /**
     * @param m_funcionario the m_funcionario to set
     */
    public void setCliente(M_cliente m_proveedor) {
        this.m_cliente = m_proveedor;
    }

    @Override
    public final void inicializarVista() {
        this.vista.jtCliente.setModel(DB_Cliente.consultarCliente("", false, true, true));
        Utilities.c_packColumn.packColumns(this.vista.jtCliente, 2);
        this.vista.jbCrearCliente.setEnabled(false);
        this.vista.jbModificarCliente.setEnabled(false);
        this.vista.jckbEntidadNombre.setEnabled(false);
        this.vista.jckbRuc.setEnabled(false);
        this.vista.jrbExclusivo.setEnabled(false);
        this.vista.jrbInclusivo.setEnabled(false);
    }

    /**
     * Establece el tamaño, posicion y visibilidad de la vista.
     */
    public void mostrarVista() {
        //this.vista.setSize(this.c_inicio.establecerTamañoPanel());
        //this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        this.c_inicio.agregarVentana(this.vista);
    }

    /**
     * Elimina la vista.
     */
    @Override
    public final void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    /**
     * Agrega ActionListeners los controles.
     */
    @Override
    public final void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbCrearCliente.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbCrearCliente.setEnabled(true);
                this.vista.jbCrearCliente.addActionListener(this);
            }
            if (this.vista.jbModificarCliente.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbModificarCliente.addActionListener(this);
            }
            if (this.vista.jtfBuscar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jtfBuscar.setEnabled(true);
                this.vista.jckbEntidadNombre.setEnabled(true);
                this.vista.jckbRuc.setEnabled(true);
                this.vista.jrbExclusivo.setEnabled(true);
                this.vista.jrbInclusivo.setEnabled(true);
                this.vista.jtfBuscar.addKeyListener(this);
                this.vista.jrbExclusivo.addActionListener(this);
                this.vista.jrbInclusivo.addActionListener(this);
                this.vista.jckbEntidadNombre.addActionListener(this);
                this.vista.jckbRuc.addActionListener(this);
            }
        }
        this.vista.jtCliente.addMouseListener(this);
        this.vista.jtCliente.addKeyListener(this);
        this.vista.jtContacto.addMouseListener(this);
    }

    public void displayQueryResults() {
        /*
         * Para permitir que los mensajes puedan ser desplegados, no se ejecuta
         * el query directamente, sino que se lo coloca en una cola de eventos
         * para que se ejecute luego de los eventos pendientes.
         */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                /*
                 * Se utiliza el objeto factory para obtener un TableModel
                 * para los resultados del query.
                 */
                boolean isExclusivo = false;
                boolean entidad = false;
                boolean ruc = false;
                if (vista.jrbExclusivo.isSelected()) {
                    isExclusivo = true;
                }
                if (vista.jckbEntidadNombre.isSelected()) {
                    entidad = true;
                }
                if (vista.jckbRuc.isSelected()) {
                    ruc = true;
                }
                String busqueda = vista.jtfBuscar.getText();
                vista.jtCliente.setModel(DB_Cliente.consultarCliente(busqueda, isExclusivo, entidad, ruc));
                Utilities.c_packColumn.packColumns(vista.jtCliente, 2);
                vista.jbModificarCliente.setEnabled(false);
            }
        });
    }

    BufferedImage createResizedCopy(Image originalImage,
            int scaledWidth, int scaledHeight,
            boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public boolean isValidImage(File fileImage) {
        this.fileImage = fileImage;
        ImageIcon imagen = new ImageIcon(fileImage.getPath());
        if (imagen.getIconHeight() > 200 && imagen.getIconWidth() > 200) {
            try {
                imagen.setImage(createResizedCopy(imagen.getImage(), 200, 200, false));
                return true;
            } catch (Exception e) {
                this.fileImage = null;
                return false;
            }
        } else {
            return true;
        }

    }

    private void borrarDatosContacto() {
        this.vista.jtfNombreContacto.setText("");
        this.vista.jtfApellidoContacto.setText("");
        this.vista.jtfTelefonoContacto.setText("");
        this.vista.jtfCiudad.setText("");
        this.vista.jtfEstadoCivil.setText("");
        this.vista.jtfGenero.setText("");
        this.vista.jtfNacionalidad.setText("");
        this.vista.jftFechaNacimiento.setValue("");
        this.vista.jtfCorreoElecContacto.setText("");
        this.vista.jtfDireccionContacto.setText("");
        this.vista.jtfObservacionContacto.setText("");
        this.vista.jftCedulaIdentidad.setValue("");
    }

    private void completarCampos() {
        borrarDatosContacto();
        Integer idClie = Integer.valueOf(String.valueOf(this.vista.jtCliente.getValueAt(this.vista.jtCliente.getSelectedRow(), 0)));
        setCliente(DB_Cliente.obtenerDatosClienteID(idClie));
        this.vista.jtfEntidad.setText(getCliente().getEntidad());
        this.vista.jtfNombre.setText(getCliente().getNombre());
        if (null == getCliente().getRuc()) {
            this.vista.jtfRUC.setText("");
        } else {
            String rucId = getCliente().getRucId();
            if (rucId == null) {
                this.vista.jtfRUC.setText(getCliente().getRuc());
            } else {
                this.vista.jtfRUC.setText(getCliente().getRuc() + "-" + rucId);
            }
        }
        this.vista.jtfObservacion.setText(getCliente().getObservacion());
        this.vista.jtfDireccion.setText(getCliente().getDireccion());
        this.vista.jtfTipoCliente.setText(getCliente().getTipo());
        this.vista.jtfCategoriaCliente.setText(getCliente().getCategoria());

        this.vista.jtTelefono.setModel(DB_Cliente.obtenerClienteTelefono(idClie));
        Utilities.c_packColumn.packColumns(this.vista.jtTelefono, 1);
        this.vista.jtSucursal.setModel(DB_Cliente.obtenerSucursal(idClie));
        Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
        this.vista.jtContacto.setModel(DB_Cliente.obtenerClienteContacto(idClie));
        Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);

        verificarAcceso();
    }

    private void verificarAcceso() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbModificarCliente.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbModificarCliente.setEnabled(true);
            }
        }
    }

    private void modificarCliente() {
        int row = this.vista.jtCliente.getSelectedRow();
        if (row > -1) {
            int id = (Integer.valueOf((String) this.vista.jtCliente.getValueAt(row, 0)));
            Modificar_cliente modificarCliente = new Modificar_cliente(this, id);
            modificarCliente.mostrarVista();
            this.vista.jbModificarCliente.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un cliente", "Atención", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbCrearCliente)) {
            Crear_cliente crear_cliente = new Crear_cliente(this);
            crear_cliente.mostrarVista();
        }
        if (e.getSource().equals(this.vista.jbModificarCliente)) {
            modificarCliente();
        }
        if (e.getSource().equals(this.vista.jrbExclusivo)) {
            displayQueryResults();
        }
        if (e.getSource().equals(this.vista.jrbInclusivo)) {
            displayQueryResults();
        }
        if (e.getSource().equals(this.vista.jckbEntidadNombre)) {
            displayQueryResults();
        }
        if (e.getSource().equals(this.vista.jckbRuc)) {
            displayQueryResults();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtCliente)) {
            borrarDatosContacto();
            int fila = this.vista.jtCliente.rowAtPoint(e.getPoint());
            int columna = this.vista.jtCliente.columnAtPoint(e.getPoint());
            idCliente = (Integer.valueOf((String) this.vista.jtCliente.getValueAt(fila, 0)));
            setCliente(DB_Cliente.obtenerDatosClienteID(idCliente));
            if ((fila > -1) && (columna > -1)) {
                this.vista.jtfEntidad.setText(getCliente().getEntidad());
                this.vista.jtfNombre.setText(getCliente().getNombre());
                if (null == getCliente().getRuc()) {
                    this.vista.jtfRUC.setText("");
                } else {
                    String rucId = getCliente().getRucId();
                    if (rucId == null) {
                        this.vista.jtfRUC.setText(getCliente().getRuc());
                    } else {
                        this.vista.jtfRUC.setText(getCliente().getRuc() + "-" + rucId);
                    }
                }
                this.vista.jtfObservacion.setText(getCliente().getObservacion());
                this.vista.jtfDireccion.setText(getCliente().getDireccion());
                this.vista.jtfTipoCliente.setText(getCliente().getTipo());
                this.vista.jtfCategoriaCliente.setText(getCliente().getCategoria());
                this.vista.jtfPaginaWebCliente.setText(getCliente().getPaginaWeb());
                this.vista.jtfEmailCliente.setText(getCliente().getEmail());

                this.vista.jtTelefono.setModel(DB_Cliente.obtenerClienteTelefono(idCliente));
                Utilities.c_packColumn.packColumns(this.vista.jtTelefono, 1);
                this.vista.jtSucursal.setModel(DB_Cliente.obtenerSucursal(idCliente));
                Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
                this.vista.jtContacto.setModel(DB_Cliente.obtenerClienteContacto(idCliente));
                Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);

                verificarAcceso();
            } else {
                this.vista.jbModificarCliente.setEnabled(false);
            }
            if (e.getClickCount() == 2) {
            }
        }
        if (e.getSource().equals(this.vista.jtContacto)) {
            int fila = this.vista.jtContacto.rowAtPoint(e.getPoint());
            int columna = this.vista.jtContacto.columnAtPoint(e.getPoint());
            idContacto = (Integer.valueOf((String) this.vista.jtContacto.getValueAt(fila, 0)));
            contacto = DB_Cliente.obtenerDatosClienteContactoID(idContacto);
            if ((fila > -1) && (columna > -1)) {
                this.vista.jtfNombreContacto.setText(contacto.getNombre());
                this.vista.jtfApellidoContacto.setText(contacto.getApellido());
                this.vista.jtfTelefonoContacto.setText(contacto.getTelefono());
                this.vista.jtfCiudad.setText(contacto.getCiudad());
                this.vista.jtfEstadoCivil.setText(contacto.getEstado_civil());
                this.vista.jtfGenero.setText(contacto.getSexo());
                this.vista.jtfNacionalidad.setText(contacto.getPais());
                this.vista.jftFechaNacimiento.setValue(contacto.getFecha_nacimiento());
                this.vista.jtfCorreoElecContacto.setText(contacto.getEmail());
                this.vista.jtfDireccionContacto.setText(contacto.getDireccion());
                this.vista.jtfObservacionContacto.setText(contacto.getObservacion());
                this.vista.jftCedulaIdentidad.setValue(contacto.getCedula());
            }
        }
    }

    public void keyTyped(KeyEvent e) {
        displayQueryResults();
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        if (this.vista.jtCliente.hasFocus()) {
            completarCampos();
        }
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
}
