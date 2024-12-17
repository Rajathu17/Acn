import socket

server = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
server.bind(('127.0.0.1', 65432))

while True:
    data, address = server.recvfrom(1024) 
    if data:
        print(f"Received from {address}: {data.decode()}")
      #  response = data.decode().lower()  
        response = data.decode().upper()  # Decode the data before processing
        server.sendto(response.encode(), address)  # Send back the uppercase data
