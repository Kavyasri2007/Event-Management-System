# Event Management System (Java)
# Project Overview

The Event Management System is a Java-based console application that allows users to log in, view available events, and book tickets.
The system is built using Object-Oriented Programming (OOP) principles and implements file handling to achieve persistent data storage using text files, without relying on any database or DBMS.

This project demonstrates core Java concepts, clean modular design, and practical handling of real-world data persistence.

# Features

User login system with role-based access (Admin & User)
View available events
Ticket booking functionality
Persistent storage using text files
Menu-driven console interface
Modular and object-oriented design

# Technologies & Concepts Used

Language: Java
IDE: Visual Studio Code (VS Code)

Core Concepts:
Object-Oriented Programming (OOP)
Classes and Objects
Encapsulation
Constructors
File Handling
Storage: Text files (.txt)
Interface: Console-based application

# Prerequisites

Java Development Kit (JDK) 8 or higher
Visual Studio Code (VS Code) or any Java-supported IDE

# Data Storage

The application uses Java file handling to store data persistently in text files:

users.txt – stores user login details and roles
events.txt – stores event information
tickets.txt – stores ticket booking records
This ensures data is retained even after the program terminates, without using any external database.

# Note: The .txt files included in this repository contain sample data only for demonstration purposes.

# Project Structure
Event-Management-System/
│
├── Main.java
├── EventBookingSystemApp.java
├── Event.java
├── User.java
├── Ticket.java
│
├── users.txt
├── events.txt
└── tickets.txt
# File Description

Main.java
Entry point of the application. Contains the main() method and starts the system.

EventBookingSystemApp.java
Handles the core logic of the application such as displaying events, booking tickets, and managing user interaction.

Event.java
Represents an event with details like event name, date, location, and price.

User.java
Represents a user/customer participating in event booking.

Ticket.java
Represents a booked ticket containing user and event information.


# How to Run the Project
Clone the repository:
git clone <your-github-repo-link>

Navigate to the project folder:
cd Event-Management-System

Compile the Java files:
javac *.java

Run the program:
java Main
