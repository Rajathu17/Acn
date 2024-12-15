import java.io.*;
import java.net.*;
import java.util.*;

public class IcmpServer {
    
    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(12345)) {
            System.out.println("ICMP Server is running...");

            byte[] receiveData = new byte[1024];  // Buffer for receiving packets
            
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                // Extract the packet data
                byte[] packetData = receivePacket.getData();
                int length = receivePacket.getLength();
                
                // Compute checksum and validate
                if (validateChecksum(packetData, length)) {
                    System.out.println("Received valid ICMP packet from " + receivePacket.getAddress());
                    String receivedData = new String(packetData, 8, length - 8);  // Extract user-defined data
                    System.out.println("Data: " + receivedData);
                    
                    // Prepare response with same data
                    byte[] responseData = Arrays.copyOf(packetData, packetData.length);
                    DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, receivePacket.getAddress(), receivePacket.getPort());
                    serverSocket.send(sendPacket);  // Send echo reply back
                    System.out.println("Sent ICMP Echo Reply to " + receivePacket.getAddress());
                } else {
                    System.out.println("Invalid checksum received from " + receivePacket.getAddress());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to compute checksum
    private static boolean validateChecksum(byte[] data, int length) {
        int checksum = 0;
        int i = 0;
        
        while (length > 1) {
            checksum += (data[i] << 8 & 0xFF00) | (data[i + 1] & 0xFF);
            i += 2;
            length -= 2;
        }
        
        if (length == 1) {
            checksum += (data[i] & 0xFF);
        }

        checksum = (checksum >> 16) + (checksum & 0xFFFF);
        checksum += (checksum >> 16);
        
        // One's complement checksum
        return (checksum == 0xFFFF);
    }
}
