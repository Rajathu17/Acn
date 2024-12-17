import socket

def crc_ccitt(data: bytes, poly=0x1021, init_crc=0xFFFF):
    crc = init_crc
    for byte in data:
        crc ^= (byte << 8)
        for _ in range(8):
            crc = (crc << 1) ^ poly if crc & 0x8000 else crc << 1
            crc &= 0xFFFF  # Ensure CRC stays within 16 bits
    return crc

def client():
    host, port = "127.0.0.1", 65432
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect((host, port))
    print("Connected to server.")

    # Receive message and CRC
    data = client_socket.recv(1024)
    message, received_crc = data[:-2], int.from_bytes(data[-2:], "big")
    print(f"Received message: {message.decode()}")
    binary = bin(received_crc)
    print(f"Received CRC: {binary}")

    # Validate CRC
    calculated_crc = crc_ccitt(message)
    if calculated_crc == received_crc:
        print("CRC check passed. Data integrity verified.")
    else:
        print("CRC check failed. Data is corrupted.")

    client_socket.close()

if __name__ == "__main__":
    client()
