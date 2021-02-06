package ru.geekbrains.cloud.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
public class FileMessage extends AbstractMessage {

    public String getName() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    private String filename;
    private byte[] data;

    public FileMessage(Path paths){
        filename = paths.getFileName().toString();
        System.out.println(filename);
        try {
            data = Files.readAllBytes(paths);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}