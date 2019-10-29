package org.longmetal;

import edu.wpi.first.wpilibj.Joystick;

public class Input {
    private int leftStickPortInit, rightStickPortInit;
    public Joystick forwardStick, turnStick;
    private boolean isQuinnDrive = false;

    public Input(int leftStickPortInit, int rightStickPortInit) {
        this.leftStickPortInit = leftStickPortInit;
        this.rightStickPortInit = rightStickPortInit;
        forwardStick = new Joystick(leftStickPortInit);
        turnStick = new Joystick(rightStickPortInit);
    }

    public void setQuinnDrive(boolean doIt) {
        if (doIt) {
            forwardStick = new Joystick(rightStickPortInit);
            turnStick = new Joystick(leftStickPortInit);
        } else {
            forwardStick = new Joystick(leftStickPortInit);
            turnStick = new Joystick(rightStickPortInit);
        }
        isQuinnDrive = doIt;
    }

    public boolean isQuinnDrive() {
        return isQuinnDrive;
    }
}