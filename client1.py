import socket

client = socket.socket()
client.connect(('127.0.0.1', 8000))

file_name = input("Enter file name: ")
client.send(file_name.encode())

response = client.recv(1024).decode()
print("Server response:", response)

client.close()
