import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running and waiting for client connections...");
            
            while (true) { // Keep the server running to accept multiple clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");

                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String message;
                    while ((message = in.readLine()) != null) { // Keep listening for messages from the client
                        out.println("Uppercase: " + message.toUpperCase());
                        out.println("Length: " + message.length());
                    }
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                } finally {
                    clientSocket.close();
                    System.out.println("Client disconnected.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
