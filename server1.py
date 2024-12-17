import socket

server = socket.socket()  # Create a server socket
server.bind(('127.0.0.1', 8000))  # Use localhost and port
server.listen(1)  # Wait for one connection

print("Server is ready...")

while 1: 
    conn, addr = server.accept()  # Accept client
    print(f"Connected to {addr}")
    file_name = conn.recv(1024).decode()  # Get file name
    try:
        with open(file_name, 'r') as file:
            conn.send(file.read().encode())  # Send file content
    except FileNotFoundError:
        conn.send(b"File not found")  # Send error message

    conn.close()  # Close connection
