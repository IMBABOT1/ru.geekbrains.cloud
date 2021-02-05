package ru.geekbrains.cloud.client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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


    private Socket socket;
    private ObjectEncoderOutputStream out;
    private ObjectDecoderInputStream in;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8189);
            in = new ObjectDecoderInputStream(socket.getInputStream());
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AbstractMessage message = (AbstractMessage) in.readObject();
                        if (message instanceof AbstractMessage){
                            FileMessage fm = (FileMessage) message;
                            System.out.println(fm);
                            Files.write(Paths.get("clientStorage/" + fm.getName()), fm.getData(), StandardOpenOption.CREATE);
                        }
                        if (message instanceof FileSend){
                            FileSend fs = (FileSend) message;
                            System.out.println(fs);
                            System.out.println(2);
                            if (Files.exists(Paths.get("ClientStorage/" + fs.getFilename()))) {
                                System.out.println(3);
                                FileMessage fm = new FileMessage(Paths.get("ClientStorage/" + fs.getFilename()));
                                System.out.println(fm.getName());
                                out.writeObject(fm);
                                System.out.println(4);
                            }

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



    public void download(ActionEvent actionEvent) {
        sendMessage(new FileRequest("12.txt"));
    }

    public void upload(ActionEvent actionEvent) {
        sendMessage(new FileSend("321.txt"));
        System.out.println(1);
    }
}