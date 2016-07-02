

#include "blueduino_499_proj_init.h"
#include "499_data_types.h"
#include "tasks_499.h"

#define SENSOR_PERIOD_US 1000;

void setup() {
  delay(1000);
  blueduino_init();
  delay(10000);

  Serial.println("end init");
}

void loop() {
  bluetooth_msg msg = poll_task();
  BT_send_task(&msg);
}
