package com.example.choo.jframe_android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Choo on 2017-06-12.
 */

public class ListViewAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<String> device = new ArrayList<>();
    Context context;

    public ListViewAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return device.size();
    }

    @Override
    public Object getItem(int position) {
        return device.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
//        academy = academy_List.get(position);
        final Context context = parent.getContext();

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.device_item, parent, false);
//            Log.d("choo","type = "+academy_List.get(pos).getType());
//            Log.d("choo","name = "+academy_List.get(pos).getName());

            // TextView에 현재 position의 문자열 추가
            TextView deviceInfo = (TextView) convertView.findViewById(R.id.device_info);
            deviceInfo.setText(device.get(pos));

//            Button academy_detail_button = (Button) convertView.findViewById(R.id.academy_detail_button);
//            academy_detail_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, DetailPageActivity.class);
//                    context.startActivity(intent);
//                }
//            });

        }

        return convertView;
    }

    public void add(String deviceInfo) {
        if(!checkDuple(deviceInfo)){
            device.add(deviceInfo);
        } else{
            Toast.makeText(context, "이미 있습니다.", Toast.LENGTH_LONG);
        }
    }

    public void remove(int position){
        device.remove(position);
    }

    public String getList(int position){
        return device.get(position);
    }

    public void setDeviceList(String data) {
        add(data);
    }

    public boolean checkDuple(String data){
        boolean isDuple = false;
        for(int i=0; i<getCount(); i++){
            if(device.get(i).equals(data)){
                isDuple = true;
            }
        }
        return isDuple;
    }
}
