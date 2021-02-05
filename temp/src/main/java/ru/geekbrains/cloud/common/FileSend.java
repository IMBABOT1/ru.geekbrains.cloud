package ru.geekbrains.cloud.common;

public class FileSend extends AbstractMessage {

    public String getFilename() {
        return filename;
    }

    private String filename;

    public FileSend(String filename){
        this.filename = filename;
    }
}
