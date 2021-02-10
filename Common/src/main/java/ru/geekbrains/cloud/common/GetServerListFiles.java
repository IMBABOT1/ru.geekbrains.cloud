package ru.geekbrains.cloud.common;

import java.util.ArrayList;
import java.util.List;

public class GetServerListFiles extends AbstractMessage  {

    public List<String> getList() {
        return list;
    }


    public void setList(String s) {
        list.add(s);
    }

    private List<String> list;

    public GetServerListFiles(){
        this.list = new ArrayList<>();
    }
}