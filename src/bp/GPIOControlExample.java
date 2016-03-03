/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bp;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import ui.GUI;

/**
 *
 * @author natha
 */
public class GPIOControlExample {

    private static int PWM_PIN = 5;

    /**
     *
     * @throws InterruptedException
     */
    public GPIOControlExample() throws InterruptedException {

        GUI.printMessage("GPIO Control Example ... started.");

        Gpio.wiringPiSetup();

        SoftPwm.softPwmCreate(PWM_PIN, 0, 100);

        int counter = 0;
        while (counter < 3) {
            // fade LED to fully ON
            for (int i = 0; i <= 100; i++) {
                // softPwmWrite(int pin, int value)
                // This updates the PWM value on the given pin. The value is
                // checked to be in-range and pins
                // that haven't previously been initialized via softPwmCreate
                // will be silently ignored.
                SoftPwm.softPwmWrite(PWM_PIN, i);
                Thread.sleep(25);
            }
            // fade LED to fully OFF
            for (int i = 100; i >= 0; i--) {
                SoftPwm.softPwmWrite(PWM_PIN, i);
                Thread.sleep(25);
            }
            counter++;
        }

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an output pin and turn on
        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED", PinState.HIGH);

        // set shutdown state for this pin
        pin.setShutdownOptions(true, PinState.LOW);

        GUI.printMessage("GPIO state should be: ON");

        Thread.sleep(5000);

        // turn off gpio pin #01
        pin.low();
        GUI.printMessage("GPIO state should be: OFF");

        Thread.sleep(5000);

        // toggle the current state of gpio pin #01 (should turn on)
        pin.toggle();
        GUI.printMessage("GPIO state should be: ON");

        Thread.sleep(5000);

        // toggle the current state of gpio pin #01  (should turn off)
        pin.toggle();
        GUI.printMessage("GPIO state should be: OFF");

        Thread.sleep(5000);

        // turn on gpio pin #01 for 1 second and then off
        GUI.printMessage("GPIO state should be: ON for only 1 second");
        pin.pulse(1000, true); // set second argument to 'true' use a blocking call

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}
