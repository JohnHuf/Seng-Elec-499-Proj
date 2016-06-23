#ifndef _BLUEDUINO_499_PROJ_INIT_H
#define _BLUEDUINO_499_PROJ_INIT_H

#include "high_g_accel.h"
#include "MPU6050.h"

#include <SPI.h>
#include <Wire.h>

#define USB_BAUD_RATE 9600

MPU6050 _lowG_Gyro;

/** 
 *  Initialization function
**/
void blueduino_init(){
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
    //Test connection?
  
  
	
	//USB serial 
	Serial.begin(USB_BAUD_RATE);
}


#endif
