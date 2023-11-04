import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ServerClientHandler implements Runnable {
    public static ArrayList<ServerClientHandler> serverClientHandlers = new ArrayList<>();
    public Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;

    public ServerClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.buffWriter = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = buffReader.readLine();
            serverClientHandlers.add(this);
        }
        catch (IOException e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = buffReader.readLine();
                sendMessage(messageFromClient);
            }
            catch (IOException e) {
                closeAll(socket, buffReader,  buffWriter);
                break;
            }
        }
    }

    public void sendMessage(String messageToSend) {
        for (ServerClientHandler serverClientHandler : serverClientHandlers) {
            try {
                if (!serverClientHandler.name.equals(name)) {
                    serverClientHandler.buffWriter.write(messageToSend);
                    serverClientHandler.buffWriter.newLine();
                    serverClientHandler.buffWriter.flush();
                }
            }
            catch(IOException e) {
                closeAll(socket,buffReader, buffWriter);
            }
        }
    }

    public void removeClientHandler() {
        serverClientHandlers.remove(this);
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
        removeClientHandler();

        try {
            if (buffReader!= null) {
                buffReader.close();
            }
            if (buffWriter != null) {
                buffWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
        catch (IOException e) {
        }
    }

}