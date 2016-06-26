#ifndef _TASKS_499_H
#define _TASKS_499_H

#include <Arduino_FreeRTOS.h>

#include "blueduino_499_proj_init.h"
#include "499_data_types.h"

extern BT_FIFO * glb_msg_fifo_ptr;
extern MPU6050 _lowG_Gyro;

void HighG_poll_task( void *pvParameters ){
	for(;;){
		int8_t tempX, tempY, tempZ;
		tempX = high_g_read(HIGH_G_ACCEL_OUT_Z);
		tempY = high_g_read(HIGH_G_ACCEL_OUT_Y);
		tempZ = high_g_read(HIGH_G_ACCEL_OUT_X);

		bluetooth_msg tempMsg;
		tempMsg.time = (uint8_t) millis();
		tempMsg.ctrl = HIGH_G_MSG;
		tempMsg.high_g_x = tempX;
		tempMsg.high_g_y = tempY;
		tempMsg.high_g_z = tempZ;
		//Set Others to zero?
  
		//Lock mutex to prevent preemption wrecking fifo?
		if(!glb_msg_fifo_ptr->push(tempMsg)){}
			//Handle errors?
		
		long time = millis();
		vTaskDelay(1);
		Serial.println( millis()- time);
	}
}

void LowG_poll_task( void *pvParameters ){
	for(;;){
		int16_t accX, accY, accZ, gyroX, gyroY, gyroZ;
		_lowG_Gyro.getMotion6(&accX, &accY, &accZ, &gyroX, &gyroY, &gyroZ);
	
		bluetooth_msg temp;
		temp.time = (uint8_t) millis();
		temp.ctrl = LOW_G_MSG;
		temp.low_g_x = (int8_t) accX/2;
		temp.low_g_y = (int8_t) accY/2;
		temp.low_g_z = (int8_t) accZ/2;
		temp.gyro_x = (int8_t) gyroX/2;
		temp.gyro_y = (int8_t) gyroY/2;
		temp.gyro_z = (int8_t) gyroZ/2;
		//Set Others to zero?
  
		//Lock mutex to prevent preemption wrecking fifo?
		if(!glb_msg_fifo_ptr->push(temp)){}
			//Handle errors?
	  
		vTaskDelay(1);
	}
}

void BT_send_task( void *pvParamters ){
	
}

#endif
