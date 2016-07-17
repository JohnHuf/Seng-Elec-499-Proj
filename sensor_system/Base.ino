#include "Arduino.h"
#include "LiquidCrystal.h"
#include "scheduler.h"

const int MAX = 10;

int screenQueue[MAX];
int screenFront = 0;
int screenRear = 0;

int LASER = 0;
int SERVO = 1;
int SCREEN = 2;

LiquidCrystal lcd(8, 9, 4, 5, 6, 7);

int servoState = 90;

int buzzer = 39;

int photocellPin = A7;    // the cell and 10K pulldown are connected to A7
int buzzerPin = 8;        // Digital pin

int photocellReading;     // the analog reading from the analog resistor divider
int joyX = A8;            // x
int joyZ = 52;            // Push-Down button

int laser = 1;
int previousLaser = 1;

int stopped = 0;

// Function prototypes
int isFull(int *front, int *rear);
int isEmpty(int *front, int *rear);
void enqueue(int val, int *queue, int *front, int *rear);
int dequeue(int *queue, int *front, int *rear);
void setup();
void bluetoothReceive();
void screenTask();
void laserTask();
void lightSensorTask();
void movementTask();
void readByte(int *inByte);


// ------------------------------ isFull ------------------------------ //
int isFull(int *front, int *rear) {
  return *rear == (*front - 1) & MAX;
}

int isEmpty(int *front, int *rear) {
  return *rear == *front;
}

// ------------------------------ Enqueue ------------------------------ //
void enqueue(int val, int *queue, int *front, int *rear){
  if(isFull(front, rear)) {
    return;
  }

  queue[*rear] = val;
  *rear = (*rear + 1) % MAX;
}

// ------------------------------ Dequeue ------------------------------ //
int dequeue(int *queue, int *front, int *rear) {
  if(isEmpty(front, rear)) {
    return -1;
  }

  int result = queue[*front];

  *front = (*front + 1) % MAX;

  return result;
}

void setup() {

  // TTA
  Scheduler_Init();

  // Start tasks arguments
  // Offset in ms, period in ms, function callback
  Scheduler_StartTask(25, 100, laserTask);
  Scheduler_StartTask(20, 100, lightSensorTask);
  Scheduler_StartTask(15, 200, movementTask);
  Scheduler_StartTask(0, 150, bluetoothReceive);
  Scheduler_StartTask(50, 150, screenTask);
  
  // Serial
  Serial.begin(9600);
  Serial1.begin(9600);
  
  // LCD
  lcd.begin(16, 2);

  lcd.setCursor(0, 0);
  lcd.print("Servo:");

  lcd.setCursor(0, 1);
  lcd.print("Laser:");

  // Buzzer
  pinMode(buzzer, OUTPUT);
  
  // Joystick
  pinMode(joyX, INPUT);
  pinMode(joyZ, INPUT_PULLUP);
  
  // Buzzer
  pinMode(buzzerPin, OUTPUT);
  digitalWrite(buzzerPin, HIGH);

  // Logic Analyzer
  pinMode(31, OUTPUT);
  pinMode(43, OUTPUT);
  pinMode(45, OUTPUT);
  pinMode(47, OUTPUT);
  pinMode(49, OUTPUT);
  pinMode(51, OUTPUT);

  digitalWrite(31, LOW); // idle
  digitalWrite(43, LOW); // bluetoothReceive
  digitalWrite(45, LOW); // laserTask
  digitalWrite(47, LOW); // lightSensorTask
  digitalWrite(49, LOW); // screenTask
  digitalWrite(51, LOW); // movementTask
}

void readByte(int *inByte) {
  while(1) {
    if(Serial1.available() > 0) {
      *inByte = Serial1.read() - '0';
      break;
    }
  }
}

void bluetoothReceive() {
  digitalWrite(43, HIGH);
  if(Serial1.available() > 0) {
    int flag;
    int laserData;
    int digits;
    int servoData;
    int servoData1;
    int servoData2;
    int servoData3;
  
    readByte(&flag);
    readByte(&laserData);
    readByte(&digits);

    if (digits == 1) {
      readByte(&servoData);
    }
    else if(digits == 2) {
      readByte(&servoData1);
      readByte(&servoData2);

      servoData = servoData1*10 + servoData2;
    }
    else {
      readByte(&servoData1);
      readByte(&servoData2);
      readByte(&servoData3);

      servoData = servoData1*100 + servoData2*10 + servoData3;
    }
  
    if (flag == SCREEN) {
      enqueue(laserData, screenQueue, &screenFront, &screenRear);
      enqueue(servoData, screenQueue, &screenFront, &screenRear);
    }
  }
  digitalWrite(43, LOW);
}

void screenTask() {
  digitalWrite(49, HIGH);
  if(!isEmpty(&screenFront, &screenRear)) {

    lcd.setCursor(6,1);

    int laserState = dequeue(screenQueue, &screenFront, &screenRear);

    if (laserState == 0) {
      lcd.print("1");
    }
    else if (laserState == 1){
      lcd.print("0");
    }
  
    lcd.setCursor(6,0);
  
    int servoState = dequeue(screenQueue, &screenFront, &screenRear);
  
    lcd.print(servoState);
    lcd.print("  ");
  
    lcd.setCursor(11, 1);

    if (photocellReading > 650) {
      lcd.print("HIT  ");
    }
    else {
      lcd.print("Clear");
    }
  }
  digitalWrite(49, LOW);
}

void laserTask() {
  digitalWrite(45, HIGH);
  laser = digitalRead(joyZ);

  if(laser != previousLaser) {
    // Send laser packet
    Serial1.print(LASER);
    Serial1.print(laser);

    previousLaser = laser;
  }
  digitalWrite(45, LOW);
}

void lightSensorTask() {
  digitalWrite(47, HIGH);
  
  photocellReading = analogRead(photocellPin);
  
  digitalWrite(47, LOW);
}

void movementTask() {
  digitalWrite(51, HIGH);
  
  int x;
  int digits;
  int newState;
  
  newState = map(analogRead(joyX), 0, 1023, 5, 175);

  if(newState < 10) {
    digits = 1;
  }
  else if (newState < 100) {
    digits = 2;
  }
  else {
    digits = 3;
  }

  if ((newState - servoState > 0 && newState > 92) || (newState < 88 && newState - servoState < 0)) {
    Serial1.print(SERVO);
    Serial1.print(digits);
    Serial1.print(newState);

    servoState = newState;
  }
  else if (newState <= 92 && newState >= 88) {

    Serial1.print(SERVO);
    Serial1.print(0);
    newState = 90;
    servoState = 90;
  }
  digitalWrite(51, LOW);
}

void idle(uint32_t idlePeriod) {
  digitalWrite(31, HIGH);
  delay(idlePeriod);
  digitalWrite(31, LOW);
}

int main() {
 init();

 setup();
 
 for(;;) {
  uint32_t idlePeriod = Scheduler_Dispatch();

  if (idlePeriod) {
    idle(idlePeriod);
  }
  
 }
}
