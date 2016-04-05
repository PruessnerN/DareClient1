/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bp;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;
import db.JDBCSQLServerConnection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ui.GUI;

/**
 *
 * @author root
 */
public class Powerswitch {
    private static Pin pinNumber = RaspiPin.GPIO_00;
    private static GpioPinDigitalOutput pin;
    public static int ID = 8;
    private JDBCSQLServerConnection database = new JDBCSQLServerConnection();


    public void turnOn() {
        if(pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            pin = gpio.provisionDigitalOutputPin(pinNumber, "MyLED", PinState.LOW);
        }
        if(pin.getState() == PinState.LOW) {
            pin.high();
            try {
                database.logEvent(ID, "Turned On", "Powerswitch");
            } catch (SQLException ex) {
                Logger.getLogger(Light.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void turnOff() {
        if(pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            pin = gpio.provisionDigitalOutputPin(pinNumber, "MyLED", PinState.LOW);
        }
        if(pin.getState() == PinState.HIGH) {
            pin.low();
            try {
                database.logEvent(ID, "Turned Off", "Powerswitch");
            } catch (SQLException ex) {
                Logger.getLogger(Light.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void getState() throws JSONException {
        if(pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            pin = gpio.provisionDigitalOutputPin(pinNumber, "MyLED", PinState.LOW);
        }
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
        String state = (pin.getState() == PinState.HIGH) ? "1" : "0";


        root.put("id",ID);
        commands.put("state",state);
        root.put("commands", commands);
        GUI.pubnub.publish("pruessner_tribe", root, callback);
    }
}
