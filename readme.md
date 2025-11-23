# 🌱 IoT Greenhouse Automation System (2018)

This project was developed as my final project for my Technical Informatics Course in 2018.  
It is an IoT-based greenhouse automation solution designed to monitor environmental conditions and remotely control irrigation.

---

## 📌 Overview
The system is composed of three integrated applications:

- 📱 **Android App (Java):** Provides real-time monitoring and allows manual irrigation control.  
- 🌐 **Webservice (PHP vanilla):** Receives and stores sensor data, exposes HTTP endpoints, and relays commands to the microcontroller.  
- 🔌 **ESP8266 Firmware (.ino):** Reads sensors, sends data to the server, and controls the solenoid valve through a relay.

---

## 🌡 Sensors & Actuators
- 🌱 Soil moisture sensor  
- 🌡 Temperature sensor (DHT series)  
- 💧 Solenoid valve for irrigation  
- ⚡ Relay module  
- 📡 ESP8266 Wi-Fi microcontroller  

---

## 🚀 Features
- Real-time soil moisture monitoring  
- Greenhouse temperature monitoring  
- Remote irrigation trigger via Android app  
- Wireless HTTP communication  
- Manual and automatic irrigation modes  

---

## 🗄 Database
The backend webservice uses **MariaDB** to store:

- Sensor readings (soil moisture, temperature)  
- Irrigation commands  
- Historical logs  

---

## 🧭 System Architecture
1. The ESP8266 reads soil moisture and temperature values.  
2. The microcontroller sends sensor readings to the PHP webservice over HTTP.  
3. The PHP backend stores the data in **MariaDB**.  
4. The Android app fetches readings from the webservice and displays them to the user.  
5. The user can trigger irrigation through the app.  
6. The webservice sends a command to the ESP8266.  
7. The microcontroller activates the solenoid valve via a relay.  

---

## 📅 Project Year
2018 — Technical Course Final Project (TCC)