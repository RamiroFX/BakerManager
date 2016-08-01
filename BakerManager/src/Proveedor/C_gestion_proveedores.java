/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import DB_manager.DB_Proveedor;
import Entities.M_contacto;
import Entities.M_proveedor;
import bakermanager.C_inicio;
import java.awt.AlphaComposite;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Usuario
 */
class C_gestion_proveedores extends MouseAdapter implements ActionListener, KeyListener {

    public C_inicio c_inicio;
    private M_proveedor m_proveedor;
    private M_contacto contacto;
    private ImageIcon foto;
    int idProveedor;
    V_gestion_proveedores vista;
    String imagePath;
    ImageIcon image;
    File fileImage;

    public C_gestion_proveedores(C_inicio c_inicio) {
        this.c_inicio = c_inicio;
        this.m_proveedor = new M_proveedor();
        this.vista = new V_gestion_proveedores();
        inicializarVista();
        agregarListeners();
    }

    /**
     * @return the m_funcionario
     */
    public M_proveedor getProveedor() {
        return m_proveedor;
    }

    /**
     * @param m_funcionario the m_funcionario to set
     */
    public void setProveedor(M_proveedor m_proveedor) {
        this.m_proveedor = m_proveedor;
    }

    private void inicializarVista() {
        this.vista.jtProveedor.setModel(DB_Proveedor.consultarProveedor("", true, true, false));
        Utilities.c_packColumn.packColumns(this.vista.jtProveedor, 2);
        this.vista.jbModificarProveedor.setEnabled(false);
    }

    /**
     * Establece el tamaño, posicion y visibilidad de la vista.
     */
    public void mostrarVista() {
        this.vista.setSize(this.c_inicio.establecerTamañoPanel());
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        this.c_inicio.agregarVentana(this.vista);
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
        //this.v_jifGesUsu.jbBorrar.addActionListener(this);
        //this.v_jifGesUsu.jtfBuscar.addActionListener(this);
        this.vista.jbCrearProveedor.addActionListener(this);
        this.vista.jbModificarProveedor.addActionListener(this);
        this.vista.jtProveedor.addMouseListener(this);
        this.vista.jtContacto.addMouseListener(this);
        this.vista.jtProveedor.addKeyListener(this);
        this.vista.jckbEntidad.addActionListener(this);
        this.vista.jckbRUC.addActionListener(this);
        this.vista.jrbExclusivo.addActionListener(this);
        this.vista.jrbInclusivo.addActionListener(this);
        this.vista.jtfBuscar.addKeyListener(this);

    }

    public void displayQueryResults(final String q) {
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
                vista.jtProveedor.setModel(DB_Proveedor.consultarProveedor(q, true, true, false));
                Utilities.c_packColumn.packColumns(vista.jtProveedor, 2);
                // se borra el msg.
            }
        });
    }

    private void displayQueryResults() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String proveedor = vista.jtfBuscar.getText();
                boolean entidad = vista.jckbEntidad.isSelected();
                boolean nombre = vista.jckbRUC.isSelected();
                boolean exclusivo = vista.jrbExclusivo.isSelected();
                vista.jtProveedor.setModel(DB_Proveedor.consultarProveedor(proveedor.toLowerCase(), entidad, nombre, exclusivo));
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == this.vista.jbCrearProveedor) {
            Crear_proveedor crearProveedor = new Crear_proveedor(this);
            crearProveedor.mostrarVista();
        }
        if (ae.getSource() == this.vista.jbModificarProveedor) {
            Modificar_proveedor modificarProveedor = new Modificar_proveedor(this, idProveedor);
            modificarProveedor.mostrarVista();
        } else if (ae.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jckbEntidad) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jckbRUC) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jrbExclusivo) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jrbInclusivo) {
            displayQueryResults();
        }
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

    private void establecerImagen(File fileImage, String name) {
        try {
            String path = fileImage.getPath();
            BufferedImage src = ImageIO.read(new File(path));
            // Convert Image to BufferedImage if required.
            BufferedImage image = toBufferedImage(src);
            save(image, "png", name);  // png okay, j2se 1.4+
            save(image, "bmp", name);  // gif okay in j2se 1.6+
            // gif okay in j2se 1.6+
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void save(BufferedImage image, String ext, String name) {
        String fileName = name;
        File file = new File(fileName + "." + ext);
        try {
            ImageIO.write(image, ext, file);  // ignore returned boolean
        } catch (IOException e) {
            System.out.println("Write error for " + file.getPath()
                    + ": " + e.getMessage());
        }
    }

    private static BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }

    private void completarCampos() {
        idProveedor = Integer.valueOf(String.valueOf(this.vista.jtProveedor.getValueAt(this.vista.jtProveedor.getSelectedRow(), 0)));
        //setProducto(DBmanagerProducto.mostrarProducto(idProducto));
        setProveedor(DB_Proveedor.obtenerDatosProveedorID(idProveedor));
        this.vista.jtfEntidad.setText(getProveedor().getEntidad());
        this.vista.jtfNombre.setText(getProveedor().getNombre());
        if (null == getProveedor().getRuc()) {
            this.vista.jtfRUC.setText("");
        } else {
            this.vista.jtfRUC.setText(getProveedor().getRuc() + "-" + getProveedor().getRuc_id());
        }
        this.vista.jftDescripcion.setText(getProveedor().getDescripcion());
        this.vista.jtfDireccion.setText(getProveedor().getDireccion());
        this.vista.jtfEmail.setText(getProveedor().getEmail());
        this.vista.jtfPagWeb.setText(getProveedor().getPagWeb());
        this.vista.jtfObservacion.setText(getProveedor().getObservacion());
        this.vista.jtTelefono.setModel(DB_Proveedor.obtenerProveedorTelefono(idProveedor));
        Utilities.c_packColumn.packColumns(this.vista.jtTelefono, 1);
        this.vista.jtSucursal.setModel(DB_Proveedor.obtenerSucursal(idProveedor));
        Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
        this.vista.jtContacto.setModel(DB_Proveedor.obtenerProveedorContacto(idProveedor));
        Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
        this.vista.jbModificarProveedor.setEnabled(true);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtProveedor)) {
            borrarDatosContacto();
            int fila = this.vista.jtProveedor.rowAtPoint(e.getPoint());
            int columna = this.vista.jtProveedor.columnAtPoint(e.getPoint());
            idProveedor = (Integer.valueOf((String) this.vista.jtProveedor.getValueAt(fila, 0)));
            setProveedor(DB_Proveedor.obtenerDatosProveedorID(idProveedor));
            if ((fila > -1) && (columna > -1)) {
                this.vista.jtfEntidad.setText(getProveedor().getEntidad());
                this.vista.jtfNombre.setText(getProveedor().getNombre());
                if (null == getProveedor().getRuc()) {
                    this.vista.jtfRUC.setText("");
                } else {
                    this.vista.jtfRUC.setText(getProveedor().getRuc() + "-" + getProveedor().getRuc_id());
                }
                this.vista.jftDescripcion.setText(getProveedor().getDescripcion());
                this.vista.jtfDireccion.setText(getProveedor().getDireccion());
                this.vista.jtfEmail.setText(getProveedor().getEmail());
                this.vista.jtfPagWeb.setText(getProveedor().getPagWeb());
                this.vista.jtfObservacion.setText(getProveedor().getObservacion());
                this.vista.jtTelefono.setModel(DB_Proveedor.obtenerProveedorTelefono(idProveedor));
                Utilities.c_packColumn.packColumns(this.vista.jtTelefono, 1);
                this.vista.jtSucursal.setModel(DB_Proveedor.obtenerSucursal(idProveedor));
                Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
                this.vista.jtContacto.setModel(DB_Proveedor.obtenerProveedorContacto(idProveedor));
                Utilities.c_packColumn.packColumns(this.vista.jtSucursal, 1);
                this.vista.jbModificarProveedor.setEnabled(true);
            } else {
                this.vista.jbModificarProveedor.setEnabled(false);
            }
            if (e.getClickCount() == 2) {
            }
        }
        if (e.getSource().equals(this.vista.jtContacto)) {
            int fila = this.vista.jtContacto.rowAtPoint(e.getPoint());
            int columna = this.vista.jtContacto.columnAtPoint(e.getPoint());
            int idContacto = (Integer.valueOf((String) this.vista.jtContacto.getValueAt(fila, 0)));
            contacto = DB_Proveedor.obtenerDatosContactoIdContacto(idContacto);
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

    @Override
    public void keyTyped(KeyEvent e) {
        displayQueryResults();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (this.vista.jtProveedor.hasFocus()) {
            completarCampos();
        }
    }
}
