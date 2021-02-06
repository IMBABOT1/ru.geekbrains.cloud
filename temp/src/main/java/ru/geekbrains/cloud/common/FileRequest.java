package ru.geekbrains.cloud.common;

public class FileRequest extends AbstractMessage {

    public String getFilename() {
        return filename;
    }

    private String filename;

    public FileRequest(String filename){
        this.filename = filename;
    }
}