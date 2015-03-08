package com.github.jbman.picar.captain;

import java.io.IOException;

import com.github.jbman.jgrove.GrovePi;
import com.github.jbman.jgrove.sensors.Button;

/**
 * Class to get press state of button and detecting a long press.
 * 
 * @author Johannes Bergmann
 */
public class ButtonPress {

	public static enum Press {
		None, ShortPress, LongPress
	};

	private final Button button;

	public ButtonPress(Button button) {
		this.button = button;
	}

	public Press getPress() throws IOException {
		boolean buttonPressed = button.isPressed();
		if (buttonPressed) {
			GrovePi.sleep(500);
			// Button still pressed?
			if (button.isPressed()) {
				return Press.LongPress;
			}
		}
		return buttonPressed ? Press.ShortPress : Press.None;
	}

}
