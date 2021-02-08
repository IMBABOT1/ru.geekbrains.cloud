package ru.geekbrains.cloud.client;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    Label cloud;

    @FXML
    Label local;

    @FXML
    Label server;

    @FXML
    PasswordField passwordField;

    @FXML
    HBox loginBox;

    @FXML
    Button sendFile;

    @FXML
    Button deleteFromClient;

    @FXML
    Button clientRenew;

    @FXML
    Button downloadFile;

    @FXML
    Button deleteFromServer;

    @FXML
    Button serverRenew;

    @FXML
    Button login;

    @FXML
    TextField loginField;


    private Network network;
    private boolean authenticated;

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        loginBox.setVisible(!authenticated);
        loginBox.setManaged(!authenticated);
        passwordField.setVisible(!authenticated);
        passwordField.setVisible(!authenticated);
        cloud.setVisible(authenticated);
        local.setVisible(authenticated);
        server.setManaged(authenticated);
        clientsFiles.setVisible(authenticated);
        serverFiles.setVisible(authenticated);
        deleteFromClient.setVisible(authenticated);
        clientRenew.setVisible(authenticated);
        downloadFile.setVisible(authenticated);
        deleteFromServer.setVisible(authenticated);
        server.setVisible(authenticated);
        sendFile.setVisible(authenticated);
        serverRenew.setVisible(authenticated);

    }

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
        setAuthenticated(false);
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
                        if (Network.getIn().readObject() instanceof String) {
                            String username = (String) network.getIn().readObject();
                            if (username.equals("")) {
                                setAuthenticated(false);
                                continue;
                            } else if (username.startsWith("username")) {
                                setAuthenticated(true);
                            }
                        }else {
                            while (true) {
                                AbstractMessage message = (AbstractMessage) network.getIn().readObject();
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
                        }
                    }
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverDelete(ActionEvent actionEvent) {
        ServerDeleteFile serverDeleteFile = new ServerDeleteFile(serverFiles.getSelectionModel().getSelectedItem());
        Network.sendMessage(serverDeleteFile);
    }

    public void sendLoginPass(String login, String pass) {
        TryToAuth tryToAuth = new TryToAuth(login, pass);
        Network.sendMessage(tryToAuth);
    }

    public void tryToAuth() {
        sendLoginPass(loginField.getText(), passwordField.getText());
        loginField.clear();
        passwordField.clear();
    }
}
