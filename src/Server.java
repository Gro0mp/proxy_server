import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java HW1Server <portNumber>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is running on port: " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle each client in a separate thread.
                ClientWorker clientHandler = new ClientWorker(clientSocket);
                Thread t = new Thread(clientHandler);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber);
            System.out.println(e.getMessage());
        }
    }
}

// A separate class to handle several client requests simultaneously.
class ClientWorker implements Runnable {
    private final Socket clientSocket;

    public ClientWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        String line;
        BufferedReader in;
        PrintWriter out;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            while (true) {
                line = in.readLine();
                System.out.println("Received from client: " + line);
                // Parse the client request.
                String[] args = line.split(" ");
                if (args.length == 2 && args[0].equals("GET")) {
                    String targetURL = args[1];
                    fetchAndRelayHTTPResponse(out, targetURL);
                } else {
                    out.println("Invalid request format.");
                }
            }
        } catch (IOException e) {
            System.out.println("in or out failed");
            System.exit(-1);
        }
    }

    // Function to fetch data from remote server and send the response back to the client.
    private void fetchAndRelayHTTPResponse(PrintWriter out, String targetURL) {
        try {
            // Add "http://" if it's not already present in the URL.
            if (!targetURL.startsWith("http://") && !targetURL.startsWith("https://")) {
                targetURL = "http://" + targetURL;
            }

            System.out.println("Request target: " + targetURL);

            URL url = new URL(targetURL);  // Full URL for the request.
            String host = url.getHost();
            String path = url.getPath();  // Extract the path.

            // Debugging: print the host and path to check correctness.
            System.out.println("Connecting to host: " + host);
            System.out.println("Request path: " + path);

            BufferedReader remoteIn;
            PrintWriter remoteOut;
            // Open socket to the remote host.
            try (Socket remoteSocket = new Socket(host, 80);) {

                remoteIn = new BufferedReader(new InputStreamReader(remoteSocket.getInputStream()));
                remoteOut = new PrintWriter(remoteSocket.getOutputStream(), true);
                // Send HTTP GET request to the remote server.
                String fullPath = "GET " + path + " HTTP/1.1\r\n";
                remoteOut.write(fullPath);
                remoteOut.write("Host: " + host + "\r\n");
                remoteOut.write("Connection: close\r\n");
                remoteOut.write("\r\n");  // End of HTTP request
                remoteOut.flush();


                // Read and relay the response from the remote server to the client.
                String responseLine;
                while ((responseLine = remoteIn.readLine()) != null) {
                    out.println(responseLine); // Send each line back to the client
                    System.out.println(responseLine);
                }
            }
        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(-1);
        }
    }
}