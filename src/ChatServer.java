import java.net.*;
import java.io.*;

public class ChatServer {
    private ServerSocket server;

    public ChatServer(ServerSocket server) {
        this.server = server;
    }

    public void serverStart() {
        try {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                System.out.println("Новое подключение");
                ServerClientHandler serverClientHandler = new ServerClientHandler(socket);
                Thread thread = new Thread(serverClientHandler);
                thread.start();
            }
        }
        catch (IOException e) {
        }
    }

    public void closerServer() {
        try {
            if (server != null) {
                server.close();
            }
        }
        catch (IOException e) {
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);
        ChatServer chatServer = new ChatServer(serverSocket);
        chatServer.serverStart();
    }
}