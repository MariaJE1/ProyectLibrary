/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author susi1
 */
public class CConexion {
    Connection conectar = null;
    String username ="root";
    String pasword ="root";
    String bd ="Bookshop";
    String ip ="localhost";
    String puerto ="3306";
    
    String cadena="jdbc:mysql://"+ip+":"+puerto+"/"+bd;
    
    public Connection estableceConexion (){
        
        try {
            //Señalo el driver 
            Class.forName("com.mysql.jdbc.Driver");
            conectar=DriverManager.getConnection(cadena,username,pasword);
            JOptionPane.showInternalMessageDialog(null, "La conexión fue exitosa");
        } catch (Exception e) {
             JOptionPane.showInternalMessageDialog(null, "Fallo la conexión");
        }
        return conectar;
          
    }
    public void cerrarConexion(){
        try {
            if(conectar !=null && conectar.isClosed()){
                conectar.close();
                JOptionPane.showInternalMessageDialog(null, "Conexion cerrada");
                
            }
        } catch (Exception e) {
             JOptionPane.showInternalMessageDialog(null, "No se pudo cerrar la conexión");
        }
 
        
    }
      
}
