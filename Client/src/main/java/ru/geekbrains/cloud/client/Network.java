package ru.geekbrains.cloud.client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.geekbrains.cloud.common.AbstractMessage;

import java.io.IOException;
import java.net.Socket;

public class Network {

    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public ObjectEncoderOutputStream getOut() {
        return out;
    }

    public ObjectDecoderInputStream getIn() {
        return in;
    }

    private ObjectEncoderOutputStream out;
    private ObjectDecoderInputStream in;


    public Network(int port){
        try {
            socket = new Socket("localhost", port);
            in = new ObjectDecoderInputStream(socket.getInputStream());
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean sendMessage(AbstractMessage abstractMessage){
        try {
            out.writeObject(abstractMessage);
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }


    public boolean isConnected(){
        if (socket == null || socket.isClosed()){
            return false;
        }
        return true;
    }


    public void stop(){
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
