#ifndef _499_DATA_TYPES_H
#define _499_DATA_TYPES_H

typedef struct{
	uint8_t time;
	uint8_t ctrl;
	int8_t high_g_x;
	int8_t high_g_y;
	int8_t high_g_z;
	int16_t low_g_x;
	int16_t low_g_y;
	int16_t low_g_z;
	int16_t gyro_x;
	int16_t gyro_y;
	int16_t gyro_z;
} bluetooth_msg;

#endif