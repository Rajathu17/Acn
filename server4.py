import socket

def ip_checksum(data: bytes):
    checksum = 0
    for i in range(0, len(data), 2):
        word = (data[i] << 8) + (data[i + 1] if i + 1 < len(data) else 0)
        checksum += word
        checksum = (checksum & 0xFFFF) + (checksum >> 16)  # Handle overflow
    return ~checksum & 0xFFFF  # Take 1's complement

s = socket.socket()
s.bind(('', 9999))
s.listen(1)
x, _ = s.accept()
m = x.recv(1024)
msg, cs = m.rsplit(b'|', 1)
print("Received:", msg)
print("Received checksum:", cs.decode())
print("Verification:", 'OK' if hex(ip_checksum(msg)) == cs.decode() else 'Error')
x.close()
s.close()
