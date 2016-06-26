#include "FreeRTOS/Arduino_FreeRTOS.h"
#include "FreeRTOS/croutine.h"
#include "FreeRTOS/event_groups.h"
#include "FreeRTOS/FreeRTOSConfig.h"
#include "FreeRTOS/FreeRTOSVariant.h"
#include "FreeRTOS/list.h"
#include "FreeRTOS/mpu_wrappers.h"
#include "FreeRTOS/portable.h"
#include "FreeRTOS/portmacro.h"
#include "FreeRTOS/projdefs.h"
#include "FreeRTOS/queue.h"
#include "FreeRTOS/semphr.h"
#include "FreeRTOS/StackMacros.h"
#include "FreeRTOS/task.h"
#include "FreeRTOS/timers.h"

#include "blueduino_499_proj_init.h"
#include "499_data_types.h"

#define SENSOR_PERIOD_US 1000;

void setup() {
  delay(1000);
  blueduino_init();
  delay(10000);

  Serial.println("end init");
  //vTaskStartScheduler();
}

void loop() {
  //EMPTY FOR RTOS IMPLEMENTATION
}
