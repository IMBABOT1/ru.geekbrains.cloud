package ru.geekbrains.cloud.common;

public class CloseConnection extends  AbstractMessage{

    public String getUsername() {
        return username;
    }

    private String username;


    public CloseConnection(String username){
        this.username = username;
    }


}
