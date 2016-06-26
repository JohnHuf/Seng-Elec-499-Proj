#ifndef _TASKS_499_H
#define _TASKS_499_H

#include <Arduino_FreeRTOS.h>

void HighG_poll_task( void *pvParameters ){
  high_g_read(HIGH_G_ACCEL_OUT_Z);
  high_g_read(HIGH_G_ACCEL_OUT_Y);
  high_g_read(HIGH_G_ACCEL_OUT_X);

}

void LowG_poll_task( void *pvParameters ){
	
}

void BT_send_task( void *pvParamters ){
	
}

#endif