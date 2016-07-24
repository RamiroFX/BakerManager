/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Charts;

import Egresos.C_gestionEgresos;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author Ramiro Ferreira
 */
public class MenuDiagramas extends JDialog implements ActionListener {

    private JButton jbCompraProveedor, jbCompProv2, jbCerrar;
    C_gestionEgresos gestionEgreso;

    public MenuDiagramas(C_gestionEgresos gestionEgreso) {
        super(gestionEgreso.c_inicio.vista, "Gráficos y diagramas", ModalityType.APPLICATION_MODAL);
        this.gestionEgreso = gestionEgreso;
        setSize(800, 600);
        setLocationRelativeTo(gestionEgreso.c_inicio.vista);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicializarComponentes();
        agregarListeners();
    }

    private void cerrar() {
        this.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(jbCerrar)) {
            cerrar();
        } else if (source.equals(jbCompraProveedor)) {
            verDiagramas();
        }
    }

    private void inicializarComponentes() {
        jbCompraProveedor = new JButton("Compra a proveedores");
        //jbCompProv2 = new JButton("Compra a prov. personalizada");
        jbCerrar = new JButton("Cerrar");
        JPanel jpProveedores = new JPanel();
        jpProveedores.add(jbCompraProveedor);
        getContentPane().add(jpProveedores, BorderLayout.CENTER);
        getContentPane().add(jbCerrar, BorderLayout.SOUTH);
    }

    private void agregarListeners() {
        jbCompraProveedor.addActionListener(this);
        jbCerrar.addActionListener(this);
    }

    private void verDiagramas() {
        GraficoEgresos ge = new GraficoEgresos(gestionEgreso);
        ge.mostrarVista();
    }
}
