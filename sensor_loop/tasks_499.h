#ifndef _TASKS_499_H
#define _TASKS_499_H

#include "blueduino_499_proj_init.h"
#include "499_data_types.h"
#include "AB_BLE.h"

extern BT_FIFO * glb_msg_fifo_ptr;
extern MPU6050 _lowG_Gyro;

bluetooth_msg poll_task(){
	bluetooth_msg temp;
	int16_t accX, accY, accZ, gyroX, gyroY, gyroZ;
	Serial.println("Polling Sensors...");
	_lowG_Gyro.getMotion6(&accX, &accY, &accZ, &gyroX, &gyroY, &gyroZ);

	
	temp.time = (uint8_t) millis();
	temp.ctrl = BOTH_MSG;
	temp.low_g_x = (int8_t) accX/256;
	temp.low_g_y = (int8_t) accY/256;
	temp.low_g_z = (int8_t) accZ/256;
	temp.gyro_x = (int8_t) gyroX/256;
	temp.gyro_y = (int8_t) gyroY/256;
	temp.gyro_z = (int8_t) gyroZ/256;
  temp.high_g_x = high_g_read(HIGH_G_ACCEL_OUT_X);
  temp.high_g_y =  high_g_read(HIGH_G_ACCEL_OUT_Y);
  temp.high_g_z = high_g_read(HIGH_G_ACCEL_OUT_Z);
  //Lock mutex to prevent preemption wrecking fifo?
  Serial.println("Sensor polling complete.");
  return temp;
}

void BT_send_task(bluetooth_msg * temp){
	AB_BLE bluetooth(&Serial1);
	unsigned char msgBuffer[12];
	Serial.println("Sending Bluetooth");

	msgBuffer[0] = temp->time;
	msgBuffer[1] = temp->ctrl;
	msgBuffer[2] = temp->high_g_x;
	msgBuffer[3] = temp->high_g_x;
	msgBuffer[4] = temp->high_g_x;
	msgBuffer[5] = temp->low_g_x;
	msgBuffer[6] = temp->low_g_x;
	msgBuffer[7] = temp->low_g_x;
	msgBuffer[8] = temp->gyro_x;
	msgBuffer[9] = temp->gyro_x;
	msgBuffer[10] = temp->gyro_x;
	msgBuffer[11] = 0x00;
	bluetooth.write(msgBuffer, 12);
	
	Serial.println("Transmission Complete");

	
}

#endif
