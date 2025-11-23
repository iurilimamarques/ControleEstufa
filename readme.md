# 🌱 IoT Greenhouse Automation System (2018)

This project was developed as my final project for my Technical Informatics Course in 2018.  
It is an IoT-based greenhouse automation solution designed to monitor environmental conditions and remotely control irrigation.

---

## 📌 Overview
The system is composed of three integrated applications:

- 📱 **Android App (Java):** Provides real-time monitoring, user account creation, login functionality, and manual irrigation control.  
- 🌐 **Webservice (PHP vanilla):** Handles authentication, receives and stores sensor data, exposes HTTP endpoints, and relays commands to the microcontroller.  
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
- Account creation and user login on the Android app  
- Remote irrigation trigger via mobile interface  
- Wireless communication with the backend via HTTP  
- Manual irrigation mode 

---

## 🗄 Database
The backend webservice uses **MariaDB** to store:

- User accounts and login credentials  
- Sensor readings (soil moisture, temperature)  
- Irrigation commands  
- Historical logs  

---

## 🧭 System Architecture
1. The ESP8266 reads soil moisture and temperature values.  
2. It sends sensor readings to the PHP webservice over HTTP.  
3. The PHP backend stores these values in **MariaDB**.  
4. Users create accounts and authenticate through the Android app.  
5. The app fetches readings from the webservice and displays them to the user.  
6. Authenticated users can trigger irrigation through the app.  
7. The webservice sends an irrigation command to the ESP8266.  
8. The microcontroller activates the solenoid valve via a relay.  

---

## 📅 Project Year
2018 — Technical Course Final Project (TCC)
