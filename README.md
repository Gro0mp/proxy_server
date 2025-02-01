# Proxy Server Program

## Overview
This project implements a simple proxy server that allows a client to send GET requests to retrieve web pages through the proxy. The server handles incoming client requests, fetches the requested content from the target web server, and forwards it back to the client.

## Components
The project consists of two main Java programs:

1. **HW1Server.java** (Proxy Server)
   - Listens for client connections on a specified port.
   - Handles multiple client requests using multithreading.
   - Forwards HTTP GET requests to the appropriate remote web server.
   - Relays the response from the web server back to the client.

2. **HW1Client.java** (Client)
   - Connects to the proxy server on the specified host and port.
   - Sends HTTP GET requests to the proxy server.
   - Receives and saves the server response to a local file.

## Usage

### 1. Compile the Code
Ensure you have Java installed. Compile both the server and client programs using:
```sh
javac HW1Server.java HW1Client.java
```

### 2. Run the Proxy Server
Start the server on a specific port (e.g., 8080):
```sh
java HW1Server 8080
```

### 3. Run the Client
Connect the client to the proxy server:
```sh
java HW1Client <server_host> 8080
```
Replace `<server_host>` with the actual hostname or IP address where the server is running.

### 4. Sending a Request
Once the client is connected, enter a GET request:
```
GET <URL>
```
Example:
```
GET www.example.com/index.html
```
The proxy server will fetch the requested webpage and send it back to the client, where it will be saved as a local file.

## Features
- Supports multiple clients using threading.
- Handles HTTP GET requests.
- Saves server responses to files on the client-side.
- Adds "http://" if missing from the requested URL.

## Limitations
- Only handles HTTP GET requests (no POST, PUT, DELETE, etc.).
- Connects to port 80 on remote servers (no HTTPS support).
- Minimal error handling for malformed requests.

## Troubleshooting
- Ensure the server is running before starting the client.
- Use valid URLs in the GET requests.
- Check firewall settings if the connection fails.

