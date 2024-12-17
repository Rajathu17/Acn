import socket

def ip_checksum(data: bytes):
    checksum = 0
    for i in range(0, len(data), 2):
        word = (data[i] << 8) + (data[i + 1] if i + 1 < len(data) else 0)
        checksum += word
        checksum = (checksum & 0xFFFF) + (checksum >> 16)  # Handle overflow
    return ~checksum & 0xFFFF  # Take 1's complement

s = socket.socket()
s.connect(('localhost', 9999))

message = b"Helddddddddddlo, Server!"
checksum = hex(ip_checksum(message))
s.send(message + b'|' + checksum.encode())
print("Message sent:", message)
print("Checksum sent:", checksum)

s.close()
