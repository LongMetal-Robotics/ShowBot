package org.longmetal;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

public class Arduino {
    private SerialPort port;
    private byte[] toSend = new byte[1];

    /**
     * Initialize communication with the Arduino
     */
    public Arduino() {
        port = new SerialPort(Constants.kSERIAL_BAUD_RATE, Port.kUSB);
    }

    /**
     * Send a status to the Arduino. The status is sent as a single character (ASCII)
     * @param status The status to send
     */
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

            case PROBLEM:
                toSend[0] = 80; // P
                break;
            
            default:
                toSend[0] = 0;  // NUL
                return;
        }

        port.write(toSend, 1);  // Send the data to the Arduino
    }


    public enum Status {
        ENABLED,
        DISABLED,
        FORWARD,
        BACKWARD,
        SHOOTING,
        PROBLEM
    }
}
