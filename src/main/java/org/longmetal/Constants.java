package org.longmetal;

public class Constants {
    // Drive Train
    public static final double kSPEED_MODIFIER = 0.7;   // Speed modifier (on throttle)
    public static final double kTHROTTLE_SHIFT = 1.05;  // Shift throttle up
    public static final double kCURVE_MODIFIER = -0.25; // Curve modifier
    // Spark Max CAN IDs
    public static final int kREAR_LEFT = 2;
    public static final int kFRONT_LEFT = 1;
    public static final int kREAR_RIGHT = 4;
    public static final int kFRONT_RIGHT = 3;

    // Input
    public static final double kINPUT_DEADBAND = 0.1;   // If the value hasn't changed by more than this much we'll ignore it
    // Drive joysticks
    public static final int kP_LEFT_STICK = 0;  // Left joystick port
    public static final int kP_RIGHT_STICK = 1; // Right joystick port
    public static final int kFORWARD_BUTTON = 5;    // Forward button
    public static final int kREVERSE_BUTTON = 3;    // Reverse button
    // Gamepad
    public static final int kP_GAMEPAD = 2; // Gamepad port
    public static final int kA_TRIGGER = 3; // Trigger axis number
    public static final int kA_LS_X = 0;    // Left stick X axis #
    public static final int kA_LS_Y = 1;    // Left stick Y axis #
    public static final int kA_RS_Y = 5;    // Right stick Y axis #
    public static final int kY_AXIS_MODIFIER = -1;  // Normally, down is positive. We want up to be positive.
    
    // Communication
    public static final int kSERIAL_BAUD_RATE = 9600;   // Serial baud rate (for Arduino)
    // String literals for multiple reuses
    public static final String kENABLED = "Enabled";
    public static final String kDISABLED = "Disabled";

    // Shooter
    public static final String kSHOOTER_STATE_KEY = "Shooter Enabled";  // SmartDashboard value key
    public static final String kSHOOTER_ENABLER_KEY = "Enable Shooter";
    // Shooter
    public static final int kP_LSHOOTER = 0;    // Left shooter PWM #
    public static final int kP_RSHOOTER = 1;    // Right shooter PWM #
    public static final double kSHOOTER_MIN = 0.1;  // Min shooter speed
    public static final double kSHOOTER_MAX = 0.75; // Max shooter speed (to protect against excessive amperage or safety I guess)
    public static final double kSHOOTER_SPEED_MODIFIER = 1; // Decrease shooter speed
    // Effect of modifiers on finals speeds
    public static final double kSHOOTER_X_MODIFIER = 0.5;
    public static final double kSHOOTER_Y_MODIFIER = 0.5;
    // Singulator
    public static final int kP_SINGULATOR = 2;  // Singulator PWM #
    public static final double kSINGULATOR_MIN = 0; // Min singulator speed
    public static final double kSINGULATOR_MAX = 0.5;   // Max singulator speed (to protect certain mechanisms/excessive amperage I guess)
    // Angle
    public static final int kP_ANGLE = 5;   // Angle CAN ID
    public static final double kANGLE_SPEED_MODIFIER = 1;

    // Collector
    public static final String kCOLLECTOR_STATE_KEY = "Collector Enabled"; // SmartDashboard value key
    public static final String kCOLLECTOR_ENABLER_KEY = "Enable Collector";
    public static final int kP_COLLECTOR = 3;   // Collector PWM #

    // Preferences
    public static final String kSHOOTER_KEY = "SHOOTER";
    public static final String kCOLLECTOR_KEY = "COLLECTOR";
}