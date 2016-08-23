#include <SPI.h>
#include <BLEPeripheral.h>

#define LED_PIN     3
#define BUTTON_PIN  4

#define BLE_REQ     10
#define BLE_RDY     2
#define BLE_RST     9
 
int ledState = 0;

// create peripheral instance
BLEPeripheral blePeripheral = BLEPeripheral(BLE_REQ, BLE_RDY, BLE_RST);

// create service
BLEService lightswitch = BLEService("FF10");


void setup() {
  Serial.begin(9600);

  pinMode(LED_PIN, OUTPUT);
  
  // set advertised local name and service UUID
  blePeripheral.setLocalName("Light Switch");  // Advertised in scan data as part of GAP
  blePeripheral.setDeviceName("Smart Light Switch"); // Advertised in generic access as part of GATT
  blePeripheral.setAdvertisedServiceUuid(lightswitch.uuid());

  // assign event handlers for connected, disconnected to peripheral
  blePeripheral.setEventHandler(BLEConnected, blePeripheralConnectHandler);
  blePeripheral.setEventHandler(BLEDisconnected, blePeripheralDisconnectHandler);
  
  // begin initialization
  blePeripheral.begin();
}

void loop() {
  blePeripheral.poll(); 

}
void blePeripheralConnectHandler(BLECentral& central) {
  //digitalWrite(LED_PIN, HIGH);
}

void blePeripheralDisconnectHandler(BLECentral& central) {
   //digitalWrite(LED_PIN, LOW);
}


