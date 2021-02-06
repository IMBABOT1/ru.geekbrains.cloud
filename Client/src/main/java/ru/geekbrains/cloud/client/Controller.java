package ru.geekbrains.cloud.client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ru.geekbrains.cloud.common.AbstractMessage;
import ru.geekbrains.cloud.common.FileMessage;
import ru.geekbrains.cloud.common.FileRequest;
import ru.geekbrains.cloud.common.FileSend;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;



public class Controller implements Initializable {

    @FXML
    ListView<String> clientsFiles;

    @FXML
    ListView<String> serverFiles;

    @FXML
    Label label1;

    @FXML
    Label cloud;

    @FXML
    Label local;

    @FXML
    Label server;


    private Socket socket;
    private ObjectEncoderOutputStream out;
    private ObjectDecoderInputStream in;

    private void moveCloudLabel(){
        cloud.setMaxWidth(Double.MAX_VALUE);
        cloud.setAlignment(Pos.BASELINE_CENTER);
        local.setPadding(new Insets(5));
    }

    private void moveLocalLabel(){
        local.setMaxWidth(Double.MAX_VALUE);
        local.setAlignment(Pos.BASELINE_LEFT);
        local.setPadding(new Insets(10));
    }

    private void moveServerLabel(){
        server.setMaxWidth(Double.MAX_VALUE);
        server.setAlignment(Pos.BASELINE_RIGHT);
        server.setPadding(new Insets(10));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moveLocalLabel();
        moveCloudLabel();
        moveServerLabel();
        try {
            socket = new Socket("localhost", 8189);
            in = new ObjectDecoderInputStream(socket.getInputStream());
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AbstractMessage message = (AbstractMessage) in.readObject();
                        if (message instanceof FileMessage){
                            FileMessage fm = (FileMessage) message;
                            Files.write(Paths.get("clientStorage/" + fm.getName()), fm.getData(), StandardOpenOption.CREATE);
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }catch (IOException e){
            throw new RuntimeException("Server is down");
        }finally {
           // stop();
        }
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

    public boolean sendMessage(AbstractMessage abstractMessage){
       try {
           out.writeObject(abstractMessage);
           return true;
       }catch (IOException e){
           e.printStackTrace();
       }

       return false;
    }



    public void clientDownload(ActionEvent actionEvent) {
        sendMessage(new FileRequest("12.txt"));
    }

    public void clientUpload(ActionEvent actionEvent) {
        FileSend fileSend = new FileSend(Paths.get("clientStorage/" + "rogueLike.rar"));
        sendMessage(fileSend);
    }


    public void clientRenew(ActionEvent actionEvent) {
    }

    public void clientDelete(ActionEvent actionEvent) {
    }

    public void serverDelete(ActionEvent actionEvent) {
    }

    public void serverRenew(ActionEvent actionEvent) {
    }
}