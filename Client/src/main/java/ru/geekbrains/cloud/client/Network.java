package ru.geekbrains.cloud.client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.geekbrains.cloud.common.AbstractMessage;

import java.io.IOException;
import java.net.Socket;

public class Network {

    private static Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public static ObjectEncoderOutputStream getOut() {
        return out;
    }

    public static ObjectDecoderInputStream getIn() {
        return in;
    }

    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;



    public Network(int port){
        try {
            socket = new Socket("localhost", port);
            in = new ObjectDecoderInputStream(socket.getInputStream(), 1024 * 1024 * 100);
            out = new ObjectEncoderOutputStream(socket.getOutputStream(), 1024 * 1024 * 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean sendMessage(AbstractMessage abstractMessage){
        try {
            out.writeObject(abstractMessage);
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }


    public static boolean isConnected(){
        if (socket == null || socket.isClosed()){
            return false;
        }
        return true;
    }


    public static void stop(){
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}