# Se Gateway Application
FH Campus Wien - SDE - Service Engineering - Final Project Part 2

The SE Gateway is a Spring Cloud Gateway application designed to provide dynamic routing, monitoring, resiliency, 
security, and more. This application acts as a gateway to the following web services, 
routing requests to the appropriate backend services based on the predefined configurations.

- Frontend Application
- Authentication Service
- Car Management Service
- Booking Service
- Currency Converter Service

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them:

- JDK 1.8 or later
- Maven 4.0.0

### Installing

A step-by-step series of examples that tell you how to get a development environment running:

1. Clone the repository:
```bash
git clone https://github.com/JasmineYil/se-gateway.git
```
2. Navigate to the project directory:
```bash
cd se-gateway
```
3. Build the project with Maven:
```bash
mvn clean install
```
4. Run the application:
```bash
mvn spring-boot:run
```
The server will start on port 9090 as specified in the application.properties file.

### Usage
Once the application is running, you can access the gateway at:
```bash
http://localhost:9090/
```
The application is configured to route requests to various backend services according to the routing rules defined in application.properties.

For example, requests to http://localhost:9090/user/** will be routed to the frontend-service running on http://localhost:9091.

### Building for Production
To build the application jar for production deployment, run:
```bash
mvn clean package
```
This will compile, test, and package the application into a runnable jar file located in the target directory.



