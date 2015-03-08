# Captain Picar

This is the source code for Captain Picar. A little autonomous driving robot built with a [Raspberry Pi][RASPI], the [GroviPi Shield][GROVEPI] and [Grove Sensors][GROVE]. 

[RASPI]: http://www.raspberrypi.org/
[GROVEPI]: http://www.dexterindustries.com/GrovePi/
[GROVE]: http://www.seeedstudio.com/wiki/GROVE_System

## What he can do 

_Captain Picar can explore rooms which were not explored by a robot before_

Currently Captain Picar does this using a very simple driving logic:

* Drive until distance to an an obstacle drops below a threshold (measured by an ultrasonic sensor)
* If an obstacle is detected: Rotate until distance is above the threshold again
* Keep same rotating direction if next obstacle is detected only after a short amount of time. 
Use new random rotating direction if driving for a little longer time was possible.  

This is nothing sophisticated but already nice to watch. My kids like to play with the robot (placing obstacles in front of him). 

## His components
Currently Picar uses the following components:

* Raspberry Pi Model B+
* DAGU Multi Chassis-4WD Robot Kit (with 4 DC motors)
* Poweradd Apollo 7200mAh (LiIon Portable Charger which powers the Raspberry Pi)
* Grove I2C Motor driver
* Grove Ultrasonic sensor
* Grove LCD Display with RGB backlight
* Grove Button

## How he is programmed
Captain Picar is programmed in Java. Grove sensors and the GrovePi don't come with Java example code. However the [pi4j I/O API and libraries][PI4J] can be used to control the GrovePi, the connected analog and digital Grove modules or other modules which use the I2C protocol. I've looked at GrovePi's Python example code and Arduino example code for the Grove modules to figure out how to control GrovePi and the modules. 

For development I connect over ssh to the Raspberry Pi. I start a tool called  [RemoteVM][REMOTEVM] on the Pi. Then I can write and compile the code at my desktop PC and launch it from there.
 
[PI4J]: http://pi4j.com/
[REMOTEVM]: http://remotevm.abstracthorizon.org

## Code license
The code of this repo is open source, licensed under the MIT License (MIT).


 
