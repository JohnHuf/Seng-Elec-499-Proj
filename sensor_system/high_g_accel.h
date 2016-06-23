//This header file defines constants pertinent to the high G accelerometer
#ifndef _HIGH_G_ACCEL_H
#define _HIGH_G_ACCEL_H

#include "stdint.h"

//****Function declarations****
void 	high_g_init();
void 	high_g_write(uint8_t addr, uint8_t data);
uint8_t	high_g_read(uint8_t addr);

//****CS define****
#define SPI_CS	10

//****Device Properties****
#define HIGH_G_ACCEL_MAX_SPI_CLK	10000000 //pg 17
#define HIGH_G_ACCEL_BIT_ORDER		MSBFIRST //pg 20
#define HIGH_G_ACCEL_SPI_MODE		SPI_MODE3 //pg 20
#define HIGH_G_ACCEL_READ			0x80
#define HIGH_G_ACCEL_WRITE			0x00
#define HIGH_G_ACCEL_BURST_MODE		0x40

//****Registers**** (pg 24 of data sheet)
#define HIGH_G_ACCEL_WHO_AM_I		0x0F
#define HIGH_G_ACCEL_CTRL_REG1		0x20
#define HIGH_G_ACCEL_CTRL_REG2		0x21
#define HIGH_G_ACCEL_CTRL_REG3		0x22
#define HIGH_G_ACCEL_CTRL_REG4		0x23
#define HIGH_G_ACCEL_CTRL_REG5		0x24
#define HIGH_G_ACCEL_HP_FILTER_REST	0x25
#define HIGH_G_ACCEL_REFERENCE		0x26
#define HIGH_G_ACCEL_STATUS_REG		0x27
#define HIGH_G_ACCEL_OUT_X			0x29
#define HIGH_G_ACCEL_OUT_Y			0x2B
#define HIGH_G_ACCEL_OUT_Z			0x2D
#define HIGH_G_ACCEL_INT1_CFG		0x30
#define HIGH_G_ACCEL_INT1_SRC		0x31
#define HIGH_G_ACCEL_INT1_THS		0x32
#define HIGH_G_ACCEL_INT1_DURATION	0x33
#define HIGH_G_ACCEL_INT2_CFG		0x34
#define HIGH_G_ACCEL_INT2_SRC		0x35
#define HIGH_G_ACCEL_INT2_THS		0x36
#define HIGH_G_ACCEL_INT2_DURATION	0x37


//****Control register 1 bits (R/W)**** (pg 25)
//Power modes: bits[7:5]
#define HIGH_G_PWR_DOWN 0x00
#define HIGH_G_PWR_NRML 0x20
//other low power modes possible see data sheet

//Output data rate(in Hz): bits[4:3]
#define HIGH_G_ODR_50 	0x00
#define HIGH_G_ODR_100	0x08
#define HIGH_G_ODR_400	0x10
#define HIGH_G_ODR_1000	0x18

//Axis enables: Zen,Yen,Xen bits[2:0]
#define HIGH_G_AXIS_ALL	0x07


//****Control register 2 bits (R/W)**** (pg 26)
//BOOT: bit[7]
#define HIGH_G_REBOOT_MEM	0x80

//High pass filter settings: bits[6:0]
//see sheet if needed


//****Control register 3 bits (R/W)**** (pg 28)
//Interrupt control register
//see sheet if interrupts needed


//****Control register 4 bits (R/W)**** (pg 28)
//Scale Selection: bit[4]
#define HIGH_G_SCALE_100 	0x00
#define HIGH_G_SCALE_200 	0x10

//SPI Serial interface type: bit[0]
#define HIGH_G_SPI_4WIRE	0x00
#define HIGH_G_SPI_3WIRE	0x01


//****Control register 5 bits (R/W)**** (pg 29)
//Sleep modes: bits[1:0]
#define HIGH_G_SLP_MODE 	0x03


//****HP filter reset (R)**** (pg 29)
//Read to reset HP filter


//****Reference (R/W)**** (pg29)
//sets reference for HPF see data sheet


//****Status Reg (R)**** (pg 30)
//Data overwites: ZYXOR, ZOR, YOR, XOR bits[7:4]
#define HIGH_G_XYZ_OR 	0x80
#define HIGH_G_Z_RO		0x04
#define HIGH_G_Y_RO		0x02
#define HIGH_G_X_RO		0x01

//Data available: ZYXDA, ZDA, YDA, XDA bits[3:0]
#define HIGH_G_XYZ_DA 	0x08
#define HIGH_G_Z_DA		0x04
#define HIGH_G_Y_DA		0x02
#define HIGH_G_X_DA		0x01


//**** data out (R) **** 
//Data out registers


//See data sheet if interupts needed for remaining registers
#endif
