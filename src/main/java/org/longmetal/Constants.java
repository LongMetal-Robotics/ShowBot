package org.longmetal;

public class Constants {
    // Drive Train
    public static final double kMAX_SPEED_MULT = 0.5;
    public static final double kSPEED_MODIFIER = 0.7;
    public static final double kTHROTTLE_SHIFT = 1.05;
    public static final double kCURVE_MODIFIER = -0.25;

    public static final int kP_REAR_LEFT = 2;
    public static final int kP_FRONT_LEFT = 1;
    public static final int kP_REAR_RIGHT = 4;
    public static final int kP_FRONT_RIGHT = 3;

    // Input
    public static final int kP_LEFT_STICK = 0;
    public static final int kP_RIGHT_STICK = 1;
    public static final int kFORWARD_BUTTON = 5;
    public static final int kREVERSE_BUTTON = 3;
    public static final int kP_GAMEPAD = 2;
    
    // Communication
    public static final int kSERIAL_BAUD_RATE = 9600;

    // Shooter
    public static final int kANGLE = 5;
    public static final int kP_LSHOOTER = 0;
    public static final int kP_RSHOOTER = 1;
    public static final double kSHOOTER_MIN = 0.1;
    public static final double kSHOOTER_MAX = 0.75;
    public static final int kP_SINGULATOR = 2;
    public static final double kSINGULATOR_MIN = 0;
    public static final double kSINGULATOR_MAX = 0.5;

    // Collector
    public static final int kP_COLLECTOR = 3;

    // Preferences
    public static final String kSHOOTER_KEY = "SHOOTER";
    public static final String kCOLLECTOR_KEY = "COLLECTOR";
}