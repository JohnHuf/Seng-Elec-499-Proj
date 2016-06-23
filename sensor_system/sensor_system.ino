#include "blueduino_499_proj_init.h"

void setup() {
  // put your setup code here, to run once:
  delay(1000);
  blueduino_init();
  Serial.println("High G Test");
}

void loop() {
  // put your main code here, to run repeatedly:

  uint8_t axis_status = 0;
  uint8_t axis_x = 0;
  uint8_t axis_y = 0;
  uint8_t axis_z = 0;

  do{
    axis_status = high_g_read(HIGH_G_ACCEL_STATUS_REG);

    if(axis_status & HIGH_G_Z_DA)
      axis_z = high_g_read(HIGH_G_ACCEL_CTRL_REG1);
    if(axis_status & HIGH_G_Y_DA)
      axis_y = high_g_read(HIGH_G_ACCEL_OUT_Y);
    if(axis_status & HIGH_G_X_DA)
      axis_x = high_g_read(HIGH_G_ACCEL_OUT_X);
  }while(axis_x & axis_y & axis_z);
  
  Serial.print("X: ");
  Serial.print(axis_x);
  Serial.print("\tY: ");
  Serial.print(axis_x);
  Serial.print("\tZ: ");
  Serial.println(axis_x);

  delay(15);
}
