/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bp;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ui.GUI;

/**
 *
 * @author root
 */
public class DoorSensor {

    private Pin pinNumber = RaspiPin.GPIO_02;
    public String ID = "10";
    GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalInput pin = gpio.provisionDigitalInputPin(pinNumber, "MyLED", PinPullResistance.PULL_UP);

    public void getState() throws JSONException {
        if (pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            pin = gpio.provisionDigitalInputPin(pinNumber, "MyLED", PinPullResistance.PULL_UP);
        }
        Callback callback = new Callback() {
            @Override
            public void successCallback(String channel, Object response) {
                GUI.printMessage(response.toString());
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                GUI.printMessage(error.toString());
            }
        };

        JSONObject root = new JSONObject();
        JSONArray commands = new JSONArray();
        JSONObject child = new JSONObject();
        String state = (pin.getState() == PinState.LOW) ? "1" : "0";


        root.put("id", ID);
        child.put("doorState", state);
        commands.put(child);
        root.put("commands", commands);
        GUI.pubnub.publish("pruessner_tribe", root, callback);
    }

}
