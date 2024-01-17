
package model;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
public class MyConnection {
    
    public static final String username ="sa";
    public static final String password="123456789";
    public static final String url="jdbc:sqlserver://DESKTOP-71IUHD0\\\\SQLEXPRESS:1433;encrypt=true;trustServerCertificate=true;databaseName=CAFE;integratedSecurity=true";
    public static Connection con= null;
    
    public static Connection getConnection(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con=DriverManager.getConnection(url,username,password);
        }catch(Exception ex){
               ex.printStackTrace();
                }
        return con;
    }
}
