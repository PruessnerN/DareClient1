/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import bp.Light;
import bp.Powerswitch;
import bp.TemperatureSensor;
import bp.DoorSensor;
import bp.ManageSchedules;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.DefaultCaret;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import static org.quartz.JobBuilder.*;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author natha
 */
public class GUI extends javax.swing.JFrame {

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        try {
            SchedulerFactory sf = new StdSchedulerFactory();
            scheduler = sf.getScheduler();
        } catch (SchedulerException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        BasicConfigurator.configure();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        consolePane = new javax.swing.JTextArea();
        DefaultCaret caret = (DefaultCaret)consolePane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        consolePane.setEditable(false);
        consolePane.setBackground(new java.awt.Color(0, 0, 0));
        consolePane.setColumns(20);
        consolePane.setForeground(new java.awt.Color(0, 204, 204));
        consolePane.setRows(5);
        consolePane.setCaretColor(new java.awt.Color(0, 204, 204));
        jScrollPane1.setViewportView(consolePane);

        jButton1.setText("Kill Client");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(302, 302, 302)
                .addComponent(jButton1)
                .addContainerGap(308, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        pubnub.unsubscribe("pruessner_tribe");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    /* GLOBAL VARIABLES */
    public static Pubnub pubnub = new Pubnub("pub-c-2a63b4b0-ca5b-4f32-8db9-1c9a1d04ec33", "sub-c-deb946f2-840e-11e5-9e96-02ee2ddab7fe", true);
    public static Light livingRoomLight = new Light(7);
    public static Powerswitch livingRoomFan = new Powerswitch();
    public static TemperatureSensor houseTemperature = new TemperatureSensor();
    public static DoorSensor frontDoor = new DoorSensor();
    public static Light bathroomLight = new Light(6);
    public static int ClientID = 1;
    public static Scheduler scheduler;
    public static ManageSchedules scheduleManager;
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws InterruptedException, JSONException, IOException, SchedulerException {
        /*START GLOBAL OBJECTS/VARIABLES*/

 /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
        Thread.sleep(8000);
        pubnub.setUUID("62173AA3D1518C02CECA5A343E6349A8528752F4");

        scheduleManager = new ManageSchedules();
        try {
            scheduleManager.startSchedules();
        } catch (SQLException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        scheduler.start();

        frontDoor.pin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                try {
                    String action = "";
                    if (event.getState() == PinState.HIGH) {
                        action = "Opened";
                    } else {
                        action = "Closed";
                    }
                    frontDoor.database.logEvent(frontDoor.ID, action, "Door Sensor");
                } catch (SQLException ex) {
                    Logger.getLogger(Light.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        try {
            pubnub.subscribe("pruessner_tribe", new Callback() {

                @Override
                public void disconnectCallback(String channel, Object message) {
                    printMessage("SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());

                }

                @Override
                public void connectCallback(String channel, Object message) {
                    printMessage("SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    printMessage("SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void successCallback(String channel, Object message, String timetoken) {
                    try {
                        queryMessage(message);
                    } catch (Exception ex) {
                        printMessage(ex.toString());
                    }
                    printMessage("channel: " + channel + "    "
                            + "message: '" + message.toString() + "'");
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    printMessage("SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }
            }
            );
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }
        //implement this code when finished developing for consistent sensor querying
        /*while (true) {
            frontDoor.getState();
            houseTemperature.getTemp();
            Thread.sleep(5000);
        }*/
    }

    public static void printMessage(String message) {
        if (!"".equals(message) && message != null) {
            consolePane.append(message + "\n");
        }
    }
    
    public static void queryMessage(Object message) throws JSONException {
        if (message instanceof JSONObject) {
            JSONObject payload = (JSONObject) message;
            if (payload.getString("id").equals("6")) {
                JSONObject ja = (JSONObject) payload.getJSONObject("commands");
                if (ja.has("getState")) {
                    bathroomLight.getState();
                } else if (ja.has("action")) {
                    if (ja.getString("action").equals("1")) {
                        bathroomLight.turnOn();
                    } else if (ja.getString("action").equals("0")) {
                        bathroomLight.turnOff();
                    }
                }
            } else if (payload.getString("id").equals("7")) {
                JSONObject ja = (JSONObject) payload.getJSONObject("commands");
                if (ja.has("getState")) {
                    livingRoomLight.getState();
                } else if (ja.has("action")) {
                    if (ja.getString("action").equals("1")) {
                        livingRoomLight.turnOn();
                    } else if (ja.getString("action").equals("0")) {
                        livingRoomLight.turnOff();
                    }
                }
            } else if (payload.getString("id").equals("8")) {
                JSONObject ja = (JSONObject) payload.getJSONObject("commands");
                if (ja.has("getState")) {
                    livingRoomFan.getState();
                } else if (ja.has("action")) {
                    if (ja.getString("action").equals("1")) {
                        livingRoomFan.turnOn();
                    } else if (ja.getString("action").equals("0")) {
                        livingRoomFan.turnOff();
                    }
                }
            } else if (payload.getString("id").equals("9")) {
                JSONObject ja = (JSONObject) payload.getJSONObject("commands");
                if (ja.has("getState")) {
                    houseTemperature.getTemp();
                }
            } else if (payload.getString("id").equals("10")) {
                JSONObject ja = (JSONObject) payload.getJSONObject("commands");
                if (ja.has("getState")) {
                    frontDoor.getState();
                }
            } else if (payload.getString("id").equals("allClients")) {
                JSONObject ja = (JSONObject) payload.getJSONObject("commands");
                if (ja.has("refreshClientSchedules")) {
                    if(ja.getString("refreshClientSchedules").equals("1")) {
                        try {
                            scheduleManager.refreshSchedules();
                        } catch (SQLException ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SchedulerException ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } else {
            String smessage = (String) message;
            printMessage(smessage);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JTextArea consolePane;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
