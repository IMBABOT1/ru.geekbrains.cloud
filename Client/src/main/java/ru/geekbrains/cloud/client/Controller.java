package ru.geekbrains.cloud.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    ListView<String> clientsFiles;

    @FXML
    ListView<String> serverFiles;

    @FXML
    Label label1;



    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            clientsFiles.getItems().addAll("File", "File1", "File2");
            serverFiles.getItems().addAll("File", "File1", "File2");
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            throw new RuntimeException("Server is down");
        }
    }



    public void btnClickSelectedClientFile(ActionEvent actionEvent) {
        label1.setText(clientsFiles.getSelectionModel().getSelectedItem());

    }

    public void btnClickSelectedServerFile(ActionEvent actionEvent) {
        label1.setText(serverFiles.getSelectionModel().getSelectedItem());
    }
}