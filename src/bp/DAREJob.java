/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bp;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ui.GUI;

/**
 *
 * @author root
 */
public class DAREJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getMergedJobDataMap();
        String id = data.getString("ThingID");

        switch (id) {
            case "5":
                if("Open".equals(data.getString("Action"))) {
                    
                } else {
                    
                }
                break;
            case "6":
                if("Turn On".equals(data.getString("Action"))) {
                    GUI.bathroomLight.turnOn();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DAREJob.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    GUI.bathroomLight.turnOff();
                } else {
                    GUI.bathroomLight.turnOff();
                }
                break;
            case "7":
                if("Turn On".equals(data.getString("Action"))) {
                    GUI.livingRoomLight.turnOn();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DAREJob.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    GUI.livingRoomLight.turnOff();
                } else {
                    GUI.livingRoomLight.turnOff();
                }
                break;
            case "8":
                if("Turn On".equals(data.getString("Action"))) {
                    GUI.livingRoomFan.turnOn();
                } else {
                    GUI.livingRoomFan.turnOff();
                }
                break;
            case "9":
                
                break;
            case "10":
                
                break;
        }
    }
}
