/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import com.toedter.calendar.JDateChooser;
import java.sql.CallableStatement;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author susi1
 */
public class CLibro {
    
   int idStatus;
    //Creo un constructor para poder enlazar la descripción de status con su valor  
    public void establecerIdStatus (int idStatus) {
        this.idStatus = idStatus;
    }
    
    
    
    public void MostrarStatusCombo (JComboBox comboStatus){
        Clases.CConexion objetoConexion = new Clases.CConexion();
        String sql = "select * from status";
        Statement st;
        
        try {
            st= objetoConexion.estableceConexion().createStatement();
            //Debo recibir la info
            ResultSet rs = st.executeQuery(sql);
            comboStatus.removeAllItems();
            //Recorrer la consulta 
            while (rs.next()){
                String nombreStatus= rs.getString("status");
                //Guardando el id Sleccionado
                this.establecerIdStatus(rs.getInt("id"));
                
                //Mostrar 
                comboStatus.addItem(nombreStatus);
                //Guardar
                comboStatus.putClientProperty(nombreStatus,idStatus);
                
            }
            
        } catch (SQLException e) {
                JOptionPane.showMessageDialog( null,"Error al mostrar status:");
           
    }
        finally{
        objetoConexion.cerrarConexion();
    }
        
    
}
    
    public void AgregarLibro ( JTextField title,JTextField author,JDateChooser publishedDate, JTextField isbn,JComboBox combostatus){
        CConexion objetoConexion= new CConexion ();
        String Consulta = "insert into books (title, author, publishedDate, isbn, fkstatus) values (?,?,?,?,?);";
        try {
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(Consulta);
            
            cs.setString(1,title.getText());
            cs.setString(2,author.getText());
            
            Date fechaSeleccionada= publishedDate.getDate();
            java.sql.Date fechaSQL = new java.sql.Date(fechaSeleccionada.getTime());
            cs.setDate(3, fechaSQL);
            
             cs.setString(4,isbn.getText());
            
            int idStatus =(int) combostatus.getClientProperty(combostatus.getSelectedItem());
            cs.setInt(5, idStatus);
            
            cs.execute();
            
            JOptionPane.showMessageDialog(null,"Se guardo correctamente");
           
                    
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null,"Error al guardar"+e.toString());
        }
        
    }
    
    
    public void MostrarLibros (JTable tablaTotalLibros){
        Clases.CConexion objetoConexion = new Clases.CConexion();
        DefaultTableModel modelo = new DefaultTableModel();
        String sql ="select books.id, books.title,books.author,books.publishedDate, books.isbn, status.status from books INNER JOIN status On books.fkstatus= status.id";
        
        
        modelo.addColumn("id");
        modelo.addColumn("title");
        modelo.addColumn("author");
        modelo.addColumn("publishedDate");
        modelo.addColumn("isbn");
        modelo.addColumn("status");
        
        tablaTotalLibros.setModel(modelo);
        
        try {
            Statement st = objetoConexion.estableceConexion().createStatement();
            //Que ejecute la consulta
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                String id = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
          
                java.sql.Date publishedDate = rs.getDate("publishedDate");
                String isbn = rs.getString("isbn");
                String status = rs.getString("status");
                
                modelo.addRow(new Object[] {id,title,author,publishedDate,isbn,status});
            }
            
            tablaTotalLibros.setModel(modelo);
            
            
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error al mostrar usuaris"+e.toString());
        }
        finally{
        objetoConexion.cerrarConexion();
    }
        }
    public void Seleccionar (JTable totalLibros, JTextField id,JTextField title,JTextField author,JDateChooser publishedDate, JTextField isbn,JComboBox status){
        //Guardar el eje seleccionado o fila en la tabla
        int fila =totalLibros.getSelectedRow();
        if (fila>=0){
            id.setText(totalLibros.getValueAt(fila, 0).toString());
            title.setText(totalLibros.getValueAt(fila, 1).toString());
            author.setText(totalLibros.getValueAt(fila, 2).toString());
            String fechaString = totalLibros.getValueAt(fila,3).toString();
            isbn.setText(totalLibros.getValueAt(fila, 4).toString());
            status.setSelectedItem(totalLibros.getValueAt(fila, 5).toString());
            
        }
            
    }
    public void ModificarLibro (JTextField id,JTextField title,JTextField author,JDateChooser publishedDate, JTextField isbn,JComboBox combostatus){
       Clases.CConexion objetoConexion = new Clases.CConexion(); 
       String consulta ="update books set books.title=?, books.author=?,books.publishedDate=?, books.isbn=?, books.fkstatus=? where books.id=?;";
        try {
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            cs.setString(1,title.getText());
            cs.setString(2,author.getText());
            
            Date fechaSeleccionada= publishedDate.getDate();
            java.sql.Date fechaSQL = new java.sql.Date(fechaSeleccionada.getTime());
            cs.setDate(3, fechaSQL);
            
            cs.setString(4,isbn.getText());
            
            int idStatus =(int) combostatus.getClientProperty(combostatus.getSelectedItem());
            cs.setInt(5, idStatus);
            
            cs.setInt(6, Integer.parseInt(id.getText()));
            
            cs.execute();
            
            JOptionPane.showMessageDialog(null,"Se modifico correctamente");
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"No se mostro correctamente"+e.toString());
        }
        
        finally{
            objetoConexion.cerrarConexion();
        }
    }
    
    public void EliminarUsuario (JTextField id ){
        Clases.CConexion objetoConexion = new Clases.CConexion();
        String consulta="delete from books where books.id =?;";
        try {
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            cs.setInt(1,Integer.parseInt(id.getText()));
            cs.execute();
            JOptionPane.showMessageDialog(null,"Se elimino correctamente");
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"No se elimino"+e.toString());
        }
        finally{
            objetoConexion.cerrarConexion();
        }
      
        }
        
}
        
        
    


