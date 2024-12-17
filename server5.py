import socket
import pickle

class IPPacket:
    def __init__(self, id, offset, more_fragments, data):
        self.id, self.offset, self.more_fragments, self.data = id, offset, more_fragments, data

def main():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_socket.bind(('0.0.0.0', 8080))  # Server listens on all interfaces, port 8080
    print("Server is listening on port 8080...")

    received_fragments = {}
    while True:
        try:
            fragment = pickle.loads(server_socket.recvfrom(4096)[0])
            received_fragments.setdefault(fragment.id, []).append(fragment)
            
            # Only process packet when all fragments are received
            if fragment.more_fragments == False:
                print(f"Received complete packet ID {fragment.id}")
                received_fragments[fragment.id].clear()  # Clear after processing

        except Exception as e:
            print(f"Error: {e}")  # Handle any errors that may occur

main()  # Directly call the main function
