import socket
import struct

def ip_checksum(data: bytes):
    checksum = 0
    for i in range(0, len(data), 2):
        word = (data[i] << 8) + (data[i + 1] if i + 1 < len(data) else 0)
        checksum += word
        checksum = (checksum & 0xFFFF) + (checksum >> 16)  # Handle overflow
    return ~checksum & 0xFFFF  # Take 1's complement

def client():
    message = b"Hello"

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_address = ("127.0.0.1", 9999)

    # Calculate checksum
    message_checksum = ip_checksum(message)
    print(f"Sending message to 127.0.0.1... CS: {hex(message_checksum)}")

    # Send message with checksum
    client_socket.sendto(message, server_address)

    # Wait for a reply
    client_socket.settimeout(5)
    try:
        data, addr = client_socket.recvfrom(1024)
        # Separate message and checksum from the server reply
        msg, received_checksum = data.rsplit(b"|", 1)
        
        # Convert the received checksum to integer
        received_checksum = struct.unpack('!H', received_checksum)[0]
        
        print(f"Received reply from {addr}: {msg.decode()}")
        print(f"Server checksum (hex): {hex(received_checksum)}")

        # Validate checksum
        calculated_checksum = ip_checksum(msg)
        if received_checksum != calculated_checksum:
            print("Checksum mismatch. Data may be corrupted.")
        else:
            print("Checksum is valid. Data integrity verified.")
    except socket.timeout:
        print("Request timed out.")
    finally:
        client_socket.close()

if __name__ == "__main__":
    client()
