import java.io.*;
import java.net.*;

public class CRCServer {
    private static final int POLYNOMIAL = 0x1021; // CRC-CCITT Polynomial
    private static final int INITIAL_VALUE = 0xFFFF;

    public static int calculateCRC(byte[] data) {
        int crc = INITIAL_VALUE;
        for (byte b : data) {
            crc ^= (b << 8);
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ POLYNOMIAL;
                } else {
                    crc <<= 1;
                }
            }
        }
        return crc & 0xFFFF;
    }

    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running...");

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("Client connected!");

                    // Receive data from the client
                    String data = in.readLine();
                    System.out.println("Received from client: " + data);

                    // Calculate CRC
                    int crc = calculateCRC(data.getBytes());
                    System.out.printf("Calculated CRC for '%s': 0x%04X%n", data, crc);

                    // Send CRC back to the client
                    out.println(crc);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
