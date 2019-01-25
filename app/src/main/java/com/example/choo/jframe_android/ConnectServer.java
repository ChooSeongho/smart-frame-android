package com.example.choo.jframe_android;

import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.GridView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Choo on 2017-05-28.
 */

public class ConnectServer extends Thread {

    private String server = "192.168.219.117";
//    private String server = "192.168.43.181";
    private int port = 2017;
    private Socket socket;
    private Scanner scanner;
    private PrintWriter output;
    BufferedReader br = null;
    private String data = new String();
    boolean done = true;
        //    public static ListViewAcademy list = new ListViewAcademy();
    private DataList dataList;
    ImageGridViewActivity imageGridViewActivity;
    JSON json = new JSON();
    GridAdapter gridAdapter;
    String BLUMAC;

    public ConnectServer(ImageGridViewActivity imageGridViewActivity, String macAddress) {
//        try {
//
//            this.mainActivity = mainActivity;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        this.imageGridViewActivity = imageGridViewActivity;
        BLUMAC = macAddress;
    }

    @Override
    public void run() {

        try {
            socket = new Socket(server, port);
            OutputStream outStream = socket.getOutputStream();
            output = new PrintWriter(new OutputStreamWriter(outStream), true);
//            scanner = new Scanner(new InputStreamReader(socket.getInputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataList = new DataList();
        while(done){
//            Log.e("choo","threadRunning");
//            dataList = new DataList();
//            data = scanner.nextLine();
            try {
                data = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(data != null && data.trim().equals("Connect")){
                String str = json.makeJSONArray(1,"And",BLUMAC,"get","null").toString();
//                String str = "get";
                send(str);
                Log.d("choo", "send get message");
            } else if(data != null){
                //                    Test();
//                    data = new Gson().toJson(list,ListViewAcademy.class);
                Log.d("choo", "data = " + data.toString());
//                    Log.d("choo", "before  "+list.getAcademies()+"");
//                    list = new Gson().fromJson(data, ListViewAcademy.class);
//                int k = 0;
//                while(k < data.length()){
//                    if(k+100 > data.length()){
//                        Log.d("choo", "redata  "+data.substring(k, data.length()));
//                        break;
//                    } else {
//                        Log.d("choo", "redata  "+data.substring(k, k+100));
//                        k+=100;
//                    }
//                }
                dataList = new Gson().fromJson(data, DataList.class);
                gridAdapter = new GridAdapter();

                for(int i = 0; i < dataList.arrayCount(); i++){
                    Log.d("choo","array count = "+dataList.arrayCount());
                    Log.d("choo","name = "+dataList.getList().get(0).getName());
                    gridAdapter.add(dataList.getList().get(i));
                    Log.d("choo", dataList.getList().get(i).getBLUMAC());
                    Log.d("choo", dataList.getList().get(i).getName());
                    if(i == 5){
                        for (int j = 0; j < dataList.getList().get(i).getImageFile().length(); j += 200) {
                            if (j + 200 >=  dataList.getList().get(i).getImageFile().length()) {
//				System.out.println(fileString.substring(j, fileString.length()));
                                Log.d("choo", "last encoded = " + dataList.getList().get(i).getImageFile().substring(j, dataList.getList().get(i).getImageFile().length()));
                            } else {
//				System.out.println(fileString.substring(j, j+199));
                                Log.d("choo", "en = " + dataList.getList().get(i).getImageFile().substring(j, j + 199));
                            }
                        }
                    }
//                    Log.d("choo", dataList.getList().get(i).getImageFile());
//                    if(dataList.getList().get(i).getName().trim().equals("list")){
//                        Log.d("choo","mark up 1");
//                        gridAdapter.add(dataList.getList().get(i));
//                        Log.d("choo","mark up 2");
//                        Log.d("choo", dataList.getList().get(i).getBLUMAC());
//                        Log.d("choo", dataList.getList().get(i).getName());
//                        Log.d("choo", dataList.getList().get(i).getImageFile());
//                        Log.d("choo","mark up 3");
//                    }
                }
                imageGridViewActivity.setGridView(gridAdapter);
//                GridView gridView = (GridView) mainActivity.findViewById(R.id.gridview);
//                gridView.setAdapter(gridAdapter);

//                    Log.d("choo", list.getAcademies()+"");
//            imageGridViewActivity.isRun = false;
            }
        }
    }

    public void send(String message){
        final String msg = message;
        new Thread(new Runnable() {
            @Override
            public void run() {
                output.println(msg);
                output.flush();
                Log.d("choo", "send = " + msg);
            }
        }).start();
    }

    public void showCheckBox(boolean isShow){
        gridAdapter.showCheckBox(isShow);
        gridAdapter.notifyDataSetChanged();
    }

    public void sendCheckedImage(){
        JSON checkedImageJSON = new JSON();
        ArrayList<String> checkedImageName = gridAdapter.checkedImageName();
        for(String str : checkedImageName){
            checkedImageJSON.addJSONArray("And",BLUMAC,str,"remove");
        }
        send(checkedImageJSON.getJSONArray().toString());
        gridAdapter.removeCheckedImage();
        gridAdapter.notifyDataSetChanged();
    }

    public boolean isShowCheckBox(){
        boolean a = gridAdapter.isShowCheckBox();
        Log.d("choo","isshowcheckbox = "+a);
        return a;
    }

    public DataList getDataList() {
        return dataList;
    }

    public void stopConnect(){
        if(socket != null){
            done = false;
            try {
                send("close");
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        if(br!=null && output!=null){
//            try {
//                done = false;
//                br.close();
//                output.close();
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
