/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.util.List;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import db.Schedule;
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
    
    public void logEvent(int thingid, String action, String type) throws SQLException {
        con = DriverManager.getConnection(dbURL, user, pass);

        
        CallableStatement cStmt = con.prepareCall("{call CreateEvent(?, ?, ?)}");
        
        cStmt.setInt("ThingID", thingid);
        cStmt.setString("Action", action);
        cStmt.setString("Type", type);
        
        boolean hadResults = cStmt.execute();
        
        while(hadResults) {
            ResultSet rs = cStmt.getResultSet();
            hadResults = cStmt.getMoreResults();
        }
    }
    
    public List<Schedule> getSchedules(String clientCode) throws SQLException {
        ResultSet rs = null;
        List<db.Schedule> schedules = new ArrayList<db.Schedule>();
        
        con = DriverManager.getConnection(dbURL, user, pass);
        
        CallableStatement cStmt = con.prepareCall("{call GetSchedulesByDevice(?)}");
        
        cStmt.setString("ClientCode", clientCode);
        
        rs = cStmt.executeQuery();
        
        while(rs.next()) {
            Schedule schedule = new Schedule(rs.getInt("ScheduleID"), 
                    rs.getInt("ThingID"), 
                    rs.getInt("ActionID"), 
                    rs.getString("Name"), 
                    rs.getString("CronExpression"), 
                    rs.getString("Description"));
            
            schedules.add(schedule);
        }
       
        
        return schedules;
    }
    
    public String getAction(int actionId) throws SQLException {
        String action = null;
                
        con = DriverManager.getConnection(dbURL, user, pass);
        
        CallableStatement cStmt = con.prepareCall("{ ? = call ufn_GetActionFromID(?)}");
        
        cStmt.setInt(2, actionId);
        cStmt.registerOutParameter(1, java.sql.Types.NVARCHAR);
        
        cStmt.execute();
        
        action = cStmt.getString(1);
        
        return action;
    }
}
