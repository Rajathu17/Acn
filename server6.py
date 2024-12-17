import socket
import struct

def ip_checksum(data: bytes):
    checksum = 0
    for i in range(0, len(data), 2):
        word = (data[i] << 8) + (data[i + 1] if i + 1 < len(data) else 0)
        checksum += word
        checksum = (checksum & 0xFFFF) + (checksum >> 16)  # Handle overflow
    return ~checksum & 0xFFFF  # Take 1's complement

def server():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_socket.bind(("0.0.0.0", 9999))
    print("UDP Server listening on port 9999...")

    while True:
        data, addr = server_socket.recvfrom(1024)
        print(f"Received message from {addr}: {data.decode()}")

        # Calculate checksum of the received data
        checksum = ip_checksum(data)
        print(f"Calculated checksum (hex): {hex(checksum)}")

        # Append checksum to the response
        msg_with_checksum = data + b'|' + struct.pack('!H', checksum)

        # Echo back the message with the checksum
        server_socket.sendto(msg_with_checksum, addr)
        print(f"Echoed message back to {addr} with checksum: {hex(checksum)}")

if __name__ == "__main__":
    server()
