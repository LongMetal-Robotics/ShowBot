/* Pinout for Arduino
 * D13 - DATA to NeoPixels
 * SDA/SCL - Connected to RIO through pull-up resistors
*/
#include <Adafruit_NeoPixel.h>

#define STRIP_PIN 13
#define LED_COUNT 16

Adafruit_NeoPixel strip(LED_COUNT, STRIP_PIN, NEO_GRB + NEO_KHZ800);

#define BAUD_RATE 9600

bool disabled = true;
char stat = 'n';

#define PULSE_TIME 500
#define STATUS_LUM 100
#define RSL_R 255
#define RSL_G 32
#define RSL_B 0

void setup() {
  Serial.begin(BAUD_RATE);

  strip.begin();
  strip.show();
}

void loop() {
  receiveEvent();

  for (int i = 0; i < LED_COUNT; i++) {
    strip.setPixelColor(i, 0, 0, 0);
  }

  if (disabled) {
    for (int i = 0; i < LED_COUNT; i++) {
      strip.setPixelColor(i, RSL_R, RSL_G, RSL_B);
    }
  } else {  // Enabled
    if (millis() % PULSE_TIME < PULSE_TIME / 2) {  // Pulse with a cycle time of PULSE_TIME
      for (int i = 0; i < LED_COUNT; i += 2) {
        strip.setPixelColor(i, RSL_R, RSL_G, RSL_B);
      }
      
      if (stat == 'p') {
        for (int i = 1; i < LED_COUNT; i += 2) {
          strip.setPixelColor(i, 0, 0, 0);
        }
      }
    } else {
      for (int i = 0; i < LED_COUNT; i += 2) {
        strip.setPixelColor(i, 0, 0, 0);
      }
      if (stat == 'p') {
        for (int i = 1; i < LED_COUNT; i += 2) {
          strip.setPixelColor(i, STATUS_LUM, 0, 0);
        }
      }
    }
    if (stat == 'f') {
      for (int i = 1; i < LED_COUNT; i += 2) {
        strip.setPixelColor(i, 0, STATUS_LUM, 0);
      }
    } else if (stat == 'b') {
      for (int i = 1; i < LED_COUNT; i += 2) {
        strip.setPixelColor(i, 0, 0, STATUS_LUM);
      }
    } else if (stat == 's') {
      for (int i = 0; i < LED_COUNT; i++) {
        strip.setPixelColor(i, STATUS_LUM, 0, STATUS_LUM);
      }
    }
  }
  strip.show();
}

void receiveEvent() {
  while (Serial.available() > 0) {
    char c = Serial.read();
    switch (c) {
      case 69: // 'E'NABLED
        disabled = false;
        break;

      case 68: // 'D'ISABLED
        disabled = true;
        break;

      case 70: // 'F'ORWARD
        stat = 'f';
        break;

      case 66: // 'B'ACKWARD
        stat = 'b';
        break;

      case 83: // 'S'HOOTING
        stat = 's';
        break;

      case 80: // 'P'ROBLEM
        stat = 'p';
        break;

      default:
        stat = 'n';
    }
  }
}
