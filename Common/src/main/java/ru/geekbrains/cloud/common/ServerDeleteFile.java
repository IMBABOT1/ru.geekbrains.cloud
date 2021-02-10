package ru.geekbrains.cloud.common;

public class ServerDeleteFile extends AbstractMessage{

    public String getFilename() {
        return filename;
    }

    private String filename;

    public ServerDeleteFile(String filename){
        this.filename = filename;
    }

}
