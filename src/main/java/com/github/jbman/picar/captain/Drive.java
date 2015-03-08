package com.github.jbman.picar.captain;

import java.io.IOException;

import com.github.jbman.jgrove.GrovePi;
import com.github.jbman.jgrove.sensors.Button;
import com.github.jbman.jgrove.sensors.Ultrasonic;
import com.github.jbman.jgrove.sensors.i2c.MotorDriver;

public class Drive {

	// If the Ultrasonic measures a value below this distance,
	// the car should turn
	private static final int OBSTACLE_DISTANCE = 80;

	private static enum Mode {
		DRIVE, LEFT, RIGHT;
	}

	private final Ultrasonic ultrasonic;
	private final MotorDriver motor;
	private final Button button;

	// Driving state information
	private Mode direction = Mode.LEFT;
	private long lastRotate = System.currentTimeMillis();

	public Drive(Ultrasonic ultrasonic, MotorDriver motor, Button button) {
		this.ultrasonic = ultrasonic;
		this.motor = motor;
		this.button = button;
	}

	public Boolean drive() throws IOException {
		Mode mode = Mode.DRIVE;

		final int distance = ultrasonic.getDistance();

		if (distance < OBSTACLE_DISTANCE) {
			// If last mode was drive and last rotation was some time ago choose
			// new direction
			if (mode == Mode.DRIVE
					&& (System.currentTimeMillis() - lastRotate > 1000)) {
				direction = (direction == Mode.LEFT ? Mode.RIGHT : Mode.LEFT);
			}
			lastRotate = System.currentTimeMillis();
			// Keep last direction, until DRIVE mode lasts longer than a second.
			mode = direction;
		} else {
			mode = Mode.DRIVE;
		}

		// Carry out mode operation
		switch (mode) {
		case DRIVE:
			// Drive forwards
			motor.setDirection(MotorDriver.DIRECTION_BWD_FWD);
			motor.setSpeed(100, 250);
			break;
		case LEFT:
			rotateLeft();
			break;
		case RIGHT:
			rotateRight();
			break;
		}
		GrovePi.sleep(50);
		if (button.isPressed()) {
			// Stop the loop
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	private void rotateLeft() throws IOException {
		motor.setDirection(MotorDriver.DIRECTION_BWD_BOTH);
		motor.setSpeed(255, 255);
	}

	private void rotateRight() throws IOException {
		motor.setDirection(MotorDriver.DIRECTION_FWD_BOTH);
		motor.setSpeed(255, 255);
	}
}
