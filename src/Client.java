import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;

    public Client(Socket socket, String name) {
        try {
            this.socket = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = name;
        }
        catch (IOException e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    public void sendMessageClient() {
        try {
            buffWriter.write(name);
            buffWriter.newLine();
            buffWriter.flush();

            Scanner sc = new Scanner(System.in);

            while (socket.isConnected()) {
                String messageToSend = sc.nextLine();

                if (messageToSend.equals("Выйти")) {
                    System.out.println("Соединение закрыто");
                    closeAll(socket, buffReader, buffWriter);
                    break;
                }

                buffWriter.write(name + ": " + messageToSend);
                buffWriter.newLine();
                buffWriter.flush();
            }
        }
        catch (IOException e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    public void readMessage() {
        new Thread( new Runnable() {
            @Override
            public void run() {
                String msfFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        msfFromGroupChat = buffReader.readLine();
                        System.out.println(msfFromGroupChat);
                    }
                    catch (IOException e) {
                        closeAll(socket, buffReader, buffWriter);
                    }
                }
            }
        }).start();
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
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

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите ваш ник в чате");
        String name = sc.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, name);
        client.readMessage();
        client.sendMessageClient();
    }

}