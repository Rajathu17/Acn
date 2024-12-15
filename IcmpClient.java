import java.io.*;
import java.net.*;
import java.util.*;

public class IcmpClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.print("Enter data to send in ICMP packet: ");
            String data = scanner.nextLine();
            
            byte[] packetData = createIcmpPacket(data);

            // Send ICMP Echo Request to server
            InetAddress serverAddress = InetAddress.getByName("localhost");
            DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, serverAddress, 12345);
            clientSocket.send(sendPacket);
            System.out.println("Sent ICMP Echo Request to server with data: " + data);

            // Receive ICMP Echo Reply
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            System.out.println("Received ICMP Echo Reply from server with data: " + new String(receivePacket.getData(), 8, receivePacket.getLength() - 8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to create an ICMP packet
    private static byte[] createIcmpPacket(String data) {
        byte[] packet = new byte[8 + data.length()]; // 8 bytes for header + data length
        packet[0] = 8;  // Type: Echo Request (8)
        packet[1] = 0;  // Code: 0
        packet[2] = 0;  // Checksum (to be filled later)
        packet[3] = 0;  // Checksum (to be filled later)
        packet[4] = 1;  // Identifier (arbitrary)
        packet[5] = 1;  // Sequence Number (arbitrary)

        // Copy user-defined data into packet
        byte[] dataBytes = data.getBytes();
        System.arraycopy(dataBytes, 0, packet, 8, dataBytes.length);

        // Calculate checksum and update packet
        int checksum = computeChecksum(packet);
        packet[2] = (byte) (checksum >> 8);
        packet[3] = (byte) (checksum & 0xFF);

        return packet;
    }

    // Method to compute checksum for ICMP packet
    private static int computeChecksum(byte[] data) {
        int checksum = 0;
        int i = 0;

        while (i < data.length - 1) {
            checksum += (data[i] << 8 & 0xFF00) | (data[i + 1] & 0xFF);
            i += 2;
        }

        if (i < data.length) {
            checksum += (data[i] & 0xFF);
        }

        checksum = (checksum >> 16) + (checksum & 0xFFFF);
        checksum += (checksum >> 16);
        return ~checksum & 0xFFFF; 
    }
}
