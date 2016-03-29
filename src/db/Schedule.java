/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class Schedule {
    private int ScheduleID;
    private int ThingID;
    private int ActionID;
    private String Name;
    private String CronExpression;
    private String Description;
    private String Action;
    private JDBCSQLServerConnection db = new JDBCSQLServerConnection();

    public Schedule(int scheduleId, int thingId, int actionId, String name, String cronExpression, String description) {
        ScheduleID = scheduleId;
        ThingID = thingId;
        ActionID = actionId;
        Name = name;
        CronExpression = cronExpression;
        Description = description;
    }
    
    public String getAction() {
        try {
            Action = db.getAction(ActionID);
        } catch (SQLException ex) {
            Logger.getLogger(Schedule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Action;
    }
    
    /**
     * @return the ScheduleID
     */
    public int getScheduleID() {
        return ScheduleID;
    }

    /**
     * @param ScheduleID the ScheduleID to set
     */
    public void setScheduleID(int ScheduleID) {
        this.ScheduleID = ScheduleID;
    }

    /**
     * @return the ThingID
     */
    public int getThingID() {
        return ThingID;
    }

    /**
     * @param ThingID the ThingID to set
     */
    public void setThingID(int ThingID) {
        this.ThingID = ThingID;
    }

    /**
     * @return the ActionID
     */
    public int getActionID() {
        return ActionID;
    }

    /**
     * @param ActionID the ActionID to set
     */
    public void setActionID(int ActionID) {
        this.ActionID = ActionID;
    }

    /**
     * @return the Name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * @return the CronExpression
     */
    public String getCronExpression() {
        return CronExpression;
    }

    /**
     * @param CronExpression the CronExpression to set
     */
    public void setCronExpression(String CronExpression) {
        this.CronExpression = CronExpression;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }
    
    
}
