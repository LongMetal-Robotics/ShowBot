package org.longmetal;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

public class Arduino {
    SerialPort port;
    byte[] toSend = new byte[1];
    boolean ready = false;

    public Arduino() {
        try {
            port = new SerialPort(Constants.kSERIAL_BAUD_RATE, Port.kUSB);
            ready = true;
        } catch (Exception e) {
            System.out.println("[SERIAL:INIT]\tSomething went wrong with Serial initialization. Stack trace and message are below");
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void sendStatus(Status status) {
        switch (status) {
            case ENABLED:
                toSend[0] = 69; // E
                break;

            case DISABLED:
                toSend[0] = 68; // D
                break;

            case FORWARD:
                toSend[0] = 70; // F
                break;

            case BACKWARD:
                toSend[0] = 66; // B
                break;

            case SHOOTING:
                toSend[0] = 83; // S
                break;
            
            default:
                toSend[0] = 0;  // NUL
                return;
        }

        port.write(toSend, 1);
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
