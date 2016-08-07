/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ramiro
 */
public class JTablePaginationModel extends DefaultTableModel {

    public JTablePaginationModel() {
        JTable table = new JTable(this);
        addColumn("Id");
        addColumn("Producto");
        addColumn("Marca");
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/panatest", "postgres", "postgres");
            String query = "select prod_id_producto ,prod_descripcion, prod_marca from producto order by prod_id_producto";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("prod_id_producto");
                String name = rs.getString("prod_descripcion");
                String address = rs.getString("prod_marca");
                addRow(new Object[]{id, name, address});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JTablePaginationModel(JTable jtable) {
        JTable table = jtable;
        table.setModel(this);
        addColumn("Id");
        addColumn("Producto");
        addColumn("Marca");
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/panatest", "postgres", "postgres");
            String query = "select prod_id_producto ,prod_descripcion, prod_marca from producto order by prod_id_producto";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("prod_id_producto");
                String name = rs.getString("prod_descripcion");
                String address = rs.getString("prod_marca");
                addRow(new Object[]{id, name, address});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
