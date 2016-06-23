#include "stdint.h"
#include "high_g_accel.h"

#include <SPI.h>

void 	high_g_init(){
  //Initialize Ctrl Reg 1
	high_g_write(HIGH_G_ACCEL_CTRL_REG1, HIGH_G_PWR_NRML | HIGH_G_ODR_50 | HIGH_G_AXIS_ALL);
}

void 	high_g_write(uint8_t addr, uint8_t data){
  digitalWrite(SPI_CS,LOW);
  SPI.transfer(addr);
  SPI.transfer(data);
  digitalWrite(SPI_CS,HIGH);
}

uint8_t	high_g_read(uint8_t addr){
  digitalWrite(SPI_CS,LOW);
  SPI.transfer(0x80|addr);
  uint8_t ret = SPI.transfer(0x00);
  digitalWrite(SPI_CS,HIGH);
  return ret;
}
