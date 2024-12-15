import java.io.*;
import java.net.*;

public class FileClient {
    public static void main(String[] args) {
        String hostname = "127.0.0.1";  // Server IP
        int port = 5000;  // Server port

        try (Socket socket = new Socket(hostname, port)) {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter filename to request from server: ");
            String filename = consoleReader.readLine();  // Get filename from the user

            // Send filename to server
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(filename);

            // Read and display the file content from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // Print received content (file or error message)
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
