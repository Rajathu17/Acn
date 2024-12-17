import socket

def crc_ccitt(data: bytes, poly=0x1021, init_crc=0xFFFF):
    crc = init_crc
    for byte in data:
        crc ^= (byte << 8)
        for _ in range(8):
            crc = (crc << 1) ^ poly if crc & 0x8000 else crc << 1
            crc &= 0xFFFF  # Ensure CRC stays within 16 bits
    return crc

def server():
    host, port = "127.0.0.1", 65432
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((host, port))
    server_socket.listen(1)
    print(f"Server listening on {host}:{port}...")

    conn, addr = server_socket.accept()
    print(f"Connected by {addr}")

    # Get user input for the message
    message = input("Enter a message to send: ").encode()
    crc = crc_ccitt(message)
    conn.send(message + crc.to_bytes(2, "big"))
    binary = bin(crc)
    print(f"Sent: {message} with CRC: {binary}")

    conn.close()
    server_socket.close()

if  __name__ == "__main__":
    server()
