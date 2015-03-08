package com.github.jbman.picar.captain;

import java.io.IOException;

import com.github.jbman.jgrove.GrovePi;
import com.github.jbman.jgrove.GroveSensors;
import com.github.jbman.jgrove.command.Command;
import com.github.jbman.jgrove.sensors.Button;
import com.github.jbman.jgrove.sensors.Ultrasonic;
import com.github.jbman.jgrove.sensors.i2c.MotorDriver;
import com.github.jbman.jgrove.sensors.i2c.RgbLcd;
import com.github.jbman.picar.captain.ButtonPress.Press;

/**
 * Code for the Raspberry Pi and GrovePi based robot "Captain J.L. Picar". The
 * robot uses an Ultrasonic sensor, the Grove MotorDriver a button for
 * start/stop and the Grove LCD with color backlight for messages.
 * 
 * @author Johannes Bergmann
 */
public class Picar {

	private final GrovePi grovePi;
	private final Ultrasonic ultrasonic;
	private final MotorDriver motor;
	private final Button button;
	private final RgbLcd lcd;

	public static void main(String[] args) throws Exception {
		new Picar().run();
	}

	public Picar() throws IOException {
		grovePi = new GrovePi();
		GroveSensors sensors = new GroveSensors(grovePi);
		ultrasonic = sensors.createUltrasonic(4);
		motor = new MotorDriver();
		button = sensors.createButton(3);
		lcd = new RgbLcd();
	}

	private void run() throws Exception {
		new Command<Boolean>(this::waitAndDrive).loopWhile(Boolean.TRUE);
		bye();
	}

	private Boolean waitAndDrive() throws IOException {

		// Recover LCD and motor if last run was interrupted.
		motor.setSpeed(0, 0);
		lcd.shutdown();

		// Display welcome message
		lcd.setColor(0, 200, 0);
		lcd.setText("Hello, here is\nCaptain Picar");
		lcd.display(true);

		// Wait for button press to start with driving
		Press press = new Command<Press>(new ButtonPress(button)::getPress)
				.loopWhile(Press.None);
		if (press.equals(Press.LongPress)) {
			// Exit
			return Boolean.FALSE;
		}

		// Start driving
		lcd.setColor(200, 0, 0);
		lcd.setText("Keep Away! I'm Driving");

		// Execute drive loop
		new Command<Boolean>(new Drive(ultrasonic, motor, button)::drive)
				.loopWhile(Boolean.TRUE);

		// Stop the engine
		motor.setSpeed(0, 0);

		// Display stop message
		lcd.setColor(0, 0, 200);
		lcd.setText("I was stopped");
		GrovePi.sleep(1000);
		lcd.clearText();
		// Fade blue to green:
		for (int i = 0; i <= 200; i++) {
			lcd.setColor(0, i, 200 - i);
			GrovePi.sleep(10);
		}

		// Wait again for start
		return Boolean.TRUE;
	}

	private void bye() throws IOException {
		lcd.setText("Good bye!");
		for (int i = 0; i <= 200; i++) {
			lcd.setColor(200 - i, 200 - i, 0);
			GrovePi.sleep(10);
		}
		lcd.shutdown();
	}
}
