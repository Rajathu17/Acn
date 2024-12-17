import socket

# Create a UDP client socket
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# Get user input and send to server
message = input("Enter message: ")
client.sendto(message.encode(), ('127.0.0.1', 65432))  # Ensure server address is supplied

data, _ = client.recvfrom(1024)
print(f"Server response: {data.decode()}")

client.close()  # Close the socket properly
