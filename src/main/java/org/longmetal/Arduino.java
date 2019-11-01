package org.longmetal;

import edu.wpi.first.wpilibj.I2C;

public class Arduino {
    I2C i2c;
    byte[] toSend = new byte[1];
    boolean ready = false;

    public Arduino() {
        try {
            i2c = new I2C(I2C.Port.kOnboard, Constants.kI2C_ADDRESS);
            ready = true;
        } catch (Exception e) {
            System.out.println("[I2C:INIT]\tSomething went wrong with I2C initialization. Stack trace and message are below");
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void sendStatus(Status status) {
        switch (status) {
            case ENABLED:
                toSend[0] = 69;
                break;

            case DISABLED:
                toSend[0] = 68;
                break;

            case FORWARD:
                toSend[0] = 70;
                break;

            case BACKWARD:
                toSend[0] = 66;
                break;

            case SHOOTING:
                toSend[0] = 83;
                break;
            
            default:
                toSend[0] = 0;
                return;
        }

        if (i2c.transaction(toSend, 1, null, 0)) { 
            // transaction(...) returns true if the transfer was aborted.
            // We should probably mention this to the console.
            System.out.println("[I2C:ERROR]\tThe transfer was aborted. Please check the connection between the RIO and Arduino");
        }
    }

    public boolean isReady() {
        return ready;
    }


    public enum Status {
        ENABLED,
        DISABLED,
        FORWARD,
        BACKWARD,
        SHOOTING
    }
}
