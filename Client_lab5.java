import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static List<String> fragmentPacket(String packet, int mtu) {
        int headerSize = 20; // Fixed header size (in bytes)
        int payloadSize = mtu - headerSize; // Max payload size per fragment
        String payload = packet.substring(headerSize); // Extract payload
        List<String> fragments = new ArrayList<>();

        // Divide the payload into chunks
        for (int i = 0; i < payload.length(); i += payloadSize) {
            String fragmentPayload = payload.substring(i, Math.min(i + payloadSize, payload.length()));
            int offset = i / 8; // Fragment offset (in 8-byte units)
            int mfFlag = (i + payloadSize < payload.length()) ? 1 : 0; // More fragments flag
            String fragmentHeader = "Offset=" + offset + ", MF=" + mfFlag; // Simulated header
            fragments.add(fragmentHeader + "|" + fragmentPayload);
        }

        return fragments;
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {
            // Input and output streams for communication
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Simulate an IP packet with header and payload
            String ipPacket = "HEADER20" + "PAYLOAD123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Example payload
            int mtu = 28;  // MTU size (28 bytes total)

            System.out.println("Original Packet: " + ipPacket);
            System.out.println("MTU Size: " + mtu);

            // Fragment the packet
            List<String> fragments = fragmentPacket(ipPacket, mtu);
            System.out.println("\nFragments sent to server:");
            for (int i = 0; i < fragments.size(); i++) {
                System.out.println("Fragment " + (i + 1) + ": " + fragments.get(i));
                out.println(fragments.get(i)); // Send each fragment to the server
            }

            // Indicate the end of transmission by sending an "END" message
            out.println("END");

            // Receive the reassembled packet from the server
            String reassembledPacket = in.readLine();
            System.out.println("\nReassembled Packet from server: " + reassembledPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
