package com.example.choo.jframe_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by Choo on 2017-05-28.
 */

public class GridAdapter extends BaseAdapter {
    private ArrayList<DataFormat> dataList = new ArrayList<DataFormat>();
    LayoutInflater inflater;
    private FrameLayout frameLayout;
    private CheckBox checkBox;
    ArrayList<CheckBox> checkArray = new ArrayList<>();
    boolean isShownCheckbox = false;

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("choo","ingetView");
        final int pos = position;
//        academy = academy_List.get(position);
        final Context context = parent.getContext();

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.image_item, parent, false);
//            Log.d("choo","type = "+academy_List.get(pos).getType());
//            Log.d("choo","name = "+academy_List.get(pos).getName());

//            ImageView image = (ImageView) convertView.findViewById(R.id.imageFromFrame);
            frameLayout = (FrameLayout) convertView.findViewById(R.id.frameLayout);
            checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
//            image.setImageBitmap(MapActivity.publicBitmap);
            byte[] byteImage = Base64.decode(dataList.get(pos).getImageFile(),0);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteImage);
            Bitmap academy_picture = BitmapFactory.decodeStream(inputStream);
            Drawable frameImgae = new BitmapDrawable(context.getResources(), academy_picture);

//            image.setImageBitmap(Bitmap.createScaledBitmap(academy_picture,200,230,true));
            frameLayout.setBackground(frameImgae);

            checkArray.add(checkBox);
        }

        return convertView;
    }

    public void add(DataFormat dataFormat){
        dataList.add(dataFormat);
    }

    public void showCheckBox(boolean isShow){
        int count;
        count = getCount();
        for(int i=0; i<count; i++){
            if(isShow){
                checkArray.get(i).setVisibility(View.VISIBLE);
                checkArray.get(i).invalidate();
                isShownCheckbox = true;
//                Log.d("choo","checkBox visible");
            } else {
                checkArray.get(i).setVisibility(View.GONE);
                checkArray.get(i).invalidate();
                isShownCheckbox = false;
            }
        }
//        if(isShow){
//            checkBox.setVisibility(View.VISIBLE);
//            checkBox.invalidate();
//            Log.d("choo","checkBox visible");
//        } else {
//            checkBox.setVisibility(View.GONE);
//            checkBox.invalidate();
//        }
    }

    public ArrayList<String> checkedImageName(){
        ArrayList<String> checkedImageName = new ArrayList<>();
        for(int i=0; i<getCount(); i++){
            if(checkArray.get(i).isChecked()){
                checkedImageName.add(dataList.get(i).getName());
            }
        }
        return checkedImageName;
    }

    public void removeCheckedImage(){
        for(int i=0; i<getCount(); i++){
            if(checkArray.get(i).isChecked()){
                dataList.remove(i);
            }
        }
    }

    public boolean isShowCheckBox(){
//        if(checkBox.getVisibility() == View.VISIBLE){
//            Log.d("choo","checkbox being visible");
//            return true;
//        }
//        Log.d("choo","checkbox not visible");
        return isShownCheckbox;
    }
}
