/* Pinout for Arduino
 * D13 - DATA to NeoPixels
 * SDA/SCL - Connected to RIO through pull-up resistors
*/
#include <Adafruit_NeoPixel.h>
#include <Wire.h>

#define STRIP_PIN 13
#define LED_COUNT 16

Adafruit_NeoPixel strip(LED_COUNT, STRIP_PIN, NEO_GRB + NEO_KHZ800);

#define I2C_ADDR 84

bool disabled = false;
char stat = 'n';

#define PULSE_TIME 500
#define STATUS_LUM 100
#define RSL_R 255
#define RSL_G 32
#define RSL_B 0

void setup() {
  Wire.begin(I2C_ADDR);
  Wire.onReceive(receiveEvent);

  strip.begin();
  strip.show();
}

void loop() {
  if (disabled) {
    for (int i = 0; i < LED_COUNT; i++) {
      strip.setPixelColor(i, RSL_R, RSL_G, RSL_B);
    }
  } else {  // Enabled
    if (millis() % PULSE_TIME < PULSE_TIME / 2) {  // Pulse with a cycle time of PULSE_TIME
      for (int i = 0; i < LED_COUNT; i += 2) {
        strip.setPixelColor(i, RSL_R, RSL_G, RSL_B);
      }
    } else {
      for (int i = 0; i < LED_COUNT; i += 2) {
        strip.setPixelColor(i, 0, 0, 0);
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

void receiveEvent(int howMany) {
  while (Wire.available()) {
    char c = Wire.read();
    switch (c) {
      case 69: // ENABLED
        disabled = false;
        break;

      case 68: // DISABLED
        disabled = true;
        break;

      case 70: // FORWARD
        stat = 'f';
        break;

      case 66: // BACKWARD
        stat = 'b';
        break;

      case 83: // SHOOTING
        stat = 's';
    }
  }
}
