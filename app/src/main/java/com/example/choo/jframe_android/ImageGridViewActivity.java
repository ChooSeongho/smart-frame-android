package com.example.choo.jframe_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.Inflater;

import gun0912.tedbottompicker.TedBottomPicker;

public class ImageGridViewActivity extends AppCompatActivity {

    GridView gridView;
    Handler handler;
    DataList frameImages;
    public static final int GALLERY = 401;
    ArrayList<Uri> selectedUriList;
    ConnectServer connectServer;
//    boolean isRemove = false;
    Menu menu;
    String BLUMAC;
    GridAdapter testImageAdaper;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
//        if(!isRemove){
//            menu.removeGroup(R.id.menu_group);
//            menu.removeGroup(R.id.remove_menu_group);
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.menu, menu);
//            Log.d("choo","create optionMenu");
//        } else{
//            menu.removeGroup(R.id.menu_group);
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.remove_menu, menu);
//            Log.d("choo","create removeMenu");
//        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuInflater inflater = getMenuInflater();
        switch (item.getItemId()){
            case R.id.image_add:
                onAdd();
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT); intent.setType("image/*");
//                startActivityForResult( Intent.createChooser(intent, "Select Picture"), GALLERY);
                return true;
            case R.id.image_remove:
                connectServer.showCheckBox(true);

                menu.removeGroup(R.id.menu_group);
                inflater.inflate(R.menu.remove_menu, menu);
                return true;
            case R.id.remove_done:
                connectServer.showCheckBox(false);

                menu.removeGroup(R.id.remove_menu_group);
                inflater.inflate(R.menu.menu, menu);

                connectServer.sendCheckedImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onAdd() {
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(this).setOnMultiImageSelectedListener(
                new TedBottomPicker.OnMultiImageSelectedListener() {
            @Override
            public void onImagesSelected(ArrayList<Uri> uriList) {
                try {
                    selectedUriList = uriList;
                    sendAddImageFile(uriList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .create();

        tedBottomPicker.show(getSupportFragmentManager());

    }

    private void sendAddImageFile(ArrayList<Uri> uriList) throws IOException {
        JSON json = new JSON();
        for(Uri uri : uriList){
            Base64EnDe en = new Base64EnDe();
            File file = new File(uri.getPath());
            String[] split = uri.toString().split("/");
            Log.d("choo","uri name = "+split[split.length-1]);
//            json.addJSONArray("And", BLUMAC, "add", en.fileToString(file));
            json.addJSONArray("And", BLUMAC, split[split.length-1], en.fileToString(file));
//            json.addJSONArray("And", BLUMAC, "add", en.getFileToByte(uri.getPath()));
            Log.d("choo","sendAddFile");
//            DataFormat dataFormat = new DataFormat();
//            dataFormat.setType("And");
//            dataFormat.setName("add");
//            dataFormat.setimageFile(en.fileToString(file));
//            dataFormat.setBLUMAC(BLUMAC);
//            testImageAdaper.add(dataFormat);
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    gridView.setAdapter(testImageAdaper);
//                }
//            });

//            json.addJSONArray("And", BLUMAC, "add", en.bytesToString(en.uriToBytes(this, uri)));
//            DataFormat aa = new DataFormat();
//            aa.setBLUMAC(BLUMAC);
//            aa.setimageFile(en.bytesToString(en.uriToBytes(this, uri)));
//            aa.setName("add");
//            aa.setType("And");
//            connectServer.gridAdapter.add(aa);
//            connectServer.gridAdapter.notifyDataSetChanged();
        }
        connectServer.send(json.getJSONArray().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        gridView = (GridView) findViewById(R.id.gridview);
        handler = new Handler();
        frameImages = new DataList();
        Intent intent = getIntent();
        BLUMAC = intent.getStringExtra("address");
        Log.d("choo","BLUMAC = "+BLUMAC);

        connectServer = new ConnectServer(this, BLUMAC);
        connectServer.start();

    }

    public void setGridView(GridAdapter gridAdapter){
        final GridAdapter adapter = gridAdapter;
        testImageAdaper = gridAdapter;
        handler.post(new Runnable() {
            @Override
            public void run() {
                gridView.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY:
                if (resultCode == RESULT_CANCELED)
                    return;
                if (data != null) {
                    Uri imgUri = data.getData();
                    Log.d("choo", "image = "+data.getDataString());
                }
                break;
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        connectServer.stopConnect();
//        Log.d("choo","request thread stop");
//    }

    @Override
    public void onBackPressed() {
        if(connectServer.isShowCheckBox()){
            Log.d("choo","shownCheckBox");
            MenuInflater inflater = getMenuInflater();
            connectServer.showCheckBox(false);

            menu.removeGroup(R.id.remove_menu_group);
            inflater.inflate(R.menu.menu, menu);
        } else {
            connectServer.stopConnect();
            super.onBackPressed();
        }
    }
}
