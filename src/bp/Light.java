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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ui.GUI;

/**
 *
 * @author natha
 */
public class Light {
    private static Pin pinNumber = RaspiPin.GPIO_03;
    private static GpioPinDigitalOutput pin;
    public static String ID = "7";

    public void turnOn() {
        if(pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            pin = gpio.provisionDigitalOutputPin(pinNumber, "MyLED", PinState.LOW);
        }
        if(pin.getState() == PinState.LOW) {
            GUI.printMessage("Light On!");
            pin.high();
        }
    }
    
    public void turnOff() {
        if(pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            pin = gpio.provisionDigitalOutputPin(pinNumber, "MyLED", PinState.LOW);
        }
        if(pin.getState() == PinState.HIGH) {
            GUI.printMessage("Light Off!");
            pin.low();
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
        JSONArray commands = new JSONArray();
        JSONObject child = new JSONObject();
        String state;
        state = (pin.getState() == PinState.LOW) ? "0": "1";
        
        root.put("id",ID);
        child.put("state", state);
        commands.put(child);
        root.put("commands", commands);
        GUI.pubnub.publish("pruessner_tribe", root, callback);
    }
    
}
