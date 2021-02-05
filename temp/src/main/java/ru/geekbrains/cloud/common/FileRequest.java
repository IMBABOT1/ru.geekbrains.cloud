package ru.geekbrains.cloud.common;

public class FileRequest extends AbstractMessage {

    public String getFilename() {
        return filename;
    }

    private String filename;

    public FileRequest(String filename){
        System.out.println(1);
        this.filename = filename;
    }
}
