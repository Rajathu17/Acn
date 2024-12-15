import java.io.*;
import java.net.*;

public class FileServer {
    public static void main(String[] args) {
        int port = 5000; // Port number the server listens on
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept()) {  // Accept a client connection
                    System.out.println("Client connected");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                    String filename = reader.readLine();  // Read the filename from the client
                    System.out.println("Client requested: " + filename);

                    File file = new File(filename);
                    if (file.exists() && !file.isDirectory()) {
                        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                            String line;
                            while ((line = fileReader.readLine()) != null) {
                                writer.println(line);  // Send file content
                            }
                        }
                    } else {
                        writer.println("File not found");
                    }
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println("Server error: " + ex.getMessage());
        }
    }
}
