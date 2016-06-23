#ifndef _BLUEDUINO_499_PROJ_INIT_H
#define _BLUEDUINO_499_PROJ_INIT_H

#include "high_g_accel.h"

#include <SPI.h>


#define USB_BAUD_RATE 9600

/** Pin Initialization function
*		Sets up pins to:
*			SPI Port on pins 9-11 & 26
*			I2C port on pins 18&19
*			BLE module on pins 13, 20, 21
*			USB on pins 3&4
*		Remaining programmable pins to high impedance
**/
void blueduino_init(){
	//SPI initialization
	pinMode(SPI_CS, OUTPUT);
	digitalWrite(SPI_CS, HIGH);
	SPI.beginTransaction(SPISettings(HIGH_G_ACCEL_MAX_SPI_CLK, HIGH_G_ACCEL_BIT_ORDER, HIGH_G_ACCEL_SPI_MODE));
	SPI.begin();
	
	//High G accelerometer init
	high_g_init();
	
	//USB serial 
	Serial.begin(USB_BAUD_RATE);
}


#endif
