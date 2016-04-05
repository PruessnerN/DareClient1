/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bp;

import db.JDBCSQLServerConnection;
import db.Schedule;
import java.sql.SQLException;
import java.util.List;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import ui.GUI;
import static org.quartz.JobBuilder.newJob;

/**
 *
 * @author root
 */
public class ManageSchedules {
    private JDBCSQLServerConnection db = new JDBCSQLServerConnection();
    
    
    public void startSchedules() throws SQLException, SchedulerException {
        List<Schedule> schedules = db.getSchedules(GUI.pubnub.getUUID());

        for (Schedule schedule : schedules) {
            JobDetail job = newJob(DAREJob.class)
                .withIdentity(Integer.toString(schedule.getScheduleID()), schedule.getName())
                .usingJobData("Action", schedule.getAction())
                .usingJobData("ThingID", Integer.toString(schedule.getThingID()))
                .build();
            
            Trigger trigger = newTrigger()
                .withIdentity(Integer.toString(schedule.getScheduleID()), schedule.getName())
                .startNow()
                .withSchedule(cronSchedule(schedule.getCronExpression()))
                .build();
            System.out.println("Built single job.");
            GUI.scheduler.scheduleJob(job, trigger);
        }
        
    }
    
    public void refreshSchedules() throws SQLException, SchedulerException {
        GUI.scheduler.clear();
        
        List<Schedule> schedules = db.getSchedules(GUI.pubnub.getUUID());

        for (Schedule schedule : schedules) {
            JobDetail job = newJob(DAREJob.class)
                .withIdentity(Integer.toString(schedule.getScheduleID()), schedule.getName())
                .usingJobData("Action", schedule.getAction())
                .usingJobData("ThingID", Integer.toString(schedule.getThingID()))
                .build();
            
            Trigger trigger = newTrigger()
                .withIdentity(Integer.toString(schedule.getScheduleID()), schedule.getName())
                .startNow()
                .withSchedule(cronSchedule(schedule.getCronExpression()))
                .build();
            System.out.println("Built single job.");
            GUI.scheduler.scheduleJob(job, trigger);
        }
    }
}
