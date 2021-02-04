package ru.geekbrains.cloud.server;

import ru.geekbrains.cloud.client.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {


    private Socket socket;
    private ExecutorService executorService;
    private DataInputStream in;
    private DataOutputStream out;


    public Server(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server Start");
        socket = serverSocket.accept();
        System.out.println("Client Connect");
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        while (!socket.isClosed()){
            String msg = in.readUTF();
            System.out.println("Client msg: " + msg);
            out.writeUTF("Echo: " + msg);
        }
    }



    public static void main(String[] args) {
        try {
            Server server = new Server(8189);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
