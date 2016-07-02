#include "blueduino_499_proj_init.h"
#include "499_data_types.h"

#define SENSOR_PERIOD_US 1000;

void setup() {
  blueduino_init();
}

void loop() {
  //Loop to poll and send sensor data via Bluetooth

  delay(1000);
  //Sample time for loop beginning
  unsigned long loop_begin_time = micros();

  //allocate Message
  bluetooth_msg bt_msg;

  //Poll Sensors
  bt_msg.high_g_z = high_g_read(HIGH_G_ACCEL_OUT_Z);
  bt_msg.high_g_y = high_g_read(HIGH_G_ACCEL_OUT_Y);
  bt_msg.high_g_x = high_g_read(HIGH_G_ACCEL_OUT_X);

  _lowG_Gyro.getMotion6(&bt_msg.low_g_x, &bt_msg.low_g_y, &bt_msg.low_g_z, &bt_msg.gyro_x, &bt_msg.gyro_y, &bt_msg.gyro_z);

  //Align axis
  // *****FIX ME*****

  Serial.println("DONE!");

  Serial.print("TIME USED:  ");
  Serial.println((micros()-loop_begin_time));
}
