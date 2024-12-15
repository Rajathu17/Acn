import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static String reassemblePacket(List<String> fragments) {
        StringBuilder packet = new StringBuilder();
        for (String fragment : fragments) {
            // Extract the fragment payload and append it to the packet
            String payload = fragment.split("\\|")[1]; // Get the part after the header
            packet.append(payload);
        }
        return packet.toString(); // Return the reassembled packet
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running and waiting for client connection...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            List<String> fragments = new ArrayList<>();
            String fragment;
            while ((fragment = in.readLine()) != null && !fragment.equals("END")) {
                fragments.add(fragment); // Collect all fragments
            }

            // Reassemble the packet from the fragments
            String reassembledPacket = reassemblePacket(fragments);
            System.out.println("Reassembled Packet: " + reassembledPacket);

            // Send the reassembled packet back to the client
            out.println(reassembledPacket);

            socket.close();  // Close client connection
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
