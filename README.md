# ShowBot
 The code for LongMetal's upcoming Show Bot

## Building
 As part of the build process (specific to us), Gradle runs scripts in the Bash shell.  
### \*NIX
 You should be able to build this project without any problems, as long as Bash is installed.  
### Windows
 Since Windows doesn't natively come with Bash, you must install it. As long as it is in your PATH (you can call it from the Run dialog, etc.), the project will build. Here is what we do to install Bash:  
 1. Install GitHub Desktop from https://desktop.github.com/.  
 2. Find the location of the Bash binary (it should be C:\Program Files\Git\usr\bin).
 3. Add that directory to PATH
 4. You should be all set!



### Suggestion from Mr Shulkin and Mr Pearce - Mentors for Control Functionality for the “SHOW BOT”

All controls are on the one “Gamepad”.  Simplicity and Portability of the system wins.

Right Joystick
- Robot movement controls.
- forward, backward, turns, in tank mode, because portability > complexity.

Left Joystick
- up down axis should control power of shooter motor controls.
- left and right axis should attempt to control ‘curve ball’ speed and direction.


RT 
- Fire the ball.   This should run the shooter motors and singulator motor.

RB
- Toggles Collector on and off.


LT
- Lower the “Angle” motor

LB
- Increase the “Angle” motor.



B Button:
- Timed Random Mode
(Think showing off for a sponsor or group of kids) 
* press it once, let go of controller kind of actions
- Run the motors for shooter at random speeds
- randomly vary the angle
- randomly vary the spin. (Difference in shooter speeds)
* should last 5 to 10 seconds max, aiming to have hopper empty of balls.

X Button:
- while held, Run the Shooter motors in reverse directions to each other to attempt to show a ball spinning and not launching.
