/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class Crear_cliente_contacto extends JDialog implements ActionListener{
    //INICIO VARIABLES DE CONTACTO
    public javax.swing.JLabel jlNombre, jlApellido, jlCedulaIdentidad, jlCiudad, jlEstadoCivil,
            jlFechaNacimiento, jlGenero, jlNacionalidad, jlObservacion;
    public javax.swing.JTextField jtfNombre, jtfApellido, jtfNroTelefono;
    public javax.swing.JComboBox jcbCiudad, jcbEstadoCivil, jcbGenero, jcbNacionalidad;
    public com.toedter.calendar.JDateChooser dccFechaNacimiento;
    public javax.swing.JFormattedTextField jftCedulaIdentidad;
    public javax.swing.JLabel jlTelefonoContacto, jlCorreoElectronico, jlDireccionContacto;
    public javax.swing.JTextField jtfTelefonoContacto, jtfCorreoElecContacto, jtfDireccionContacto, jtfObservacion;
    public javax.swing.JPanel jpDatosContacto;
    public Crear_cliente_contacto(javax.swing.JFrame v_jfMain, boolean modal, boolean isCreate) {
        super(v_jfMain, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        if (isCreate) {
            setTitle("Crear contacto");
            setName("jdCrearClienteContacto");
        } else {
            setTitle("Modificar contacto");
            setName("jdModificarClienteContacto");
        }
        setAlwaysOnTop(false);
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        inicializarComponentes();
        
    }
    
    private void inicializarComponentes(){
        jpDatosContacto = new javax.swing.JPanel(new net.miginfocom.swing.MigLayout());
        //jpDatosPersonales1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos personales 1"));
        int swingConstant = javax.swing.SwingConstants.CENTER;
        //Labels, textfields, combobox
        jlNombre = new javax.swing.JLabel("Nombre (*)");
        jlNombre.setHorizontalAlignment(swingConstant);
        jtfNombre = new javax.swing.JTextField();
        jlApellido = new javax.swing.JLabel("Apellido (*)");
        jtfApellido = new javax.swing.JTextField();
        jlApellido.setHorizontalAlignment(swingConstant);
        jlFechaNacimiento = new javax.swing.JLabel("Fecha de nacimiento");
        jlFechaNacimiento.setHorizontalAlignment(swingConstant);
        dccFechaNacimiento = new com.toedter.calendar.JDateChooser();
        dccFechaNacimiento.setDateFormatString("dd/MM/yyyy");
        jlCedulaIdentidad = new javax.swing.JLabel("Cedula de identidad (*)");
        jlCedulaIdentidad.setHorizontalAlignment(swingConstant);
        jftCedulaIdentidad = new javax.swing.JFormattedTextField();
        jlNacionalidad = new javax.swing.JLabel("Nacionalidad");
        jlNacionalidad.setHorizontalAlignment(swingConstant);
        jcbNacionalidad = new javax.swing.JComboBox();
        jlCiudad = new javax.swing.JLabel("Ciudad");
        jlCiudad.setHorizontalAlignment(swingConstant);
        jcbCiudad = new javax.swing.JComboBox();
        jlGenero = new javax.swing.JLabel("Género");
        jlGenero.setHorizontalAlignment(swingConstant);
        jcbGenero = new javax.swing.JComboBox();
        jlEstadoCivil = new javax.swing.JLabel("Estado civil");
        jlEstadoCivil.setHorizontalAlignment(swingConstant);
        jcbEstadoCivil = new javax.swing.JComboBox();

        String width1 = "width :100:";
        String width2 = "width :270:";
        jpDatosContacto.add(jlNombre, width1 + ", cell 0 0");
        jpDatosContacto.add(jtfNombre, width2 + ", cell 1 0");

        jpDatosContacto.add(jlApellido, width1 + ", cell 0 1");
        jpDatosContacto.add(jtfApellido, width2 + ", cell 1 1");

        jpDatosContacto.add(jlFechaNacimiento, width1 + ", cell 0 2");
        jpDatosContacto.add(dccFechaNacimiento, width2 + ", cell 1 2");

        jpDatosContacto.add(jlCedulaIdentidad, width1 + ", cell 0 3");
        jpDatosContacto.add(jftCedulaIdentidad, width2 + ", cell 1 3");

        jpDatosContacto.add(jlNacionalidad, width1 + ", cell 0 4");
        jpDatosContacto.add(jcbNacionalidad, width2 + ", cell 1 4");

        jpDatosContacto.add(jlCiudad, width1 + ", cell 0 5");
        jpDatosContacto.add(jcbCiudad, width2 + ", cell 1 5");

        jpDatosContacto.add(jlGenero, width1 + ", cell 0 6");
        jpDatosContacto.add(jcbGenero, width2 + ", cell 1 6");

        jpDatosContacto.add(jlEstadoCivil, width1 + ", cell 0 7");
        jpDatosContacto.add(jcbEstadoCivil, width2 + ", cell 1 7");

        jlTelefonoContacto = new javax.swing.JLabel("Telefono");
        jlTelefonoContacto.setHorizontalAlignment(swingConstant);
        jlCorreoElectronico = new javax.swing.JLabel("E-mail");
        jlCorreoElectronico.setHorizontalAlignment(swingConstant);
        jlDireccionContacto = new javax.swing.JLabel("Dirección");
        jlDireccionContacto.setHorizontalAlignment(swingConstant);
        jlObservacion = new javax.swing.JLabel("Observación");
        jlObservacion.setHorizontalAlignment(swingConstant);
        jtfTelefonoContacto = new javax.swing.JTextField();
        jtfCorreoElecContacto = new javax.swing.JTextField();
        jtfDireccionContacto = new javax.swing.JTextField();
        jtfObservacion = new javax.swing.JTextField();

        jpDatosContacto.add(jlTelefonoContacto, width1 + ", cell 2 0");
        jpDatosContacto.add(jtfTelefonoContacto, width2 + ",cell 3 0");

        jpDatosContacto.add(jlDireccionContacto, width1 + ",cell 2 1");
        jpDatosContacto.add(jtfDireccionContacto, width2 + ",cell 3 1");

        jpDatosContacto.add(jlCorreoElectronico, width1 + ",cell 2 2");
        jpDatosContacto.add(jtfCorreoElecContacto, width2 + ",cell 3 2");

        jpDatosContacto.add(jlObservacion, width1 + ",cell 2 3");
        jpDatosContacto.add(jtfObservacion, width2 + ",cell 3 3");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
