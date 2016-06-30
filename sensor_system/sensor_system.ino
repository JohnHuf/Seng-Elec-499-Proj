
#include "blueduino_499_proj_init.h"
#include "499_data_types.h"
#include "scheduler.h"

#define SENSOR_PERIOD_US 1000;

void setup() {
  delay(1000);
  blueduino_init();
  delay(10000);
  Serial.println("end init");

}

void loop() {
    Scheduler_Dispatch();
  //EMPTY FOR RTOS IMPLEMENTATION
}
