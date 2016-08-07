/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ramiro Ferreira
 */
public class Conexiones {

    final public static int SGBD_ORACLE = 0;
    final public static int SGBD_POSTGRES = 1;

    public static void cargarDriver(int db_type) throws ClassNotFoundException {
        switch (db_type) {
            case SGBD_ORACLE: {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                break;
            }
            case SGBD_POSTGRES: {
                Class.forName("org.postgresql.Driver");
                break;
            }
        }
    }

    public static Connection obtenerConexion(String url, String user, String password) throws SQLException {
        Connection con = null;
        con = DriverManager.getConnection(url, user, password);
        return con;
    }

    public static Connection obtenerConexion(int db, String user, String password) throws SQLException {
        Connection con = null;
        String url;
        switch (db) {
            case SGBD_POSTGRES:
                url = "jdbc:postgresql://localhost:5432/bakermanager2";
                con = DriverManager.getConnection(url, user, password);
                break;
            case SGBD_ORACLE:
                url = "jdbc:oracle:thin:@localhost:1521:orcl";
                con = DriverManager.getConnection(url, user, password);
                break;
        }
        return con;
    }

    public static Connection obtenerConexion(int db, String url, String user, String password) throws SQLException {
        Connection con = null;
        switch (db) {
            case SGBD_POSTGRES:
                con = DriverManager.getConnection(url, user, password);
                break;
            case SGBD_ORACLE:
                con = DriverManager.getConnection(url, user, password);
                break;
        }
        return con;
    }
}
