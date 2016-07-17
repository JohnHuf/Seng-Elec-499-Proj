#ifndef _TASKS_499_H
#define _TASKS_499_H

#include "FreeRTOS/Arduino_FreeRTOS.h"

#include "blueduino_499_proj_init.h"
#include "499_data_types.h"
#include "AB_BLE.h"


extern BT_FIFO * glb_msg_fifo_ptr;
extern MPU6050 _lowG_Gyro;
extern  TaskHandle_t highGHandle;

void timerInterrupt();

void HighG_poll_task(){
	bluetooth_msg tempMsg;
	int8_t tempX, tempY, tempZ;
	
	Timer1.initialize(10000);
	Timer1.attachInterrupt(&timerInterrupt);
  
	for(;;){
		Serial.println("high g start");

		
		
		tempX = high_g_read(HIGH_G_ACCEL_OUT_Z);
		tempY = high_g_read(HIGH_G_ACCEL_OUT_Y);
		tempZ = high_g_read(HIGH_G_ACCEL_OUT_X);

		
		tempMsg.time = (uint8_t) millis();
		tempMsg.ctrl = HIGH_G_MSG;
		tempMsg.high_g_x = tempX;
		tempMsg.high_g_y = tempY;
		tempMsg.high_g_z = tempZ;
		//Set Others to zero?
  
		//Lock mutex to prevent preemption wrecking fifo?
		if(!glb_msg_fifo_ptr->push(tempMsg)){}
			//Handle errors?
	}
}





void LowG_poll_task(){
	bluetooth_msg temp;
	int16_t accX, accY, accZ, gyroX, gyroY, gyroZ;
//	for(;;){
		Serial.println("Low G start");
		
		_lowG_Gyro.getMotion6(&accX, &accY, &accZ, &gyroX, &gyroY, &gyroZ);
	
		
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
		Serial.println("Low G end");
    Serial.println(millis());
		//vTaskDelay(6);
	//}
}

void schedule_tasks(void *pvParameters){
  Scheduler_Init();
  Scheduler_StartTask(0,1,LowG_poll_task);
  Scheduler_StartTask(0,1,HighG_poll_task);
  Scheduler_Dispatch();
}
void BT_send_task( void *pvParamters ){
	AB_BLE bluetooth(&Serial1);
	unsigned char msgBuffer[12];
	bluetooth_msg temp;
	for(;;){
		
		Serial.println("BLE start");
		temp = glb_msg_fifo_ptr->pop();
		if(temp.time == 0 && temp.ctrl == 0){
			//empty msg
		}
		else{
			;
			msgBuffer[0] = temp.time;
			msgBuffer[1] = temp.ctrl;
			msgBuffer[2] = temp.high_g_x;
			msgBuffer[3] = temp.high_g_x;
			msgBuffer[4] = temp.high_g_x;
			msgBuffer[5] = temp.low_g_x;
			msgBuffer[6] = temp.low_g_x;
			msgBuffer[7] = temp.low_g_x;
			msgBuffer[8] = temp.gyro_x;
			msgBuffer[9] = temp.gyro_x;
			msgBuffer[10] = temp.gyro_x;
			msgBuffer[11] = 0x00;
			bluetooth.write(msgBuffer, 12);
		}	
		
		Serial.println("BLE end");
    Serial.println(millis());
		vTaskDelay(10);
	}
}

void timerInterrupt(){
	Serial.println("interrupt");
	Timer1.stop();
	vTaskResume(highGHandle);
}

#endif
