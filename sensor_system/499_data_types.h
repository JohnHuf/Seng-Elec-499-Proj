#ifndef _499_DATA_TYPES_H
#define _499_DATA_TYPES_H

#define HIGH_G_MSG 0x01;
#define LOW_G_MSG  0x02;

typedef struct{
	uint8_t time;
	uint8_t ctrl;
	int8_t high_g_x;
	int8_t high_g_y;
	int8_t high_g_z;
	int8_t low_g_x;
	int8_t low_g_y;
	int8_t low_g_z;
	int8_t gyro_x;
	int8_t gyro_y;
	int8_t gyro_z;
}bluetooth_msg;

class BT_FIFO{
	public:
		BT_FIFO(uint8_t size = 16);
		
		~BT_FIFO();
		
		bool push(bluetooth_msg MSG);
		bluetooth_msg pop();
		
	private:
		bluetooth_msg * data;
		uint8_t size, count, front, back;
};

BT_FIFO::BT_FIFO(uint8_t size){
	data = new bluetooth_msg[size];
	back = 0;
	front = 0;
	this->size = size;
	count = 0;
}

BT_FIFO::~BT_FIFO(){
	delete(data);
}

bool BT_FIFO::push(bluetooth_msg MSG){
	if(count == size){
		return false;
	}
	
	data[back] = MSG;
	back++;
	count++;
	
	return true;
}

bluetooth_msg BT_FIFO::pop(){
	if(count == 0){
		bluetooth_msg temp;
		temp.time = 0;
		temp.ctrl = 0;
		return temp;
	}
	
	bluetooth_msg temp = data[front];
	front++;
	count--;
	
	return temp;
}


#endif