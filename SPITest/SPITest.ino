
#include <SPI.h>


const byte cs =  10;

void setup() {
  pinMode(cs,OUTPUT);
  digitalWrite(cs,HIGH);
  
  SPI.beginTransaction(SPISettings(10000000, MSBFIRST, SPI_MODE3));
  SPI.begin();
  SPIWrite(0x20,0xE7);

  Serial.begin(9600);
 
}


uint8_t SPIRead(uint8_t addr){
  digitalWrite(cs,LOW);
  SPI.transfer(addr);
  uint8_t ret = SPI.transfer(0x00);
  digitalWrite(cs,HIGH);
  return ret;
}

void SPIWrite(uint8_t addr,uint8_t data){
   digitalWrite(cs,LOW);
  SPI.transfer(0x80|addr);
  SPI.transfer(data);
   digitalWrite(cs,HIGH);
}

void loop() {

  uint8_t ret = SPIRead(0x29);
  Serial.println(ret);
  delay(50);
}
