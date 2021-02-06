package ru.geekbrains.cloud.client;

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

    public Network network;


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
            if (network != null && network.isConnected()) {
                return;
            }
            network = new Network(8189);
            moveLocalLabel();
            moveCloudLabel();
            moveServerLabel();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AbstractMessage message = (AbstractMessage) network.getIn().readObject();
                        if (message instanceof FileMessage) {
                            FileMessage fm = (FileMessage) message;
                            Files.write(Paths.get("clientStorage/" + fm.getName()), fm.getData(), StandardOpenOption.CREATE);
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        network.stop();
                    }
                }
            }).start();
        }





    public void clientDownload(ActionEvent actionEvent) {
        network.sendMessage(new FileRequest("12.txt"));
    }

    public void clientUpload(ActionEvent actionEvent) {
        FileSend fileSend = new FileSend(Paths.get("clientStorage/" + "rogueLike.rar"));
        network.sendMessage(fileSend);
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