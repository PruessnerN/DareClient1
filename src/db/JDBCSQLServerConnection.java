/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class JDBCSQLServerConnection {
    
    Connection con = null;
    String dbURL = "jdbc:sqlserver://cs.cofo.edu;databaseName=npruessnerE";
    String user = "npruessner";
    String pass = "80dbknf9";
    
    public void testConnection() {
        try {
            con = DriverManager.getConnection(dbURL, user, pass);
            
            if(con != null) {
                DatabaseMetaData dm = (DatabaseMetaData) con.getMetaData();
                System.out.println("Driver Name: " + dm.getDriverName());
                System.out.println("Driver Version: " + dm.getDriverVersion());
                System.out.println("Product Name: " + dm.getDatabaseProductName());
                System.out.println("Product Version" + dm.getDatabaseProductVersion());
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCSQLServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(con != null && !con.isClosed()) {
                    con.close();
                } 
            } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void logEvent(int clientid, String name, String type, String description) throws SQLException {
        con = DriverManager.getConnection(dbURL, user, pass);

        
        CallableStatement cStmt = con.prepareCall("{call CreateEvent(?, ?, ?, ?)}");
        
        cStmt.setInt("ClientID", clientid);
        cStmt.setString("Name", name);
        cStmt.setString("Type", type);
        cStmt.setString("Description", description);
        
        boolean hadResults = cStmt.execute();
        
        while(hadResults) {
            ResultSet rs = cStmt.getResultSet();
            hadResults = cStmt.getMoreResults();
        }
    }
    
}
