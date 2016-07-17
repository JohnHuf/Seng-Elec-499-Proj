#ifndef _BLUEDUINO_499_PROJ_INIT_H
#define _BLUEDUINO_499_PROJ_INIT_H

#include "high_g_accel.h"
#include "MPU6050.h"
#include "tasks_499.h"
#include "499_data_types.h"
#include "timer1_code/timerOne.h"

#include <SPI.h>
#include <Wire.h>

#define USB_BAUD_RATE 9600
#define LOW_G_1KHz   	7
#define MSG_FIFO_SIZE	16

MPU6050 _lowG_Gyro;
BT_FIFO * glb_msg_fifo_ptr;
TaskHandle_t highGHandle;

/** 
 *  Initialization function
**/
void blueduino_init(){
	//global variable init
	glb_msg_fifo_ptr = new BT_FIFO(MSG_FIFO_SIZE);
	
	//SPI initialization
	pinMode(SPI_CS, OUTPUT);
	digitalWrite(SPI_CS, HIGH);
	SPI.beginTransaction(SPISettings(HIGH_G_ACCEL_MAX_SPI_CLK, HIGH_G_ACCEL_BIT_ORDER, HIGH_G_ACCEL_SPI_MODE));
	SPI.begin();
	
	//High G accelerometer init
	high_g_init();

  /**
   * Low G/Gyro Init
   * Using MPU6050_test.ino as reference file for initalization procedure
   **/
  Wire.begin();
  _lowG_Gyro.initialize();
  //set sample rate to 1 KHz (Same as High G)
  _lowG_Gyro.setRate(LOW_G_1KHz);
  //Set G range to 16
  _lowG_Gyro.setFullScaleAccelRange(MPU6050_ACCEL_FS_16);
  //Leave Gyro for now? 250
	
	//USB serial 
	Serial.begin(USB_BAUD_RATE);
	Serial1.begin(USB_BAUD_RATE);

  //Task Initializations
 // xTaskCreate(HighG_poll_task,(const portCHAR *)"HIGH_G_POLL",128,NULL,1,NULL);

  xTaskCreate(schedule_tasks,(const portCHAR *)"LOW_G_POLL",128, NULL,3, NULL);

  xTaskCreate( BT_send_task, (const portCHAR *)"BT_MSG",128, NULL, 1, NULL);
}


#endif
