package ru.geekbrains.cloud.client;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ru.geekbrains.cloud.common.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;


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

    private Path clientStorage = Paths.get("clientStorage/");
    private Path serverStorage = Paths.get("serverStorage/");


    private void moveCloudLabel() {
        cloud.setMaxWidth(Double.MAX_VALUE);
        cloud.setAlignment(Pos.BASELINE_CENTER);
        local.setPadding(new Insets(5));
    }

    private void moveLocalLabel() {
        local.setMaxWidth(Double.MAX_VALUE);
        local.setAlignment(Pos.BASELINE_LEFT);
        local.setPadding(new Insets(10));
    }

    private void moveServerLabel() {
        server.setMaxWidth(Double.MAX_VALUE);
        server.setAlignment(Pos.BASELINE_RIGHT);
        server.setPadding(new Insets(10));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moveLocalLabel();
        moveCloudLabel();
        moveServerLabel();
        ObservableList<String> clients = observableArrayList();
        ObservableList<String> server = observableArrayList();
        start();
    }


    public void start() {
        if (network != null && network.isConnected()) {
            return;
        }
        network = new Network(8189);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            AbstractMessage message = (AbstractMessage) network.getIn().readObject();
                            System.out.println(message.toString());
                            if (message instanceof FileMessage) {
                                FileMessage fm = (FileMessage) message;
                                Files.write(Paths.get("clientStorage/" + fm.getName()), fm.getData(), StandardOpenOption.CREATE);
                            }
                            if (message instanceof GetServerListFiles) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        serverFiles.getItems().clear();
                                        GetServerListFiles list = (GetServerListFiles) message;
                                        try {
                                            Files.list(serverStorage).map(path -> path.getFileName().toString()).forEach(o -> serverFiles.getItems().add(o));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

    }


    public void refreshLocalList() {
        updateUI(new Runnable() {
            @Override
            public void run() {
                try {
                    clientsFiles.getItems().clear();
                    Files.list(clientStorage).map(path -> path.getFileName().toString()).forEach(o -> clientsFiles.getItems().add(o));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void refreshServerList() {
    }


    public void updateUI(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }


    public void clientDownload(ActionEvent actionEvent) {
        Network.sendMessage(new FileRequest(serverFiles.getSelectionModel().getSelectedItem()));
    }

    public void clientUpload(ActionEvent actionEvent) {
        FileSend fileSend = new FileSend(Paths.get("clientStorage/" + clientsFiles.getSelectionModel().getSelectedItem()));
        Network.sendMessage(fileSend);
    }

    public void serverRenew(ActionEvent actionEvent) {
        GetServerListFiles get = new GetServerListFiles();
        Network.sendMessage(get);
    }


    public void clientRenew(ActionEvent actionEvent) {
        refreshLocalList();
    }

    public void clientDelete(ActionEvent actionEvent) {
        try {
            Files.delete(Paths.get("clientStorage/" + clientsFiles.getSelectionModel().getSelectedItem()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void serverDelete(ActionEvent actionEvent) {
        ServerDeleteFile serverDeleteFile = new ServerDeleteFile(serverFiles.getSelectionModel().getSelectedItem());
        Network.sendMessage(serverDeleteFile);
        System.out.println(1);
    }
}