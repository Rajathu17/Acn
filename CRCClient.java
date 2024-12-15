import java.io.*;
import java.net.*;

public class CRCClient {
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
        String hostname = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.print("Enter data to send to the server: ");
            String data = consoleReader.readLine();

            // Send data to the server
            out.println(data);

            // Receive CRC from the server
            int serverCRC = Integer.parseInt(in.readLine());
            System.out.printf("Received CRC from server: 0x%04X%n", serverCRC);

            // Calculate CRC on the client side
            int clientCRC = calculateCRC(data.getBytes());
            System.out.printf("Calculated CRC on client: 0x%04X%n", clientCRC);

            // Verify CRC
            if (serverCRC == clientCRC) {
                System.out.println("CRC verification passed!");
            } else {
                System.out.println("CRC verification failed!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
