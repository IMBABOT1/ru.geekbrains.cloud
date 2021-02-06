package ru.geekbrains.cloud.common;

import java.util.ArrayList;
import java.util.List;

public class GetServerListFiles extends AbstractMessage  {

    public String getServerList() {
        return getServerList;
    }

    public List<String> getList() {
        return list;
    }


    public void setList(String s) {
        list.add(s);
    }

    private List<String> list;
    private String getServerList;

    public GetServerListFiles(String getServerList){
        this.getServerList = getServerList;
        this.list = new ArrayList<>();
    }
}