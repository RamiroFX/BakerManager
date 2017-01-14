/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.*;
import javax.swing.event.*;

/**
 * Esta tabla toma un objeto JDBC ResultSet e implementa la interfaz TableModel
 * de modo que se puedan desplegar los resultados.
 **/
public class ResultSetTableModel extends DefaultTableModel {

    ResultSet results;             // El ResultSet a interpretar.
    ResultSetMetaData metadata;    // Información adicional sobre los resultados.
    int numcols, numrows;          // Cuántas columnas y filas tendrá la tabla.

    /**
     * El contructor crea un TableModel a partir de un ResultSet.
     **/
    public ResultSetTableModel(ResultSet results) throws SQLException {
		this.results = results;
		metadata = results.getMetaData();
		numcols = metadata.getColumnCount();    // Se obtiene la cantidad de columnas
		results.last();
		numrows = results.getRow();             // Se obtiene la cantidad de filas
    }

    /**
     * Se cierra el statement.
     **/
    public void close() {
		try {
			results.getStatement().close();
		}catch(SQLException e) {};
    }

    protected void finalize() {
    	close();
    }

    /**
     * Retorna la cantidad de columnas de la tabla.
     */
    public int getColumnCount() {
    	return numcols;
    }

    /**
     * Retorna la cantidad de filas de la tabla.
     */
    public int getRowCount() {
    	return numrows;
    }

    /**
     * Retorna el nombre de las columnas a partir de los metadatos.
     */
    public String getColumnName(int column) {
		String nombre = "";
    	try {
		    nombre = metadata.getColumnLabel(column+1);
		} catch (SQLException e) {
			nombre = e.toString();
		}
		return nombre;
    }


    /**
     * En este método se especifica el tipo de dato de cada columna.
     */
    public Class getColumnClass(int column) {
    	return String.class;
    }

    /**
     * Este es el método principal del TableModel. Se encarga de retornar
     * el valor de cada celda de la tabla.
     **/
    public Object getValueAt(int row, int column) {
		try {
		    results.absolute(row+1);                // Moverse a la fila indicada
		    Object o = results.getObject(column+1); // Se obtiene el valor de la columna indicada
		    if (o == null)
		    	return null;
		    else
		    	return o.toString();               // Se devuelve un String
		} catch (SQLException e) {
			return e.toString();
		}
    }

    /**
     * Dice si la celda es editable. En este caso no lo es.
     */
    public boolean isCellEditable(int row, int column) {
    	return false;
    }

    /**
     * Como la tabla no es editable, no es necesario
     * implementar estos métodos, pero sí deben estar porque
     * estamos implementando la interfaz TableModel
     */
    public void setValueAt(Object value, int row, int column) {}
    public void addTableModelListener(TableModelListener l) {}
    public void removeTableModelListener(TableModelListener l) {}

}
