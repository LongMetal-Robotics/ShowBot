#include <Adafruit_NeoPixel.h>
#include <Wire.h>

#define STRIP_PIN 13
#define LED_COUNT 16

Adafruit_NeoPixel strip(LED_COUNT, STRIP_PIN, NEO_GRB + NEO_KHZ800);

#define I2C_ADDR 84

bool disabled = true;
char stat = 'f';

void setup() {
  Wire.begin(I2C_ADDR);
  Wire.onReceive(receiveEvent);

  strip.begin();
  strip.show();
}

void loop() {
  if (disabled) {
    for (int i = 0; i < LED_COUNT; i += 2) {
      strip.setPixelColor(i, 255, 32, 0);
    }
  } else {  // Enabled
    if (millis() % 1000 < 500) {  // Within the first half of every second
      for (int i = 0; i < LED_COUNT; i += 2) {
        strip.setPixelColor(i, 255, 32, 0);
      }
    } else {
      for (int i = 0; i < LED_COUNT; i += 2) {
        strip.setPixelColor(i, 0, 0, 0);
      }
    }
    if (stat == 70) {
      for (int i = 1; i < LED_COUNT; i += 2) {
        strip.setPixelColor(i, 0, 255, 0);
      }
    } else if (stat == 66) {
      for (int i = 1; i < LED_COUNT; i += 2) {
        strip.setPixelColor(i, 0, 0, 255);
      }
    } else if (stat == 83) {
      for (int i = 0; i < LED_COUNT; i++) {
        strip.setPixelColor(i, 255, 0, 255);
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
