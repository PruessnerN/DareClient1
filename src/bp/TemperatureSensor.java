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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
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
public class TemperatureSensor {

    private static Pin pinNumber = RaspiPin.GPIO_06;
    private static GpioPinDigitalOutput pin;
    public static String ID = "9";

    public void getTemp() throws JSONException {
        if (pin == null) {
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
        double state;
        try {
            state = getCalculatedTemp();
            root.put("id", ID);
            child.put("state", state);
            commands.put(child);
            root.put("commands", commands);
            GUI.pubnub.publish("pruessner_tribe", root, callback);
        } catch (IOException ex) {
            Logger.getLogger(TemperatureSensor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public double getCalculatedTemp() throws IOException {
        String w1DirPath = "/sys/bus/w1/devices";
        File dir = new File(w1DirPath);
        File[] files = dir.listFiles(new DirectoryFileFilter());
        if (files != null) {
            for (File file : files) {
                System.out.print(file.getName() + ": ");
                // Device data in w1_slave file
                String filePath = w1DirPath + "/" + file.getName() + "/w1_slave";
                File f = new File(filePath);
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String output;
                    while ((output = br.readLine()) != null) {
                        int idx = output.indexOf("t=");
                        if (idx > -1) {
                            // Temp data (x1000) in 5 chars after t=
                            float tempC = Float.parseFloat(
                                    output.substring(output.indexOf("t=") + 2));
                            // Divide by 1000 to get degrees Celsius
                            tempC /= 1000;
                            return (double) tempC;
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        double bad = 0.0;
        return bad;
    }

}

class DirectoryFileFilter implements FileFilter {

    public boolean accept(File file) {
        String dirName = file.getName();
        String startOfName = dirName.substring(0, 3);
        return (file.isDirectory() && startOfName.equals("28-"));
    }
}
