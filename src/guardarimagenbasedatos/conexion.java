
package Programa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Gabriel González Rojas
 */
public class conexion {
    private Connection conexion=null ;

    public Connection getConexion()
    {
       return conexion;
    }
    
    public boolean crearConexion()
    {
       try {
          Class.forName("org.postgresql.Driver");
          conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Imagen"
          		+ "?characterEncoding =UTF8", "postgres","admin123");
          if(conexion!=null){
              return true;
          }
       }
       catch (SQLException ex) {
          System.out.println("error en conexion: "+ex);
       }
       catch (ClassNotFoundException ex) {
          System.out.println(ex);
       }
       return false;
    }
    public boolean ejecutarSQL(String sql)
    {
       try {
           PreparedStatement sentencia = conexion.prepareStatement(sql);
          sentencia.execute(sql);
          return true;
       } catch (SQLException ex) {
            return false;
       }
    }

    public ResultSet ejecutarSQLSelect(String sql)
    {
       ResultSet resultado;
       try {
          PreparedStatement sentencia = conexion.prepareStatement(sql);
          resultado = sentencia.executeQuery();
          return resultado;
       } catch (SQLException ex) {
          System.err.println("Error "+ex);
          return null;
       }
    }
}
