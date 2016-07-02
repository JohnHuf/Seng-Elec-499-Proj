

#include "blueduino_499_proj_init.h"
#include "499_data_types.h"
#include "tasks_499.h"

#define SENSOR_PERIOD_US 1000;

void setup() {
  blueduino_init();
  delay(10000);

  Serial.println("end init");
}

void loop() {
  //Loop to poll and send sensor data via Bluetooth
  unsigned long loop_begin_time = micros();
  bluetooth_msg msg = poll_task();
  BT_send_task(&msg);
  Serial.println("DONE!");
  Serial.print("TIME USED:  ");
  Serial.println((micros()-loop_begin_time));
}
