package ru.geekbrains.cloud.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {


    public Server(int port)  {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server Start");
            Socket socket = serverSocket.accept();
            System.out.println("Client Connect");
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            while (true) {
                String msg = in.readUTF();
                System.out.println("Client msg: " + msg);
                out.writeUTF("Echo: " + msg);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }



    public static void main(String[] args) {
        Server server = new Server(8189);
    }
}
