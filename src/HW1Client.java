import java.io.*;
import java.net.*;

public class HW1Client {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java HW1Client <host> <portNumber>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to the server. Type your GET command:");

            // Read user input.
            String userInput = stdIn.readLine();

            if (userInput.startsWith("GET")) {
                // Send the GET command to the server.
                out.println(userInput);

                // Parse the file name to save the response in a local file.
                String[] response = userInput.split(" ");
                if (response.length == 2) {
                    String urlPath = response[1];
                    String fileName = extractFileName(urlPath);

                    // Create a file output stream to save the received data.
                    try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName))) {
                        String serverInput;
                        System.out.println("Receiving data from server and saving to file: " + fileName);

                        // Read and save the response from the server.
                        while ((serverInput = in.readLine()) != null) {
                            fileWriter.write(serverInput + "\n");
                            fileWriter.flush();
                            System.out.println(serverInput);
                        }
                        System.out.println("File saved successfully.");
                    }
                } else {
                    System.out.println("Invalid command");
                }
            } else {
                System.out.println("Invalid command.");
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown Host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostName);
            System.exit(1);
        }
    }

    // Helper function to extract the file name from the URL.
    private static String extractFileName(String urlPath) {
        if (urlPath.endsWith("/")) {
            return "index.html";
        }

        // Extract the file name from the URL path.
        String[] urlParts = urlPath.split("/");
        return urlParts[urlParts.length - 1];
    }
}