package org.longmetal;

public class Constants {
    // Drive Train
    public static final double kMAX_SPEED_MULT = 0.5;
    public static final double kSPEED_MODIFIER = 0.7;
    public static final double kTHROTTLE_SHIFT = 1.05;
    public static final double kCURVE_MODIFIER = -0.25;

    public static final int kREAR_LEFT = 2;
    public static final int kFRONT_LEFT = 1;
    public static final int kREAR_RIGHT = 4;
    public static final int kFRONT_RIGHT = 3;

    // Input
    public static final int kLEFT_STICK = 0;
    public static final int kRIGHT_STICK = 1;

    // Arduino
    public static final int kI2C_ADDRESS = 168; // This is address 84 because the RIO is weird (it shifts the address 1 bit)
}