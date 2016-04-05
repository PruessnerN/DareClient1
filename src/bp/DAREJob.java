/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bp;

import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ui.GUI;
import static ui.GUI.pubnub;

/**
 *
 * @author root
 */
public class DAREJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getMergedJobDataMap();
        String id = data.getString("ThingID");
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                GUI.printMessage(response.toString());
            }

            public void errorCallback(String channel, PubnubError error) {
                GUI.printMessage(error.toString());
            }
        };
        
        JSONObject root = new JSONObject();
        JSONObject commands = new JSONObject();
        

        if("Turn On".equals(data.getString("Action"))) {
            try {
                root.put("id",id);
                commands.put("action","1");
                commands.put("state","1");
                root.put("commands", commands);
                GUI.printMessage(root.toString());
                pubnub.publish("pruessner_tribe", root, callback);
            } catch (JSONException ex) {
                Logger.getLogger(DAREJob.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if("Turn Off".equals(data.getString("Action"))) {
            try {
                root.put("id",id);
                commands.put("action","0");
                commands.put("state","0");
                root.put("commands", commands);
                pubnub.publish("pruessner_tribe", root, callback);
            } catch (JSONException ex) {
                Logger.getLogger(DAREJob.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
