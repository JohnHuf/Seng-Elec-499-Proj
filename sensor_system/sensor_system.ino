#include <Arduino_FreeRTOS.h>
#include <croutine.h>
#include <event_groups.h>
#include <FreeRTOSConfig.h>
#include <FreeRTOSVariant.h>
#include <list.h>
#include <mpu_wrappers.h>
#include <portable.h>
#include <portmacro.h>
#include <projdefs.h>
#include <queue.h>
#include <semphr.h>
#include <StackMacros.h>
#include <task.h>
#include <timers.h>

#include "blueduino_499_proj_init.h"
#include "499_data_types.h"

#define SENSOR_PERIOD_US 1000;

void setup() {
  blueduino_init();

  //scheduler gets automatically called
}

void loop() {
  //EMPTY FOR RTOS IMPLEMENTATION
}
