package ru.geekbrains.cloud.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSend extends AbstractMessage {
    public String getName() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    private String filename;
    private byte[] data;

    public FileSend(Path path){
        try {
            filename = path.getFileName().toString();
            data = Files.readAllBytes(path);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}