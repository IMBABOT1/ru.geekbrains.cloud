package ru.geekbrains.cloud.client;

import ru.geekbrains.cloud.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {


    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    public Client(int port) {
        try {
            socket = new Socket("localhost", port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while(!socket.isOutputShutdown()) {
                {
                    String msg = in.readUTF();
                    System.out.println(msg);


                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        Client client = new Client(8189);
    }
}
