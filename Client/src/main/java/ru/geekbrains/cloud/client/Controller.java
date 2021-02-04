package ru.geekbrains.cloud.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    TextArea textArea;

    @FXML
    TextField textField;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        String msg = null;
                        try {
                            msg = in.readUTF();
                            System.out.println("Server: " + msg + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }catch (IOException e){
            throw new RuntimeException("Server is down");
        }
    }


    public void sendMsg(ActionEvent actionEvent) {
        try {
            out.writeUTF(textField.getText()+ "\n");
            textField.setText("");
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}