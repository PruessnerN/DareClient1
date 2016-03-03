package bp;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import ui.GUI;

public class GPIOControl {

    private static int PWM_PIN = 5;

    /**
     *
     * @throws InterruptedException
     */
    
    public static void pulseLED(int pPin) throws InterruptedException {
        Gpio.wiringPiSetup();

        SoftPwm.softPwmCreate(pPin, 0, 100);

        int counter = 0;
        while (counter < 3) {
            // fade LED to fully ON
            for (int i = 0; i <= 100; i++) {
                // softPwmWrite(int pin, int value)
                // This updates the PWM value on the given pin. The value is
                // checked to be in-range and pins
                // that haven't previously been initialized via softPwmCreate
                // will be silently ignored.
                SoftPwm.softPwmWrite(pPin, i);
                Thread.sleep(20);
            }
            // fade LED to fully OFF
            for (int i = 100; i >= 0; i--) {
                SoftPwm.softPwmWrite(pPin, i);
                Thread.sleep(20);
            }
            counter++;
        }
    }
    
    public static void turnLEDOn(int pPin) throws InterruptedException {
        Pin pinNumber;
        
        if(pPin > 9) {
            pinNumber = RaspiPin.getPinByName("GPIO_" + pPin);
        } else {
            pinNumber = RaspiPin.getPinByName("GPIO_0" + pPin);
        }
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an output pin and turn on
        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(pinNumber, "MyLED", PinState.HIGH);
       
        // toggle the current state of gpio pin #01 (should turn on)
        pin.toggle();
        GUI.printMessage("GPIO state should be: ON");

        Thread.sleep(2000);

        // toggle the current state of gpio pin #01  (should turn off)
        pin.toggle();
        GUI.printMessage("GPIO state should be: OFF");

        Thread.sleep(2000);
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }

}
