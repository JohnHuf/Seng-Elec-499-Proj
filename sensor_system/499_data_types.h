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
}bluetooth_msg;

class 499_BT_FIFO{
	public:
		499_BT_FIFO(uint8_t size = 16);
		
		~499_BT_FIFO();
		
		bool push(bluetooth_msg MSG);
		bluetooth_msg pop();
		
	private:
		bluetooth_msg * data, front, back;
		uint8_t size, count;
}

499_BT_FIFO::499_BT_FIFO(uint8_t size){
	data = (bluetooth_msg) new(sizeof(bluetooth_msg)*size);
	back = data;
	front = data;
	this->size = size;
	count = 0;
}

499_BT_FIFO::~499_BT_FIFO(){
	delete(data);
}



#endif