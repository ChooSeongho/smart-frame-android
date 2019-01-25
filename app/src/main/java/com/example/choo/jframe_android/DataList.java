package com.example.choo.jframe_android;

import java.util.ArrayList;

/**
 * Created by Choo on 2017-05-28.
 */

public class DataList {
    private ArrayList<DataFormat> list = new ArrayList<DataFormat>();

    public void setList(ArrayList<DataFormat> list){
        this.list = list;
    }

    public ArrayList<DataFormat> getList(){
        return list;
    }

    public int arrayCount(){
        return list.size();
    }

    public void addList(DataFormat data){
        list.add(data);
    }
}
