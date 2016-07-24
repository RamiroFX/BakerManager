/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JFprincipal.java
 *
 * Created on 18-feb-2012, 7:29:34
 */

package Producto;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class v_jdCostearProducto extends javax.swing.JDialog {
    public javax.swing.JButton jbModificar,jbAgregarIngrediente,jbQuitarIngrediente,jbGestionIngrediente;
    private javax.swing.JLabel jlTotal,jlTotalUnidad,jlCantidad,jlPrecioVenta,jlPrecioMayorista,jlPorcMinGanacia,jlPorcMayGanancia;
    private javax.swing.JScrollPane jspProducto,jspIngrediente;
    public javax.swing.JTable jtProducto,jtIngrediente;
    public javax.swing.JFormattedTextField jtfCantidad,jtfGanMay,jtfGanVenta,jtfPrecioMayorista,jtfPrecioVenta,jtfTotal,jtfPrecioUnitario;
    public javax.swing.JPanel jpEast,jpSouth,jpSouth1,jpSouth2;
    
    public v_jdCostearProducto(javax.swing.JFrame parent) {
        super(parent,"Definir costo de producto");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(800, 600));
        initComponents();
        setLayout(new BorderLayout());
        getContentPane().add(jspProducto,BorderLayout.CENTER);
        getContentPane().add(jpEast,BorderLayout.WEST);
        getContentPane().add(jspIngrediente,BorderLayout.EAST);
        getContentPane().add(jpSouth,BorderLayout.SOUTH);        
        setLocationRelativeTo(parent);
        //llenarTablaProducto("");
    }


    private void initComponents() {

        //Formateadores de numeros(enteros, dobles y porcentuales)
        java.text.NumberFormat dfi = java.text.NumberFormat.getIntegerInstance();
        java.text.NumberFormat dfp = java.text.NumberFormat.getPercentInstance();
        java.text.NumberFormat dfd = java.text.NumberFormat.getNumberInstance();
        NumberFormatter nf = new NumberFormatter(dfi);
        NumberFormatter nfp = new NumberFormatter(dfp);
        NumberFormatter nfd = new NumberFormatter(dfd);
        DefaultFormatterFactory dff= new DefaultFormatterFactory(nf);

        // fuente de labels y textFields
        Font fuenteLabel= new Font("Times New Roman", 1, 15);
        Font fuenteTextField= new Font("Times New Roman", 1, 15);
        //botones
        jbAgregarIngrediente = new javax.swing.JButton("Agregar ingrediente");
        jbAgregarIngrediente.setEnabled(false);
        jbModificar = new javax.swing.JButton("Modificar");
        jbModificar.setEnabled(false);
        jbGestionIngrediente= new javax.swing.JButton("Gestion ingrediente");
        jbQuitarIngrediente = new javax.swing.JButton("Quitar ingrediente");
        jbQuitarIngrediente.setEnabled(false);
        //Jtables y jscrollpanes
        jtProducto = new javax.swing.JTable();
        jtProducto.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        jspProducto = new javax.swing.JScrollPane(jtProducto);
        jtIngrediente = new javax.swing.JTable();        
        
        jspIngrediente = new javax.swing.JScrollPane(jtIngrediente);
        jtIngrediente.setSelectionBackground(new java.awt.Color(255, 0, 0));

        //Compos de textos formateados
        jtfTotal = new javax.swing.JFormattedTextField(dff);
        jtfPrecioUnitario = new javax.swing.JFormattedTextField(dff);        
        jtfCantidad = new javax.swing.JFormattedTextField(dff);
        jtfPrecioVenta = new javax.swing.JFormattedTextField(dff);
        jtfPrecioMayorista = new javax.swing.JFormattedTextField();
        jtfGanVenta = new javax.swing.JFormattedTextField(dfd);
        jtfGanMay = new javax.swing.JFormattedTextField(dfd);
        jtfTotal.setColumns(10);
        jtfPrecioUnitario.setColumns(10);
        jtfCantidad.setColumns(10);

        //Etiquetas
        jlCantidad = new javax.swing.JLabel("Cantidad");
        jlTotalUnidad = new javax.swing.JLabel("Total por unidad");
        jlTotal = new javax.swing.JLabel("Total");
        jlPorcMayGanancia = new javax.swing.JLabel("% de Ganancia");
        jlPorcMinGanacia = new javax.swing.JLabel("% de Ganancia");
        jlPrecioMayorista = new javax.swing.JLabel("Precio mayorista");
        jlPrecioVenta = new javax.swing.JLabel("Precio de venta");
        jlPrecioVenta.setHorizontalAlignment(SwingConstants.CENTER);
        jlPrecioMayorista.setHorizontalAlignment(SwingConstants.CENTER);
        jlPorcMinGanacia.setHorizontalAlignment(SwingConstants.CENTER);
        jlPorcMayGanancia.setHorizontalAlignment(SwingConstants.CENTER);
        jlCantidad.setFont(fuenteLabel);
        jlTotalUnidad.setFont(fuenteLabel);
        jlTotal.setFont(fuenteLabel);
        jlPorcMayGanancia.setFont(fuenteLabel);
        jlPorcMinGanacia.setFont(fuenteLabel);
        jlPrecioMayorista.setFont(fuenteLabel);
        jlPrecioVenta.setFont(fuenteLabel);


        
       
        jpEast= new JPanel(new GridLayout(5, 1));
        jpEast.add(jbModificar);
        /*jpEast.add(jbAgregarIngrediente);
        jpEast.add(jbQuitarIngrediente);*/
        jpEast.add(new JLabel());
        jpEast.add(new JLabel());
        jpEast.add(new JLabel());
        jpEast.add(jbGestionIngrediente);
        jpSouth= new JPanel(new GridLayout(2, 1));
        jpSouth1= new JPanel();
        jpSouth1.setBorder(new EtchedBorder());
        jpSouth1.add(jlTotal);
        jpSouth1.add(jtfTotal);
        jpSouth1.add(jlCantidad);
        jpSouth1.add(jtfCantidad);
        jpSouth1.add(jlTotalUnidad);
        jpSouth1.add(jtfPrecioUnitario);
        jpSouth2= new JPanel(new GridLayout(2, 4));
        jpSouth2.add(jlPrecioVenta);

        jpSouth2.add(jtfPrecioVenta);
        jpSouth2.add(jlPrecioMayorista);
        jpSouth2.add(jtfPrecioMayorista);
        jpSouth2.add(jlPorcMinGanacia);
        jpSouth2.add(jtfGanVenta);
        jpSouth2.add(jlPorcMayGanancia);
        jpSouth2.add(jtfGanMay);
        jpSouth.add(jpSouth1);
        jpSouth.add(jpSouth2);
    }

    

    public void packColumns(JTable table, int margin) {
        for (int c=0; c<table.getColumnCount(); c++) {
          packColumn(table, c, margin);
        }
    }    //Ajusta a largura preferida da coluna visível especificada pelo vColIndex.
// A coluna será larga o bastante para mostrar o cabeçalho da coluna e a
// célula de maior conteúdo.
    public void packColumn(JTable table, int vColIndex, int margin) {
        TableModel model = table.getModel();
        DefaultTableColumnModel colModel = (DefaultTableColumnModel)table.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);
        int width = 0;            // Obtém a largura do cabeçalho da coluna
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(
            table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;            // Obtém a largura maxima da coluna de dados
       for (int r=0; r<table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(
                table, table.getValueAt(r, vColIndex), false, false, r,vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }            width += 2*margin;            // Configura a largura
        col.setPreferredWidth(width);
    }
    public JTable getJtIngrediente(){

        return jtIngrediente;
    }
        

    }



